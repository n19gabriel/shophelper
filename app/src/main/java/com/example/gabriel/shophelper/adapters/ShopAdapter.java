package com.example.gabriel.shophelper.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.gabriel.shophelper.R;
import com.example.gabriel.shophelper.model.Shop;

import java.util.ArrayList;

/**
 * Created by gabriel on 22.03.18.
 */

public class ShopAdapter extends ArrayAdapter<Shop> {
    public ShopAdapter(Context context, ArrayList<Shop> offices) {
        super(context, 0, offices);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.shop_item, parent, false);
        }

        Shop currentShop = getItem(position);
        TextView nameTextView = listItemView.findViewById(R.id.tv_shop_name);
        nameTextView.setText(currentShop.getName());
        return listItemView;
    }
}
