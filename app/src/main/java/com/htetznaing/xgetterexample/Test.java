package com.htetznaing.xgetterexample;

public class Test {
    public static void main(String aaa[]){
        String a = "Openload,VidCloud,StreaMango,RapidVideo,StreamCherry,Google Drive,MegaUp,Google Photos,Mp4Upload,Facebook,Mediafire,Ok.Ru,VK,Twitter,Youtube,SolidFiles,Vidoza,UptoStream,SendVid,FanSubs,Uptobox";
        int i= 1;
        for (String f:a.split(",")){
            System.out.println(" - **"+i+". "+f+"**");
            i++;
        }
    }
}
