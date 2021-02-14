package com.rental.transport.validator;

import android.text.TextUtils;

import java.util.regex.Pattern;

public class IntegerValidator implements IStringValidator {

    private Pattern pattern = Pattern.compile("[0-9]+");

    @Override
    public Boolean validate(String value) {

        if (TextUtils.isEmpty(value)) {
            return false;
        }

        return pattern.matcher(value).matches();
    }
}
