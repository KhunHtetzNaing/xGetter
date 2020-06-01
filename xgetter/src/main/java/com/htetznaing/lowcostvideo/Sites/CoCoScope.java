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
import static com.htetznaing.lowcostvideo.Utils.Utils.putModel;

/*
This is direct link getter for CoCoScope
    By
Khun Htetz Naing
 */

public class CoCoScope {
    public static void fetch(String url, final LowCostVideo.OnTaskCompleted onComplete){
        AndroidNetworking.get(url)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        String src = getSrc(response);
                        boolean lowQuality = response.contains("VHSButton");
                        if (src!=null){
                            ArrayList<XModel> xModels = new ArrayList<>();
                            putModel(src,"Normal",xModels);
                            if (lowQuality) {
                                String last = src.substring(src.lastIndexOf("."));
                                src = src.replace(last, "_360" + last);
                                putModel(src, "Low", xModels);
                            }
                            onComplete.onTaskCompleted(xModels,lowQuality);
                        }else onComplete.onError();
                    }

                    private String getSrc(String response){
                        final String regex = "<source ?src=\"(.*?)\"";
                        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
                        final Matcher matcher = pattern.matcher(response);

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
}
