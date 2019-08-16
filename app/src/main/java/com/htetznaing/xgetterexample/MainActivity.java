package com.htetznaing.xgetterexample;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
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
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.htetznaing.xgetter.Model.XModel;
import com.htetznaing.xgetter.XGetter;
import com.htetznaing.xgetterexample.Player.SimpleVideoPlayer;
import com.htetznaing.xgetterexample.Utils.XDownloader;
import com.htetznaing.xplayer.XPlayer;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    XGetter xGetter;
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
        xGetter = new XGetter(this);

        xGetter.onFinish(new XGetter.OnTaskCompleted() {

            @Override
            public void onTaskCompleted(ArrayList<XModel> vidURL, boolean multiple_quality) {
                progressDialog.dismiss();
                if (multiple_quality){
                    if (vidURL!=null) {
                        //This video you can choose qualities
                        for (XModel model : vidURL) {
                            String url = model.getUrl();
                            //If google drive video you need to set cookie for play or download
                            String cookie = model.getCookie();
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
    }

    private void letGo(String url) {
        org = url;
        if (checkInternet()) {
            progressDialog.show();
            xGetter.find(url);
        }
    }

    public void openload(View view) {
        letGo("https://openload.co/f/ManbcvAX2_M/");
    }

    public void streamango(View view) {
        letGo("https://fruithosts.net/f/scqfctmltrormqar");
    }

    public void streamcherry(View view) {
        letGo("https://streamcherry.com/f/nfbbfdpcqnafkltc/10000000_133548554346206_9058973369364748674_n_mp4");
    }

    public void megaup(View view) {
        letGo("https://megaup.net/3tD1V?pt=ns7RGzoGCR%2F0dl9WG4J0b8FWzwcixEtyF1yia8JH6zM%3D");
    }

    public void mp4upload(View view) {
        letGo("https://www.mp4upload.com/ct5j6f6hn1fk");
    }

    public void vidcloud(View view) {
        letGo("https://vcstream.to/embed/5d4685b52d726/34147942_424434834686205_6312223197668311040_n.mp4");
    }

    public void rapidvideo(View view) {
        letGo("https://www.rapidvideo.com/v/FW5M4CBTFF");
    }

    public void gdrive(View view) {
        letGo("https://drive.google.com/open?id=1nxWszNOJqkx7q0r_lFI_0IfAPiDGCmRJ");
    }

    public void gphotos(View view) {
        letGo("https://photos.google.com/share/AF1QipPsU5wF954O4FwAaLp1YiTmCBv1c2cHg2dpu53fpVFggA_Ba8N9V0yH2mo7mcEHEQ/photo/AF1QipN2p_PrQiRRKgeg5flVmTaS-NSVbhTsfp1b15cf?key=NU81ZkxmY09NMWp0M01QNlY3Mng5OVA5VXNzUjJn");
    }

    public void fb(View view) {
        letGo("925916414463007");
    }

    public void mediafire(View view) {
        letGo("https://www.mediafire.com/file/dd00f818ybeu83x/");
    }

    public void okru(View view) {
        letGo("https://ok.ru/video/31800494765");
    }

    public void vk(View view) {
        letGo("https://vk.com/video-94920838_456240508");
    }

    public void twitter(View view) {
        letGo("https://twitter.com/i/status/1115097302920847363");
    }

    public void youtube(View view) {
        letGo("https://www.youtube.com/watch?v=Q4CSEK663Wc");
    }

    public void solidfiles(View view) {
        letGo("http://www.solidfiles.com/v/PAwwxGvgqd5VX");
    }

    public void vidozafiles(View view) {
        letGo("https://vidoza.net/ptzlgta9cl1q.html");
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

    public boolean checkInternet() {
        boolean what = false;
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
//                            Intent intent = new Intent(getApplicationContext(), SimpleVideoPlayer.class);
//                            intent.putExtra("url",finalUrl);
//                            //If google drive you need to put cookie
//                            if (xModel.getCookie()!=null){
//                                intent.putExtra("cookie",xModel.getCookie());
//                            }
//                            startActivity(intent);
                            watchVideo(xModel);
                        }
                    })
                    .setNegativeText("Download")
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                            downloadFile(xModel);
                            downloadWithADM(xModel);
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
        String message = "Extract stream/download url!\n" +
                "\n" +
                "#Supported Sites\n" +
                "\n" +
                "Openload\n" +
                "StreaMango\n" +
                "RapidVideo\n" +
                "StreamCherry\n" +
                "Google Drive\n" +
                "Google Photos\n" +
                "Mp4Upload\n" +
                "Facebook\n" +
                "Mediafire\n" +
                "Ok.Ru\n" +
                "VK\n" +
                "Twitter\n" +
                "Youtube\n" +
                "SolidFiles\n" +
                "Vidoza\n" +
                "UptoStream\n" +
                "SendVid\n" +
                "FanSubs\n"+
                "Uptobox\n"+
                "\n" +
                "Github Repo => https://github.com/KhunHtetzNaing/xGetter" +
                "\n" +
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
}
