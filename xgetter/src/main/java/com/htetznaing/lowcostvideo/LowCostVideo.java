package com.htetznaing.lowcostvideo;

import android.content.Context;

import androidx.annotation.NonNull;

import com.androidnetworking.AndroidNetworking;
import com.htetznaing.lowcostvideo.Sites.BitTube;
import com.htetznaing.lowcostvideo.Sites.MegaUp;
import com.htetznaing.lowcostvideo.Sites.StreamKIWI;
import com.htetznaing.lowcostvideo.Sites.StreamTape;
import com.htetznaing.lowcostvideo.Sites.VideoBIN;
import com.htetznaing.lowcostvideo.Sites.VivoSX;
import com.htetznaing.lowcostvideo.Sites.Vlare;
import com.htetznaing.lowcostvideo.Sites.Pstream;
import com.htetznaing.lowcostvideo.Sites.CoCoScope;
import com.htetznaing.lowcostvideo.Sites.DailyMotion;
import com.htetznaing.lowcostvideo.Sites.GoUnlimited;
import com.htetznaing.lowcostvideo.Sites.Muvix;
import com.htetznaing.lowcostvideo.Sites.VideoBM;
import com.htetznaing.lowcostvideo.Sites.Vudeo;
import com.htetznaing.lowcostvideo.Utils.DailyMotionUtils;
import com.htetznaing.lowcostvideo.Core.GDrive2020;
import com.htetznaing.lowcostvideo.Model.XModel;
import com.htetznaing.lowcostvideo.Sites.FB;
import com.htetznaing.lowcostvideo.Sites.FEmbed;
import com.htetznaing.lowcostvideo.Sites.FanSubs;
import com.htetznaing.lowcostvideo.Sites.FileRIO;
import com.htetznaing.lowcostvideo.Sites.GPhotos;
import com.htetznaing.lowcostvideo.Sites.MFire;
import com.htetznaing.lowcostvideo.Sites.MP4Upload;
import com.htetznaing.lowcostvideo.Sites.OKRU;
import com.htetznaing.lowcostvideo.Sites.SendVid;
import com.htetznaing.lowcostvideo.Sites.SolidFiles;
import com.htetznaing.lowcostvideo.Sites.TW;
import com.htetznaing.lowcostvideo.Sites.UpToStream;
import com.htetznaing.lowcostvideo.Sites.VK;
import com.htetznaing.lowcostvideo.Sites.Vidoza;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.htetznaing.lowcostvideo.Utils.FacebookUtils.check_fb_video;
import static com.htetznaing.lowcostvideo.Utils.GDriveUtils.get_drive_id;

public class LowCostVideo {
    private Context context;
    private OnTaskCompleted onComplete;
    public static final String agent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.99 Safari/537.36";
    private final String mp4upload = "https?:\\/\\/(www\\.)?(mp4upload)\\.[^\\/,^\\.]{2,}\\/.+";
    private final String filerio = "https?:\\/\\/(www\\.)?(filerio)\\.[^\\/,^\\.]{2,}\\/.+";
    private final String sendvid = "https?:\\/\\/(www\\.)?(sendvid)\\.[^\\/,^\\.]{2,}\\/.+";
    private final String gphoto = "https?:\\/\\/(photos.google.com)\\/(u)?\\/?(\\d)?\\/?(share)\\/.+(key=).+";
    private final String mediafire = "https?:\\/\\/(www\\.)?(mediafire)\\.[^\\/,^\\.]{2,}\\/(file)\\/.+";
    private final String okru = "https?:\\/\\/(www.|m.)?(ok)\\.[^\\/,^\\.]{2,}\\/(video|videoembed)\\/.+";
    private final String vk = "https?:\\/\\/(www\\.)?vk\\.[^\\/,^\\.]{2,}\\/video\\-.+";
    private final String twitter = "http(?:s)?:\\/\\/(?:www\\.)?twitter\\.com\\/([a-zA-Z0-9_]+)";
    private final String youtube = "^((?:https?:)?\\/\\/)?((?:www|m)\\.)?((?:youtube\\.com|youtu.be))(\\/(?:[\\w\\-]+\\?v=|embed\\/|v\\/)?)([\\w\\-]+)(\\S+)?$";
    private final String solidfiles = "https?:\\/\\/(www\\.)?(solidfiles)\\.[^\\/,^\\.]{2,}\\/(v)\\/.+";
    private final String vidoza = "https?:\\/\\/(www\\.)?(vidoza)\\.[^\\/,^\\.]{2,}.+";
    private final String uptostream = "https?:\\/\\/(www\\.)?(uptostream|uptobox)\\.[^\\/,^\\.]{2,}.+";
    private final String fansubs = "https?:\\/\\/(www\\.)?(fansubs\\.tv)\\/(v|watch)\\/.+";
    private final String fembed = "https?:\\/\\/(www\\.)?(fembed|vcdn)\\.[^\\/,^\\.]{2,}\\/(v|f)\\/.+";
    private final String megaup = "https?:\\/\\/(www\\.)?(megaup)\\.[^\\/,^\\.]{2,}\\/.+";
    private final String gounlimited = "https?:\\/\\/(www\\.)?(gounlimited)\\.[^\\/,^\\.]{2,}\\/.+";
    private final String cocoscope = "https?:\\/\\/(www\\.)?(cocoscope)\\.[^\\/,^\\.]{2,}\\/(watch\\?v).+";
    private final String vidbm = "https?:\\/\\/(www\\.)?(vidbm)\\.[^\\/,^\\.]{2,}\\/.+";
    private final String muvix = "https?:\\/\\/(www\\.)?(muvix)\\.[^\\/,^\\.]{2,}\\/(video|download).+";
    private final String pstream = "https?:\\/\\/(www\\.)?(pstream)\\.[^\\/,^\\.]{2,}\\/(.*)\\/.+";
    private final String vlareTV = "https?:\\/\\/(www\\.)?(vlare\\.tv)\\/(.*)\\/.+";
    private final String vivoSX = "https?:\\/\\/(www\\.)?(vivo\\.sx)\\/.+";
    private final String streamKiwi = "https?:\\/\\/(www\\.)?(stream\\.kiwi)\\/(.*)\\/.+";
    private final String bitTube = "https?:\\/\\/(www\\.)?(bittube\\.video\\/videos)\\/(watch|embed)\\/.+";
    private final String videoBIN = "https?:\\/\\/(www\\.)?(videobin\\.co)\\/.+";
    private final String fourShared = "https?:\\/\\/(www\\.)?(4shared\\.com)\\/(video|web\\/embed)\\/.+";
    private final String streamtape = "https?:\\/\\/(www\\.)?(streamtape\\.com)\\/(v)\\/.+";
    private final String vudeo = "https?:\\/\\/(www\\.)?(vudeo\\.net)\\/.+";

