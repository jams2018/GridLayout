package com.example.anar.gridlayout;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {

    SupportMapFragment supportMapFragment;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    GoogleMap mGoogleMap;
    String[] list;
    Geocoder geocoder;
    List<Address> addresses;

    public static final int PERMISSIONS_REQUEST_LOCATION = 100;
    private String url = "https://web6.seattle.gov/Travelers/api/Map/Data?zoomId=13&type=2";
    private Hashtable<String, String> markers = new Hashtable<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);

    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mGoogleMap.setMyLocationEnabled(true);
            }
            else {
                //Request Location Permission to user
                checkPermission();
            }
        }
        else {
            buildGoogleApiClient();
            mGoogleMap.setMyLocationEnabled(true);
        }

        mGoogleMap.setInfoWindowAdapter(new InfoWindow(MapsActivity.this));
        getData();
    }

    public void getData() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray array = response.getJSONArray("Features");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject camObj = array.getJSONObject(i);
                        Camera camera = new Camera();
                        JSONArray camLocation = camObj.getJSONArray("PointCoordinate");

                        camera.setLatitude(camLocation.getDouble(0));
                        camera.setLongitude(camLocation.getDouble(1));

                        JSONArray camArray = camObj.getJSONArray("Cameras");
                        for (int j = 0; j < camArray.length(); j++) {
                            JSONObject cameras = camArray.getJSONObject(j);
                            camera.setId(cameras.getString("Id"));
                            camera.setDescription(cameras.getString("Description"));
                            camera.setType(cameras.getString("Type"));
                            camera.setImageUrl(cameras.getString("ImageUrl"));
                        }

                        MarkerOptions markerOptions = new MarkerOptions();
                        LatLng newCamLocation = new LatLng(camera.getLatitude(),
                                camera.getLongitude());
                        markerOptions.position(newCamLocation).title(camera.getId()).snippet(camera.getDescription());

                        final Marker marker = mGoogleMap.addMarker(markerOptions);
                        markers.put(marker.getId(), camera.getImageUrl());
                        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(newCamLocation));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
                progressDialog.dismiss();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(15000);
        mLocationRequest.setFastestInterval(15000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        }
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}

    @Override
    public void onLocationChanged(final Location location)
    {
        String currentAddress = "";
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        final LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        currentAddress = getAddress(location);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Location");
        markerOptions.snippet(currentAddress);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,13));

        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(latLng);
        circleOptions.radius(200);
        circleOptions.fillColor(Color.YELLOW);
        circleOptions.strokeWidth(6);
        mGoogleMap.addCircle(circleOptions);
    }

    public String getAddress(Location location) {
        String result = "";
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if(addresses != null){
                Address address = addresses.get(0);
                StringBuilder mCurrentAddress = new StringBuilder();

                mCurrentAddress.append("Current Location:");
                mCurrentAddress.append("\n");
                if(address.getSubThoroughfare() == null) {
                    mCurrentAddress.append("");
                } else {
                    mCurrentAddress.append(address.getSubThoroughfare());
                    mCurrentAddress.append(" ");
                }

                if(address.getThoroughfare() == null){
                    mCurrentAddress.append("");
                } else {
                    mCurrentAddress.append(address.getThoroughfare());
                    mCurrentAddress.append("\n");
                }

                mCurrentAddress.append("City: ");
                mCurrentAddress.append(address.getLocality());
                mCurrentAddress.append("\n");

                mCurrentAddress.append("State: ");
                mCurrentAddress.append(address.getAdminArea());
                mCurrentAddress.append("\n");

                mCurrentAddress.append("Zip Code: ");
                mCurrentAddress.append(address.getPostalCode());
                mCurrentAddress.append("\n");

                result = result + mCurrentAddress;

            } else{
                result = "No Address Found!";
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                ActivityCompat.requestPermissions(MapsActivity.this,
                                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                        PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mGoogleMap.setMyLocationEnabled(true);
                    }

                } else {

                    Toast.makeText(this, "permission denied by user", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast toast = Toast.makeText(MapsActivity.this, "Setting Options!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 10);
            toast.show();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    private class InfoWindow implements GoogleMap.InfoWindowAdapter {

        private Context context;

        InfoWindow(Context ctx) {
            context = ctx;
        }

        @Override
        public View getInfoWindow(Marker marker) {

            View view = ((MapsActivity) context).getLayoutInflater()
                    .inflate(R.layout.activity_info_window, null);

            final String url = markers.get(marker.getId());

            final String snippet = marker.getSnippet();

            TextView name = view.findViewById(R.id.camera_name);
            ImageView img = view.findViewById(R.id.camera_image);
            Log.d("url", "" + url);
            Picasso.with(context).load(url).error(R.mipmap.ic_launcher).into(img, new InfoWindowRefresher(marker));

            name.setText(snippet);
            return view;
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }

    }

    static class InfoWindowRefresher implements Callback {
        Marker markerToRefresh;

        InfoWindowRefresher(Marker markerToRefresh) {
            this.markerToRefresh = markerToRefresh;
        }

        @Override
        public void onSuccess() {
            if (markerToRefresh == null) {
                return;
            }

            if (!markerToRefresh.isInfoWindowShown()) {
                return;
            }

            markerToRefresh.hideInfoWindow();
            markerToRefresh.showInfoWindow();
        }


        @Override
        public void onError() { }
    }
}
