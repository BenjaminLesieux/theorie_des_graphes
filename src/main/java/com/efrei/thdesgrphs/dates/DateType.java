package com.efrei.thdesgrphs.dates;

public enum DateType {
    SOONEST("Dates au plus tot"), LATEST("Dates au plus tard"), MARGINS("Marges");

    private String stringValue;

    DateType(String stringValue) {
        this.stringValue = stringValue;
    }

    public String getStringValue() {
        return stringValue;
    }
}
