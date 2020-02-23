package com.gkvk.main;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gkvk.R;
import com.gkvk.util.TouchImageView;

public class LargeImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_large_image);

        String imagepath = getIntent().getExtras().getString("imagepath");

        TouchImageView large_image = (TouchImageView) findViewById(R.id.large_image);
        Button buttonCancel = (Button) findViewById(R.id.cancel_btn);

        Glide.with(LargeImageActivity.this)
                .load(imagepath)
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .into(large_image);

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
