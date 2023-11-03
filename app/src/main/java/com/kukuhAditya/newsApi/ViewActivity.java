package com.kukuhAditya.newsApi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

public class ViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        WebView view = findViewById(R.id.webView);
        String link = getIntent().getStringExtra("link");

        view.loadUrl(link);
    }
}