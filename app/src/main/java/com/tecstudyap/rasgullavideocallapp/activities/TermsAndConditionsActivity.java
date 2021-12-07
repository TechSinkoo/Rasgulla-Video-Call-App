package com.tecstudyap.rasgullavideocallapp.activities;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.tecstudyap.rasgullavideocallapp.R;

public class TermsAndConditionsActivity extends AppCompatActivity {
    WebView webView;
    ImageView closeBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_conditions);


        closeBtn = findViewById(R.id.closeBtn);

        webView = findViewById(R.id.webViewterms);
        webView.loadUrl("file:///android_asset/termsandcondition.html");

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}