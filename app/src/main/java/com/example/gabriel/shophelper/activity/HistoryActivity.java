package com.example.gabriel.shophelper.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.gabriel.shophelper.R;
import com.example.gabriel.shophelper.adapters.HistoryAdapter;
import com.example.gabriel.shophelper.model.Record;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
    private ArrayList<String> dates;
    private FirebaseAuth mAuth;
    private ListView listView;
    private DatabaseReference myRef;
    private String userId;
    private Toolbar toolbar;
    private MaterialSearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userId = user.getUid();
        myRef = FirebaseDatabase.getInstance().getReference();

        toolbar =findViewById(R.id.toolbar_history);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("History");
        searchView = findViewById(R.id.search_view);

        dates = new ArrayList<String>();
        listView = findViewById(R.id.list);

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {
                //If closed Search View , lstView will return default
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(HistoryActivity.this, R.layout.support_simple_spinner_dropdown_item, dates);
                listView.setAdapter(adapter);
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
                    ArrayList<String> date_search = new ArrayList<>();
                    for(String date:dates){
                        if(date.contains(newText)){
                            date_search.add(date);
                        }
                    }
                    HistoryAdapter adapter = new HistoryAdapter(HistoryActivity.this, dates);
                    listView.setAdapter(adapter);
                }
                return false;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        myRef.child("Users").child(userId).child("History").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dates.clear();
                for(DataSnapshot historySnapshot: dataSnapshot.getChildren()){
                    Record time = historySnapshot.getValue(Record.class);
                    dates.add(time.getTime()+": "+time.getRecord());
                }
                HistoryAdapter adapter = new HistoryAdapter(HistoryActivity.this, dates);

                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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
}
