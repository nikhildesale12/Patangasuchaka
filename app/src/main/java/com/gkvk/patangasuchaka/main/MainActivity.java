package com.gkvk.patangasuchaka.main;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gkvk.R;
import com.gkvk.patangasuchaka.adapter.DrawerItemCustomAdapter;
import com.gkvk.patangasuchaka.bean.NavigationDataModel;
import com.gkvk.patangasuchaka.fragment.AboutUsFragment;
import com.gkvk.patangasuchaka.fragment.FeedbackFragment;
import com.gkvk.patangasuchaka.fragment.HistoryFragment;
import com.gkvk.patangasuchaka.fragment.HomeFragment;
import com.gkvk.patangasuchaka.fragment.HowItWorksFragment;
import com.gkvk.patangasuchaka.fragment.IntroductionFragment;
import com.gkvk.patangasuchaka.fragment.ProfileFragment;
import com.gkvk.patangasuchaka.util.ApplicationConstant;

public class MainActivity extends AppCompatActivity {

    private String[] mNavigationDrawerItemTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    Toolbar toolbar;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getIntent() != null && getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }

        if(android.os.Build.VERSION.SDK_INT >= ApplicationConstant.API_LEVEL_23){
            if(ApplicationConstant.checkPermission(MainActivity.this)){
                if(!ApplicationConstant.isGPSEnabled(MainActivity.this)){
                    ApplicationConstant.showSettingsAlert(MainActivity.this);
                }
            }else {
                ApplicationConstant.requestPermission(MainActivity.this);
            }
        }else{
            if(!ApplicationConstant.isGPSEnabled(MainActivity.this)){
                ApplicationConstant.showSettingsAlert(MainActivity.this);
            }
        }

        mTitle = mDrawerTitle = getTitle();
        mNavigationDrawerItemTitles= getResources().getStringArray(R.array.navigation_drawer_items_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        setupToolbar();

        NavigationDataModel[] drawerItem = new NavigationDataModel[8];
        drawerItem[0] = new NavigationDataModel(R.drawable.homeicon, "Home");
        drawerItem[1] = new NavigationDataModel(R.drawable.fixtures, "About Us");
        drawerItem[2] = new NavigationDataModel(R.drawable.introductionicon, "Introduction");
        drawerItem[3] = new NavigationDataModel(R.drawable.howitworksicon, "How it works");
        drawerItem[4] = new NavigationDataModel(R.drawable.feedbackicon, "Feedback");
        drawerItem[5] = new NavigationDataModel(R.drawable.history, "History");
        drawerItem[6] = new NavigationDataModel(R.drawable.profile, "Profile");
        drawerItem[7] = new NavigationDataModel(R.drawable.icon_exit, "Logout");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);

        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this, R.layout.list_view_item_row, drawerItem);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        setupDrawerToggle();

        selectItem(0);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }

    }

    private void selectItem(int position) {

        Fragment fragment = null;

        switch (position) {
            case 0:
                fragment = new HomeFragment();
                break;
            case 1:
                fragment = new AboutUsFragment();
                break;
            case 2:
                fragment = new IntroductionFragment();
                break;
            case 3:
                fragment = new HowItWorksFragment();
                break;

            case 4:
                fragment = new FeedbackFragment(MainActivity.this);
                break;

            case 5:
                fragment = new HistoryFragment();
                break;

            case 6:
                fragment = new ProfileFragment();
                break;

            case 7:
                SharedPreferences.Editor editor = getSharedPreferences(ApplicationConstant.MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.clear();
                editor.commit();

                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
                finishAffinity();

                break;

            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(mNavigationDrawerItemTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);

        } else {
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    void setupToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    void setupDrawerToggle(){
        mDrawerToggle = new ActionBarDrawerToggle(MainActivity.this,mDrawerLayout,toolbar,R.string.app_name,R.string.app_name);
        //This is necessary to change the icon of the Drawer Toggle upon state change.
        mDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        showCloseAppPopUp();
    }

    private void showCloseAppPopUp() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Alert !");
        alertDialog.setMessage("Do you want to close application?");
        alertDialog.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                //finish();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("EXIT", true);
                startActivity(intent);
                finishAffinity();
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

}
