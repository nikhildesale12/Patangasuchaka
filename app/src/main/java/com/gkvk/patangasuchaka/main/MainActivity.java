package com.gkvk.patangasuchaka.main;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

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
import com.gkvk.patangasuchaka.fragment.HomeFragment;
import com.gkvk.patangasuchaka.fragment.HowItWorksFragment;
import com.gkvk.patangasuchaka.fragment.IntroductionFragment;
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

        NavigationDataModel[] drawerItem = new NavigationDataModel[4];
        drawerItem[0] = new NavigationDataModel(R.drawable.connect, "Home");
        drawerItem[1] = new NavigationDataModel(R.drawable.fixtures, "About Us");
        drawerItem[2] = new NavigationDataModel(R.drawable.table, "Introduction");
        drawerItem[3] = new NavigationDataModel(R.drawable.table, "How it works");
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
}
