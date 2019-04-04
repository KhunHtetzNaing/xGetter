package com.htetznaing.xgetterexample;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.google.android.material.snackbar.Snackbar;
import com.htetznaing.xgetter.OkRuLinks;
import com.htetznaing.xgetter.VkLinks;
import com.htetznaing.xgetter.XGetter;
import com.htetznaing.xgetterexample.Model.TitleAndUrl;
import com.htetznaing.xgetterexample.Player.XPlayer;
import com.htetznaing.xgetterexample.Utils.XDownloader;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    XGetter xGetter;
    ProgressDialog progressDialog;
    String org;
    EditText edit_query;
    XDownloader xDownloader;

    private int AFTER_PERMISSION_GRANTED = 0;
    private final int PLAY = 1;
    private final int DOWNLOAD = 2;

    String current_src=null;
    AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        xGetter = new XGetter(this);
        xGetter.onFinish(new XGetter.OnTaskCompleted() {
            @Override
            public void onTaskCompleted(final String vidURL) {
                progressDialog.dismiss();
                if (vidURL != null) {
                    done(vidURL);
                } else done(null, null, null, false, true);
            }

            @Override
            public void onFbTaskCompleted(final String sd, final String hd) {
                progressDialog.dismiss();
                if (sd != null || hd != null) {
                    CharSequence[] text = {"HD","SD"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Quality!")
                            .setItems(text, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface d, int which) {
                                    switch (which){
                                        case 0:
                                            if (hd!=null){
                                                done(hd);
                                            }else {
//                                                Snackbar.make(getWindow().getDecorView(), "This video not available in HD", Snackbar.LENGTH_SHORT).show();
                                                dialog.show();
                                            }
                                            break;
                                        case 1:
                                            if (sd!=null){
                                                done(sd);
                                            }else {
//                                                Snackbar.make(getWindow().getDecorView(), "This video not available in SD", Snackbar.LENGTH_SHORT).show();
                                                dialog.show();
                                            }
                                            break;
                                    }
                                }
                            })
                            .setPositiveButton("Ok",null);
                    dialog = builder.create();
                    dialog.show();
                } else done(null, null, null, false, true);
            }

            @Override
            public void onOkRuTaskCompleted(OkRuLinks okRuLinks) {
                progressDialog.dismiss();
                if (okRuLinks!=null){
                    try {
                        okRuOrVkDialog(okRuLinks,true);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else done(null, null, null, false, true);
            }

            @Override
            public void onVkTaskComplete(VkLinks vkLinks) {
                progressDialog.dismiss();
                if (vkLinks!=null){
                    try {
                        okRuOrVkDialog(vkLinks,false);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else done(null, null, null, false, true);
            }

            @Override
            public void onError() {
                progressDialog.dismiss();
                done(null, null, null, false, true);
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
        letGo("https://streamcherry.com/f/nfbbfdpcqnafkltc/10000000_133548554346206_9058973369364748674_n_mp4\n");
    }

    public void megaup(View view) {
        letGo("https://megaup.net/e8ni/Logan_(2017)_720p.MP4");
    }

    public void mp4upload(View view) {
        letGo("https://www.mp4upload.com/ct5j6f6hn1fk");
    }

    public void vidcloud(View view) {
        letGo("https://vidcloud.co/v/5c2206a02affa");
    }

    public void rapidvideo(View view) {
        letGo("https://www.rapidvideo.com/v/FW5M4CBTFF");
    }

    public void gdrive(View view) {
        letGo("https://drive.google.com/open?id=1BsXhA3Sw64d2S2ZeGr5qDcPCxMRvK5Ch");
    }

    public void gphotos(View view) {
        letGo("https://photos.google.com/share/AF1QipPsU5wF954O4FwAaLp1YiTmCBv1c2cHg2dpu53fpVFggA_Ba8N9V0yH2mo7mcEHEQ/photo/AF1QipN2p_PrQiRRKgeg5flVmTaS-NSVbhTsfp1b15cf?key=NU81ZkxmY09NMWp0M01QNlY3Mng5OVA5VXNzUjJn");
    }

    public void fb(View view) {
        letGo("925916414463007");
    }

    public void mediafire(View view) {
        letGo("http://www.mediafire.com/file/dd00f818ybeu83x/");
    }

    public void okru(View view) {
        letGo("https://ok.ru/video/1246547348046");
    }

    public void vk(View view) {
        letGo("https://vk.com/video-94920838_456240508");
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

    private void done(final String url){
        MaterialStyledDialog.Builder builder = new MaterialStyledDialog.Builder(this)
                .setTitle("Congratulations!")
                .setDescription("Now,you can stream or download.")
                .setStyle(Style.HEADER_WITH_ICON)
                .setIcon(R.drawable.done)
                .withDialogAnimation(true)
                .setPositiveText("Stream")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        startActivity(new Intent(getApplicationContext(),XPlayer.class).putExtra("url",url));
                    }
                })
                .setNegativeText("Download")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        downloadFile(url);
                    }
                });
        MaterialStyledDialog dialog = builder.build();
        dialog.show();
    }


    private void downloadFile(String url){
        current_src = url;
        if (checkPermissions()){
            xDownloader.download(current_src);
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

    public void done(final String url, final String sd, final String hd, boolean fb, boolean error) {
        String message = null;
        if (!error) {
            if (!fb) {
                message = "Input\n" + org + "\n\nResult\n" + url;
            } else message = "Input\n" + org + "\n\nHD\n" + hd + "\n\nSD\n" + sd;
        } else message = "ERROR";

        View view = getLayoutInflater().inflate(R.layout.done, null);
        TextView textView = view.findViewById(R.id.message);
        textView.setText(message);
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Done")
                .setView(view);
        if (!error) {
            if (!fb) {
                builder.setPositiveButton("Stream", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MainActivity.this, XPlayer.class);
                        intent.putExtra("url", url);
                        startActivity(intent);
                    }
                });
            } else builder.setPositiveButton("SD", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(MainActivity.this, XPlayer.class);
                    intent.putExtra("url", sd);
                    startActivity(intent);
                }
            })
                    .setNegativeButton("HD", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(MainActivity.this, XPlayer.class);
                            intent.putExtra("url", hd);
                            startActivity(intent);
                        }
                    });
        } else builder.setPositiveButton("OK", null);
        builder.show();
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
                "Google Drive\n" +
                "Google Photos\n" +
                "Facebook\n" +
                "Openload\n" +
                "Streamango\n" +
                "StreamCherry\n" +
                "Mp4Upload\n" +
                "RapidVideo\n" +
                "SendVid\n" +
                "VidCloud\n" +
                "MegaUp\n" +
                "Mediafire\n" +
                "VK\n" +
                "Ok.Ru" +
                "\n" +
                "Github Repo => https://github.com/KhunHtetzNaing/xGetter";
        View view = getLayoutInflater().inflate(R.layout.done, null);
        TextView textView = view.findViewById(R.id.message);
        textView.setText(message);
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("About")
                .setView(view)
                .setPositiveButton("Stream", null);
        builder.show();
    }


    private void okRuOrVkDialog(Object object,boolean isOK) throws JSONException {
        ArrayList<TitleAndUrl> model = new ArrayList<>();
        if (isOK){
            model = generateOkLinkForDialog((OkRuLinks) object);
        }else model = generateVkLinkForDialog((VkLinks) object);

        CharSequence [] name = new CharSequence[model.size()];

        for (int i=0;i<model.size();i++){
            name[i] = model.get(i).getTitle();
        }

        final ArrayList<TitleAndUrl> finalModel = model;
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Quality!")
                .setItems(name, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        done(finalModel.get(which).getUrl());
                    }
                })
                .setPositiveButton("OK",null);
        builder.show();
    }

    private ArrayList<TitleAndUrl> generateOkLinkForDialog(OkRuLinks okRuLinks){
        ArrayList<TitleAndUrl> arrayList = new ArrayList<>();

        if (okRuLinks.getMobile144px()!=null){
            TitleAndUrl titleAndUrl = new TitleAndUrl();
            titleAndUrl.setUrl(okRuLinks.getMobile144px());
            titleAndUrl.setTitle("144p");
            arrayList.add(titleAndUrl);
        }

        if (okRuLinks.getLowest240px()!=null){
            TitleAndUrl titleAndUrl = new TitleAndUrl();
            titleAndUrl.setUrl(okRuLinks.getLowest240px());
            titleAndUrl.setTitle("240p");
            arrayList.add(titleAndUrl);
        }

        if (okRuLinks.getLow360px()!=null){
            TitleAndUrl titleAndUrl = new TitleAndUrl();
            titleAndUrl.setUrl(okRuLinks.getLow360px());
            titleAndUrl.setTitle("360p");
            arrayList.add(titleAndUrl);
        }

        if (okRuLinks.getSd480px()!=null){
            TitleAndUrl titleAndUrl = new TitleAndUrl();
            titleAndUrl.setUrl(okRuLinks.getSd480px());
            titleAndUrl.setTitle("480p");
            arrayList.add(titleAndUrl);
        }

        if (okRuLinks.getHD()!=null){
            TitleAndUrl titleAndUrl = new TitleAndUrl();
            titleAndUrl.setUrl(okRuLinks.getHD());
            titleAndUrl.setTitle("HD");
            arrayList.add(titleAndUrl);
        }

        if (okRuLinks.getFullHD()!=null){
            TitleAndUrl titleAndUrl = new TitleAndUrl();
            titleAndUrl.setUrl(okRuLinks.getFullHD());
            titleAndUrl.setTitle("Full HD");
            arrayList.add(titleAndUrl);
        }

        if (okRuLinks.getQuad2K()!=null){
            TitleAndUrl titleAndUrl = new TitleAndUrl();
            titleAndUrl.setUrl(okRuLinks.getQuad2K());
            titleAndUrl.setTitle("2K");
            arrayList.add(titleAndUrl);
        }

        if (okRuLinks.getUltra4K()!=null){
            TitleAndUrl titleAndUrl = new TitleAndUrl();
            titleAndUrl.setUrl(okRuLinks.getUltra4K());
            titleAndUrl.setTitle("Ultra4K");
            arrayList.add(titleAndUrl);
        }

        if (okRuLinks.getUrl()!=null){
            TitleAndUrl titleAndUrl = new TitleAndUrl();
            titleAndUrl.setUrl(okRuLinks.getUrl());
            titleAndUrl.setTitle("Default");
            arrayList.add(titleAndUrl);
        }

        return arrayList;
    }

    private ArrayList<TitleAndUrl> generateVkLinkForDialog(VkLinks vkLinks){
        ArrayList<TitleAndUrl> arrayList = new ArrayList<>();

        if (vkLinks.getUrl240()!=null){
            TitleAndUrl titleAndUrl = new TitleAndUrl();
            titleAndUrl.setUrl(vkLinks.getUrl240());
            titleAndUrl.setTitle("240p");
            arrayList.add(titleAndUrl);
        }

        if (vkLinks.getUrl360()!=null){
            TitleAndUrl titleAndUrl = new TitleAndUrl();
            titleAndUrl.setUrl(vkLinks.getUrl360());
            titleAndUrl.setTitle("360p");
            arrayList.add(titleAndUrl);
        }

        if (vkLinks.getUrl480()!=null){
            TitleAndUrl titleAndUrl = new TitleAndUrl();
            titleAndUrl.setUrl(vkLinks.getUrl480());
            titleAndUrl.setTitle("480p");
            arrayList.add(titleAndUrl);
        }

        if (vkLinks.getUrl720()!=null){
            TitleAndUrl titleAndUrl = new TitleAndUrl();
            titleAndUrl.setUrl(vkLinks.getUrl720());
            titleAndUrl.setTitle("720p");
            arrayList.add(titleAndUrl);
        }

        if (vkLinks.getUrl1080()!=null){
            TitleAndUrl titleAndUrl = new TitleAndUrl();
            titleAndUrl.setUrl(vkLinks.getUrl1080());
            titleAndUrl.setTitle("1080p");
            arrayList.add(titleAndUrl);
        }

        return arrayList;
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
                downloadFile(current_src);
            } else {
                checkPermissions();
                Toast.makeText(this, "You need to allow this permission!", Toast.LENGTH_SHORT).show();
            }
            return;
        }
    }
}
