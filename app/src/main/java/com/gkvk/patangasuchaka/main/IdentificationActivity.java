package com.gkvk.patangasuchaka.main;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gkvk.R;
import com.gkvk.patangasuchaka.bean.UploadDataToWebRequest;
import com.gkvk.patangasuchaka.bean.UploadDataToWebResponse;
import com.gkvk.patangasuchaka.retrofit.ApiService;
import com.gkvk.patangasuchaka.util.ApplicationConstant;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Credentials;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
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
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        setContentView(R.layout.activity_identification);
        initView();
        Intent intent = getIntent();
        String jsonResult = intent.getStringExtra("result");
        String path = intent.getStringExtra("path");
        Log.d("Result in Display page:",jsonResult);
        Log.d("path:",path);
        UploadDataToWebRequest uploadDataToWebRequest = new UploadDataToWebRequest();
        try {
            uploadDataToWebRequest.setImage("");
            uploadDataToWebRequest.setUsername("");
            uploadDataToWebRequest.setPlace_cap("");
            uploadDataToWebRequest.setDate_cap("");
            uploadDataToWebRequest.setLat("");
            uploadDataToWebRequest.setLng("");

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
                //Call service to upload data to server
                uploadDataToWebAppServer(uploadDataToWebRequest);
            }
            displayImage.setImageBitmap(BitmapFactory.decodeFile(path));

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

    private void uploadDataToWebAppServer(UploadDataToWebRequest uploadDataToWebRequest) {
        dialog = new ProgressDialog(this);
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
        Call<UploadDataToWebResponse> call = null;
        if(uploadDataToWebRequest.getButt_category()!=null && uploadDataToWebRequest.getButt_category().equalsIgnoreCase(ApplicationConstant.Butterfly))
        {
            call = service.uploadDataToWebServerButterFly(uploadDataToWebRequest);
        }else
        if(uploadDataToWebRequest.getButt_category()!=null && uploadDataToWebRequest.getButt_category().equalsIgnoreCase(ApplicationConstant.Moth))
        {
            call = service.uploadDataToWebServerMoth(uploadDataToWebRequest);
        }

        call.enqueue(new Callback<UploadDataToWebResponse>() {
            @Override
            public void onResponse(Call<UploadDataToWebResponse> call, Response<UploadDataToWebResponse> response) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (response != null && response.body() != null) {
                    if (response.body().getStatus()) {
                        dispalyDialog( "Result", response.body().getMessage());
                    }else{
                        dispalyDialog( "Result", "Failed to upload data");
                    }
                }
            }
            @Override
            public void onFailure(Call<UploadDataToWebResponse> call, Throwable t) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                dispalyDialog("Result", t.toString());
            }

            private void dispalyDialog(String result, String message) {
                final Dialog interrnetConnection = new Dialog(IdentificationActivity.this);
                interrnetConnection.requestWindowFeature(Window.FEATURE_NO_TITLE);
                interrnetConnection.setContentView(R.layout.dialog_popup);
                interrnetConnection.setCanceledOnTouchOutside(false);
                TextView tv = (TextView) interrnetConnection.findViewById(R.id.textMessage);
                tv.setText(message);
                TextView titleText = (TextView) interrnetConnection.findViewById(R.id.dialogHeading);
                titleText.setText(result);
                Button btnLogoutNo = (Button) interrnetConnection.findViewById(R.id.ok);
                btnLogoutNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        interrnetConnection.dismiss();
//                        Intent i = new Intent(view.getContext(),MainActivity.class);
//                        startActivity(i);
//                        activity.finish();
//                        activity.finishAffinity();

                    }
                });
                interrnetConnection.show();
            }
        });
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
}
