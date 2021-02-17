package com.rental.transport.service;

import android.content.Context;
import android.text.InputType;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.rental.transport.R;
import com.rental.transport.model.Property;
import com.rental.transport.validator.IStringValidator;
import com.rental.transport.validator.ValidatorFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PropertyService {

    private Context context;
    private static PropertyService mInstance;

    public PropertyService(Context context) {
        this.context = context;
    }

    public static PropertyService getInstance(Context context) {

        if (mInstance == null)
            mInstance = new PropertyService(context);

        return mInstance;
    }

    public static PropertyService getInstance() {

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

        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TableRow row = (TableRow) inflater.inflate(R.layout.property_element, null);

        TextView logic = (TextView) row.findViewById(R.id.propertyLogic);
        logic.setText(property.getLogicName());

        TextView name = (TextView) row.findViewById(R.id.propertyName);
        name.setText(property.getHumanName());
        name.setSingleLine(false);

        EditText value = (EditText) row.findViewById(R.id.propertyValue);
        value.setText(property.getValue());
        value.setInputType(editable ? InputType.TYPE_CLASS_TEXT : InputType.TYPE_NULL);
        value.setSingleLine(false);

        table.addView(row);
    }

    public PropertyService setPropertyToList(ListView listView, List<Property> properties, Boolean editable) {

//        table.removeAllViews();
//
//        for (Property prop : properties)
//            addTableRow(table, prop, editable);

        return this;
    }

    public PropertyService setPropertyToView(TableLayout table, Property property) {

        addTableRow(table, property, false);
        return this;
    }

    public Set<Property> getPropertyFromTable(TableLayout table) {

        Set<Property> props = new HashSet<>();
        for (int id = 0; id < table.getChildCount(); id++) {

//            View row = table.getChildAt(id);
//            if (row instanceof TableRow) {
//
//                TextView field0 = (TextView) ((TableRow) row).getChildAt(0);
//                TextView field1 = (TextView) ((TableRow) row).getChildAt(1);
//                TextView field2 = (TextView) ((TableRow) row).getChildAt(2);
//                TextView field3 = (TextView) ((TableRow) row).getChildAt(3);
//
//                if (!field0.getText().equals("Calculated")) {
//
//                    ValidatorFactory factory = new ValidatorFactory();
//                    IStringValidator validator = factory.getValidator(field0.getText().toString());
//                    if (validator.validate(field3.getText().toString())) {
//                        Property property = new Property(
//                                field0.getText().toString(),
//                                field1.getText().toString(),
//                                field2.getText().toString(),
//                                field3.getText().toString()
//                        );
//                        props.add(property);
//                    }
//                    else {
//                        field3.requestFocus();
//                        field3.setError(context.getString(R.string.wrong_data_entered));
//                    }
//                }
//            }
        }

        return props;
    }
}
