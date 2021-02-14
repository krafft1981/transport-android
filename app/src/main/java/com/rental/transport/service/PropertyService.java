package com.rental.transport.service;

import android.content.Context;
import android.graphics.Typeface;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.rental.transport.R;
import com.rental.transport.model.Property;
import com.rental.transport.validator.IStringValidator;
import com.rental.transport.validator.ValidatorFactory;

import java.util.HashSet;
import java.util.Set;

public class PropertyService {

    private Context context;
    private static PropertyService mInstance;

    public PropertyService(Context context) {
        this.context = context;
    }

    public static PropertyService getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new PropertyService(context);
        }

        return mInstance;
    }

    public Property searchProperty(Set<Property> props, String name) throws IllegalArgumentException {

        for (Property prop : props) {
            if (prop.getLogicName().equals(name)) {
                return prop;
            }
        }

        throw new IllegalArgumentException("Property '" + name + "' not found");
    }

    public String getValue(Set<Property> props, String name) throws IllegalArgumentException {

        Property prop = searchProperty(props, name);
        return prop.getValue();
    }

    public void setValue(Set<Property> props, String name, String value) throws IllegalArgumentException {

        Property prop = searchProperty(props, name);
        prop.setValue(value);
    }

    public IStringValidator getValidator(Set<Property> props, String name) throws IllegalArgumentException {

        Property prop = searchProperty(props, name);
        ValidatorFactory factory = new ValidatorFactory();
        return factory.getValidator(prop.getType());
    }

    public void addTableRow(TableLayout table, Property property, Boolean editable) {

        TableRow row = new TableRow(context);

        TextView type = new TextView(context);
        type.setText(property.getType());
        type.setVisibility(View.GONE);

        TextView logic = new TextView(context);
        logic.setText(property.getLogicName());
        logic.setVisibility(View.GONE);

        TextView name = new TextView(context);
        name.setText(property.getHumanName());
        name.setSingleLine(false);

        EditText value = new EditText(context);
        value.setText(property.getValue());
        value.setInputType(editable ? InputType.TYPE_CLASS_TEXT : InputType.TYPE_NULL);
        value.setSingleLine(false);

        row.addView(type);
        row.addView(logic);
        row.addView(name);
        row.addView(value);

        table.addView(row);
    }

    public void setPropertyToTable(TableLayout table, Set<Property> property, Boolean editable) {

        for (Property prop : property)
            addTableRow(table, prop, editable);
    }

    public Set<Property> getPropertyFromTable(TableLayout table) {

        Set<Property> props = new HashSet<>();
        for (int id = 0; id < table.getChildCount(); id++) {

            View row = table.getChildAt(id);
            if (row instanceof TableRow) {

                TextView field0 = (TextView) ((TableRow) row).getChildAt(0);
                TextView field1 = (TextView) ((TableRow) row).getChildAt(1);
                TextView field2 = (TextView) ((TableRow) row).getChildAt(2);
                TextView field3 = (TextView) ((TableRow) row).getChildAt(3);

                if (!field0.getText().equals("Calculated")) {

                    ValidatorFactory factory = new ValidatorFactory();
                    IStringValidator validator = factory.getValidator(field0.getText().toString());
                    if (validator.validate(field3.getText().toString())) {
                        Property property = new Property(
                                field0.getText().toString(),
                                field1.getText().toString(),
                                field2.getText().toString(),
                                field3.getText().toString()
                        );
                        props.add(property);
                    }
                    else {
                        field3.requestFocus();
                        field3.setError(context.getString(R.string.wrong_data_entered));
                    }
                }
            }
        }

        return props;
    }
}
