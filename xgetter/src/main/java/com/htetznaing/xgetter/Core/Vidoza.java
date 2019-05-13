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
    public static XModel fetch(final String file_id) {
        //https://vidoza.net/4g6i833joos8.html
        // sourcesCode: [{ src: "https://str20.vidoza.net/x4lfmhuxonzpvjumxamuwssrvfmpztbyalgpbixe6vjfkxsa2yszvqohso4a/v.mp4", type: "video/mp4", label:"SD", res:"720"}],

        try {
            URL obj = new URL(file_id);
            URLConnection conn = obj.openConnection();

            InputStream is = conn.getInputStream();
            int ptr = 0;
            String response = "";
            while ((ptr = is.read()) != -1) {
                response+=((char)ptr);
            }
            String regex = "src:.+?\"(.*?)\",";

            String videosUrl=scrapergenerico(response,regex);

            if (null!=videosUrl){
                XModel xModel = new XModel();
                xModel.setUrl(videosUrl);
                xModel.setQuality("Normal");
                return xModel;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static String scrapergenerico(String code, String regex) throws UnsupportedEncodingException {

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