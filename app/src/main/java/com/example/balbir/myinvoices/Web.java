package com.example.balbir.myinvoices;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Web extends AppCompatActivity {
    private WebView web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);

        web = (WebView) findViewById(R.id.webview);
        web.setWebViewClient(new WebViewClient());
        web.loadUrl("https://en.wikipedia.org/wiki/Invoice");

    }
}
