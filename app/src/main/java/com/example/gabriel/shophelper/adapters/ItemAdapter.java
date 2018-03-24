package com.example.gabriel.shophelper.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.gabriel.shophelper.R;
import com.example.gabriel.shophelper.model.Item;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * Created by gabriel on 23.03.18.
 */

public class ItemAdapter extends ArrayAdapter<Item> {
    Context context;
    private StorageReference mStorageRef;
    public ItemAdapter(Context context, ArrayList<Item> things) {
        super(context, 0, things);
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item, parent, false);
        }

        Item currentItem = getItem(position);

        TextView nameTextView = listItemView.findViewById(R.id.name_item);

        nameTextView.setText(currentItem.getName());

        TextView quantityTextView = listItemView.findViewById(R.id.quantity);

        quantityTextView.setText("Q-ty: "+currentItem.getQuantity());

        ImageView imageView = listItemView.findViewById(R.id.image);

        TextView priceTextView = listItemView.findViewById(R.id.price_item);

        priceTextView.setText(Integer.parseInt(currentItem.getQuantity())*Integer.parseInt(currentItem.getPrice())+"$");
        // Check if an image is provided for this word or not

        if(currentItem.getId_Image()!=null) {
            mStorageRef = FirebaseStorage.getInstance().getReference().child(currentItem.getId_Image());
            Glide.with(context)
                    .using(new FirebaseImageLoader())
                    .load(mStorageRef)
                    .into(imageView);
            // Make sure the view is visible
            imageView.setVisibility(View.VISIBLE);
        }else {
            imageView.setVisibility(View.GONE);
        }

        return listItemView;
    }
}
