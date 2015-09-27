package com.example.raf0c.myoffice.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.raf0c.myoffice.MainActivity;
import com.example.raf0c.myoffice.R;
import com.example.raf0c.myoffice.adapters.DrawerAdapter;
import com.example.raf0c.myoffice.model.RowItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by raf0c on 25/09/15.
 */
public class NavigationDrawerFragment extends Fragment {

    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
    public ActionBarDrawerToggle mDrawerToggle;
    public DrawerLayout mDrawerLayout;
    public ListView mDrawerListView;
    private LinearLayout myLayout;
    public View mFragmentContainerView;
    private int mCurrentSelectedPosition = 0;
    private boolean mFromSavedInstanceState;
    String[] menutitles;
    TypedArray menuIcons;
    private List<RowItem> rowItems;
    private DrawerAdapter adapter;
    public Fragment fragment;
    private Context mContext;
    private Context mLittleContext;

    public NavigationDrawerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        menutitles      = getResources().getStringArray(R.array.drawer_search);
        menuIcons       = getResources().obtainTypedArray(R.array.drawer_icons);
        rowItems        = new ArrayList<>();
        mContext        = getActivity().getApplicationContext();
        mLittleContext  = getActivity();

        for (int i = 0; i < menutitles.length; i++) {
            RowItem items = new RowItem(menutitles[i], menuIcons.getResourceId(i, -1));
            rowItems.add(items);
        }
        menuIcons.recycle();
        adapter = new DrawerAdapter(mContext, rowItems);
        if(savedInstanceState == null){
            fragment = new MainFragment();
            FragmentManager fragmentManager = ((FragmentActivity) mLittleContext).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.mainContainer, fragment);
            fragmentTransaction.commit();
        }else{
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }
        // Select either the default item (0) or the last selected item.
        selectItem(mCurrentSelectedPosition);

    }


    public void setUp(int fragmentId, DrawerLayout drawerLayout, Toolbar toolbar) {

        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),
                mDrawerLayout,
                toolbar,
                R.string.openDrawer,
                R.string.closeDrawer){

            @Override
            public void onDrawerOpened(View drawerView) {

                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

        }; // Drawer Toggle Object Made
        mDrawerLayout.setDrawerListener(mDrawerToggle); // Drawer Listener set to the Drawer toggle
        mDrawerToggle.syncState();               // Finally we set the drawer toggle sync State
        mDrawerLayout.closeDrawers();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myLayout = (LinearLayout) inflater.inflate(R.layout.fragment_navigation_drawer, container, false);

        mDrawerListView = (ListView) myLayout.findViewById(R.id.categories);
        mDrawerListView.setAdapter(adapter);

        mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                fragment = null;

                switch (position) {
                    case 0:
                        fragment = new MainFragment();
                        mDrawerLayout.closeDrawers();
                        break;
                    case 1:
                        fragment = VisitsFragment.newInstance(getActivity().getApplicationContext());
                        mDrawerLayout.closeDrawers();
                        break;
                }

                if (fragment != null) {
                    MainActivity.setmCurrentFragment(fragment);
                    FragmentManager fragmentManager = ((FragmentActivity) mLittleContext).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.mainContainer, fragment);
                    fragmentTransaction.commit();
                }
                mDrawerLayout.closeDrawers();
            }
        });

        mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);
        return myLayout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }


    private void selectItem(int position) {
        mCurrentSelectedPosition = position;
        if (mDrawerListView != null) {
            mDrawerListView.setItemChecked(position, true);
        }
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // If the drawer is open, show the global app actions in the action bar. See also
        // showGlobalContextActionBar, which controls the top-left area of the action bar.
        if (mDrawerLayout != null && isDrawerOpen()) {
            inflater.inflate(R.menu.menu_main, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
