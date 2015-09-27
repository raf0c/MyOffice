package com.example.raf0c.myoffice.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.raf0c.myoffice.R;
import com.example.raf0c.myoffice.adapters.DrawerAdapter;
import com.example.raf0c.myoffice.adapters.VisitsAdapter;
import com.example.raf0c.myoffice.model.RowItem;
import com.example.raf0c.myoffice.model.Visits;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by raf0c on 27/09/15.
 */
public class VisitsFragment extends Fragment {

    private static Context mContext;
    private LinearLayout myLayout;
    public ListView mVisitsListView;
    private List<Visits> list;
    private VisitsAdapter mAdapter;


    public VisitsFragment() {}

    public static VisitsFragment newInstance(Context context) {
        VisitsFragment fragment = new VisitsFragment();
        mContext = context;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list = new ArrayList<>();
        mAdapter = new VisitsAdapter(mContext,R.layout.item_visits,list);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myLayout = (LinearLayout) inflater.inflate(R.layout.fragment_visits, container, false);
        mVisitsListView = (ListView) myLayout.findViewById(R.id.visits_list);
        mVisitsListView.setAdapter(mAdapter);


        return myLayout;
    }

}
