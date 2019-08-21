package com.bellin.erp.supplychain.deliveryticket.domain.file;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class ReqFileLine {
    private int index;
    private Map<String, ReqFileLineField> reqFileLineFields = new TreeMap<String, ReqFileLineField>();
    private Map<String, Map<String, Object>> config;
    //log

    public ReqFileLine(int index, Map<String, Map<String, Object>> config) {
        this.index = index;

        for (String key : config.keySet()) {
            int width = (int) config.get(key).get("width");
            String pad = (String) config.get(key).get("pad");

            ReqFileLineField rflf = new ReqFileLineField(key, width, pad);
            this.reqFileLineFields.put(key, rflf);
        }
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
