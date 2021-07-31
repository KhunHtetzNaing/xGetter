package com.htetznaing.lowcostvideo.Sites;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.htetznaing.lowcostvideo.LowCostVideo;
import com.htetznaing.lowcostvideo.Model.XModel;
import java.util.ArrayList;

import static com.htetznaing.lowcostvideo.LowCostVideo.agent;
import static com.htetznaing.lowcostvideo.Utils.FacebookUtils.getFbLink;
import static com.htetznaing.lowcostvideo.Utils.Utils.putModel;

/*
This is direct link getter for Facebook
    By
Khun Htetz Naing
 */

public class FB {
    public static void fetch(String url, final LowCostVideo.OnTaskCompleted onTaskCompleted){
        AndroidNetworking.post("https://fdown.net/download.php")
                .addBodyParameter("URLz", "https://www.facebook.com/video.php?v="+ url)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        ArrayList<XModel> xModels = new ArrayList<>();
                        putModel(getFbLink(response, false), "SD", xModels);
                        putModel(getFbLink(response, true), "HD", xModels);
                        onTaskCompleted.onTaskCompleted(xModels, true);
                    }

                    @Override
                    public void onError(ANError anError) {
                        onTaskCompleted.onError();
                    }
                });
    }
}
