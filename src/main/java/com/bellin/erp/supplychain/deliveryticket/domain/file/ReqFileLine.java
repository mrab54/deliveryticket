package com.bellin.erp.supplychain.deliveryticket.domain.file;

import com.bellin.erp.supplychain.deliveryticket.exception.ReqFileLineFieldMissingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.TreeMap;

@SuppressWarnings("ALL")
public class ReqFileLine {
    private static final Logger logger = LoggerFactory.getLogger(ReqFileLine.class);
    private int index;
    private Map<String, ReqFileLineField> reqFileLineFields = new TreeMap<String, ReqFileLineField>();
    private Map<String, Map<String, Object>> config = null;

    public ReqFileLine(int index, Map<String, Map<String, Object>> config) {
        this.index = index;

        for (Map.Entry<String, Map<String, Object>> entry : config.entrySet()) {
            String key = entry.getKey();
            int width = (int) entry.getValue().get("width");
            String pad = (String) entry.getValue().get("pad");
            String type = (String) entry.getValue().get("type");

            ReqFileLineField rflf;

            if ("String".equals(type)) {
                rflf = new ReqFileLineStringField(key, width, pad);
            } else if ("DateTime".equals(type)) {
                rflf = new ReqFileLineDateTimeField(key, width, pad);
            } else {
                rflf = new ReqFileLineField(key, width, pad);
            }
            reqFileLineFields.put(key, rflf);
        }
    }

    public void read(Map<String, String> lineMap) throws ReqFileLineFieldMissingException {
        // For every ReqFileLineField in this ReqFileLine
        // Do a ReqFileLineField.read(lineMap)
        // lineMap is the CSV file line key/value format
        for (Map.Entry<String, ReqFileLineField> entry : reqFileLineFields.entrySet()) {
            ReqFileLineField lineField = entry.getValue();
            lineField.read(lineMap);
        }
    }

    public boolean isValid() {
        return true;
    }

    public Map<String, ReqFileLineField> getReqFileLineFields() {
        return reqFileLineFields;
    }

}
