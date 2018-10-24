package com.bellin.erp.supplychain.deliveryticket.report;

import com.bellin.erp.supplychain.deliveryticket.domain.file.ReqFile;
import com.bellin.erp.supplychain.deliveryticket.domain.file.ReqFileLine;
import com.bellin.erp.supplychain.deliveryticket.domain.file.ReqFileLineField;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class ReportWriter {
    final static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    final static int pageWidth = 130;
    final static int pageHeight = 58;
    final static Charset ENCODING = StandardCharsets.UTF_8;

    public void writeDeliveryTicket(ReqFile reqFile) {

        List<String> headerLines = createHeader(reqFile, 1);
        System.exit(0);
        // TODO
        String OUTPUT_FILE_NAME = "D:\\ipaoutput\\SHIPMENTRELEASE\\whsrpt-0000015809.txt";

        Path path = Paths.get(OUTPUT_FILE_NAME);
        try (BufferedWriter writer = Files.newBufferedWriter(path, this.ENCODING)) {
            /*
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
            */
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public static String padRight(String s, int n) {
        return String.format("%1$-" + n + "s", s);
    }

    public static String padLeft(String s, int n) {
        return String.format("%1$" + n + "s", s);
    }

    private List<String> createHeader(ReqFile reqFile, int pageNumber) {
        List<String> headerLines = new ArrayList<>();
        String timeStamp = dateFormat.format(Calendar.getInstance().getTime());

        Map<String, ReqFileLineField> reqFileLineFieldMap = reqFile.getReqFileLines().get(0).getReqFileLineFields();

        StringBuilder sb = new StringBuilder();
        sb.append(timeStamp);
        sb.append(StringUtils.repeat(StringUtils.SPACE, 25));
        sb.append(reqFileLineFieldMap.get("COMPANY").toString());
        sb.append(" - ");
        //TODO COMPANY name
        sb.append(reqFileLineFieldMap.get("FROM_LOCATION").toString());
        //TODO LOCATION name
        sb.append(StringUtils.repeat(StringUtils.SPACE, 23));
        sb.append("Page");
        sb.append(padLeft(Integer.toString(pageNumber), 12));
        headerLines.add(sb.toString());
        sb.setLength(0);

        sb.append(padLeft("Delivery Ticket", 60));
        headerLines.add(sb.toString());
        sb.setLength(0);

        headerLines.add(StringUtils.EMPTY);
        headerLines.add(StringUtils.EMPTY);

        sb.append("Shipment");
        sb.append(StringUtils.repeat(StringUtils.SPACE, 4));
        sb.append("Requesting Location");
        sb.append(StringUtils.repeat(StringUtils.SPACE, 5));
        sb.append("Requisition Number");
        headerLines.add(sb.toString());
        sb.setLength(0);

        sb.append(padLeft(StringUtils.stripStart(reqFileLineFieldMap.get("SHIPMENT_NUMBER").toString(), "0"), 12));
        sb.append(padLeft(reqFileLineFieldMap.get("REQ_LOCATION").toString(), 25));
        sb.append(StringUtils.repeat(StringUtils.SPACE, 20));
        sb.append(padRight(reqFileLineFieldMap.get("REQ_NUMBER").toString(), 18));
        sb.append(StringUtils.repeat(StringUtils.SPACE, 30));
        sb.append(reqFileLineFieldMap.get("RQL_ADDR_1"));
        headerLines.add(sb.toString());
        sb.setLength(0);

        headerLines.add(sb.toString());
        return headerLines;
    }

    private List<String> createReqLine(ReqFileLine reqFileLine) {
        List<String> reqLines = new ArrayList<>();

        return reqLines;
    }
}
