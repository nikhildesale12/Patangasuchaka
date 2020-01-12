package com.gkvk.patangasuchaka.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.animation.ArgbEvaluator;
import android.os.Bundle;
import android.view.Window;

import com.gkvk.patangasuchaka.adapter.ViewPagerAdapter;
import com.gkvk.patangasuchaka.bean.DashboardViewpagerBean;
import com.gkvk.R;
import com.gkvk.patangasuchaka.util.ApplicationConstant;

import java.util.ArrayList;
import java.util.List;


public class DashboardActivity extends AppCompatActivity {

    ViewPager viewPager;
    ViewPagerAdapter adapter;
    List<DashboardViewpagerBean> models;
    Integer[] colors = null;
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_dashboard);

        if (android.os.Build.VERSION.SDK_INT >= ApplicationConstant.API_LEVEL_23) {
            if (ApplicationConstant.checkPermission(DashboardActivity.this)) {
                if (!ApplicationConstant.isGPSEnabled(DashboardActivity.this)) {
                    ApplicationConstant.showSettingsAlert(DashboardActivity.this);
                }
            } else {
                ApplicationConstant.requestPermission(DashboardActivity.this);
            }
        } else {
            if (!ApplicationConstant.isGPSEnabled(DashboardActivity.this)) {
                ApplicationConstant.showSettingsAlert(DashboardActivity.this);
            }
        }

        models = new ArrayList<>();
        models.add(new DashboardViewpagerBean(R.drawable.one2, "Capture and upload", ApplicationConstant.MODULE_CAPTURE));
        models.add(new DashboardViewpagerBean(R.drawable.two2, "Upload from gallery", ApplicationConstant.MODULE_GALLERY));
        models.add(new DashboardViewpagerBean(R.drawable.three2, "Search species", ApplicationConstant.MODULE_SPECIES));
        models.add(new DashboardViewpagerBean(R.drawable.four2, "Distribution", ApplicationConstant.MODULE_DISTRIBUTION));
        //models.add(new Model(R.drawable.five,"Five"));

        adapter = new ViewPagerAdapter(models, DashboardActivity.this);
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        viewPager.setPadding(100, 20, 100, 20);

        Integer[] colors_temp = {
                getResources().getColor(R.color.color1),
                getResources().getColor(R.color.color2),
                getResources().getColor(R.color.color3),
                getResources().getColor(R.color.color4)
        };

        colors = colors_temp;

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if (position < (adapter.getCount() - 1) && position < (colors.length - 1)) {
                    viewPager.setBackgroundColor((Integer) argbEvaluator.evaluate(positionOffset,
                            colors[position], colors[position + 1]));
                } else {
                    viewPager.setBackgroundColor(colors[colors.length - 1]);
                }
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
}
