package com.gkvk.patangasuchaka.main;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.SyncStateContract;

import com.gkvk.R;
import com.gkvk.patangasuchaka.retrofit.ApiService;
import com.gkvk.patangasuchaka.util.ApplicationConstant;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /*if (ApplicationConstant.isNetworkAvailable(LoginActivity.this)) {
            executeLoginService();
        } else {
            ApplicationConstant.dispalyDialogInternet(LoginActivity.this, "Internet Connection Issue", "Please check internet connection ...", false, false);
        }
    }

    private void executeLoginService() {
        dialog = new ProgressDialog(LoginActivity.this);
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
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ApplicationConstant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();
        ApiService service = retrofit.create(ApiService.class);
        Call<LoginResponse> call = service.loginService(USER, PASS, GOOG_ID);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response != null && response.body() != null) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    if (response.body().getSuccess().toString().trim().equals("1")) {
                        //dispalyToast(response.body().getResult());
                        SharedPreferences.Editor editor1 = getSharedPreferences(Constants.MY_PREFS_LOGIN, MODE_PRIVATE).edit();
                        editor1.putString(Constants.KEY_USERNAME, USER);
                        editor1.putString(Constants.KEY_USERID, response.body().getUserId());
                        editor1.putString(Constants.KEY_PHOTO, personPhotoUrl);
                        editor1.putBoolean(Constants.KEY_IS_LOGIN, true);
                        *//*if(keepMeSignin.isChecked()){
                            editor1.putBoolean(Constants.KEY_IS_LOGIN,true);
                        }else{
                            editor1.putBoolean(Constants.KEY_IS_LOGIN,false);
                        }*//*
                        editor1.commit();
                        if (response.body().getIsNameAvailable().equals("YES")) {
                            SharedPreferences.Editor editor2 = getSharedPreferences(Constants.MY_PREFS_USER_INFO, MODE_PRIVATE).edit();
                            editor2.putString(Constants.KEY_FIRSTNAME, response.body().getFirstName());
                            editor2.putString(Constants.KEY_MIDDLENAME, response.body().getMiddleName());
                            editor2.putString(Constants.KEY_LASTNAME, response.body().getLastName());
                            editor2.putString(Constants.KEY_OCCUPATION, response.body().getOccupation());
                            editor2.putString(SyncStateContract.Constants.KEY_MOBILE, response.body().getMobile());
                            editor2.commit();
                            SharedPreferences prefs = getSharedPreferences(SyncStateContract.Constants.MY_PREFS_SWIPE, MODE_PRIVATE);
                            boolean isShowScreen = prefs.getBoolean(SyncStateContract.Constants.KEY_ONE_TIME_PAGE, true);
                            if (isShowScreen) {
                                SharedPreferences.Editor editor3 = getSharedPreferences(SyncStateContract.Constants.MY_PREFS_SWIPE, MODE_PRIVATE).edit();
                                editor3.putBoolean(SyncStateContract.Constants.KEY_ONE_TIME_PAGE, false);
                                editor3.commit();
                                Intent intent = new Intent(LoginActivity.this, AboutActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Intent intent = new Intent(LoginActivity.this, Dashboard.class);
                                intent.putExtra("uploadedCount", Constants.FROM_);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            getUserDetailPopup(response.body().getUserId());
                        }
                    } else if (response.body().getSuccess().toString().trim().equals("0")) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        Constants.dispalyDialogInternet(LoginMainActivity.this, "Result", response.body().getResult(), true, false);
                    } else {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        Constants.dispalyDialogInternet(LoginMainActivity.this, "Error", "Technical Error !!!", false, false);
                    }
                } else {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Constants.dispalyDialogInternet(LoginMainActivity.this, "Error", "Technical Error !!!", false, false);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                //Log.d("response :","");
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                Constants.dispalyDialogInternet(LoginMainActivity.this, "Result", t.toString(), false, false);
            }
        });*/

    }
}
