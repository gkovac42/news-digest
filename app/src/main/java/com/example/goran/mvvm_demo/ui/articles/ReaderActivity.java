package com.example.goran.mvvm_demo.ui.articles;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;


public class ReaderActivity extends AppCompatActivity {

    public static final String EXTRA_URL = "url";

    public static Intent newIntent(Context context, String url) {
        Intent intent = new Intent(context, ReaderActivity.class);
        intent.putExtra(EXTRA_URL, url);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WebView webView = new WebView(this);
        setContentView(webView);

        String url = getIntent().getStringExtra(EXTRA_URL);

        webView.loadUrl(url);
    }
}
