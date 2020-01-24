package com.gkvk.patangasuchaka.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gkvk.R;
import com.gkvk.patangasuchaka.bean.CommonResponse;
import com.gkvk.patangasuchaka.bean.HistoryRequest;
import com.gkvk.patangasuchaka.bean.HistoryResponse;
import com.gkvk.patangasuchaka.bean.ProfileRequest;
import com.gkvk.patangasuchaka.bean.ProfileResponse;
import com.gkvk.patangasuchaka.main.MainActivity;
import com.gkvk.patangasuchaka.retrofit.ApiService;
import com.gkvk.patangasuchaka.util.ApplicationConstant;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Credentials;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    ProgressDialog dialog;

    TextView fullname_Profile, username_Profile,email_Profile;
    CircleImageView profile_image;
    CardView cardviewBackProfile;
    Activity activity;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_profile, container, false);
        initView(view);

        cardviewBackProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(ProfileFragment.this, MainActivity.class);
                startActivity(intent);
                finish();*/
            }
        });

        executeProfileService(view);

        return view;
    }

    private void executeProfileService(final View view) {
        dialog = new ProgressDialog(view.getContext());
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
        ProfileRequest profileRequest = new ProfileRequest();
        SharedPreferences sharedPreferences = view.getContext().getSharedPreferences(ApplicationConstant.MY_PREFS_NAME, MODE_PRIVATE);
        String id = sharedPreferences.getString(ApplicationConstant.KEY_ID, "");
        profileRequest.setId(id);

        Call<ProfileResponse> call = service.profileService(profileRequest);
        call.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (response != null && response.body() != null) {
                    if (response.body().getStatus()) {
                        //dispalyDialog(activity,view.getContext(), "Result", response.body().getMessage());
                        fullname_Profile.setText(response.body().getData().getFullName());
                        username_Profile.setText(response.body().getData().getUsername());
                        email_Profile.setText(response.body().getData().getEmail());

                    }else{
                        //set data from sharedpreference
                    }
                }else{
                    //set data from sharedpreference
                }
            }
            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                dispalyDialog(activity,view.getContext(), "Result", t.toString());
            }

            private void dispalyDialog(final Activity activity, final Context context, String result, String message) {
                final Dialog interrnetConnection = new Dialog(context);
                interrnetConnection.requestWindowFeature(Window.FEATURE_NO_TITLE);
                interrnetConnection.setContentView(R.layout.dialog_popup);
                interrnetConnection.setCanceledOnTouchOutside(false);
                TextView tv = (TextView) interrnetConnection.findViewById(R.id.textMessage);
                tv.setText(message);
                TextView titleText = (TextView) interrnetConnection.findViewById(R.id.dialogHeading);
                titleText.setText(result);
                Button btnOk = (Button) interrnetConnection.findViewById(R.id.ok);
                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        interrnetConnection.dismiss();
                        Intent i = new Intent(view.getContext(), MainActivity.class);
                        startActivity(i);
                        //activity.finish();
                        //activity.finishAffinity();
                    }
                });
                interrnetConnection.show();
            }
        });

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void initView(View view) {
        profile_image=(CircleImageView) view.findViewById(R.id.profile_image);
        fullname_Profile=(TextView)view.findViewById(R.id.fullname_profile);
        username_Profile=(TextView)view.findViewById(R.id.username_profile);
        email_Profile=(TextView)view.findViewById(R.id.email_profile);
        cardviewBackProfile=(CardView)view.findViewById(R.id.cardviewBackProfile);
    }
}
