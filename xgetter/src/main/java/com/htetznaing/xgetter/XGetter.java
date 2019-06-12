package com.htetznaing.xgetter;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.SparseArray;
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
import com.htetznaing.xgetter.Core.Fruits;
import com.htetznaing.xgetter.Core.GDrive;
import com.htetznaing.xgetter.Core.SolidFiles;
import com.htetznaing.xgetter.Core.Uptostream;
import com.htetznaing.xgetter.Core.Vidoza;
import com.htetznaing.xgetter.Model.XModel;
import com.htetznaing.xgetter.Core.Twitter;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;

/*
 *      xGetter
 *         By
 *   Khun Htetz Naing
 * Repo => https://github.com/KhunHtetzNaing/xGetter
 * Openload,Google GDrive,Google Photos,MediafireStreamango,StreamCherry,Mp4Upload,RapidVideo,SendVid,VidCloud,MegaUp,VK,Ok.Ru,Youtube,Twitter,SolidFils Stream/Download URL Finder!
 *
 */

public class XGetter {
    private WebView webView;
    private Context context;
    private OnTaskCompleted onComplete;
    public static final String agent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.99 Safari/537.36";
    private final String openload = "https?:\\/\\/(www\\.)?(openload|oload)\\.[^\\/,^\\.]{2,}\\/(embed|f)\\/.+";
    private final String fruits = "https?:\\/\\/(www\\.)?(streamango|fruitstreams|streamcherry|fruitadblock|fruithosts)\\.[^\\/,^\\.]{2,}\\/(f|embed)\\/.+";
    private final String megaup = "https?:\\/\\/(www\\.)?(megaup)\\.[^\\/,^\\.]{2,}\\/.+";
    private final String mp4upload = "https?:\\/\\/(www\\.)?(mp4upload)\\.[^\\/,^\\.]{2,}\\/.+";
    private final String sendvid = "https?:\\/\\/(www\\.)?(sendvid)\\.[^\\/,^\\.]{2,}\\/.+";
    private final String vidcloud = "https?:\\/\\/(www\\.)?(vidcloud|vcstream|loadvid)\\.[^\\/,^\\.]{2,}\\/(v|embed)\\/.+";
    private final String rapidvideo = "https?:\\/\\/(www\\.)?rapidvideo\\.[^\\/,^\\.]{2,}\\/(\\?v=[^&\\?]*|e\\/.+|v\\/.+|d\\/.+)";
    private final String gphoto = "https?:\\/\\/(photos.google.com)\\/(u)?\\/?(\\d)?\\/?(share)\\/.+(key=).+";
    private final String mediafire = "https?:\\/\\/(www\\.)?(mediafire)\\.[^\\/,^\\.]{2,}\\/(file)\\/.+";
    private final String okru = "https?:\\/\\/(www.|m.)?(ok)\\.[^\\/,^\\.]{2,}\\/(video|videoembed)\\/.+";
    private final String vk = "https?:\\/\\/(www\\.)?vk\\.[^\\/,^\\.]{2,}\\/video\\-.+";
    private final String twitter = "http(?:s)?:\\/\\/(?:www\\.)?twitter\\.com\\/([a-zA-Z0-9_]+)";
    private final String youtube = "^((?:https?:)?\\/\\/)?((?:www|m)\\.)?((?:youtube\\.com|youtu.be))(\\/(?:[\\w\\-]+\\?v=|embed\\/|v\\/)?)([\\w\\-]+)(\\S+)?$";
    private final String solidfiles = "https?:\\/\\/(www\\.)?(solidfiles)\\.[^\\/,^\\.]{2,}\\/(v)\\/.+";
    private final String vidoza = "https?:\\/\\/(www\\.)?(vidoza)\\.[^\\/,^\\.]{2,}.+";
    private final String uptostream = "https?:\\/\\/(www\\.)?(uptostream|uptobox)\\.[^\\/,^\\.]{2,}.+";

