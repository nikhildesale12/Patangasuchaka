package com.gkvk.patangasuchaka.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gkvk.R;
import com.gkvk.patangasuchaka.bean.HistoryData;
import com.gkvk.patangasuchaka.bean.HistoryResponse;
import com.gkvk.patangasuchaka.retrofit.ApiService;
import com.gkvk.patangasuchaka.util.ApplicationConstant;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

public class DistributionSearchActivity extends AppCompatActivity {

    private Toolbar toolbarDistribution;
    AutoCompleteTextView searchAddressView;
    Spinner spinnerButterfly,spinnerState;
    Button btnSearch;
    ArrayAdapter<String> spinnerArrayAdapterButterfly;
    List<HistoryData> responseList = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distribution_search);

        initViews();

        setSupportActionBar(toolbarDistribution);
        toolbarDistribution.setTitle("Distribution");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),GoogleMapActivity.class);
                startActivity(i);
            }
        });

        setStateDataToSpinner();
        executeDistributionServiceData();

    } // oncreate end

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    ProgressDialog dialog;
    List<String> addressList = new ArrayList<>();
    List<String> butterflyList = new ArrayList<>();
    HashMap<String,HistoryData> hashMap;

    private void executeDistributionServiceData() {
        dialog = new ProgressDialog(DistributionSearchActivity.this);
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
        Call<HistoryResponse> call = service.getDistributionData();
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
                    displayDialog( "Result", message);
                }else
                if (response != null && response.body().getData() != null && response.body().getData().size()>0) {
                    responseList = response.body().getData();
                    Set<String> butterflySet = new HashSet<>();
                    Set<String> addressSet = new HashSet<>();
                    hashMap = new HashMap<>();
                    for (int i=0;i<response.body().getData().size();i++){
                        if(response.body().getData().get(i).getLng() != null && response.body().getData().get(i).getLng().trim().length()>0) {
                            butterflySet.add(response.body().getData().get(i).getOneCommonName());
                            hashMap.put(response.body().getData().get(i).getOneCommonName(),response.body().getData().get(i));

                            addressSet.add(response.body().getData().get(i).getPlaceCap());
                            hashMap.put(response.body().getData().get(i).getPlaceCap(),response.body().getData().get(i));
                        }
                    }
                    butterflyList = convertSetToList(butterflySet);
                    addressList = convertSetToList(addressSet);

                    Collections.sort(butterflyList);

                    butterflyList.add(0,"Select butterfly");
                    addressList.add(0,"Select address");

                    setButterFlyToDropdown(butterflyList);

                    setAddressAutoComplete(addressList);
                    
                }else{
                    displayDialog( "Failed", "Failed to get history of user !!!");
                }
            }
            @Override
            public void onFailure(Call<HistoryResponse> call, Throwable t) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                displayDialog( "Result", t.toString());
            }
        });
    }

    private void setAddressAutoComplete(List<String> addressList) {
        //Creating the instance of ArrayAdapter containing list of fruit names
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, addressList);
        //Getting the instance of AutoCompleteTextView
        searchAddressView.setThreshold(1);//will start working from first character
        searchAddressView.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        searchAddressView.setTextColor(Color.BLACK);

        searchAddressView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                // Notify the selected item text
                //Toast.makeText(getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT).show();
                HistoryData distributionBean = hashMap.get(selectedItemText);
                List<HistoryData> selectedBean = new ArrayList<>();
                selectedBean.add(distributionBean);

                searchAddressView.setText("");

                Intent i=new Intent(getApplicationContext(),GoogleMapActivity.class);
                //i.putExtra("distribution_bean", distributionBean);
                i.putParcelableArrayListExtra("distribution_list", (ArrayList<? extends Parcelable>) selectedBean);
                startActivity(i);
            }
        });
    }

    private void setButterFlyToDropdown(final List<String> butterflyList) {
        spinnerArrayAdapterButterfly = new ArrayAdapter<String>(
                this,R.layout.spinner_item,butterflyList) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                TextView txt = new TextView(DistributionSearchActivity.this);
                txt.setPadding(16, 16, 16, 16);
                txt.setTextSize(18);
                txt.setGravity(Gravity.CENTER_VERTICAL);
                txt.setText(butterflyList.get(position));
                txt.setTextColor(Color.parseColor("#000000"));
                return  txt;
            }

            public View getView(int i, View view, ViewGroup viewgroup) {
                TextView txt = new TextView(DistributionSearchActivity.this);
                txt.setGravity(Gravity.CENTER);
                txt.setPadding(16, 16, 16, 16);
                txt.setTextSize(16);
                txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_down_float, 0);
                txt.setText(butterflyList.get(i));
                txt.setTextColor(Color.parseColor("#000000"));
                return  txt;
            }
        };

        spinnerArrayAdapterButterfly.setDropDownViewResource(R.layout.spinner_item);
        spinnerButterfly.setAdapter(spinnerArrayAdapterButterfly);
        spinnerButterfly.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                if(position > 0){
                    // Notify the selected item text
                    //Toast.makeText(getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT).show();
                    HistoryData distributionBean = hashMap.get(selectedItemText);
                    List<HistoryData> selectedBean = new ArrayList<>();
                    selectedBean.add(distributionBean);

                    spinnerButterfly.setSelection(0);

                    Intent i=new Intent(getApplicationContext(),GoogleMapActivity.class);
                    //i.putExtra("distribution_bean", distributionBean);
                    i.putParcelableArrayListExtra("distribution_list", (ArrayList<? extends Parcelable>) selectedBean);
                    startActivity(i);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setStateDataToSpinner() {
        final List<String> stateList = new ArrayList<>();
        stateList.add("Select State");
        stateList.add("Andaman and Nicobar Islands");
        stateList.add("Andhra Pradesh");
        stateList.add("Arunachal Pradesh");
        stateList.add("Assam");
        stateList.add("Bihar");
        stateList.add("Chattisgarh");
        stateList.add("Chandigarh");
        stateList.add("Daman and Diu");
        stateList.add("Delhi");
        stateList.add("Dadra and Nagar Haveli");
        stateList.add("Goa");
        stateList.add("Gujarat");
        stateList.add("Himachal Pradesh");
        stateList.add("Haryana");
        stateList.add("Jammu and Kashmir");
        stateList.add("Jharkhand");
        stateList.add("Kerala");
        stateList.add("Karnataka");
        stateList.add("Lakshadweep");
        stateList.add("Meghalaya");
        stateList.add("Maharashtra");
        stateList.add("Manipur");
        stateList.add("Madhya Pradesh");
        stateList.add("Mizoram");
        stateList.add("Nagaland");
        stateList.add("Orissa");
        stateList.add("Punjab");
        stateList.add("Puducherry");
        stateList.add("Rajasthan");
        stateList.add("Sikkim");
        stateList.add("Tamil Nadu");
        stateList.add("Tripura");
        stateList.add("Uttarakhand");
        stateList.add("Uttar Pradesh");
        stateList.add("West Bengal");
        stateList.add("Telangana");

        final ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<String>(
                this,R.layout.spinner_item,stateList) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                TextView txt = new TextView(DistributionSearchActivity.this);
                txt.setPadding(16, 16, 16, 16);
                txt.setTextSize(18);
                txt.setGravity(Gravity.CENTER_VERTICAL);
                txt.setText(stateList.get(position));
                txt.setTextColor(Color.parseColor("#000000"));
                return  txt;
            }
            public View getView(int i, View view, ViewGroup viewgroup) {
                TextView txt = new TextView(DistributionSearchActivity.this);
                txt.setGravity(Gravity.CENTER);
                txt.setPadding(16, 16, 16, 16);
                txt.setTextSize(16);
                txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_down_float, 0);
                txt.setText(stateList.get(i));
                txt.setTextColor(Color.parseColor("#000000"));
                return  txt;
            }

        };

        spinnerArrayAdapter1.setDropDownViewResource(R.layout.spinner_item);
        spinnerState.setAdapter(spinnerArrayAdapter1);
        spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                if(position > 0){
                    List<HistoryData> stateSelectedData = new ArrayList<>();
                    //Toast.makeText(getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT).show();
                    if(responseList != null){
                        for(int i = 0;i<responseList.size();i++){
                            if(responseList.get(i).getPlaceCap().contains(selectedItemText)){
                                stateSelectedData.add(responseList.get(i));
                            }
                        }
                        spinnerState.setSelection(0);
                        Intent intent =new Intent(getApplicationContext(),GoogleMapActivity.class);
                        intent.putParcelableArrayListExtra("distribution_list", (ArrayList<? extends Parcelable>) stateSelectedData);
                        startActivity(intent);
                    }else{
                        Toast.makeText(DistributionSearchActivity.this,"Error while getting data",Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private List<String> convertSetToList(Set<String> set) {
        List<String> list = new ArrayList<>();
        for (String t : set) {
            list.add(t);
        }
        return list;
    }

    Dialog distDialog;
    private void displayDialog(String result, String message) {
        distDialog = new Dialog(DistributionSearchActivity.this);
        distDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        distDialog.setContentView(R.layout.dialog_popup);
        distDialog.setCanceledOnTouchOutside(false);
        TextView tv = (TextView) distDialog.findViewById(R.id.textMessage);
        tv.setText(message);
        TextView titleText = (TextView) distDialog.findViewById(R.id.dialogHeading);
        titleText.setText(result);
        Button btnOk = (Button) distDialog.findViewById(R.id.ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                distDialog.dismiss();
            }
        });
        distDialog.show();
    }

    private void initViews() {
        toolbarDistribution = (Toolbar) findViewById(R.id.toolbarDistribution);
        searchAddressView=(AutoCompleteTextView) findViewById(R.id.searchAddressView);
        spinnerButterfly=(Spinner) findViewById(R.id.spinnerButterfly);
        spinnerState=(Spinner) findViewById(R.id.spinnerState);
        btnSearch=(Button) findViewById(R.id.btnSearch);
    }
}
