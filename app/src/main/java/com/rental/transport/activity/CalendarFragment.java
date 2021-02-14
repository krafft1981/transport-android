package com.rental.transport.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.fragment.app.Fragment;

import com.rental.transport.R;
import com.rental.transport.adapter.CalendarGridAdapter;

import java.util.ArrayList;

public class CalendarFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        Bundle bundle = getArguments();
//        if (bundle != null) {
//            String msg = bundle.getString("name");
//            if (msg != null) {
//            }
//        }

        View root = inflater.inflate(R.layout.calendar_fragment, container, false);
        GridView grid = root.findViewById(R.id.calendar_gridview);

        ArrayList<CalendarGridAdapter.ViewHolder> data = new ArrayList();

        CalendarGridAdapter adapter = new CalendarGridAdapter(getActivity(), data);
        grid.setAdapter(adapter);
        return root;
    }
}
