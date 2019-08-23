package com.bellin.erp.supplychain.deliveryticket.report;

import com.bellin.erp.supplychain.deliveryticket.domain.file.ReqFile;
import com.bellin.erp.supplychain.deliveryticket.domain.file.ReqFileLine;
import com.bellin.erp.supplychain.deliveryticket.domain.file.ReqFileLineField;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.apache.velocity.tools.generic.DisplayTool;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;


public class ReportWriter {
    final static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    final static String PADLEFT = "left";
    final static String PADRIGHT = "right";
    final static int pageWidth = 130;
    final static int pageHeight = 58;
    final static int reqLinesPerPage = 10;
    final static Charset ENCODING = StandardCharsets.UTF_8;

    public void writeDeliveryTicket(ReqFile reqFile, String outFilePath) {

        // https://wiki.apache.org/velocity/VelocityWhitespaceGobbling
        //String OUTPUT_FILE_NAME = "D:\\ipaoutput\\SHIPMENTRELEASE\\whsrpt-0000015809.txt";
        //String OUTPUT_FILE_NAME = "C:\\Users\\mrab\\dev\\code\\java\\deliveryticket\\whsrpt-0000015809.txt";
        String OUTPUT_FILE_NAME = outFilePath;
        String timeStamp = dateFormat.format(Calendar.getInstance().getTime());
        VelocityEngine ve = new VelocityEngine();

        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "class,file");
        ve.setProperty(RuntimeConstants.SPACE_GOBBLING, "none");
        ve.setProperty("runtime.log.logsystem.log4j.logger", "VELLOGGER");
        ve.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        ve.setProperty("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.NullLogSystem");
        ve.init();
        Template headerTemplate = ve.getTemplate("header.vm");
        Template reqLineTemplate = ve.getTemplate("reqline.vm");

        List<ReqFileLine> reqFileLines = reqFile.getReqFileLines();

