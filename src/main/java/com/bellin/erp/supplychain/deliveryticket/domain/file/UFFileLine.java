package com.bellin.erp.supplychain.deliveryticket.domain.file;

import com.bellin.erp.supplychain.deliveryticket.exception.UFFileLineFieldMissingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.TreeMap;

@SuppressWarnings("ALL")
public class UFFileLine {
    private static final Logger logger = LoggerFactory.getLogger(UFFileLine.class);
    private int index;
    private Map<String, UFFileLineField> ufFileLineFields = new TreeMap<String, UFFileLineField>();
    private Map<String, Map<String, Object>> config = null;

    public UFFileLine(int index, Map<String, Map<String, Object>> config) {
        this.index = index;

        for (Map.Entry<String, Map<String, Object>> entry : config.entrySet()) {
            String key = entry.getKey();
            int width = (int) entry.getValue().get("width");
            String pad = (String) entry.getValue().get("pad");
            String type = (String) entry.getValue().get("type");

            UFFileLineField ufflf;

            if ("String".equals(type)) {
                ufflf = new UFFileLineStringField(key, width, pad);
            } else if ("DateTime".equals(type)) {
                ufflf = new UFFileLineDateTimeField(key, width, pad);
            } else {
                ufflf = new UFFileLineField(key, width, pad);
            }
            ufFileLineFields.put(key, ufflf);
        }
    }

    public void read(Map<String, String> lineMap) throws UFFileLineFieldMissingException {
        // For every UFFileLineField in this UFFileLine
        // Do a UFFileLineField.read(lineMap)
        // lineMap is the CSV file line key/value format
        for (Map.Entry<String, UFFileLineField> entry : ufFileLineFields.entrySet()) {
            UFFileLineField lineField = entry.getValue();
            lineField.read(lineMap);
        }
    }

    public boolean isValid() {
        return true;
    }

    public Map<String, UFFileLineField> getUFFileLineFields() {
        return ufFileLineFields;
    }

}
