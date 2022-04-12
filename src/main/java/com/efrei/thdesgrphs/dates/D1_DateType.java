package com.efrei.thdesgrphs.dates;

public enum D1_DateType {
    SOONEST("Dates au plus tot"), LATEST("Dates au plus tard"), MARGINS("Marges");

    private String stringValue;

    D1_DateType(String stringValue) {
        this.stringValue = stringValue;
    }

    public String getStringValue() {
        return stringValue;
    }
}