    public LowCostVideo(@NonNull Context context){
        this.context=context;
        AndroidNetworking.initialize(context);
    }

    public void find(String url){
        if (check(mp4upload, url)) {
            //https://www.mp4upload.com/
            MP4Upload.fetch(url,onComplete);
        } else if (check(sendvid, url)) {
            //http://sendvid.com/
            SendVid.fetch(url,onComplete);
        } else if (check(gphoto, url)) {
            //https://photos.google.com/share/AF1QipMkSCF43RzZEXWyGNMYWHCegzCgdW5ao_qJEBVZ8SPkS2IQmHZFz4a13PfAZGgvUQ/photo/AF1QipNnj95SaWHJca-Q8rUxzuRkYxX6UmnDSVykJhhw?key=dGhiZnl1SURYZmRhcFF0OVdueEk2TEtDWG9pb0J3
            GPhotos.fetch(url,onComplete);
        } else if (url.contains("drive.google.com") && get_drive_id(url) != null) {
            //https://drive.google.com/open?id=1IebKJvPykCjbWroUAhFtxLkjfbEh8nVU
            GDrive2020.get(context,url,onComplete);
        } else if (check_fb_video(url)) {
            FB.fetch(url,onComplete);
        } else if (check(mediafire, url)) {
            MFire.fetch(url,onComplete);
        } else if (check(okru,url)){
            OKRU.fetch(url,onComplete);
        } else if (check(vk,url)){
            VK.fetch(url,onComplete);
        } else if (check(twitter,url)){
            TW.fetch(url,onComplete);
        } else if (check(solidfiles,url)){
            SolidFiles.fetch(url,onComplete);
        } else if (check(vidoza,url)){
            Vidoza.fetch(url,onComplete);
        } else if (check(uptostream, url)) {
            UpToStream.fetch(context,url,onComplete);
        } else if (check(fansubs,url)){
            FanSubs.fetch(url,onComplete);
        } else if (check(fembed,url)){
            FEmbed.fetch(url,onComplete);
        } else if (check(filerio,url)){
            FileRIO.fetch(url,onComplete);
        } else if (DailyMotionUtils.getDailyMotionID(url)!=null){
            DailyMotion.fetch(url,onComplete);
        } else if (check(megaup,url)){
            new MegaUp().get(context,url,onComplete);
        } else  if (check(gounlimited,url)){
            GoUnlimited.fetch(url,onComplete);
        } else if (check(cocoscope,url)){
            CoCoScope.fetch(url,onComplete);
        } else if (check(vidbm,url)){
            VideoBM.get(context,url,onComplete);
        } else if (check(muvix,url)){
            Muvix.fetch(url,onComplete);
        } else if (check(pstream,url)){
            Pstream.fetch(url,onComplete);
        } else if(check(vlareTV,url)){
            Vlare.fetch(url,onComplete);
        }else if (check(vivoSX,url)){
            VivoSX.fetch(url,onComplete);
//        }else if (check(streamKiwi,url)){
//            StreamKIWI.get(context,url,onComplete);
        }else if (check(bitTube,url)){
            BitTube.fetch(url,onComplete);
        }else if (check(videoBIN,url)){
            VideoBIN.fetch(url,onComplete);
        }else if (check(fourShared,url)){
            StreamKIWI.get(context,url,onComplete);
        }else if (check(streamtape,url)){
            StreamTape.fetch(url,onComplete);
        }else if (check(vudeo,url)) {
            Vudeo.fetch(url, onComplete);
        }else onComplete.onError();
    }

    public interface OnTaskCompleted {
        void onTaskCompleted(ArrayList<XModel> vidURL, boolean multiple_quality);
        void onError();
    }

    public void onFinish(OnTaskCompleted onComplete) {
        this.onComplete = onComplete;
    }

    private boolean check(String regex, String string) {
        final Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        final Matcher matcher = pattern.matcher(string);
        return matcher.find();
    }
}
