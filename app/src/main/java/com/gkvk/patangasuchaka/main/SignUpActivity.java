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

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gkvk.R;
import com.gkvk.patangasuchaka.bean.CaptchaImageView;
import com.gkvk.patangasuchaka.bean.CommonResponse;
import com.gkvk.patangasuchaka.bean.RegisterRequest;
import com.gkvk.patangasuchaka.bean.RegisterResponse;
import com.gkvk.patangasuchaka.retrofit.ApiService;
import com.gkvk.patangasuchaka.util.ApplicationConstant;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;


public class SignUpActivity extends AppCompatActivity {

    ProgressDialog dialog;
    EditText editTextName,editTextEmailId,editTextUsername,editTextPassword,editTextConfirmPassword;
    CardView cardviewSignUp;
    ImageView refresh;
    EditText captchEditText;
    CaptchaImageView captcha_Image_View;
    CheckBox checkboxTermCondition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initView();

        captcha_Image_View.setCaptchaType(3);
        refresh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SignUpActivity.this.captcha_Image_View.regenerate();
            }
        });

        cardviewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextName.getText().toString().trim().length() == 0){
                    editTextName.requestFocus();
                    editTextName.setError("Please enter full name");
                }else if(editTextEmailId.getText().toString().trim().length() == 0) {
                    editTextName.clearFocus();
                    editTextName.setError(null);
                    editTextEmailId.requestFocus();
                    editTextEmailId.setError("Please enter email Id");
                }else if(editTextUsername.getText().toString().trim().length() == 0){
                    editTextEmailId.clearFocus();
                    editTextEmailId.setError(null);
                    editTextUsername.requestFocus();
                    editTextUsername.setError("Please enter username");
                }else if(editTextPassword.getText().toString().trim().length() < 6){
                    editTextUsername.clearFocus();
                    editTextUsername.setError(null);
                    editTextPassword.requestFocus();
                    editTextPassword.setError("Please enter minimum 6 character password");
                }else if(editTextConfirmPassword.getText().toString().trim().length() == 0 || !editTextPassword.getText().toString().trim().equals(editTextConfirmPassword.getText().toString().trim())) {
                    editTextPassword.clearFocus();
                    editTextPassword.setError(null);
                    editTextConfirmPassword.requestFocus();
                    editTextConfirmPassword.setError("Password not match");
                } else if (captchEditText.getText().toString().trim().length() == 0) {
                    editTextConfirmPassword.clearFocus();
                    editTextConfirmPassword.setError(null);
                    //Toast.makeText(SignUpActivity.this, "Please enter captcha", Toast.LENGTH_SHORT).show();
                    captchEditText.setError("Please enter minimum 6 character password");
                } else if(!captchEditText.getText().toString().equals(captcha_Image_View.getCaptchaCode())){
                    //Toast.makeText(SignUpActivity.this, "Invalid captcha", Toast.LENGTH_SHORT).show();
                    captchEditText.setError(null);
                    Toast toast = Toast.makeText(SignUpActivity.this,"Invalid captcha", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if(!checkboxTermCondition.isChecked()) {
                    //Toast.makeText(SignUpActivity.this, "Accept terms and condition", Toast.LENGTH_SHORT).show();
                    Toast toast = Toast.makeText(SignUpActivity.this,"Accept terms and condition", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else{
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
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail(editTextEmailId.getText().toString());
        registerRequest.setFull_name(editTextName.getText().toString());
        registerRequest.setPassword(editTextPassword.getText().toString());
        registerRequest.setUsername(editTextUsername.getText().toString());
        Call<RegisterResponse> call = service.signUpService(registerRequest);
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (response != null && response.body() != null) {
                    if (response.body().getStatus()) {
                        dispalyDialog(SignUpActivity.this, "One more step", "Check email for verification");
                    }else{
                        ApplicationConstant.dispalyDialogInternet(SignUpActivity.this, "Failed", "Failed to register user !!!", false, false);
                    }
                } else if(response != null && response.errorBody() != null){
                    BufferedReader reader = null;
                    StringBuilder sb = new StringBuilder();
                    reader = new BufferedReader(new InputStreamReader(response.errorBody().byteStream()));
                    String line;
                    try {
                        while ((line = reader.readLine()) != null) {
                            sb.append(line);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    String finallyError = sb.toString();
                    ApplicationConstant.dispalyDialogInternet(SignUpActivity.this, "Result", finallyError, false, false);
                } else{
                    ApplicationConstant.dispalyDialogInternet(SignUpActivity.this, "Failed", "Failed to register user !!!", false, false);
                }
            }
            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                ApplicationConstant.dispalyDialogInternet(SignUpActivity.this, "Result", t.toString(), false, false);
            }
        });
    }

    private void dispalyDialog(SignUpActivity signUpActivity, String one_more_step, String check_email_for_verification) {
        final Dialog interrnetConnection = new Dialog(signUpActivity);
        interrnetConnection.requestWindowFeature(Window.FEATURE_NO_TITLE);
        interrnetConnection.setContentView(R.layout.dialog_popup);
        interrnetConnection.setCanceledOnTouchOutside(false);
        TextView tv = (TextView) interrnetConnection.findViewById(R.id.textMessage);
        tv.setText(check_email_for_verification);
        TextView titleText = (TextView) interrnetConnection.findViewById(R.id.dialogHeading);
        titleText.setText(one_more_step);
        Button btnLogoutNo = (Button) interrnetConnection.findViewById(R.id.ok);
        btnLogoutNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interrnetConnection.dismiss();
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        interrnetConnection.show();
    }

    private void initView() {
        cardviewSignUp = (CardView) findViewById(R.id.cardviewSignUp);
        editTextName = (EditText) findViewById(R.id.editTextName) ;
        editTextEmailId = (EditText) findViewById(R.id.editTextEmailId) ;
        editTextUsername = (EditText) findViewById(R.id.editTextUsername) ;
        editTextPassword = (EditText) findViewById(R.id.editTextPassword) ;
        editTextConfirmPassword = (EditText) findViewById(R.id.editTextConfirmPassword);
        refresh=(ImageView)findViewById(R.id.refresh);
        captchEditText=(EditText)findViewById(R.id.captchEditText);
        captcha_Image_View = (CaptchaImageView)findViewById(R.id.captcha_image_view);
        checkboxTermCondition = (CheckBox) findViewById(R.id.checkboxTermCondition);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
