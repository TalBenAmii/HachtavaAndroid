package com.example.comps.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a custom adapter that changes the strings in a listview to black.
 */

public class WordListAdapter extends ArrayAdapter<String> {

    private Context mContext;
    private int id;
    private List<String> items;

    /**
     * Sets the custom adapter.
     * Works when the adapter is defined.
     *
     * @param context
     * @param textViewResourceId
     * @param list
     */
    public WordListAdapter(Context context, int textViewResourceId, List<String> list) {
        super(context, textViewResourceId, list);
        mContext = context;
        id = textViewResourceId;
        items = list;
    }

    /**
     * Sets the black color.
     * Works when the adapter is defined.
     *
     * @param position
     * @param v
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View v, ViewGroup parent) {
        View mView = v;
        if (mView == null) {
            LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = vi.inflate(id, null);
        }

        TextView text = (TextView) mView.findViewById(R.id.textView);

        if (items.get(position) != null) {
            text.setTextColor(Color.BLACK);
            text.setText(items.get(position));

        }

        return mView;
    }

}