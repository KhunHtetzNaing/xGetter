package com.htetznaing.xgetter.Utils;

import com.htetznaing.xgetter.Model.XModel;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.htetznaing.xgetter.Utils.Utils.putModel;

public class GPhotosUtils {
    private static String cleanString(String string){
        string = string.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
        try {
            string = URLDecoder.decode(string, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return string;
    }

    public static ArrayList<XModel> getGPhotoLink(String string) {
        string = cleanString(string);
        final String regex = "https:\\/\\/(.*?)=m(22|18|37|36)";
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(string);
        ArrayList<XModel> xModels = new ArrayList<>();
        boolean p18=false,p22=false,p37=false,p36=false;

        String or = getOriginal(string);
        if (or!=null) {
            putModel(getOriginal(string), "Original", xModels);
        }

        while (matcher.find()) {
            switch (matcher.group(2)){
                case "36":
                    if (!p36){
                        putModel(matcher.group(),"180p",xModels);
                        p36=true;
                    }
                    break;
                case "18":
                    if (!p18) {
                        putModel(matcher.group(), "360p", xModels);
                        p18=true;
                    }
                    break;
                case "22":
                    if (!p22) {
                        putModel(matcher.group(), "720p", xModels);
                        p22=true;
                    }
                    break;
                case "37":
                    if (!p37) {
                        putModel(matcher.group(), "1080p", xModels);
                        p37=true;
                    }
                    break;
            }
        }
        return xModels;
    }

    public static String getOriginal(String string){
        final String regex = "https:\\/\\/video-downloads(.*?)\"";
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(string);
        if (matcher.find()) {
            String url = matcher.group(0).replace("\"","");
            return url;
        }
        return null;
    }
}
