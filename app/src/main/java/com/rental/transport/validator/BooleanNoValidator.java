package com.rental.transport.validator;

import android.text.TextUtils;

import java.util.regex.Pattern;

public class BooleanNoValidator implements IStringValidator {

    private Pattern pattern = Pattern.compile("[Нн][Ее][Тт]|[Ff][aA][Ll][Ss][Ee]|0|[Nn][Oo]");

    @Override
    public Boolean validate(String value) {

        if (TextUtils.isEmpty(value)) {
            return false;
        }

        return pattern.matcher(value).matches();
    }
}
