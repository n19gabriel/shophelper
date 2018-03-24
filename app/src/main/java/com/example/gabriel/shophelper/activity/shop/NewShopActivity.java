package com.example.gabriel.shophelper.activity.shop;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gabriel.shophelper.R;
import com.example.gabriel.shophelper.model.Shop;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewShopActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private EditText ETname;
    private Button Badd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_shop);

        mAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference();

        ETname = findViewById(R.id.et_shop_name);
        Badd = findViewById(R.id.add_shop);

        Badd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addShop();
            }
        });
    }

    private void addShop() {
        //getting the values to save
        String name = ETname.getText().toString();

        //checking if the value is provided
        if (!TextUtils.isEmpty(name)) {

            //getting a unique id using push().getKey() method
            String id = myRef.push().getKey();

            //creating an Shop Object
            Shop shop = new Shop(id, name);

            //Saving the Floor
            myRef.child("Shops").child(id).setValue(shop);

            //setting editText to blank again
            ETname.setText("");

            //displaying a success toast
            Toast.makeText(this, "Shop added", Toast.LENGTH_LONG).show();
        } else {
            //if the value is not given displaying a toast
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show();
        }
    }
}
