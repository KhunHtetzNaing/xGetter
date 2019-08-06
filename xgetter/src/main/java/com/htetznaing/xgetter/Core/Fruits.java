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

public class Fruits {
    public static ArrayList<XModel> fetch(String response){
        String start = getStart(response);
        String end = getEnd(response);

        if (null!=start && null!=end){
            XModel xModel = new XModel();
            xModel.setUrl("https:"+getStreamURL(start,Integer.parseInt(end)));
            xModel.setQuality("Normal");

            ArrayList<XModel> xModels = new ArrayList<>();
            xModels.add(xModel);
            return xModels;
        }

        return null;
    }

    private static String fromCharCode(int... codePoints) {
        return new String(codePoints, 0, codePoints.length);
    }

    private static String getStreamURL(String hashCode,int intVal){
        String chars = "=/+9876543210zyxwvutsrqponmlkjihgfedcbaZYXWVUTSRQPONMLKJIHGFEDCBA";
        String retVal = "";
        hashCode = hashCode.replace("[^A-Za-z0-9\\+\\/\\=]", "");
        for (int hashIndex=0;hashIndex<hashCode.length();hashIndex += 4){
            int hashCharCode_0 = chars.indexOf(hashCode.charAt(hashIndex));
            int hashCharCode_1 = chars.indexOf(hashCode.charAt(hashIndex + 1));
            int hashCharCode_2 = chars.indexOf(hashCode.charAt(hashIndex + 2));
            int hashCharCode_3 = chars.indexOf(hashCode.charAt(hashIndex + 3));
            retVal = retVal + fromCharCode((((hashCharCode_0 << 0x2) | (hashCharCode_1 >> 0x4)) ^ intVal));
            if (hashCharCode_2 != 0x40){
                retVal = retVal + fromCharCode(((hashCharCode_1 & 0xf) << 0x4) | (hashCharCode_2 >> 0x2));
            }
            if (hashCharCode_3 !=0x40){
                retVal = retVal + fromCharCode(((hashCharCode_2 & 0x3) << 0x6) | hashCharCode_3);
            }
        }
        return retVal;
    }



    private static String getStart(String string){
        final String regex = "src:d\\('([^']*)',([^\\)]*)";
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(string);
        if (matcher.find()) {
            return matcher.group(1);

        }
        return null;
    }

    private static String getEnd(String string){
        final String regex = "src:d\\('([^']*)',([^\\)]*)";
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(string);
        if (matcher.find()) {
            return matcher.group(2);

        }
        return null;
    }
}
