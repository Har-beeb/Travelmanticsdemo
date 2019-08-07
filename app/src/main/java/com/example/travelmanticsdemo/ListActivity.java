package com.example.travelmanticsdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ListActivity extends AppCompatActivity {
    private RecyclerView mDealRecycler;
    private DealAdapter mDealAdapter;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListActivity.this, AdminActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        displayFab();
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout_menu:
                AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("LOGOUT", "user Logged Out");
                        FirebaseUtils.attachListenr();
                        displayFab();
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
        displayFab();
        FirebaseUtils.openFbReference("traveldeals", this);
        mDealRecycler = findViewById(R.id.list_item);
        mDealAdapter = new DealAdapter();
        mDealRecycler.setAdapter(mDealAdapter);
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        mDealRecycler.setLayoutManager(linearLayoutManager);
        FirebaseUtils.attachListenr();
    }

    private void displayFab() {
        if (FirebaseUtils.isAdmin){
            enableFab(true);
            fab.show();
        }
        else {
            enableFab(false);
            fab.hide();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        displayFab();
        FirebaseUtils.detachListener();
    }

    public void showMenu(){
        invalidateOptionsMenu();
    }

    public void enableFab(boolean isEnabled){
        fab.setEnabled(isEnabled);
    }
}
