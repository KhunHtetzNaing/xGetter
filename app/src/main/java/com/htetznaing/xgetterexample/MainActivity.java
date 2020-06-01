package com.htetznaing.xgetterexample;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.htetznaing.lowcostvideo.LowCostVideo;
import com.htetznaing.lowcostvideo.Model.XModel;
import com.htetznaing.xgetterexample.Utils.XDownloader;
import com.htetznaing.xplayer.XPlayer;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    LowCostVideo xGetter;
    ProgressDialog progressDialog;
    String org;
    EditText edit_query;
    XDownloader xDownloader;
    XModel current_Xmodel =null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        xGetter = new LowCostVideo(this);

        xGetter.onFinish(new LowCostVideo.OnTaskCompleted() {

            @Override
            public void onTaskCompleted(ArrayList<XModel> vidURL, boolean multiple_quality) {
                progressDialog.dismiss();
                if (multiple_quality){
                    if (vidURL!=null) {
                        //This video you can choose qualities
                        for (XModel model : vidURL) {
                            //If google drive video you need to set cookie for play or download
                        }
                        multipleQualityDialog(vidURL);
                    }else done(null);
                }else {
                   done(vidURL.get(0));
                }
            }

            @Override
            public void onError() {
                progressDialog.dismiss();
                done(null);
            }
        });

        xDownloader = new XDownloader(this);
        xDownloader.OnDownloadFinishedListerner(new XDownloader.OnDownloadFinished() {
            @Override
            public void onCompleted(String path) {

            }
        });

        edit_query = findViewById(R.id.edit_query);

        parseMe();
    }

    private void parseMe() {
        AndroidNetworking.initialize(this);
        AndroidNetworking.get("https://www.youtube.com/get_video_info?video_id=bOh9a2euDEM&eurl=https%3A%2F%2Fyoutube.googleapis.com%2Fv%2FbOh9a2euDEM")
                .setUserAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.99 Safari/537.36")
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("onResponse: "+response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        System.out.println("onError: "+anError.getErrorDetail());
                    }
                });
    }

    private void letGo(String url) {
        org = url;
        if (checkInternet()) {
            progressDialog.show();
            xGetter.find(url);
        }
    }

    public void mp4upload(View view) {
        letGo("https://www.mp4upload.com/ct5j6f6hn1fk");
    }

    public void gdrive(View view) {
        letGo("https://drive.google.com/open?id=1IebKJvPykCjbWroUAhFtxLkjfbEh8nVU");
    }

    public void gphotos(View view) {
        letGo("https://photos.google.com/share/AF1QipMkSCF43RzZEXWyGNMYWHCegzCgdW5ao_qJEBVZ8SPkS2IQmHZFz4a13PfAZGgvUQ/photo/AF1QipNnj95SaWHJca-Q8rUxzuRkYxX6UmnDSVykJhhw?key=dGhiZnl1SURYZmRhcFF0OVdueEk2TEtDWG9pb0J3");
    }

    public void fb(View view) {
        letGo("317455892574677");
    }

    public void mediafire(View view) {
        letGo("http://www.mediafire.com/file/i8sjfas751yknlm/PaO_Ma_Lay_%255BOfficial_MV%255D_-_%25281080P_HD%2529.mp4/file");
    }

    public void okru(View view) {
        letGo("https://ok.ru/video/2155182492341");
    }

    public void vk(View view) {
        letGo("https://vk.com/video-94920838_456240508");
    }

    public void twitter(View view) {
        letGo("https://twitter.com/i/status/1115097302920847363");
    }

    public void youtube(View view) {
        letGo("https://youtu.be/O6Xu-9mhCQ0");
    }

    public void solidfiles(View view) {
        letGo("http://www.solidfiles.com/v/PAwwxGvgqd5VX");
    }

    public void vidozafiles(View view) {
        letGo("https://vidoza.net/vjo82qvj91d0.html");
    }

    public void uptostreamfiles(View view) {
        letGo("https://uptostream.com/iframe/eyrasguzy8lk");
    }

    public void fansubs(View view) {
        letGo("http://fansubs.tv/v/qLS5z7");
    }

    public void sendvid(View view) {
        letGo("http://sendvid.com/3bh9588j");
    }

    public void fEmbed(View view) {
        letGo("https://www.fembed.com/v/53k5qudgx48m08g");
    }

    public void filerio(View view){
        letGo("https://filerio.in/9sufkwnytiwp");
    }

    public void dailymotion(View view) {
        letGo("https://www.dailymotion.com/video/xyt1t1");
    }

    public void megaup(View view) {
        letGo("https://megaup.net/Lx8o/");
    }

    public boolean checkInternet() {
        boolean what;
        CheckInternet checkNet = new CheckInternet(this);
        if (checkNet.isInternetOn()) {
            what = true;
        } else {
            what = false;
            Toast.makeText(this, "No internet connection!", Toast.LENGTH_SHORT).show();
        }
        return what;
    }

    private void done(XModel xModel){
        String url = null;
        if (xModel!=null) {
            url = xModel.getUrl();
            checkFileSize(xModel);
        }
        MaterialStyledDialog.Builder builder = new MaterialStyledDialog.Builder(this);
        if (url!=null) {
            String finalUrl = url;
            builder.setTitle("Congratulations!")
                    .setDescription("Now,you can stream or download.")
                    .setStyle(Style.HEADER_WITH_ICON)
                    .setIcon(R.drawable.right)
                    .withDialogAnimation(true)
                    .setPositiveText("Stream")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            watchDialog(xModel);
                        }
                    })
                    .setNeutralText("Copy")
                    .onNeutral(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                            clipboardManager.setText(xModel.getUrl());
                            if (clipboardManager.hasText()){
                                Toast.makeText(MainActivity.this, "Copied", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .setNegativeText("Download")
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            downloadDialog(xModel);
                        }
                    });
        }else {
            builder.setTitle("Sorry!")
                    .setDescription("Error")
                    .setStyle(Style.HEADER_WITH_ICON)
                    .setIcon(R.drawable.wrong)
                    .withDialogAnimation(true)
                    .setPositiveText("OK")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                        }
                    });
        }
        MaterialStyledDialog dialog = builder.build();
        dialog.show();
    }

    private String encode(String text){
        if (text==null){
            return text;
        }
        try {
            byte[] data = text.getBytes("UTF-8");
            return Base64.encodeToString(data, Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void watchDialog(XModel xModel){
        MaterialStyledDialog.Builder builder = new MaterialStyledDialog.Builder(this);
        builder.setTitle("Notice!")
                .setDescription("Choose your player")
                .setStyle(Style.HEADER_WITH_ICON)
                .setIcon(R.drawable.right)
                .withDialogAnimation(true)
                .setPositiveText("Own Exoplayer")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        watchVideo(xModel);
                    }
                })
                .setNegativeText("MXPlayer")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        openWithMXPlayer(xModel);
                    }
                });
        MaterialStyledDialog dialog = builder.build();
        dialog.show();
    }

    private void downloadDialog(XModel xModel){
        MaterialStyledDialog.Builder builder = new MaterialStyledDialog.Builder(this);
        builder.setTitle("Notice!")
                .setDescription("Choose your downloader")
                .setStyle(Style.HEADER_WITH_ICON)
                .setIcon(R.drawable.right)
                .withDialogAnimation(true)
                .setPositiveText("Built in downloader")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        downloadFile(xModel);
                    }
                })
                .setNegativeText("ADM")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        downloadWithADM(xModel);
                    }
                });
        MaterialStyledDialog dialog = builder.build();
        dialog.show();
    }

    private void watchVideo(XModel xModel){
        Intent intent = new  Intent(this, XPlayer.class);
        intent.putExtra(XPlayer.XPLAYER_URL, xModel.getUrl());
        if (xModel.getCookie()!=null){
            intent.putExtra(XPlayer.XPLAYER_COOKIE,xModel.getCookie());
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    private void downloadFile(XModel xModel){
        current_Xmodel = xModel;
        if (checkPermissions()){
            xDownloader.download(current_Xmodel);
        }
    }

    public void open(String url,String title) {
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(URLDecoder.decode(url, "UTF-8")), "video/mp4");
            startActivity(Intent.createChooser(intent, title));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void dev(View view) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("fb://profile/100030031876000"));
            startActivity(intent);
        } catch (Exception e) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://m.facebook.com/100030031876000"));
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.about) {
            showAbout();
        }
        return super.onOptionsItemSelected(item);
    }

    public void fetch(View view) {
        String url = edit_query.getText().toString();
        letGo(url);
    }

    public void showAbout() {
        String message = "Need to fix\n" +
                "4Shared\n" +
                "StreamWIKI\n" +
                "\n" +
                "Extract stream/download url!\n" +
                "\n" +
                "#Supported Sites\n" +
                "\n" +
                "1. Google Drive\n" +
                "2. MegaUp\n" +
                "3. Google Photos\n" +
                "4. Mp4Upload\n" +
                "5. Facebook\n" +
                "6. Mediafire\n" +
                "7. Ok.Ru\n" +
                "8. VK\n" +
                "9. Twitter\n" +
                "10. Youtube [Removed]\n" +
                "11. SolidFiles\n" +
                "12. Vidoza\n" +
                "13. UptoStream\n" +
                "14. SendVid\n" +
                "15. FanSubs\n" +
                "16. Uptobox\n" +
                "17. FEmbed\n" +
                "18. FileRio\n" +
                "19. DailyMotion\n" +
                "20. MegaUp\n" +
                "21. GoUnlimited\n" +
                "22. CocoScope\n" +
                "23. VidBM\n" +
                "23. Vlare\n" +
                "24. pStream\n" +
                "25. Vivo.sx\n" +
                "26. VideoBin\n" +
                "27. BitTube\n" +
                "28. 4Shared\n" +
                "29. StreamTape\n" +
                "30. Vudeo\n" +
                "\n" +
                "Github Repo => https://github.com/KhunHtetzNaing/xGetter\n" +
                "Developer => https://facebook.com/KhunHtetzNaing0";
        View view = getLayoutInflater().inflate(R.layout.done, null);
        TextView textView = view.findViewById(R.id.message);
        textView.setText(message);
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("About")
                .setView(view)
                .setPositiveButton("Ok", null);
        builder.show();
    }


    private void multipleQualityDialog(ArrayList<XModel> model) {
        CharSequence[] name = new CharSequence[model.size()];

        for (int i = 0; i < model.size(); i++) {
            name[i] = model.get(i).getQuality();
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Quality!")
                .setItems(name, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        done(model.get(which));
                    }
                })
                .setPositiveButton("OK", null);
        builder.show();
    }

    private boolean checkPermissions() {
        int storage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        final List<String> listPermissionsNeeded = new ArrayList<>();
        if (storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 1000);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==1000){
            if (grantResults.length > 0&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                downloadFile(current_Xmodel);
            } else {
                checkPermissions();
                Toast.makeText(this, "You need to allow this permission!", Toast.LENGTH_SHORT).show();
            }
            return;
        }
    }

    public boolean appInstalledOrNot(String str) {
        try {
            getPackageManager().getPackageInfo(str, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    //Example Download Google Drive Video with ADM
    public void downloadWithADM(XModel xModel) {
        boolean appInstalledOrNot = appInstalledOrNot( "com.dv.adm");
        boolean appInstalledOrNot2 = appInstalledOrNot("com.dv.adm.pay");
        boolean appInstalledOrNot3 = appInstalledOrNot( "com.dv.adm.old");
        String str3;
        if (appInstalledOrNot || appInstalledOrNot2 || appInstalledOrNot3) {
            if (appInstalledOrNot2) {
                str3 = "com.dv.adm.pay";
            } else if (appInstalledOrNot) {
                str3 = "com.dv.adm";
            } else {
                str3 = "com.dv.adm.old";
            }

            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(xModel.getUrl()), "application/x-mpegURL");
                intent.setPackage(str3);
                if (xModel.getCookie()!=null) {
                    intent.putExtra("Cookie", xModel.getCookie());
                    intent.putExtra("Cookies", xModel.getCookie());
                    intent.putExtra("cookie", xModel.getCookie());
                    intent.putExtra("cookies", xModel.getCookie());
                }

                startActivity(intent);
                return;
            } catch (Exception e) {
                return;
            }
        }
        str3 = "com.dv.adm";
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+str3)));
        } catch (ActivityNotFoundException e2) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+str3)));
        }
    }

    //Example Open Google Drive Video with MX Player
    private void openWithMXPlayer(XModel xModel) {
        boolean appInstalledOrNot = appInstalledOrNot("com.mxtech.videoplayer.ad");
        boolean appInstalledOrNot2 = appInstalledOrNot("com.mxtech.videoplayer.pro");
        String str2;
        if (appInstalledOrNot || appInstalledOrNot2) {
            String str3;
            if (appInstalledOrNot2) {
                str2 = "com.mxtech.videoplayer.pro";
                str3 = "com.mxtech.videoplayer.ActivityScreen";
            } else {
                str2 = "com.mxtech.videoplayer.ad";
                str3 = "com.mxtech.videoplayer.ad.ActivityScreen";
            }
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(xModel.getUrl()), "application/x-mpegURL");
                intent.setPackage(str2);
                intent.setClassName(str2, str3);
                if (xModel.getCookie() != null) {
                    intent.putExtra("headers", new String[]{"cookie", xModel.getCookie()});
                    intent.putExtra("secure_uri", true);
                }
                startActivity(intent);
                return;
            } catch (Exception e) {
                e.fillInStackTrace();
                Log.d("errorMx", e.getMessage());
                return;
            }
        }
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.mxtech.videoplayer.ad")));
        } catch (ActivityNotFoundException e2) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.mxtech.videoplayer.ad")));
        }
    }

    public void gounlimited(View view) {
        letGo("https://gounlimited.to/fnuwuz7jyyz9");
    }

    public void cocoscope(View view) {
        letGo("https://www.cocoscope.com/watch?v=57072");
    }

    public void vidbm(View view) {
        letGo("https://www.vidbm.com/n27dkro8fo8v.html");
    }

    public void pstream(View view) {
        letGo("https://www.pstream.net/v/BRrExbMveZOvP4B");
    }

    public void vlare(View view) {
        letGo("https://vlare.tv/v/V0egtPxz");
    }

    public void streamWiki(View view) {
        letGo("https://stream.kiwi/e/Auy1iW");
    }

    public void vivosx(View view) {
        letGo("https://vivo.sx/5427854097");
    }

    public void bittube(View view) {
        letGo("https://bittube.video/videos/watch/8e02be96-fa09-4309-a469-3c74c945182b");
    }

    public void videobin(View view) {
        letGo("https://videobin.co/4swhhd3thhe7");
    }

    private void checkFileSize(XModel xModel){
        new AsyncTask<Void,Void,String>(){

            @Override
            protected String doInBackground(Void... voids) {
                if (xModel.getUrl()!=null) {
                    try {
                        URLConnection connection = new URL(xModel.getUrl()).openConnection();
                        if (xModel.getCookie() != null) {
                            connection.setRequestProperty("Cookie", xModel.getCookie());
                        }
                        connection.connect();
                        return calculateFileSize(connection.getContentLength());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                System.out.println(xModel.getUrl()+" => File Size: "+s+"\nCookie => "+xModel.getCookie());
                Toast.makeText(MainActivity.this, "File Size: "+s, Toast.LENGTH_SHORT).show();
            }
        }.execute();
    }

    private String calculateFileSize(long size) {
        if(size <= 0) return "0";
        final String[] units = new String[] { "B", "kB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    public void fourshared(View view) {
        letGo("https://www.4shared.com/video/bLza9r9mea/45016068_2204489923208618_5254.html");
    }
    public void streamtape(View view) {
        letGo("https://streamtape.com/v/GbmzAG9ZaVHlzK/%5BAsahi%5D_Fugou_Keiji_-_Balance_-_UNLIMITED_-_01_%5B1080p%5D.mp4");
    }

    public void vudeo(View view) {
        letGo("https://vudeo.net/azhfxfpzq6yq.html");
    }
}
