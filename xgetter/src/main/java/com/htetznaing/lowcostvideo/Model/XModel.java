package com.htetznaing.lowcostvideo.Model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XModel implements Comparable<XModel>{
    String quality,url,cookie;

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }


    @Override
    public String toString() {
        return quality;
    }

    @Override
    public int compareTo(XModel xModel) {
        if (startWithNumber(xModel.quality)){
            return Integer.valueOf(quality.replaceAll("\\D+", "")) - Integer.valueOf(xModel.quality.replaceAll("\\D+", ""));
        }
        return this.quality.length() - xModel.quality.length();
    }

    private boolean startWithNumber(String string){
        //final String regex = "^[0-9][A-Za-z0-9-]*$";
        final String regex ="^[0-9][A-Za-z0-9-\\s,]*$"; // start with number and can contain space or comma ( 480p , ENG)
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(string);
        return  matcher.find();
    }


}
