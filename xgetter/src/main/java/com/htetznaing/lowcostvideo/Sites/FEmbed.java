package com.htetznaing.lowcostvideo.Sites;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.htetznaing.lowcostvideo.LowCostVideo;
import com.htetznaing.lowcostvideo.Model.XModel;
import com.htetznaing.lowcostvideo.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.htetznaing.lowcostvideo.Utils.Utils.sortMe;

/*
This is direct link getter for Fembed
    By
Khun Htetz Naing
 */

public class FEmbed {
    public static void fetch(String url, final LowCostVideo.OnTaskCompleted onComplete){
        String id = get_fEmbed_video_ID(url);
        if (id!=null){
            AndroidNetworking.post("https://www.fembed.com/api/source/"+id)
                    .build()
                    .getAsString(new StringRequestListener() {
                        @Override
                        public void onResponse(String response) {
                            ArrayList<XModel> xModels = parse(response);
                            if (xModels!=null){
                                onComplete.onTaskCompleted(sortMe(xModels),true);
                            }else onComplete.onError();
                        }

                        @Override
                        public void onError(ANError anError) {
                            onComplete.onError();
                        }
                    });
        }else onComplete.onError();
    }

    private static ArrayList<XModel> parse(String response){
        ArrayList<XModel> xModels = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.has("data")){
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject object = jsonArray.getJSONObject(i);
                    Utils.putModel(object.getString("file"),object.getString("label"),xModels);
                }
                return xModels;
            }else return null;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static String get_fEmbed_video_ID(String string){
        final String regex = "(v|f)(\\/|=)(.+)(\\/|&)?";
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(string);
        if (matcher.find()) {
            return matcher.group(3).replaceAll("&|/","");
        }
        return null;
    }
}
