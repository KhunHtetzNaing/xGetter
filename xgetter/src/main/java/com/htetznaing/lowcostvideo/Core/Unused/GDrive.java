package com.htetznaing.lowcostvideo.Core.Unused;
import com.htetznaing.lowcostvideo.Model.XModel;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Deprecated
public class GDrive {
    public static ArrayList<XModel> fetch(final String file_id){
        ArrayList<XModel> xModels = new ArrayList<>();
        try {
            URL obj = new URL("https://drive.google.com/get_video_info?docid="+file_id);
            URLConnection conn = obj.openConnection();
            String header = conn.getHeaderFields().get("Set-Cookie").toString();
            String cookie = getDRIVE_STREAM(header)+getCookie(header);

            InputStream is = conn.getInputStream();
            int ptr = 0;
            String response = "";
            while ((ptr = is.read()) != -1) {
                response+=((char)ptr);
            }

            String fmt_stream_map = getQueryVariable(response);

            if (fmt_stream_map!=null){
            String maps []= fmt_stream_map.split("%2C");
            String purl = "";
            String quality = "";

            for (int i=0;i<maps.length;i++) {
                String res[] = decodeURIComponent(maps[i]).split("\\|");
                purl = res[1];
                switch (Integer.parseInt(res[0])) {
                    case 5:
                        quality = "240p";
                        break;
                    case 17:
                        quality = "Low Quality, 144p, 3GP, 0x0";
                        break;
                    case 18:
                        quality = "Medium Quality, 360p, MP4, 480x360";
                        break;
                    case 22:
                        quality = "High Quality, 720p, MP4, 1280x720";
                        break;
                    case 34:
                        quality = "Medium Quality, 360p, FLV, 640x360";
                        break;
                    case 35:
                        quality = "Standard Definition, 480p, FLV, 854x480";
                        break;
                    case 36:
                        quality = "Low Quality, 240p, 3GP, 0x0";
                        break;
                    case 37:
                        quality = "Full High Quality, 1080p, MP4, 1920x1080";
                        break;
                    case 38:
                        quality = "Original Definition, MP4, 4096x3072";
                        break;
                    case 43:
                        quality = "Medium Quality, 360p, WebM, 640x360";
                        break;
                    case 44:
                        quality = "Standard Definition, 480p, WebM, 854x480";
                        break;
                    case 45:
                        quality = "High Quality, 720p, WebM, 1280x720";
                        break;
                    case 46:
                        quality = "Full High Quality, 1080p, WebM, 1280x720";
                        break;
                    case 82:
                        quality = "Medium Quality 3D, 360p, MP4, 640x360";
                        break;
                    case 84:
                        quality = "High Quality 3D, 720p, MP4, 1280x720";
                        break;
                    case 102:
                        quality = "Medium Quality 3D, 360p, WebM, 640x360";
                        break;
                    case 104:
                        quality = "High Quality 3D, 720p, WebM, 1280x720";
                        break;
                    default:
                        quality = "Transcoded (unknown) quality";
                        break;
                }
                putModel(decodeURIComponent(purl),quality,cookie,xModels);
                }
            }else {
                return null;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return xModels;
    }

    private static String getCookie(String string){
        final String regex = "NID=(.*?);";

        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(string);

        if (matcher.find()) {
            return "NID="+matcher.group(1) +";";
        }

        return null;
    }

    private static String getDRIVE_STREAM(String string){
        final String regex = "DRIVE_STREAM=(.*?);";

        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(string);

        if (matcher.find()) {
            return "DRIVE_STREAM="+matcher.group(1) +";";
        }

        return null;
    }

    private static String getQueryVariable(String data){
        final String regex = "fmt_stream_map=(.*?)&";
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(data);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }


    private static String decodeURIComponent(String data){
        try {
            return URLDecoder.decode(data,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static void putModel(String url,String quality,String cookie,ArrayList<XModel> model){
        XModel xModel = new XModel();
        xModel.setUrl(url);
        xModel.setQuality(quality);
        xModel.setCookie(cookie);
        model.add(xModel);
    }
}
