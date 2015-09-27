package com.example.raf0c.myoffice.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.raf0c.myoffice.R;
import com.example.raf0c.myoffice.model.Visits;

import java.util.List;

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
            LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
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

        Visits visit = mRecords.get(position);
        holder.tv_office.setText(visit.getOffice());
        holder.tv_duration.setText(String.valueOf(visit.getDuration()));
        holder.tv_entry.setText(String.valueOf(visit.getOffice()));
        holder.tv_exit.setText(String.valueOf(visit.getOffice()));

        return row;
    }


}
