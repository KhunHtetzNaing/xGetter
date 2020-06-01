package com.htetznaing.lowcostvideo.Sites;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.htetznaing.lowcostvideo.LowCostVideo;
import com.htetznaing.lowcostvideo.Model.XModel;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.htetznaing.lowcostvideo.LowCostVideo.agent;
import static com.htetznaing.lowcostvideo.Utils.Utils.getDomainFromURL;
import static com.htetznaing.lowcostvideo.Utils.Utils.putModel;

/*
This is direct link getter for Vidoza
    By
Khun Htetz Naing
 */

public class Vidoza {
    public static void fetch(String url, final LowCostVideo.OnTaskCompleted onTaskCompleted){
        url = fixURL(url);
        if (url!=null) {
            AndroidNetworking.get(url)
                    .build()
                    .getAsString(new StringRequestListener() {
                        @Override
                        public void onResponse(String response) {
                            ArrayList<XModel> xModels = parse(response);
                            if (xModels!=null) {
                                onTaskCompleted.onTaskCompleted(xModels, false);
                            }else onTaskCompleted.onError();
                        }

                        @Override
                        public void onError(ANError anError) {
                            onTaskCompleted.onError();
                        }
                    });
        }else onTaskCompleted.onError();
    }

    private static String fixURL(String url){
        if (!url.contains("embed-")) {
            final String regex = "net\\/([^']*)";
            final Pattern pattern = Pattern.compile(regex);
            final Matcher matcher = pattern.matcher(url);
            if (matcher.find()) {
                String id = matcher.group(1);
                if (id.contains("/")) {
                    id = id.substring(0, id.lastIndexOf("/"));
                }
                url = getDomainFromURL(url)+"/embed-" + id;
            } else {
                return null;
            }
        }
        return url;
    }

    private static ArrayList<XModel> parse(String response){
        String regex = "src:.+?\"(.*?)\",";
        String videosUrl= null;
        try {
            videosUrl = scrape(response,regex);
            if (null!=videosUrl){
                XModel xModel = new XModel();
                xModel.setUrl(videosUrl);

                System.out.println(videosUrl);

                xModel.setQuality("Normal");
                ArrayList<XModel> xModels = new ArrayList<>();
                xModels.add(xModel);
                return xModels;
            }return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String scrape(String code, String regex) throws UnsupportedEncodingException {
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(code);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
