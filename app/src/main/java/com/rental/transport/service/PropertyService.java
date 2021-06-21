package com.rental.transport.service;

import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.rental.transport.R;
import com.rental.transport.adapter.PropertyListAdapter;
import com.rental.transport.model.Property;

import java.util.List;

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

    public List<Property> getPropertyFromList(ListView listView) {

        PropertyListAdapter adapter = (PropertyListAdapter) listView.getAdapter();
        List<Property> data = adapter.getData();

        for (Integer id = 0; id < listView.getChildCount(); id++) {
            View view = listView.getChildAt(id);
            TextView name = view.findViewById(R.id.propertyLogic);
            EditText value = view.findViewById(R.id.propertyValue);
            setValue(
                    data,
                    name.getText().toString(),
                    value.getText().toString()
            );
        }

        return adapter.getData();
    }
}
