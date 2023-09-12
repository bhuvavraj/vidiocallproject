package com.example.vidiocallproject.Activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.vidiocallproject.Activites.login_activity;
import com.example.vidiocallproject.R;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    Button buttonstarted;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonstarted = findViewById(R.id.btn1);
        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null) {
            Intent intent = new Intent(getApplicationContext(), login_activity.class);
            startActivity(intent);

        }
        buttonstarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), login_activity.class);
                startActivity(intent);
                finish();


            }
        });

    }
}