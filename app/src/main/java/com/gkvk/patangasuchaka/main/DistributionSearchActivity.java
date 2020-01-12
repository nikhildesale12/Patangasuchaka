package com.gkvk.patangasuchaka.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.gkvk.R;

public class DistributionSearchActivity extends AppCompatActivity {

    private Toolbar toolbarDistribution;
    SearchView searchAddressView;
    Spinner spinnerButterfly,spinnerState;
    TextView butterflyTextview,stateTextview;;
    Button btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distribution_search);

        toolbarDistribution = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbarDistribution);

        searchAddressView=(SearchView) findViewById(R.id.searchAddressView);
        searchAddressView.setQueryHint("Search by Address");

        spinnerButterfly=(Spinner) findViewById(R.id.spinnerButterfly);
        spinnerState=(Spinner) findViewById(R.id.spinnerState);

        butterflyTextview=(TextView) findViewById(R.id.butterflyTextview);
        stateTextview=(TextView) findViewById(R.id.stateTextView);

        btnSearch=(Button) findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(getApplicationContext(),MapActivity.class);
                startActivity(i);
            }
        });
    }


}
