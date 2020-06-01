package com.htetznaing.lowcostvideo.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GDriveUtils {
    public static String get_drive_id(String string) {
        final String regex = "[-\\w]{25,}";
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(string);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    public static String getDRIVE_STREAM(String string){
        final String regex = "DRIVE_STREAM=(.*?);";

        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(string);

        if (matcher.find()) {
            return "DRIVE_STREAM="+matcher.group(1) +";";
        }

        return null;
    }

    public static String getTitle(String string){
        final String regex = "title=(.*?)&";
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(string);
        if (matcher.find()) {
            String a = matcher.group(1);
            if (a.contains("+")){
                a = a.replace("+","_");
            }
            return a;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH);
        Date now = new Date();
        return formatter.format(now) + "_xStreamPlayer.mp4";
    }

    public static String getCookie(String string){
        final String regex = "NID=(.*?);";

        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(string);

        if (matcher.find()) {
            return "NID="+matcher.group(1) +";";
        }

        return null;
    }
}
