package com.htetznaing.xgetter.Core;

import com.google.gson.Gson;
import com.htetznaing.xgetter.Model.XModel;
import com.htetznaing.xgetter.XGetter;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Uptostream {
    public static ArrayList<XModel> fetch(final String file_id) {

        ArrayList<XModel> xModels = new ArrayList<>();

        //https://uptostream.com/iframe/eyrasguzy8lk
        // window.sources = JSON.parse('[{"src":"https:\/\/www78.uptostream.com\/dfhuceeedb\/1080\/0\/video.mp4","type":"video\/mp4","label":"1080p","res":"1080","lang":"spa","idLang":"0"},{"src":"https:\/\/www78.uptostream.com\/dfhuceeedb\/720\/0\/video.mp4","type":"video\/mp4","label":"720p","res":"720","lang":"spa","idLang":"0"},{"src":"https:\/\/www78.uptostream.com\/dfhuceeedb\/480\/0\/video.mp4","type":"video\/mp4","label":"480p","res":"480","lang":"spa","idLang":"0"},{"src":"https:\/\/www78.uptostream.com\/dfhuceeedb\/360\/0\/video.mp4","type":"video\/mp4","label":"360p","res":"360","lang":"spa","idLang":"0"},{"src":"https:\/\/www78.uptostream.com\/dfhuceeedb\/1080\/1\/video.mp4","type":"video\/mp4","label":"1080p","res":"1080","lang":"eng","idLang":"1"},{"src":"https:\/\/www78.uptostream.com\/dfhuceeedb\/720\/1\/video.mp4","type":"video\/mp4","label":"720p","res":"720","lang":"eng","idLang":"1"},{"src":"https:\/\/www78.uptostream.com\/dfhuceeedb\/480\/1\/video.mp4","type":"video\/mp4","label":"480p","res":"480","lang":"eng","idLang":"1"},{"src":"https:\/\/www78.uptostream.com\/dfhuceeedb\/360\/1\/video.mp4","type":"video\/mp4","label":"360p","res":"360","lang":"eng","idLang":"1"}]');

        try {
            URL obj = new URL(file_id);
            URLConnection conn = obj.openConnection();
            InputStream is = conn.getInputStream();
            int ptr = 0;
            String response = "";
            while ((ptr = is.read()) != -1) {
                response+=((char)ptr);
            }
            String regex = "sources.*?\\.parse.*'(.*?)'";

            String videosUrlsJsonString=scrapergenerico(response,regex);

            Gson gson = new Gson();

            XUptoStream[] videosUrlsJson = gson.fromJson(videosUrlsJsonString,
                    XUptoStream[].class);
            if (videosUrlsJson!=null){
                for (XUptoStream videosUrlJson : videosUrlsJson) {
                    XModel xModel = new XModel();
                    putModel(videosUrlJson,xModels);
                }

            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (RuntimeException e) {
        e.printStackTrace();
        }
        return xModels;
    }

    private static String scrapergenerico(String code, String regex) throws UnsupportedEncodingException {

        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(code);


        while (matcher.find()) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                code = matcher.group(i);
            }
        }

        return code;
    }
    private static void putModel(XUptoStream videoJson, ArrayList<XModel> model){
        // window.sources = JSON.parse('[{"src":"https:\/\/www78.uptostream.com\/dfhuceeedb\/1080\/0\/video.mp4","type":"video\/mp4","label":"1080p","res":"1080","lang":"spa","idLang":"0"},{"src":"https:\/\/www78.uptostream.com\/dfhuceeedb\/720\/0\/video.mp4","type":"video\/mp4","label":"720p","res":"720","lang":"spa","idLang":"0"},{"src":"https:\/\/www78.uptostream.com\/dfhuceeedb\/480\/0\/video.mp4","type":"video\/mp4","label":"480p","res":"480","lang":"spa","idLang":"0"},{"src":"https:\/\/www78.uptostream.com\/dfhuceeedb\/360\/0\/video.mp4","type":"video\/mp4","label":"360p","res":"360","lang":"spa","idLang":"0"},{"src":"https:\/\/www78.uptostream.com\/dfhuceeedb\/1080\/1\/video.mp4","type":"video\/mp4","label":"1080p","res":"1080","lang":"eng","idLang":"1"},{"src":"https:\/\/www78.uptostream.com\/dfhuceeedb\/720\/1\/video.mp4","type":"video\/mp4","label":"720p","res":"720","lang":"eng","idLang":"1"},{"src":"https:\/\/www78.uptostream.com\/dfhuceeedb\/480\/1\/video.mp4","type":"video\/mp4","label":"480p","res":"480","lang":"eng","idLang":"1"},{"src":"https:\/\/www78.uptostream.com\/dfhuceeedb\/360\/1\/video.mp4","type":"video\/mp4","label":"360p","res":"360","lang":"eng","idLang":"1"}]');
        String quality=videoJson.getLabel()+","+ videoJson.getLang().toUpperCase() ;
        XModel xModel = new XModel();
        xModel.setUrl(videoJson.getSrc());
        xModel.setQuality(quality);
        model.add(xModel);
    }
    // window.sources = JSON.parse('[{"src":"https:\/\/www78.uptostream.com\/dfhuceeedb\/1080\/0\/video.mp4","type":"video\/mp4","label":"1080p","res":"1080","lang":"spa","idLang":"0"},{"src":"https:\/\/www78.uptostream.com\/dfhuceeedb\/720\/0\/video.mp4","type":"video\/mp4","label":"720p","res":"720","lang":"spa","idLang":"0"},{"src":"https:\/\/www78.uptostream.com\/dfhuceeedb\/480\/0\/video.mp4","type":"video\/mp4","label":"480p","res":"480","lang":"spa","idLang":"0"},{"src":"https:\/\/www78.uptostream.com\/dfhuceeedb\/360\/0\/video.mp4","type":"video\/mp4","label":"360p","res":"360","lang":"spa","idLang":"0"},{"src":"https:\/\/www78.uptostream.com\/dfhuceeedb\/1080\/1\/video.mp4","type":"video\/mp4","label":"1080p","res":"1080","lang":"eng","idLang":"1"},{"src":"https:\/\/www78.uptostream.com\/dfhuceeedb\/720\/1\/video.mp4","type":"video\/mp4","label":"720p","res":"720","lang":"eng","idLang":"1"},{"src":"https:\/\/www78.uptostream.com\/dfhuceeedb\/480\/1\/video.mp4","type":"video\/mp4","label":"480p","res":"480","lang":"eng","idLang":"1"},{"src":"https:\/\/www78.uptostream.com\/dfhuceeedb\/360\/1\/video.mp4","type":"video\/mp4","label":"360p","res":"360","lang":"eng","idLang":"1"}]');


    public class XUptoStream {

        String src,type,label,res,lang,idLang;

        public String getSrc() {
            return src;
        }

        public void setSrc(String src) {
            this.src = src;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getRes() {
            return res;
        }

        public void setRes(String res) {
            this.res = res;
        }

        public String getLang() {
            return lang;
        }

        public void setLang(String lang) {
            this.lang = lang;
        }

        public String getIdLang() {
            return idLang;
        }

        public void setIdLang(String idLang) {
            this.idLang = idLang;
        }
    }

}