package com.htetznaing.lowcostvideo.Core;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.htetznaing.lowcostvideo.LowCostVideo;
import com.htetznaing.lowcostvideo.Model.XModel;
import com.htetznaing.lowcostvideo.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class GDrive2020 {
    private static WebView webView;
    private static LowCostVideo.OnTaskCompleted onTaskCompleted;

    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    public static void get(Context context,String url, final LowCostVideo.OnTaskCompleted onDone){
        onTaskCompleted = onDone;
        webView = new WebView(context);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new MyInterface(),"HtetzNaing");
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                return super.onConsoleMessage(consoleMessage);
            }
        });
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                findMe();
            }
        });
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
            String js = "javascript: (function() {" + decodeBase64(getJs()) + "})()";
            webView.loadUrl(js);
        }
    }

    private static String getJs(){
        return "ZnVuY3Rpb24gZ2V0X21hcCgpIHsKICAgIHZhciBzdHIgPSBkb2N1bWVudC5oZWFkLmlubmVySFRNTCwKICAgICAgICByZWdleCA9IC9mbXRfc3RyZWFtX21hcCIsKC4qPyldL2dtLAogICAgICAgIG07CgogICAgaWYgKChtID0gcmVnZXguZXhlYyhzdHIpKSAhPT0gbnVsbCkgewogICAgICAgIHJldHVybiBtWzFdLnJlcGxhY2UoLyIvZywgIiwiKTsKICAgIH0KICAgIHJldHVybiBudWxsOwp9CgoKZnVuY3Rpb24gcGFyc2VfbWFwKHN0cikgewogICAgdmFyIHJlZ2V4ID0gL2h0dHBzOiguKj8pLC9tZywKICAgICAgICBtLAogICAgICAgIHNyYyA9IFtdOwogICAgd2hpbGUgKChtID0gcmVnZXguZXhlYyhzdHIpKSAhPT0gbnVsbCkgewogICAgICAgIGlmIChtLmluZGV4ID09PSByZWdleC5sYXN0SW5kZXgpIHsKICAgICAgICAgICAgcmVnZXgubGFzdEluZGV4Kys7CiAgICAgICAgfQoKICAgICAgICBtLmZvckVhY2goKG1hdGNoLCBncm91cEluZGV4KSA9PiB7CiAgICAgICAgICAgIGlmIChncm91cEluZGV4ID09IDEpIHsKICAgICAgICAgICAgICAgIHNyYy5wdXNoKG1hdGNoKTsKICAgICAgICAgICAgfQogICAgICAgIH0pOwogICAgfQogICAgcmV0dXJuIHNyYzsKfQoKZnVuY3Rpb24gZ2V0X3F1YWxpdHkodXJsKSB7CiAgICBpZiAoY29udGFpbih1cmwsICdpdGFnPTM3JykpIHsKICAgICAgICByZXR1cm4gJzEwODBwJzsKICAgIH0gZWxzZSBpZiAoY29udGFpbih1cmwsICdpdGFnPTIyJykpIHsKICAgICAgICByZXR1cm4gJzcyMHAnOwogICAgfSBlbHNlIGlmIChjb250YWluKHVybCwgJ2l0YWc9NTknKSkgewogICAgICAgIHJldHVybiAnNDgwcCc7CiAgICB9IGVsc2UgaWYgKGNvbnRhaW4odXJsLCAnaXRhZz0xOCcpKSB7CiAgICAgICAgcmV0dXJuICczNjBwJzsKICAgIH0KICAgIHJldHVybiBudWxsOwp9CgpmdW5jdGlvbiBjb250YWluKG0sIHMpIHsKICAgIHJldHVybiBtLmluZGV4T2YocykgIT0gLTE7Cn0KCmZ1bmN0aW9uIGdldERyaXZlSUQoKSB7CiAgICByZXR1cm4gd2luZG93LmxvY2F0aW9uLmhyZWYubWF0Y2goL1stXHddezI1LH0vKTsKfQoKaWYgKHdpbmRvdy5sb2NhdGlvbi5ob3N0ID09ICdkcml2ZS5nb29nbGUuY29tJyAmJiBnZXREcml2ZUlEKCkpIHsKICAgIHZhciBtYXAgPSBnZXRfbWFwKCksCiAgICAgICAgb3V0ID0gW107CgogICAgaWYgKG1hcCAhPSBudWxsKSB7CiAgICAgICAgdmFyIHNyeCA9IHBhcnNlX21hcChtYXApOwogICAgICAgIHNyeC5mb3JFYWNoKGUgPT4gewogICAgICAgICAgICBlID0gSlNPTi5wYXJzZSgnImh0dHBzOicgKyBlICsgJyInKTsKICAgICAgICAgICAgdmFyIHF1YSA9IGdldF9xdWFsaXR5KGUpOwogICAgICAgICAgICBpZiAocXVhICE9IG51bGwpIHsKICAgICAgICAgICAgICAgIHZhciB0bXAgPSB7CiAgICAgICAgICAgICAgICAgICAgJ2xhYmVsJzogcXVhLAogICAgICAgICAgICAgICAgICAgICdmaWxlJzogZQogICAgICAgICAgICAgICAgfTsKICAgICAgICAgICAgICAgIG91dC5wdXNoKHRtcCk7CiAgICAgICAgICAgIH0KICAgICAgICB9KTsKICAgIH0gZWxzZSB7CiAgICAgICAgSHRldHpOYWluZy5lcnJvcigpOwogICAgfQoKICAgIEh0ZXR6TmFpbmcucmVzdWx0KEpTT04uc3RyaW5naWZ5KG91dCkpOwp9";
    }

    private static void destroyWebView() {
        if (webView!=null) {
            webView.loadUrl("about:blank");
        }
    }

    private static class MyInterface {
        @SuppressWarnings("unused")
        @JavascriptInterface
        public void error() {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    destroyWebView();
                    out(null);
                }
            });
        }

        @SuppressWarnings("unused")
        @JavascriptInterface
        public void result(final String result) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    destroyWebView();
                    out(result);
                }
            });
        }
    }

    private static void out(String result){
        destroyWebView();
        if (result!=null){
            ArrayList<XModel> xModels = new ArrayList<>();
            try {
                JSONArray jsonArray = new JSONArray(result);
                for(int i=0;i<jsonArray.length();i++){
                    String url = jsonArray.getJSONObject(i).getString("file"),quality = jsonArray.getJSONObject(i).getString("label"),cookies = CookieManager.getInstance().getCookie(url);
                    XModel model = new XModel();
                    model.setUrl(url);
                    model.setQuality(quality);
                    model.setCookie(cookies);
                    xModels.add(model);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            onTaskCompleted.onTaskCompleted(Utils.sortMe(xModels),true);
        }else onTaskCompleted.onError();
    }
}