    //  https://uptobox.com/eyrasguzy8lk
    //  https://uptostream.com/eyrasguzy8lk
    //  https://uptostream.com/eyrasguzy8lk
    //  https://uptostream.com/iframe/eyrasguzy8lk

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
                System.out.println(url);
                ArrayList<XModel> xModels = new ArrayList<>();
                putModel(url,"",xModels);
                onComplete.onTaskCompleted(xModels,false);
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
                    ArrayList<XModel> xModels = new ArrayList<>();
                    putModel(url,"",xModels);
                    onComplete.onTaskCompleted(xModels,false);
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
        boolean mfire = false, oload = false,isOkRu = false,isVk=false,isRapidVideo=false,tw=false,gdrive=false,fruit=false,yt=false,solidf=false,isvidoza=false,isuptostream=false;
        if (check(openload, url)) {
            //Openload
            run = true;
            oload = true;
        } else if (check(fruits, url)) {
            //Fruits
            fruit=true;
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
            if (url.contains("/e/") || url.contains("/v/")){
                url = url.replace("/e/","/d/");
            }
        } else if (check(gphoto, url)) {
            //gphotos
            run = true;
        } else if (url.contains("drive.google.com") && get_drive_id(url) != null) {
            //gdrive
            run = true;
            gdrive = true;
            url = get_drive_id(url);
        } else if (check_fb_video(url)) {
            //fb
            run = true;
            fb = true;
        } else if (check(mediafire, url)) {
            //mediafire
            run = true;
            mfire = true;
            if (!url.startsWith("https")){
                url = url.replace("http","https");
            }
        } else if (check(okru,url)){
            run = true;
            isOkRu = true;
            if (!url.startsWith("https")){
                url = url.replace("http","https");
            }

            if (url.contains("m.")){
                url = url.replace("m.","");
            }

            if (url.contains("/video/")){
                url = url.replace("/video/","/videoembed/");
            }

        } else if (check(vk,url)){
            run = true;
            isVk = true;
            if (!url.startsWith("https")){
                url = url.replace("http","https");
            }
        }else if (check(twitter,url)){
            run = true;
            tw = true;
        }else if (check(youtube,url)){
            run = true;
            yt = true;
        }else if (check(solidfiles,url)){
            run = true;
            solidf = true;
        } else if (check(vidoza, url)) {
        //Vidoza
        isvidoza=true;
        run = true;

        }else if (check(uptostream, url)) {
            //uptostream, uptobox
            isuptostream=true;
            run = true;
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
            } else if (isRapidVideo) {
                rapidVideo(url);
            } else if (tw) {
                twitter(url);
            } else if (gdrive) {
                gdrive(url);
            } else if (fruit) {
                fruits(url);
            } else if (yt) {
                youtube(url);
            } else if (solidf){
                solidfiles(url);
            } else if (isvidoza){
                vidozafiles(url);
            } else if (isuptostream){
                uptostreamfiles(url);

            } else {
                webView.loadUrl(url);
            }
        }else onComplete.onError();
    }


    private void solidfiles(final String url){
        new AsyncTask<Void,Void,ArrayList<XModel>>(){
            @Override
            protected ArrayList<XModel> doInBackground(Void... voids) {
                XModel model = SolidFiles.fetch(url);
                if (model!=null){
                    ArrayList<XModel> xModels = new ArrayList<>();
                    xModels.add(model);
                    return xModels;
                }
                return null;
            }

            @Override
            protected void onPostExecute(ArrayList<XModel> xModels) {
                super.onPostExecute(xModels);
                if (xModels!=null){
                    onComplete.onTaskCompleted(xModels,false);
                }else onComplete.onError();
            }
        }.execute();
    }

    private void fruits(final String url){
        new AsyncTask<Void,Void,ArrayList<XModel>>(){

            @Override
            protected ArrayList<XModel> doInBackground(Void... voids) {
                XModel model = Fruits.fetch(url);
                if (model!=null){
                    ArrayList<XModel> xModels = new ArrayList<>();
                    xModels.add(model);
                    return xModels;
                }
                return null;
            }

            @Override
            protected void onPostExecute(ArrayList<XModel> xModels) {
                super.onPostExecute(xModels);
                if (xModels!=null){
                    onComplete.onTaskCompleted(xModels,false);
                }else onComplete.onError();
            }
        }.execute();
    }

    private void gdrive(final String url){
        new AsyncTask<Void,Void,ArrayList<XModel>>(){

            @Override
            protected ArrayList<XModel> doInBackground(Void... voids) {
                return GDrive.fetch(url);
            }

            @Override
            protected void onPostExecute(ArrayList<XModel> xModels) {
                super.onPostExecute(xModels);
                if (xModels!=null) {
                    onComplete.onTaskCompleted(sortMe(xModels), true);
                }else onComplete.onError();
            }
        }.execute();
    }

    private void twitter(final String url){
        new AsyncTask<Void,Void,ArrayList<XModel>>(){

            @Override
            protected ArrayList<XModel> doInBackground(Void... voids) {
                return Twitter.fetch(url);
            }

            @Override
            protected void onPostExecute(ArrayList<XModel> xModels) {
                super.onPostExecute(xModels);
                onComplete.onTaskCompleted(sortMe(xModels),true);
            }
        }.execute();
    }

    private void youtube(String url){
        if (check(youtube,url)) {
            new YouTubeExtractor(context) {
                @Override
                public void onExtractionComplete(SparseArray<YtFile> ytFiles, VideoMeta vMeta) {
                    if (ytFiles != null) {
                        ArrayList<XModel> xModels = new ArrayList<>();

                        for (int i = 0, itag; i < ytFiles.size(); i++) {
                            itag = ytFiles.keyAt(i);
                            YtFile ytFile = ytFiles.get(itag);
                            if (ytFile.getFormat().getExt().equals("mp4") && ytFile.getFormat().getAudioBitrate()!=-1){
                                putModel(ytFile.getUrl(), ytFile.getFormat().getHeight() + "p", xModels);
                            }
                        }

                        onComplete.onTaskCompleted(sortMe(xModels), true);
                    }else {
                        onComplete.onError();
                    }
                }
            }.extract(url, true, false);
        }else onComplete.onError();
    }

    private boolean check_fb_video(String url) {
        return url.matches("-?\\d+(\\.\\d+)?");
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
                    ArrayList<XModel> xModels = new ArrayList<>();
                    putModel(matcher.group(1),"",xModels);
                    onComplete.onTaskCompleted(xModels,false);
                }else onComplete.onError();
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
                    ArrayList<XModel> xModels = new ArrayList<>();
                    if (fb) {
                        putModel(getFbLink(response, false),"SD",xModels);
                        putModel(getFbLink(response, true),"HD",xModels);
                    } else {
                        xModels = getGPhotoLink(response);
                    }
                    onComplete.onTaskCompleted(xModels,true);
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
                        params.put("URLz", "https://www.facebook.com/video.php?v="+data);
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

    private ArrayList<XModel> getGPhotoLink(String string) {
        string = string.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
        try {
            string = URLDecoder.decode(string, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        final String regex = "https:\\/\\/(.*?)=m(22|18|37)";
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(string);
        ArrayList<XModel> xModels = new ArrayList<>();
        boolean p18=false,p22=false,p37=false;
        while (matcher.find()) {
            switch (matcher.group(2)){
                case "18":
                    if (!p18) {
                        putModel(matcher.group(), "360p", xModels);
                        p18=true;
                    }
                    break;
                case "22":
                    if (!p22) {
                        putModel(matcher.group(), "720p", xModels);
                        p22=true;
                    }
                    break;
                case "37":
                    if (!p37) {
                        putModel(matcher.group(), "1080p", xModels);
                        p37=true;
                    }
                    break;
            }
        }
        return xModels;
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
        void onTaskCompleted(ArrayList<XModel> vidURL,boolean multiple_quality);
        void onError();
    }

    public void onFinish(OnTaskCompleted onComplete) {
        this.onComplete = onComplete;
    }

    private void openload(final String url) {
        if (url != null) {
            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    String longString = getLongEncrypt(response);
                    if (longString==null){
                        longString = getLongEncrypt2(response);
                    }
                    String key1 = getKey1(response);
                    String key2 = getKey2(response);
                    String js = "ZnVuY3Rpb24gZ2V0T3BlbmxvYWRVUkwoZW5jcnlwdFN0cmluZywga2V5MSwga2V5MikgewogICAgdmFyIHN0cmVhbVVybCA9ICIiOwogICAgdmFyIGhleEJ5dGVBcnIgPSBbXTsKICAgIGZvciAodmFyIGkgPSAwOyBpIDwgOSAqIDg7IGkgKz0gOCkgewogICAgICAgIGhleEJ5dGVBcnIucHVzaChwYXJzZUludChlbmNyeXB0U3RyaW5nLnN1YnN0cmluZyhpLCBpICsgOCksIDE2KSk7CiAgICB9CiAgICBlbmNyeXB0U3RyaW5nID0gZW5jcnlwdFN0cmluZy5zdWJzdHJpbmcoOSAqIDgpOwogICAgdmFyIGl0ZXJhdG9yID0gMDsKICAgIGZvciAodmFyIGFyckl0ZXJhdG9yID0gMDsgaXRlcmF0b3IgPCBlbmNyeXB0U3RyaW5nLmxlbmd0aDsgYXJySXRlcmF0b3IrKykgewogICAgICAgIHZhciBtYXhIZXggPSA2NDsKICAgICAgICB2YXIgdmFsdWUgPSAwOwogICAgICAgIHZhciBjdXJySGV4ID0gMjU1OwogICAgICAgIGZvciAodmFyIGJ5dGVJdGVyYXRvciA9IDA7IGN1cnJIZXggPj0gbWF4SGV4OyBieXRlSXRlcmF0b3IgKz0gNikgewogICAgICAgICAgICBpZiAoaXRlcmF0b3IgKyAxID49IGVuY3J5cHRTdHJpbmcubGVuZ3RoKSB7CiAgICAgICAgICAgICAgICBtYXhIZXggPSAweDhGOwogICAgICAgICAgICB9CiAgICAgICAgICAgIGN1cnJIZXggPSBwYXJzZUludChlbmNyeXB0U3RyaW5nLnN1YnN0cmluZyhpdGVyYXRvciwgaXRlcmF0b3IgKyAyKSwgMTYpOwogICAgICAgICAgICB2YWx1ZSArPSAoY3VyckhleCAmIDYzKSA8PCBieXRlSXRlcmF0b3I7CiAgICAgICAgICAgIGl0ZXJhdG9yICs9IDI7CiAgICAgICAgfQogICAgICAgIHZhciBieXRlcyA9IHZhbHVlIF4gaGV4Qnl0ZUFyclthcnJJdGVyYXRvciAlIDldIF4ga2V5MSBeIGtleTI7CiAgICAgICAgdmFyIHVzZWRCeXRlcyA9IG1heEhleCAqIDIgKyAxMjc7CiAgICAgICAgZm9yICh2YXIgaSA9IDA7IGkgPCA0OyBpKyspIHsKICAgICAgICAgICAgdmFyIHVybENoYXIgPSBTdHJpbmcuZnJvbUNoYXJDb2RlKCgoYnl0ZXMgJiB1c2VkQnl0ZXMpID4+IDggKiBpKSAtIDEpOwogICAgICAgICAgICBpZiAodXJsQ2hhciAhPSAiJCIpIHsKICAgICAgICAgICAgICAgIHN0cmVhbVVybCArPSB1cmxDaGFyOwogICAgICAgICAgICB9CiAgICAgICAgICAgIHVzZWRCeXRlcyA9IHVzZWRCeXRlcyA8PCA4OwogICAgICAgIH0KICAgIH0KICAgIC8vY29uc29sZS5sb2coc3RyZWFtVXJsKQogICAgcmV0dXJuIHN0cmVhbVVybDsKfQp2YXIgZW5jcnlwdFN0cmluZyA9ICJIdGV0ekxvbmdTdHJpbmciOwp2YXIga2V5TnVtMSA9ICJIdGV0ektleTEiOwp2YXIga2V5TnVtMiA9ICJIdGV0ektleTIiOwp2YXIga2V5UmVzdWx0MSA9IDA7CnZhciBrZXlSZXN1bHQyID0gMDsKdmFyIG9ob3N0ID0gIkh0ZXR6SG9zdCI7Ci8vY29uc29sZS5sb2coZW5jcnlwdFN0cmluZywga2V5TnVtMSwga2V5TnVtMik7CnRyeSB7CiAgICB2YXIga2V5TnVtMV9PY3QgPSBwYXJzZUludChrZXlOdW0xLm1hdGNoKC9wYXJzZUludFwoJyguKiknLDhcKS8pWzFdLCA4KTsKICAgIHZhciBrZXlOdW0xX1N1YiA9IHBhcnNlSW50KGtleU51bTEubWF0Y2goL1wpXC0oW15cK10qKVwrLylbMV0pOwogICAgdmFyIGtleU51bTFfRGl2ID0gcGFyc2VJbnQoa2V5TnVtMS5tYXRjaCgvXC9cKChbXlwtXSopXC0vKVsxXSk7CiAgICB2YXIga2V5TnVtMV9TdWIyID0gcGFyc2VJbnQoa2V5TnVtMS5tYXRjaCgvXCsweDRcLShbXlwpXSopXCkvKVsxXSk7CiAgICBrZXlSZXN1bHQxID0gKGtleU51bTFfT2N0IC0ga2V5TnVtMV9TdWIgKyA0IC0ga2V5TnVtMV9TdWIyKSAvIChrZXlOdW0xX0RpdiAtIDgpOwogICAgdmFyIGtleU51bTJfT2N0ID0gcGFyc2VJbnQoa2V5TnVtMi5tYXRjaCgvXCgnKFteJ10qKScsLylbMV0sIDgpOwogICAgdmFyIGtleU51bTJfU3ViID0gcGFyc2VJbnQoa2V5TnVtMi5zdWJzdHIoa2V5TnVtMi5pbmRleE9mKCIpLSIpICsgMikpOwogICAga2V5UmVzdWx0MiA9IGtleU51bTJfT2N0IC0ga2V5TnVtMl9TdWI7CiAgICBjb25zb2xlLmxvZyhrZXlOdW0xLCBrZXlOdW0yKTsKfSBjYXRjaCAoZSkgewogICAgLy9jb25zb2xlLmVycm9yKGUuc3RhY2spOwogICAgdGhyb3cgRXJyb3IoIktleSBOdW1iZXJzIG5vdCBwYXJzZWQhIik7Cn0KdmFyIHNyYyA9IG9ob3N0ICsgJy9zdHJlYW0vJyArIGdldE9wZW5sb2FkVVJMKGVuY3J5cHRTdHJpbmcsIGtleVJlc3VsdDEsIGtleVJlc3VsdDIpOwp4R2V0dGVyLmZ1Y2soc3JjKTs=";
                    js = base64Decode(js);
                    js = js.replace("HtetzLongString", longString);
                    js = js.replace("HtetzKey1", key1);
                    js = js.replace("HtetzKey2", key2);
                    js = js.replace("HtetzHost",getDomainFromURL(url));
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
                    if (json!=null) {
                        json = StringEscapeUtils.unescapeHtml4(json);
                        try {
                            json = new JSONObject(json).getJSONObject("flashvars").getString("metadata");
                            if (json!=null) {
                                JSONArray jsonArray = new JSONObject(json).getJSONArray("videos");
                                ArrayList<XModel> models = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    String url = jsonArray.getJSONObject(i).getString("url");
                                    String name = jsonArray.getJSONObject(i).getString("name");
                                    if (name.equals("mobile")) {
                                        putModel(url, "144p", models);
                                    } else if (name.equals("lowest")) {
                                        putModel(url, "240p", models);
                                    } else if (name.equals("low")) {
                                        putModel(url, "360p", models);
                                    } else if (name.equals("sd")) {
                                        putModel(url, "480p", models);
                                    } else if (name.equals("hd")) {
                                        putModel(url, "HD", models);
                                    } else if (name.equals("full")) {
                                        putModel(url, "Full HD", models);
                                    } else if (name.equals("quad")) {
                                        putModel(url, "2K", models);
                                    } else if (name.equals("ultra")) {
                                        putModel(url, "4K", models);
                                    } else {
                                        putModel(url, "Default", models);
                                    }
                                }
                                onComplete.onTaskCompleted(sortMe(models), true);
                            }else {
                                onComplete.onError();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            onComplete.onError();
                        }
                    }else onComplete.onError();
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
                    headers.put("User-agent", "Mozilla/5.0 (Linux; Android 4.1.1; Galaxy Nexus Build/JRO03C) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.166 Mobile Safari/535.19");
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
                        ArrayList<XModel> models = new ArrayList<>();
                        String x240="url240",x360="url360",x480="url480",x720="url720",x1080="url1080";
                        JSONObject object = new JSONArray(json).getJSONObject(4).getJSONObject("player").getJSONArray("params").getJSONObject(0);

                        if (object.has(x240)){
                            putModel(object.getString(x240),"240p",models);
                        }

                        if (object.has(x360)){
                            putModel(object.getString(x360),"360p",models);
                        }

                        if (object.has(x480)){
                            putModel(object.getString(x480),"480p",models);
                        }

                        if (object.has(x720)){
                            putModel(object.getString(x720),"720p",models);
                        }

                        if (object.has(x1080)){
                            putModel(object.getString(x1080),"1080p",models);
                        }
                        onComplete.onTaskCompleted(sortMe(models),true);
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
        new AsyncTask<Void,Void,ArrayList<XModel>>(){

            @Override
            protected ArrayList<XModel> doInBackground(Void... voids) {
                ArrayList<XModel> xModels = new ArrayList<>();
                Document document = null;
                try {
                    document = Jsoup.connect(mUrl).userAgent(agent).get();
                    if (document.html().contains("<source")){
                        Elements element = document.getElementsByTag("source");
                        for (int i=0;i<element.size();i++){
                            Element temp = element.get(i);
                            if (temp.hasAttr("src")) {
                                String url = temp.attr("src");
                                putModel(url, temp.attr("label"), xModels);
                            }
                        }
                    }else {
                        Elements element = document.getElementsByTag("a");
                        for (int i=0;i<element.size();i++){
                            if (element.get(i).hasAttr("href")) {
                                String url = element.get(i).attr("href");
                                if (url.contains(".mp4")) {
                                    String quality = element.get(i).text().replace("Download","").replace(" ","");;
                                    putModel(url, quality, xModels);
                                }
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return xModels;
            }

            @Override
            protected void onPostExecute(ArrayList<XModel> s) {
                super.onPostExecute(s);
                if (s!=null && s.size()!=0){

                    onComplete.onTaskCompleted(sortMe(s),true);
                }else onComplete.onError();
            }
        }.execute();
    }

    private void vidozafiles(final String url){
        new AsyncTask<Void,Void,ArrayList<XModel>>(){
            @Override
            protected ArrayList<XModel> doInBackground(Void... voids) {
                XModel model = Vidoza.fetch(url);
                if (model!=null){
                    ArrayList<XModel> xModels = new ArrayList<>();
                    xModels.add(model);
                    return xModels;
                }
                return null;
            }

            @Override
            protected void onPostExecute(ArrayList<XModel> xModels) {
                super.onPostExecute(xModels);
                if (xModels!=null){
                    onComplete.onTaskCompleted(xModels,false);
                }else onComplete.onError();
            }
        }.execute();
    }
    private void uptostreamfiles(final String url) {
        final String urlprepared=prepareUptoStream(url);

        new AsyncTask<Void,Void,ArrayList<XModel>>(){

            @Override
            protected ArrayList<XModel> doInBackground(Void... voids) {
                return Uptostream.fetch(urlprepared);
            }

            @Override
            protected void onPostExecute(ArrayList<XModel> xModels) {
                super.onPostExecute(xModels);
                if (xModels!=null) {
                    onComplete.onTaskCompleted(sortMe(xModels), true);
                }else onComplete.onError();
            }
        }.execute();
    }
    private String prepareUptoStream(String urlUnpreprared) {
        URL u= null;
        try {
            u = new URL(urlUnpreprared);
            String domain=u.getHost();
            String path=u.getPath();
            String[] files= path.split("/");
            String file=files[files.length-1];
            return "https://uptostream.com/iframe/" + file;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

       return urlUnpreprared;

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

    private String getLongEncrypt2(String string) {
        final String regex = "<p style=\"\" id=[^>]*>([^<]*)<\\/p>";
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

    private String getDomainFromURL(String url){
        String regex = "^(?:https?:\\/\\/)?(?:[^@\\n]+@)?(?:www\\.)?([^:\\/\\n?]+)";
        String string = "https://oladblock.me/f/ManbcvAX2_M/";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(string);

        if (matcher.find()) {
            return matcher.group(0);
        }
        return null;
    }

    private void putModel(String url,String quality,ArrayList<XModel> model){
        XModel xModel = new XModel();
        xModel.setUrl(url);
        xModel.setQuality(quality);
        model.add(xModel);
    }


    private ArrayList<XModel> sortMe(ArrayList<XModel> x){
        ArrayList<XModel> result = new ArrayList<>();
        for (XModel t:x){
            if (startWithNumber(t.getQuality())|| t.getQuality().isEmpty()){  // with this modificaction it is included those with quality field is empty. EX. openload
                result.add(t);
            }
        }
        Collections.sort(result,Collections.reverseOrder());
        return result;
    }

    private boolean startWithNumber(String string){
        //final String regex = "^[0-9][A-Za-z0-9-]*$";
        final String regex ="^[0-9][A-Za-z0-9-\\s,]*$"; // start with number and can contain space or comma ( 480p , ENG)
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(string);
        return  matcher.find();
    }
}
