package com.sairajen.saibabamantra;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

public class MediaPlayerActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private ImageView playPauseButton, stopButton;
    private TextView txtCount;
    private AdView adView;

    private InterstitialAd interstitialAd;

    private MediaPlayer mp;

    private int count = 0;
    private int maxCount;

    private final static String INTENT_COUNT = "count";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);
        initToolbar();

        initViews();

        MobileAds.initialize(getApplicationContext(),getResources().getString(R.string.banner_ad));
        adView = (AdView) findViewById(R.id.adViewMediaPlayerActivity);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        prepareInterstitialAds();

        if (getIntent().hasExtra(INTENT_COUNT)) {
            maxCount = getIntent().getExtras().getInt(INTENT_COUNT);
        }

        txtCount.setText("Chanting counts : "+String.valueOf(maxCount));

        playPauseButton.setImageResource(R.drawable.pause);
        mp = MediaPlayer.create(MediaPlayerActivity.this,R.raw.mantra);
        mp.start(); //this method will play the music

        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (count<maxCount) {
                    count++;
                    mp.seekTo(0);
                    mp.start();
                    txtCount.setText("Chanting counts : "+String.valueOf(maxCount - count));
                }
            }
        });

        playPauseButton.setOnClickListener(this);
        stopButton.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        if (!showInterstitialAd()) {
            super.onBackPressed();
        }
    }

    private void prepareInterstitialAds() {
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.interstitial_ad));
        AdRequest adRequest2 = new AdRequest.Builder().build();
        interstitialAd.loadAd(adRequest2);
    }

    private boolean showInterstitialAd() {
        if (interstitialAd != null && interstitialAd.isLoaded()) {
            interstitialAd.show();
            mp.stop();
            mp.release();
            interstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    MediaPlayerActivity.this.finish();
                }
            });
            return  true;
        }
        return  false;
    }

    private void initViews() {
        playPauseButton = (ImageView) findViewById(R.id.playPauseBtn);
        stopButton = (ImageView) findViewById(R.id.stopBtn);
        txtCount = (TextView) findViewById(R.id.txtCount);
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        MediaPlayerActivity.this.setTitle("");
        toolbar.findViewById(R.id.toolbar_title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.openLink(MediaPlayerActivity.this,"http://www.saihere.com/more-app.html");
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.playPauseBtn:
                if (mp.isPlaying()) {
                    mp.pause();
                    playPauseButton.setImageResource(R.drawable.play);
                } else {
                    mp.start();
                    playPauseButton.setImageResource(R.drawable.pause);
                }
                break;
            case R.id.stopBtn:
                mp.seekTo(0);
                mp.pause();
                playPauseButton.setImageResource(R.drawable.play);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_share:
                Helper.shareApp(MediaPlayerActivity.this);
                break;
            case R.id.menu_about_us:
                startActivity(new Intent(MediaPlayerActivity.this,About.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        if (adView != null)
            adView.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (adView != null) {
            adView.resume();
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adView != null) {
            adView.destroy();
        }
    }

}
