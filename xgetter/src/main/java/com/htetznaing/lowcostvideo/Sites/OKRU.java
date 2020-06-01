package com.htetznaing.lowcostvideo.Sites;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.htetznaing.lowcostvideo.LowCostVideo;
import com.htetznaing.lowcostvideo.Model.XModel;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.htetznaing.lowcostvideo.LowCostVideo.agent;
import static com.htetznaing.lowcostvideo.Utils.Utils.putModel;
import static com.htetznaing.lowcostvideo.Utils.Utils.sortMe;

/*
This is direct link getter for OK.ru
    By
Khun Htetz Naing
 */

public class OKRU {
    public static void fetch(String url, final LowCostVideo.OnTaskCompleted onComplete){
        AndroidNetworking.get(fixURL(url))
                .addHeaders("User-agent", "Mozilla/5.0 (Linux; Android 4.1.1; Galaxy Nexus Build/JRO03C) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.166 Mobile Safari/535.19")
                .build()
                .getAsString(new StringRequestListener() {
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
                                            putModel(url, "720p", models);
                                        } else if (name.equals("full")) {
                                            putModel(url, "1080p", models);
                                        } else if (name.equals("quad")) {
                                            putModel(url, "2000p", models);
                                        } else if (name.equals("ultra")) {
                                            putModel(url, "4000p", models);
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
                    public void onError(ANError anError) {
                        onComplete.onError();
                    }
                });
    }

    private static String fixURL(String url){
        if (!url.startsWith("https")){
            url = url.replace("http","https");
        }
        if (url.contains("m.")){
            url = url.replace("m.","");
        }
        if (url.contains("/video/")){
            url = url.replace("/video/","/videoembed/");
        }
        return url;
    }
}
