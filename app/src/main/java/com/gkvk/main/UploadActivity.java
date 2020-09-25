package com.gkvk.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gkvk.BuildConfig;
import com.gkvk.R;
import com.gkvk.bean.UploadImageToAIResponse;
import com.gkvk.retrofit.ApiService;
import com.gkvk.util.ApplicationConstant;
import com.gkvk.util.GPSTracker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.channels.FileChannel;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UploadActivity extends AppCompatActivity {
    RelativeLayout imageInfoRelativeLayout;
    double latitude = 0;
    double longitude = 0;
    private String finalAddress = "";
    Button buttonAnalyze;
    ImageView captureImage;
    String pathtoUpload;
    String imageName;
    EditText txtDate;
    LinearLayout mainlinearlayout;
    ImageView buttonGoToCrop;
    Uri imageUri;
    private TextView mProgressViewText;
    private View mProgressView;
    public static final int REQUEST_IMAGE = 100;

    @Override
    public void onSupportActionModeStarted(@NonNull ActionMode mode) {
        super.onSupportActionModeStarted(mode);
    }

    AutoCompleteTextView autocompletePlaces;
    String userName;
    private int mYear, mMonth, mDay;
    Timestamp timestampObj ;
    long timestamp;
    String currentDateTimeString;
    private File mFileTemp;
    GPSTracker gps;

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        setContentView(R.layout.activity_upload);
        initViews();
        SharedPreferences sharedPreferences = UploadActivity.this.getSharedPreferences(ApplicationConstant.MY_PREFS_NAME, MODE_PRIVATE);
        userName  = sharedPreferences.getString(ApplicationConstant.KEY_USERNAME, "");
        timestampObj = new Timestamp(System.currentTimeMillis());
        timestamp = timestampObj.getTime();
        autocompletePlaces.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.list_text_item));
        if (android.os.Build.VERSION.SDK_INT >= ApplicationConstant.API_LEVEL_23) {
            if (ApplicationConstant.checkPermission(UploadActivity.this)) {
                if (!ApplicationConstant.isGPSEnabled(UploadActivity.this)) {
                    ApplicationConstant.showSettingsAlert(UploadActivity.this);
                }
            } else {
                ApplicationConstant.requestPermission(UploadActivity.this);
            }
        } else {
            if (!ApplicationConstant.isGPSEnabled(UploadActivity.this)) {
                ApplicationConstant.showSettingsAlert(UploadActivity.this);
            }
        }
        File rootDirectory = new File(ApplicationConstant.FOLDER_PATH);
