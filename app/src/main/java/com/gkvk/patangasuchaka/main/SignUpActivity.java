package com.gkvk.patangasuchaka.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import okhttp3.Credentials;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.gkvk.R;
import com.gkvk.patangasuchaka.bean.CommonResponse;
import com.gkvk.patangasuchaka.retrofit.ApiService;
import com.gkvk.patangasuchaka.util.ApplicationConstant;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;


public class Activity extends AppCompatActivity {

    ProgressDialog dialog;
    EditText editTextName,editTextEmailId,editTextUsername,editTextPassword,editTextConfirmPassword;
    CardView cardviewSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initView();

        cardviewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // do whatever you want to do on click (to launch any fragment or activity you need to put intent here.)
                /*Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);*/


                if(editTextName.getText().toString().trim().length() == 0){
                    editTextName.requestFocus();
                    editTextName.setError("Please Enter Full Name");
                }else if(editTextEmailId.getText().toString().trim().length() == 0) {
                    editTextEmailId.requestFocus();
                    editTextEmailId.setError("Please Enter Email Id");
                }else if(editTextUsername.getText().toString().trim().length() == 0){
                    editTextUsername.requestFocus();
                    editTextUsername.setError("Please Enter Username");
                }else if(editTextPassword.getText().toString().trim().length() == 0){
                    editTextPassword.requestFocus();
                    editTextPassword.setError("Please Enter Password");
                }else if(editTextConfirmPassword.getText().toString().trim().length() == 0 && !editTextPassword.equals(editTextConfirmPassword)){
                    editTextConfirmPassword.requestFocus();
                    editTextConfirmPassword.setError("Password Not Match");
                }else{
                    executeSignUpService();
                }

            }
        });
    }



    private void executeSignUpService() {
        dialog = new ProgressDialog(SignUpActivity.this);
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
        Call<CommonResponse> call = service.signUpService(editTextName.getText().toString(),editTextEmailId.getText().toString(),
                editTextUsername.getText().toString(),editTextPassword.getText().toString());
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if (response != null && response.body() != null) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    if (response.body().getStatus() != null && response.body().getStatus().toString().trim().equals("true")) {
                        Toast.makeText(SignUpActivity.this,response.body().getMessage(),Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }else{
                        ApplicationConstant.dispalyDialogInternet(SignUpActivity.this, "Invalid credentials", "Email and password does not match !!!", false, false);
                    }
                }
            }
            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                ApplicationConstant.dispalyDialogInternet(SignUpActivity.this, "Result", t.toString(), false, false);
            }
        });
    }



    private void initView() {
        cardviewSignUp = (CardView) findViewById(R.id.cardviewSignUp);
        editTextName = (EditText) findViewById(R.id.editTextName) ;
        editTextEmailId = (EditText) findViewById(R.id.editTextEmailId) ;
        editTextUsername = (EditText) findViewById(R.id.editTextUsername) ;
        editTextPassword = (EditText) findViewById(R.id.editTextPassword) ;
        editTextConfirmPassword = (EditText) findViewById(R.id.editTextConfirmPassword) ;
    }
}
