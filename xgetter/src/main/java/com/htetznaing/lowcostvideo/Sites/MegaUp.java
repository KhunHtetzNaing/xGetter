package com.htetznaing.lowcostvideo.Sites;

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

import com.htetznaing.lowcostvideo.LowCostVideo;
import com.htetznaing.lowcostvideo.Model.XModel;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class MegaUp {
    private WebView webView;
    private LowCostVideo.OnTaskCompleted onDone;
    private Handler mHandler = new Handler();
    private boolean stopMMHandler = false;

    @SuppressLint("JavascriptInterface")
    public void get(Context context, String url, final LowCostVideo.OnTaskCompleted onDone){
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

    private String decodeBase64(String coded){
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

    private void destroyWebView() {
        if (webView!=null) {
            webView.loadUrl("about:blank");
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

    private void result(String result){
        destroyWebView();
        if (result!=null){
            ArrayList<XModel> xModels = new ArrayList<>();
            XModel xModel = new XModel();
            xModel.setUrl(result);
            xModel.setQuality("Normal");
            xModels.add(xModel);
            onDone.onTaskCompleted(xModels,false);
        }else {
            onDone.onError();
        }
    }
}