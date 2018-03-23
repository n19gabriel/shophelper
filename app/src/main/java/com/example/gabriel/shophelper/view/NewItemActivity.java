package com.example.gabriel.shophelper.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gabriel.shophelper.R;
import com.example.gabriel.shophelper.model.Item;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class NewItemActivity extends AppCompatActivity {
    static final int GALLERY_REQUEST = 1;
    private String id_Shop;
    private String code;
    private EditText ETname;
    private EditText ETprice;
    private Button Badd;
    private Button BsetImage;
    private TextView TVcode;
    private ImageView IVsetImage;
    private Bitmap bitmap;
    private Uri selectedImage;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private StorageReference mStorageRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        myRef = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        Intent intent = getIntent();
        id_Shop = intent.getStringExtra("id_Shop");
        code = intent.getStringExtra("code");

        ETname = findViewById(R.id.et_name_item);
        ETprice = findViewById(R.id.et_price);

        TVcode = findViewById(R.id.tv_code);

        Badd = findViewById(R.id.add_item);
        BsetImage = findViewById(R.id.bt_set_image);
        IVsetImage = findViewById(R.id.iv_set_image);

        BsetImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
            }
        });


        Badd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addThings();
            }
        });
    }
    private void uploadImage(String id_image){
        if(selectedImage != null){
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = mStorageRef.child(id_image);
            ref.putFile(selectedImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(NewItemActivity.this, "Upload", Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(NewItemActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progres = (100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progres+"%");
                        }
                    });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        bitmap = null;
        switch(requestCode) {
            case GALLERY_REQUEST:
                if(resultCode == RESULT_OK){
                    selectedImage = imageReturnedIntent.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    IVsetImage.setImageBitmap(bitmap);
                }
        }
    }
    private void addThings() {
        //getting the values to save
        String name = ETname.getText().toString().trim();
        String price =ETprice.getText().toString().trim();

        //checking if the value is provided
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(price)) {

            //getting a unique id using push().getKey() method
            String id = myRef.push().getKey();
            String id_image = myRef.push().getKey();

            uploadImage(id_image);
            //creating an Thing Object
            Item item = new Item(id, code, name, price ,id_Shop);
            if (selectedImage!=null){
                item.setId_Image(id_image);
            }
            //Saving the Office
            myRef.child("Items").child(id).setValue(item);

            //setting edittext to blank again
            ETname.setText("");
            ETprice.setText("");


            //displaying a success toast
            Toast.makeText(this, "Thing added", Toast.LENGTH_LONG).show();
        }else if(TextUtils.isEmpty(name)){
            //if the value is not given displaying a toast
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show();
        }else if(TextUtils.isEmpty(price)) {
            //if the value is not given displaying a toast
            Toast.makeText(this, "Please enter a price", Toast.LENGTH_LONG).show();
        }
    }

}
