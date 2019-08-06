package com.htetznaing.xgetter.Core;

import com.htetznaing.xgetter.Model.XModel;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Vidoza {
    public static ArrayList<XModel> fetch(String response){
        String regex = "src:.+?\"(.*?)\",";
        String videosUrl= null;
        try {
            videosUrl = scrapergenerico(response,regex);
            if (null!=videosUrl){
                XModel xModel = new XModel();
                xModel.setUrl(videosUrl);
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

    public static String scrapergenerico(String code, String regex) throws UnsupportedEncodingException {

        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(code);


        while (matcher.find()) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                code = matcher.group(i);
            }
        }

        return code;
    }
}