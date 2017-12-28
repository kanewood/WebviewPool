package com.kanewood.myapplication;


import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Kane on 2017/9/1.
 */


public class WebViewPool {

    private static final String DEMO_URL = "http://mc.vip.qq.com/demo/indexv3";
    private static List<WebView> available = new ArrayList<WebView>();
    private static List<WebView> inUse = new ArrayList<WebView>();

    private static final byte[] lock = new byte[]{};
    private static int maxSize = 3;
    private int currentSize = 0;

    private WebViewPool() {
        available = new ArrayList<WebView>();
        inUse = new ArrayList<WebView>();
    }

    private static volatile WebViewPool instance = null;

    public static WebViewPool getInstance() {
        if (instance == null) {
            synchronized (WebViewPool.class) {
                if (instance == null) {
                    instance = new WebViewPool();
                }
            }
        }
        return instance;
    }

    /**
     * Webview 初始化
     * 最好放在application oncreate里
     * */
    public static void init() {
        for (int i = 0; i < maxSize; i++) {
            WebView webView = new WebView(BaseApplication.getInstance());
            ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            webView.setLayoutParams(params);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);  //设置 缓存模式(true);
            webView.getSettings().setAppCacheEnabled(false);
            webView.getSettings().setSupportZoom(false);
            webView.getSettings().setUseWideViewPort(true);
            webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            webView.getSettings().setDomStorageEnabled(true);
            webView.loadUrl(DEMO_URL);
            available.add(webView);
        }
    }



    /**
     * 获取webview
     *
     * */
    public WebView getWebView() {
        synchronized (lock) {
            WebView webView;
            if (available.size() > 0) {
                webView = available.get(0);
                available.remove(0);
                currentSize++;
                inUse.add(webView);
            } else {
                webView = new WebView(BaseApplication.getInstance());
                inUse.add(webView);
                currentSize++;
            }
            ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            webView.setLayoutParams(params);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);  //设置 缓存模式(true);
            webView.getSettings().setAppCacheEnabled(false);
            webView.getSettings().setSupportZoom(false);
            webView.getSettings().setUseWideViewPort(true);
            webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            webView.getSettings().setDomStorageEnabled(true);
            webView.loadUrl("about:blank");
            return webView;
        }
    }

    /**
     * 回收webview ,不解绑
     *@param webView 需要被回收的webview
     *
     * */
    public void removeWebView(WebView webView) {
            webView.loadUrl("");
            webView.stopLoading();
            webView.setWebChromeClient(null);
            webView.setWebViewClient(null);
            webView.clearCache(true);
            webView.clearHistory();
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);  //设置 缓存模式(true);
            webView.getSettings().setAppCacheEnabled(false);
            webView.getSettings().setSupportZoom(false);
            webView.getSettings().setUseWideViewPort(true);
            webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            webView.getSettings().setDomStorageEnabled(true);

            webView.getSettings().setBuiltInZoomControls(false);
            webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);

            webView.getSettings().setLoadWithOverviewMode(false);

            webView.getSettings().setUserAgentString("android_client");
            webView.getSettings().setDefaultTextEncodingName("UTF-8");
            webView.getSettings().setDefaultFontSize(16);
            synchronized (lock) {
                inUse.remove(webView);
                if (available.size() < maxSize) {
                    available.add(webView);
                } else {
                    webView = null;
                }
                currentSize--;
            }
    }

    /**
     * 回收webview ,解绑
     *@param webView 需要被回收的webview
     *
     * */
    public void removeWebView(ViewGroup view, WebView webView) {
        view.removeView(webView);
        webView.loadUrl("");
        webView.stopLoading();
        webView.setWebChromeClient(null);
        webView.setWebViewClient(null);
        webView.clearCache(true);
        webView.clearHistory();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);  //设置 缓存模式(true);
        webView.getSettings().setAppCacheEnabled(false);
        webView.getSettings().setSupportZoom(false);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setDomStorageEnabled(true);

        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);

        webView.getSettings().setLoadWithOverviewMode(false);

        webView.getSettings().setUserAgentString("android_client");
        webView.getSettings().setDefaultTextEncodingName("UTF-8");
        webView.getSettings().setDefaultFontSize(16);
        synchronized (lock) {
            inUse.remove(webView);
            if (available.size() < maxSize) {
                available.add(webView);
            } else {
                webView = null;
            }
            currentSize--;
        }
    }

    /**
     * 设置webview池个数
     * @param size webview池个数
     * */
    public void setMaxPoolSize(int size) {
        synchronized (lock) {
            maxSize = size;
        }
    }
}
