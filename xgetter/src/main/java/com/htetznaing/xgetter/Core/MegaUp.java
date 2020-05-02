package com.htetznaing.xgetter.Core;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.UnsupportedEncodingException;

public class MegaUp {
    private static WebView webView;
    private static OnDone onDone;
    private Handler mHandler = new Handler();
    private boolean stopMMHandler = false;

    @SuppressLint("JavascriptInterface")
    public void get(Context context,String url, final OnDone onDone){
        this.onDone = onDone;
        webView = new WebView(context);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new MyInterface(),"xGetter");
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                findMe();
            }
        });

        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                final String fileName= URLUtil.guessFileName(url,contentDisposition,mimetype);
                if (webView.getTitle().contains(fileName)) {
                    destroyWebView();
                    stopMMHandler = true;
                    result(url);
                }
            }
        });
        webView.loadUrl(url);
    }

    public static String decodeBase64(String coded){
        try {
            return new String(Base64.decode(coded.getBytes("UTF-8"), Base64.DEFAULT));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void findMe() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (webView!=null) {
                    webView.loadUrl("javascript: (function() {" + decodeBase64(getJs()) + "})()");
                    if (!stopMMHandler) {
                        mHandler.postDelayed(this, 100);
                    }
                }
            }
        };
        mHandler.post(runnable);
    }

    private String getJs(){
        return "aWYgKHdpbmRvdy5sb2NhdGlvbi5ocmVmLmluZGV4T2YoJ2Vycm9yLmh0bWwnKSAhPT0gLTEpIHsKICAgIHhHZXR0ZXIuZXJyb3Iod2luZG93LmxvY2F0aW9uLmhyZWYpOwp9IGVsc2UgewogICAgc2Vjb25kcyA9IDA7CiAgICBkaXNwbGF5KCk7CgogICAgdmFyIHJlZ2V4ID0gL2hyZWY9JyguKiknL2dtLAogICAgICAgIHN0ciA9IGRvY3VtZW50LmJvZHkuaW5uZXJIVE1MLAogICAgICAgIG07CgogICAgaWYgKChtID0gcmVnZXguZXhlYyhzdHIpKSAhPT0gbnVsbCkgewogICAgICAgIGRsKG1bMV0pOwogICAgfQoKICAgIGZ1bmN0aW9uIGRsKHVybCkgewogICAgICAgIHZhciBhbmNob3IgPSBkb2N1bWVudC5jcmVhdGVFbGVtZW50KCdhJyk7CiAgICAgICAgYW5jaG9yLnNldEF0dHJpYnV0ZSgnaHJlZicsIHVybCk7CiAgICAgICAgYW5jaG9yLnNldEF0dHJpYnV0ZSgnZG93bmxvYWQnLCBkb2N1bWVudC50aXRsZSk7CiAgICAgICAgYW5jaG9yLnN0eWxlLmRpc3BsYXkgPSAnbm9uZSc7CiAgICAgICAgZG9jdW1lbnQuYm9keS5hcHBlbmRDaGlsZChhbmNob3IpOwogICAgICAgIGFuY2hvci5jbGljaygpOwogICAgICAgIGRvY3VtZW50LmJvZHkucmVtb3ZlQ2hpbGQoYW5jaG9yKTsKICAgIH0KCn0=";
    }

    private static void destroyWebView() {
        if (webView!=null) {
            webView.clearHistory();
            // NOTE: clears RAM cache, if you pass true, it will also clear the disk cache.
            // Probably not a great idea to pass true if you have other WebViews still alive.
            webView.clearCache(true);

            // Loading a blank page is optional, but will ensure that the WebView isn't doing anything when you destroy it.
            webView.loadUrl("about:blank");

            webView.onPause();
            webView.removeAllViews();
            webView.destroyDrawingCache();

            // NOTE: This pauses JavaScript execution for ALL WebViews,
            // do not use if you have other WebViews still alive.
            // If you create another WebView after calling this,
            // make sure to call mWebView.resumeTimers().
            webView.pauseTimers();

            // NOTE: This can occasionally cause a segfault below API 17 (4.2)
            webView.destroy();

            // Null out the reference so that you don't end up re-using it.
            webView = null;
        }
    }

    private class MyInterface {
        @SuppressWarnings("unused")
        @JavascriptInterface
        public void error(final String error) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    destroyWebView();
                    stopMMHandler = true;
                    result(null);
                }
            });
        }
    }

    private static void result(String result){
        destroyWebView();
        onDone.result(result);
    }

    public interface OnDone{
        void result(String result);
    }
}
