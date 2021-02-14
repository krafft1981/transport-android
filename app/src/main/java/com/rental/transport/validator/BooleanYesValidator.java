package com.rental.transport.validator;

import android.text.TextUtils;

import java.util.regex.Pattern;

public class BooleanYesValidator implements IStringValidator {

    private Pattern pattern = Pattern.compile("[Дд][Аа]|[Tt][Rr][Uu][Ee]|1|[Yy][Ee][Ss]");

    @Override
    public Boolean validate(String value) {

        if (TextUtils.isEmpty(value)) {
            return false;
        }

        return pattern.matcher(value).matches();
    }
}
