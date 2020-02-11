package com.bellin.erp.supplychain.deliveryticket.domain.file;

import com.bellin.erp.supplychain.deliveryticket.exception.UFFileLineFieldMissingException;
import com.bellin.erp.supplychain.deliveryticket.report.ReportWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class UFFileLineField {
    private static final Logger logger = LoggerFactory.getLogger(UFFileLineField.class);
    protected String value = null;
    protected String name;
    protected int width;
    protected String pad;


    public UFFileLineField(String name, int width, String pad) {
        this.name = name;
        this.width = width;
        this.pad = pad;
    }

    public void read(Map<String, String> lineMap) throws UFFileLineFieldMissingException {

        if (lineMap.containsKey(name)) {
            value = lineMap.get(name);
        }
        else {
            throw new UFFileLineFieldMissingException("Missing UFFileLineField: " + name);
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

    public void setValue(String value) { this.value = value; }

    public String toString() {
        return ReportWriter.pad(value, width, pad);
    }

}
