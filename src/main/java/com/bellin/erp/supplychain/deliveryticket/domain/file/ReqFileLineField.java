package com.bellin.erp.supplychain.deliveryticket.domain.file;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ReqFileLineField {
    //log
    private String value;
    private String name;


    private static final Map<String, Integer> FIELD_WIDTH_MAP = createMap();

    private static Map<String, Integer> createMap() {
        Map<String, Integer> result = new HashMap<String, Integer>();
        result.put("COMPANY", 4);
        result.put("REQ_NUMBER", 7);
        result.put("LINE_NUMBER", 6);
        result.put("ITEM", 32);
        result.put("DESCRIPTION_1", 30);
        result.put("DESCRIPTION_2", 30);
        result.put("LONG_DESCRIPTION", 350);
        result.put("QUANTITY", 18);
        result.put("ENTERED_UOM", 4);
        result.put("UNIT_COST", 24);
        result.put("FROM_COMPANY", 4);
        result.put("FROM_LOCATION", 5);
        result.put("PICK_FROM_BIN", 7);
        result.put("PUT_AWAY_BIN", 7);
        result.put("SHIPMENT_NUMBER", 10);
        result.put("CREATED_BY", 10);
        result.put("REQ_LOCATION", 5);
        result.put("RQL_ADDR_1", 30);
        result.put("RQL_ADDR_2", 30);
        result.put("RQL_CITY", 18);
        result.put("RQL_STATE", 2);
        result.put("RQL_POSTAL_CODE", 10);
        return Collections.unmodifiableMap(result);
    }

    public ReqFileLineField(String name) {
        this.name = name;
    }

    public void read(Map<String, String> lineMap) throws Exception{
        if (lineMap.containsKey(this.name)) {
            this.value = lineMap.get(this.name);
        }
        else {
            // TODO
            throw new Exception("asdfsadf");
        }
    }
}
