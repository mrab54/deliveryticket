package com.bellin.erp.supplychain.deliveryticket.domain.file;

import java.util.Map;

public class ReqFileLine {
    private int index;
    private TreeMap<String, ReqFileLineField> reqFileLineFields = new TreeMap<String, ReqFileLineField>();
    //log
    //confObj

    //public ReqFileLine(int index, confObj){}
    public ReqFileLine(int index) {
        this.index = index;
        // TODO
        // for every lineField in the config, this.reqFileLineFields.put(<fieldName>, new ReqFileLineField(fieldName,...))
    }

    public void read(Map<String, String> lineMap) {
        // // NOPE for k in lineMap
        // for k,v in reqFileLineFields
        //   v.read(lineMap)
    }

}
