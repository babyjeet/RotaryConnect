package com.android.aquagiraffe.rotaryconnect;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapView extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMarkerDragListener,
        GoogleMap.OnMapLongClickListener,
        View.OnClickListener {

    SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private double longitude;
    private double latitude;

    ArrayList<LatLng> points;
    private ImageButton btnAddress;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);

        points = new ArrayList<LatLng>();

        btnAddress = (ImageButton) findViewById(R.id.buttonAddress);

        btnAddress.setOnClickListener(this);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mMap = mapFragment.getMap();
        mMap.setTrafficEnabled(true);
        mMap.setBuildingsEnabled(true);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(19.1941911, 72.9530811))
                        .title("TMA, Hall, Near Dwarka Hotel, Wagle Estate, Thane West")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(19.1941911, 72.9530811), 12));
                enableMyLocationIfPermitted();

            }
        });

    }

    private void enableMyLocationIfPermitted() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else if (mMap != null) {
            mMap.setMyLocationEnabled(true);

        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        LatLng latLng = new LatLng(19.4540, 73.3709);
        mMap.addMarker(new MarkerOptions().position(latLng).draggable(true));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.setOnMarkerDragListener(this);
        mMap.setOnMapLongClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if (v==btnAddress)
        {
            getAddress();
        }
    }

    private void getAddress() {
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(19.1941911, 72.9530811))
                        .title("TMA, Hall, Near Dwarka Hotel, Wagle Estate, Thane West")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(19.1941911, 72.9530811), 10));


            }
        });
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        mMap.clear();
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .draggable(true)
        );
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        latitude = marker.getPosition().latitude;
        longitude = marker.getPosition().longitude;
        //  moveMap();

    }
}
