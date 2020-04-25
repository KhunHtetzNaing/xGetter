package com.htetznaing.xgetter.Core;

import android.os.AsyncTask;

import com.htetznaing.xgetter.Model.XModel;
import com.htetznaing.xgetter.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DailyMotion {
    ArrayList<XModel> xModels = new ArrayList<>();
    OnDone onDone;
    public void fetch(String response,OnDone onDone){
        this.onDone=onDone;
        try {
            JSONObject jsonObject = new JSONObject(getJson(response)).getJSONObject("metadata").getJSONObject("qualities");
            Iterator<String> iterator = jsonObject.keys();
            while(iterator.hasNext()) {
                String key = iterator.next();
                if (!key.equalsIgnoreCase("auto")) {
                    JSONArray array = jsonObject.getJSONArray(key);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject temp = array.getJSONObject(i);
                        String type = temp.getString("type");
                        String url = temp.getString("url");
                        if (key.contains("@")) {
                            key = key.replace("@", "-");
                        }
                        String quality = key + "p";
                        if (type.contains("mp4")) {
                            Utils.putModel(url, quality, xModels);
                        }
                    }
                    showResult();
                }else {
                    try {
                        final String url = jsonObject.getJSONArray(key).getJSONObject(0).getString("url");
                        new AsyncTask<Void,Void,Void>(){
                            @Override
                            protected Void doInBackground(Void... voids) {
                                try {
                                    BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
                                    String line;
                                    String s = "";
                                    while ((line = reader.readLine()) !=null){
                                        s+=line;
                                    }
                                    for (String a:s.split("#EXT-X-STREAM-INF")){
                                        String mUrl = query("PROGRESSIVE-URI",a);
                                        String mName = query("NAME",a)+"p";
                                        if (mUrl != null) {
                                            Utils.putModel(mUrl, mName, xModels);
                                        }
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                return null;
                            }

                            @Override
                            protected void onPostExecute(Void aVoid) {
                                super.onPostExecute(aVoid);
                                showResult();
                            }
                        }.execute();
                    }catch (Exception e){
                        showResult();
                        e.printStackTrace();
                    }
                }
            }
        } catch (JSONException e) {
            showResult();
            e.printStackTrace();
        }
    }

    private void showResult() {
        if (xModels.size()>0){
            onDone.onResult(xModels);
        }else {
            onDone.onResult(null);
        }
    }

    public interface OnDone{
        void onResult(ArrayList<XModel> xModels);
    }

    private String query(String key,String string){
        final String regex = key+"=\"(.*?)\"";
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(string);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private String getJson(String html){
        final String regex = "var ?config ?=(.*);";

        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(html);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public static String getDailyMotionID(String link){
        final String regex = "^.+dailymotion.com\\/(embed)?\\/?(video|hub)\\/([^_]+)[^#]*(#video=([^_&]+))?";
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(link);
        if (matcher.find()) {
            if (matcher.group(5)!=null && !matcher.group(5).equals("null")){
                return removeSlash(matcher.group(5));
            }else return removeSlash(matcher.group(3));
        }
        return null;
    }

    private static String removeSlash(String ogay){
        if (ogay.contains("/")){
            return ogay.replace("/","");
        }
        return ogay;
    }
}
