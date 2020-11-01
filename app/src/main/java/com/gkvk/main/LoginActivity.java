package com.gkvk.main;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gkvk.R;
import com.gkvk.bean.CommonResponse;
import com.gkvk.bean.ForgotPassRequest;
import com.gkvk.bean.ForgotPassResponse;
import com.gkvk.bean.LoginGoogleRequest;
import com.gkvk.bean.LoginNewResponse;
import com.gkvk.bean.LoginRequest;
import com.gkvk.bean.ProfileResponse;
import com.gkvk.retrofit.ApiService;
import com.gkvk.util.ApplicationConstant;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
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

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    ProgressDialog dialog;
    CardView card_view;
    TextView textViewRegisterHere;
    EditText editText_email,editText_password;
    TextView textviewForgetPassword;
    ImageView signinGmail;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 007;
    String personPhotoUrl="";

    private static final String TAG = LoginActivity.class.getSimpleName();

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
                        ApplicationConstant.displayDialogInternet(LoginActivity.this, "Internet Connection Issue", "Please check internet connection ...", false, false);
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

        signinGmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ApplicationConstant.isNetworkAvailable(LoginActivity.this)){
                    signInGoogle();
                }else{
                    ApplicationConstant.displayDialogInternet(LoginActivity.this,"Internet Connection Issue","Please check internet connection ...",false,false);
                }
            }
        });

    }

    private void signInGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
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
        Call<LoginNewResponse> call = service.loginService(loginRequest);
        call.enqueue(new Callback<LoginNewResponse>() {
            @Override
            public void onResponse(Call<LoginNewResponse> call, Response<LoginNewResponse> response) {
                if (response != null && response.body() != null) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    if (response.body().getData().getId() != null) {

                        SharedPreferences.Editor editor1 = getSharedPreferences(ApplicationConstant.MY_PREFS_NAME, MODE_PRIVATE).edit();
                        editor1.putBoolean(ApplicationConstant.KEY_IS_LOGIN,true);
                        editor1.putString(ApplicationConstant.KEY_ID,response.body().getData().getId());
                        editor1.putString(ApplicationConstant.KEY_USERNAME,response.body().getData().getUsername());
                        editor1.putString(ApplicationConstant.KEY_EMAIL,response.body().getData().getEmail());
                        editor1.putString(ApplicationConstant.KEY_FULL_NAME,response.body().getData().getFull_name());
                        editor1.putString(ApplicationConstant.KEY_PROFILE_IMG,response.body().getData().getProfile_img());
                        editor1.commit();

//                        Toast toast = Toast.makeText(LoginActivity.this,"Login Successful", Toast.LENGTH_LONG);
//                        toast.setGravity(Gravity.CENTER, 0, 0);
//                        toast.show();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        ApplicationConstant.displayDialogInternet(LoginActivity.this, "Invalid credentials", "Email and password does not match !!!", false, false);
                    }
                }
            }
            @Override
            public void onFailure(Call<LoginNewResponse> call, Throwable t) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                //ApplicationConstant.dispalyDialogInternet(LoginActivity.this, "Result", t.toString(), false, false);
                ApplicationConstant.displayDialogInternet(LoginActivity.this, "Invalid credentials", "Invalid Credentials or verification is pending ", false, false);

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
                        ApplicationConstant.displayDialogInternet(LoginActivity.this,"Result",response.body().getData(),false,false);
                    }else{
                        ApplicationConstant.displayDialogInternet(LoginActivity.this,"Result","Failed to send password reset link",false,false);
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
                ApplicationConstant.displayDialogInternet(LoginActivity.this,"Result",t.getMessage(),false,false);
            }
        });
    }

    private void initView() {
        card_view = (CardView) findViewById(R.id.cardviewLogin);
        textViewRegisterHere = (TextView) findViewById(R.id.textviewRegisterHere);
        editText_email = (EditText) findViewById(R.id.editText_email) ;
        editText_password = (EditText) findViewById(R.id.editText_password) ;
        textviewForgetPassword = (TextView) findViewById(R.id.textviewForgetPassword);
        signinGmail = (ImageView) findViewById(R.id.signin_gmail);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(LoginActivity.this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            Log.e(TAG, "display name: " + acct.getDisplayName());

            String personName = acct.getDisplayName();
            if(acct.getPhotoUrl() != null){
                personPhotoUrl = acct.getPhotoUrl().toString();
            }
            String email = "";
            email = acct.getEmail();
            Log.e(TAG, "Name: " + personName + ", email: " + email + ", Image: " + personPhotoUrl);
            if(personPhotoUrl.length()>0){
                Picasso.with(LoginActivity.this)
                        .load(personPhotoUrl)
                        .into(getTarget());
            }
            if(email.length() > 0){
                if(ApplicationConstant.isNetworkAvailable(LoginActivity.this)){
                    if(email.length() > 0){
                        executeGoogleLoginService(personName,email);
                    }else{
                        ApplicationConstant.displayDialogInternet(LoginActivity.this,"Invalid Credentials","Invalid Email ID...",true,false);
                    }
                }else{
                    ApplicationConstant.displayDialogInternet(LoginActivity.this,"Internet Connection Issue","Please check internet connection ...",false,false);
                }
            }
        } else {
            Toast.makeText(LoginActivity.this,"unauthenticated user due to "+result.getStatus(),Toast.LENGTH_SHORT).show();
        }
    }

    private void executeGoogleLoginService(String personName, String email) {
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
        LoginGoogleRequest loginRequest = new LoginGoogleRequest();
        loginRequest.setEmail(email);
        loginRequest.setFull_name(personName);
        Call<LoginNewResponse> call = service.loginGoogleService(loginRequest);
        call.enqueue(new Callback<LoginNewResponse>() {
            @Override
            public void onResponse(Call<LoginNewResponse> call, Response<LoginNewResponse> response) {
                if (response != null && response.body() != null) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    if (response.body().getData().getId() != null) {

                        SharedPreferences.Editor editor1 = getSharedPreferences(ApplicationConstant.MY_PREFS_NAME, MODE_PRIVATE).edit();
                        editor1.putBoolean(ApplicationConstant.KEY_IS_LOGIN,true);
                        editor1.putString(ApplicationConstant.KEY_ID,response.body().getData().getId());
                        editor1.putString(ApplicationConstant.KEY_USERNAME,response.body().getData().getUsername());
                        editor1.putString(ApplicationConstant.KEY_EMAIL,response.body().getData().getEmail());
                        editor1.putString(ApplicationConstant.KEY_FULL_NAME,response.body().getData().getFull_name());
                        editor1.putString(ApplicationConstant.KEY_PROFILE_IMG,response.body().getData().getProfile_img());
                        editor1.putString(ApplicationConstant.KEY_PROFILE_IMG_GOOGLE,personPhotoUrl);

                        editor1.commit();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        ApplicationConstant.displayDialogInternet(LoginActivity.this, "Invalid credentials", "Email and password does not match !!!", false, false);
                    }
                }
            }
            @Override
            public void onFailure(Call<LoginNewResponse> call, Throwable t) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                //ApplicationConstant.dispalyDialogInternet(LoginActivity.this, "Result", t.toString(), false, false);
                ApplicationConstant.displayDialogInternet(LoginActivity.this, "Invalid credentials", "Invalid Credentials or verification is pending ", false, false);

            }
        });
    }

    private static Target getTarget(){
        Target target = new Target(){
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        //File file = new File(Environment.getExternalStorageDirectory().getPath() + "/" + url);
                        File file = new File(ApplicationConstant.FOLDER_PATH + File.separator + ApplicationConstant.USER_PHOTO);
                            try {
                            if(file != null && !file.exists()) {
                                file.createNewFile();
                                FileOutputStream ostream = new FileOutputStream(file);
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
                                ostream.flush();
                                ostream.close();
                            }
                        } catch (IOException e) {
                            Log.e("IOException", e.getLocalizedMessage());
                        }
                    }
                }).start();
            }
            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }
            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        return target;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(LoginActivity.this,"Failed to connect google server",Toast.LENGTH_SHORT).show();
    }
}
