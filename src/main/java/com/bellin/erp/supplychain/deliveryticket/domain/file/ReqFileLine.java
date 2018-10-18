package com.bellin.erp.supplychain.deliveryticket.domain.file;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class ReqFileLine {
    private int index;
    private Map<String, ReqFileLineField> reqFileLineFields = new TreeMap<String, ReqFileLineField>();
    //log
    //confObj

    //public ReqFileLine(int index, confObj){}
    public ReqFileLine(int index) {
        this.index = index;
        // TODO
        // for every lineField in the config, this.reqFileLineFields.put(<fieldName>, new ReqFileLineField(fieldName,...))
    }

    public void read(Map<String, String> lineMap) {
        for (Map.Entry<String, ReqFileLineField> entry : this.reqFileLineFields.entrySet()) {
            ReqFileLineField lineField = (ReqFileLineField) entry.getValue();
            try {
                lineField.read(lineMap);
            } catch (Exception e){
                //TODO
                System.err.println("asdf");
            }
        }
        // // NOPE for k in lineMap
        // for k,v in reqFileLineFields
        //   v.read(lineMap)
    }

}
