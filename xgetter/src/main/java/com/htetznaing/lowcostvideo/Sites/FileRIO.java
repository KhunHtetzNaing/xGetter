package com.htetznaing.lowcostvideo.Sites;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.htetznaing.lowcostvideo.LowCostVideo;
import com.htetznaing.lowcostvideo.Model.XModel;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.htetznaing.lowcostvideo.LowCostVideo.agent;
import static com.htetznaing.lowcostvideo.Utils.Utils.getDomainFromURL;
import static com.htetznaing.lowcostvideo.Utils.Utils.putModel;

/*
This is direct link getter for FileRIO
    By
Khun Htetz Naing
 */

public class FileRIO {
    public static void fetch(String url, final LowCostVideo.OnTaskCompleted onTaskCompleted){
        url = fixURL(url);
        if (url!=null){
            SendVid.fetch(url,onTaskCompleted);
        }else onTaskCompleted.onError();
    }

    private static String fixURL(String url){
        if (!url.contains("embed-")) {
            final String regex = "in\\/([^']*)";
            final Pattern pattern = Pattern.compile(regex);
            final Matcher matcher = pattern.matcher(url);
            if (matcher.find()) {
                String id = matcher.group(1);
                if (id.contains("/")) {
                    id = id.substring(0, id.lastIndexOf("/"));
                }
                url = getDomainFromURL(url)+"/embed-" + id + ".html";
            } else {
                return null;
            }
        }
        return url;
    }
}
