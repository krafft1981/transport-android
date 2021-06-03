package com.rental.transport.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.rental.transport.R;
import com.rental.transport.service.ImageService;
import com.rental.transport.service.MemoryService;

public class PictureFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.picture_fragment, container, false);
        ImageView image = root.findViewById(R.id.image);
        Long imageId = MemoryService
                .getInstance()
                .getImageId();

        ImageService
                .getInstance()
                .setImage(getContext(), imageId, R.drawable.background, image);

        return root;
    }
}
