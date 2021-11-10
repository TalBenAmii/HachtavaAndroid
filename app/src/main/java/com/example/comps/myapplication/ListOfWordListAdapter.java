package com.example.comps.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * This is a custom adapter that changes the strings in a listview to blue.
 */

public class ListOfWordListAdapter extends ArrayAdapter<WordList> {

    private Context mContext;
    private int id;
    private List<WordList> items;

    /**
     * Sets the custom adapter.
     * Works when the adapter is defined.
     *
     * @param context
     * @param textViewResourceId
     * @param list
     */
    public ListOfWordListAdapter(Context context, int textViewResourceId, List<WordList> list) {
        super(context, textViewResourceId, list);
        mContext = context;
        id = textViewResourceId;
        items = list;
    }

    /**
     * Sets the blue color.
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
            text.setTextColor(Color.BLUE);
            text.setText(items.get(position).getListName());

        }
        return mView;
    }

}