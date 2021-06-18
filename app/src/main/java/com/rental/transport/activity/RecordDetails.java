package com.rental.transport.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.rental.transport.R;
import com.rental.transport.service.FragmentService;

public class RecordDetails extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.record_details, container, false);
        root.findViewById(R.id.buttonSave).setOnClickListener(v -> FragmentService.getInstance().load(getActivity(), "CalendarFragment"));
        root.findViewById(R.id.buttonDelete).setOnClickListener(v -> FragmentService.getInstance().load(getActivity(), "CalendarFragment"));
        return root;
    }
}
