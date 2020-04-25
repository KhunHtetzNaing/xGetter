package com.htetznaing.xgetterexample;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MegeNZ {
    public static void main(String a[]){
        System.out.println(get("https://mega.nz/#!8ss3iSwZ!ATj1hyI17G5PKLTYgEQ8DFIjHA8VAPe6g9jiKHMETpY"));
    }

    private static String get(String url){
        try {
            url = URLEncoder.encode(url,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            URL obj = new URL("http://www.autogeneratelink.info/link.php?link="+url+"&token=agl");
            URLConnection conn = null;
            conn = obj.openConnection();
            InputStream is = conn.getInputStream();
            int ptr = 0;
            String response = "";
            while ((ptr = is.read()) != -1) {
                response+=((char)ptr);
            }
            return parse(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String parse(String html){
        final String regex = "href='(.*)'";

        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(html);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
