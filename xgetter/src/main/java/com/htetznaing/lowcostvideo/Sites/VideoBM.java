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

public class VideoBM {
    private static WebView webView;
    private static LowCostVideo.OnTaskCompleted onTaskCompleted;
    private static boolean gotMe = false;

    @SuppressLint("JavascriptInterface")
    public static void get(Context context,String url, final LowCostVideo.OnTaskCompleted onDone){
        onTaskCompleted = onDone;
        webView = new WebView(context);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new MyInterface(),"xGetter");
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                System.out.println("Load => Find");
                findMe();
            }
        });

        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                final String fileName= URLUtil.guessFileName(url,contentDisposition,mimetype);
                if (webView.getTitle().contains(fileName)) {
                    destroyWebView();
                    result(url);
                }
            }
        });

        url = url.replaceAll(".com/",".com/embed-");
        gotMe = false;
        webView.loadUrl(url);
    }

    private static String decodeBase64(String coded){
        try {
            return new String(Base64.decode(coded.getBytes("UTF-8"), Base64.DEFAULT));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void findMe() {
        if (webView!=null) {
            webView.loadUrl("javascript: (function() {" + decodeBase64(getJs()) + "})()");
        }
    }

    private static String getJs(){
        return "dmFyIHNyYyA9IGRvY3VtZW50LmdldEVsZW1lbnRzQnlUYWdOYW1lKCdzb3VyY2UnKTsKaWYoc3JjLmxlbmd0aD4wKXsKICAgIHNyYyA9IHNyYy5pdGVtKDApLnNyYzsKICAgIGRsKHNyYyk7Cn0gZWxzZSB7CiAgICB4R2V0dGVyLmVycm9yKHdpbmRvdy5sb2NhdGlvbi5ocmVmKTsKfQoKZnVuY3Rpb24gZGwodXJsKSB7CiAgICB2YXIgYW5jaG9yID0gZG9jdW1lbnQuY3JlYXRlRWxlbWVudCgnYScpOwogICAgYW5jaG9yLnNldEF0dHJpYnV0ZSgnaHJlZicsIHVybCk7CiAgICBhbmNob3Iuc2V0QXR0cmlidXRlKCdkb3dubG9hZCcsIGRvY3VtZW50LnRpdGxlKTsKICAgIGFuY2hvci5zdHlsZS5kaXNwbGF5ID0gJ25vbmUnOwogICAgZG9jdW1lbnQuYm9keS5hcHBlbmRDaGlsZChhbmNob3IpOwogICAgYW5jaG9yLmNsaWNrKCk7CiAgICBkb2N1bWVudC5ib2R5LnJlbW92ZUNoaWxkKGFuY2hvcik7Cn0";
    }

    private static void destroyWebView() {
        if (webView!=null) {
            webView.loadUrl("about:blank");
            webView.destroy();
        }
    }

    private static class MyInterface {
        @SuppressWarnings("unused")
        @JavascriptInterface
        public void error(final String error) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    destroyWebView();
                    result(null);
                }
            });
        }
    }

    private static void result(String result){
        if (!gotMe) {
            destroyWebView();
            System.out.println("Fucked: " + result);
            if (result != null && !result.isEmpty()) {
                ArrayList<XModel> xModels = new ArrayList<>();
                XModel model = new XModel();
                model.setUrl(result);
                model.setQuality("Normal");
                xModels.add(model);
                onTaskCompleted.onTaskCompleted(xModels, false);
            } else onTaskCompleted.onError();
        }
    }
}
