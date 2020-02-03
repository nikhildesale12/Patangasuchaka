package com.gkvk.patangasuchaka.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

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

import java.io.IOException;
import java.io.InputStream;


public class ButterflyFragment extends Fragment implements View.OnClickListener{

    String companies[] = {"Google", "Windows", "iPhone", "Nokia", "Samsung",
            "Google", "Windows", "iPhone", "Nokia", "Samsung",
            "Google", "Windows", "iPhone", "Nokia", "Samsung"};
    String os[] = {"Android", "Mango", "iOS", "Symbian", "Bada",
            "Android", "Mango", "iOS", "Symbian", "Bada",
            "Android", "Mango", "iOS", "Symbian", "Bada"};


    SearchView searchView;


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
        View view = inflater.inflate(R.layout.fragment_butterfly, container, false);
        searchView=(SearchView)view.findViewById(R.id.searchViewSpecies);

        String response = loadJSONFromAsset();

        addData(view);


        return view;
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open("butterfly.json");
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

    private TextView getTextView(int id, String title, int color, int typeface, int bgColor) {
        TextView tv = new TextView(getContext());
        tv.setId(id);
        tv.setText(title);
        tv.setTextColor(color);
        tv.setPadding(40, 40, 40, 40);
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
        params.setMargins(2, 0, 0, 2);
        return params;
    }

    @NonNull
    private TableLayout.LayoutParams getTblLayoutParams() {
        return new TableLayout.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT);
    }

    public void addData(View view) {
        int numCompanies = companies.length;
        TableLayout tl = view.findViewById(R.id.table);
        for (int i = 0; i < numCompanies; i++) {
            TableRow tr = new TableRow(view.getContext());
            tr.setLayoutParams(getLayoutParams());
            tr.addView(getTextView(i + 1, companies[i], Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(view.getContext(), R.color.white)));
            tr.addView(getTextView(i + numCompanies, os[i], Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(view.getContext(), R.color.white)));
            tl.addView(tr, getTblLayoutParams());
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
}