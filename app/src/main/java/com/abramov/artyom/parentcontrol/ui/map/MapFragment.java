package com.abramov.artyom.parentcontrol.ui.map;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abramov.artyom.parentcontrol.R;
import com.abramov.artyom.parentcontrol.domain.Location;
import com.abramov.artyom.parentcontrol.services.LocationService;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.LinkedList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.Subscription;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List<Subscription> mSubscribes = new LinkedList<>();
    private Realm mRealm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_map_main, container, false);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_main);
        mapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        startLocationService();
        subscribeToRealm();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();

        stopLocationService();
        unsubscribeFromRealm();
    }

    @Override
    public void onPause() {
        super.onPause();
    }



    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        }


    }

    private void startLocationService() {
        Intent serviceIntent = new Intent(getContext(), LocationService.class);
        serviceIntent.putExtra(LocationService.EXTRA_IS_SERVER, false);
        getContext().startService(serviceIntent);
    }

    private void stopLocationService() {
        Intent serviceIntent = new Intent(getContext(), LocationService.class);
        getContext().stopService(serviceIntent);
    }

    private void subscribeToRealm() {
        mRealm = Realm.getDefaultInstance();

        mSubscribes.add(mRealm.where(Location.class)
                .findAll()
                .asObservable()
                .subscribe(this::updateMapMarkers));
    }

    private void unsubscribeFromRealm() {
        for (Subscription subscription : mSubscribes) {
            subscription.unsubscribe();
            mSubscribes.remove(subscription);
        }
        mRealm.close();
    }

    private void updateMapMarkers(RealmResults<Location> locations) {
        for (Location location : locations) {
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(new LatLng(
                    location.getLatitude(),
                    location.getLongitude()))
                    .title(location.getTitle()));
        }
    }

}
