package com.tecstudyap.rasgullavideocallapp.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.tecstudyap.rasgullavideocallapp.R;
import com.tecstudyap.rasgullavideocallapp.databinding.ActivityMainBinding;
import com.tecstudyap.rasgullavideocallapp.models.User;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    long coins = 200;
    String[] permissions = new String[] {Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};
    private int requestCode = 1;
    User user;
  KProgressHUD progress;

  WebView mWebView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loadAds();

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });


        setupHyperlink();

        alertReminderDialog();

//        mWebView.setWebViewClient(new WebViewClient());
//        mWebView.setWebChromeClient(new WebChromeClient());
//        mWebView.loadUrl("file:///android_asset/dialogReminder.html");

//        SharedPreferences sp = getSharedPreferences("Reminder",MODE_PRIVATE);
//        boolean bln =sp.getBoolean("ONCE",true);
//
//        if (bln){
//
//            alertDialog();
//            SharedPreferences sps = getSharedPreferences("Reminder",MODE_PRIVATE);
//            SharedPreferences.Editor editor = sps.edit();
//            editor.putBoolean("ONCE",false);
//            editor.apply();
//        }


        progress = KProgressHUD.create(this);
        progress.setDimAmount(0.5f);
        progress.show();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        FirebaseUser currentUser = auth.getCurrentUser();

        database.getReference().child("profiles")
                .child(currentUser.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                      try {
                          progress.dismiss();
                          user = snapshot.getValue(User.class);
                          coins = user.getCoins();

                          binding.coins.setText("You have: " + coins);

                          Glide.with(MainActivity.this)
                                  .load(user.getProfile())
                                  .into(binding.profilePicture);
                      }catch (Exception e ){}
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });


        binding.findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.checkboxicon.isChecked()){
                if(isPermissionsGranted() ) {
                    if (coins >= 5) {
                        coins = coins - 5;
                        database.getReference().child("profiles")
                                .child(currentUser.getUid())
                                .child("coins")
                                .setValue(coins);
                        Intent intent = new Intent(MainActivity.this, ConnectingActivity.class);
                        intent.putExtra("profile", user.getProfile());
                        startActivity(intent);
                        //startActivity(new Intent(MainActivity.this, ConnectingActivity.class));
                    } else {
                        Toast.makeText(MainActivity.this, "No Coins", Toast.LENGTH_SHORT).show();
                    }
                }

                }
                else {
                    askPermissions();
                    alertCheckDialog();
                }
            }
        });

        binding.rewardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              startActivity(new Intent(MainActivity.this, RewardActivity.class));
            }
        });




    }

    void askPermissions(){
        ActivityCompat.requestPermissions(this, permissions, requestCode);
    }

    private boolean isPermissionsGranted() {
        for(String permission : permissions ){
            if(ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED)
                return false;
        }

        return true;
    }

    //google banner ads load
    private void loadAds() {

        AdView mAdView = findViewById(R.id.adView2);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void setupHyperlink() {
        TextView linkTextView = findViewById(R.id.rules);

        linkTextView.setMovementMethod(LinkMovementMethod.getInstance());


        linkTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this,TermsAndConditionsActivity.class);
                startActivity(intent);


            }
        });



    }





    public void alertReminderDialog(){
      //  Dialog dialog = new Dialog(MainActivity.this,R.style.Dialoge);
        final Dialog dialog = new Dialog(MainActivity.this,R.style.Dialoge);
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_layout,null);
        dialog.setContentView(view);


        dialog.setCancelable(false);



        TextView yesbtn,nobtn;



        yesbtn = dialog.findViewById(R.id.yesbtn);
        nobtn = dialog.findViewById(R.id.nobtn);
       final TextView reminderAlert = (TextView) dialog.findViewById(R.id.termsofuse);
        reminderAlert.setMovementMethod(LinkMovementMethod.getInstance());





        yesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();

            }
        });

        nobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
               System.exit(0);

            }
        });
        reminderAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(MainActivity.this,TermsAndConditionsActivity.class);
                startActivity(intent);

            }
        });

        dialog.show();

    }


//    WebView webView = new WebView(this);
//        webView.loadUrl("file:///android_asset/dialogReminder.html");
//        webView.setWebViewClient(new WebViewClient(){
//
//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            view.loadUrl(url);
//            return true;
//        }
//    });



    public void alertCheckDialog(){
        Dialog dialog = new Dialog(MainActivity.this,R.style.Dialoge);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_check_layout);

        TextView yesbtn,nobtn;

        yesbtn = dialog.findViewById(R.id.yesbtn);
        nobtn = dialog.findViewById(R.id.nobtn);





        yesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();

//                FirebaseAuth.getInstance().signOut();
//                startActivity(new Intent(MainActivity.this,RegistrationActivityst.class));
            }
        });

        nobtn.setOnClickListener(null);


        dialog.show();

    }





}
