package com.rental.transport.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ListView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.rental.transport.R;
import com.rental.transport.adapter.PropertyListAdapter;
import com.rental.transport.adapter.TransportGalleryAdapter;
import com.rental.transport.model.Transport;
import com.rental.transport.service.FragmentService;
import com.rental.transport.service.ImageService;
import com.rental.transport.service.MemoryService;
import com.rental.transport.service.NetworkService;
import com.rental.transport.service.ProgresService;
import com.rental.transport.service.PropertyService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransportDetails extends Fragment {

    private int currentImage = 0;
    private final int PICK_IMAGE_SELECTED = 100;
    private final int STORAGE_PERMISSION_CODE = 101;
    private Gallery gallery;
    private View root;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.transport_details, container, false);
        Transport transport = MemoryService.getInstance().getTransport();
        gallery = root.findViewById(R.id.gallery);
        gallery.setAdapter(new TransportGalleryAdapter(getContext(), transport.getImage()));
        gallery.setOnItemClickListener((parent, view, position, id) -> {
            MemoryService.getInstance().setImageId(transport.getImage().get(position));
            FragmentService.getInstance().load(getActivity(), "PictureFragment");
        });

        if (transport.getImage().size() == 0)
            root.findViewById(R.id.buttonDelete).setEnabled(false);

        gallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentImage = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                currentImage = 0;
            }
        });

        ListView listView = root.findViewById(R.id.property);
        listView.setAdapter(new PropertyListAdapter(getContext(), transport.getProperty(), true));

        root
                .findViewById(R.id.transportMap)
                .setOnClickListener(view -> FragmentService.getInstance().load(getActivity(), "MapFragment"));

        root.findViewById(R.id.buttonDelete).setOnClickListener(v -> {

            transport.getImage().remove(transport.getImage().get(currentImage));
            gallery.setAdapter(new TransportGalleryAdapter(getContext(), transport.getImage()));
            gallery.invalidate();

            transport.setProperty(PropertyService.getInstance().getPropertyFromList(listView));
            ProgresService.getInstance().showProgress(getContext(), getString(R.string.transport_saving));
            NetworkService
                    .getInstance()
                    .getTransportApi()
                    .doPutTransport(transport)
                    .enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            ProgresService.getInstance().hideProgress();
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            ProgresService.getInstance().hideProgress();
                            Toast
                                    .makeText(getActivity(), t.toString(), Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

            if (transport.getImage().isEmpty())
                v.setEnabled(false);
        });

        root.findViewById(R.id.buttonSave).setOnClickListener(v -> {

            transport.setProperty(PropertyService.getInstance().getPropertyFromList(listView));
            ProgresService.getInstance().showProgress(getContext(), getString(R.string.transport_saving));
            NetworkService
                    .getInstance()
                    .getTransportApi()
                    .doPutTransport(transport)
                    .enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            ProgresService.getInstance().hideProgress();
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            ProgresService.getInstance().hideProgress();
                            Toast
                                    .makeText(getActivity(), t.toString(), Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
        });

        root.findViewById(R.id.buttonLoad).setOnClickListener(v -> {

            String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
            if (ContextCompat.checkSelfPermission(getContext(), permission) == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, 10);

                Toast
                        .makeText(getActivity(), "no transport permissions", Toast.LENGTH_LONG)
                        .show();
                return;
            }

            getFile();
        });

        return root;
    }

    private void getFile() {
        Intent ringIntent = new Intent();
        ringIntent.setType("image/*");
        ringIntent.setAction(Intent.ACTION_GET_CONTENT);
        ringIntent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(ringIntent, "Select Image"), PICK_IMAGE_SELECTED);
    }

    //Обрабатываем результат выбора в галерее:
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case PICK_IMAGE_SELECTED:
                if (resultCode == Activity.RESULT_OK) {
                    Transport transport = MemoryService.getInstance().getTransport();
                    try {
                        String data = ImageService.getInstance().getImage(getContext(), imageReturnedIntent);
                        ProgresService.getInstance().showProgress(getContext(), getString(R.string.transport_saving));
                        NetworkService
                                .getInstance()
                                .getImageApi()
                                .doPostImage(data)
                                .enqueue(new Callback<Long>() {
                                    @Override
                                    public void onResponse(Call<Long> call, Response<Long> response) {
                                        ProgresService.getInstance().hideProgress();
                                        transport.getImage().add(response.body());
                                        NetworkService
                                                .getInstance()
                                                .getTransportApi()
                                                .doPutTransport(transport)
                                                .enqueue(new Callback<Void>() {
                                                    @Override
                                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                                        ProgresService.getInstance().hideProgress();
                                                        gallery.setAdapter(new TransportGalleryAdapter(getContext(), transport.getImage()));
                                                        gallery.invalidate();
                                                        if (transport.getImage().size() != 0)
                                                            root.findViewById(R.id.buttonDelete).setEnabled(true);
                                                    }

                                                    @Override
                                                    public void onFailure(Call<Void> call, Throwable t) {
                                                        ProgresService.getInstance().hideProgress();
                                                        Toast
                                                                .makeText(getActivity(), t.toString(), Toast.LENGTH_LONG)
                                                                .show();
                                                    }
                                                });
                                    }

                                    @Override
                                    public void onFailure(Call<Long> call, Throwable t) {
                                        ProgresService.getInstance().hideProgress();
                                        Toast
                                                .makeText(getActivity(), t.toString(), Toast.LENGTH_LONG)
                                                .show();
                                    }
                                });
                    }
                    catch (Exception e) {
                    }
                }
        }
    }
}
