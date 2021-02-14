package com.rental.transport.validator;

import android.text.TextUtils;

public class StringValidator implements IStringValidator {

    @Override
    public Boolean validate(String value) {

        return !TextUtils.isEmpty(value);
    }
}
