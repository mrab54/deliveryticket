package com.bellin.erp.supplychain.deliveryticket.domain.file;

import com.bellin.erp.supplychain.deliveryticket.exception.ReqFileLineFieldMissingException;
import com.bellin.erp.supplychain.deliveryticket.report.ReportWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class ReqFileLineField {
    private static final Logger logger = LoggerFactory.getLogger(ReqFileLineField.class);
    protected String value = null;
    protected String name;
    protected int width;
    protected String pad;


    public ReqFileLineField(String name, int width, String pad) {
        this.name = name;
        this.width = width;
        this.pad = pad;
    }

    public void read(Map<String, String> lineMap) throws ReqFileLineFieldMissingException {

        if (lineMap.containsKey(name)) {
            value = lineMap.get(name);
        }
        else {
            throw new ReqFileLineFieldMissingException("Missing ReqFileLineField: " + name);
        }
    }

    private boolean isValid() {
        return true;
    }

    public int getWidth(){
        return width;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getPad() {
        return pad;
    }

    public String toString() {
        return ReportWriter.pad(value, width, pad);
    }

}
