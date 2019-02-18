package com.htetznaing.xgetter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
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
    private final String openload = "https?:\\/\\/(www\\.)?(openload|oload)\\.[^\\/,^\\.]{2,}\\/(embed|f)\\/.+";
    private final String fruits = "https?:\\/\\/(www\\.)?(streamango|fruitstreams|streamcherry|fruitadblock|fruithosts)\\.[^\\/,^\\.]{2,}\\/(f|embed)\\/.+";
    private final String megaup = "https?:\\/\\/(www\\.)?(megaup)\\.[^\\/,^\\.]{2,}\\/.+";
    private final String mp4upload = "https?:\\/\\/(www\\.)?(mp4upload)\\.[^\\/,^\\.]{2,}\\/.+";
    private final String sendvid = "https?:\\/\\/(www\\.)?(sendvid)\\.[^\\/,^\\.]{2,}\\/.+";
    private final String vidcloud = "https?:\\/\\/(www\\.)?(vidcloud|vcstream|loadvid)\\.[^\\/,^\\.]{2,}\\/(v|embed)\\/.+";
    private final String rapidvideo = "https?:\\/\\/(www\\.)?rapidvideo\\.[^\\/,^\\.]{2,}\\/(\\?v=[^&\\?]*|e\\/.+|v\\/.+)";
    private final String gphoto = "https?:\\/\\/(photos.google.com)\\/(u)?\\/?(\\d)?\\/?(share)\\/.+(key=).+";
    private final String fb = "(?:https?://)?(?:www.|web.|m.)?facebook.com/(?:video.php\\?v=\\d+|photo.php\\?v=\\d+|\\?v=\\d+)|\\S+/videos/((\\S+)/(\\d+)|(\\d+))/?";

    public XGetter(Context view) {
        this.context=view;
        init();
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
                    "Zy94R2V0dGVyCgoqLwoKdmFyIG9wZW5sb2FkID0gL2h0dHBzPzpcL1wvKHd3d1wuKT8ob3Blbmxv\n" +
                    "YWR8b2xvYWQpXC5bXlwvLF5cLl17Mix9XC8oZW1iZWR8ZilcLy4rL2ksCiAgICBzdHJlYW0gPSAv\n" +
                    "aHR0cHM/OlwvXC8od3d3XC4pPyhzdHJlYW1hbmdvfGZydWl0c3RyZWFtc3xzdHJlYW1jaGVycnl8\n" +
                    "ZnJ1aXRhZGJsb2NrfGZydWl0aG9zdHMpXC5bXlwvLF5cLl17Mix9XC8oZnxlbWJlZClcLy4rL2ks\n" +
                    "CiAgICBtZWdhdXAgPSAvaHR0cHM/OlwvXC8od3d3XC4pPyhtZWdhdXApXC5bXlwvLF5cLl17Mix9\n" +
                    "XC8uKy9pLAogICAgbXA0dXBsb2FkID0gL2h0dHBzPzpcL1wvKHd3d1wuKT9tcDR1cGxvYWRcLlte\n" +
                    "XC8sXlwuXXsyLH1cL2VtYmVkXC0uKy9pLAogICAgc2VuZHZpZCA9IC9odHRwcz86XC9cLyh3d3dc\n" +
                    "Lik/KHNlbmR2aWQpXC5bXlwvLF5cLl17Mix9XC8uKy9pLAogICAgdmlkY2xvdWQgPSAvaHR0cHM/\n" +
                    "OlwvXC8od3d3XC4pPyh2aWRjbG91ZHx2Y3N0cmVhbXxsb2FkdmlkKVwuW15cLyxeXC5dezIsfVwv\n" +
                    "ZW1iZWRcLyhbYS16QS1aMC05XSopL2ksCiAgICByYXBpZHZpZGVvID0gL2h0dHBzPzpcL1wvKHd3\n" +
                    "d1wuKT9yYXBpZHZpZGVvXC5bXlwvLF5cLl17Mix9XC8oXD92PVteJlw/XSp8ZVwvLit8dlwvLisp\n" +
                    "L2k7CgppZiAob3BlbmxvYWQudGVzdCh3aW5kb3cubG9jYXRpb24uaHJlZikpIHsKICAgIHhHZXR0\n" +
                    "ZXIuZnVjayhkb2N1bWVudC5sb2NhdGlvbi5wcm90b2NvbCArICcvLycgKyBkb2N1bWVudC5sb2Nh\n" +
                    "dGlvbi5ob3N0ICsgJy9zdHJlYW0vJyArIGRvY3VtZW50LmdldEVsZW1lbnRCeUlkKCJEdHNCbGtW\n" +
                    "RlF4IikudGV4dENvbnRlbnQgKyAnP21pbWU9dHJ1ZScpOwp9IGVsc2UgaWYgKHN0cmVhbS50ZXN0\n" +
                    "KHdpbmRvdy5sb2NhdGlvbi5ocmVmKSkgewogICAgeEdldHRlci5mdWNrKHdpbmRvdy5sb2NhdGlv\n" +
                    "bi5wcm90b2NvbCArIHNyY2VzWzBdWyJzcmMiXSk7Cn0gZWxzZSBpZiAobWVnYXVwLnRlc3Qod2lu\n" +
                    "ZG93LmxvY2F0aW9uLmhyZWYpKSB7CiAgICBzZWNvbmRzID0gMDsKICAgIGRpc3BsYXkoKTsKICAg\n" +
                    "IHdpbmRvdy5sb2NhdGlvbi5yZXBsYWNlKGRvY3VtZW50LmdldEVsZW1lbnRzQnlDbGFzc05hbWUo\n" +
                    "ImJ0biBidG4tZGVmYXVsdCIpLml0ZW0oMCkuaHJlZik7Cn0gZWxzZSBpZiAobXA0dXBsb2FkLnRl\n" +
                    "c3Qod2luZG93LmxvY2F0aW9uLmhyZWYpKSB7CiAgICB4R2V0dGVyLmZ1Y2soZG9jdW1lbnQuZ2V0\n" +
                    "RWxlbWVudHNCeUNsYXNzTmFtZSgnanctdmlkZW8ganctcmVzZXQnKS5pdGVtKDApLnNyYyk7Cn0g\n" +
                    "ZWxzZSBpZiAocmFwaWR2aWRlby50ZXN0KHdpbmRvdy5sb2NhdGlvbi5ocmVmKSkgewogICAgeEdl\n" +
                    "dHRlci5mdWNrKGRvY3VtZW50LmdldEVsZW1lbnRzQnlUYWdOYW1lKCdzb3VyY2UnKS5pdGVtKDAp\n" +
                    "LnNyYyk7Cn0gZWxzZSBpZiAoc2VuZHZpZC50ZXN0KHdpbmRvdy5sb2NhdGlvbi5ocmVmKSkgewog\n" +
                    "ICAgeEdldHRlci5mdWNrKGRvY3VtZW50LmdldEVsZW1lbnRzQnlUYWdOYW1lKCdzb3VyY2UnKS5p\n" +
                    "dGVtKDApLnNyYyk7Cn0gZWxzZSBpZiAodmlkY2xvdWQudGVzdCh3aW5kb3cubG9jYXRpb24uaHJl\n" +
                    "ZikpIHsKICAgICQuYWpheCh7CiAgICAgICAgdXJsOiAnL2Rvd25sb2FkJywKICAgICAgICBtZXRo\n" +
                    "b2Q6ICdQT1NUJywKICAgICAgICBkYXRhOiB7CiAgICAgICAgICAgIGZpbGVfaWQ6IGZpbGVJRAog\n" +
                    "ICAgICAgIH0sCiAgICAgICAgZGF0YVR5cGU6ICdqc29uJywKICAgICAgICBzdWNjZXNzOiBmdW5j\n" +
                    "dGlvbihyZXMpIHsKICAgICAgICAgICAgJCgnLnF1YWxpdHktbWVudScpLmh0bWwocmVzLmh0bWwp\n" +
                    "OwogICAgICAgICAgICB2YXIgZGF0YSA9IHJlcy5odG1sOwogICAgICAgICAgICB2YXIgcmVnZXgg\n" +
                    "PSAvaHJlZj0iKC4qPykiLzsKICAgICAgICAgICAgdmFyIG07CiAgICAgICAgICAgIGlmICgobSA9\n" +
                    "IHJlZ2V4LmV4ZWMoZGF0YSkpICE9PSBudWxsKSB7CiAgICAgICAgICAgICAgICB4R2V0dGVyLmZ1\n" +
                    "Y2sobVsxXSk7CiAgICAgICAgICAgIH0KICAgICAgICB9CiAgICB9KTsKfSBlbHNlIGlmKHdpbmRv\n" +
                    "dy5sb2NhdGlvbi5ob3N0ID09ICdkcml2ZS5nb29nbGUuY29tJyl7Cglkb2N1bWVudC5nZXRFbGVt\n" +
                    "ZW50QnlJZCgndWMtZG93bmxvYWQtbGluaycpLmNsaWNrKCk7Cn0KCi8qClN1cHBvcnRlZCBTaXRl\n" +
                    "cwo9PiBPcGVubG9hZCAoQWxsIGRvbWFpbnMpCj0+IEZydWl0U3RyZWFtcyAoU3RyZWFtY2hlcnJ5\n" +
                    "LFN0cmVhbWFuZ28gYW5kIGV0Yy4uKQo9PiBNcDRVcGxvYWQKPT4gUmFwaWRWaWRlbwo9PiBTZW5k\n" +
                    "VmlkCj0+IE1lZ2FVcAo9PiBWaWRDbG91ZCAoQWxsIGRvbWFpbnMpCiov";
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
        boolean fb = false;
        boolean run = false;
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
        }else if (check_fb_video(url)!=null){
            //fb
            run = true;
            fb = true;
            url = "https://www.facebook.com/video.php?v="+check_fb_video(url);
        }

        if (run) {
            if (check(gphoto,url)){
                gphotoORfb(url,false);
            }else if (fb){
                gphotoORfb(url,true);
            }else {
                webView.loadUrl(url);
            }
        }else onComplete.onError();
    }

    private String check_fb_video(String url){
        final Pattern pattern = Pattern.compile(fb);
        final Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            String s = matcher.group(1);
            if (s!=null) {
                if (s.contains("v=")) {
                    s = s.substring(s.indexOf("v=") + 2);
                } else if (s.contains("vb.")) {
                    for (int i = 1; i <= matcher.groupCount(); i++) {
                        String temp = matcher.group(i);
                        if (isNumber(temp)) {
                            s = temp;
                        }
                    }
                }
                return s;
            }
        }
        return null;
    }

    private boolean isNumber(String str){
        return str.replaceAll("[0-9]","").length() == 0;
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

    private void gphotoORfb(String url, final boolean fb){
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (fb){
                    onComplete.onFbTaskCompleted(getFbLink(response,false),getFbLink(response,true));
                }else {
                    onComplete.onTaskCompleted(getGPhotoLink(response));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onComplete.onError();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("User-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36");
                return headers;
            }
        };

        Volley.newRequestQueue(context).add(request);
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

    private String getFbLink(String source,boolean hdquality){
        String s=source;
        String q=(hdquality?"hd_src:\"":"sd_src:\"");
        int idx=s.indexOf(q);
        if(idx!=-1){
            s=s.substring(idx+q.length());
            return s.substring(0,s.indexOf("\""));
        }else{
            return null;
        }
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
