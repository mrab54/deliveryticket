package com.bellin.erp.supplychain.deliveryticket.domain.file;

import java.util.Map;

public class ReqFileLineField {
    //log
    private String value;
    private String name;

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
