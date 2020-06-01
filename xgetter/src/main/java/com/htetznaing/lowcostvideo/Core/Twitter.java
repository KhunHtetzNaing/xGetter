package com.htetznaing.lowcostvideo.Core;

import com.htetznaing.lowcostvideo.Model.XModel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Twitter {
    public static ArrayList<XModel> fetch(String response){
            Document doc = Jsoup.parse(response);
            try{
                Elements elements = doc.getElementsByTag("tbody").get(0).getElementsByTag("tr");
                ArrayList<XModel> models = new ArrayList<>();
                for (int i=0;i<elements.size();i++) {
                    Element items = elements.get(i);
                    String url = getUrl(items);
                    String size = getSize(items);
                    if (url!=null && size!=null){
                        XModel xModel = new XModel();
                        xModel.setQuality(size);
                        xModel.setUrl(url);
                        models.add(xModel);
                    }
                }
                return models;
            }catch (Exception e){
                e.printStackTrace();
            }
        return null;
    }


    private static String getSize(Element input){
        Elements elements = input.getElementsByTag("td");
        for (int i=0;i<elements.size();i++){
            String s = elements.get(i).html();
            if (!s.startsWith("<") && s.contains("x")){
                if (s.contains(" ")){
                    s = s.replace(" ","");
                }
                return s;
            }
        }
        return null;
    }

    private static String getUrl(Element data){
        String regex = "preview_video\\('(.*)'";
        Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(data.html());

        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
