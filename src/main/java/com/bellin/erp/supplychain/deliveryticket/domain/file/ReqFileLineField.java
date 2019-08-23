package com.bellin.erp.supplychain.deliveryticket.domain.file;

import com.bellin.erp.supplychain.deliveryticket.report.ReportWriter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ReqFileLineField {
    //log
    protected String value;
    protected String name;
    protected int width;
    protected String pad;


    public ReqFileLineField(String name, int width, String pad) {
        this.name = name;
        this.width = width;
        this.pad = pad;
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

    public int getWidth(){
        return this.width;
    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }

    public String getPad() {
        return this.pad;
    }

    public String toString() {
        return ReportWriter.pad(this.value, this.width, this.pad);
    }

}
