package com.softpo.hellomap;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class DetailActivity extends AppCompatActivity {
    private WebView webView_detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        webView_detail = (WebView) findViewById(R.id.webView);
        webView_detail.setWebViewClient(new WebViewClient());
        // 让webView支持javascript语句。但是类似alert()这样的特殊语句依然不支持
        webView_detail.getSettings().setJavaScriptEnabled(true);
        // 为了让webview支持类似alert()这样的特殊语句，必须设置以下
        webView_detail.setWebChromeClient(new WebChromeClient());

        webView_detail.loadUrl(getIntent().getExtras().getString("url"));
    }
}
