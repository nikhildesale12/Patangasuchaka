package com.gkvk.patangasuchaka.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gkvk.R;
import com.gkvk.patangasuchaka.bean.HistoryData;
import com.gkvk.patangasuchaka.util.ApplicationConstant;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GoogleMapActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    //GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    private GoogleMap mMap;
    HistoryData historyData;
    List<HistoryData> historyDataList;
    Dialog internetConn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);
        Intent i = getIntent();
        //historyData = (HistoryData) i.getParcelableExtra("distribution_bean");
        historyDataList = i.getParcelableArrayListExtra("distribution_list");

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                //buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            //buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        if(historyDataList !=null && historyDataList.size()>0) {
            for(int i=0;i<historyDataList.size();i++) {
                //Showing Current Location Marker on Map
                if (historyDataList.get(i).getLat() != null && historyDataList.get(i).getLat().length() > 0) {
                    if (historyDataList.get(i).getLat() != null && historyDataList.get(i).getLat().trim().length() > 1) {
                        LatLng latLng = new LatLng(Double.parseDouble(historyDataList.get(i).getLat()), Double.parseDouble(historyDataList.get(i).getLng()));
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(latLng);
                        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        String provider = locationManager.getBestProvider(new Criteria(), true);
                        if (ActivityCompat.checkSelfPermission(this,
                                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                                        != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }

                        markerOptions.title(historyDataList.get(i).getOneCommonName());
                        markerOptions.snippet(historyDataList.get(i).getPlaceCap());

                        //markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.markericon);
                        markerOptions.icon(icon);
                        mCurrLocationMarker = mMap.addMarker(markerOptions);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(4));
                    }
                }else{
                    if(historyDataList.size() ==1)
                    displayDialog("Species details not available");
                }
            }
        }else{
            displayDialog("Species not available");
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }
    @Override
    public void onConnectionSuspended(int i) {
    }
    @Override
    public void onLocationChanged(Location loc) {
    }

    private void displayDialog(String message) {
        internetConn = new Dialog(GoogleMapActivity.this);
        internetConn.requestWindowFeature(Window.FEATURE_NO_TITLE);
        internetConn.setContentView(R.layout.dialog_popup);
        internetConn.setCanceledOnTouchOutside(false);
        TextView tv = (TextView) internetConn.findViewById(R.id.textMessage);
        tv.setText(message);
        TextView titleText = (TextView) internetConn.findViewById(R.id.dialogHeading);
        titleText.setText("Result");
        Button btnClose = (Button) internetConn.findViewById(R.id.ok);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                internetConn.dismiss();
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finish();
            }
        });
        internetConn.show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }
    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        /*if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }*/
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(this, "permission denied",
                            Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
}