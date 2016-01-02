package com.govibs.viva.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.govibs.viva.R;

import java.util.List;

/**
 * Default display adapter with one text view.
 * Created by goswamiv on 12/28/15.
 */
public class UIDisplayAdapter extends ArrayAdapter<String> {

    private Context mContext;
    private int mLayout;
    private List<String> mListDisplay;

    public UIDisplayAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        mContext = context;
        mLayout = resource;
        mListDisplay = objects;
    }

    static class ViewHolder {
        TextView textView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(mLayout, parent, false);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.tvListViewItem);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textView.setText(mListDisplay.get(position));

        return convertView;
    }
}
