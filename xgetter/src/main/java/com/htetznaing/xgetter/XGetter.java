package com.htetznaing.xgetter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.webkit.ConsoleMessage;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 *      xGetter
 *         By
 *   Khun Htetz Naing
 * Repo => https://github.com/KhunHtetzNaing/xGetter
 * Openload,Streamango,StreamCherry,Mp4Upload,RapidVideo,SendVid,VidCloud,MegaUp Stream/Download URL Finder!
 *
 */

public class XGetter {
    private WebView webView;
    private Context context;
    private OnTaskCompleted onComplete;
    private final String agent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36";
    private final String openload = "https?:\\/\\/(www\\.)?(openload|oload)\\.[^\\/,^\\.]{2,}\\/(embed|f)\\/.+";
    private final String fruits = "https?:\\/\\/(www\\.)?(streamango|fruitstreams|streamcherry|fruitadblock|fruithosts)\\.[^\\/,^\\.]{2,}\\/(f|embed)\\/.+";
    private final String megaup = "https?:\\/\\/(www\\.)?(megaup)\\.[^\\/,^\\.]{2,}\\/.+";
    private final String mp4upload = "https?:\\/\\/(www\\.)?(mp4upload)\\.[^\\/,^\\.]{2,}\\/.+";
    private final String sendvid = "https?:\\/\\/(www\\.)?(sendvid)\\.[^\\/,^\\.]{2,}\\/.+";
    private final String vidcloud = "https?:\\/\\/(www\\.)?(vidcloud|vcstream|loadvid)\\.[^\\/,^\\.]{2,}\\/(v|embed)\\/.+";
    private final String rapidvideo = "https?:\\/\\/(www\\.)?rapidvideo\\.[^\\/,^\\.]{2,}\\/(\\?v=[^&\\?]*|e\\/.+|v\\/.+)";
    private final String gphoto = "https?:\\/\\/(photos.google.com)\\/(u)?\\/?(\\d)?\\/?(share)\\/.+(key=).+";
    private final String fb = "(?:https?://)?(?:www.|web.|m.)?facebook.com/(?:video.php\\?v=\\d+|photo.php\\?v=\\d+|\\?v=\\d+)|\\S+/videos/((\\S+)/(\\d+)|(\\d+))/?";
    private final String mediafire = "https?:\\/\\/(www\\.)?(mediafire)\\.[^\\/,^\\.]{2,}\\/(file)\\/.+";

    public XGetter(Context view) {
        this.context=view;
    }

