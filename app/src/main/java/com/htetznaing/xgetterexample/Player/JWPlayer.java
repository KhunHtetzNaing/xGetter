package com.htetznaing.xgetterexample.Player;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.htetznaing.xgetterexample.R;

public class JWPlayer extends AppCompatActivity {
    private WebView webView;
    private ProgressBar progress;
    private boolean doubleBackToExitPressedOnce = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        getWindow().addFlags(128);
        setContentView(R.layout.activity_jwplayer);

        if (getActionBar()!=null){
            getActionBar().hide();
        }
        if (getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }

        String url = getIntent().getStringExtra("url");
        if (url==null){
            Toast.makeText(this, "Cannot play this video!", Toast.LENGTH_SHORT).show();
            finish();
        }

        progress = findViewById(R.id.progress);
        webView = findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                unMute();
                hideFullScreen();
                progress.setVisibility(View.GONE);
            }


            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progress.setVisibility(View.VISIBLE);
            }
        });

        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                System.out.println(consoleMessage.message());
                return super.onConsoleMessage(consoleMessage);
            }
        });
        webView.loadUrl(url);
    }

    private void unMute(){
        webView.loadUrl("javascript:(function() {\n" +
                "\tplayerInstance.setMute(false);\n" +
                "})()");
    }

    private void hideFullScreen(){
        webView.loadUrl("javascript:(function() {\n" +
                "\tdocument.getElementsByClassName('jw-icon-fullscreen').item(0).style.display='none';\n" +
                "})()");
    }

    private void pause(){
        webView.loadUrl("javascript:(function() {\n" +
                "\tplayerInstance.pause();\n" +
                "})()");
    }


    private void play(){
        webView.loadUrl("javascript:(function() {\n" +
                "\tplayerInstance.play();\n" +
                "})()");
    }


    @Override
    protected void onResume() {
        play();
        super.onResume();
    }

    @Override
    protected void onPause() {
        pause();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Click BACK again to Close!", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
