package com.bellin.erp.supplychain.deliveryticket.domain.file;


import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;

public class ReqFileLineDateTimeField extends ReqFileLineField {
    private LocalDateTime dateTimeValue;
    private static DateTimeFormatter parseFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yy");
    public ReqFileLineDateTimeField(String name, int width, String pad) {
        super(name, width, pad);

    }

    @Override
    public void read(Map<String, String> lineMap) throws Exception{
        if (lineMap.containsKey(this.name)) {
            try {
                parseFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
                this.dateTimeValue = LocalDateTime.parse(lineMap.get(this.name), parseFormatter);
                this.value = this.dateTimeValue.format(dateFormatter);

            } catch (DateTimeParseException e) {
                e.printStackTrace();
                this.value = lineMap.get(this.name).substring(0, 10);
            } catch (DateTimeException e) {
                e.printStackTrace();
                this.value = "00/00/00";
            }
        }
        else {
            // TODO
            throw new Exception("Data field " + this.name + " is missing!");
        }
    }

    public LocalDateTime getDateTimeValue() {
        return this.dateTimeValue;
    }
}
