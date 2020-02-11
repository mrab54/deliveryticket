package com.bellin.erp.supplychain.deliveryticket.domain.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UFFileLineStringField extends UFFileLineField {

    private static final Logger logger = LoggerFactory.getLogger(UFFileLineStringField.class);

    public UFFileLineStringField(String name, int width, String pad) {
        super(name, width, pad);
    }
}
