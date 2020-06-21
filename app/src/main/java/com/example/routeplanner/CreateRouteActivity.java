package com.example.routeplanner;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.graphics.Color;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.Stack;

public class CreateRouteActivity extends FragmentActivity
        implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private static final String TAG = CreateRouteActivity.class.getSimpleName();
    private GoogleMap map;
    private CameraPosition cameraPosition;
    private Stack<Marker> markerStack = new Stack<Marker>();
    private Stack<Polyline> polylineStack = new Stack<Polyline>();
    private CheckBox cycleCheckBox;

    private ArrayList<Float> routeLength;
    private Polyline cycleLine;

    // The entry point to the Places API.
    private PlacesClient mPlacesClient;

    private final String TAG2 = "Route";

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;

    // A default location (DTU, Denmark) and default zoom to use when location permission is
    // not granted.
    private final LatLng defaultLocation = new LatLng(55.7858105, 12.5195605);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean locationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location lastKnownLocation;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    private GoogleMap mMap;
    ArrayList<LatLng> arrayList = new ArrayList<LatLng>();
    Polyline line;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        setContentView(R.layout.activity_create_route);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        cycleCheckBox = findViewById(R.id.checkBox);
        cycleCheckBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked() && markerStack.size() > 1) {
                    if (markerStack.size() == 2) {
                        routeLength.add(0f);
                    } else {
                        float cycleDist = getDistanceBetweenMarkers(markerStack.get(0), markerStack.peek());
                        routeLength.add(cycleDist);
                    }
                    updateCalcLengthText();
                    createCycleLineBetweenFirstAndLast();
                } else if (markerStack.size() > 1) {
                    routeLength.remove(routeLength.size()-1);
                    updateCalcLengthText();
                    cycleLine.remove();
                }
            }
        });

        routeLength = new ArrayList<>();

        // Construct a FusedLocationProviderClient
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Construct a PlacesClient
        Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));
        mPlacesClient = Places.createClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync((OnMapReadyCallback) this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    protected void createCycleLineBetweenFirstAndLast() {
        cycleLine = map.addPolyline(new PolylineOptions()
                .add(markerStack.get(0).getPosition(), markerStack.peek().getPosition())
                .width(10)
                .color(Color.RED));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;

        initializeListeners();

        map.setOnMarkerClickListener(this);
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();
    }

    private void initializeListeners() {

        // Sets a listener on the map to handle the event of
        // the user long pressing anywhere on the map that is not a marker
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                if (markerStack.size() != 0) {
                    markerStack.peek().setDraggable(false);
                }

                // Creates new marker at the pressed point
                MarkerOptions newMarkerOptions = new MarkerOptions()
                        .position(latLng)
                        .title("New marker")
                        .draggable(true);
                Marker newMarker = map.addMarker(newMarkerOptions);

                // Creates a line between the given points
                if (!markerStack.isEmpty()) {
                    Polyline line = map.addPolyline(new PolylineOptions()
                            .add(markerStack.peek().getPosition(), latLng)
                            .width(10)
                            .color(Color.RED));
                    polylineStack.push(line);
                }

                Toast.makeText(getApplicationContext(), "New marker added" ,Toast.LENGTH_SHORT).show();

                // Push current position and marker on top of respective stacks
                Log.d(TAG, "Size of stack: " + markerStack.size());
                if (markerStack.size() >= 1) {
                    float result = getDistanceBetweenMarkers(newMarker, markerStack.peek());

                    // If checkbox is checked, replace final cycle length
                    // with new final cycle length
                    if ( cycleCheckBox.isChecked() ) {
                        if (markerStack.size() > 1) {
                            routeLength.remove(routeLength.size()-1);
                        }

                        routeLength.add(result);
                        float newDist = getDistanceBetweenMarkers(markerStack.get(0), newMarker);
                        routeLength.add(newDist);

                        // Replace cycle line
                        if (markerStack.size() > 1) {
                            cycleLine.remove();
                        }
                        cycleLine = map.addPolyline(new PolylineOptions()
                                .add(markerStack.get(0).getPosition(), newMarker.getPosition())
                                .width(10)
                                .color(Color.RED));
                    } else {
                        routeLength.add(result);
                    }
                    updateCalcLengthText();
                }
                markerStack.push(newMarker);
            }
        });

        // Sets a listener to respond to drag events
        map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {

            private ArrayList<LatLng> latLngList = new ArrayList<LatLng>();

            @Override
            public void onMarkerDragStart(Marker marker) {
                if (!(markerStack.size() == 1)) {
                    markerStack.pop();
                    polylineStack.pop().remove();
                    routeLength.remove(routeLength.size()-1);
                }
            }

            @Override
            public void onMarkerDrag(Marker marker) { }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                float result = getDistanceBetweenMarkers(markerStack.peek(), marker);
                routeLength.add(result);
                updateCalcLengthText();
                Polyline line = map.addPolyline(new PolylineOptions()
                          .add(markerStack.peek().getPosition(), marker.getPosition())
                          .width(10)
                          .color(Color.RED));
                markerStack.push(marker);
                polylineStack.push(line);

            }
        });
    }

    public boolean onMarkerClick(final Marker marker) {
        // Center camera on marker clicked and zoom to default zoom level
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), DEFAULT_ZOOM));

        Toast.makeText(getApplicationContext(), "Marker clicked", Toast.LENGTH_SHORT).show();

        // Return true to mark the event is consumed
        // and prevent any extra UI menus to appear
        return true;
    }

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

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                Task<Location> locationResult =  mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            lastKnownLocation = task.getResult();
                            if (lastKnownLocation != null) {
                                map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(lastKnownLocation.getLatitude(),
                                                lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            }
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            map.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                            map.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }

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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (map != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, map.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, lastKnownLocation);
        }
        super.onSaveInstanceState(outState);
    }

    public void updateCalcLengthText() {
        TextView calcLength = (TextView) findViewById(R.id.textView_calc_length);
        float totalRouteLength = 0;
        for (int i = 0; i < routeLength.size(); i++) {
            totalRouteLength += routeLength.get(i);
        }

        float distance = totalRouteLength/1000;
        float num = (float) Math.round(distance*100)/100;
        calcLength.setText(num + " km");
    }

    protected float getDistanceBetweenMarkers(Marker A, Marker B) {
        float[] results = new float[1];
        LatLng firstMarkerPosition = A.getPosition();
        LatLng lastMarkerPosition = B.getPosition();
        Location.distanceBetween(
                firstMarkerPosition.latitude,
                firstMarkerPosition.longitude,
                lastMarkerPosition.latitude,
                lastMarkerPosition.longitude,
                results
        );
        return results[0];
    }

    public ArrayList<Float> getRouteLength() {
        return routeLength;
    }

    public Polyline getCycleLine() {
        return cycleLine;
    }

    public Stack<Marker> getMarkerStack() {
        return markerStack;
    }

    public Stack<Polyline> getPolylineStack() {
        return polylineStack;
    }

    public GoogleMap getMap() {
        return map;
    }

    public void setCycleLine(Polyline cycleLine) {
        this.cycleLine = cycleLine;
    }

    public CheckBox getCycleCheckBox() {
        return cycleCheckBox;
    }
}