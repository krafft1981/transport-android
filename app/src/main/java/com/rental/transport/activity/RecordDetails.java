package com.rental.transport.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.rental.transport.R;
import com.rental.transport.model.Event;
import com.rental.transport.service.FragmentService;
import com.rental.transport.service.MemoryService;
import com.rental.transport.service.NetworkService;

public class RecordDetails extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.record_details, container, false);

        root.findViewById(R.id.buttonSave).setOnClickListener(v -> {

            //update record
            NetworkService
                    .getInstance()
                    .getOrderApi();

            FragmentService.getInstance().load(getActivity(), "CalendarFragment");
        });

        root.findViewById(R.id.buttonDelete).setOnClickListener(v -> {

            Event event = MemoryService
                    .getInstance()
                    .getEvent();

//            NetworkService
//                    .getInstance()
//                    .getOrderApi()
//                    .doDeleteAbsentCustomer(0);

            //delete record
            FragmentService.getInstance().load(getActivity(), "CalendarFragment");
        });

        return root;
    }
}
