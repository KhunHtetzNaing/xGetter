package com.htetznaing.xgetter;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.webkit.ConsoleMessage;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
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
 * Openload,Google Drive,Google Photos,MediafireStreamango,StreamCherry,Mp4Upload,RapidVideo,SendVid,VidCloud,MegaUp,VK,Ok.Ru Stream/Download URL Finder!
 *
 */

public class XGetter {
    private WebView webView;
    private Context context;
    private OnTaskCompleted onComplete;
    private final String agent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.99 Safari/537.36";
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
    private final String okru = "https?:\\/\\/(www\\.)?(ok)\\.[^\\/,^\\.]{2,}\\/(video|videoembed)\\/.+";
    private final String vk = "https?:\\/\\/(www\\.)?vk\\.[^\\/,^\\.]{2,}\\/video\\-.+";

    public XGetter(Context view) {
        this.context = view;
    }

    private void init() {
        webView = new WebView(context);
        webView.setWillNotDraw(true);
        webView.addJavascriptInterface(new xJavascriptInterface(), "xGetter");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);

        webView.setWebViewClient(new WebViewClient() {

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
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
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
                    if (url.startsWith("okru")){
                        String json = url;
                        json = json.replace("okru","");
                        try {
                            JSONArray jsonArray = new JSONArray(json);
                            OkRuLinks okRuLinks = new OkRuLinks();
                            for (int i=0;i<jsonArray.length();i++){
                                String url = jsonArray.getJSONObject(i).getString("url");
                                String name = jsonArray.getJSONObject(i).getString("name");
                                if (name.equals("mobile")) {
                                    okRuLinks.setMobile144px(url);
                                } else if (name.equals("lowest")) {
                                    okRuLinks.setLowest240px(url);
                                } else if (name.equals("low")) {
                                    okRuLinks.setLow360px(url);
                                } else if (name.equals("sd")) {
                                    okRuLinks.setSd480px(url);
                                } else if (name.equals("hd")) {
                                    okRuLinks.setHD(url);
                                } else if (name.equals("full")) {
                                    okRuLinks.setFullHD(url);
                                } else if (name.equals("quad")) {
                                    okRuLinks.setQuad2K(url);
                                } else if (name.equals("ultra")) {
                                    okRuLinks.setUltra4K(url);
                                } else {
                                    okRuLinks.setUrl(url);
                                }
                            }
                            onComplete.onOkRuTaskCompleted(okRuLinks);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            onComplete.onError();
                        }
                    }else onComplete.onTaskCompleted(url);
                }
            });
        }
    }

    private void letFuck(WebView view) {
        String encoded = "LyoKICAgICAgICB4R2V0dGVyCiAgICAgICAgICBCeQogICAgS2h1biBIdGV0eiBOYWluZyBbZmIu\n" +
                "Y29tL0tIdGV0ek5haW5nXQpSZXBvID0+IGh0dHBzOi8vZ2l0aHViLmNvbS9LaHVuSHRldHpOYWlu\n" +
                "Zy94R2V0dGVyCgoqLwp2YXIgc3RyZWFtID0gL2h0dHBzPzpcL1wvKHd3d1wuKT8oc3RyZWFtYW5n\n" +
                "b3xmcnVpdHN0cmVhbXN8c3RyZWFtY2hlcnJ5fGZydWl0YWRibG9ja3xmcnVpdGhvc3RzKVwuW15c\n" +
                "LyxeXC5dezIsfVwvKGZ8ZW1iZWQpXC8uKy9pLAogICAgbWVnYXVwID0gL2h0dHBzPzpcL1wvKHd3\n" +
                "d1wuKT8obWVnYXVwKVwuW15cLyxeXC5dezIsfVwvLisvaSwKICAgIG1wNHVwbG9hZCA9IC9odHRw\n" +
                "cz86XC9cLyh3d3dcLik/bXA0dXBsb2FkXC5bXlwvLF5cLl17Mix9XC9lbWJlZFwtLisvaSwKICAg\n" +
                "IHNlbmR2aWQgPSAvaHR0cHM/OlwvXC8od3d3XC4pPyhzZW5kdmlkKVwuW15cLyxeXC5dezIsfVwv\n" +
                "LisvaSwKICAgIHZpZGNsb3VkID0gL2h0dHBzPzpcL1wvKHd3d1wuKT8odmlkY2xvdWR8dmNzdHJl\n" +
                "YW18bG9hZHZpZClcLlteXC8sXlwuXXsyLH1cL2VtYmVkXC8oW2EtekEtWjAtOV0qKS9pLAogICAg\n" +
                "cmFwaWR2aWRlbyA9IC9odHRwcz86XC9cLyh3d3dcLik/cmFwaWR2aWRlb1wuW15cLyxeXC5dezIs\n" +
                "fVwvKFw/dj1bXiZcP10qfGVcLy4rfHZcLy4rKS9pLAogICAgb2tydSA9IC9odHRwcz86XC9cLyh3\n" +
                "d3dcLik/KG9rKVwuW15cLyxeXC5dezIsfVwvKHZpZGVvKVwvLisvaTsKaWYgKHN0cmVhbS50ZXN0\n" +
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
                "Y2sobVsxXSk7CiAgICAgICAgICAgIH0KICAgICAgICB9CiAgICB9KTsKfSBlbHNlIGlmICh3aW5k\n" +
                "b3cubG9jYXRpb24uaG9zdCA9PSAnZHJpdmUuZ29vZ2xlLmNvbScpIHsKICAgIGRvY3VtZW50Lmdl\n" +
                "dEVsZW1lbnRCeUlkKCd1Yy1kb3dubG9hZC1saW5rJykuY2xpY2soKTsKfSBlbHNlIGlmIChva3J1\n" +
                "LnRlc3Qod2luZG93LmxvY2F0aW9uLmhyZWYpKSB7CiAgICB2YXIgdmlkZW8gPSBkb2N1bWVudC5n\n" +
                "ZXRFbGVtZW50c0J5Q2xhc3NOYW1lKCJ2cF92aWRlbyIpWzBdOwogICAgdmFyIHZtID0gdmlkZW8u\n" +
                "Z2V0RWxlbWVudHNCeUNsYXNzTmFtZSgidmlkLWNhcmRfY250IGgtbW9kIilbMF07CiAgICB2YXIg\n" +
                "b3B0aW9ucyA9IHZtLmdldEF0dHJpYnV0ZSgnZGF0YS1vcHRpb25zJyk7CgogICAgdmFyIGRhdGEg\n" +
                "PSBKU09OLnBhcnNlKG9wdGlvbnMpOwogICAgZGF0YSA9IEpTT04ucGFyc2UoZGF0YS5mbGFzaHZh\n" +
                "cnMubWV0YWRhdGEpOwogICAgdmFyIHZpZGVvcyA9IGRhdGEudmlkZW9zOwogICAgdmFyIG91dHB1\n" +
                "dCA9IFtdOwogICAgZm9yICh2YXIgaSA9IDA7IGkgPCB2aWRlb3MubGVuZ3RoOyBpKyspIHsKICAg\n" +
                "ICAgICB2YXIgdSA9IHZpZGVvc1tpXS51cmwucmVwbGFjZSgvY3Q9MC9naSwgImN0PTQiKSArICcm\n" +
                "Ynl0ZXM9MC0xMDAwMDAwMDAnOwogICAgICAgIHZhciBxID0gdmlkZW9zW2ldLm5hbWU7CiAgICAg\n" +
                "ICAgdmFyIG9iaiA9IHsKICAgICAgICAgICAgJ25hbWUnOiBxLAogICAgICAgICAgICAndXJsJzog\n" +
                "dQogICAgICAgIH0KICAgICAgICBvdXRwdXQucHVzaChvYmopOwogICAgfQogICAgeEdldHRlci5m\n" +
                "dWNrKCdva3J1JyArIEpTT04uc3RyaW5naWZ5KG91dHB1dCkpOwp9Ci8qClN1cHBvcnRlZCBTaXRl\n" +
                "cwo9PiBPcGVubG9hZCAoQWxsIGRvbWFpbnMpCj0+IEZydWl0U3RyZWFtcyAoU3RyZWFtY2hlcnJ5\n" +
                "LFN0cmVhbWFuZ28gYW5kIGV0Yy4uKQo9PiBNcDRVcGxvYWQKPT4gUmFwaWRWaWRlbwo9PiBTZW5k\n" +
                "VmlkCj0+IE1lZ2FVcAo9PiBWaWRDbG91ZCAoQWxsIGRvbWFpbnMpCj0+IE1lZGlhZmlyZQo9PiBH\n" +
                "b29nbGUgUGhvdG9zCj0+IEdvb2dsZSBEcml2ZQo9PiBPay5SdQoqLw==";
        view.loadUrl("javascript:(function() {" +
                "var parent = document.getElementsByTagName('head').item(0);" +
                "var script = document.createElement('script');" +
                "script.type = 'text/javascript';" +
                // Tell the browser to BASE64-decode the string into your script !!!
                "script.innerHTML = window.atob('" + encoded + "');" +
                "parent.appendChild(script)" +
                "})()");
    }

    public void find(String url) {
        init();
        boolean fb = false;
        boolean run = false;
        boolean mfire = false, oload = false,isOkRu = false,isVk=false,isRapidVideo=false;
        if (check(openload, url)) {
            //Openload
            run = true;
            oload = true;
        } else if (check(fruits, url)) {
            //Fruits
            run = true;
        } else if (check(megaup, url)) {
            //megaup
            run = true;
        } else if (check(mp4upload, url)) {
            run = true;
            if (!url.contains("embed-")) {
                final String regex = "com\\/([^']*)";
                final Pattern pattern = Pattern.compile(regex);
                final Matcher matcher = pattern.matcher(url);
                if (matcher.find()) {
                    String id = matcher.group(1);
                    if (id.contains("/")) {
                        id = id.substring(0, id.lastIndexOf("/"));
                    }
                    url = "https://www.mp4upload.com/embed-" + id + ".html";
                } else {
                    run = false;
                }
            }

        } else if (check(sendvid, url)) {
            //sendvid
            run = true;
        } else if (check(vidcloud, url)) {
            //vidcloud
            run = true;
            if (!url.contains("/embed/") && url.contains("/v/")) {
                url = url.replace("/v/", "/embed/");
            }
        } else if (check(rapidvideo, url)) {
            //rapidvideo
            run = true;
            isRapidVideo=true;
            if (url.contains("/e/")){
                url = url.replace("/e/","/v/");
            }
        } else if (check(gphoto, url)) {
            //gphotos
            run = true;
        } else if (url.contains("drive.google.com") && get_drive_id(url) != null) {
            //gdrive
            run = true;
            url = "https://drive.google.com/uc?id=" + get_drive_id(url) + "&export=download";
        } else if (check_fb_video(url)) {
            //fb
            run = true;
            fb = true;
        } else if (check(mediafire, url)) {
            //mediafire
            run = true;
            mfire = true;
        } else if (check(okru,url)){
            run = true;
            isOkRu = true;
            if (!url.startsWith("https")){
                url = url.replace("http","https");
            }

            if (url.contains("m.")){
                url = url.replace("m.","");
            }

            if (url.contains("/videoembed/")){
                url = url.replace("/videoembed/","/video/");
            }

        } else if (check(vk,url)){
            run = true;
            isVk = true;
            if (!url.startsWith("https")){
                url = url.replace("http","https");
            }
        }

        if (run) {
            if (check(gphoto, url)) {
                gphotoORfb(url, false);
            } else if (fb) {
                gphotoORfb(url, true);
            } else if (mfire) {
                mfire(url);
            } else if (oload) {
                openload(url);
            } else if (isOkRu){
                okru(url);
            } else if (isVk) {
                vk(url);
            } else if (isRapidVideo){
                rapidVideo(url);
            } else {
                webView.loadUrl(url);
            }
        }else onComplete.onError();
    }

    private boolean check_fb_video(String url) {
        final Pattern pattern = Pattern.compile(fb);
        final Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            return true;
        }
        return false;
    }

    private String get_drive_id(String string) {
        final String regex = "[-\\w]{25,}";
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(string);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    private void mfire(String url) {
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

    private void gphotoORfb(String url, final boolean fb) {
        final String data = url;
        if (url != null) {
            int method = Request.Method.GET;

            if (fb) {
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
        } else onComplete.onError();
    }

    private String getGPhotoLink(String string) {
        string = string.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
        try {
            string = URLDecoder.decode(string, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        final String regex = "https:\\/\\/(.*?)=m(22|18|37)";
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(string);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    private String getFbLink(String source, boolean hd) {
        if (source != null) {
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
                        return matcher.group(1).replace("&amp;", "&");
                    }
                }
            }
        }
        return null;
    }

    private boolean check(String regex, String string) {
        final Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        final Matcher matcher = pattern.matcher(string);
        return matcher.find();
    }

    public interface OnTaskCompleted {
        void onTaskCompleted(String vidURL);

        void onFbTaskCompleted(String sd, String hd);

        void onOkRuTaskCompleted(OkRuLinks okRuLinks);

        void onVkTaskComplete(VkLinks vkLinks);

        void onError();
    }

    public void onFinish(OnTaskCompleted onComplete) {
        this.onComplete = onComplete;
    }



    private void openload(String url) {
        init();
        if (url != null) {
            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    String longString = getLongEncrypt(response);
                    String key1 = getKey1(response);
                    String key2 = getKey2(response);
                    String js = "ZnVuY3Rpb24gZ2V0T3BlbmxvYWRVUkwoZW5jcnlwdFN0cmluZywga2V5MSwga2V5MikgewogICAg\n" +
                            "dmFyIHN0cmVhbVVybCA9ICIiOwogICAgdmFyIGhleEJ5dGVBcnIgPSBbXTsKICAgIGZvciAodmFy\n" +
                            "IGkgPSAwOyBpIDwgOSAqIDg7IGkgKz0gOCkgewogICAgICAgIGhleEJ5dGVBcnIucHVzaChwYXJz\n" +
                            "ZUludChlbmNyeXB0U3RyaW5nLnN1YnN0cmluZyhpLCBpICsgOCksIDE2KSk7CiAgICB9CiAgICBl\n" +
                            "bmNyeXB0U3RyaW5nID0gZW5jcnlwdFN0cmluZy5zdWJzdHJpbmcoOSAqIDgpOwogICAgdmFyIGl0\n" +
                            "ZXJhdG9yID0gMDsKICAgIGZvciAodmFyIGFyckl0ZXJhdG9yID0gMDsgaXRlcmF0b3IgPCBlbmNy\n" +
                            "eXB0U3RyaW5nLmxlbmd0aDsgYXJySXRlcmF0b3IrKykgewogICAgICAgIHZhciBtYXhIZXggPSA2\n" +
                            "NDsKICAgICAgICB2YXIgdmFsdWUgPSAwOwogICAgICAgIHZhciBjdXJySGV4ID0gMjU1OwogICAg\n" +
                            "ICAgIGZvciAodmFyIGJ5dGVJdGVyYXRvciA9IDA7IGN1cnJIZXggPj0gbWF4SGV4OyBieXRlSXRl\n" +
                            "cmF0b3IgKz0gNikgewogICAgICAgICAgICBpZiAoaXRlcmF0b3IgKyAxID49IGVuY3J5cHRTdHJp\n" +
                            "bmcubGVuZ3RoKSB7CiAgICAgICAgICAgICAgICBtYXhIZXggPSAweDhGOwogICAgICAgICAgICB9\n" +
                            "CiAgICAgICAgICAgIGN1cnJIZXggPSBwYXJzZUludChlbmNyeXB0U3RyaW5nLnN1YnN0cmluZyhp\n" +
                            "dGVyYXRvciwgaXRlcmF0b3IgKyAyKSwgMTYpOwogICAgICAgICAgICB2YWx1ZSArPSAoY3Vyckhl\n" +
                            "eCAmIDYzKSA8PCBieXRlSXRlcmF0b3I7CiAgICAgICAgICAgIGl0ZXJhdG9yICs9IDI7CiAgICAg\n" +
                            "ICAgfQogICAgICAgIHZhciBieXRlcyA9IHZhbHVlIF4gaGV4Qnl0ZUFyclthcnJJdGVyYXRvciAl\n" +
                            "IDldIF4ga2V5MSBeIGtleTI7CiAgICAgICAgdmFyIHVzZWRCeXRlcyA9IG1heEhleCAqIDIgKyAx\n" +
                            "Mjc7CiAgICAgICAgZm9yICh2YXIgaSA9IDA7IGkgPCA0OyBpKyspIHsKICAgICAgICAgICAgdmFy\n" +
                            "IHVybENoYXIgPSBTdHJpbmcuZnJvbUNoYXJDb2RlKCgoYnl0ZXMgJiB1c2VkQnl0ZXMpID4+IDgg\n" +
                            "KiBpKSAtIDEpOwogICAgICAgICAgICBpZiAodXJsQ2hhciAhPSAiJCIpIHsKICAgICAgICAgICAg\n" +
                            "ICAgIHN0cmVhbVVybCArPSB1cmxDaGFyOwogICAgICAgICAgICB9CiAgICAgICAgICAgIHVzZWRC\n" +
                            "eXRlcyA9IHVzZWRCeXRlcyA8PCA4OwogICAgICAgIH0KICAgIH0KICAgIC8vY29uc29sZS5sb2co\n" +
                            "c3RyZWFtVXJsKQogICAgcmV0dXJuIHN0cmVhbVVybDsKfQp2YXIgZW5jcnlwdFN0cmluZyA9ICJI\n" +
                            "dGV0ekxvbmdTdHJpbmciOwp2YXIga2V5TnVtMSA9ICJIdGV0ektleTEiOwp2YXIga2V5TnVtMiA9\n" +
                            "ICJIdGV0ektleTIiCnZhciBrZXlSZXN1bHQxID0gMDsKdmFyIGtleVJlc3VsdDIgPSAwOwovL2Nv\n" +
                            "bnNvbGUubG9nKGVuY3J5cHRTdHJpbmcsIGtleU51bTEsIGtleU51bTIpOwp0cnkgewogICAgdmFy\n" +
                            "IGtleU51bTFfT2N0ID0gcGFyc2VJbnQoa2V5TnVtMS5tYXRjaCgvcGFyc2VJbnRcKCcoLiopJyw4\n" +
                            "XCkvKVsxXSwgOCk7CiAgICB2YXIga2V5TnVtMV9TdWIgPSBwYXJzZUludChrZXlOdW0xLm1hdGNo\n" +
                            "KC9cKVwtKFteXCtdKilcKy8pWzFdKTsKICAgIHZhciBrZXlOdW0xX0RpdiA9IHBhcnNlSW50KGtl\n" +
                            "eU51bTEubWF0Y2goL1wvXCgoW15cLV0qKVwtLylbMV0pOwogICAgdmFyIGtleU51bTFfU3ViMiA9\n" +
                            "IHBhcnNlSW50KGtleU51bTEubWF0Y2goL1wrMHg0XC0oW15cKV0qKVwpLylbMV0pOwogICAga2V5\n" +
                            "UmVzdWx0MSA9IChrZXlOdW0xX09jdCAtIGtleU51bTFfU3ViICsgNCAtIGtleU51bTFfU3ViMikg\n" +
                            "LyAoa2V5TnVtMV9EaXYgLSA4KTsKICAgIHZhciBrZXlOdW0yX09jdCA9IHBhcnNlSW50KGtleU51\n" +
                            "bTIubWF0Y2goL1woJyhbXiddKiknLC8pWzFdLCA4KTsKICAgIHZhciBrZXlOdW0yX1N1YiA9IHBh\n" +
                            "cnNlSW50KGtleU51bTIuc3Vic3RyKGtleU51bTIuaW5kZXhPZigiKS0iKSArIDIpKTsKICAgIGtl\n" +
                            "eVJlc3VsdDIgPSBrZXlOdW0yX09jdCAtIGtleU51bTJfU3ViOwogICAgY29uc29sZS5sb2coa2V5\n" +
                            "TnVtMSwga2V5TnVtMik7Cn0gY2F0Y2ggKGUpIHsKICAgIC8vY29uc29sZS5lcnJvcihlLnN0YWNr\n" +
                            "KTsKICAgIHRocm93IEVycm9yKCJLZXkgTnVtYmVycyBub3QgcGFyc2VkISIpOwp9CnZhciBzcmMg\n" +
                            "PSAnaHR0cHM6Ly9vcGVubG9hZC5jby9zdHJlYW0vJytnZXRPcGVubG9hZFVSTChlbmNyeXB0U3Ry\n" +
                            "aW5nLCBrZXlSZXN1bHQxLCBrZXlSZXN1bHQyKTsKeEdldHRlci5mdWNrKHNyYyk7";
                    js = base64Decode(js);
                    js = js.replace("HtetzLongString", longString);
                    js = js.replace("HtetzKey1", key1);
                    js = js.replace("HtetzKey2", key2);
                    js = base64Encode(js);
                    webView.loadUrl("javascript:(function() {" +
                            "var parent = document.getElementsByTagName('head').item(0);" +
                            "var script = document.createElement('script');" +
                            "script.type = 'text/javascript';" +
                            // Tell the browser to BASE64-decode the string into your script !!!
                            "script.innerHTML = window.atob('" + js + "');" +
                            "parent.appendChild(script)" +
                            "})()");
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
    }

    private void okru(String url) {
        if (url != null) {
            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                private String getJson(String html){
                    final String regex = "data-options=\"(.*?)\"";
                    final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
                    final Matcher matcher = pattern.matcher(html);
                    if (matcher.find()) {
                        return matcher.group(1);
                    }
                    return null;
                }

                @Override
                public void onResponse(String response) {
                    String json = getJson(response);
                    json = StringEscapeUtils.unescapeHtml4(json);
                    try {
                        json = new JSONObject(json).getJSONObject("flashvars").getString("metadata");
                        JSONArray jsonArray = new JSONObject(json).getJSONArray("videos");
                        OkRuLinks okRuLinks = new OkRuLinks();
                        for (int i=0;i<jsonArray.length();i++){
                            String url = jsonArray.getJSONObject(i).getString("url").replace("ct=0", "ct=4");
                            String name = jsonArray.getJSONObject(i).getString("name");
                            if (name.equals("mobile")) {
                                okRuLinks.setMobile144px(url);
                            } else if (name.equals("lowest")) {
                                okRuLinks.setLowest240px(url);
                            } else if (name.equals("low")) {
                                okRuLinks.setLow360px(url);
                            } else if (name.equals("sd")) {
                                okRuLinks.setSd480px(url);
                            } else if (name.equals("hd")) {
                                okRuLinks.setHD(url);
                            } else if (name.equals("full")) {
                                okRuLinks.setFullHD(url);
                            } else if (name.equals("quad")) {
                                okRuLinks.setQuad2K(url);
                            } else if (name.equals("ultra")) {
                                okRuLinks.setUltra4K(url);
                            } else {
                                okRuLinks.setUrl(url);
                            }
                        }
                        onComplete.onOkRuTaskCompleted(okRuLinks);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        onComplete.onError();
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
    }

    private void vk(String url) {
        if (url != null) {
            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

                private String get(String regex,String html){
                    final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
                    final Matcher matcher = pattern.matcher(html);
                    if (matcher.find()) {
                        return matcher.group(1);
                    }
                    return null;
                }

                @Override
                public void onResponse(String response) {
                    String json = get("al_video.php', ?(\\{.*])",response);
                    json = get("\\}, ?(.*)",json);

                    try {
                        VkLinks vkLinks = new VkLinks();
                        String x240="url240",x360="url360",x480="url480",x720="url720",x1080="url1080";
                        JSONObject object = new JSONArray(json).getJSONObject(4).getJSONObject("player").getJSONArray("params").getJSONObject(0);
                        if (object.has(x240)){
                            vkLinks.setUrl240(object.getString(x240));
                        }

                        if (object.has(x360)){
                            vkLinks.setUrl360(object.getString(x360));
                        }

                        if (object.has(x480)){
                            vkLinks.setUrl480(object.getString(x480));
                        }

                        if (object.has(x720)){
                            vkLinks.setUrl720(object.getString(x720));
                        }

                        if (object.has(x1080)){
                            vkLinks.setUrl1080(object.getString(x1080));
                        }

                        onComplete.onVkTaskComplete(vkLinks);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        onComplete.onError();
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
    }

    private void rapidVideo(final String mUrl){
        new AsyncTask<Void,Void,String>(){

            @Override
            protected String doInBackground(Void... voids) {
                URL url;
                InputStream is = null;
                BufferedReader br;
                String line;

                try {
                    url = new URL(mUrl);
                    is = url.openStream();  // throws an IOException
                    br = new BufferedReader(new InputStreamReader(is));
                    String result = null;
                    while ((line = br.readLine()) != null) {
                        result +=line;
                    }

                    if (result!=null){
                        final String regex = "<source src=\"(.*?)\"";
                        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
                        final Matcher matcher = pattern.matcher(result);
                        if (matcher.find()) {
                            return matcher.group(1);
                        }
                    }

                } catch (MalformedURLException mue) {
                    mue.printStackTrace();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                } finally {
                    try {
                        if (is != null) is.close();
                    } catch (IOException ioe) {
                        // nothing to see here
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s!=null){
                    onComplete.onTaskCompleted(s);
                }else onComplete.onError();
            }
        }.execute();
    }

    private String getLongEncrypt(String string) {
        final String regex = "<p id=[^>]*>([^<]*)<\\/p>";
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(string);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private String getKey1(String string) {
        final String regex = "\\_0x45ae41\\[\\_0x5949\\('0xf'\\)\\]\\(_0x30725e,(.*)\\),\\_1x4bfb36";
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(string);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private String getKey2(String string) {
        final String regex = "\\_1x4bfb36=(.*);";
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(string);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }


    private String base64Encode(String text) {
        byte[] data = new byte[0];
        try {
            data = text.getBytes("UTF-8");
            return Base64.encodeToString(data, Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String base64Decode(String text) {
        byte[] data = Base64.decode(text, Base64.DEFAULT);
        try {
            return new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
