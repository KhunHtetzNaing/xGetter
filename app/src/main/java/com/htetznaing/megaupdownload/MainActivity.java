package com.htetznaing.megaupdownload;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.htetznaing.xgetter.XGetter;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    XGetter xGetter;
    ProgressDialog progressDialog;
    String org;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        textView = findViewById(R.id.test);
        xGetter = new XGetter(this);
        xGetter.onFinish(new XGetter.OnTaskCompleted() {
            @Override
            public void onTaskCompleted(final String vidURL) {
                progressDialog.dismiss();
                textView.setText("Original: "+org+"\n\nResult: "+vidURL);
                System.out.println("Original: "+org+"\n\nResult: "+vidURL);
            }

            @Override
            public void onError() {
                progressDialog.dismiss();
                textView.setText("ERROR");
            }
        });
    }

    private void letGo(String url){
        progressDialog.show();
        org = url;
        xGetter.find(url);
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
}
