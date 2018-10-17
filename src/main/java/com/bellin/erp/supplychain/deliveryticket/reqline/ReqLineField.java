package com.bellin.erp.supplychain.deliveryticket.reqline;

public class ReqLineField {
    private int index;
    private String name;
    private String value;
    private int width;

    public ReqLineField(int index, String name, String value, int width) {
        this.index = index;
        this.name = name;
        this.value = value;
        this.width = width;
    }

    public int getIndex() {
        return this.index;
    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }

    public int getWidth() {
        return width;
    }
}
