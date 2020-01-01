package com.gkvk.patangasuchaka.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gkvk.R;

import org.json.JSONArray;
import org.json.JSONObject;

public class IdentificationActivity extends AppCompatActivity {
    ImageView displayImage;
    TextView textSciName1,textSciName2,textSciName3;
    TextView textComName1,textComName2,textComName3;
    TextView textProbability1,textProbability2,textProbability3;
    RelativeLayout result3,result2;
    ProgressBar progressBar1,progressBar2,progressBar3;
    Button buttonBackIdentify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        setContentView(R.layout.activity_identification);
        initView();
        Intent intent = getIntent();
        String jsonResult = intent.getStringExtra("result");
        String path = intent.getStringExtra("path");
        Log.d("Result in Display page:",jsonResult);
        Log.d("path:",path);

        try {
            JSONArray jsonArray = new JSONArray(jsonResult);
            if(jsonArray != null && jsonArray.length()>0) {
                JSONObject jsonResult1 = jsonArray.getJSONObject(0);
                textSciName1.setText(jsonResult1.optString("butterfly_sn"));
                textComName1.setText(jsonResult1.optString("butterfly_cn"));
                textProbability1.setText(jsonResult1.optString("probability"));
                if(!jsonResult1.optString("probability").equalsIgnoreCase("NONE") && !jsonResult1.optString("probability").equalsIgnoreCase("NA")){
                    String progress1Array[] = jsonResult1.optString("probability").split("\\.");
                    int progress1 = 0;
                    if(progress1Array.length>0) {
                        progress1 = Integer.parseInt(progress1Array[0]);
                    }
                    progressBar1.setProgress(progress1);
                }
                if(jsonArray.length()>1){
                    JSONObject jsonResult2 = jsonArray.getJSONObject(1);
                    textSciName2.setText(jsonResult2.optString("butterfly_sn"));
                    textComName2.setText(jsonResult2.optString("butterfly_cn"));
                    if(jsonResult2.optString("butterfly_sn").equals("NA")){
                        result2.setVisibility(View.INVISIBLE);
                    }
                    textProbability2.setText(jsonResult2.optString("probability"));
                    if(!jsonResult2.optString("probability").equalsIgnoreCase("NONE") && !jsonResult2.optString("probability").equalsIgnoreCase("NA")) {
                        String progress2Array[] = jsonResult2.optString("probability").split("\\.");
                        int progress2 = 0;
                        if (progress2Array.length > 0) {
                            progress2 = Integer.parseInt(progress2Array[0]);
                        }
                        progressBar2.setProgress(progress2);
                    }else{
                        result2.setVisibility(View.INVISIBLE);
                    }
                }
                if(jsonArray.length()>2){
                    JSONObject jsonResult3 = jsonArray.getJSONObject(2);
                    textSciName3.setText(jsonResult3.optString("butterfly_sn"));
                    textComName3.setText(jsonResult3.optString("butterfly_cn"));
                    if(jsonResult3.optString("butterfly_sn").equals("NA")){
                        result3.setVisibility(View.INVISIBLE);
                    }
                    textProbability3.setText(jsonResult3.optString("probability"));
                    if(!jsonResult3.optString("probability").equalsIgnoreCase("NONE") && !jsonResult3.optString("probability").equalsIgnoreCase("NA")) {
                        String progress3Array[] = jsonResult3.optString("probability").split("\\.");
                        int progress3 = 0;
                        if(progress3Array.length>0) {
                           progress3 = Integer.parseInt(progress3Array[0]);
                        }
                        progressBar3.setProgress(progress3);
                    }else{
                        result3.setVisibility(View.INVISIBLE);
                    }
                }
            }
            displayImage.setImageBitmap(BitmapFactory.decodeFile(path));

            buttonBackIdentify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    private void initView() {
        displayImage = (ImageView) findViewById(R.id.displayImage);
        textSciName1 = (TextView) findViewById(R.id.textSciName1);
        textSciName2 = (TextView) findViewById(R.id.textSciName2);
        textSciName3 = (TextView) findViewById(R.id.textSciName3);
        textComName1 = (TextView) findViewById(R.id.textComName1);
        textComName2 = (TextView) findViewById(R.id.textComName2);
        textComName3 = (TextView) findViewById(R.id.textComName3);
        textProbability1 = (TextView) findViewById(R.id.textProbability1);
        textProbability2 = (TextView) findViewById(R.id.textProbability2);
        textProbability3 = (TextView) findViewById(R.id.textProbability3);
        result3 = (RelativeLayout) findViewById(R.id.result3);
        result2 = (RelativeLayout) findViewById(R.id.result2);
        progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
        progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);
        progressBar3 = (ProgressBar) findViewById(R.id.progressBar3);
        buttonBackIdentify = (Button) findViewById(R.id.buttonBackIdentify);
    }
}
