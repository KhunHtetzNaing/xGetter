package com.htetznaing.xgetter.Core;

import com.htetznaing.xgetter.Model.XModel;
import com.htetznaing.xgetter.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DailyMotion {

    public static ArrayList<XModel> fetch(String response){
        ArrayList<XModel> xModels = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(getJson(response)).getJSONObject("metadata").getJSONObject("qualities");
            Iterator<String> iterator = jsonObject.keys();
            while(iterator.hasNext()) {
                String key = iterator.next();
                JSONArray array = jsonObject.getJSONArray(key);
                for (int i=0;i<array.length();i++){
                    JSONObject temp = array.getJSONObject(i);
                    String type = temp.getString("type");
                    String url = temp.getString("url");
                    if (key.contains("@")){
                        key = key.replace("@","-");
                    }
                    String quality = key+"p";
                    if (type.contains("mp4")){
                        Utils.putModel(url,quality,xModels);
                    }
                }
            }
            return xModels;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getJson(String html){
        final String regex = "var ?config ?=(.*);";

        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(html);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public static String getDailyMotionID(String link){
        final String regex = "^.+dailymotion.com\\/(embed)?\\/?(video|hub)\\/([^_]+)[^#]*(#video=([^_&]+))?";
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(link);
        if (matcher.find()) {
            if (matcher.group(5)!=null && !matcher.group(5).equals("null")){
                return removeSlash(matcher.group(5));
            }else return removeSlash(matcher.group(3));
        }
        return null;
    }

    private static String removeSlash(String ogay){
        if (ogay.contains("/")){
            return ogay.replace("/","");
        }
        return ogay;
    }
}
