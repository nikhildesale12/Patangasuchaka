package com.gkvk.patangasuchaka.entry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.gkvk.R;
import com.gkvk.patangasuchaka.main.LoginActivity;
import com.gkvk.patangasuchaka.main.MainActivity;

public class SplashNewActivity extends AppCompatActivity {

    TextView versionName;
    String version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_new);
        //databaseHelper = DatabaseHelper.getDatabaseInstance(SplashScreen.this);
        versionName = (TextView) findViewById(R.id.version_name);
        PackageInfo pInfo = null;
        try {
            pInfo = SplashNewActivity.this.getPackageManager().getPackageInfo(SplashNewActivity.this.getPackageName(), 0);
            version = pInfo.versionName;
            versionName.setText("Version : "+version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Thread init = new Thread() {
            public void run() {
                try {
                    sleep(3000);
                    Intent i = new Intent(SplashNewActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        init.start();
    }//end create


   // Intent i = new Intent(SplashNewActivity.this, MainActivity.class);
}
