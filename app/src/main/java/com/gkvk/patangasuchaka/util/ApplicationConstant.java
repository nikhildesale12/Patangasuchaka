package com.gkvk.patangasuchaka.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.gkvk.R;
import java.io.File;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.content.Context.MODE_PRIVATE;

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
    //public static final String IMAGE_NAME = "Upload_" ;

    public static final String ENDPOINT_URL_AI_UPLOAD = "http://45.117.30.211:8080/classify";
    public static final String ENDPOINT_URL_WEB_UPLOAD = "http://www.pathangasuchaka.in/pathanga_api/index.php/api_auth/Imagesync/upload";
    public static final String EXTENTION_JPG=".jpg";
    public static final String MY_PREFS_NAME = "myPref";
    public static final String KEY_INTRO = "KEY_INTRO";
    public static final String KEY_ABOUT = "KEY_ABOUT";
    public static final String KEY_HOWITWORKS = "KEY_HOWITWORKS";
    public static final String KEY_IS_LOGIN = "KEY_IS_LOGIN";
    public static final String KEY_IS_FEILDMODE = "KEY_IS_FEILDMODE";

    public static final String BASE_URL = "http://www.pathangasuchaka.in";
    public static final String LOGIN_SERVICE_URL = "/pathanga_api/index.php/api_auth/Authentication/login";
    public static final String FORGOTPASS_SERVICE_URL ="/pathanga_api/index.php/api_auth/Authentication/passreset" ;
    public static final String SIGNUP_SERVICE_URL = "/pathanga_api/index.php/api_auth/Authentication/registration";
    public static final String FEEDBACK_SERVICE_URL = "/pathanga_api/index.php/api_auth/Authentication/feedback";
    public static final String ABOUT_US_URL = "/pathanga_api/index.php/api_auth/Authentication/contentedit";
    public static final String HISTORY_SERVICE_URL = "/pathanga_api/index.php/api_auth/Authentication/history";
    public static final String PROFILE_SERVICE_URL ="/pathanga_api/index.php/api_auth/Authentication/profile" ;
    public static final String UPLOAD_DATA_WEB_BUTTERFLY_URL = "/pathanga_api/index.php/api_auth/Imagesync/imagebutterfly";
    public static final String UPLOAD_DATA_MOTH_URL = "/pathanga_api/index.php/api_auth/Imagesync/imagemoth";
    public static final String GET_DISTRIBUTION_DATA ="/pathanga_api/index.php/api_auth/Authentication/distribution" ;
    public static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";

    public static final String KEY_ID = "KEY_ID";
    public static final String KEY_USERNAME = "KEY_USERNAME";
    public static final String KEY_EMAIL = "KEY_EMAIL";
    public static final String KEY_FULL_NAME = "KEY_FULL_NAME";
    public static final String KEY_PROFILE_IMG = "KEY_PROFILE_IMG";
    public static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    public static final String OUT_JSON = "/json";
    //debug
//    public static final String API_KEY = "AIzaSyCoNT5vDSVQmxhG2kS_JXsozKtym48r_54";
    public static final String API_KEY = "AIzaSyCRIriw_45fLM8_Qa-K2MNj5FC32JfRljQ";
    public static final String Butterfly="Butterfly";
    public static final String Moth = "Moth";

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

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /*No Internet Connection*/
    public static void dispalyDialogInternet(final Context context , final String title, String message, boolean cancelDialog , final boolean isFinish) {
        final Dialog internetConn = new Dialog(context);
        internetConn.requestWindowFeature(Window.FEATURE_NO_TITLE);
        internetConn.setContentView(R.layout.dialog_popup);
        internetConn.setCanceledOnTouchOutside(cancelDialog);
        TextView tv = (TextView) internetConn.findViewById(R.id.textMessage);
        tv.setText(message);
        TextView titleText = (TextView) internetConn.findViewById(R.id.dialogHeading);
        titleText.setText(title);
        Button btnLogoutNo = (Button) internetConn.findViewById(R.id.ok);
        btnLogoutNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                internetConn.dismiss();
            }
        });
        internetConn.show();
    }

}
