package com.gkvk.patangasuchaka.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gkvk.R;
import com.gkvk.patangasuchaka.bean.FeedbackRequest;
import com.gkvk.patangasuchaka.bean.RegisterResponse;
import com.gkvk.patangasuchaka.main.MainActivity;
import com.gkvk.patangasuchaka.retrofit.ApiService;
import com.gkvk.patangasuchaka.util.ApplicationConstant;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import androidx.fragment.app.Fragment;

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

import static android.content.Context.MODE_PRIVATE;


public class FeedbackFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private ProgressDialog dialog;
    EditText editTextFBName,editTextFBEmailId,editTextFBContact,editText_FBComment;
    Button btnFeedbacksubmit;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    public FeedbackFragment() {
        // Required empty public constructor
    }


   /* // TODO: Rename and change types and number of parameters
    public static FeedbackFragment newInstance(String param1, String param2) {
        FeedbackFragment fragment = new FeedbackFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }*/

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

        final View view=inflater.inflate(R.layout.fragment_feedback, container, false);
        initView(view);

        SharedPreferences sharedPreferences = view.getContext().getSharedPreferences(ApplicationConstant.MY_PREFS_NAME, MODE_PRIVATE);
        String fullName = sharedPreferences.getString(ApplicationConstant.KEY_FULL_NAME, "");
        String email = sharedPreferences.getString(ApplicationConstant.KEY_EMAIL, "");
        editTextFBName.setText(fullName);
        editTextFBEmailId.setText(email);
        btnFeedbacksubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ApplicationConstant.isNetworkAvailable(getContext())) {
                    if (editTextFBName.getText().toString().length() == 0) {
                        editTextFBName.requestFocus();
                        editTextFBName.setError("Please Enter Full Name");
                        //Toast.makeText(getContext(),"Please enter Name",Toast.LENGTH_SHORT).show();
                    } else if (editTextFBEmailId.getText().toString().length() == 0) {
                        editTextFBName.setError(null);
                        editTextFBEmailId.requestFocus();
                        editTextFBEmailId.setError("Please Enter EmailId");
                        //Toast.makeText(getContext(),"Please enter Emai id",Toast.LENGTH_SHORT).show();
                    } else if (editTextFBContact.getText().toString().length() == 0) {
                        editTextFBEmailId.setError(null);
                        editTextFBContact.requestFocus();
                        editTextFBContact.setError("Please Enter Contact No");
                        //Toast.makeText(getContext(),"Please enter contact",Toast.LENGTH_SHORT).show();

                    } else if (editText_FBComment.getText().toString().length() == 0) {
                        editTextFBContact.setError(null);
                        editText_FBComment.requestFocus();
                        editText_FBComment.setError("Please Give Feedback");
                        //Toast.makeText(getContext(),"Please enter Feedback",Toast.LENGTH_SHORT).show();
                    } else {
                        executeFeedbackService(view);
                    }
                }else{
                    ApplicationConstant.displayDialogInternet(getContext(),"Connection Error","Check internet connection",false,false);
                }
            }

        });

        return view;
    }

    private void executeFeedbackService(final View view) {
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
        FeedbackRequest feedbackRequest = new FeedbackRequest();
        feedbackRequest.setContact(editTextFBContact.getText().toString());
        feedbackRequest.setEmail(editTextFBEmailId.getText().toString());
        feedbackRequest.setFull_name(editTextFBName.getText().toString());
        feedbackRequest.setFeedback(editText_FBComment.getText().toString());
        Call<RegisterResponse> call = service.feedbackService(feedbackRequest);
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (response != null && response.body() != null) {
                    if (response.body().getStatus()) {
                        displayDialog(view.getContext(), "Result", response.body().getMessage());
                    }else{
                        displayDialog(view.getContext(), "Result", "Failed to submit your feedback");
                    }
                }
            }
            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                displayDialog(view.getContext(), "Result", t.toString());
            }
        });
    }


    private void displayDialog(final Context context, String result, String message) {
        final Dialog fragmentDialog = new Dialog(context);
        fragmentDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        fragmentDialog.setContentView(R.layout.dialog_popup);
        fragmentDialog.setCanceledOnTouchOutside(false);
        TextView tv = (TextView) fragmentDialog.findViewById(R.id.textMessage);
        tv.setText(message);
        TextView titleText = (TextView) fragmentDialog.findViewById(R.id.dialogHeading);
        titleText.setText(result);
        Button btnLogoutNo = (Button) fragmentDialog.findViewById(R.id.ok);
        btnLogoutNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentDialog.dismiss();
                ((MainActivity) getActivity()).setTitle("Home");
                Fragment fragment = new HomeFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();

            }
        });
        fragmentDialog.show();
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
        editTextFBName=(EditText) view.findViewById(R.id.editTextFBName);
        editTextFBEmailId=(EditText) view.findViewById(R.id.editTextFBEmailId);
        editTextFBContact=(EditText) view.findViewById(R.id.editTextFBContact);
        editText_FBComment=(EditText) view.findViewById(R.id.editText_FBComment);
        btnFeedbacksubmit=(Button) view.findViewById(R.id.btnFeedbacksubmit);
    }
}
