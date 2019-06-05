package com.htetznaing.xgetter.Model;

public class XModel implements Comparable<XModel>{
    String quality,url,cookie;

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    @Override
    public int compareTo(XModel xModel) {
        return this.quality.compareTo(xModel.quality);
    }
}
