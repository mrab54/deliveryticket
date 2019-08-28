package com.bellin.erp.supplychain.deliveryticket.domain.file;


import com.bellin.erp.supplychain.deliveryticket.exception.ReqFileLineFieldMissingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;

public class ReqFileLineDateTimeField extends ReqFileLineField {

    private static final Logger logger = LoggerFactory.getLogger(ReqFileLineDateTimeField.class);
    private LocalDateTime dateTimeValue = null;
    private static final DateTimeFormatter parseFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yy");
    public ReqFileLineDateTimeField(String name, int width, String pad) {
        super(name, width, pad);

    }

    @Override
    public void read(Map<String, String> lineMap) throws ReqFileLineFieldMissingException {
        if (lineMap.containsKey(name)) {
            try {
                dateTimeValue = LocalDateTime.parse(lineMap.get(name), ReqFileLineDateTimeField.parseFormatter);
                value = dateTimeValue.format(ReqFileLineDateTimeField.dateFormatter);

            } catch (DateTimeParseException e) {
                ReqFileLineDateTimeField.logger.error("Unable to parse DateTime field: {}", lineMap.get(name));
                value = lineMap.get(name).substring(0, 10);
            } catch (DateTimeException e) {
                ReqFileLineDateTimeField.logger.error("Unable to parse DateTime field: {}", lineMap.get(name));
                value = "00/00/00";
            }
        }
        else {
            throw new ReqFileLineFieldMissingException("Missing ReqFileLineField: " + name);
        }
    }

    public LocalDateTime getDateTimeValue() {
        return dateTimeValue;
    }
}
