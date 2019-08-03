package com.example.travelmanticsdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    EditText txtTitle;
    EditText txtDescription;
    EditText txtPrice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseUtils.openFbReference("traveldeals");
        mFirebaseDatabase = FirebaseUtils.mFirebaseDatabase;
        mDatabaseReference = FirebaseUtils.mDatabaseReference;

        txtTitle = findViewById(R.id.txtTitle);
        txtDescription = findViewById(R.id.txtDescription);
        txtPrice = findViewById(R.id.txtPrice);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_menu:
                saveDeal();
                Toast.makeText(this, "Deal Saved", Toast.LENGTH_LONG).show();
                clean();
                return  true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveDeal() {
        String title = txtTitle.getText().toString();
        String description = txtDescription.getText().toString();
        String price = txtPrice.getText().toString();

        TravelDeal deal = new TravelDeal(title, description, price, "");
        mDatabaseReference.push().setValue(deal);
    }

    private void clean() {
        txtTitle.setText("");
        txtDescription.setText("");
        txtPrice.setText("");
        txtTitle.requestFocus();

    }
}
