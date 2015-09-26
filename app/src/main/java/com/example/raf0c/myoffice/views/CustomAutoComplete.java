package com.example.raf0c.myoffice.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

import java.util.HashMap;

/**
 * Created by raf0c on 26/09/15.
 */
public class CustomAutoComplete extends AutoCompleteTextView {


    public CustomAutoComplete(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected CharSequence convertSelectionToString(Object selectedItem) {
        /** Each item in the autocompetetextview suggestion list is a hashmap object */
        HashMap<String, String> hm = (HashMap<String, String>) selectedItem;
        return hm.get("description");
    }
}
