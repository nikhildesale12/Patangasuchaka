package com.gkvk.patangasuchaka.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.gkvk.R;
import com.google.android.gms.common.internal.Constants;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private RadioGroup rgViews;
    private ImageView refreshPoints;
    public TextView animateText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        initViews();

        rgViews.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.rb_normal_around){
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                }else if(checkedId == R.id.rb_satellite_around){
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                }
            }
        });

        /*refreshPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(Constants.isNetworkAvailable(MapActivity.this)){
                                    animateText.setVisibility(View.VISIBLE);
                                    Animation startAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blinking_animation);
                                    animateText.startAnimation(startAnimation);
                                    Intent intentService = new Intent(MapActivity.this, GetAllUploadedDataService.class);
                                    startService(intentService);
                                }else{
                                    Toast.makeText(MapActivity.this,"No Internet Connction",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }.start();
            }
        });*/



        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void initViews() {

        animateText = (TextView) findViewById(R.id.animateText);
        rgViews=(RadioGroup) findViewById(R.id.rg_views_around);
        refreshPoints = (ImageView) findViewById(R.id.refreshPoints);



    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
