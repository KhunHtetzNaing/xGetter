package com.htetznaing.lowcostvideo.Sites;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.htetznaing.lowcostvideo.LowCostVideo;
import com.htetznaing.lowcostvideo.Model.XModel;
import com.htetznaing.lowcostvideo.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VideoBIN {

    public static void fetch(String url, final LowCostVideo.OnTaskCompleted onTaskCompleted){
        AndroidNetworking.get(url)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        ArrayList<XModel> xModels = parseVideo(response);
                        if (xModels.isEmpty()) {
                            onTaskCompleted.onError();
                        } else onTaskCompleted.onTaskCompleted(Utils.sortMe(xModels), true);
                    }

                    @Override
                    public void onError(ANError anError) {
                        onTaskCompleted.onError();
                    }
                });
    }

    private static String quality(int size,int index){
        List<String> quality = new ArrayList<>();
        switch (size){
            case 1:quality.add("480p");break;
            case 2:quality.add("720p");quality.add("480p");break;
            case 3:quality.add("1080p");quality.add("720p");quality.add("480p");break;
            case 4:quality.add("Higher");quality.add("1080p");quality.add("720p");quality.add("480p");break;
        }
        return quality.get(index);
    }

    private static ArrayList<XModel> parseVideo(String html){
        ArrayList<XModel> xModels = new ArrayList<>();
        try {
            String regex = "sources:(.*),";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(html);
            if (matcher.find()){
                String source = matcher.group(1).trim();
                JSONArray array = new JSONArray(source);
                List<String> list = new ArrayList<>();
                for (int i=0;i<array.length();i++){
                    String src = array.getString(i);
                    if (!src.endsWith(".m3u8")){
                        list.add(src);
                    }
                }

                for (int i=0;i<list.size();i++){
                    String label = quality(list.size(),i);
                    XModel xModel = new XModel();
                    xModel.setQuality(label);
                    xModel.setUrl(list.get(i));
                    xModels.add(xModel);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return xModels;
    }
}
