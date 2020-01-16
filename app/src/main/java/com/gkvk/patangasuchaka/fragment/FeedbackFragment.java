package com.gkvk.patangasuchaka.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.gkvk.R;

import androidx.fragment.app.Fragment;


public class FeedbackFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    ProgressDialog dialog;
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


    // TODO: Rename and change types and number of parameters
    public static FeedbackFragment newInstance(String param1, String param2) {
        FeedbackFragment fragment = new FeedbackFragment();
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

        View view=inflater.inflate(R.layout.fragment_feedback, container, false);
        initView(view);

        btnFeedbacksubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextFBName.getText().toString().length()==0){

                    editTextFBName.requestFocus();
                    editTextFBName.setError("Please Enter Full Name");
                    //Toast.makeText(getContext(),"Please enter Name",Toast.LENGTH_SHORT).show();

                } else if(editTextFBEmailId.getText().toString().length()==0){
                    editTextFBEmailId.requestFocus();
                    editTextFBEmailId.setError("Please Enter EmailId");
                    //Toast.makeText(getContext(),"Please enter Emai id",Toast.LENGTH_SHORT).show();
                }
                else if(editTextFBContact.getText().toString().length()==0){
                    editTextFBContact.requestFocus();
                    editTextFBContact.setError("Please Enter Contact No");
                    //Toast.makeText(getContext(),"Please enter contact",Toast.LENGTH_SHORT).show();

                } else if(editText_FBComment.getText().toString().length()==0){
                    editText_FBComment.requestFocus();
                    editText_FBComment.setError("Please Give Feedback");
                    //Toast.makeText(getContext(),"Please enter Feedback",Toast.LENGTH_SHORT).show();

                }else {
                    executeFeedbackService();
                }
            }

        });

        return view;
    }

    private void executeFeedbackService() {

        /*dialog = new ProgressDialog(FeedbackFragment.this);
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
        Call<CommonResponse> call = service.feedbackService(editTextFBName.getText().toString(),editTextFBEmailId.getText().toString(),
                editTextFBContact.getText().toString(),editText_FBComment.getText().toString());
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

*/
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
        editTextFBName=(EditText) view.findViewById(R.id.editTextFBName);
        editTextFBEmailId=(EditText) view.findViewById(R.id.editTextFBEmailId);
        editTextFBContact=(EditText) view.findViewById(R.id.editTextFBContact);
        editText_FBComment=(EditText) view.findViewById(R.id.editText_FBComment);
        btnFeedbacksubmit=(Button) view.findViewById(R.id.btnFeedbacksubmit);
    }
}
