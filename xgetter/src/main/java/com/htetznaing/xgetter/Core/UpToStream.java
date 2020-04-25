package com.htetznaing.xgetter.Core;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class UpToStream {
    private static WebView webView;
    private static OnDone onDone;
    @SuppressLint("JavascriptInterface")
    public void get(Context context, final String js, final OnDone onDone){
        this.onDone = onDone;
        webView = new WebView(context);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                System.out.println(consoleMessage.message());
                String msg = consoleMessage.message().toLowerCase();
                if (msg.contains("sources is not defined")){
                    destroyWebView();
                    onDone.result(null);
                }else if (consoleMessage.message().toLowerCase().contains("error")){
                    destroyWebView();
                    System.out.println("Retry");
                    onDone.retry();
                }
                return super.onConsoleMessage(consoleMessage);
            }
        });
        webView.addJavascriptInterface(new MyInterface(),"xGetter");
        inject(js);
    }

    private static void inject(String js){
        webView.loadUrl("javascript: (function() {"+js+";\nxGetter.fuck(JSON.stringify(sources));})()");
    }

    private static void destroyWebView() {
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

    private static class MyInterface {
            @SuppressWarnings("unused")
            @JavascriptInterface
            public void fuck(final String html) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        result(html);
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
        void retry();
    }
}
