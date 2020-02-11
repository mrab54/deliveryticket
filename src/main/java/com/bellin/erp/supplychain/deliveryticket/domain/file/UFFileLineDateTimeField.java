package com.bellin.erp.supplychain.deliveryticket.domain.file;


import com.bellin.erp.supplychain.deliveryticket.exception.UFFileLineFieldMissingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;

public class UFFileLineDateTimeField extends UFFileLineField {

    private static final Logger logger = LoggerFactory.getLogger(UFFileLineDateTimeField.class);
    private LocalDateTime dateTimeValue = null;
    private static final DateTimeFormatter parseFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yy");
    public UFFileLineDateTimeField(String name, int width, String pad) {
        super(name, width, pad);

    }

    @Override
    public void read(Map<String, String> lineMap) throws UFFileLineFieldMissingException {
        if (lineMap.containsKey(name)) {
            try {
                dateTimeValue = LocalDateTime.parse(lineMap.get(name), UFFileLineDateTimeField.parseFormatter);
                value = dateTimeValue.format(UFFileLineDateTimeField.dateFormatter);

            } catch (DateTimeParseException e) {
                UFFileLineDateTimeField.logger.error("Unable to parse DateTime field: {}", lineMap.get(name));
                value = lineMap.get(name).substring(0, 10);
            } catch (DateTimeException e) {
                UFFileLineDateTimeField.logger.error("Unable to parse DateTime field: {}", lineMap.get(name));
                value = "00/00/00";
            }
        }
        else {
            throw new UFFileLineFieldMissingException("Missing ReqFileLineField: " + name);
        }
    }

    public LocalDateTime getDateTimeValue() {
        return dateTimeValue;
    }
}
