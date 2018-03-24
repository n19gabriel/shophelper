package com.example.gabriel.shophelper.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gabriel.shophelper.Adapters.ItemAdapter;
import com.example.gabriel.shophelper.R;
import com.example.gabriel.shophelper.model.Item;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.melnykov.fab.FloatingActionButton;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;

public class BasketActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private FloatingActionButton Badd;
    private Toolbar toolbar;
    private MaterialSearchView searchView;
    private ListView listView;
    private TextView amount;
    private ArrayList<Item> items;
    private ItemAdapter itemAdapter;
    private String id_Shop;
    private String roles;
    private String code;
    private String userId;
    private int amo;
    private int counter;
    private boolean flag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);

        Intent intent = getIntent();
        id_Shop = intent.getStringExtra("id_Shop");
        roles = intent.getStringExtra("roles");
        code = intent.getStringExtra("code");

        Badd = findViewById(R.id.new_item);
        listView = findViewById(R.id.list);
        amount = findViewById(R.id.amount);

        items = new ArrayList<>();
        counter = 1;

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userId = user.getUid();
        myRef = FirebaseDatabase.getInstance().getReference();

        toolbar =findViewById(R.id.toolbar_item);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Items");
        searchView = findViewById(R.id.search_view_item);


        Badd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BasketActivity.this, ScannerActivity.class);
                intent.putExtra("id_Shop", id_Shop);
                intent.putExtra("roles", roles);
                startActivity(intent);
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {
                //If closed Search View , lstView will return default
                itemAdapter = new ItemAdapter(BasketActivity.this, items);
                listView.setAdapter(itemAdapter);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(BasketActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_menu_item, null);
                Button add = mView.findViewById(R.id.add);
                Button delete = mView.findViewById(R.id.delete_item);
                ImageButton plus = mView.findViewById(R.id.plus);
                ImageButton minus = mView.findViewById(R.id.minus);
                flag = true;
                final TextView view_counter = mView.findViewById(R.id.view_counter);
                view_counter.setText(String.valueOf(counter));
                final Item item = items.get(position);

                plus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!view_counter.getText().toString().equals("1000")){
                            view_counter.setText(String.valueOf(++counter));
                        }
                    }
                });

                minus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!view_counter.getText().toString().equals("1")){
                            view_counter.setText(String.valueOf(--counter));
                        }
                    }
                });

                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (flag) {
                            if (counter + Integer.parseInt(item.getQuantity()) <= 1000) {
                                myRef.child("Users").child(userId).child("Basket")
                                        .child(item.getId()).child("quantity")
                                        .setValue(String.valueOf(counter + Integer.parseInt(item.getQuantity())));
                                item.setQuantity(String.valueOf(counter + Integer.parseInt(item.getQuantity())));
                            } else {
                                myRef.child("Users").child(userId).child("Basket")
                                        .child(item.getId()).child("quantity")
                                        .setValue(String.valueOf("1000"));
                                item.setQuantity("1000");

                            }
                        }
                    }
                });
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(counter>=Integer.parseInt(item.getQuantity())) {
                            myRef.child("Users").child(userId).child("Basket")
                                    .child(item.getId()).removeValue();
                            flag = false;
                        }
                        else {
                            myRef.child("Users").child(userId).child("Basket")
                                    .child(item.getId()).child("quantity")
                                    .setValue(String.valueOf(Integer.parseInt(item.getQuantity())-counter));
                            item.setQuantity(String.valueOf(Integer.parseInt(item.getQuantity())-counter));
                            flag = false;
                        }
                    }
                });
                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();
                return false;
            }
        });


        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText != null && !newText.isEmpty()){
                    ArrayList<Item> items_search = new ArrayList<>();
                    for(Item item:items){
                        if(item.getName().contains(newText)){
                                items_search.add(item);
                        }
                    }
                    itemAdapter = new ItemAdapter(BasketActivity.this, items_search);
                    listView.setAdapter(itemAdapter);
                }
                return false;
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list,menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.action_settings) {
            mAuth.signOut();
            Toast.makeText(BasketActivity.this, "Signing Out...", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(BasketActivity.this, AuthenticationActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

        myRef.child("Items").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot itemSnapshot: dataSnapshot.getChildren()){
                    Item item = itemSnapshot.getValue(Item.class);
                    if (item.getBarcode().equals(code) && item.getId_Shop().equals(id_Shop)) {
                        myRef.child("Users").child(userId).child("Basket").child(item.getId()).setValue(item);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        myRef.child("Users").child(userId).child("Basket").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                items.clear();
                amo = 0;
                for(DataSnapshot itemSnapshot: dataSnapshot.getChildren()){
                    Item item = itemSnapshot.getValue(Item.class);
                    amo+= Integer.parseInt(item.getQuantity())*Integer.parseInt(item.getPrice());
                    items.add(item);

                }
                amount.setText("Q-ty: "+amo+"$");
                itemAdapter = new ItemAdapter(BasketActivity.this, items);
                listView.setAdapter(itemAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
