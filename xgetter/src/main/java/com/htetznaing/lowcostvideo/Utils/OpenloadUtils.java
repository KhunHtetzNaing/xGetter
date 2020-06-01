package com.htetznaing.lowcostvideo.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OpenloadUtils {
    public static String getLongEncrypt(String string) {
        final String regex = "<p id=[^>]*>([^<]*)<\\/p>";
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(string);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public static String getLongEncrypt2(String string) {
        final String regex = "<p style=\"\" id=[^>]*>([^<]*)<\\/p>";
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(string);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public static String getKey1(String string) {
        final String regex = "\\_0x45ae41\\[\\_0x5949\\('0xf'\\)\\]\\(_0x30725e,(.*)\\),\\_1x4bfb36";
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(string);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public static String getKey2(String string) {
        final String regex = "\\_1x4bfb36=(.*);";
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(string);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
