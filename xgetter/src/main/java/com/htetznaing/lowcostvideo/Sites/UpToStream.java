package com.htetznaing.lowcostvideo.Sites;

import android.content.Context;
import android.webkit.CookieManager;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.htetznaing.lowcostvideo.LowCostVideo;
import com.htetznaing.lowcostvideo.Model.XModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

import static com.htetznaing.lowcostvideo.LowCostVideo.agent;
import static com.htetznaing.lowcostvideo.Utils.Utils.putModel;
import static com.htetznaing.lowcostvideo.Utils.Utils.sortMe;

/*
This is direct link getter for UpToStream,UpToBox
    By
Khun Htetz Naing
 */

public class UpToStream {
    private static String COOKIE = null;
    public static void fetch(final Context context, final String url, final LowCostVideo.OnTaskCompleted onComplete){
        CookieJar cookieJar = new CookieJar() {
            private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                COOKIE = cookies.toString();
                cookieStore.put(url.host(), cookies);
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                List<Cookie> cookies = cookieStore.get(url.host());
                return cookies != null ? cookies : new ArrayList<Cookie>();
            }
        };

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .build();

        AndroidNetworking.get("https://uptostream.com/api/streaming/source/get?token=&file_code="+getUpToStreamID(url))
                .setOkHttpClient(okHttpClient)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            response = new JSONObject(response).getJSONObject("data").getString("sources");
                            new com.htetznaing.lowcostvideo.Core.UpToStream().get(context, response, new com.htetznaing.lowcostvideo.Core.UpToStream.OnDone() {
                                @Override
                                public void result(String result) {
                                    if (result!=null){
                                        try {
                                            JSONArray array = new JSONArray(result);
                                            ArrayList<XModel> xModels = new ArrayList<>();
                                            for (int i=0;i<array.length();i++){
                                                String src = array.getJSONObject(i).getString("src");
                                                String label = array.getJSONObject(i).getString("label");
                                                String lang = array.getJSONObject(i).getString("lang");
                                                if (lang!=null && !lang.isEmpty()){
                                                    lang = lang.toUpperCase();
                                                }
                                                String quality=label+","+ lang;
                                                putModel(src,quality,xModels);
                                            }

                                            if (xModels.size()!=0) {
                                                onComplete.onTaskCompleted(sortMe(xModels), true);
                                            }else onComplete.onError();
                                        } catch (JSONException e) {
                                            onComplete.onError();
                                        }
                                    }else {
                                        onComplete.onError();
                                    }
                                }

                                @Override
                                public void retry() {
                                    fetch(context,url,onComplete);
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                            onComplete.onError();
                        }
                    }

                    private String get(String regex,String code){
                        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
                        final Matcher matcher = pattern.matcher(code);
                        code = null;
                        while (matcher.find()) {
                            for (int i = 1; i <= matcher.groupCount(); i++) {
                                code = matcher.group(i);
                            }
                        }

                        return code;
                    }

                    @Override
                    public void onError(ANError anError) {
                        onComplete.onError();
                    }
                });
    }

    private static void putModel(String url, String quality, ArrayList<XModel> model){
        for (XModel x:model){
            if (x.getQuality().equalsIgnoreCase(quality)){
                return;
            }
        }
        if (url!=null && quality!=null) {
            XModel xModel = new XModel();
            xModel.setUrl(url);
            xModel.setQuality(quality);
            if (COOKIE!=null) {
                xModel.setCookie(COOKIE);
            }
            model.add(xModel);
        }
    }

    private static String getUpToStreamID(String string) {
        final String regex = "[-\\w]{12,}";
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(string);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }
}