//      File rootDirectory = new File(Environment.getExternalStorageDirectory(), ApplicationConstant.FOLDER_PATH);
        if (!rootDirectory.exists()) {
            rootDirectory.mkdir();
        }
        getLocationDetail();

        autocompletePlaces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String selectedPlace = (String) parent.getItemAtPosition(position);
                Geocoder coder = new Geocoder(UploadActivity.this);
                List<Address> address;
                try {
                    address = coder.getFromLocationName(selectedPlace, 5);
                    Address location = address.get(0);
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    System.out.println("New latitude : "+latitude);
                    System.out.println("New longitude : "+longitude);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        ImagePickerActivity.clearCache(this);

        Intent intent = getIntent();
        String fromModule = intent.getStringExtra(ApplicationConstant.FROM_MODULE);
        if (fromModule.equals(ApplicationConstant.MODULE_CAPTURE)) {
            //imageInfoRelativeLayout.setVisibility(View.INVISIBLE);
            openCamera();
            //launchCameraIntent();
        } else if (fromModule.equals(ApplicationConstant.MODULE_GALLERY)) {
            //imageInfoRelativeLayout.setVisibility(View.VISIBLE);
            openGallery();
//            launchGalleryIntent();
        }

        buttonAnalyze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ApplicationConstant.isNetworkAvailable(UploadActivity.this)) {
                    //check image is selected or not
                    //new UploadFileToServer(pathtoUpload).execute();

                    /**Upload Imgage by Retrofit*/
                    Log.d("Lat", String.valueOf(latitude));
                    Log.d("Lng", String.valueOf(longitude));

                    executeUploadService(pathtoUpload);
                } else {
                    Toast.makeText(UploadActivity.this, "Please check internet connection", Toast.LENGTH_LONG).show();
                }
            }
        });
        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(UploadActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        buttonGoToCrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void launchGalleryIntent() {

        Intent intent = new Intent(UploadActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);

        //Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //startActivityForResult(intent, ApplicationConstant.RESULT_OPEN_GALLERY);

    }

    private void launchCameraIntent() {

        currentDateTimeString = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        txtDate.setText(currentDateTimeString);
        txtDate.setEnabled(false);
        String state = Environment.getExternalStorageState();
        imageName = currentDateTimeString+"@"+userName+"@"+timestamp+ ApplicationConstant.EXTENTION_JPG;
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            mFileTemp = new File(ApplicationConstant.FOLDER_PATH, imageName);
        } else {
            mFileTemp = new File(getFilesDir(), imageName);
        }
        /*Uri.fromFile(mFileTemp)*/
        Uri photoURI = null;
        if (android.os.Build.VERSION.SDK_INT >= ApplicationConstant.API_LEVEL_23) {
            photoURI = FileProvider.getUriForFile(UploadActivity.this, BuildConfig.APPLICATION_ID + ".provider", mFileTemp);
        } else {
            photoURI = Uri.fromFile(mFileTemp);
        }

        Intent intent = new Intent(UploadActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void getLocationDetail() {
        gps = new GPSTracker(UploadActivity.this);
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            if (latitude != 0 && longitude != 0) {
                List<Address> addresses = getAddress(latitude, longitude);
                String address = "", city = "", country = "";
                if (addresses != null) {
                    address = addresses.get(0).getAddressLine(0);
                    city = addresses.get(0).getAddressLine(1);
                    country = addresses.get(0).getAddressLine(2);
                }
                if ((city == null || (city != null && city.equals("null")))
                        && (country == null || (country != null && country.equals("null")))) {
                    finalAddress = address;
                } else if (city == null || (city != null && city.equals("null"))
                        && country != null && !country.equals("null")) {
                    finalAddress = address + "," + country;
                } else if (country == null || (country != null && country.equals("null"))
                        && city != null && !city.equals("null")) {
                    finalAddress = address + "," + city;
                } else {
                    finalAddress = address + "," + city + "," + country;
                }
            } else {
                Toast.makeText(UploadActivity.this, "Error while getting location , please restart mobile GPS", Toast.LENGTH_SHORT).show();
            }
        }
    }

    ProgressDialog dialog;
    private void executeUploadService(final String pathtoUpload) {
        dialog = new ProgressDialog(UploadActivity.this);
        dialog.setMessage("Please Wait...");
        dialog.setIndeterminate(false);
        dialog.setCancelable(false);
        dialog.show();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ApplicationConstant.BASE_URL_AI)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();
        ApiService service = retrofit.create(ApiService.class);
        File file = new File(pathtoUpload);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        final MultipartBody.Part body = MultipartBody.Part.createFormData("image", imageName, requestFile);
        Call<List<UploadImageToAIResponse>> call = service.uploadImageToAI(body);
        call.enqueue(new Callback<List<UploadImageToAIResponse>>() {
            @Override
            public void onResponse(Call<List<UploadImageToAIResponse>> call, Response<List<UploadImageToAIResponse>> response) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (response != null && response.body() != null) {
                    try {
                        JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                        Intent intent = new Intent(UploadActivity.this, IdentificationActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("result", jsonArray.toString());
                        bundle.putString("path", pathtoUpload);
                        bundle.putString("imageName",imageName);
                        bundle.putString("autocompletePlaces",autocompletePlaces.getText().toString());
                        bundle.putDouble("lat",latitude);
                        bundle.putDouble("lng",longitude);
                        bundle.putString("date",txtDate.getText().toString());
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if(response.errorBody() != null) {
                    Log.d("Error",response.errorBody().toString());
                }
            }
            @Override
            public void onFailure(Call<List<UploadImageToAIResponse>> call, Throwable t) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                displayDialog(UploadActivity.this, "Result", t.toString());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ApplicationConstant.RESULT_OPEN_CAMERA:
                try {
                    if (resultCode == Activity.RESULT_OK) {
                        File file = new File(ApplicationConstant.FOLDER_PATH, imageName);
                        if (file != null) {
                            Log.e("file.length() : ", "file.length()  : " + file.length());
                            autocompletePlaces.setText(finalAddress);
                            autocompletePlaces.setEnabled(false);

                            //captureImage.setImageURI(Uri.parse(ApplicationConstant.FOLDER_PATH+File.separator+ApplicationConstant.IMAGE_NAME+currentDateTimeString+ApplicationConstant.ExTNTION_JPG));
                            pathtoUpload = ApplicationConstant.FOLDER_PATH + File.separator + imageName;
                            //compress and set image to image view
                            compressImage(ApplicationConstant.FOLDER_PATH + File.separator + imageName, ApplicationConstant.MODULE_CAPTURE);
                            imageUri = Uri.parse(pathtoUpload);
                            //imageUri = CropImage.getPickImageResultUri(this, data);
                            imageUri = Uri.parse("file://"+getIntent().getExtras().getString("imageUri"));
                        }
                    } else if (resultCode == Activity.RESULT_CANCELED) {
                        Toast.makeText(this, "Image not capture", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(UploadActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                        finishAffinity();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case ApplicationConstant.RESULT_OPEN_GALLERY:
                try {
                    if (requestCode == ApplicationConstant.RESULT_OPEN_GALLERY && resultCode == RESULT_OK && null != data) {
                        Uri URI = data.getData();
                        /*String[] FILE = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getContentResolver().query(URI,FILE, null, null, null);
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(FILE[0]);
                        String galleryPath = cursor.getString(columnIndex);
                        cursor.close();*/
                        currentDateTimeString = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

                        imageName = currentDateTimeString+"@"+userName+"@"+timestamp+ ApplicationConstant.EXTENTION_JPG;

                        pathtoUpload = ApplicationConstant.FOLDER_PATH + File.separator + imageName;

                        File destFile = new File(pathtoUpload);
                        copyFile(new File(getPath(data.getData())), destFile,pathtoUpload);
                        //imageUri = Uri.parse(pathtoUpload);

                    } else if (resultCode == Activity.RESULT_CANCELED) {
                        Toast.makeText(this, "Image not selected", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(UploadActivity.this, MainActivity.class);
                        //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();
                        finishAffinity();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Please try again", Toast.LENGTH_LONG)
                            .show();
                }
                break;
             case REQUEST_IMAGE:
                if (resultCode == Activity.RESULT_OK) {
                    Uri uri = data.getParcelableExtra("path");
                    try {
                        // You can update this bitmap to your server
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);

                        // loading profile image from local cache
                        loadProfile(uri.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
    }

    private void loadProfile(String url) {
        Log.d("", "Image cache path: " + url);
        captureImage.setImageURI(Uri.parse(url));
    }

    public String getPath(Uri uri) {
        String[] FILE = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri,FILE, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(FILE[0]);
        String galleryPath = cursor.getString(columnIndex);
        cursor.close();
        return galleryPath;
    }

    private void copyFile(File sourceFile, File destFile,String pathtoUpload) throws IOException {
        if (!sourceFile.exists()) {
            return;
        }
        FileChannel source = null;
        FileChannel destination = null;
        source = new FileInputStream(sourceFile).getChannel();
        destination = new FileOutputStream(destFile).getChannel();
        if (destination != null && source != null) {
            destination.transferFrom(source, 0, source.size());
        }
        if (source != null) {
            source.close();
        }
        if (destination != null) {
            destination.close();
        }
        if(destFile != null){
            compressImage(pathtoUpload, ApplicationConstant.MODULE_GALLERY);
        }
    }

    private void openCamera() {
        ///currentDateTimeString =new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a").format(new Date());
        currentDateTimeString = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        txtDate.setText(currentDateTimeString);
        txtDate.setEnabled(false);
        String state = Environment.getExternalStorageState();
        imageName = currentDateTimeString+"@"+userName+"@"+timestamp+ ApplicationConstant.EXTENTION_JPG;
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            mFileTemp = new File(ApplicationConstant.FOLDER_PATH, imageName);
        } else {
            mFileTemp = new File(getFilesDir(), imageName);
        }
        /*Uri.fromFile(mFileTemp)*/
        Uri photoURI = null;
        if (android.os.Build.VERSION.SDK_INT >= ApplicationConstant.API_LEVEL_23) {
            photoURI = FileProvider.getUriForFile(UploadActivity.this, BuildConfig.APPLICATION_ID + ".provider", mFileTemp);
        } else {
            photoURI = Uri.fromFile(mFileTemp);
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            List<ResolveInfo> resInfoList = UploadActivity.this.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                UploadActivity.this.grantUriPermission(packageName, photoURI, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        startActivityForResult(intent, ApplicationConstant.RESULT_OPEN_CAMERA);
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, ApplicationConstant.RESULT_OPEN_GALLERY);
    }


    private void initViews() {
        imageInfoRelativeLayout = (RelativeLayout) findViewById(R.id.imageInfoRelativeLayout);
        buttonAnalyze = (Button) findViewById(R.id.buttonAnalyze);
        captureImage = (ImageView) findViewById(R.id.selectedImage);
        autocompletePlaces = (AutoCompleteTextView) findViewById(R.id.autocompletePlaces);
        txtDate = (EditText) findViewById(R.id.txtDate);
        mainlinearlayout = (LinearLayout) findViewById(R.id.mainlinearlayout);
        buttonGoToCrop = (ImageView) findViewById(R.id.buttonGoToCrop);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ApplicationConstant.REQUESTPERMISSIONCODE:
                if (grantResults.length > 0) {
                    boolean AccessFineLocationPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean AccessCoarseLocPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean InternetPermission = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean WriteInternalStoragePermission = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadInternalStoragePermission = grantResults[4] == PackageManager.PERMISSION_GRANTED;
                    if (AccessFineLocationPermission && AccessCoarseLocPermission && InternetPermission && WriteInternalStoragePermission && ReadInternalStoragePermission) {
                    } else {
                        Toast.makeText(UploadActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    private void displayDialog(final Context context, String result, String message) {
        final Dialog fragmentDialog = new Dialog(context);
        fragmentDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        fragmentDialog.setContentView(R.layout.dialog_popup);
        fragmentDialog.setCanceledOnTouchOutside(false);
        TextView tv = (TextView) fragmentDialog.findViewById(R.id.textMessage);
        tv.setText(message);
        TextView titleText = (TextView) fragmentDialog.findViewById(R.id.dialogHeading);
        titleText.setText(result);
        Button btnLogoutNo = (Button) fragmentDialog.findViewById(R.id.ok);
        btnLogoutNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentDialog.dismiss();
            }
        });
        fragmentDialog.show();
    }

    public List<Address> getAddress(double lat, double lng) {
        try {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this);
            if (lat != 0 || lng != 0) {
                addresses = geocoder.getFromLocation(lat, lng, 1);
                String address = addresses.get(0).getAddressLine(0);
                String city = addresses.get(0).getAddressLine(1);
                String country = addresses.get(0).getAddressLine(2);
                System.out.println(address + " - " + city + " - " + country);

                return addresses;

            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /*Image Compression new starts*/
    public void compressImage(String path, String module) {
        pathtoUpload = path;
        String imagePath = getRealPathFromURI(path);
        Bitmap scaledBitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(imagePath, options);
        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;
//      max Height and width values of the compressed image is taken as 816x612
        if (actualWidth > 0 && actualHeight > 0) {
            float maxHeight = 816.0f;
            float maxWidth = 612.0f;
            float imgRatio = 0;
            float maxRatio = 0;
            if (actualHeight != 0) {
                imgRatio = actualWidth / actualHeight;
            }
            if (maxHeight != 0) {
                maxRatio = maxWidth / maxHeight;
            }
            // width and height values are set maintaining the aspect ratio of the image
            if (actualHeight > maxHeight || actualWidth > maxWidth) {
                if (imgRatio < maxRatio) {
                    imgRatio = maxHeight / actualHeight;
                    actualWidth = (int) (imgRatio * actualWidth);
                    actualHeight = (int) maxHeight;
                } else if (imgRatio > maxRatio) {
                    imgRatio = maxWidth / actualWidth;
                    actualHeight = (int) (imgRatio * actualHeight);
                    actualWidth = (int) maxWidth;
                } else {
                    actualHeight = (int) maxHeight;
                    actualWidth = (int) maxWidth;
                }
            }
            options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
            options.inJustDecodeBounds = false;
            options.inPurgeable = true;
            options.inInputShareable = true;
            options.inTempStorage = new byte[16 * 1024];
            try {
                bmp = BitmapFactory.decodeFile(imagePath, options);
            } catch (OutOfMemoryError exception) {
                exception.printStackTrace();
            }
            try {
                scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
            } catch (OutOfMemoryError exception) {
                exception.printStackTrace();
            }
            float ratioX = actualWidth / (float) options.outWidth;
            float ratioY = actualHeight / (float) options.outHeight;
            float middleX = actualWidth / 2.0f;
            float middleY = actualHeight / 2.0f;

            Matrix scaleMatrix = new Matrix();
            scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

            Canvas canvas = new Canvas(scaledBitmap);
            canvas.setMatrix(scaleMatrix);
            canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));
            ExifInterface exif;
            try {
                exif = new ExifInterface(imagePath);

                int orientation = exif.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION, 0);
                Log.d("EXIF", "Exif: " + orientation);
                Matrix matrix = new Matrix();
                if (orientation == 6) {
                    matrix.postRotate(90);
                    Log.d("EXIF", "Exif: " + orientation);
                } else if (orientation == 3) {
                    matrix.postRotate(180);
                    Log.d("EXIF", "Exif: " + orientation);
                } else if (orientation == 8) {
                    matrix.postRotate(270);
                    Log.d("EXIF", "Exif: " + orientation);
                }
                scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                        scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                        true);
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileOutputStream out = null;
            //String filename = getFilename();
            File outFile = new File(imagePath);
            if (outFile != null && outFile.exists()) {
                outFile.delete();
            }
            try {
                out = new FileOutputStream(outFile);
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();

                if (module.equals(ApplicationConstant.MODULE_CAPTURE)) {
                    captureImage.setImageURI(Uri.parse(pathtoUpload));
                }
                if (module.equals(ApplicationConstant.MODULE_GALLERY)) {
                    captureImage.setImageBitmap(BitmapFactory.decodeFile(pathtoUpload));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }
        return inSampleSize;
    }

    /*place coed start*/
    class GooglePlacesAutocompleteAdapter extends ArrayAdapter<String> implements Filterable {
        private ArrayList<String> resultList;

        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }

    public static ArrayList<String> autocomplete(String input) {
        ArrayList<String> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(ApplicationConstant.PLACES_API_BASE + ApplicationConstant.TYPE_AUTOCOMPLETE + ApplicationConstant.OUT_JSON);
            sb.append("?key=" + ApplicationConstant.API_KEY);
            //sb.append("&components=");
            sb.append("&components=country:in");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());

            System.out.println("URL: "+url);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e("UploadActiviy", "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e("UploadActivity", "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        try {
            System.out.println("========================="+jsonResults.toString()+"===================================");
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList<String>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
                System.out.println("============================================================");
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
        } catch (JSONException e) {
            Log.e("UploadActivity", "Cannot process JSON results", e);
        }

        return resultList;
    }
    /*place coed end*/

    /*ProgressDialog dialog;
    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        String path;

        private UploadFileToServer(String path) {
            this.path = path;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(UploadActivity.this);
            dialog.setMessage("Please Wait...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile(path);
        }

        @SuppressWarnings("deprecation")
        private String uploadFile(String filePath) {
            String result = "";
            final String boundary = "-------------" + System.currentTimeMillis();
            MultipartEntityBuilder entity = MultipartEntityBuilder.create();
            Log.d("path : ", path);
            URLConnection connection = null;
            HttpURLConnection httpConn = null;
            try {
                entity.setBoundary(boundary);
                File sourceFile = new File(filePath);
                entity.addPart("image", new FileBody(sourceFile));
                java.net.URL url = new URL(ApplicationConstant.ENDPOINT_URL_AI_UPLOAD);
                connection = url.openConnection();
                httpConn = (HttpURLConnection) connection;
                httpConn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
                httpConn.setConnectTimeout(50000);
                httpConn.setRequestMethod("POST");
                httpConn.setDoInput(true);
                httpConn.setDoOutput(true);
                httpConn.connect();

                OutputStream os = httpConn.getOutputStream();
                entity.build().writeTo(os);
                os.flush();
                os.close();

                int statusCode;
                try {
                    statusCode = httpConn.getResponseCode();
                } catch (EOFException e) {
                    e.printStackTrace();
                    return "";
                }
                InputStreamReader isr;
                if (statusCode != 200 && statusCode != 204 && statusCode != 201) {
                    isr = new InputStreamReader(
                            httpConn.getErrorStream());
                } else {
                    isr = new InputStreamReader(
                            httpConn.getInputStream());
                }
                BufferedReader br = new BufferedReader(isr);
                String line;
                String tempResponse = "";
                // Create a string using response from web services
                while ((line = br.readLine()) != null)
                    tempResponse = tempResponse + line;
                result = tempResponse;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                showErrorToast();
                if (e.toString().contains("failed to connect")) {
                    result = "failed to connect AI Server";
                }
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (httpConn != null) {
                    httpConn.disconnect();
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("ImageUploadService", "Response from server: " + result);
            super.onPostExecute(result);
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            Intent intent = new Intent(UploadActivity.this, IdentificationActivity.class);
            intent.putExtra("result", result);
            intent.putExtra("path", path);
            intent.putExtra("imageName",imageName);
            intent.putExtra("autocompletePlaces",autocompletePlaces.getText().toString());
            intent.putExtra("lat",latitude);
            intent.putExtra("lng",longitude);
            intent.putExtra("date",txtDate.getText().toString());
            startActivity(intent);
            finish();
        }
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
       /* if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }*/
    }

    private void showErrorToast() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "Failed to connect to Server", Toast.LENGTH_LONG).show();
            }
        });
        finishAffinity();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

}
