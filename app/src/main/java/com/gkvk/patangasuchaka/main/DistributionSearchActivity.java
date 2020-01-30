package com.gkvk.patangasuchaka.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gkvk.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DistributionSearchActivity extends AppCompatActivity {

    private Toolbar toolbarDistribution;
    SearchView searchAddressView;
    Spinner spinnerButterfly,spinnerState;
    Button btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distribution_search);

        initViews();

        setSupportActionBar(toolbarDistribution);

        searchAddressView.setQueryHint("Search by Address");

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(getApplicationContext(),GoogleMapActivity.class);
                startActivity(i);
            }
        });


        // Initializing a String Array
        String[] butterfly = new String[]{
                "Select Buterfly...",
                "California sycamore",
                "Mountain mahogany",
                "Butterfly weed",
                "Carrot weed"
        };

        String[] state = new String[]{
                "Select State...",
                "Maharashtra",
                "Goa",
                "Gujrat",
                "Bihar"
        };



        final List<String> butterflyList = new ArrayList<>(Arrays.asList(butterfly));
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
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
                txt.setTextColor(Color.parseColor("#909092"));
                return  txt;
            }
        };

        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerButterfly.setAdapter(spinnerArrayAdapter);
        spinnerButterfly.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);

                if(position > 0){
                    // Notify the selected item text
                    Toast.makeText
                            (getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        final List<String> stateList = new ArrayList<>(Arrays.asList(state));
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
                txt.setTextColor(Color.parseColor("#909092"));
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
                    // Notify the selected item text
                    Toast.makeText
                            (getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void initViews() {
        toolbarDistribution = (Toolbar) findViewById(R.id.toolbar);
        searchAddressView=(SearchView) findViewById(R.id.searchAddressView);
        spinnerButterfly=(Spinner) findViewById(R.id.spinnerButterfly);
        spinnerState=(Spinner) findViewById(R.id.spinnerState);
        btnSearch=(Button) findViewById(R.id.btnSearch);



    }

}
