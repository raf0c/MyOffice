package com.example.raf0c.myoffice.adapters;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.raf0c.myoffice.R;
import com.example.raf0c.myoffice.model.Visits;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by raf0c on 27/09/15.
 */
public class VisitsAdapter extends ArrayAdapter<Visits> {

    private Context mContext;
    private int mResourceId;
    private List<Visits> mRecords;


    public VisitsAdapter(Context context, int resource, List<Visits> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResourceId = resource;
        this.mRecords = objects;
    }

    static class VisitsHolder{
        TextView tv_office;
        TextView tv_duration;
        TextView tv_entry;
        TextView tv_exit;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        VisitsHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(mResourceId, parent, false);

            holder = new VisitsHolder();
            holder.tv_office = (TextView) row.findViewById(R.id.tv_office);
            holder.tv_duration = (TextView) row.findViewById(R.id.tv_duration);
            holder.tv_entry = (TextView) row.findViewById(R.id.tv_entry);
            holder.tv_exit = (TextView) row.findViewById(R.id.tv_exit);

            row.setTag(holder);
        }
        else
        {
            holder = (VisitsHolder)row.getTag();
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd HH:mm:ss", Locale.US);

        Visits visit = mRecords.get(position);
        holder.tv_office.setText(visit.getOffice());
        holder.tv_duration.setText(sdf.format(visit.getDuration()));
        holder.tv_entry.setText(sdf.format(visit.getEntry()));
        holder.tv_exit.setText(sdf.format(visit.getExit()));

        return row;
    }


}
