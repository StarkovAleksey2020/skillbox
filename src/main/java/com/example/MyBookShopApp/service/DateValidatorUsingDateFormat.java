package com.example.MyBookShopApp.service;

import org.apache.commons.validator.routines.DateValidator;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateValidatorUsingDateFormat extends DateValidator {
    private String dateFormat;

    public DateValidatorUsingDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    @Override
    public boolean isValid(String value) {
        DateFormat sdf = new SimpleDateFormat(this.dateFormat);
        sdf.setLenient(false);
        try {
            sdf.parse(value);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }
}
