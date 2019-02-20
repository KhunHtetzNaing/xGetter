package com.htetznaing.xgetterexample.Player;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;
import com.htetznaing.xgetterexample.R;
import com.htetznaing.xgetterexample.Utils.NormalText;

import java.util.ArrayList;
import java.util.List;

public class MyExoPlayer extends AppCompatActivity {
    long avaliablebuffer = 0;
    private BandwidthMeter bandwidthMeter;
    private DataSource.Factory mediaDataSourceFactory;
    SimpleExoPlayer player;
    long seekvalue = 0;
    private boolean shouldAutoPlay;
    private DefaultTrackSelector trackSelector;
    String url;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        getWindow().addFlags(128);
        setContentView(R.layout.activity_my_player);

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_VIEW.equals(action) && type != null) {
            if (type.startsWith("video/") || type.startsWith("audio/")) {
                url = intent.getData().toString();
            }
        }else if (Intent.ACTION_DEFAULT.equals(action) && type != null){
            if (type.startsWith("video/") || type.startsWith("audio/")) {
                url = intent.getData().toString();
            }
        }else if (Intent.CATEGORY_BROWSABLE.equals(action) && type != null){
            if (type.startsWith("video/") || type.startsWith("audio/")) {
                url = intent.getData().toString();
            }
        }else if (Intent.ACTION_MAIN.equals(action) && type != null){
            if (type.startsWith("video/") || type.startsWith("audio/")) {
                url = intent.getData().toString();
            }
        }else if (intent.getStringExtra("url")!=null){
            url = intent.getStringExtra("url");
        }

        if (url==null){
            finish();
            Toast.makeText(this, "File Not Support!", Toast.LENGTH_SHORT).show();
            return;
        }else {
            if (checkPermissions()){
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==1000){
            if (grantResults.length > 0&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initApp();
            } else {
                checkPermissions();
                Toast.makeText(this, "You need to allow this permission!", Toast.LENGTH_SHORT).show();
            }
            return;
        }
    }

    public void initApp(){
        shouldAutoPlay = true;
        bandwidthMeter = new DefaultBandwidthMeter();
        progressBar = findViewById(R.id.progrssBar);
        mediaDataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "mediaPlayerSample"), (TransferListener<? super DataSource>) bandwidthMeter);
        SimpleExoPlayerView simpleExoPlayerView = findViewById(R.id.video_view);
        trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(this.bandwidthMeter));
        player = ExoPlayerFactory.newSimpleInstance((Context) this, this.trackSelector);
        simpleExoPlayerView.setPlayer(this.player);
        simpleExoPlayerView.showController();
        player.setPlayWhenReady(this.shouldAutoPlay);
        player.prepare(new ExtractorMediaSource(Uri.parse(this.url), this.mediaDataSourceFactory, new DefaultExtractorsFactory(), null, null));
        progressBar.setVisibility(View.VISIBLE);
        player.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {
                avaliablebuffer = player.getBufferedPosition();
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                switch (playbackState) {
                    case 1:
                        Log.e("STATE_IDLE", "STATE_IDLE");
                        break;
                    case 2:
                        Log.e("STATE_BUFFERING", "STATE_BUFFERING");
                        if (player.getCurrentPosition() > avaliablebuffer) {
                            progressBar.setVisibility(View.VISIBLE);
                            break;
                        }
                        break;
                    case 3:
                        Log.e("STATE_READY", "STATE_READY");
                        progressBar.setVisibility(View.GONE);
                        seekvalue = player.getCurrentPosition();
                        break;
                    case 4:
                        Log.e("STATE_ENDED", "STATE_ENDED");
                        break;
                    default:
                        Log.e("default", "default");
                        progressBar.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                finish();
                Toast.makeText(MyExoPlayer.this, "Can't play this video!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }

            @Override
            public void onSeekProcessed() {

            }
        });
        player.seekTo(0);


        ImageButton rotate = findViewById(R.id.rotate);
        rotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int rotation = getWindowManager().getDefaultDisplay().getRotation();
                if (rotation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }else{
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    private void pausePlayer() {
        if (player!=null) {
            player.setPlayWhenReady(false);
            player.getPlaybackState();
        }
    }

    private void startPlayer() {
        if (player!=null) {
            player.setPlayWhenReady(true);
            player.getPlaybackState();
        }
    }

    protected void onPause() {
        super.onPause();
        pausePlayer();
    }

    protected void onResume() {
        super.onResume();
        startPlayer();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Notice!")
                .setMessage("Do you want to close ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MyExoPlayer.super.onBackPressed();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
        NormalText.set(dialog);
    }
}