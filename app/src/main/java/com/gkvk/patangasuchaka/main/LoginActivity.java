package com.gkvk.patangasuchaka.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gkvk.R;
import com.gkvk.patangasuchaka.bean.CommonResponse;
import com.gkvk.patangasuchaka.bean.LoginRequest;
import com.gkvk.patangasuchaka.retrofit.ApiService;
import com.gkvk.patangasuchaka.util.ApplicationConstant;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    ProgressDialog dialog;
    CardView card_view;
    TextView textviewRegisterHere;
    EditText editText_email,editText_password;

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
                    editText_password.requestFocus();
                    editText_password.setError("Please Enter Password");
                }else {
                    if (ApplicationConstant.isNetworkAvailable(LoginActivity.this)) {
                        executeLoginService();
                    } else {
                        ApplicationConstant.dispalyDialogInternet(LoginActivity.this, "Internet Connection Issue", "Please check internet connection ...", false, false);
                    }
                }
            }
        });

        textviewRegisterHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
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
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
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
                    if (response.body().getStatus() != null && response.body().getStatus().toString().trim().equals("true")) {
                        Toast.makeText(LoginActivity.this,response.body().getMessage(),Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
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
                ApplicationConstant.dispalyDialogInternet(LoginActivity.this, "Result", t.toString(), false, false);
            }
        });
    }

    private void initView() {
        card_view = (CardView) findViewById(R.id.cardviewLogin);
        textviewRegisterHere = (TextView) findViewById(R.id.textviewRegisterHere);
        editText_email = (EditText) findViewById(R.id.editText_email) ;
        editText_password = (EditText) findViewById(R.id.editText_password) ;
    }


}
