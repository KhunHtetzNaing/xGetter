package com.htetznaing.lowcostvideo.Sites;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.htetznaing.lowcostvideo.LowCostVideo;
import com.htetznaing.lowcostvideo.Model.XModel;
import com.htetznaing.lowcostvideo.Utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Pstream {

    public static void fetch(String url, final LowCostVideo.OnTaskCompleted onTaskCompleted){
        url = url.replaceAll(".net\\/.*\\/",".net/e/");
        AndroidNetworking.get(url)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        String api = getAPI(response);
                        if (api!=null){
                            AndroidNetworking.get(api)
                                    .build()
                                    .getAsString(new StringRequestListener() {
                                        @Override
                                        public void onResponse(String response) {
                                            ArrayList<XModel> xModels = parseVideo(response);
                                            if (xModels!=null) {
                                                boolean multiple = xModels.size() > 1;
                                                if (multiple) {
                                                    onTaskCompleted.onTaskCompleted(Utils.sortMe(xModels), true);
                                                } else
                                                    onTaskCompleted.onTaskCompleted(xModels, false);
                                            }else onTaskCompleted.onError();
                                        }

                                        @Override
                                        public void onError(ANError anError) {
                                            onTaskCompleted.onError();
                                        }
                                    });


                        }else {
                            onTaskCompleted.onError();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        onTaskCompleted.onError();
                    }
                });
    }

    private static ArrayList<XModel> parseVideo(String string){
        ArrayList<XModel> xModels = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(string);
            Iterator<String> keys = object.keys();
            while (keys.hasNext()){
                String key = keys.next();
                XModel xModel = new XModel();
                xModel.setQuality(key+"p");
                xModel.setUrl(object.getString(key));
                xModels.add(xModel);
            }
            return xModels;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getAPI(String html){
        final String regex = "vsuri =(.*)'";
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(html);
        if (matcher.find()) {
            String url = matcher.group(1);
            return url.substring(url.indexOf("http"));
        }
        return null;
    }
}
