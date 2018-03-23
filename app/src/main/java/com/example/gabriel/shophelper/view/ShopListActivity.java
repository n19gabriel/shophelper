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
import android.widget.ListView;
import android.widget.Toast;

import com.example.gabriel.shophelper.Adapters.ShopAdapter;
import com.example.gabriel.shophelper.R;
import com.example.gabriel.shophelper.model.Shop;
import com.example.gabriel.shophelper.model.User;
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

public class ShopListActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private FloatingActionButton Badd;
    private Toolbar toolbar;
    private MaterialSearchView searchView;
    private ListView listView_1;
    private ListView listView_2;
    private ArrayList<Shop> shops;
    private ArrayList<Shop> shops_1;
    private ArrayList<Shop> shops_2;
    private ShopAdapter shopAdapter_1;
    private ShopAdapter shopAdapter_2;
    private String userId;
    private String roles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_list);

        Badd = findViewById(R.id.new_Shop);

        listView_1 = findViewById(R.id.list_1);
        listView_2 = findViewById(R.id.list_2);
        shops_1 = new ArrayList<>();
        shops_2 = new ArrayList<>();
        shops = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userId = user.getUid();
        String userId = user.getUid();
        myRef = FirebaseDatabase.getInstance().getReference();


        toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Shop");
        searchView = findViewById(R.id.search_view);

        Badd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShopListActivity.this, NewShopActivity.class);
                startActivity(intent);
            }
        });

        listView_1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Shop shop = shops.get(position);
                Intent intent  = new Intent(ShopListActivity.this, ScannerActivity.class);
                intent.putExtra("id_Shop", shop.getId());
                intent.putExtra("roles", roles);
                startActivity(intent);
            }
        });

        listView_2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Shop shop = shops.get(position);
                Intent intent  = new Intent(ShopListActivity.this, ScannerActivity.class);
                intent.putExtra("id_Shop", shop.getId());
                intent.putExtra("roles", roles);
                startActivity(intent);
            }
        });

        listView_1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (roles.equals("admin")) {
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(ShopListActivity.this);
                    View mView = getLayoutInflater().inflate(R.layout.dialog_menu_shop, null);
                    Button delete = mView.findViewById(R.id.delete);
                    final Shop shop = shops_1.get(position);
                    delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            myRef.child("Shops").child(shop.getId()).removeValue();
                        }
                    });
                    mBuilder.setView(mView);
                    final AlertDialog dialog = mBuilder.create();
                    dialog.show();
                    return false;
                }
                return false;
            }
        });
        listView_2.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (roles.equals("admin")) {
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(ShopListActivity.this);
                    View mView = getLayoutInflater().inflate(R.layout.dialog_menu_shop, null);
                    Button delete = mView.findViewById(R.id.delete);
                    final Shop shop = shops_2.get(position);
                    delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            myRef.child("Shops").child(shop.getId()).removeValue();
                        }
                    });
                    mBuilder.setView(mView);
                    final AlertDialog dialog = mBuilder.create();
                    dialog.show();
                    return false;
                }
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {
                //If closed Search View , lstView will return default
                shopAdapter_1 = new ShopAdapter(ShopListActivity.this, shops_1);
                listView_1.setAdapter(shopAdapter_1);
                shopAdapter_2 = new ShopAdapter(ShopListActivity.this, shops_2);
                listView_2.setAdapter(shopAdapter_2);
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
                    ArrayList<Shop> shops_search_1 = new ArrayList<Shop>();
                    ArrayList<Shop> shops_search_2 = new ArrayList<Shop>();
                    boolean flag = true;
                    for(Shop shop:shops){
                        if(shop.getName().contains(newText)){
                            if(flag) {
                                shops_search_1.add(shop);
                                flag = false;
                            }else{
                                shops_search_2.add(shop);
                                flag = true;
                            }
                        }
                    }
                    shopAdapter_1 = new ShopAdapter(ShopListActivity.this, shops_search_1);
                    listView_1.setAdapter(shopAdapter_1);
                    shopAdapter_2 = new ShopAdapter(ShopListActivity.this, shops_search_2);
                    listView_2.setAdapter(shopAdapter_2);
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
            Toast.makeText(ShopListActivity.this, "Signing Out...", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ShopListActivity.this, AuthenticationActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

        myRef.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot userSnapshot: dataSnapshot.getChildren()){
                    User user = userSnapshot.getValue(User.class);
                    if (user.getId().equals(userId)) {
                        roles = user.getRoles();
                        if (roles.equals("user")) {
                            Badd.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        myRef.child("Shops").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                shops_1.clear();
                shops_2.clear();
                shops.clear();
                Boolean flag =true;
                for(DataSnapshot shopSnapshot: dataSnapshot.getChildren()){
                    Shop shop = shopSnapshot.getValue(Shop.class);
                    shops.add(shop);
                    if(flag) {
                        shops_1.add(shop);
                        flag = false;
                    }else{
                        shops_2.add(shop);
                        flag = true;
                    }
                }
                shopAdapter_1 = new ShopAdapter(ShopListActivity.this, shops_1);
                listView_1.setAdapter(shopAdapter_1);
                shopAdapter_2 = new ShopAdapter(ShopListActivity.this, shops_2);
                listView_2.setAdapter(shopAdapter_2);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
