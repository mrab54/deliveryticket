package com.bellin.erp.supplychain.deliveryticket.report;

import com.bellin.erp.supplychain.deliveryticket.domain.file.ReqFile;
import com.bellin.erp.supplychain.deliveryticket.domain.file.ReqFileLine;
import com.bellin.erp.supplychain.deliveryticket.domain.file.ReqFileLineField;
import com.sun.xml.internal.ws.api.ha.StickyFeature;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
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
    final static int reqLinesPerPage = 10;
    final static Charset ENCODING = StandardCharsets.UTF_8;

    public void writeDeliveryTicket(ReqFile reqFile) {
        List<List<String>> reqLines = new ArrayList<>();

        for (ReqFileLine reqLine : reqFile.getReqFileLines()) {
            List<String> newReqLine = createReqLine(reqLine);
            reqLines.add(newReqLine);
        }

        int reqLinesRemaining = reqLines.size();
        int curReqLinesOnPage = 0;
        int curPage = 1;
        int curReqLine = 0;

        //String OUTPUT_FILE_NAME = "D:\\ipaoutput\\SHIPMENTRELEASE\\whsrpt-0000015809.txt";
        String OUTPUT_FILE_NAME = "C:\\Users\\mrab\\dev\\code\\java\\deliveryticket\\whsrpt-0000015809.txt";

        Path path = Paths.get(OUTPUT_FILE_NAME);
        try (BufferedWriter writer = Files.newBufferedWriter(path, ENCODING)) {

            while (reqLinesRemaining > 0) {
                if (curReqLinesOnPage == 0) {
                    List<String> headerLines = createHeader(reqFile, curPage);
                    for (String line : headerLines) {
                        writer.write(line);
                        writer.newLine();
                    }
                }

                if (curReqLinesOnPage < 10) {
                    List<String> reqLine = reqLines.get(curReqLine);
                    for (String line : reqLine) {
                        writer.write(line);
                        writer.newLine();
                        writer.newLine();
                    }
                    curReqLinesOnPage += 1;
                    reqLinesRemaining -= 1;
                    curReqLine += 1;
                }
                else {
                    curReqLinesOnPage = 0;
                    curPage += 1;
                }
            }
        } catch (IOException e) {
            System.err.println(e);
        }


        System.exit(0);
        // TODO
            /*
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
            */
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
        // TODO this should be Req loc name description. Add to query.
        sb.append(reqFileLineFieldMap.get("RQL_ADDR_1"));
        headerLines.add(sb.toString());
        sb.setLength(0);

        sb.append(StringUtils.repeat(StringUtils.SPACE, 89));
        //sb.append(padLeft(reqFileLineFieldMap.get("RQL_ADDR_2").toString(), 106));
        sb.append(reqFileLineFieldMap.get("RQL_ADDR_2").toString());
        headerLines.add(sb.toString());
        sb.setLength(0);

        sb.append(StringUtils.repeat(StringUtils.SPACE, 89));
        //sb.append(padLeft(reqFileLineFieldMap.get("RQL_CITY").toString(), 89));
        sb.append(reqFileLineFieldMap.get("RQL_CITY").toString());
        sb.append(StringUtils.repeat(StringUtils.SPACE, 3));
        sb.append(reqFileLineFieldMap.get("RQL_STATE").toString());
        sb.append(StringUtils.SPACE);
        sb.append(reqFileLineFieldMap.get("RQL_POSTAL_CODE"));
        headerLines.add(sb.toString());
        sb.setLength(0);

        headerLines.add(StringUtils.EMPTY);
        headerLines.add(StringUtils.EMPTY);

        sb.append("Destination: ");
        sb.append(reqFileLineFieldMap.get("REQ_LOCATION").toString());
        sb.append(StringUtils.repeat(StringUtils.SPACE, 19));
        // TODO get req loc description
        sb.append("REQ_LOC_DESCRIPTION_PLACE_HOLDER");
        //sb.append(reqFileLineFieldMap.get("REQ_LOC_DESCRIPTION"))
        headerLines.add(sb.toString());
        sb.setLength(0);

        sb.append("Requester: ");
        // TODO add to query and CSV output
        sb.append(reqFileLineFieldMap.get("REQUESTER").toString());
        sb.append(StringUtils.repeat(StringUtils.SPACE, 21));
        // TODO get requester description
        sb.append("REQUESTER_DESCRIPTION_PLACEHOLDER");
        //sb.append(reqFileLineFieldMap.get("REQUESTER_DESCRIPTION"));
        headerLines.add(sb.toString());
        sb.setLength(0);

        headerLines.add(StringUtils.EMPTY);
        headerLines.add(StringUtils.EMPTY);

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
        return headerLines;
    }

    private List<String> createReqLine(ReqFileLine reqFileLine) {
        List<String> reqLine = new ArrayList<>();
        Map<String, ReqFileLineField> reqFileLineFieldMap = reqFileLine.getReqFileLineFields();

        String unitCost = reqFileLineFieldMap.get("UNIT_COST").toString();
        String quantity = reqFileLineFieldMap.get("QUANTITY").toString();
        BigDecimal unitCostDecimal = new BigDecimal(unitCost);
        BigDecimal quantityDecimal = new BigDecimal(quantity);
        BigDecimal extendedCostDecimal = unitCostDecimal.multiply(quantityDecimal);
        extendedCostDecimal = extendedCostDecimal.setScale(4, BigDecimal.ROUND_HALF_UP);

        StringBuilder sb = new StringBuilder();
        sb.append(reqFileLineFieldMap.get("PICK_FROM_BIN").toString());
        sb.append(StringUtils.repeat(StringUtils.SPACE, 7));
        sb.append(reqFileLineFieldMap.get("LINE_NUMBER").toString());
        sb.append(StringUtils.repeat(StringUtils.SPACE, 2));
        sb.append(reqFileLineFieldMap.get("ITEM").toString());
        sb.append(StringUtils.repeat(StringUtils.SPACE, 40));
        // TODO get item manufacturing info 1
        sb.append("Manufacturer Information 1");
        sb.append(StringUtils.repeat(StringUtils.SPACE, 20));
        sb.append(quantity);
        sb.append(StringUtils.repeat(StringUtils.SPACE, 2));
        sb.append(reqFileLineFieldMap.get("ENTERED_UOM").toString());
        sb.append(StringUtils.repeat(StringUtils.SPACE, 2));
        sb.append(StringUtils.repeat("_", 15));
        sb.append(StringUtils.repeat(StringUtils.SPACE, 2));
        sb.append(StringUtils.repeat("_", 15));
        reqLine.add(sb.toString());
        sb.setLength(0);

        sb.append(reqFileLineFieldMap.get("PUT_AWAY_BIN").toString());
        sb.append(StringUtils.repeat(StringUtils.SPACE, 9));
        sb.append(reqFileLineFieldMap.get("DESCRIPTION_1").toString());
        sb.append(StringUtils.repeat(StringUtils.SPACE, 4));
        // TODO get item manufacturing info 1
        //sb.append(reqFileLineFieldMap.get("MANUFACTURE_2").toString());
        sb.append("Manufacturer Information 2");
        reqLine.add(sb.toString());
        sb.setLength(0);

        sb.append(StringUtils.repeat(StringUtils.SPACE, 16));
        sb.append(reqFileLineFieldMap.get("DESCRIPTION_2").toString());
        sb.append(StringUtils.repeat(StringUtils.SPACE, 16));
        sb.append(reqFileLineFieldMap.get("LONG_DESCRIPTION").toString());
        sb.append(StringUtils.repeat(StringUtils.SPACE, 4));
        sb.append(unitCost);
        sb.append(StringUtils.repeat(StringUtils.SPACE, 20));
        sb.append(extendedCostDecimal);
        reqLine.add(sb.toString());

        return reqLine;
    }
}
