package com.htetznaing.lowcostvideo.Sites;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.htetznaing.lowcostvideo.LowCostVideo;
import com.htetznaing.lowcostvideo.Model.XModel;

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
This is direct link getter for VK
    By
Khun Htetz Naing
 */

public class VK {
    public static void fetch(String url, final LowCostVideo.OnTaskCompleted onComplete){
        AndroidNetworking.get(fixURL(url))
                .addHeaders("User-agent", agent)
                .build()
                .getAsString(new StringRequestListener() {
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

                    private String get(String regex,String html){
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
        return url;
    }
}
