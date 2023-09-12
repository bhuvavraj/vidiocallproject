package com.example.vidiocallproject.Activites;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vidiocallproject.Activites.Model.UserModel;
import com.example.vidiocallproject.Activites.home_activity;
import com.example.vidiocallproject.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

public class login_activity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    GoogleSignInClient googleSignInClient;
    FirebaseDatabase firebaseDatabase;
    TextView textViewgoogle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        textViewgoogle = findViewById(R.id.txtgoogle);

        if (firebaseAuth.getCurrentUser()!=null){
            Intent intent = new Intent(getApplicationContext(), home_activity.class);
            startActivity(intent);
            finish();
        }
        textViewgoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();

            }
        });
        createrequest();


    }
    private void login(){
        Intent loginintent = googleSignInClient.getSignInIntent();
        startActivityForResult(loginintent,123);

    }
    private void createrequest(){
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("520543382520-eisahfvtnv3pgb88lkka6ulmbfdnm9kf.apps.googleusercontent.com")
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode ==123) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount googleSignInAccount = task.getResult(ApiException.class);
                firebaseAUthwithgoogle(googleSignInAccount);

            }catch (ApiException e){
                Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }

    }
    private void firebaseAUthwithgoogle(GoogleSignInAccount account){
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            UserModel userModel = new UserModel(user.getUid(),user.getDisplayName(),user.getPhotoUrl().toString(),"surat",500);
                            firebaseDatabase.getReference()
                                    .child("profiles")
                                    .child(user.getUid())
                                    .setValue(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Intent intent = new Intent(getApplicationContext(), home_activity.class);
                                                startActivity(intent);
                                                finish();
                                            }else {
                                                Toast.makeText(getApplicationContext(),task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                    });
                        }
                        else{
                            Toast.makeText(getApplicationContext(),task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}