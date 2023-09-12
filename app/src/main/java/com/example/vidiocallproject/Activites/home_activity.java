package com.example.vidiocallproject.Activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.vidiocallproject.Activites.Model.UserModel;
import com.example.vidiocallproject.Activites.connecting;
import com.example.vidiocallproject.R;
import com.example.vidiocallproject.databinding.ActivityHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class home_activity extends AppCompatActivity {

    ActivityHomeBinding binding;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    private int requestcode = 1;
    long coins = 0;
    UserModel userModel;
    String permission[] = new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        FirebaseUser user = firebaseAuth.getCurrentUser();
        firebaseDatabase.getReference().child("profiles")
                .child(user.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userModel = snapshot.getValue(UserModel.class);
                        coins = userModel.getCoins();

                        binding.coins.setText("you have " + coins);
                        Glide.with(home_activity.this).load(userModel.getProfile()).into(binding.profilePicture);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("database error", error.getMessage());

                    }
                });
        binding.findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ispermissiongranted()) {
                    if (coins > 5) {
                        coins = coins - 5;
                        firebaseDatabase.getReference().child("profiles")
                                .child(user.getUid())
                                .child("coins")
                                .setValue(coins);
                        Intent intent = new Intent(getApplicationContext(), connecting.class);
                        intent.putExtra("profile", userModel.getProfile());
                        startActivity(intent);


                    } else {
                        Toast.makeText(getApplicationContext(), "insuficient coins", Toast.LENGTH_SHORT).show();

                    }

                } else {
                    askpermission();
                }

            }
        });
        binding.rewardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), rewards.class);
                startActivity(intent);
            }
        });

    }

    void askpermission() {
        ActivityCompat.requestPermissions(this, permission, requestcode);
    }

    private boolean ispermissiongranted() {
        for (String permission : permission) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED)
                return false;
        }
        return true;
    }
}