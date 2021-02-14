package com.rental.transport.validator;

import android.text.TextUtils;

import java.util.regex.Pattern;

public class HourValidator implements IStringValidator {

    private Pattern pattern = Pattern.compile("([01]?[0-9]|2[0-3])");

    @Override
    public Boolean validate(String value) {

        if (TextUtils.isEmpty(value)) {
            return false;
        }

        return pattern.matcher(value).matches();
    }
}
