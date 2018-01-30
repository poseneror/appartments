package com.poz.appartments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapsActivity extends FragmentActivity
        implements GoogleMap.OnMapClickListener,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnCameraIdleListener,
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private GoogleMap map;
    private GoogleApiClient googleApiClient;

    private List apartments;
    private Map<Marker, Integer> ApIds = new HashMap<Marker, Integer>();

    private Map<Integer, Marker> IdAps = new HashMap<Integer, Marker>();

    private boolean hasLocationPremmision = false;
    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    public static final int DEFAULT_ZOOM = 10;
    public static final int UPDATE_DISTANCE = 1;

    private Marker newMarker;
    private Location lastKnownLocation;

    private ImageButton newApartmentBtn, searchBtn;
    private EditText searchField;

    private LatLng lastUpdateLatLng;

    private boolean lastUpdateInitialized = false;

    private boolean newApMode = false;
    private boolean showApartments = false;
    private boolean showAreas = false;

    // TODO: from sertine zoom view only city names
    // each city has its zoom value (for areas and stuff)
    // if a city is clicked, zoom in and show apartments

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
        googleApiClient.connect();
    }

    //  After connecting to the google API
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    //  When the map is ready for use
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnCameraIdleListener(this);
        map.setOnMapClickListener(this);
        GoogleMap.InfoWindowAdapter infoAdapter = new ApInfoWindowAdapter(this);
        map.setInfoWindowAdapter(infoAdapter);
        map.setOnInfoWindowClickListener(this);
        newMarker = map.addMarker(new MarkerOptions().position(new LatLng(0,0))
                .title("NEW")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_new_icon))
                .visible(false));
        updateLocationUI();
        if(getDeviceLocation()) {

        }

        searchField = (EditText) findViewById(R.id.map_search_text);
        searchBtn = (ImageButton) findViewById(R.id.map_search_btn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String location = searchField.getText().toString();
                List<Address> adList = null;
                if(location != null || !location.equals("")){
                    Geocoder geocoder = new Geocoder(getApplicationContext());
                    try{
                        adList = geocoder.getFromLocationName(location, 1);
                        Address address = adList.get(0);
                        LatLng adLatLng = new LatLng(address.getLatitude(), address.getLongitude());
                        map.addMarker(new MarkerOptions().position(adLatLng)
                                .title(address.getAddressLine(0)));
                        map.animateCamera(CameraUpdateFactory.newLatLng(adLatLng));
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }
        });

        newApartmentBtn = (ImageButton) findViewById(R.id.maps_new_apartment_btn);
        newApartmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newApMode){
                    newApMode = false;
                    newMarker.setVisible(false);
                } else {
                    newApMode = true;
                }
            }
        });
    }

    public void onSearch(){

    }

    @Override
    public void onMapClick(LatLng latLng) {
        if(newApMode) {
            newMarker.setPosition(latLng);
            newMarker.setVisible(true);
            newMarker.showInfoWindow();
        }
    }

    private void checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            hasLocationPremmision = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    private boolean getDeviceLocation() {
        checkLocationPermission();
        if (hasLocationPremmision) {
            lastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if(lastKnownLocation != null) {
                map.addMarker(new MarkerOptions()
                        .position(new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()))
                        .title("YOU")
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_you_icon)));
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()))
                        .zoom(7)
                        .bearing(lastKnownLocation.getBearing())
                        .build();
                CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                map.animateCamera(cameraUpdate);
                return true;
            }
            return false;
        }
        return false;
    }

    private void showAreas(){

    }

    private void hideAreas(){

    }

    private void showApartments(){
        if(!showApartments) {
            for (Marker m : ApIds.keySet()) {
                m.setVisible(true);
            }
            showApartments = true;
        }
    }

    private void hideApartments(){
        if(showApartments) {
            for (Marker m : ApIds.keySet()) {
                m.setVisible(false);
            }
            showApartments = false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        hasLocationPremmision = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    hasLocationPremmision = true;
                }
            }
        }
    }

    public void updateLocationUI() {
        if (map != null) {
            checkLocationPermission();
            if (hasLocationPremmision) {
                map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                map.setMyLocationEnabled(false);
                map.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
            }
        }
    }

    public void getApartments(double latitude, double longtitude){
        // request apartments from the web
        apartments = new ArrayList();
        final double lat = latitude;
        final double lng = longtitude;
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://appartments.herokuapp.com/GetNearby";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for(int i=0; i<jsonArray.length(); i++){
                                JSONObject jresponse = jsonArray.getJSONObject(i);
                                int id = jresponse.getInt("id");
                                String title = jresponse.getString("title");
                                double x = jresponse.getDouble("x");
                                double y = jresponse.getDouble("y");
                                int price = jresponse.getInt("price");
                                Apartment ap = new Apartment(id, title, x, y, price);
                                apartments.add(ap);
                            }
                            updateApartmentsOnMap();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // failed to fatch apartments
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("x", Double.toString(lat));
                params.put("y", Double.toString(lng));
                return params;
            }
        };
        queue.add(postRequest);
    }

    private void removeMarkers(){
        for(Marker m : ApIds.keySet()){
            m.remove();
        }
        ApIds.clear();
    }

    public void updateApartmentsOnMap(){
        // DONT GET EXISTING APARTMENTS !# FIX!$@$!@#
        removeMarkers();
        for(Object obj : apartments){
            Apartment ap = (Apartment) obj;
            LatLng loc = new LatLng(ap.getX(), ap.getY());
            Marker newMarker = map.addMarker(new MarkerOptions()
                        .position(loc)
                        .title(ap.getTitle())
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_ap_icon))
                        .snippet(ap.getPrice() + "$"));
            ApIds.put(newMarker, ap.getId());
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        if(ApIds.containsKey(marker)){
            Intent intent = new Intent(this, ApartmentDetailsActivity.class);
            Bundle b = new Bundle();
            b.putInt("id", ApIds.get(marker));
            intent.putExtras(b);
            startActivity(intent);
            finish();
        } else if (marker.getTitle().equals("NEW")){
            Intent intent = new Intent(this, NewAppartmentActivity.class);
            Bundle b = new Bundle();
            b.putDouble("lat", marker.getPosition().latitude);
            b.putDouble("lan", marker.getPosition().longitude);
            b.putString("address", searchField.getText().toString());
            intent.putExtras(b);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onCameraIdle() {
        if(map.getCameraPosition().zoom > 10) {
            if(!lastUpdateInitialized){
                lastUpdateLatLng = new LatLng(map.getCameraPosition().target.latitude,
                        map.getCameraPosition().target.longitude);
                getApartments(map.getCameraPosition().target.latitude, map.getCameraPosition().target.longitude);
                Log.d("UPDATE", "FETCHED NEW APARTMENTS" + map.getCameraPosition().target.toString());
                lastUpdateInitialized = true;
                showApartments();
            }
            else if (distance(lastUpdateLatLng, map.getCameraPosition().target) > 25000) {
                getApartments(map.getCameraPosition().target.latitude, map.getCameraPosition().target.longitude);
                Log.d("UPDATE", "FETCHED NEW APARTMENTS" + map.getCameraPosition().target.toString());
                lastUpdateLatLng = new LatLng(map.getCameraPosition().target.latitude, map.getCameraPosition().target.longitude);
                showApartments();
                hideAreas();
            }
        } else {
            hideApartments();
            showAreas();
        }

    }

    public static double distance(LatLng pos1, LatLng pos2) {
        double lat1 = pos1.latitude;
        double lng1 = pos1.longitude;
        double lat2 = pos2.latitude;
        double lng2 = pos2.longitude;
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        float dist = (float) (earthRadius * c);
        return dist;
    }
}
