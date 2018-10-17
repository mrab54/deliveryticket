package com.bellin.erp.supplychain.deliveryticket.domain.file;

import com.bellin.erp.supplychain.deliveryticket.reqline.ReqLineField;
import org.apache.commons.csv.CSVRecord;
import java.util.ArrayList;
import java.util.Map;

public class ReqLine {
    private ArrayList<ReqLineField> reqLineFields = new ArrayList<ReqLineField>();

    public ReqLine(CSVRecord csvRecord) {
        Map<String, String> csvRecordMap = csvRecord.toMap();

        for (String key : csvRecordMap.keySet()) {
            //this.addReqLineField(csvRecordMap);
            //System.out.println(key + ": " + csvRecordMap.get(key));
        }
    }

    private void addReqLineField() {
        this.reqLineFields.add(reqLineField);
    }

    /*
    private String company;
    private String reqNumber
    private String lineNumber;
    private String item;
    private String description1;
    private String description2;
    private String longDescription;
    private String quantity;
    private String enteredUOM;
    private String unitCost;
    private String fromCompany;
    private String fromLocation;
    private String pickFromBin;
    private String putAwayBin;
    private String shipmentNumber;
    private String createdBy;
    private String reqLocation;
    private String rqlAddr1;
    private String rqlAddr2;
    private String rqlCity;
    private String rqlState;
    private String rqlPostalCode;
    */



}
