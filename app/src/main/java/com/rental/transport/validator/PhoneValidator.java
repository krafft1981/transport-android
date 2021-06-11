package com.rental.transport.validator;

import android.text.TextUtils;

import java.util.regex.Pattern;

public class PhoneValidator implements IStringValidator {

    private Pattern pattern = Pattern.compile("^[7-8]\\d{10}");

    @Override
    public Boolean validate(String value) {

        if (TextUtils.isEmpty(value)) {
            return false;
        }

//        return pattern.matcher(value).matches();
        return !TextUtils.isEmpty(value);
    }
}
