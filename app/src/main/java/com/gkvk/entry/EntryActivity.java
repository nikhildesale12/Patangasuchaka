package com.gkvk.entry;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;

import com.gkvk.R;

import java.io.File;

public class EntryActivity extends AppCompatActivity {

    TextView method1 , method2 ,method3,method4,method5,method6,method7,method8;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        method1=findViewById(R.id.method1);
        method2=findViewById(R.id.method2);
        method3=findViewById(R.id.method3);
        method4=findViewById(R.id.method4);
        method5=findViewById(R.id.method5);
        method6=findViewById(R.id.method6);
        method7=findViewById(R.id.method7);
        method8=findViewById(R.id.method8);

        method1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                File rootDirectory = new File(Environment.getExternalStorageDirectory() + File.separator + "METHOD1");
                if (!rootDirectory.exists()) {
                    rootDirectory.mkdir();
                }
            }
        });

        method2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            File rootDirectory = new File(Environment.getExternalStorageDirectory(), "METHOD2");
                if (!rootDirectory.exists()) {
                    rootDirectory.mkdir();
                }
            }
        });

        method3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File rootDirectory = new File(Environment.getExternalStorageState() + File.separator + "METHOD3");
                if (!rootDirectory.exists()) {
                    rootDirectory.mkdir();
                }
            }
        });

        method4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File rootDirectory = new File(Environment.getExternalStorageState() , "METHOD4");
                if (!rootDirectory.exists()) {
                    rootDirectory.mkdir();
                }
            }
        });

        method5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                File file = Environment.getExternalFilesDir("METHOD5");
//                File rootDirectory = new File(file+ File.separator + "METHOD5");
//                if (!rootDirectory.exists()) {
//                    rootDirectory.mkdir();
//                }
            }
        });

        method6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                File rootDirectory = new File(getExternalCacheDir() + File.separator + "METHOD6");
//                if (!rootDirectory.exists()) {
//                    rootDirectory.mkdir();
//                }
            }
        });

        method7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                File rootDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + "METHOD7");
//                if (!rootDirectory.exists()) {
//                    rootDirectory.mkdir();
//                }
            }
        });

        method8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                File rootDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) , "METHOD8");
//                if (!rootDirectory.exists()) {
//                    rootDirectory.mkdir();
//                }
            }
        });
    }
}
