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

import com.htetznaing.xgetter.Model.XModel;
import com.htetznaing.xgetter.XGetter;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class VideoBmX {
    private static WebView webView;
    private static XGetter.OnTaskCompleted onTaskCompleted;

    @SuppressLint("JavascriptInterface")
    public static void get(Context context,String url, final XGetter.OnTaskCompleted onDone){
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
        return "dmFyIHNyYyA9IGRvY3VtZW50LmdldEVsZW1lbnRzQnlUYWdOYW1lKCd2aWRlbycpOwppZihzcmMpewogICAgc3JjID0gc3JjLml0ZW0oMCkuc3JjOwogICAgZGwoc3JjKTsKfQoKZnVuY3Rpb24gZGwodXJsKSB7CiAgICAgICAgdmFyIGFuY2hvciA9IGRvY3VtZW50LmNyZWF0ZUVsZW1lbnQoJ2EnKTsKICAgICAgICBhbmNob3Iuc2V0QXR0cmlidXRlKCdocmVmJywgdXJsKTsKICAgICAgICBhbmNob3Iuc2V0QXR0cmlidXRlKCdkb3dubG9hZCcsIGRvY3VtZW50LnRpdGxlKTsKICAgICAgICBhbmNob3Iuc3R5bGUuZGlzcGxheSA9ICdub25lJzsKICAgICAgICBkb2N1bWVudC5ib2R5LmFwcGVuZENoaWxkKGFuY2hvcik7CiAgICAgICAgYW5jaG9yLmNsaWNrKCk7CiAgICAgICAgZG9jdW1lbnQuYm9keS5yZW1vdmVDaGlsZChhbmNob3IpOwp9";
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
        System.out.println("Fucked: "+result);
        if (result!=null && !result.isEmpty()){
            ArrayList<XModel> xModels = new ArrayList<>();
            XModel model = new XModel();
            model.setUrl(result);
            model.setQuality("Normal");
            xModels.add(model);
            onTaskCompleted.onTaskCompleted(xModels,false);
        }else onTaskCompleted.onError();
    }
}
