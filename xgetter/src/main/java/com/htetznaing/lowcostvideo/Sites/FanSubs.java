package com.htetznaing.lowcostvideo.Sites;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.htetznaing.lowcostvideo.LowCostVideo;
import com.htetznaing.lowcostvideo.Model.XModel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.htetznaing.lowcostvideo.LowCostVideo.agent;
import static com.htetznaing.lowcostvideo.Utils.Utils.putModel;
import static com.htetznaing.lowcostvideo.Utils.Utils.sortMe;

/*
This is direct link getter for FanSubs
    By
Khun Htetz Naing
 */

public class FanSubs {
    public static void fetch(String url, final LowCostVideo.OnTaskCompleted onComplete){
        AndroidNetworking.get(url)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        ArrayList<XModel> models = new ArrayList<>();
                        Document document = Jsoup.parse(response);
                        if (document.html().contains("<source")){
                            Elements element = document.getElementsByTag("source");
                            for (int i=0;i<element.size();i++){
                                Element temp = element.get(i);
                                if (temp.hasAttr("src")) {
                                    String url = temp.attr("src");
                                    putModel(url, temp.attr("label"), models);
                                }
                            }
                        }
                        if (models.size()!=0){
                            onComplete.onTaskCompleted(sortMe(models),true);
                        }else onComplete.onError();
                    }

                    @Override
                    public void onError(ANError anError) {
                        System.out.println(anError.getErrorBody());
                        onComplete.onError();
                    }
                });
    }
}
