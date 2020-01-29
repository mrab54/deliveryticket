package com.bellin.erp.supplychain.deliveryticket.report;

import com.bellin.erp.supplychain.deliveryticket.domain.file.ReqFile;
import com.bellin.erp.supplychain.deliveryticket.domain.file.ReqFileLine;
import com.bellin.erp.supplychain.deliveryticket.domain.file.ReqFileLineField;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.runtime.RuntimeConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@SuppressWarnings("ALL")
public class ReportWriter {
    private static final Logger logger = LoggerFactory.getLogger(ReportWriter.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final String PADLEFT = "left";
    static final String PADRIGHT = "right";
    static final int pageWidth = 130;
    static final int pageHeight = 58;
    private static final int reqLinesPerPage = 10;
    final static int pageNumberWidth = 10;
    final static int timestampWidth = 19;
    private static final Charset ENCODING = StandardCharsets.UTF_8;

    private VelocityEngine ve = new VelocityEngine();

    public ReportWriter() {
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "class,file");
        ve.setProperty(RuntimeConstants.SPACE_GOBBLING, "none");
        ve.setProperty("runtime.log.logsystem.log4j.logger", "VELLOGGER");
        ve.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        ve.setProperty("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.NullLogSystem");
        ve.init();
    }
    private StringWriter writeReqFileHeader(int curPageNum, ReqFile reqFile, String currentTimeStamp) {
        StringWriter stringWriter = new StringWriter();
        Template headerTemplate = ve.getTemplate("header.vm");
        Map<String, String> headerMap;

        // TODO - need to verify this is saved in the StringBuffer and not cleared by Template.merge()
        if (curPageNum != 1) {
            stringWriter.append("\f");
        }

        headerMap = getHeaderMap(reqFile, currentTimeStamp, curPageNum);
        Context context = new VelocityContext();

        for (Map.Entry<String, String> entry : headerMap.entrySet()) {
            context.put(entry.getKey(), entry.getValue());
        }

        headerTemplate.merge(context, stringWriter);
        return stringWriter;
    }

    private StringWriter writeReqFileLine(ReqFileLine reqFileLine) {
        StringWriter stringWriter = new StringWriter();
        Template reqLineTemplate = ve.getTemplate("reqline.vm");
        Map<String, String> reqLineMap = getReqLineMap(reqFileLine);

        Context context = new VelocityContext();
        stringWriter.getBuffer().setLength(0);

        for (Map.Entry<String, String> entry : reqLineMap.entrySet()) {
            context.put(entry.getKey(), entry.getValue());
        }

        reqLineTemplate.merge(context, stringWriter);
        return stringWriter;
    }

    private void writeToFile(String filePath, StringBuilder sb) throws IOException {
        Path path = Paths.get(filePath);
        try (BufferedWriter fileWriter = Files.newBufferedWriter(path, ReportWriter.ENCODING)) {
            fileWriter.write(sb.toString());
        }
        ReportWriter.logger.debug(sb.toString());
    }

    public void writeDeliveryTicket(ReqFile reqFile, String outFilePath) throws IOException{
        String currentTimeStamp = ReportWriter.dateFormat.format(Calendar.getInstance().getTime());
        List<ReqFileLine> reqFileLines = reqFile.getReqFileLines();

        int curPageNum = 0;
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < reqFileLines.size(); i++) {

            if (i % ReportWriter.reqLinesPerPage == 0) {
                curPageNum += 1;
                sb.append(writeReqFileHeader(curPageNum, reqFile, currentTimeStamp).getBuffer());
            }
            sb.append(writeReqFileLine(reqFileLines.get(i)).getBuffer());
        }

        writeToFile(outFilePath, sb);
    }

    public static String padRight(String s, int n) {
        return String.format("%1$-" + n + "s", s);
    }

    public static String padLeft(String s, int n) {
        return String.format("%1$" + n + "s", s);
    }

    public static String pad(String s, int n, String paddingBehavior) {
        if (ReportWriter.PADLEFT.equals(paddingBehavior)) {
            return ReportWriter.padLeft(s, n);
        }
        return ReportWriter.padRight(s, n);
    }

    private Map<String, String> getReqLineMap(ReqFileLine reqFileLine) {
        Map<String, ReqFileLineField>  rflfs = reqFileLine.getReqFileLineFields();
        Map<String, String> reqFileLineFieldMap = new HashMap<>();

        for (Map.Entry<String, ReqFileLineField> entry : rflfs.entrySet()) {
            ReqFileLineField  rflf = entry.getValue();
            String rflfValue = rflf.getValue();

            // TODO - pull this out, maybe refactor into a new method - preprocessing(key, rflf)
            // 3200L  or 4200K is a par location to use as an example.  Use that for your data for test
            if (entry.getKey().equals("PUT_AWAY_BIN")) {
                if (rflfValue.startsWith("EXCL")) {
                    reqFileLineFieldMap.put(entry.getKey(), pad("--- EXCLUDE ---", rflf.getWidth(), rflf.getPad()));
                    // TODO - if the first character is not numeric it is UNASSIGNED
                    // MR-B3D would be unassigned.  So can change /add a new scenario to say "if it starts with an Alpha character"
                } else if (!rflfValue.equals("") && Character.isLetter(rflfValue.charAt(0)) ) {
                    reqFileLineFieldMap.put(entry.getKey(), pad("--- UNASSIGNED - NONE ---", rflf.getWidth(), rflf.getPad()));
                } else if (rflfs.get("DL_USER_FIELD1").getValue().length() > 0) {
                    reqFileLineFieldMap.put(entry.getKey(), rflfs.get("DL_USER_FIELD1").toString());
                } else {
                    if (rflfValue.length() <= 4) {
                        reqFileLineFieldMap.put(entry.getKey(), ReportWriter.pad(rflfValue.substring(0, rflfValue.length()), rflf.getWidth(), rflf.getPad()));
                    } else {
                        reqFileLineFieldMap.put(entry.getKey(), ReportWriter.pad(rflfValue.substring(0, 5), rflf.getWidth(), rflf.getPad()));
                    }
                }
            } else {
                reqFileLineFieldMap.put(entry.getKey(), rflf.toString());
            }
        }

        return reqFileLineFieldMap;
    }

    private Map<String, String> getHeaderMap(ReqFile reqFile, String currentTimeStamp, int pageNumber) {
        Map<String, String> header = new HashMap<>();
        Map<String, ReqFileLineField> rflfs = reqFile.getReqFileLines().get(0).getReqFileLineFields();

        for (Map.Entry<String, ReqFileLineField> entry : rflfs.entrySet()) {
            header.put(entry.getKey(), entry.getValue().toString());
        }

        header.put("PAGE_NUMBER", ReportWriter.padLeft(String.valueOf(pageNumber), pageNumberWidth));
        header.put("TIMESTAMP", ReportWriter.padLeft(String.valueOf(currentTimeStamp), timestampWidth));

        return header;
    }
}
