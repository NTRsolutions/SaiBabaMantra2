package com.sairajen.saibabamantra;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class About extends AppCompatActivity {

    private Toolbar toolbar;
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initToolbar();

        MobileAds.initialize(getApplicationContext(),getResources().getString(R.string.banner_ad));
        adView = (AdView) findViewById(R.id.adViewAboutActivity);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        ((TextView) findViewById(R.id.textWebLink)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.openLink(About.this,"http://www.saihere.com");
            }
        });

    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        About.this.setTitle("About");
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
