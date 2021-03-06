package com.gkvk.entry;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.gkvk.R;
import com.gkvk.bean.AboutUsResponse;
import com.gkvk.main.LoginActivity;
import com.gkvk.main.MainActivity;
import com.gkvk.retrofit.ApiService;
import com.gkvk.util.ApplicationConstant;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Credentials;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SplashNewActivity extends AppCompatActivity {

    TextView versionName;
    String version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_new);

        SharedPreferences sharedPreferences = SplashNewActivity.this.getSharedPreferences(ApplicationConstant.MY_PREFS_NAME, MODE_PRIVATE);
        boolean isFieldModeOn = sharedPreferences.getBoolean(ApplicationConstant.KEY_IS_FEILDMODE, false);
        if(isFieldModeOn){
            Intent i = new Intent(SplashNewActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        } else{
            versionName = (TextView) findViewById(R.id.version_name);
            PackageInfo pInfo = null;
            try {
                pInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
                version = pInfo.versionName;
                versionName.setText("Version : "+version);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            getAppDataFromServer();

            Thread init = new Thread() {
                public void run() {
                    try {
                        sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        SharedPreferences sharedPreferences = SplashNewActivity.this.getSharedPreferences(ApplicationConstant.MY_PREFS_NAME, MODE_PRIVATE);
                        boolean login = sharedPreferences.getBoolean(ApplicationConstant.KEY_IS_LOGIN, false);
                        if (login) {
                            Intent i = new Intent(SplashNewActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        } else {
                            Intent i = new Intent(SplashNewActivity.this, LoginActivity.class);
                            startActivity(i);
                            finish();
                        }
                    }
                }
            };
            init.start();
        }
    }//end create

    private void getAppDataFromServer() {

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
        Call<AboutUsResponse> call = service.loadAboutData();
        call.enqueue(new Callback<AboutUsResponse>() {
            @Override
            public void onResponse(Call<AboutUsResponse> call, Response<AboutUsResponse> response) {
                if (response != null && response.body() != null) {
                    if (response.body() != null) {
                        SharedPreferences.Editor editor1 = getSharedPreferences(ApplicationConstant.MY_PREFS_NAME_INTRO, MODE_PRIVATE).edit();
                        editor1.putString(ApplicationConstant.KEY_INTRO, response.body().getData().getIntro());
                        editor1.putString(ApplicationConstant.KEY_ABOUT, response.body().getData().getAbout());
                        editor1.putString(ApplicationConstant.KEY_HOWITWORKS,response.body().getData().getHowItworks());
                        editor1.commit();
                    }
                }
            }
            @Override
            public void onFailure(Call<AboutUsResponse> call, Throwable t) {
                Log.e("Error",t.getMessage());
               //ApplicationConstant.dispalyDialogInternet(SplashNewActivity.this, "Result", t.toString(), false, true);
//                Intent i = new Intent(SplashNewActivity.this, LoginActivity.class);
//                startActivity(i);
//                finish();
            }
        });

    }


    // Intent i = new Intent(SplashNewActivity.this, MainActivity.class);
}
