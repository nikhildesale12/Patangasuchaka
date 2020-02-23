package com.gkvk.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.gkvk.R;
import com.gkvk.adapter.HistoryAdapter;
import com.gkvk.bean.FeedbackRequest;
import com.gkvk.bean.History;
import com.gkvk.bean.HistoryData;
import com.gkvk.bean.HistoryRequest;
import com.gkvk.bean.HistoryResponse;
import com.gkvk.bean.RegisterResponse;
import com.gkvk.entry.SplashNewActivity;
import com.gkvk.main.MainActivity;
import com.gkvk.main.SignUpActivity;
import com.gkvk.retrofit.ApiService;
import com.gkvk.util.ApplicationConstant;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static android.content.Context.MODE_PRIVATE;


public class HistoryFragment extends Fragment {
    ProgressDialog dialog;
    Button backFromViewHis;
    private OnFragmentInteractionListener mListener;

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    private List<HistoryData> historyList = new ArrayList<>();
    private RecyclerView recyclerView;
    private HistoryAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_history, container, false);
        initView(view);
        mAdapter = new HistoryAdapter(historyList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        executeHistoryService(view);

        backFromViewHis = (Button) view.findViewById(R.id.backFromViewHis);
        backFromViewHis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).setTitle("Home");
                Fragment fragment = new HomeFragment();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, fragment).commit();
            }
        });

        return view;
    }


    private void executeHistoryService(final View view) {
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

        SharedPreferences sharedPreferences = view.getContext().getSharedPreferences(ApplicationConstant.MY_PREFS_NAME, MODE_PRIVATE);
        String userName = sharedPreferences.getString(ApplicationConstant.KEY_USERNAME, "");

        ApiService service = retrofit.create(ApiService.class);
        HistoryRequest historyRequest = new HistoryRequest();
        historyRequest.setUsername(userName);
        //historyRequest.setUsername("charan123");
        Call<HistoryResponse> call = service.historyService(historyRequest);
        call.enqueue(new Callback<HistoryResponse>() {
            @Override
            public void onResponse(Call<HistoryResponse> call, Response<HistoryResponse> response) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (response != null && response.errorBody() != null) {
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
                    String message="";
                    try {
                        JSONObject jsonObject = new JSONObject(finallyError);
                        message = jsonObject.optString("message");
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    displayDialog(view.getContext(), "Result", message);
                }else
                if (response != null && response.body().getData() != null) {
                    historyList.addAll(response.body().getData());
                    mAdapter.notifyDataSetChanged();
                }else{
                    displayDialog(view.getContext(), "Failed", "Failed to get history of user !!!");
                }
            }
            @Override
            public void onFailure(Call<HistoryResponse> call, Throwable t) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                displayDialog(view.getContext(), "Result", t.toString());
            }
        });
    }

    private void displayDialog(final Context context, String result, String message) {
        final Dialog historyDialog = new Dialog(context);
        historyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        historyDialog.setContentView(R.layout.dialog_popup);
        historyDialog.setCanceledOnTouchOutside(false);
        TextView tv = (TextView) historyDialog.findViewById(R.id.textMessage);
        tv.setText(message);
        TextView titleText = (TextView) historyDialog.findViewById(R.id.dialogHeading);
        titleText.setText(result);
        Button btnOk = (Button) historyDialog.findViewById(R.id.ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                historyDialog.dismiss();
                ((MainActivity) getActivity()).setTitle("Home");
                Fragment fragment = new HomeFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
            }
        });
        historyDialog.show();
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }

        ((MainActivity) getActivity()).setTitle("Home");
        Fragment fragment = new HomeFragment();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, fragment).commit();
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
        void onFragmentInteraction(Uri uri);
    }

    private void initView(View view) {
        recyclerView=(RecyclerView) view.findViewById(R.id.recyclerView);
    }
}
