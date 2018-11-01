package com.bellin.erp.supplychain.deliveryticket.report;

import com.bellin.erp.supplychain.deliveryticket.domain.file.ReqFile;
import com.bellin.erp.supplychain.deliveryticket.domain.file.ReqFileLine;
import com.bellin.erp.supplychain.deliveryticket.domain.file.ReqFileLineField;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.tools.generic.DisplayTool;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

public class ReportWriter {
    final static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    final static int pageWidth = 130;
    final static int pageHeight = 58;
    final static int reqLinesPerPage = 10;
    final static Charset ENCODING = StandardCharsets.UTF_8;

    public void writeDeliveryTicket(ReqFile reqFile) {

        String timeStamp = dateFormat.format(Calendar.getInstance().getTime());
        VelocityEngine ve = new VelocityEngine();
        ve.init();
        Template t = ve.getTemplate("deliveryticket.vm");
        VelocityContext context = new VelocityContext();


        List<ReqFileLine> reqFileLines = reqFile.getReqFileLines();

        int curPageNum = 0;
        Map<String, String> headerMap = new HashMap<>();
        List<Map<String, String>> reqLines = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        for (int i = 1; i < reqFileLines.size(); i++) {
            if (i % 10 == 0) {
                curPageNum += 1;
                //private Map<String, String> getHeaderMap(ReqFile reqFile, String timeStamp, int pageNumber) {
                headerMap = getHeaderMap(reqFile, timeStamp, curPageNum);
                reqLines = new ArrayList<Map<String, String>>();
            }


        }







        //context.put("name", "World");
        //Person p = new Person("Bob   ", 10);
        //Person p = new Person("   Bob", 10);
        //context.put("person", p);

        context.put("display", new DisplayTool());
        StringWriter writer = new StringWriter();
        t.merge(context, writer);
        System.out.println(writer.toString());

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
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
            header.put(entry.getKey(), padLeft(entry.getValue().toString(), entry.getValue().getWidth()));
        }

        header.put("PAGE_NUMBER", padLeft(String.valueOf(pageNumber), 10));
        header.put("TIMESTAMP", padLeft(String.valueOf(timeStamp), 19));

        return header;
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

        headerLines.add(StringUtils.EMPTY);
        headerLines.add(StringUtils.EMPTY);

        sb.append("Destination: ");
        sb.append(reqFileLineFieldMap.get("REQ_LOCATION").toString());
        sb.append(StringUtils.repeat(StringUtils.SPACE, 19));
        sb.append(reqFileLineFieldMap.get("RQL_NAME").toString());
        headerLines.add(sb.toString());
        sb.setLength(0);

        sb.append("Requester: ");
        // TODO add to query and CSV output
        sb.append(reqFileLineFieldMap.get("REQUESTER").toString());
        sb.append(StringUtils.repeat(StringUtils.SPACE, 21));
        sb.append(reqFileLineFieldMap.get("REQUESTER_NAME").toString());
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
