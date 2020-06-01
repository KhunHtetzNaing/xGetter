package com.htetznaing.lowcostvideo.Core;

import com.htetznaing.lowcostvideo.Model.XModel;
import com.htetznaing.lowcostvideo.Utils.AADecoder;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VidBM {
    public static ArrayList<XModel> fetch(String response){
        String encoded = getEncode(response);

        System.out.println(response);

        if (encoded!=null && AADecoder.isAAEncoded(encoded)){
            try {
                String src = getSrc(AADecoder.decode(encoded));
                if (src!=null && src.length()>0){
                    XModel xModel = new XModel();
                    xModel.setUrl(src);
                    xModel.setQuality("Normal");
                    ArrayList<XModel> xModels = new ArrayList<>();
                    xModels.add(xModel);
                    return xModels;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    private static String getSrc(String code){
        final String regex = "file:\"(.*?)\"";
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(code);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private static String getEncode(String html){
        final String regex = "ﾟ(.*)\\);";
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(html);
        if (matcher.find()) {
            return matcher.group(0);
        }
        return null;
    }
}
