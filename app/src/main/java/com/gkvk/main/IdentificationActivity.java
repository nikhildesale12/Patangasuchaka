package com.gkvk.main;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gkvk.R;
import com.gkvk.bean.UploadDataToWebRequest;
import com.gkvk.bean.UploadDataToWebResponse;
import com.gkvk.bean.UploadToWebResponse;
import com.gkvk.retrofit.ApiService;
import com.gkvk.util.ApplicationConstant;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Credentials;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class IdentificationActivity extends AppCompatActivity {
    ImageView displayImage;
    TextView textSciName1,textSciName2,textSciName3;
    TextView textComName1,textComName2,textComName3;
    TextView textProbability1,textProbability2,textProbability3;
    TextView textCategory1,textCategory2,textCategory3;
    RelativeLayout result3,result2;
    ProgressBar progressBar1,progressBar2,progressBar3;
    Button buttonBackIdentify;
    UploadDataToWebRequest uploadDataToWebRequest = null;
    String imageName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        setContentView(R.layout.activity_identification);
        initView();
        SharedPreferences sharedPreferences = IdentificationActivity.this.getSharedPreferences(ApplicationConstant.MY_PREFS_NAME, MODE_PRIVATE);
        String userName  = sharedPreferences.getString(ApplicationConstant.KEY_USERNAME, "");
        Bundle bundle = getIntent().getExtras();
        String jsonResult = bundle.getString("result");
        String path = bundle.getString("path");
        imageName = bundle.getString("imageName");
        String autocompletePlaces = bundle.getString("autocompletePlaces");
        double lat = bundle.getDouble("lat");
        double lng = bundle.getDouble("lng");
        String date = bundle.getString("date");
        if (jsonResult != null) {
            Log.d("Result in Display page:", jsonResult);
        }
        if(path != null){
            Log.d("path:",path);
        }
        if(imageName != null){
            Log.d("imageName:",imageName);
        }
        if(autocompletePlaces != null){
            Log.d("autocompletePlaces:",autocompletePlaces);
        }
        Log.d("lat:", String.valueOf(lat));
        Log.d("lng:", String.valueOf(lng));
        if(date != null){
            Log.d("date:",date);
        }

        uploadDataToWebRequest = new UploadDataToWebRequest();
        try {
            uploadDataToWebRequest.setImage(imageName);
            uploadDataToWebRequest.setUsername(userName);
            uploadDataToWebRequest.setPlace_cap(autocompletePlaces);
            uploadDataToWebRequest.setDate_cap(date);
            uploadDataToWebRequest.setLat(String.valueOf(lat));
            uploadDataToWebRequest.setLng(String.valueOf(lng));

            JSONArray jsonArray = new JSONArray(jsonResult);
            if(jsonArray != null && jsonArray.length()>0) {
                JSONObject jsonResult1 = jsonArray.getJSONObject(0);
                uploadDataToWebRequest.setButt_category(jsonResult1.optString("category"));
                uploadDataToWebRequest.setOne_common_name(jsonResult1.optString("butterfly_cn"));
                uploadDataToWebRequest.setOne_scientific_name(jsonResult1.optString("butterfly_sn"));
                uploadDataToWebRequest.setOne_probability(jsonResult1.optString("probability"));

                textSciName1.setText(jsonResult1.optString("butterfly_sn"));
                textComName1.setText(jsonResult1.optString("butterfly_cn"));
                textProbability1.setText(jsonResult1.optString("probability"));
                textCategory1.setText(jsonResult1.optString("category"));
                if(!jsonResult1.optString("probability").equalsIgnoreCase("NONE") && !jsonResult1.optString("probability").equalsIgnoreCase("NA")){
                    String progress1Array[] = jsonResult1.optString("probability").split("\\.");
                    int progress1 = 0;
                    if(progress1Array.length>0) {
                        progress1 = Integer.parseInt(progress1Array[0]);
                    }
                    progressBar1.setProgress(progress1);
                }
                if(jsonArray.length()>1){
                    JSONObject jsonResult2 = jsonArray.getJSONObject(1);
                    uploadDataToWebRequest.setTwo_common_name(jsonResult2.optString("butterfly_cn"));
                    uploadDataToWebRequest.setTwo_scientific_name(jsonResult2.optString("butterfly_sn"));
                    uploadDataToWebRequest.setTwo_probability(jsonResult2.optString("probability"));

                    textSciName2.setText(jsonResult2.optString("butterfly_sn"));
                    textComName2.setText(jsonResult2.optString("butterfly_cn"));
                    textCategory2.setText(jsonResult2.optString("category"));
                    if(jsonResult2.optString("butterfly_sn").equals("NA")){
                        result2.setVisibility(View.INVISIBLE);
                    }
                    textProbability2.setText(jsonResult2.optString("probability"));
                    if(!jsonResult2.optString("probability").equalsIgnoreCase("NONE") && !jsonResult2.optString("probability").equalsIgnoreCase("NA")) {
                        String progress2Array[] = jsonResult2.optString("probability").split("\\.");
                        int progress2 = 0;
                        if (progress2Array.length > 0) {
                            progress2 = Integer.parseInt(progress2Array[0]);
                        }
                        progressBar2.setProgress(progress2);
                    }else{
                        result2.setVisibility(View.INVISIBLE);
                    }
                }
                if(jsonArray.length()>2){
                    JSONObject jsonResult3 = jsonArray.getJSONObject(2);
                    uploadDataToWebRequest.setThree_common_name(jsonResult3.optString("butterfly_cn"));
                    uploadDataToWebRequest.setThree_probability(jsonResult3.optString("probability"));
                    uploadDataToWebRequest.setThree_scientific_name(jsonResult3.optString("butterfly_sn"));

                    textSciName3.setText(jsonResult3.optString("butterfly_sn"));
                    textComName3.setText(jsonResult3.optString("butterfly_cn"));
                    textCategory3.setText(jsonResult3.optString("category"));
                    if(jsonResult3.optString("butterfly_sn").equals("NA")){
                        result3.setVisibility(View.INVISIBLE);
                    }
                    textProbability3.setText(jsonResult3.optString("probability"));
                    if(!jsonResult3.optString("probability").equalsIgnoreCase("NONE") && !jsonResult3.optString("probability").equalsIgnoreCase("NA")) {
                        String progress3Array[] = jsonResult3.optString("probability").split("\\.");
                        int progress3 = 0;
                        if(progress3Array.length>0) {
                           progress3 = Integer.parseInt(progress3Array[0]);
                        }
                        progressBar3.setProgress(progress3);
                    }else{
                        result3.setVisibility(View.INVISIBLE);
                    }
                }
            }

            displayImage.setImageBitmap(BitmapFactory.decodeFile(path));

            /** Upload image to Webapp server */
            uploadImageToWebAppServer(path,uploadDataToWebRequest);

            buttonBackIdentify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    private void uploadImageToWebAppServer(String path,UploadDataToWebRequest uploadDataToWebRequest) {
        //new UploadFileToServer(path,uploadDataToWebRequest).execute();
        executeUploadService(path,uploadDataToWebRequest);
    }

    ProgressDialog dialog;
    private void executeUploadService(String path, final UploadDataToWebRequest uploadDataToWebRequest) {
        try {
            dialog = new ProgressDialog(IdentificationActivity.this);
            dialog.setMessage("Please Wait...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.show();
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            //Basic Auth
            final String authToken = Credentials.basic("admin", "1234");

            //Create a new Interceptor.
            Interceptor headerAuthorizationInterceptor = new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    okhttp3.Request request = chain.request();
                    Headers headers = request.headers().newBuilder().add("Authorization", authToken).build();
                    request = request.newBuilder().headers(headers).build();
                    return chain.proceed(request);
                }
            };

            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(30, TimeUnit.SECONDS)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .addInterceptor(headerAuthorizationInterceptor)
                    .build();

            Retrofit retrofit = new Retrofit.Builder().baseUrl(ApplicationConstant.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(okHttpClient)
                    .build();
            ApiService service = retrofit.create(ApiService.class);
            File file = new File(path);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", imageName, requestFile);
            Call<UploadToWebResponse> call = service.uploadImageToWeb(body);
            call.enqueue(new Callback<UploadToWebResponse>() {
                @Override
                public void onResponse(Call<UploadToWebResponse> call, Response<UploadToWebResponse> response) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    if (response != null && response.body() != null) {
                        if (response.body().getMessage() != null && response.body().getMessage().equalsIgnoreCase("successful")) {
                            uploadDataToWebAppServer(uploadDataToWebRequest);
                        } else {
                            Toast.makeText(IdentificationActivity.this, "Failed to upload image on server", Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<UploadToWebResponse> call, Throwable t) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    displayDialog("Result", t.toString());
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /*ProgressDialog dialog1;
    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        String path;
        UploadDataToWebRequest uploadDataToWebRequest;

        private UploadFileToServer(String path,UploadDataToWebRequest uploadDataToWebRequest) {
            this.path = path;
            this.uploadDataToWebRequest = uploadDataToWebRequest;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog1 = new ProgressDialog(IdentificationActivity.this);
            dialog1.setMessage("Please Wait...");
            dialog1.setIndeterminate(false);
            dialog1.setCancelable(false);
            dialog1.show();
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
                entity.addPart("file", new FileBody(sourceFile));
                java.net.URL url = new URL(ApplicationConstant.ENDPOINT_URL_WEB_UPLOAD);
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
                    result = "failed to connect web Server";
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
            Log.d("IdentificationActivity", "Response from server: " + result);
            super.onPostExecute(result);
            if (dialog1 != null && dialog1.isShowing()) {
                dialog1.dismiss();
            }
            String uploadResult="";
            try {
                JSONObject jsonObject = new JSONObject(result);
                uploadResult= jsonObject.optString("message");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Call service to upload data to server
            if(uploadResult.equalsIgnoreCase("successful")) {
                uploadDataToWebAppServer(uploadDataToWebRequest);
            }else{
                Toast.makeText(IdentificationActivity.this,"Failed to upload image on server",Toast.LENGTH_LONG).show();
            }
        }
    }*/

    ProgressDialog dialog1;
    private void uploadDataToWebAppServer(UploadDataToWebRequest uploadDataToWebRequest) {
        try {
            dialog1 = new ProgressDialog(this);
            dialog1.setMessage("Please Wait...");
            dialog1.setIndeterminate(false);
            dialog1.setCancelable(false);
            dialog1.show();
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            //Basic Auth
            final String authToken = Credentials.basic("admin", "1234");

            //Create a new Interceptor.
            Interceptor headerAuthorizationInterceptor = new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    okhttp3.Request request = chain.request();
                    Headers headers = request.headers().newBuilder().add("Authorization", authToken).build();
                    request = request.newBuilder().headers(headers).build();
                    return chain.proceed(request);
                }
            };

            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(30, TimeUnit.SECONDS)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .addInterceptor(headerAuthorizationInterceptor)
                    .build();
            Retrofit retrofit = new Retrofit.Builder().baseUrl(ApplicationConstant.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(okHttpClient)
                    .build();
            ApiService service = retrofit.create(ApiService.class);
            Call<UploadDataToWebResponse> call = null;
            if (uploadDataToWebRequest.getButt_category() != null && uploadDataToWebRequest.getButt_category().equalsIgnoreCase(ApplicationConstant.Moth)) {
                call = service.uploadDataToWebServerMoth(uploadDataToWebRequest);
            } else {
                call = service.uploadDataToWebServerButterFly(uploadDataToWebRequest);
            }

            call.enqueue(new Callback<UploadDataToWebResponse>() {
                @Override
                public void onResponse(Call<UploadDataToWebResponse> call, Response<UploadDataToWebResponse> response) {
                    if (dialog1 != null && dialog1.isShowing()) {
                        dialog1.dismiss();
                    }
                    if (response != null && response.body() != null) {
                        if (response.body().getStatus()) {
                            displayDialog("Result", response.body().getMessage());
                        } else {
                            displayDialog("Result", "Failed to upload data");
                        }
                    }
                }

                @Override
                public void onFailure(Call<UploadDataToWebResponse> call, Throwable t) {
                    if (dialog1 != null && dialog1.isShowing()) {
                        dialog1.dismiss();
                    }
                    displayDialog("Result", t.toString());
                }
            });
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void initView() {
        displayImage = (ImageView) findViewById(R.id.displayImage);
        textSciName1 = (TextView) findViewById(R.id.textSciName1);
        textSciName2 = (TextView) findViewById(R.id.textSciName2);
        textSciName3 = (TextView) findViewById(R.id.textSciName3);
        textComName1 = (TextView) findViewById(R.id.textComName1);
        textComName2 = (TextView) findViewById(R.id.textComName2);
        textComName3 = (TextView) findViewById(R.id.textComName3);
        textProbability1 = (TextView) findViewById(R.id.textProbability1);
        textProbability2 = (TextView) findViewById(R.id.textProbability2);
        textProbability3 = (TextView) findViewById(R.id.textProbability3);
        textCategory1 = (TextView) findViewById(R.id.textCategory1);
        textCategory2 = (TextView) findViewById(R.id.textCategory2);
        textCategory3 = (TextView) findViewById(R.id.textCategory3);
        result3 = (RelativeLayout) findViewById(R.id.result3);
        result2 = (RelativeLayout) findViewById(R.id.result2);
        progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
        progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);
        progressBar3 = (ProgressBar) findViewById(R.id.progressBar3);
        buttonBackIdentify = (Button) findViewById(R.id.buttonBackIdentify);
    }

    private void displayDialog(String result, String message) {
        final Dialog identificationDialog = new Dialog(IdentificationActivity.this);
        identificationDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        identificationDialog.setContentView(R.layout.dialog_popup);
        identificationDialog.setCanceledOnTouchOutside(false);
        TextView tv = (TextView) identificationDialog.findViewById(R.id.textMessage);
        tv.setText(message);
        TextView titleText = (TextView) identificationDialog.findViewById(R.id.dialogHeading);
        titleText.setText(result);
        Button btnLogoutNo = (Button) identificationDialog.findViewById(R.id.ok);
        btnLogoutNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                identificationDialog.dismiss();
            }
        });
        identificationDialog.show();
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
}
