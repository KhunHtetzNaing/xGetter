package com.htetznaing.xgetterexample;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.htetznaing.xgetter.XGetter;

public class MainActivity extends AppCompatActivity {
    XGetter xGetter;
    ProgressDialog progressDialog;
    String org;
    EditText edit_query;
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
                if (vidURL!=null) {
                    done("Input\n" + org + "\n\nResult\n" + vidURL);
                }else done("ERROR");
            }

            @Override
            public void onFbTaskCompleted(String sd, String hd) {
                progressDialog.dismiss();
                if (sd!=null || hd!=null) {
                    done("Input\n" + org + "\n\nHD\n" + hd + "\n\nSD\n" + sd);
                }else done("ERROR");
            }

            @Override
            public void onError() {
                progressDialog.dismiss();
                done("ERROR");
            }
        });

        edit_query = findViewById(R.id.edit_query);
    }

    private void letGo(String url){
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
        letGo("https://www.rapidvideo.com/v/FY1NF585GW");
    }

    public void gdrive(View view) {
        letGo("https://drive.google.com/open?id=1BsXhA3Sw64d2S2ZeGr5qDcPCxMRvK5Ch");
    }

    public void gphotos(View view) {
        letGo("https://photos.google.com/share/AF1QipPsU5wF954O4FwAaLp1YiTmCBv1c2cHg2dpu53fpVFggA_Ba8N9V0yH2mo7mcEHEQ/photo/AF1QipN2p_PrQiRRKgeg5flVmTaS-NSVbhTsfp1b15cf?key=NU81ZkxmY09NMWp0M01QNlY3Mng5OVA5VXNzUjJn");
    }

    public void fb(View view) {
        letGo("https://www.facebook.com/KHtetzNaing/videos/925916414463007/");
    }

    public boolean checkInternet(){
        boolean what = false;
        CheckInternet checkNet = new CheckInternet(this);
        if (checkNet.isInternetOn()){
            what = true;
        }else{
            what = false;
            Toast.makeText(this, "No internet connection!", Toast.LENGTH_SHORT).show();
        }
        return what;
    }


    public void done(String message){
        View view = getLayoutInflater().inflate(R.layout.done,null);
        TextView textView = view.findViewById(R.id.message);
        textView.setText(message);
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Done")
                .setView(view)
                .setPositiveButton("OK",null);
        builder.show();
    }

    public void dev(View view) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("fb://profile/100030031876000"));
            startActivity(intent);
        }catch (Exception e){
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://m.facebook.com/100030031876000"));
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.about){
            showAbout();
        }
        return super.onOptionsItemSelected(item);
    }

    public void fetch(View view) {
        String url = edit_query.getText().toString();
        if (URLUtil.isValidUrl(url)){
            letGo(url);
        }else Toast.makeText(this, "Input valid url :)", Toast.LENGTH_SHORT).show();
    }

    public void showAbout(){
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
                "\n" +
                "Github Repo => https://github.com/KhunHtetzNaing/xGetter";
        View view = getLayoutInflater().inflate(R.layout.done,null);
        TextView textView = view.findViewById(R.id.message);
        textView.setText(message);
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("About")
                .setView(view)
                .setPositiveButton("OK",null);
        builder.show();
    }
}
