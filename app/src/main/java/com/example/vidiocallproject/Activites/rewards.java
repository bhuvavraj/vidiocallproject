package com.example.vidiocallproject.Activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.vidiocallproject.R;
import com.example.vidiocallproject.databinding.ActivityRewardsBinding;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class rewards extends AppCompatActivity {
    ActivityRewardsBinding binding;
    FirebaseDatabase firebaseDatabase;
    RewardedAd rewardedAd;
    String currentid;
    int coins = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRewardsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseDatabase = FirebaseDatabase.getInstance();
        currentid = FirebaseAuth.getInstance().getUid();
        AdLoad();
        firebaseDatabase.getReference().child("profiles")
                .child(currentid)
                .child("coins")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        coins = snapshot.getValue(Integer.class);
                        binding.coins.setText(String.valueOf(coins));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        binding.video1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rewardedAd != null) {
                    Activity activitycontext = rewards.this;
                    rewardedAd.show(activitycontext, new OnUserEarnedRewardListener() {
                        @Override
                        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                            AdLoad();
                            coins = coins + 200;
                            firebaseDatabase.getReference().child("profiles")
                                    .child(currentid)
                                    .child("coins")
                                    .setValue(coins);
                            binding.video1Icon.setImageResource(R.drawable.check);
                        }
                    });
                } else {

                }
            }
        });

        binding.video2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rewardedAd != null) {
                    Activity activitycontext = rewards.this;
                    rewardedAd.show(activitycontext, new OnUserEarnedRewardListener() {
                        @Override
                        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                            AdLoad();
                            coins = coins + 200;
                            firebaseDatabase.getReference().child("profiles")
                                    .child(currentid)
                                    .child("coins")
                                    .setValue(coins);
                            binding.video2Icon.setImageResource(R.drawable.check);
                        }
                    });
                } else {

                }
            }
        });

        binding.video3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rewardedAd != null) {
                    Activity activitycontext = rewards.this;
                    rewardedAd.show(activitycontext, new OnUserEarnedRewardListener() {
                        @Override
                        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                            AdLoad();
                            coins = coins + 200;
                            firebaseDatabase.getReference().child("profiles")
                                    .child(currentid)
                                    .child("coins")
                                    .setValue(coins);
                            binding.video3Icon.setImageResource(R.drawable.check);
                        }
                    });
                } else {

                }
            }
        });

        binding.video4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rewardedAd != null) {
                    Activity activitycontext = rewards.this;
                    rewardedAd.show(activitycontext, new OnUserEarnedRewardListener() {
                        @Override
                        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                            AdLoad();
                            coins = coins + 200;
                            firebaseDatabase.getReference().child("profiles")
                                    .child(currentid)
                                    .child("coins")
                                    .setValue(coins);
                            binding.video4Icon.setImageResource(R.drawable.check);
                        }
                    });
                } else {

                }
            }
        });
        binding.video5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rewardedAd != null) {
                    Activity activitycontext = rewards.this;
                    rewardedAd.show(activitycontext, new OnUserEarnedRewardListener() {
                        @Override
                        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                            AdLoad();
                            coins = coins + 200;
                            firebaseDatabase.getReference().child("profiles")
                                    .child(currentid)
                                    .child("coins")
                                    .setValue(coins);
                            binding.video5Icon.setImageResource(R.drawable.check);
                        }
                    });
                } else {

                }
            }
        });
    }

    void AdLoad() {
        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(this, "ca-app-pub-3940256099942544/5224354917",
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        rewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedad) {
                        rewardedAd = rewardedad;
                    }
                }

        );
    }
}