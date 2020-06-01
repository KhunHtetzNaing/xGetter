package com.htetznaing.lowcostvideo.Utils;

import android.util.Base64;
import android.webkit.CookieManager;

import com.htetznaing.lowcostvideo.Model.XModel;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public static void putModel(String url, String quality, ArrayList<XModel> model){
        for (XModel x:model){
            if (x.getQuality().equalsIgnoreCase(quality)){
                return;
            }
        }
        if (url!=null && quality!=null) {
            XModel xModel = new XModel();
            xModel.setUrl(url);
            xModel.setQuality(quality);
            model.add(xModel);
        }
    }

    public static ArrayList<XModel> sortMe(ArrayList<XModel> x){
        if (x!=null) {
            if (x.size() > 0) {
                ArrayList<XModel> result = new ArrayList<>();
                for (XModel t : x) {
                    if (startWithNumber(t.getQuality()) || t.getQuality().isEmpty()) {  // with this modificaction it is included those with quality field is empty. EX. openload
                        result.add(t);
                    }
                }
                Collections.sort(result, Collections.reverseOrder());
                return result;
            }
        }
        return null;
    }

    private static boolean startWithNumber(String string){
        //final String regex = "^[0-9][A-Za-z0-9-]*$";
        final String regex ="^[0-9][A-Za-z0-9-\\s,]*$"; // start with number and can contain space or comma ( 480p , ENG)
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(string);
        return  matcher.find();
    }

    public static String getDomainFromURL(String url){
        String regex = "^(?:https?:\\/\\/)?(?:[^@\\n]+@)?(?:www\\.)?([^:\\/\\n?]+)";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(url);

        if (matcher.find()) {
            return matcher.group(0);
        }
        return null;
    }

    public static String base64Encode(String text) {
        byte[] data = new byte[0];
        try {
            data = text.getBytes("UTF-8");
            return Base64.encodeToString(data, Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String base64Decode(String text) {
        byte[] data = Base64.decode(text, Base64.DEFAULT);
        try {
            return new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
