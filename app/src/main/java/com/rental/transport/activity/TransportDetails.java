package com.rental.transport.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransportDetails extends Fragment {

    private final int PICK_IMAGE_SELECTED = 100;
    private final int STORAGE_PERMISSION_CODE = 1;
    private Gallery gallery;
    private View root;
    private Button buttonDelete;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Transport transport = MemoryService.getInstance().getTransport();

        root = inflater.inflate(R.layout.transport_details, container, false);
        buttonDelete = root.findViewById(R.id.buttonDelete);
        gallery = root.findViewById(R.id.gallery);
        gallery.setAdapter(new TransportGalleryAdapter(getContext()));
        gallery.setOnItemClickListener((parent, view, position, id) -> {
            Long imageId = MemoryService.getInstance().getTransport().getImage().get(position);
            Bundle bundle = new Bundle();
            bundle.putLong("imageId", imageId);
            FragmentService.getInstance().get("PictureFragment").setArguments(bundle);
            FragmentService.getInstance().load(getActivity(), "PictureFragment");
        });

        if (transport.getImage().isEmpty())
            buttonDelete.setEnabled(false);

        ListView listView = root.findViewById(R.id.transportProperty);
        listView.setAdapter(new PropertyListAdapter(getContext(), transport.getProperty(), true));

        root
                .findViewById(R.id.transportMap)
                .setOnClickListener(view -> FragmentService.getInstance().load(getActivity(), "MapFragment"));

        root.findViewById(R.id.buttonDelete).setOnClickListener(v -> {

            Long selected = gallery.getSelectedItemId();
            Long imageId = (Long) gallery.getAdapter().getItem(selected.intValue());

            ProgresService.getInstance().showProgress(getContext(), getString(R.string.transport_saving));
            NetworkService
                    .getInstance()
                    .getTransportApi()
                    .doDropTransportImage(transport.getId(), imageId)
                    .enqueue(new Callback<Transport>() {
                        @Override
                        public void onResponse(Call<Transport> call, Response<Transport> response) {
                            ProgresService.getInstance().hideProgress();
                            if (response.isSuccessful()) {
                                MemoryService.getInstance().setTransport(response.body());
                                gallery.setAdapter(new TransportGalleryAdapter(getContext()));
                                gallery.invalidate();
                                if (response.body().getImage().isEmpty())
                                    buttonDelete.setEnabled(false);
                            }
                            else {
                                try {
                                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                                    Toast
                                            .makeText(getContext(), jObjError.getString("message"), Toast.LENGTH_LONG)
                                            .show();
                                }
                                catch (Exception e) {
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Transport> call, Throwable t) {
                            ProgresService.getInstance().hideProgress();
                            Toast
                                    .makeText(getActivity(), t.toString(), Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
        });

        root.findViewById(R.id.buttonSave).setOnClickListener(v -> {
//            transport.setProperty(PropertyService.getInstance().getPropertyFromList(listView));
            ProgresService.getInstance().showProgress(getContext(), getString(R.string.transport_saving));
            NetworkService
                    .getInstance()
                    .getTransportApi()
                    .doPutTransport(transport)
                    .enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            ProgresService.getInstance().hideProgress();
                            if (!response.isSuccessful()) {
                                try {
                                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                                    Toast
                                            .makeText(getContext(), jObjError.getString("message"), Toast.LENGTH_LONG)
                                            .show();
                                }
                                catch (Exception e) {
                                }
                            }
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                String permission = Manifest.permission.READ_EXTERNAL_STORAGE;
                if (ContextCompat.checkSelfPermission(getContext(), permission) == PackageManager.PERMISSION_GRANTED) {
                    Intent ringIntent = new Intent();
                    ringIntent.setType("image/*");
                    ringIntent.setAction(Intent.ACTION_GET_CONTENT);
                    ringIntent.addCategory(Intent.CATEGORY_OPENABLE);
                    this.startActivityForResult(Intent.createChooser(ringIntent, "Select Image"), PICK_IMAGE_SELECTED);
                }
                else {

                    ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, STORAGE_PERMISSION_CODE);
                }
            }
            else {
                Intent ringIntent = new Intent();
                ringIntent.setType("image/*");
                ringIntent.setAction(Intent.ACTION_GET_CONTENT);
                ringIntent.addCategory(Intent.CATEGORY_OPENABLE);
                this.startActivityForResult(Intent.createChooser(ringIntent, "Select Image"), PICK_IMAGE_SELECTED);
            }
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case PICK_IMAGE_SELECTED: {
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        Transport transport = MemoryService.getInstance().getTransport();
                        byte[] data = ImageService.getInstance().getImage(getContext(), imageReturnedIntent);
                        ProgresService.getInstance().showProgress(getContext(), getString(R.string.transport_saving));
                        NetworkService
                                .getInstance()
                                .getTransportApi()
                                .doAddTransportImage(transport.getId(), data)
                                .enqueue(new Callback<Transport>() {
                                    @Override
                                    public void onResponse(Call<Transport> call, Response<Transport> response) {
                                        ProgresService.getInstance().hideProgress();
                                        if (response.isSuccessful()) {
                                            MemoryService.getInstance().setTransport(response.body());
                                            gallery.setAdapter(new TransportGalleryAdapter(getContext()));
                                            gallery.invalidate();
                                            if (!response.body().getImage().isEmpty())
                                                buttonDelete.setEnabled(true);
                                        }
                                        else {
                                            try {
                                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                                Toast
                                                        .makeText(getContext(), jObjError.getString("message"), Toast.LENGTH_LONG)
                                                        .show();
                                            }
                                            catch (Exception e) {
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Transport> call, Throwable t) {
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
                break;
            }
        }
    }
}
