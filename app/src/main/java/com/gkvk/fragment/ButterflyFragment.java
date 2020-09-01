package com.gkvk.fragment;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.gkvk.R;
import com.gkvk.bean.SpeciesData;
import com.gkvk.adapter.ButterflyAdapter;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class ButterflyFragment extends Fragment implements View.OnClickListener{

    SearchView searchView;
    private List<SpeciesData> butterflyList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ButterflyAdapter mAdapter;

    public ButterflyFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_butterfly_new, container, false);
        searchView=(SearchView)view.findViewById(R.id.searchViewSpecies);
        recyclerView=(RecyclerView) view.findViewById(R.id.recyclerViewSpecies);
        mAdapter = new ButterflyAdapter(butterflyList,butterflyList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        new loadDataFromAsset(view).execute();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return view;
    }



    //OLD CODE
    private TextView getTextView(int id, String title, int color, int typeface, int bgColor) {
        TextView tv = new TextView(getContext());
        tv.setId(id);
        tv.setText(title);
        tv.setTextColor(color);
        tv.setPadding(20, 20, 20, 20);
        tv.setTypeface(Typeface.DEFAULT, typeface);
        tv.setBackgroundColor(bgColor);
        tv.setLayoutParams(getLayoutParams());
        tv.setOnClickListener(this);
        return tv;
    }

    @NonNull
    private TableRow.LayoutParams getLayoutParams() {
        TableRow.LayoutParams params = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        params.setMargins(1, 0, 0, 2);
        return params;
    }

    @NonNull
    private TableLayout.LayoutParams getTblLayoutParams() {
        return new TableLayout.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT);
    }

    public void addData(View view,String response) {
        TableLayout tl = view.findViewById(R.id.table);
        if(response !=null){
            try {
                JSONArray jsonArray = new JSONArray(response);
                if(jsonArray != null && jsonArray.length()>0){
                    for(int i=0;i<jsonArray.length();i++){
                        TableRow tr = new TableRow(view.getContext());
                        tr.setLayoutParams(getLayoutParams());
                        tr.addView(getTextView(i + 1, jsonArray.getJSONObject(i).optString("species_list"), Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(view.getContext(), R.color.white)));
                        tr.addView(getTextView(i + jsonArray.length(), jsonArray.getJSONObject(i).optString("common_name"), Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(view.getContext(), R.color.white)));
                        tl.addView(tr, getTblLayoutParams());
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        TextView tv = v.findViewById(id);
        if (null != tv) {
            Log.i("onClick", "Clicked on row :: " + id);
            Toast.makeText(v.getContext(), "Clicked on row :: " + id + ", Text :: " + tv.getText(), Toast.LENGTH_SHORT).show();
        }
    }

    ProgressDialog dialog;
    private class loadDataFromAsset extends AsyncTask<Void, Integer, String> {
        View view;

        private loadDataFromAsset(View view) {
            this.view = view;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(view.getContext());
            dialog.setMessage("Please Wait...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            String responseButterfly = loadButterflyJSONFromAsset();
            //addData(view,responseButterfly);

            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(responseButterfly);
                if(jsonArray != null && jsonArray.length()>0){
                    for(int i=0;i<jsonArray.length();i++){
                        SpeciesData speciesData = new SpeciesData();
                        speciesData.setSpeciesName(jsonArray.getJSONObject(i).optString("species_list"));
                        speciesData.setCommonName(jsonArray.getJSONObject(i).optString("common_name"));
                        butterflyList.add(speciesData);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    public String loadButterflyJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getContext().getAssets().open("butterfly.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

}