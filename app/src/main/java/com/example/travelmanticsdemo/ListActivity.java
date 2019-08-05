package com.example.travelmanticsdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class ListActivity extends AppCompatActivity {
    private RecyclerView mDealRecycler;
    private DealAdapter mDealAdapter;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(ListActivity.this, DealActivity.class));
//            }
//        });

//        mFirebaseDatabase = FirebaseDatabase.getInstance();
//        mDatabaseReference = mFirebaseDatabase.getReference().child("traveldeals");
//        mChildListener = new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                TextView txtViewDeal = findViewById(R.id.textView);
//                TravelDeal travelDeal = dataSnapshot.getValue(TravelDeal.class);
//                txtViewDeal.setText(txtViewDeal.getText() + "\n" + travelDeal.getTitle());
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        };
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListActivity.this, DealActivity.class));
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu, menu);
        getMenuInflater().inflate(R.menu.menu, menu);
        //MenuItem insertMenu = menu.findItem(R.id.insert_menu);
        if (FirebaseUtils.isAdmin == true) {
            //insertMenu.setVisible(true);
            fab.setVisibility(View.VISIBLE);
        }
        else if (FirebaseUtils.isAdmin == false){
            fab.setVisibility(View.INVISIBLE);
            //insertMenu.setVisible(false);
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
//            case R.id.insert_menu:
//                startActivity(new Intent(this, DealActivity.class));
//                return  true;
            case R.id.logout_menu:
                AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("LOGOUT", "user Logged Out");
                        FirebaseUtils.attachListenr();
                    }
                });
                FirebaseUtils.detachListener();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseUtils.openFbReference("traveldeals", this);
        mDealRecycler = findViewById(R.id.list_item);
        mDealAdapter = new DealAdapter();
        mDealRecycler.setAdapter(mDealAdapter);
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        mDealRecycler.setLayoutManager(linearLayoutManager);
        FirebaseUtils.attachListenr();

    }

    @Override
    protected void onPause() {
        super.onPause();
        FirebaseUtils.detachListener();
    }

    public void showMenu(){
        invalidateOptionsMenu();
    }
}
