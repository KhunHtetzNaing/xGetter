package com.htetznaing.xgetter.Core;

import com.htetznaing.xgetter.Model.XModel;
import com.htetznaing.xgetter.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Fembed {
    public static ArrayList<XModel> fetch(String response){
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


    public static String get_fEmbed_video_ID(String string){
        final String regex = "(v|f)(\\/|=)(.+)(\\/|&)?";
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(string);
        if (matcher.find()) {
            String id = matcher.group(3);
            if (id.contains("/")){
                id = id.replace("/","");
            }
            return id;
        }
        return null;
    }
}
