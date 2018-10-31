package com.bellin.erp.supplychain.deliveryticket.report;

import com.bellin.erp.supplychain.deliveryticket.domain.file.ReqFile;
import com.bellin.erp.supplychain.deliveryticket.domain.file.ReqFileLine;
import com.bellin.erp.supplychain.deliveryticket.domain.file.ReqFileLineField;
import com.sun.xml.internal.ws.api.ha.StickyFeature;
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
        sb.append(StringUtils.repeat(StringUtils.SPACE, 14));
        sb.append(reqFileLineFieldMap.get("RQL_NAME").toString());
        headerLines.add(sb.toString());
        sb.setLength(0);

        sb.append(StringUtils.repeat(StringUtils.SPACE, 89));
        sb.append(reqFileLineFieldMap.get("RQL_ADDR_1").toString());
        headerLines.add(sb.toString());
        sb.setLength(0);

        sb.append(StringUtils.repeat(StringUtils.SPACE, 89));
        sb.append(reqFileLineFieldMap.get("RQL_ADDR_2").toString());
        headerLines.add(sb.toString());
        sb.setLength(0);

        sb.append(StringUtils.repeat(StringUtils.SPACE, 89));
        sb.append(reqFileLineFieldMap.get("RQL_CITY").toString());
        sb.append(StringUtils.repeat(StringUtils.SPACE, 3));
        sb.append(reqFileLineFieldMap.get("RQL_STATE").toString());
        sb.append(StringUtils.SPACE);
        sb.append(reqFileLineFieldMap.get("RQL_POSTAL_CODE").toString());
        headerLines.add(sb.toString());
        sb.setLength(0);

        sb.append("Destination: ");
        sb.append(reqFileLineFieldMap.get("REQ_LOCATION").toString());
        sb.append(StringUtils.repeat(StringUtils.SPACE, 19));
        sb.append(reqFileLineFieldMap.get("RQL_NAME").toString());
        headerLines.add(sb.toString());
        sb.setLength(0);

        sb.append("Requester: ");
        sb.append(StringUtils.repeat(StringUtils.SPACE, 21));
        sb.append(reqFileLineFieldMap.get("REQUESTER_NAME").toString());
        headerLines.add(sb.toString());
        sb.setLength(0);

        sb.append("Pick Bin  Line  Item Description");
        sb.append(StringUtils.repeat(StringUtils.SPACE, 18));
        sb.append("Manufacturer Information");
        sb.append(StringUtils.repeat(StringUtils.SPACE, 3));
        sb.append("Quantity To Pick Uom");
        sb.append(StringUtils.repeat(StringUtils.SPACE, 2));
        sb.append("Shipped");
        headerLines.add(sb.toString());
        sb.setLength(0);

        sb.append("Putaway");
        sb.append(StringUtils.repeat(StringUtils.SPACE, 70));
        sb.append("Unit Cost");
        sb.append(StringUtils.repeat(StringUtils.SPACE, 30));
        sb.append("Extended Cost");
        headerLines.add(sb.toString());
        sb.setLength(0);

        sb.append("-------  ----  --------------------------------   -------------------------  ---------------- ---- ---------------  ---------------");
        headerLines.add(sb.toString());
        headerLines.add(StringUtils.EMPTY);
        sb.setLength(0);

        headerLines.add(sb.toString());
        return headerLines;
    }

    private List<String> createReqLine(ReqFileLine reqFileLine) {
        List<String> reqLines = new ArrayList<>();
        Map<String, ReqFileLineField> reqFileLineFieldMap = reqFileLine.getReqFileLineFields();

        StringBuilder sb = new StringBuilder();
        sb.append(reqFileLineFieldMap.get("PICK_FROM_BIN").toString());
        sb.append(StringUtils.repeat(StringUtils.SPACE, 7));
        sb.append(reqFileLineFieldMap.get("LINE_NUMBER").toString());
        sb.append(StringUtils.repeat(StringUtils.SPACE, 2));
        sb.append(reqFileLineFieldMap.get("ITEM").toString());
        //sb.append();

        return reqLines;
    }
}
