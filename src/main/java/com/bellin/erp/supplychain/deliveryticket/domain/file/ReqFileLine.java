package com.bellin.erp.supplychain.deliveryticket.domain.file;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class ReqFileLine {
    private int index;
    private Map<String, ReqFileLineField> reqFileLineFields = new TreeMap<String, ReqFileLineField>();
    //log

    public ReqFileLine(int index) {
        this.index = index;
        for (String key : ReqFileLineField.FIELD_WIDTH_MAP.keySet()) {
            ReqFileLineField rflf = new ReqFileLineField(key, ReqFileLineField.FIELD_WIDTH_MAP.get(key));
            this.reqFileLineFields.put(key, rflf);
        };
    }

    public void read(Map<String, String> lineMap) {
        for (Map.Entry<String, ReqFileLineField> entry : this.reqFileLineFields.entrySet()) {
            ReqFileLineField lineField = entry.getValue();
            try {
                lineField.read(lineMap);
            } catch (Exception e){
                //TODO
                System.err.println("asdf");
                System.err.println(e);
            }
        }
        // // NOPE for k in lineMap
        // for k,v in reqFileLineFields
        //   v.read(lineMap)
    }

    public boolean isValid() {
        // TODO - finish this
        return true;
    }

    public Map<String, ReqFileLineField> getReqFileLineFields() {
        return this.reqFileLineFields;
    }

}
