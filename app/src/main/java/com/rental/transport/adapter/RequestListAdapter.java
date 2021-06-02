package com.rental.transport.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.rental.transport.model.Event;

import java.util.Map;

public class RequestListAdapter extends BaseAdapter {

    private Context context;
    private Map<Integer, Event> data;

    public RequestListAdapter(Context context, Map<Integer, Event> data) {
        this.context = context;
        this.data = data;
    }

    public class ViewHolder {
        TextView type;
        TextView logic;
        TextView name;
        EditText value;
    }

    public Map<Integer, Event> getdata() {
        return data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int id) {
        return data.get(id);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

//        PropertyListAdapter.ViewHolder holder;
//        if (convertView == null) {
//            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            convertView = inflater.inflate(R.layout.property_element, parent, false);
//            holder = new PropertyListAdapter.ViewHolder();
//
//            holder.type = convertView.findViewById(R.id.propertyType);
//            holder.logic = convertView.findViewById(R.id.propertyLogic);
//            holder.name = convertView.findViewById(R.id.propertyName);
//            holder.value = convertView.findViewById(R.id.propertyValue);
//            if (!editable) {
//                holder.value.setKeyListener(null);
//            }
//            else {
//                holder.value.setOnFocusChangeListener((v, hasFocus) -> {
//                    if (!hasFocus) {
//                        IStringValidator validator = vf.getValidator(holder.type.getText().toString());
//                        if (!validator.validate(holder.value.getText().toString()))
//                            holder.value.setError(context.getString(R.string.error));
//                        else
//                            data.get(position).setValue(holder.value.getText().toString());
//                    }
//                });
//            }
//
//            convertView.setTag(holder);
//        }
//        else {
//            holder = (PropertyListAdapter.ViewHolder) convertView.getTag();
//        }
//
//        Property property = data.get(position);
//
//        holder.type.setText(property.getType());
//        holder.logic.setText(property.getLogicName());
//        holder.name.setText(property.getHumanName());
//        holder.value.setText(property.getValue());

        return convertView;
    }
}
