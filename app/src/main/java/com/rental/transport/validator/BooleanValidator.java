package com.rental.transport.validator;

import android.text.TextUtils;

import java.util.regex.Pattern;

public class BooleanValidator implements IStringValidator {

    private Pattern pattern = Pattern.compile("[Нн][Ее][Тт]|[Дд][Аа]|[Tt][Rr][Uu][Ee]|[Ff][Aa][Ll][Ss][Ee]|0|1|[Yy][Ee][Ss]|[Nn][Oo]");

    @Override
    public Boolean validate(String value) {

        if (TextUtils.isEmpty(value)) {
            return false;
        }

        return pattern.matcher(value).matches();
    }
}
