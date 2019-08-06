package com.htetznaing.xgetter.Core;

import com.htetznaing.xgetter.Model.XModel;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SolidFiles {
    public static ArrayList<XModel> fetch(String response){
        String src = getUrl(response);

        if (null!=src){
            XModel xModel = new XModel();
            xModel.setUrl(src);
            xModel.setQuality("Normal");

            ArrayList<XModel> xModels = new ArrayList<>();
            xModels.add(xModel);
            return xModels;
        }
        return null;
    }

    private static String getUrl(String html){
        final String regex = "downloadUrl\":\"(.*?)\"";
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(html);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
