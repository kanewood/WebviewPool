package com.kanewood.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

public class WebViewActivity extends AppCompatActivity {

    protected FrameLayout mWebviewRoot;
    protected com.tencent.smtt.sdk.WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        mWebviewRoot = (FrameLayout) findViewById(R.id.webview_root_fl);
        mWebView = WebViewPool.getInstance().getWebView();
        mWebviewRoot.addView(mWebView);

        initUrl();
    }
    private void initUrl() {
        mWebView.loadUrl("http://www.jianshu.com/p/5d78a084f97f");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WebViewPool.getInstance().removeWebView(mWebviewRoot,mWebView);
    }
}
