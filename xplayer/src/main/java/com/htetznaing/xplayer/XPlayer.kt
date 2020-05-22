package com.htetznaing.xplayer

import android.app.Activity
import android.app.PictureInPictureParams
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.v4.media.session.MediaSessionCompat
import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.activity_xplayer.*

class XPlayer : AppCompatActivity() {
    companion object {
        @JvmField val XPLAYER_URL = "xPlayer.URL"
        val XPLAYER_POSITION = "xPlayer.POSITION"
        @JvmField val XPLAYER_COOKIE = "xPlayer.COOKIE"
    }

    lateinit var mUrl: String
    var mCookie:String = "null"
    lateinit var player : SimpleExoPlayer
    private var videoPosition:Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_xplayer)
        if (intent.extras == null || !intent.hasExtra(XPLAYER_URL)) {
            finish()
        }

        mUrl = intent.getStringExtra(XPLAYER_URL)

        if(intent.getStringExtra(XPLAYER_COOKIE)!=null) {
            mCookie = intent.getStringExtra(XPLAYER_COOKIE)
        }
        savedInstanceState?.let { videoPosition = savedInstanceState.getLong(XPLAYER_POSITION) }
    }

    override fun onStart() {
        super.onStart()


        player = ExoPlayerFactory.newSimpleInstance(this, DefaultTrackSelector())

        playerView.player = player

        var dataSourceFactory = DefaultDataSourceFactory(this, Util.getUserAgent(this, applicationInfo.loadLabel(packageManager).toString()))

        //If google drive you need to set custom cookie
        if (mCookie != "null") {
            val httpDataSourceFactory = DefaultHttpDataSourceFactory(Util.getUserAgent(this, applicationInfo.loadLabel(packageManager).toString()), null)
            httpDataSourceFactory.defaultRequestProperties.set("Cookie", mCookie)
            dataSourceFactory = DefaultDataSourceFactory(applicationContext, null, httpDataSourceFactory)
        }

        when (Util.inferContentType(Uri.parse(mUrl))) {
            C.TYPE_HLS -> {
                val mediaSource = HlsMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(mUrl))
                player.prepare(mediaSource)
            }

            C.TYPE_OTHER -> {
                val mediaSource = ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(mUrl))
                player.prepare(mediaSource)
            }

            else -> {
                //This is to catch SmoothStreaming and DASH types which are not supported currently
                setResult(Activity.RESULT_CANCELED)
                finish()
            }
        }

        progresbar_video_play.setVisibility(View.VISIBLE)

        var returnResultOnce:Boolean = true

        player.addListener(object : Player.EventListener{
            override fun onTimelineChanged(timeline: Timeline?, manifest: Any?, reason: Int) {}

            override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {}

            override fun onRepeatModeChanged(repeatMode: Int) {}

            override fun onPositionDiscontinuity(reason: Int) {}

            override fun onLoadingChanged(isLoading: Boolean) {}

            override fun onTracksChanged(trackGroups: TrackGroupArray?, trackSelections: TrackSelectionArray?) {}

            override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters?) {}

            override fun onPlayerError(error: ExoPlaybackException?) {
                setResult(Activity.RESULT_CANCELED)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAndRemoveTask()
                }
            }

            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                if(playbackState == Player.STATE_READY && returnResultOnce){
                    setResult(Activity.RESULT_OK)
                    progresbar_video_play.setVisibility(View.GONE)
                    returnResultOnce = false
                }
            }

            override fun onSeekProcessed() {}
        })
        player.playWhenReady = true

        //Use Media Session Connector from the EXT library to enable MediaSession Controls in PIP.
        val mediaSession = MediaSessionCompat(this, packageName)
        val mediaSessionConnector = MediaSessionConnector(mediaSession)
        mediaSessionConnector.setPlayer(player, null)
        mediaSession.isActive = true
    }

    override fun onPause() {
        System.out.println("onPause")
        videoPosition = player.currentPosition
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        System.out.println("onResume")
        if(videoPosition > 0L){
            player.seekTo(videoPosition)
        }
        //Makes sure that the media controls pop up on resuming and when going between PIP and non-PIP states.
        player.playWhenReady = true
        playerView.useController = true
    }

    override fun onStop() {
        super.onStop()
        System.out.println("onStop")
        videoPosition = player.currentPosition
        player.playWhenReady = false
    }

    override fun onDestroy() {
        super.onDestroy()
        System.out.println("onDestroy")
        playerView.player = null
        player.release()
        //PIPmode activity.finish() does not remove the activity from the recents stack.
        //Only finishAndRemoveTask does this.
        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) && packageManager.hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finishAndRemoveTask()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState.apply {
            this?.putLong(XPLAYER_POSITION, player.currentPosition)
        })
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        videoPosition = savedInstanceState!!.getLong(XPLAYER_POSITION)
    }

    override fun onBackPressed(){
        super.onBackPressed()
    }
}