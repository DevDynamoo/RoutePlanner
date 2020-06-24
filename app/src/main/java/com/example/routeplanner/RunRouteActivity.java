package com.example.routeplanner;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Stack;

public class RunRouteActivity extends FragmentActivity
        implements OnMapReadyCallback {

    private static final String TAG = RunRouteActivity.class.getSimpleName();
    public static final String EXTRA_MESSAGE_ROUTE_IS_CYCLIC = "com.example.routeplanner.IS_CYCLIC";
    public static final String EXTRA_MESSAGE_ROUTE_POSITIONS = "com.example.routeplanner.ROUTE_POSITIONS";
    public static final String EXTRA_MESSAGE_ROUTE_DISTANCE = "com.example.routeplanner.ROUTE_DISTANCE";


    private final LatLng defaultLocation = new LatLng(55.7858105, 12.5195605);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private GoogleMap map;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location lastKnownLocation;

    private String positions;
    private boolean isCyclic;
    private boolean locationPermissionGranted;

    public float distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_route);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras != null) {
                positions = extras.getString(EXTRA_MESSAGE_ROUTE_POSITIONS);
                isCyclic = extras.getBoolean(EXTRA_MESSAGE_ROUTE_IS_CYCLIC);
                distance = extras.getFloat(EXTRA_MESSAGE_ROUTE_DISTANCE);
            }
        } else {
            positions = (String) savedInstanceState.getSerializable(EXTRA_MESSAGE_ROUTE_POSITIONS);
            isCyclic = (boolean) savedInstanceState.getSerializable(EXTRA_MESSAGE_ROUTE_IS_CYCLIC);
            distance = (float) savedInstanceState.getSerializable(EXTRA_MESSAGE_ROUTE_DISTANCE);
        }
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.run_map);
        mapFragment.getMapAsync((OnMapReadyCallback) this);

    }
    /*
     * Method taken from Maps SDK for Android - Documentation
     * https://developers.google.com/maps/documentation/android-sdk/intro
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;

        // Location setup
        getLocationPermission();
        updateLocationUI();

        // Generates the selected route's markers
        generateMarkers();

    }

    private void generateMarkers() {
        String[] coordinateArray = positions.split(";");


        for (String s : coordinateArray) {
            Log.d(TAG, "Coordinate: " + s);
        }
        Stack<Marker> markerStack = new Stack<>();
        LatLngBounds.Builder bulider = new LatLngBounds.Builder();

        for (int index = 0; index < coordinateArray.length; index ++) {
            MarkerOptions newMarkerOptions = new MarkerOptions()
                    .position(new LatLng(
                            Double.parseDouble(coordinateArray[index].split(",")[0]),
                            Double.parseDouble(coordinateArray[index].split(",")[1]) ))
                    .draggable(false);
            Marker newMarker = map.addMarker(newMarkerOptions);

            //Calculating the markers to get their position and zoom
            bulider.include(newMarker.getPosition());
            LatLngBounds bounds = bulider.build();
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, DEFAULT_ZOOM);
            map.animateCamera(cu);

            // Creates a line between the given points
            if (!markerStack.isEmpty()) {
                Polyline line = map.addPolyline(new PolylineOptions()
                        .add(markerStack.peek().getPosition(), newMarker.getPosition())
                        .width(10)
                        .color(Color.RED));
            }
            markerStack.push(newMarker);
        }
        if (isCyclic) {
            Polyline line = map.addPolyline(new PolylineOptions()
                    .add(markerStack.peek().getPosition(), markerStack.get(0).getPosition())
                    .width(10)
                    .color(Color.RED));
        }

    }
    /*
     * Method taken from Maps SDK for Android - Documentation
     * https://developers.google.com/maps/documentation/android-sdk/intro
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }
    /*
     * Method taken from Maps SDK for Android - Documentation
     * https://developers.google.com/maps/documentation/android-sdk/intro
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    /*
     * Method taken from Maps SDK for Android - Documentation
     * https://developers.google.com/maps/documentation/android-sdk/intro
     */
    private void updateLocationUI() {
        if (map == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                map.setMyLocationEnabled(false);
                map.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    public float getDistance() {
        return distance;
    }
}