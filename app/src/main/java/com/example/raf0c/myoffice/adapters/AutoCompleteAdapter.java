package com.example.raf0c.myoffice.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.raf0c.myoffice.R;
import com.example.raf0c.myoffice.model.AutoCompleteText;
import com.example.raf0c.myoffice.model.RowItem;

import java.util.List;

/**
 * Created by raf0c on 26/09/15.
 */
public class AutoCompleteAdapter extends ArrayAdapter<AutoCompleteText> {

    Context context;
    List<AutoCompleteText> rowItem;
    Context mContext;
    int layoutResourceId;

    public AutoCompleteAdapter(Context mContext, int layoutResourceId, List<AutoCompleteText> rowItem) {

        super(mContext, layoutResourceId, rowItem);

        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.rowItem = rowItem;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(layoutResourceId, null);
        }
        TextView txtTitle = (TextView) convertView.findViewById(R.id.autoText);
        AutoCompleteText row_pos = rowItem.get(position);

        // setting the image resource and title
        txtTitle.setText(row_pos.getDescription());
        return convertView;
    }

}
