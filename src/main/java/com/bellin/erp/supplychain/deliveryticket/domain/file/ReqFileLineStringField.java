package com.bellin.erp.supplychain.deliveryticket.domain.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReqFileLineStringField extends ReqFileLineField {

    private static final Logger logger = LoggerFactory.getLogger(ReqFileLineStringField.class);

    public ReqFileLineStringField(String name, int width, String pad) {
        super(name, width, pad);
    }
}