        int curPageNum = 0;
        Map<String, String> headerMap;
        Map<String, String> reqLineMap;
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < reqFileLines.size(); i++) {
            if (i % 10 == 0) {
                curPageNum += 1;

                if (curPageNum != 1) {
                    sb.append("\f");
                }

                // Get headerMap
                headerMap = getHeaderMap(reqFile, timeStamp, curPageNum);

                // write to string via template
                VelocityContext context = new VelocityContext();
                StringWriter stringWriter = new StringWriter();

                for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                    context.put(entry.getKey(), entry.getValue());
                }

                // sb.append that string
                headerTemplate.merge(context, stringWriter);
                sb.append(stringWriter.toString());


                // TODO if curPageNum != 1, sb.append newline and formfeed character

            }
            reqLineMap = getReqLineMap(reqFileLines.get(i));

            VelocityContext context = new VelocityContext();
            StringWriter stringWriter = new StringWriter();

            for (Map.Entry<String, String> entry : reqLineMap.entrySet()) {
                context.put(entry.getKey(), entry.getValue());
            }

            // write to string via template and sb.append
            reqLineTemplate.merge(context, stringWriter);
            sb.append(stringWriter.toString());
        }

        Path path = Paths.get(OUTPUT_FILE_NAME);
        try (BufferedWriter fileWriter = Files.newBufferedWriter(path, ENCODING)) {
            fileWriter.write(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(sb.toString());
        System.exit(0);
    }

    public static String padRight(String s, int n) {
        return String.format("%1$-" + n + "s", s);
    }

    public static String padLeft(String s, int n) {
        return String.format("%1$" + n + "s", s);
    }

    public static String pad(String s, int n, String paddingBehavior) {
        if (PADLEFT.equals(paddingBehavior)) {
            return padLeft(s, n);
        }
        return padRight(s, n);
    }

    private Map<String, String> getReqLineMap(ReqFileLine reqFileLine) {
        Map<String, ReqFileLineField>  rflfs = reqFileLine.getReqFileLineFields();
        Map<String, String> reqFileLineFieldMap = new HashMap<>();

        for (Map.Entry<String, ReqFileLineField> entry : rflfs.entrySet()) {
            reqFileLineFieldMap.put(entry.getKey(), pad(entry.getValue().toString(), entry.getValue().getWidth(), entry.getValue().getPad()));
        }

        return reqFileLineFieldMap;
    }

    private Map<String, String> getHeaderMap(ReqFile reqFile, String timeStamp, int pageNumber) {
        Map<String, String> header = new HashMap<>();
        //String timeStamp = dateFormat.format(Calendar.getInstance().getTime());
        Map<String, ReqFileLineField> rflfs = reqFile.getReqFileLines().get(0).getReqFileLineFields();

        // TODO include timestamp
        /*
        ReqFileLineField company = rflfs.get("COMPANY");
        header.put("COMPANY", padLeft(company.toString(), company.getWidth()));

        ReqFileLineField fromLocation = rflfs.get("FROM_LOCATION");
        header.put("FROM_LOCATION", padLeft(fromLocation.toString(), fromLocation.getWidth()));

        ReqFileLineField companyName = rflfs.get("COMPANY_NAME");
        header.put("COMPANY_NAME", padLeft(companyName.toString(), companyName.getWidth()));

        ReqFileLineField fromLocationName = rflfs.get("FROM_LOCATION_NAME");
        header.put("FROM_LOCATION_NAME", padLeft(fromLocationName.toString(), fromLocationName.getWidth()));

        //TODO note this somehow
        header.put("PAGE_NUMBER", padLeft(String.valueOf(pageNumber), 10));

        ReqFileLineField shipmentNumber = rflfs.get("SHIPMENT_NUMBER");
        header.put("SHIPMENT_NUMBER", padLeft(shipmentNumber.toString(), shipmentNumber.getWidth()));

        ReqFileLineField reqLocation = rflfs.get("REQ_LOCATION");
        header.put("REQ_LOCATION", padLeft(reqLocation.toString(), reqLocation.getWidth()));

        ReqFileLineField reqNumber = rflfs.get("REQ_NUMBER");
        header.put("REQ_NUMBER", padLeft(reqNumber.toString(), reqNumber.getWidth()));

        ReqFileLineField rqlName = rflfs.get("RQL_NAME");
        header.put("RQL_NAME", padLeft(rqlName.toString(), rqlName.getWidth()));

        ReqFileLineField rqlAddr1 = rflfs.get("RQL_ADDR_1");
        header.put("RQL_ADDR_1", padLeft(rqlAddr1.toString(), rqlAddr1.getWidth()));

        ReqFileLineField rqlAddr2 = rflfs.get("RQL_ADDR_2");
        header.put("RQL_ADDR_2", padLeft(rqlAddr2.toString(), rqlAddr2.getWidth()));

        ReqFileLineField rqlCity = rflfs.get("RQL_CITY");
        header.put("RQL_CITY", padLeft(rqlCity.toString(), rqlCity.getWidth()));

        ReqFileLineField rqlState = rflfs.get("RQL_STATE");
        header.put("RQL_STATE", padLeft(rqlState.toString(), rqlState.getWidth()));

        ReqFileLineField rqlPostalCode = rflfs.get("RQL_POSTAL_CODE");
        header.put("RQL_POSTAL_CODE", padLeft(rqlPostalCode.toString(), rqlPostalCode.getWidth()));

        ReqFileLineField requester = rflfs.get("REQUESTER");
        header.put("REQUESTER", padLeft(requester.toString(), requester.getWidth()));

        ReqFileLineField requesterName = rflfs.get("REQUESTER_NAME");
        header.put("REQUESTER_NAME", padLeft(rqlState.toString(), rqlState.getWidth()));

        // TODO check on this for "Destination" is this equivalent
        //ReqFileLineField reqLocation = rflfs.get("REQ_LOCATION");
        //header.put("REQ_LOCATION", padLeft(reqLocation.toString(), reqLocation.getWidth()));
        */

        for (Map.Entry<String, ReqFileLineField> entry : rflfs.entrySet()) {
            header.put(entry.getKey(), pad(entry.getValue().toString(), entry.getValue().getWidth(), entry.getValue().getPad()));
        }

        header.put("PAGE_NUMBER", padLeft(String.valueOf(pageNumber), 10));
        header.put("TIMESTAMP", padLeft(String.valueOf(timeStamp), 19));

        return header;
    }
}
