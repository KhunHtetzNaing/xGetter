package com.htetznaing.lowcostvideo.Sites;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.htetznaing.lowcostvideo.LowCostVideo;
import com.htetznaing.lowcostvideo.Model.XModel;
import com.htetznaing.lowcostvideo.Utils.JSUnpacker;
import com.htetznaing.lowcostvideo.Utils.Utils;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
This is direct link getter for MP4Upload
    By
Khun Htetz Naing
 */

public class MP4Upload {
    public static void fetch(String url, final LowCostVideo.OnTaskCompleted onTaskCompleted){
        AndroidNetworking.get(fixURL(url))
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        ArrayList<XModel> xModels = parseVideo(response);
                        if (xModels==null){
                            onTaskCompleted.onError();
                        }else onTaskCompleted.onTaskCompleted(xModels, false);
                    }

                    @Override
                    public void onError(ANError anError) {
                        onTaskCompleted.onError();
                    }
                });
    }

    private static ArrayList<XModel> parseVideo(String response){
        JSUnpacker jsUnpacker = new JSUnpacker(getEvalCode(response));
        if(jsUnpacker.detect()) {
            String src = getSrc(jsUnpacker.unpack());
            if (src!=null && src.length()>0){
                ArrayList<XModel> xModels = new ArrayList<>();
                Utils.putModel(src,"Normal",xModels);
                if (!xModels.isEmpty()){
                    return xModels;
                }
            }
        }
        return null;
    }

    private static String fixURL(String url){
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
            }
        }
        return url;
    }

    private static String getSrc(String code){
        final String regex = "src: ?\"(.*?)\"";
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(code);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private static String getEvalCode(String html){
        final String regex = "eval(.*)";
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(html);
        if (matcher.find()) {
            return matcher.group(0);
        }
        return null;
    }
}
