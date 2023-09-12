package com.example.vidiocallproject.Activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.vidiocallproject.R;
import com.example.vidiocallproject.databinding.ActivityConnectingBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

public class connecting extends AppCompatActivity {
    ActivityConnectingBinding binding;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    boolean isokay = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConnectingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        String profile = getIntent().getStringExtra("profile");

        Glide.with(this).load(profile).into(binding.profile);
        String username = firebaseAuth.getUid();

        firebaseDatabase.getReference().child("users")
                .orderByChild("status")
                .equalTo(0).limitToFirst(1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getChildrenCount() > 0) {
                            //room available
                            isokay = true;
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                firebaseDatabase.getReference().child("users")
                                        .child(dataSnapshot.getKey())
                                        .child("incoming")
                                        .setValue(username);
                                firebaseDatabase.getReference().child("users")
                                        .child(dataSnapshot.getKey())
                                        .child("status")
                                        .setValue(1);
                                Intent intent = new Intent(getApplicationContext(), call_activity.class);
                                String incoming = dataSnapshot.child("incoming").getValue(String.class);
                                String createdby = dataSnapshot.child("createdby").getValue(String.class);
                                boolean isavailable = dataSnapshot.child("isavailable").getValue(Boolean.class);
                                intent.putExtra("username", username);
                                intent.putExtra("incoming", incoming);
                                intent.putExtra("createdby", createdby);
                                intent.putExtra("isavailable", isavailable);
                                startActivity(intent);
                                finish();
                            }


                        } else {
                            //room not available
                            HashMap<String, Object> room = new HashMap<>();
                            room.put("incoming", username);
                            room.put("createdby", username);
                            room.put("isavailable", true);
                            room.put("status", 0);

                            firebaseDatabase.getReference()
                                    .child("users")
                                    .child(username)
                                    .setValue(room)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            firebaseDatabase.getReference().child("users").child(username)
                                                    .addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            if (snapshot.child("status").exists()) {
                                                                if (snapshot.child("status").getValue(Integer.class) == 1) {
                                                                    if (isokay)
                                                                        return;
                                                                    isokay = true;

                                                                    Intent intent = new Intent(getApplicationContext(), call_activity.class);
                                                                    String incoming = snapshot.child("incoming").getValue(String.class);
                                                                    String createdby = snapshot.child("createdby").getValue(String.class);
                                                                    boolean isavailable = snapshot.child("isavailable").getValue(Boolean.class);
                                                                    intent.putExtra("username", username);
                                                                    intent.putExtra("incoming", incoming);
                                                                    intent.putExtra("createdby", createdby);
                                                                    intent.putExtra("isavailable", isavailable);
                                                                    startActivity(intent);
                                                                    finish();
                                                                }
                                                            }

                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });
                                        }
                                    });
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }
}