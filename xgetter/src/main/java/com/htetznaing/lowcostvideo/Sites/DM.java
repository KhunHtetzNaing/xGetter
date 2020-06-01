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
import com.htetznaing.lowcostvideo.Utils.DailyMotionUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import static com.htetznaing.lowcostvideo.Utils.Utils.sortMe;

public class DM {
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
        });

        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                final String fileName= URLUtil.guessFileName(url,contentDisposition,mimetype);
                destroyWebView();
                result(url);
            }
        });

        url = "https://www.dailymotion.com/embed/video/"+ DailyMotionUtils.getDailyMotionID(url);

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
        return "eEdldHRlci5lcnJvcihKU09OLnN0cmluZ2lmeShjb25maWcubWV0YWRhdGEucXVhbGl0aWVzKSk7";
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
                    System.out.println(error);
                    destroyWebView();
                    result(error);
                }
            });
        }
    }

    private static void result(String response){
        destroyWebView();
        if (response!=null){
            new DailyMotionUtils().fetch(response, new DailyMotionUtils.OnDone() {
                @Override
                public void onResult(ArrayList<XModel> xModels) {
                    if (xModels!=null){
                        onTaskCompleted.onTaskCompleted(sortMe(xModels),true);
                    }else onTaskCompleted.onError();
                }
            });
        }else onTaskCompleted.onError();
    }
}
