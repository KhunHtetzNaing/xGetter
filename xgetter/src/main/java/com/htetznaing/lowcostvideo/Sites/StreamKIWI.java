package com.htetznaing.lowcostvideo.Sites;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.URLUtil;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.htetznaing.lowcostvideo.LowCostVideo;
import com.htetznaing.lowcostvideo.Model.XModel;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class StreamKIWI {
    private static WebView webView;
    private static LowCostVideo.OnTaskCompleted onTaskCompleted;

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

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                System.out.println("shouldOverrideUrlLoading => "+url);
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                System.out.println("shouldOverrideUrlLoading => "+view.getUrl());
                return super.shouldOverrideUrlLoading(view, request);
            }
        });

        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                final String fileName= URLUtil.guessFileName(url,contentDisposition,mimetype);
                destroyWebView();
                result(url);
            }
        });

        url = url.replaceAll(".kiwi\\/.*\\/",".kiwi/e/");

        System.out.println("Load => "+url);
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
        return "dmFyIHNyYyA9IGRvY3VtZW50LmdldEVsZW1lbnRzQnlUYWdOYW1lKCdzb3VyY2UnKTsKaWYgKHNyYykgewogICAgc3JjID0gc3JjLml0ZW0oMCkuc3JjOwogICAgZGwoc3JjKTsKfSBlbHNlIHsKICAgIHhHZXR0ZXIuZXJyb3Iod2luZG93LmxvY2F0aW9uLmhyZWYpOwp9CgpmdW5jdGlvbiBkbCh1cmwpIHsKICAgIHZhciBhbmNob3IgPSBkb2N1bWVudC5jcmVhdGVFbGVtZW50KCdhJyk7CiAgICBhbmNob3Iuc2V0QXR0cmlidXRlKCdocmVmJywgdXJsKTsKICAgIGFuY2hvci5zZXRBdHRyaWJ1dGUoJ2Rvd25sb2FkJywgZG9jdW1lbnQudGl0bGUpOwogICAgYW5jaG9yLnNldEF0dHJpYnV0ZSgndGFyZ2V0JywgJ19ibGFuaycpOwogICAgYW5jaG9yLnN0eWxlLmRpc3BsYXkgPSAnbm9uZSc7CiAgICBkb2N1bWVudC5ib2R5LmFwcGVuZENoaWxkKGFuY2hvcik7CiAgICBhbmNob3IuY2xpY2soKTsKICAgIGRvY3VtZW50LmJvZHkucmVtb3ZlQ2hpbGQoYW5jaG9yKTsKfQ";
    }

    private static void destroyWebView() {
        if (webView!=null) {
            webView.loadUrl("about:blank");
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
        destroyWebView();
        if (result!=null){
            ArrayList<XModel> xModels = new ArrayList<>();
            XModel model = new XModel();
            model.setUrl(result);
            model.setQuality("Normal");
            xModels.add(model);
            onTaskCompleted.onTaskCompleted(xModels,false);
        }else onTaskCompleted.onError();
    }
}
