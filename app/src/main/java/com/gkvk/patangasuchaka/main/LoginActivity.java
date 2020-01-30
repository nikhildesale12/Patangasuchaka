package com.gkvk.patangasuchaka.main;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gkvk.R;
import com.gkvk.patangasuchaka.bean.CommonResponse;
import com.gkvk.patangasuchaka.bean.FeedbackRequest;
import com.gkvk.patangasuchaka.bean.ForgotPassRequest;
import com.gkvk.patangasuchaka.bean.ForgotPassResponse;
import com.gkvk.patangasuchaka.bean.LoginRequest;
import com.gkvk.patangasuchaka.bean.RegisterResponse;
import com.gkvk.patangasuchaka.retrofit.ApiService;
import com.gkvk.patangasuchaka.util.ApplicationConstant;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

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

public class LoginActivity extends AppCompatActivity {
    ProgressDialog dialog;
    CardView card_view;
    TextView textViewRegisterHere;
    EditText editText_email,editText_password;
    TextView textviewForgetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();

        card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText_email.getText().toString().length()==0){
                        // Toast.makeText(getApplicationContext(),"Please enter Name",Toast.LENGTH_SHORT).show();
                        editText_email.requestFocus();
                        editText_email.setError("Please Enter Email id");
                } else if(editText_password.getText().toString().length()==0){
                    // Toast.makeText(getApplicationContext(),"Please enter Password",Toast.LENGTH_SHORT).show();
                    editText_email.setError(null);
                    editText_password.requestFocus();
                    editText_password.setError("Please Enter Password");
                }else {
                    editText_password.setError(null);
                    if (ApplicationConstant.isNetworkAvailable(LoginActivity.this)) {
                        executeLoginService();
                    } else {
                        ApplicationConstant.dispalyDialogInternet(LoginActivity.this, "Internet Connection Issue", "Please check internet connection ...", false, false);
                    }
                }
            }
        });

        textViewRegisterHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });

        textviewForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayDialog();
            }
        });

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
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(editText_email.getText().toString());
        loginRequest.setPassword(editText_password.getText().toString());
        Call<CommonResponse> call = service.loginService(loginRequest);
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if (response != null && response.body() != null) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    if (response.body().getId() != null) {

                        SharedPreferences.Editor editor1 = getSharedPreferences(ApplicationConstant.MY_PREFS_NAME, MODE_PRIVATE).edit();
                        editor1.putBoolean(ApplicationConstant.KEY_IS_LOGIN,true);
                        editor1.putString(ApplicationConstant.KEY_ID,response.body().getId());
                        editor1.putString(ApplicationConstant.KEY_USERNAME,response.body().getUsername());
                        editor1.putString(ApplicationConstant.KEY_EMAIL,response.body().getEmail());
                        editor1.putString(ApplicationConstant.KEY_FULL_NAME,response.body().getFull_name());
                        editor1.putString(ApplicationConstant.KEY_PROFILE_IMG,response.body().getProfile_img());
                        editor1.commit();

//                        Toast toast = Toast.makeText(LoginActivity.this,"Login Successful", Toast.LENGTH_LONG);
//                        toast.setGravity(Gravity.CENTER, 0, 0);
//                        toast.show();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        ApplicationConstant.dispalyDialogInternet(LoginActivity.this, "Invalid credentials", "Email and password does not match !!!", false, false);
                    }
                }
            }
            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                //ApplicationConstant.dispalyDialogInternet(LoginActivity.this, "Result", t.toString(), false, false);
                ApplicationConstant.dispalyDialogInternet(LoginActivity.this, "Invalid credentials", "Invalid Credentials or verification is pending ", false, false);

            }
        });
    }


    Dialog dialogForgotPassword;
    EditText ed;
    public void displayDialog() {
        dialogForgotPassword = new Dialog(LoginActivity.this);
        dialogForgotPassword.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogForgotPassword.setContentView(R.layout.forgotpassword_popup);
        dialogForgotPassword.setCanceledOnTouchOutside(false);
        ed = (EditText) dialogForgotPassword.findViewById(R.id.editEmailForgotPass);
        final Button btnOk = (Button) dialogForgotPassword.findViewById(R.id.ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailId=ed.getText().toString();
                if(emailId.length()==0){
                    // Toast.makeText(getApplicationContext(),"Please enter Name",Toast.LENGTH_SHORT).show();
                    ed.requestFocus();
                    ed.setError("Please Enter Email id");
                }else {
                    executeForgotPasService(emailId);
                }
            }
        });
        Button btnCancel = (Button) dialogForgotPassword.findViewById(R.id.cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogForgotPassword.dismiss();
            }
        });
        dialogForgotPassword.show();
    }

    private void executeForgotPasService(String emailId) {

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
        ForgotPassRequest forgotPassRequest = new ForgotPassRequest();
        forgotPassRequest.setEmail(emailId);

        Call<ForgotPassResponse> call = service.forgotPassService(forgotPassRequest);
        call.enqueue(new Callback<ForgotPassResponse>() {
            @Override
            public void onResponse(Call<ForgotPassResponse> call, Response<ForgotPassResponse> response) {
                if(dialogForgotPassword != null)
                dialogForgotPassword.dismiss();

                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (response != null && response.body() != null) {
                    if (response.body().getStatus()) {
                        ApplicationConstant.dispalyDialogInternet(LoginActivity.this,"Result",response.body().getData(),false,false);
                    }else{
                        ApplicationConstant.dispalyDialogInternet(LoginActivity.this,"Result","Failed to send password reset link",false,false);
                    }
                }
            }

            @Override
            public void onFailure(Call<ForgotPassResponse> call, Throwable t) {
                if(dialogForgotPassword != null)
                dialogForgotPassword.dismiss();

                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                ApplicationConstant.dispalyDialogInternet(LoginActivity.this,"Result",t.getMessage(),false,false);
            }
        });
    }

    private void initView() {
        card_view = (CardView) findViewById(R.id.cardviewLogin);
        textViewRegisterHere = (TextView) findViewById(R.id.textviewRegisterHere);
        editText_email = (EditText) findViewById(R.id.editText_email) ;
        editText_password = (EditText) findViewById(R.id.editText_password) ;
        textviewForgetPassword = (TextView) findViewById(R.id.textviewForgetPassword);
    }
}
