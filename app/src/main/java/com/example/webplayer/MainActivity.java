package com.example.webplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebBackForwardList;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;


import org.w3c.dom.Document;



public class MainActivity extends AppCompatActivity implements LifecycleObserver {
    WebView webView;
    View view;
    String refined_id="";
    String id,playlist_url;
    TextView conn;
    Handler handler;

    //include &t=1m45s in youtube url of watch to start from that exact time

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView=findViewById(R.id.webview);
        view=View.inflate(this,R.layout.activity_main,null);
        conn=findViewById(R.id.connection);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(MainActivity.this);
        handler=new Handler();

        if(savedInstanceState==null)
            startConnection();


    }


    private void startConnection() {

        String ua = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36";

        if(!CheckConnection.checkInternetConnection(this)){
            Toast.makeText(getApplicationContext(), "No Internet", Toast.LENGTH_SHORT).show();
            webView.setVisibility(View.INVISIBLE);
            conn.setVisibility(View.VISIBLE);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startConnection();
                }
            },5000);
        }
        else {
            Toast.makeText(this, "Internet available", Toast.LENGTH_SHORT).show();
            webView.setVisibility(View.VISIBLE);
            conn.setVisibility(View.INVISIBLE);

            webView.getSettings().setMediaPlaybackRequiresUserGesture(true);
            webView.getSettings().setPluginState(WebSettings.PluginState.ON);

            webView.getSettings().setLoadWithOverviewMode(true);
            webView.getSettings().setUseWideViewPort(true);

            webView.getSettings().setSupportZoom(true);
            webView.getSettings().setBuiltInZoomControls(true);
            webView.getSettings().setDisplayZoomControls(false);

            webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
            webView.setScrollbarFadingEnabled(false);

            webView.getSettings().setJavaScriptEnabled(true);
            // webView.getSettings().setUserAgentString(ua);  //to open in desktop site
            webView.clearCache(true);

            webView.setWebViewClient(new MyWebViewClient(getApplicationContext(),webView));
            webView.loadUrl("http://www.youtube.com/");
            webView.setVisibility(View.VISIBLE);



        }
    }


    @Override
    public void onBackPressed() {
        if(webView.canGoBack())
            webView.goBack();
        else
            super.onBackPressed();
    }

  //  @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
   // public void appInResumeState() {
   //     Toast.makeText(this,"In Foreground",Toast.LENGTH_SHORT).show();
   // }

   // @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
   // public void appInPauseState() {
   //     Toast.makeText(this,"In Background  ",Toast.LENGTH_SHORT).show();
   // }


    public class MyWebViewClient extends WebViewClient {

        Context context;
        WebView View;

        public MyWebViewClient(Context context,WebView view) {
            this.context=context;
            this.View=view;
            //start anything you need to
        }

        public void onPageStarted(final WebView view, final String url, Bitmap favicon) {

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            if(url.startsWith("https://m.youtube.com/watch?"))
            {

                id= setVideoIdFromLink(url);
                playlist_url=url;

               if(id==null)
                   Toast.makeText(context, "id is null", Toast.LENGTH_SHORT).show();
              // else
                    // Toast.makeText(context, "video id - "+id, Toast.LENGTH_SHORT).show();


                //view.loadUrl("https://www.google.com");



                for(int j=0;j<id.length();j++)
                {
                    if(id.charAt(j)=='&')
                        break;
                    else
                        refined_id+=id.charAt(j);
                }

                Intent intent=new Intent(getApplicationContext(),UsingCustomYoutubeApi.class);
                intent.putExtra("video_id",refined_id);
                intent.putExtra("video_url",playlist_url);
                view.destroy();
                startActivity(intent);


            }
            else {
                view.setVisibility(android.view.View.VISIBLE);
            }
        }

        public String setVideoIdFromLink(String url)
        {
            //get the video id from the link
            String[] args=url.split("v=");
            String videoId=args[args.length-1];

            return videoId;
        }

    }



    @Override
    protected void onResume() {
        super.onResume();
        if(refined_id!=""){
            Intent i=new Intent(MainActivity.this,UsingCustomYoutubeApi.class);
            i.putExtra("video_id",refined_id);
            if(playlist_url!=null)
                i.putExtra("video_url",playlist_url);
            startActivity(i);
        }

    }
}