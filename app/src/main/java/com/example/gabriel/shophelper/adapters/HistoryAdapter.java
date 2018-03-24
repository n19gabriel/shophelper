package com.example.gabriel.shophelper.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.gabriel.shophelper.R;

import java.util.ArrayList;

/**
 * Created by gabriel on 24.03.18.
 */

public class HistoryAdapter extends ArrayAdapter<String> {
    public HistoryAdapter(Context context, ArrayList<String> dates) {
        super(context, 0, dates);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.history_item, parent, false);
        }

        String currentDate = getItem(position);

        TextView nameTextView =listItemView.findViewById(R.id.history_TV);

        nameTextView.setText(currentDate);

        return listItemView;
    }
}
