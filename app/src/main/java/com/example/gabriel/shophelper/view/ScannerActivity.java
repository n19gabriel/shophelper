package com.example.gabriel.shophelper.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.gabriel.shophelper.R;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    private ZXingScannerView zXingScannerView;
    private String id_Shop;
    private String roles;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        Intent intent = getIntent();
        id_Shop = intent.getStringExtra("id_Shop");
        roles = intent.getStringExtra("roles");
    }
    public void scan(View view){
        zXingScannerView =new ZXingScannerView(getApplicationContext());
        setContentView(zXingScannerView);
        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //zXingScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {
        Toast.makeText(getApplicationContext(),result.getText(),Toast.LENGTH_SHORT).show();
        zXingScannerView.resumeCameraPreview(this);
        if(roles.equals("admin")) {
            Intent intent = new Intent(ScannerActivity.this, NewItemActivity.class);
            intent.putExtra("id_Shop", id_Shop);
            intent.putExtra("code", result.getText());
            startActivity(intent);
        }else{
            Intent intent = new Intent(ScannerActivity.this,BasketActivity.class);
            intent.putExtra("id_Shop", id_Shop);
            intent.putExtra("code", result.getText());
            startActivity(intent);
        }
    }
}
