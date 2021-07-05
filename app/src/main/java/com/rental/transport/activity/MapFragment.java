package com.rental.transport.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rental.transport.R;
import com.rental.transport.model.Parking;
import com.rental.transport.model.Transport;
import com.rental.transport.service.FragmentService;
import com.rental.transport.service.MemoryService;
import com.rental.transport.service.NetworkService;
import com.rental.transport.service.ProgresService;
import com.rental.transport.service.PropertyService;
import java.util.List;
import lombok.NonNull;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapFragment extends Fragment {

    MapView view;

    private void loadParkig(Long parkingId) {

        ProgresService.getInstance().showProgress(getContext(), getString(R.string.parking_loading));
        NetworkService
                .getInstance()
                .getParkingApi()
                .doGetParking(parkingId)
                .enqueue(new Callback<Parking>() {
                    @Override
                    public void onResponse(@NonNull Call<Parking> call, @NonNull Response<Parking> response) {
                        ProgresService.getInstance().hideProgress();
                        if (response.code() == 401)
                            FragmentService.getInstance().load(getActivity(), "CustomerLogin");

                        else {
                            if (response.isSuccessful()) {

                                view.getMapAsync(mMap -> {

                                    LatLng place = new LatLng(
                                            Double.parseDouble(PropertyService.getInstance().getValue(response.body().getProperty(), "parking_latitude")),
                                            Double.parseDouble(PropertyService.getInstance().getValue(response.body().getProperty(), "parking_longitude"))
                                    );

                                    mMap.addMarker(new MarkerOptions()
                                            .position(place)
                                            .title(PropertyService.getInstance().getValue(response.body().getProperty(), "parking_name"))
                                            .snippet(PropertyService.getInstance().getValue(response.body().getProperty(), "parking_description"))
                                    );

                                    // For zooming automatically to the location of the marker
                                    CameraPosition cameraPosition = new CameraPosition.Builder().target(place).zoom(12).build();
                                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                                });
                            }
                            else {
                                try {
                                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                                    Toast
                                            .makeText(getContext(), jObjError.getString("message"), Toast.LENGTH_LONG)
                                            .show();
                                } catch (Exception e) {
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Parking> call, @NonNull Throwable t) {
                        ProgresService.getInstance().hideProgress();
                        Toast
                                .makeText(getActivity(), t.toString(), Toast.LENGTH_LONG)
                                .show();
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.map_fragment, container, false);

        view = rootView.findViewById(R.id.mapView);
        view.onCreate(savedInstanceState);

        view.onResume();

        try {
            MapsInitializer.initialize(getContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Transport transport = MemoryService.getInstance().getTransport();
        List<Long> parkings = transport.getParking();
        if (!parkings.isEmpty()) {
            Long parkingId = parkings.get(0);
            loadParkig(parkingId);
        }

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        view.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        view.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        view.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        view.onLowMemory();
    }
}