package com.rental.transport.service;

import android.widget.ListView;
import android.widget.TableLayout;

import com.rental.transport.model.Property;
import com.rental.transport.validator.IStringValidator;
import com.rental.transport.validator.ValidatorFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PropertyService {

    private static PropertyService mInstance;

    public PropertyService() {
    }

    public static PropertyService getInstance() {

        if (mInstance == null)
            mInstance = new PropertyService();

        return mInstance;
    }

    public Property searchProperty(List<Property> props, String name) throws IllegalArgumentException {

        for (Property prop : props) {
            if (prop.getLogicName().equals(name)) {
                return prop;
            }
        }

        throw new IllegalArgumentException("Property '" + name + "' not found");
    }

    public String getValue(List<Property> props, String name) throws IllegalArgumentException {

        Property prop = searchProperty(props, name);
        return prop.getValue();
    }

    public void setValue(List<Property> props, String name, String value) throws IllegalArgumentException {

        Property prop = searchProperty(props, name);
        prop.setValue(value);
    }

    public IStringValidator getValidator(List<Property> props, String name) throws IllegalArgumentException {

        Property prop = searchProperty(props, name);
        ValidatorFactory factory = new ValidatorFactory();
        return factory.getValidator(prop.getType());
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

    public List<Property> getProperty(ListView listView) {
        return new ArrayList<>();
    }
}
