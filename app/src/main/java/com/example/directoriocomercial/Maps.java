package com.example.directoriocomercial;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

public class Maps extends AppCompatActivity {

    private WebView googleMapWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setTitle("Google Maps");
        String iframe = getIntent().getStringExtra("iframe");
        googleMapWebView = (WebView) findViewById(R.id.googlemap_webView);
        googleMapWebView.getSettings().setJavaScriptEnabled(true);
        googleMapWebView.loadData(iframe, "text/html", "utf-8");
    }
}