    private void init(){
        webView = new WebView(context);
        webView.setWillNotDraw(true);
        webView.addJavascriptInterface(new xJavascriptInterface(),"xGetter");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient(){

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                letFuck(view);
            }
        });
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                onComplete.onTaskCompleted(url);
            }
        });
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                System.out.println(consoleMessage.message());
                return super.onConsoleMessage(consoleMessage);
            }
        });
    }

    class xJavascriptInterface {
        @SuppressWarnings("unused")
        @JavascriptInterface
        public void fuck(final String url) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    onComplete.onTaskCompleted(url);
                }
            });
        }
    }

    private void letFuck(WebView view) {
            String encoded = "LyoKICAgICAgICB4R2V0dGVyCiAgICAgICAgICBCeQogICAgS2h1biBIdGV0eiBOYWluZyBbZmIu\n" +
                    "Y29tL0tIdGV0ek5haW5nXQpSZXBvID0+IGh0dHBzOi8vZ2l0aHViLmNvbS9LaHVuSHRldHpOYWlu\n" +
                    "Zy94R2V0dGVyCgoqLwp2YXIgb3BlbmxvYWQgPSAvaHR0cHM/OlwvXC8od3d3XC4pPyhvcGVubG9h\n" +
                    "ZHxvbG9hZClcLlteXC8sXlwuXXsyLH1cLyhlbWJlZHxmKVwvLisvaSwKICAgIHN0cmVhbSA9IC9o\n" +
                    "dHRwcz86XC9cLyh3d3dcLik/KHN0cmVhbWFuZ298ZnJ1aXRzdHJlYW1zfHN0cmVhbWNoZXJyeXxm\n" +
                    "cnVpdGFkYmxvY2t8ZnJ1aXRob3N0cylcLlteXC8sXlwuXXsyLH1cLyhmfGVtYmVkKVwvLisvaSwK\n" +
                    "ICAgIG1lZ2F1cCA9IC9odHRwcz86XC9cLyh3d3dcLik/KG1lZ2F1cClcLlteXC8sXlwuXXsyLH1c\n" +
                    "Ly4rL2ksCiAgICBtcDR1cGxvYWQgPSAvaHR0cHM/OlwvXC8od3d3XC4pP21wNHVwbG9hZFwuW15c\n" +
                    "LyxeXC5dezIsfVwvZW1iZWRcLS4rL2ksCiAgICBzZW5kdmlkID0gL2h0dHBzPzpcL1wvKHd3d1wu\n" +
                    "KT8oc2VuZHZpZClcLlteXC8sXlwuXXsyLH1cLy4rL2ksCiAgICB2aWRjbG91ZCA9IC9odHRwcz86\n" +
                    "XC9cLyh3d3dcLik/KHZpZGNsb3VkfHZjc3RyZWFtfGxvYWR2aWQpXC5bXlwvLF5cLl17Mix9XC9l\n" +
                    "bWJlZFwvKFthLXpBLVowLTldKikvaSwKICAgIHJhcGlkdmlkZW8gPSAvaHR0cHM/OlwvXC8od3d3\n" +
                    "XC4pP3JhcGlkdmlkZW9cLlteXC8sXlwuXXsyLH1cLyhcP3Y9W14mXD9dKnxlXC8uK3x2XC8uKykv\n" +
                    "aTsKCmlmIChvcGVubG9hZC50ZXN0KHdpbmRvdy5sb2NhdGlvbi5ocmVmKSkgewogICAgdmFyIHNw\n" +
                    "YW4gPSBkb2N1bWVudC5xdWVyeVNlbGVjdG9yQWxsKCJzcGFuIik7CiAgICBmb3IgKHZhciBpID0g\n" +
                    "MDsgaSA8IHNwYW4ubGVuZ3RoOyBpKyspIHsKICAgICAgICB2YXIgbG9sID0gc3Bhbi5pdGVtKGkp\n" +
                    "LnRleHRDb250ZW50OwogICAgICAgIGlmIChsb2wuaW5kZXhPZignficpICE9IC0xKSB7CiAgICAg\n" +
                    "ICAgICAgIHhHZXR0ZXIuZnVjayhkb2N1bWVudC5sb2NhdGlvbi5wcm90b2NvbCArICcvLycgKyBk\n" +
                    "b2N1bWVudC5sb2NhdGlvbi5ob3N0ICsgJy9zdHJlYW0vJyArIGxvbCArICc/bWltZT10cnVlJyk7\n" +
                    "CiAgICAgICAgfQogICAgfQp9IGVsc2UgaWYgKHN0cmVhbS50ZXN0KHdpbmRvdy5sb2NhdGlvbi5o\n" +
                    "cmVmKSkgewogICAgeEdldHRlci5mdWNrKHdpbmRvdy5sb2NhdGlvbi5wcm90b2NvbCArIHNyY2Vz\n" +
                    "WzBdWyJzcmMiXSk7Cn0gZWxzZSBpZiAobWVnYXVwLnRlc3Qod2luZG93LmxvY2F0aW9uLmhyZWYp\n" +
                    "KSB7CiAgICBzZWNvbmRzID0gMDsKICAgIGRpc3BsYXkoKTsKICAgIHdpbmRvdy5sb2NhdGlvbi5y\n" +
                    "ZXBsYWNlKGRvY3VtZW50LmdldEVsZW1lbnRzQnlDbGFzc05hbWUoImJ0biBidG4tZGVmYXVsdCIp\n" +
                    "Lml0ZW0oMCkuaHJlZik7Cn0gZWxzZSBpZiAobXA0dXBsb2FkLnRlc3Qod2luZG93LmxvY2F0aW9u\n" +
                    "LmhyZWYpKSB7CiAgICB4R2V0dGVyLmZ1Y2soZG9jdW1lbnQuZ2V0RWxlbWVudHNCeUNsYXNzTmFt\n" +
                    "ZSgnanctdmlkZW8ganctcmVzZXQnKS5pdGVtKDApLnNyYyk7Cn0gZWxzZSBpZiAocmFwaWR2aWRl\n" +
                    "by50ZXN0KHdpbmRvdy5sb2NhdGlvbi5ocmVmKSkgewogICAgeEdldHRlci5mdWNrKGRvY3VtZW50\n" +
                    "LmdldEVsZW1lbnRzQnlUYWdOYW1lKCdzb3VyY2UnKS5pdGVtKDApLnNyYyk7Cn0gZWxzZSBpZiAo\n" +
                    "c2VuZHZpZC50ZXN0KHdpbmRvdy5sb2NhdGlvbi5ocmVmKSkgewogICAgeEdldHRlci5mdWNrKGRv\n" +
                    "Y3VtZW50LmdldEVsZW1lbnRzQnlUYWdOYW1lKCdzb3VyY2UnKS5pdGVtKDApLnNyYyk7Cn0gZWxz\n" +
                    "ZSBpZiAodmlkY2xvdWQudGVzdCh3aW5kb3cubG9jYXRpb24uaHJlZikpIHsKICAgICQuYWpheCh7\n" +
                    "CiAgICAgICAgdXJsOiAnL2Rvd25sb2FkJywKICAgICAgICBtZXRob2Q6ICdQT1NUJywKICAgICAg\n" +
                    "ICBkYXRhOiB7CiAgICAgICAgICAgIGZpbGVfaWQ6IGZpbGVJRAogICAgICAgIH0sCiAgICAgICAg\n" +
                    "ZGF0YVR5cGU6ICdqc29uJywKICAgICAgICBzdWNjZXNzOiBmdW5jdGlvbihyZXMpIHsKICAgICAg\n" +
                    "ICAgICAgJCgnLnF1YWxpdHktbWVudScpLmh0bWwocmVzLmh0bWwpOwogICAgICAgICAgICB2YXIg\n" +
                    "ZGF0YSA9IHJlcy5odG1sOwogICAgICAgICAgICB2YXIgcmVnZXggPSAvaHJlZj0iKC4qPykiLzsK\n" +
                    "ICAgICAgICAgICAgdmFyIG07CiAgICAgICAgICAgIGlmICgobSA9IHJlZ2V4LmV4ZWMoZGF0YSkp\n" +
                    "ICE9PSBudWxsKSB7CiAgICAgICAgICAgICAgICB4R2V0dGVyLmZ1Y2sobVsxXSk7CiAgICAgICAg\n" +
                    "ICAgIH0KICAgICAgICB9CiAgICB9KTsKfSBlbHNlIGlmICh3aW5kb3cubG9jYXRpb24uaG9zdCA9\n" +
                    "PSAnZHJpdmUuZ29vZ2xlLmNvbScpIHsKICAgIGRvY3VtZW50LmdldEVsZW1lbnRCeUlkKCd1Yy1k\n" +
                    "b3dubG9hZC1saW5rJykuY2xpY2soKTsKfQoKLyoKU3VwcG9ydGVkIFNpdGVzCj0+IE9wZW5sb2Fk\n" +
                    "IChBbGwgZG9tYWlucykKPT4gRnJ1aXRTdHJlYW1zIChTdHJlYW1jaGVycnksU3RyZWFtYW5nbyBh\n" +
                    "bmQgZXRjLi4pCj0+IE1wNFVwbG9hZAo9PiBSYXBpZFZpZGVvCj0+IFNlbmRWaWQKPT4gTWVnYVVw\n" +
                    "Cj0+IFZpZENsb3VkIChBbGwgZG9tYWlucykKKi8=";
            view.loadUrl("javascript:(function() {" +
                    "var parent = document.getElementsByTagName('head').item(0);" +
                    "var script = document.createElement('script');" +
                    "script.type = 'text/javascript';" +
                    // Tell the browser to BASE64-decode the string into your script !!!
                    "script.innerHTML = window.atob('" + encoded + "');" +
                    "parent.appendChild(script)" +
                    "})()");
    }

    public void find(String url){
        init();
        boolean fb = false;
        boolean run = false;
        boolean mfire = false;
        if (check(openload,url)){
            //Openload
            run = true;
        }else if (check(fruits,url)){
            //Fruits
            run = true;
        }else if (check(megaup,url)){
            //megaup
            run = true;
        }else if (check(mp4upload,url)){
            run = true;
            if (!url.contains("embed-")){
                final String regex = "com\\/([^']*)";
                final Pattern pattern = Pattern.compile(regex);
                final Matcher matcher = pattern.matcher(url);
                if (matcher.find()) {
                    String id = matcher.group(1);
                    if (id.contains("/")) {
                        id = id.substring(0, id.lastIndexOf("/"));
                    }
                    url = "https://www.mp4upload.com/embed-"+id+".html";
                }else {
                    run = false;
                }
            }

        }else if (check(sendvid,url)){
            //sendvid
            run = true;
        }else if (check(vidcloud,url)){
            //vidcloud
            run = true;
            if (!url.contains("/embed/") && url.contains("/v/")){
                url = url.replace("/v/","/embed/");
            }
        }else if (check(rapidvideo,url)){
            //rapidvideo
            run = true;
        }else if (check(gphoto,url)){
            //gphotos
            run = true;
        }else if (url.contains("drive.google.com") && get_drive_id(url)!=null) {
            //gdrive
            run = true;
            url = "https://drive.google.com/uc?id="+get_drive_id(url)+"&export=download";
        }else if (check_fb_video(url)){
            //fb
            run = true;
            fb = true;
        }else if (check(mediafire,url)){
            //mediafire
            run = true;
            mfire = true;
        }

        if (run) {
            if (check(gphoto,url)){
                gphotoORfb(url,false);
            }else if (fb) {
                gphotoORfb(url, true);
            }else if (mfire){
                mfire(url);
            }else {
                webView.loadUrl(url);
            }
        }else onComplete.onError();
    }

    private boolean check_fb_video(String url){
        final Pattern pattern = Pattern.compile(fb);
        final Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            return true;
        }
        return false;
    }

    private String get_drive_id(String string){
        final String regex = "[-\\w]{25,}";
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(string);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    private void mfire(String url){
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                final String regex = "aria-label=\"Download file\"\\n.+href=\"(.*)\"";
                final Pattern pattern = Pattern.compile(regex);
                final Matcher matcher = pattern.matcher(response);
                if (matcher.find()) {
                    onComplete.onTaskCompleted(matcher.group(1));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onComplete.onError();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("User-agent", agent);
                return headers;
            }
        };
        Volley.newRequestQueue(context).add(request);
    }

    private void gphotoORfb(String url, final boolean fb){
        final String data = url;
        if (url!=null) {
            int method = Request.Method.GET;

            if (fb){
                method = Request.Method.POST;
                url = "https://fbdown.net/download.php";
            }

            StringRequest request = new StringRequest(method, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (fb) {
                        onComplete.onFbTaskCompleted(getFbLink(response, false), getFbLink(response, true));
                    } else {
                        onComplete.onTaskCompleted(getGPhotoLink(response));
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    onComplete.onError();
                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    if (fb) {
                        Map<String, String> params = new HashMap<>();
                        params.put("URLz", data);
                        return params;
                    }
                    return super.getParams();
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("User-agent", agent);
                    return headers;
                }
            };

            Volley.newRequestQueue(context).add(request);
        }else onComplete.onError();
    }

    private String getGPhotoLink(String string){
        string = string.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
        try {
            string = URLDecoder.decode(string,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        final String regex = "https:\\/\\/(.*?)=m(22|18|37)";
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(string);
        if (matcher.find()){
            return matcher.group();
        }
        return null;
    }

    private String getFbLink(String source,boolean hd){
        if (source!=null) {
            String end = "download=";
            String start = (hd ? "id=\"hdlink\"" : "id=\"sdlink\"");
            int idx = source.indexOf(start);
            if (idx != -1) {
                source = source.substring(idx + start.length());
                String string = source.substring(0, source.indexOf(end));
                if (string != null) {
                    final String regex = "href=\"(.*?)\"";
                    final Pattern pattern = Pattern.compile(regex);
                    final Matcher matcher = pattern.matcher(string);
                    if (matcher.find()) {
                        return matcher.group(1).replace("&amp;","&");
                    }
                }
            }
        }
        return null;
    }

    private boolean check(String regex,String string){
        final Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        final Matcher matcher = pattern.matcher(string);
        return matcher.find();
    }

    public interface OnTaskCompleted {
        void onTaskCompleted(String vidURL);
        void onFbTaskCompleted(String sd,String hd);
        void onError();
    }

    public void onFinish(OnTaskCompleted onComplete) {
        this.onComplete = onComplete;
    }

    private void destory(){
        webView.destroy();
        webView = null;
    }
}
