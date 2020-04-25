package com.htetznaing.xgetterexample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YT {
    static final String SD = "sd_src:\"(.*?)\"";
    static final String HD = "hd_src:\"(.*?)\"";
    static final String DASH = "dash_manifest:\"(.*?)\",";
    static final String AUDIO = "\\/><BaseURL>(.*?)<";
    public static void main(String a[]){
        try {
            String html = loadPage("https://web.facebook.com/iamHtetzNaing/videos/325729971610584/");

            System.out.println("SD: "+parse(SD,html)+"\n");
            System.out.println("HD: "+parse(HD,html)+"\n");

            String xml = parse(DASH,html);

            xml = removeUnwant(xml);

            decodeURL(xml);

            System.out.println("AUDIO: "+parseURL(parse(AUDIO,xml)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static String removeUnwant(String s){
        if (s!=null) {
            s = replace("\\x3C", "<", s);
            s = replace("\\n", "\n", s);
            s = replace("\\\"", "\"", s);
            return s;
        }return null;
    }

    private static String replace(String oldChar,String newChar,String s){
        if (s.contains(oldChar)){
            return s.replace(oldChar,newChar);
        }
        return s;
    }

    private static String parse(String regex,String html){
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(html);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }


    private static void decodeURL(String html){
        System.out.println(html);
        final String regex = "FBQualityClass=\"(.*?)\" ?FBQualityLabel=\"(.*?)\"><BaseURL>(.*?)<";

        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(html);
        while (matcher.find()) {
            String group = matcher.group(1);
            String quality = matcher.group(2);
            String url = parseURL(matcher.group(3));
            System.out.println("Group: "+group+"\nQuality: "+quality+"\nUrl: "+url+"\n\n");
        }
    }

    private static String parseURL(String url){
        if (url==null){
            return null;
        }
        if (url.contains("amp;")){
            url = url.replace("amp;","");
        }
        return url;
    }
    private static String loadPage(String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");
        connection.setRequestProperty("Accept-Language", "en-US,en;");

        BufferedReader in = new BufferedReader(new InputStreamReader(
                connection.getInputStream()));

        StringBuilder sb = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null)
            sb.append(inputLine);
        in.close();

        return sb.toString();
    }
}
