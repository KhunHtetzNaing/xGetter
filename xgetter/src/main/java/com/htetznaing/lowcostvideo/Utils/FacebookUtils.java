package com.htetznaing.lowcostvideo.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FacebookUtils {
    public static String getFbLink(String source, boolean hd) {
        if (source != null) {
            String end = "download=";
            String start = (hd ? "id=\"hdlink\"" : "id=\"sdlink\"");
            int idx = source.indexOf(start);
            if (idx != -1) {
                source = source.substring(idx + start.length());
                String string = source.substring(0, source.indexOf(end));
                final String regex = "href=\"(.*?)\"";
                final Pattern pattern = Pattern.compile(regex);
                final Matcher matcher = pattern.matcher(string);
                if (matcher.find()) {
                    return matcher.group(1).replace("&amp;", "&");
                }
            }
        }
        return null;
    }

    public static boolean check_fb_video(String url) {
        return url.matches("-?\\d+(\\.\\d+)?");
    }
}
