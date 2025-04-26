package com.kush.sas.log.check.constant;

import java.util.Date;

public final class Message {
    private final String simpleFileName;
    private final Date dateOfCreation;

    private String valueFromLog;

    public Message(String simpleFileName, long formattedTime) {
        this.simpleFileName = simpleFileName;
        this.dateOfCreation = new Date(formattedTime);
    }

    public Message(String simpleFileName, long formattedTime, String valueFromLog) {
        this.simpleFileName = simpleFileName;
        this.dateOfCreation = new Date(formattedTime);
        this.valueFromLog = valueFromLog;
    }

    public String getSimpleFileName() {
        return simpleFileName;
    }

    public Date getDateOfCreation() {
        return dateOfCreation;
    }

    public String getValueFromLog() {
        return valueFromLog;
    }

    @Override
    public String toString() {
        return simpleFileName + " " + dateOfCreation + " " + valueFromLog;
    }
}
