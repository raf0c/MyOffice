package com.example.raf0c.myoffice;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import com.example.raf0c.myoffice.fragments.MainFragment;
import com.example.raf0c.myoffice.fragments.NavigationDrawerFragment;


public class MainActivity extends AppCompatActivity {

    private int mCurrentSelectedPosition = 0;
    private boolean mFromSavedInstanceState;
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
    private Toolbar toolbar;
    static Fragment mCurrentFragment = new MainFragment();
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private MainFragment mainf = new MainFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);


        if(savedInstanceState!=null){
            mCurrentFragment = getSupportFragmentManager().getFragment(savedInstanceState,"mCurrentFragment");
        }
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.myDrawerLayout),toolbar);

    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mNavigationDrawerFragment.mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mNavigationDrawerFragment.mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onStart(){
        super.onStart();
        FragmentTransaction fragTransaction = getSupportFragmentManager().beginTransaction();
        fragTransaction.replace(R.id.mainContainer, mCurrentFragment);
        fragTransaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onStop(){
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
        getSupportFragmentManager().putFragment(outState, "mCurrentFragment", mCurrentFragment);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }
    public static void  setmCurrentFragment(Fragment newFrag){
        mCurrentFragment = newFrag;
    }
}
