package com.htetznaing.xgetter.Core;

import android.util.Base64;

import com.htetznaing.xgetter.Model.XModel;
import com.htetznaing.xgetter.Utils.JSUnpacker;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Muvix {

    public static ArrayList<XModel> fetch(String string){
        if (string!=null) {
            final String regex = "<a href=\"(.*?)\">|<spa.>(.*?)<\\/span>";
            final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
            final Matcher matcher = pattern.matcher(string);
            ArrayList<XModel> xModels = new ArrayList<>();

            XModel xModel = new XModel();
            while (matcher.find()) {
                if (matcher.group(1)!=null){
                    //URL
                    String data = matcher.group(1);
                    if (data.contains("muvix.us/")) {
                        xModel.setUrl(data);
                    }
                }else if (matcher.group(2)!=null){
                    String data = matcher.group(2);
                    if (data.toLowerCase().endsWith("p")){
                        xModel.setQuality(data);
                    }
                }
                if (xModel.getUrl()!=null && xModel.getQuality()!=null){
                    xModels.add(xModel);
                    xModel = new XModel();
                }
            }
            return xModels;
        }
        return null;
    }

    private static ArrayList<XModel> getVideo(String string){
        string = decodeJuicy(getJuicy(string));
        if (string!=null) {
            final String regex = "\"file\": ?\"(.*?)\",\"label\": ?\"(.*?)\"";
            final Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
            final Matcher matcher = pattern.matcher(string);
            ArrayList<XModel> xModels = new ArrayList<>();
            while (matcher.find()) {
                String url = matcher.group(1);
                String label = matcher.group(2);
                if (url!=null && url.length()>0){
                    XModel xModel = new XModel();
                    xModel.setUrl(url);
                    xModel.setQuality(label!=null ? label : "Normal");
                    xModels.add(xModel);
                }
            }
            return xModels;
        }
        return null;
    }

    private static String getJuicy(String html){
        final String regex = "JuicyCodes.Run\\(\"(.*)\\\"";
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(html);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }


    private static String decodeJuicy(String jcCode){
        if (jcCode==null)return null;
        String target = "\"+\"";
        if (jcCode.contains(target)){
            jcCode = jcCode.replace(target,"");
        }
        jcCode = jcCode.replaceAll("[^A-Za-z0-9+\\/=]","");
        byte[] bytes = Base64.decode(jcCode.getBytes(),Base64.DEFAULT);
        String coded = new String(bytes);
        JSUnpacker unpacker = new JSUnpacker(coded);
        if (unpacker.detect()){
            return unpacker.unpack().replaceAll("\\n|\\t|\\s","");
        }
        return null;
    }
}
