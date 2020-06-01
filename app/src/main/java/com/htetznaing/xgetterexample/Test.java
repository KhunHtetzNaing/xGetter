package com.htetznaing.xgetterexample;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
    public static void main(String zz[]){
//
//        try {
//            Document document = Jsoup.connect("https://photos.google.com/share/AF1QipMkSCF43RzZEXWyGNMYWHCegzCgdW5ao_qJEBVZ8SPkS2IQmHZFz4a13PfAZGgvUQ/photo/AF1QipNnj95SaWHJca-Q8rUxzuRkYxX6UmnDSVykJhhw?key=dGhiZnl1SURYZmRhcFF0OVdueEk2TEtDWG9pb0J3").get();
//            String string = document.toString();
//            string = string.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
//            try {
//                string = URLDecoder.decode(string, "UTF-8");
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//            System.out.println(GPhotosUtils.getOriginal(string).getUrl());
//            ArrayList<XModel> xModels = getGPhotoLink(string);
//            for (XModel x:xModels){
//                System.out.println(x.getUrl());
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        try {
            Connection.Response response = Jsoup.connect("https://mate01.y2mate.com/analyze/ajax")
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.129 Safari/537.36")
                    .method(Connection.Method.POST)
                    .ignoreContentType(true)
                    .data("url","https://www.youtube.com/watch?v=-0M4Rw5tV1I")
                    .data("ajax","1")
                    .execute();
            String html = response.parse().html();
            String result = getHTML(html);

            String convert_url = getExtra("url: ?\"(.*?)\"",html);
            String convert__id = getExtra("_id: ?'(.*?)'",html);
            String convert_v_id = getExtra("v_id: ?'(.*?)'",html);

            Document document = Jsoup.parse(result);
            Elements elements = document.getElementsByTag("li");
            for (Element e:elements){
                String cat = e.text();
                if (cat.equalsIgnoreCase("video")){
                    //Video
                    ArrayList<MyModel> mp4 = parseData(document,"mp4");
                    for (MyModel m:mp4){
                        convert(convert_url,convert__id,convert_v_id,m.type,m.quality);
                    }
                }else if (cat.equalsIgnoreCase("mp3")){
                    //MP3
                    ArrayList<MyModel> mp3 = parseData(document,"mp3");
                }else if (cat.equalsIgnoreCase("audio")){
                    //Audio
                    ArrayList<MyModel> audio = parseData(document,"audio");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static ArrayList<MyModel> parseData(Document document,String id){
        ArrayList<MyModel> data = new ArrayList<>();
        Element element = document.getElementById(id);
        Elements tr = element.getElementsByTag("tr");
        for (Element e:tr){
            Elements td = e.getElementsByTag("td");
            if (td.size()>2) {
                String type = null,size = td.get(1).text(),quality = null,link=null;
                Element what = td.get(2).getElementsByTag("a").get(0);
                if (what.attr("href").startsWith("http")){
                    String temp =td.get(0).text();
                    if (temp!=null) {
                        type = temp.substring(temp.indexOf(".")+1, temp.indexOf(")"));
                        System.out.println("Type: "+type);
                        quality = temp.substring(temp.indexOf("(")+1, temp.indexOf(")"));
                    }
                    link = what.attr("href");
                }else {
                    type = what.attr("data-ftype");
                    quality = what.attr("data-fquality");
                }
                if (size!=null && !size.toLowerCase().equals("nan")){
                    MyModel myModel = new MyModel();
                    myModel.setQuality(quality);
                    myModel.setSize(size);
                    myModel.setType(type);
                    myModel.setLink(link);
                    data.add(myModel);
                }
            }
        }
        return data;
    }

    private static String getHTML(String html){
        final String regex = "<div class=(.*)<script";
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(html);
        if (matcher.find()) {
            String result = "<div class="+matcher.group(1);
            result = removeUnwants(result);
            return result;
        }
        return null;
    }

    private static String removeUnwants(String string){
        if (string.contains("\\\"")) {
            string = string.replace("\\\"", "\"");
        }
        if (string.contains("\\/")) {
            string = string.replace("\\/", "/");
        }
        return string;
    }
    private static String getExtra(String regex,String string){
        string = removeUnwants(string);
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(string);
        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }

    private static String upperFirstLetter(String word){
        return word.replace(String.valueOf(word.charAt(0)), String.valueOf(word.charAt(0)).toUpperCase());
    }


    private static void convert(String url,String _id,String v_id,String ftype,String fquality){
        try {
            Connection.Response response = Jsoup.connect(url)
                    .method(Connection.Method.POST)
                    .data("type","youtube")
                    .data("_id",_id)
                    .data("v_id",v_id)
                    .data("ajax","1")
                    .data("token","")
                    .data("ftype",ftype)
                    .data("fquality",fquality)
                    .ignoreContentType(true)
                    .execute();

            String result = response.parse().html();
            result = removeUnwants(result);
            result = getExtra("<a href=\"(.*?)\"",result);
            System.out.println(fquality+": "+result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
