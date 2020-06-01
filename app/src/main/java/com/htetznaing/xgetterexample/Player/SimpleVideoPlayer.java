package com.htetznaing.xgetterexample.Player;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.htetznaing.lowcostvideo.Model.XModel;
import com.htetznaing.xgetterexample.R;
import com.htetznaing.xgetterexample.Utils.XDownloader;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;



public class SimpleVideoPlayer extends AppCompatActivity {
    private int AFTER_PERMISSION_GRANTED = 0;
    private final int PLAY = 1;
    private final int DOWNLOAD = 2;

    private boolean doubleBackToExitPressedOnce = false;
    private SimpleExoPlayer player;
    private PlayerView playerView;
    private String url,cookie=null;
    private ProgressBar progressBar;
    private XDownloader xDownloader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        getWindow().addFlags(128);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_video_player);

        if (getActionBar()!=null){
            getActionBar().hide();
        }

        if (getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }

        Intent intent = getIntent();

        if (intent.getStringExtra("url")!=null){
            url = intent.getStringExtra("url");
        }

        // get cookie from intent if google drive
        if (intent.getStringExtra("cookie")!=null){
            cookie = intent.getStringExtra("cookie");
        }

        xDownloader = new XDownloader(this);
        xDownloader.OnDownloadFinishedListerner(new XDownloader.OnDownloadFinished() {
            @Override
            public void onCompleted(String path) {
                Toast.makeText(SimpleVideoPlayer.this, path, Toast.LENGTH_SHORT).show();
            }
        });

        if (url==null){
            finish();
            Toast.makeText(this, "File Not Support!", Toast.LENGTH_SHORT).show();
        }else {
            if (url.startsWith("http")){
                initApp();
            }else
            if (checkPermissions()){
                AFTER_PERMISSION_GRANTED = PLAY;
                initApp();
            }
        }
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

    private void download(){
        XModel xModel = new XModel();
        xModel.setUrl(url);
        xModel.setCookie(cookie);
        xDownloader.download(xModel);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==1000){
            if (grantResults.length > 0&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (AFTER_PERMISSION_GRANTED==PLAY) {
                    initApp();
                }else
                    download();

            } else {
                checkPermissions();
                Toast.makeText(this, "You need to allow this permission!", Toast.LENGTH_SHORT).show();
            }
            return;
        }
    }

    private void initApp() {
        playerView = findViewById(R.id.player_view);
        progressBar = findViewById(R.id.progresbar_video_play);
        progressBar.setVisibility(View.VISIBLE);

        DefaultTrackSelector trackSelector = new DefaultTrackSelector();
        player = ExoPlayerFactory.newSimpleInstance(SimpleVideoPlayer.this, trackSelector);
        playerView.setPlayer(player);

        String userAgent = Util.getUserAgent(SimpleVideoPlayer.this, getResources().getString(R.string.app_name));
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(SimpleVideoPlayer.this, userAgent);

        //If google drive you need to set custom cookie
        if (cookie!=null) {
            DefaultHttpDataSourceFactory httpDataSourceFactory = new DefaultHttpDataSourceFactory(userAgent, null);
            httpDataSourceFactory.getDefaultRequestProperties().set("Cookie", cookie);
            dataSourceFactory = new DefaultDataSourceFactory(getApplicationContext(), null, httpDataSourceFactory);
        }

        DefaultHttpDataSourceFactory httpDataSourceFactory = new DefaultHttpDataSourceFactory(userAgent, null);
        httpDataSourceFactory.getDefaultRequestProperties().set("Referer", "");
        dataSourceFactory = new DefaultDataSourceFactory(getApplicationContext(), null, httpDataSourceFactory);

        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(url));
        player.prepare(videoSource);
        player.setPlayWhenReady(true);
        player.addListener(new Player.DefaultEventListener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playWhenReady) {
                    progressBar.setVisibility(View.GONE);
                }
                super.onPlayerStateChanged(playWhenReady, playbackState);
            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                super.onPlayerError(error);
                finish();
                System.out.println(error.getMessage());
                Toast.makeText(SimpleVideoPlayer.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        ImageButton rotate = findViewById(R.id.rotate);

        if (rotate!=null) {
            rotate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int rotation = getWindowManager().getDefaultDisplay().getRotation();
                    if (rotation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    } else {
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    }
                }
            });
        }


        ImageButton download = findViewById(R.id.download);
        if (download!=null) {
            if (!url.startsWith("http")) {
                download.setVisibility(View.GONE);
                download.setEnabled(false);
            }
            download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (checkPermissions()) {
                        AFTER_PERMISSION_GRANTED = DOWNLOAD;
                        download();
                    }
                }
            });
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Click BACK again to EXIT!", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (player !=null){
            player.setPlayWhenReady(true);
        }
    }

    @Override
    protected void onPause() {
        if (player != null) {
            player.setPlayWhenReady(false);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (player != null) {
            player.setPlayWhenReady(false);
        }
        super.onDestroy();
    }
}
