package com.htetznaing.xgetter.Utils;

import com.htetznaing.xgetter.Model.XModel;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.htetznaing.xgetter.Utils.Utils.putModel;

public class GPhotosUtils {
    public static ArrayList<XModel> getGPhotoLink(String string) {
        string = string.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
        try {
            string = URLDecoder.decode(string, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        final String regex = "https:\\/\\/(.*?)=m(22|18|37)";
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(string);
        ArrayList<XModel> xModels = new ArrayList<>();
        boolean p18=false,p22=false,p37=false;
        while (matcher.find()) {
            switch (matcher.group(2)){
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
}
