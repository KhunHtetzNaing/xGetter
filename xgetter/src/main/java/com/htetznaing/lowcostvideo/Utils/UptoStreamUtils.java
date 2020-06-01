package com.htetznaing.lowcostvideo.Utils;

import java.net.MalformedURLException;
import java.net.URL;

public class UptoStreamUtils {
    public static String prepareUptoStream(String urlUnpreprared) {
        URL u= null;
        try {
            u = new URL(urlUnpreprared);
            String path=u.getPath();
            String[] files= path.split("/");
            String file=files[files.length-1];
            return "https://uptostream.com/iframe/" + file;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return urlUnpreprared;
    }
}
