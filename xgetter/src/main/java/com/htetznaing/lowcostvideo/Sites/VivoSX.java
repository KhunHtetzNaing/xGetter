package com.htetznaing.lowcostvideo.Sites;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.htetznaing.lowcostvideo.LowCostVideo;
import com.htetznaing.lowcostvideo.Model.XModel;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VivoSX {
    public static void fetch(String url, final LowCostVideo.OnTaskCompleted onTaskCompleted){
        AndroidNetworking.get(url)
                .addHeaders("User-Agent","Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.99 Safari/537.36")
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        String encString = getEncrypt(response);
                        if (encString!=null){
                            ArrayList<XModel> xModels = new ArrayList<>();
                            String src = decode(encString);
                            if (src!=null) {
                                XModel xModel = new XModel();
                                xModel.setUrl(src);
                                xModel.setQuality("Normal");
                                xModels.add(xModel);
                                onTaskCompleted.onTaskCompleted(xModels,false);
                            }else onTaskCompleted.onError();
                        }else onTaskCompleted.onError();
                    }

                    @Override
                    public void onError(ANError anError) {
                        onTaskCompleted.onError();
                    }
                });
    }

    private static String decodeURIComponent(String s) {
        if (s == null) {
            return null;
        }
        String result;
        try {
            result = URLDecoder.decode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            result = s;
        }
        return result;
    }

    private static String getEncrypt(String html){
        String regex = "source:? ?'(.*)'";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(html);
        if (matcher.find()){
            return matcher.group(1);
        }
        return null;
    }

    private static String decode(String data){
        data = decodeURIComponent(data);
        String result = null;
        for (String a:data.split("")) {
            if (a.length()>0) {
                if (result==null){result="";}
                int tmp = a.codePointAt(0) + 47;
                tmp = tmp > 126 ? tmp - 94 : tmp;
                result += fromCharCode(tmp);
            }
        }
        return result;
    }

    private static String fromCharCode(int... codePoints) {
        return new String(codePoints, 0, codePoints.length);
    }
}
