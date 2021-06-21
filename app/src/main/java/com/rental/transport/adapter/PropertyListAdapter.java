package com.rental.transport.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.rental.transport.R;
import com.rental.transport.model.Property;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PropertyListAdapter extends BaseAdapter {

    private Context context;

    private List<Property> data;
    private Boolean editable;

    public PropertyListAdapter(Context context, List<Property> data, Boolean editable) {
        this.context = context;
        this.data = data;
        this.editable = editable;

        Collections.sort(this.data, (Comparator) (o1, o2) -> {
            Property p1 = (Property) o1;
            Property p2 = (Property) o2;
            return p1.getId().compareTo(p2.getId());
        });
    }

    public List<Property> getData() {

        System.out.println(data.toString());
        return data;
    }

    public class ViewHolder {
        TextView type;
        TextView logic;
        TextView name;
        EditText value;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        PropertyListAdapter.ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.property_element, parent, false);
            holder = new PropertyListAdapter.ViewHolder();

            holder.type = convertView.findViewById(R.id.propertyType);
            holder.logic = convertView.findViewById(R.id.propertyLogic);
            holder.name = convertView.findViewById(R.id.propertyName);
            holder.value = convertView.findViewById(R.id.propertyValue);
            if (!editable)
                holder.value.setKeyListener(null);

            convertView.setTag(holder);
        } else {
            holder = (PropertyListAdapter.ViewHolder) convertView.getTag();
        }

        Property property = data.get(position);

        holder.type.setText(property.getType());
        holder.logic.setText(property.getLogicName());
        holder.name.setText(property.getHumanName());
        holder.value.setText(property.getValue());

        return convertView;
    }
}
