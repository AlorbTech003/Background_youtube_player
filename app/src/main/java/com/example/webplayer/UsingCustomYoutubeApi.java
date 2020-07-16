package com.example.webplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ProcessLifecycleOwner;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.PlayerUiController;

import java.util.function.LongToDoubleFunction;

public class UsingCustomYoutubeApi extends AppCompatActivity  {
    private YouTubePlayerView youTubePlayerView;
    Intent intent;
    String id,webview_content;
    static long doubletap_starttime=0;
    Double duration,current_time;
    RelativeLayout back,front;
    YouTubePlayerTracker tracker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_using_custom_youtube_api);


        intent=getIntent();
        id=intent.getStringExtra("video_id");
        webview_content=intent.getStringExtra("video_url");

        current_time=0.0;
        duration=0.0;

        getSupportActionBar().hide();

        if(id==null)
            Toast.makeText(this, "Video id is null", Toast.LENGTH_SHORT).show();


        youTubePlayerView=findViewById(R.id.youtube_player_view);
       // youTubePlayerView.enterFullScreen();
        youTubePlayerView.setEnableAutomaticInitialization(false);
        youTubePlayerView.enterFullScreen();
      //  tracker=new YouTubePlayerTracker();

        if(!CheckConnection.checkInternetConnection(this))
        {
            Toast.makeText(this, "Internet Not Available", Toast.LENGTH_SHORT).show();
            Intent i=new Intent(this,MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            startActivity(i);
        }
        else
            video_start(id,webview_content,0);

    }


    public void video_start(final String vid_id,final String webview_display_content, final float start_time){

        if(webview_display_content.contains("list")){
            String[] args=webview_display_content.split("list=");
            String temp=args[args.length-1];
            String a= extract_string(temp,'&');

        }


        youTubePlayerView.initialize(new YouTubePlayerListener() {
            @Override
            public void onReady(YouTubePlayer youTubePlayer) {
                String videoId = vid_id;
              //  youTubePlayer.addListener(tracker);
                youTubePlayer.loadVideo(videoId, start_time);
            }

            @Override
            public void onStateChange(YouTubePlayer youTubePlayer, PlayerConstants.PlayerState playerState) {
                if(playerState.equals(PlayerConstants.PlayerState.ENDED))
                    {
                        Intent i=new Intent(getApplicationContext(),MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        youTubePlayerView.release();
                        finish();
                        startActivity(i);
                    }
            }

            @Override
            public void onPlaybackQualityChange(YouTubePlayer youTubePlayer, PlayerConstants.PlaybackQuality playbackQuality) {

            }

            @Override
            public void onPlaybackRateChange(YouTubePlayer youTubePlayer, PlayerConstants.PlaybackRate playbackRate) {

            }

            @Override
            public void onError(YouTubePlayer youTubePlayer, PlayerConstants.PlayerError playerError) {

            }

            @Override
            public void onCurrentSecond(YouTubePlayer youTubePlayer, float v) {
            }

            @Override
            public void onVideoDuration(YouTubePlayer youTubePlayer, float v) {
            }

            @Override
            public void onVideoLoadedFraction(YouTubePlayer youTubePlayer, float v) {
            }

            @Override
            public void onVideoId(YouTubePlayer youTubePlayer, String s) {

            }

            @Override
            public void onApiChange(YouTubePlayer youTubePlayer) {

            }




        });
    }


    @Override
    public void onBackPressed() {
        Intent i=new Intent(this,MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        youTubePlayerView.release();
        finish();
        startActivity(i);
    }


    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this, "Youtube playing in background..relax!!", Toast.LENGTH_SHORT).show();
        youTubePlayerView.enableBackgroundPlayback(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public String extract_string(String str,char endchar){
        String extracted_string="";
        if(str!=null||String.valueOf(endchar)!=null){
            for(int a=0;a<str.length();a++)
            {
                if(str.charAt(a)!=endchar)
                    extracted_string+=str.charAt(a);
                else
                    break;
            }
        }

        return extracted_string;

    }



}