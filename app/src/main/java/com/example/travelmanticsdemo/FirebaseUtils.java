package com.example.travelmanticsdemo;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FirebaseUtils {
    private static final int RC_SIGN_IN = 123;
    public static FirebaseDatabase mFirebaseDatabase;
    public static DatabaseReference mDatabaseReference;
    private static FirebaseUtils firebaseUtils;
    public static ArrayList<TravelDeal> mDeals;
    private static FirebaseAuth mAuth;
    private static  FirebaseAuth.AuthStateListener mAuthListener;
    public static FirebaseStorage mStorage;
    public static StorageReference mStorageRef;
    private static ListActivity caller;
    public static boolean isAdmin;

    private FirebaseUtils(){}

    public static void openFbReference(String ref, final ListActivity callerActivity){
        if (firebaseUtils == null){
            firebaseUtils = new FirebaseUtils();
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            mAuth = FirebaseAuth.getInstance();
            caller = callerActivity;

            mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    if (mAuth.getCurrentUser() == null) FirebaseUtils.signIn();
                    else{
                        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        checkAdmin(userId);
                    }
                }
            };
            connectStorage();
        }
        mDeals = new ArrayList<TravelDeal>();
        mDatabaseReference = mFirebaseDatabase.getReference().child(ref);
    }

    private static void checkAdmin(String userId) {
        FirebaseUtils.isAdmin = false;
        DatabaseReference ref = mFirebaseDatabase.getReference().child("administrators").child(userId);
        ChildEventListener listener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                FirebaseUtils.isAdmin = true;
                Log.d("Admin", "You're an Administrator");
                caller.showMenu();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        ref.addChildEventListener(listener);
    }

    public static void signIn() {
        List<AuthUI.IdpConfig> provider =
                Arrays.asList(new AuthUI.IdpConfig.GoogleBuilder().build(),
                        new AuthUI.IdpConfig.EmailBuilder().build());

        caller.startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(provider).
                        setLogo(R.drawable.f_logo)
                .setTosAndPrivacyPolicyUrls("https://gmail.com/terms.html",
                        "https://gmail.com/privacy.html")
                .build(), RC_SIGN_IN);
    }

    public static void attachListenr(){
        mAuth.addAuthStateListener(mAuthListener);

    }

    public static void detachListener(){
        mAuth.removeAuthStateListener(mAuthListener);

    }
    public  static void connectStorage(){
        mStorage = FirebaseStorage.getInstance();
        mStorageRef = mStorage.getReference().child("deals_pictures");
    }

}
