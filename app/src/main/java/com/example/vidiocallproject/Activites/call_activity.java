package com.example.vidiocallproject.Activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.vidiocallproject.Activites.Model.UserInterface;
import com.example.vidiocallproject.Activites.Model.UserModel;
import com.example.vidiocallproject.R;
import com.example.vidiocallproject.databinding.ActivityCallBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

public class call_activity extends AppCompatActivity {
    ActivityCallBinding binding;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    String username="";
    String createdby ;
    String Friendusername= "";
    String uniqueid ="";
    boolean ispeerconnected = false;
    boolean pageexit = false;
    boolean isvidio = true;
    boolean isAduio = true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =  ActivityCallBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth= FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");

        username = getIntent().getStringExtra("username");
        String incoming = getIntent().getStringExtra("incoming");
        createdby = getIntent().getStringExtra("createdby");
        Friendusername = incoming;
        setupWebview();


        binding.micBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAduio=!isAduio;
                calljavascriptfunction("javascript:toggleAudio(\""+isAduio+"\")");
                if (isAduio){
                    binding.micBtn.setImageResource(R.drawable.btn_unmute_normal);
                }else {
                    binding.micBtn.setImageResource(R.drawable.btn_mute_normal);


                }

            }
        });

        binding.endCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        binding.videoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isvidio=!isvidio;
                calljavascriptfunction("javascript:toggleVideo(\""+isvidio+"\")");
                if (isvidio){
                    binding.videoBtn.setImageResource(R.drawable.btn_video_normal);
                }else {
                    binding.videoBtn.setImageResource(R.drawable.btn_video_muted);


                }

            }
        });

    }
    void setupWebview(){
        binding.webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onPermissionRequest(PermissionRequest request) {
                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
                    request.grant(request.getResources());
                }
            }
        });
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        binding.webView.addJavascriptInterface(new UserInterface(this),"Android");

        loadvidiocall();

    }
    public void loadvidiocall(){
        String filepath="file:android_asset/call.html";
        binding.webView.loadUrl(filepath);
        binding.webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                initializepeer();
            }
        });
    }
    void calljavascriptfunction(String function){
        binding.webView.post(new Runnable() {
            @Override
            public void run() {
                binding.webView.evaluateJavascript(function,null);
            }
        });
    }
    void initializepeer(){
        uniqueid = getUniqueid();
        calljavascriptfunction("javascript:init(\""+uniqueid+"\")");
        if (createdby.equalsIgnoreCase(username)){
            if (pageexit)
                return;
            databaseReference.child(username).child("connId").setValue(uniqueid);
            databaseReference.child(username).child("isavailable").setValue(true);

            binding.loadingGroup.setVisibility(View.GONE);
            binding.controls.setVisibility(View.VISIBLE);

            FirebaseDatabase.getInstance().getReference()
                    .child("profiles")
                    .child(Friendusername)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            UserModel userModel = snapshot.getValue(UserModel.class);
                            Glide.with(call_activity.this).load(userModel.getProfile()).into(binding.profile);
                            binding.name .setText(userModel.getName());
                            binding.city.setText(userModel.getCity());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Friendusername=createdby;
                    FirebaseDatabase.getInstance().getReference().child("profiles").child(Friendusername).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            UserModel userModel = snapshot.getValue(UserModel.class);
                            Glide.with(call_activity.this).load(userModel.getProfile()).into(binding.profile);
                            binding.name.setText(userModel.getName());
                            binding.city.setText(userModel.getCity());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    FirebaseDatabase.getInstance().getReference().child("users").child(Friendusername).child("connId").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.getValue()!=null){
                                sendCallRequest();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            },3000);
        }


    }
    public void onPeerconnected(){
        ispeerconnected=true;
    }
    void sendCallRequest(){
        if (ispeerconnected == true){
            listenconnId();

        }
        else {
            Toast.makeText(getApplicationContext(),"you are not connected",Toast.LENGTH_SHORT).show();
            listenconnId();
        }



    }
    void listenconnId(){
        databaseReference.child(Friendusername).child("connId").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() == null) {
                    return;
                }
                binding.loadingGroup.setVisibility(View.GONE);
                binding.controls.setVisibility(View.VISIBLE);
                String connId = snapshot.getValue(String.class);
                calljavascriptfunction("javascript:startCall(\""+connId+"\")");
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    String getUniqueid(){
        return UUID.randomUUID().toString();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pageexit = true;
        databaseReference.child(createdby).setValue(null);
        finish();
    }
}