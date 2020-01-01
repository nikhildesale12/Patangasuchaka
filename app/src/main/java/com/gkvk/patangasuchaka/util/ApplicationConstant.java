package com.gkvk.patangasuchaka.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Environment;
import android.provider.Settings;

import java.io.File;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class ApplicationConstant {

    public static final String MODULE_CAPTURE = "Camera";
    public static final String MODULE_GALLERY = "Gallery";
    public static final String MODULE_SPECIES = "Species";
    public static final String MODULE_DISTRIBUTION = "Distribution";

    public static final int API_LEVEL_23 = 23;
    public static final int REQUESTPERMISSIONCODE = 1;
    public static final int RESULT_OPEN_CAMERA = 2 ;
    public static final int RESULT_OPEN_GALLERY = 3 ;
    public static final String FROM_MODULE = "FROM_MODULE";
    public static final String FOLDER_PATH = Environment.getExternalStorageDirectory() + File.separator + "PATANGASUCHAKA" ;
    public static final String IMAGE_NAME = "Upload_" ;

    public static final String ENDPOINT_URL_AI_UPLOAD = "http://45.117.30.211:8080/classify";
    public static final String ExTNTION_JPG=".jpg";

    public static boolean checkPermission(Context context) {
        int FirstPermissionResult = ContextCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(context, ACCESS_COARSE_LOCATION);
        int ThirdPermissionResult = ContextCompat.checkSelfPermission(context, INTERNET);
        int ForthPermissionResult = ContextCompat.checkSelfPermission(context, WRITE_EXTERNAL_STORAGE);
        int FifthPermissionResult = ContextCompat.checkSelfPermission(context, READ_EXTERNAL_STORAGE);

        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ThirdPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ForthPermissionResult == PackageManager.PERMISSION_GRANTED &&
                FifthPermissionResult == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isGPSEnabled(Context context){
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean GPSStatus = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return GPSStatus;
    }

    public static void showSettingsAlert(final Context context){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("GPS Is Disable");
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    public static void requestPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]
                {
                        ACCESS_FINE_LOCATION,
                        ACCESS_COARSE_LOCATION,
                        INTERNET,
                        WRITE_EXTERNAL_STORAGE,
                        READ_EXTERNAL_STORAGE
                }, ApplicationConstant.REQUESTPERMISSIONCODE);
    }
}
