package com.sairajen.saibabamantra;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private Button startChantBtn;
    private EditText editText;
    private TextView readMoreTxt;
    private AdView adView;

    private int count = 0;

    private final static String INTENT_COUNT = "count";

    private SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();

        initViews();

        sharedPref = new SharedPref(MainActivity.this);
        if (sharedPref.isFirstTime()) {
            sharedPref.setFirstTime(false);
            setNotification();
        }

        MobileAds.initialize(getApplicationContext(),getResources().getString(R.string.banner_ad));
        adView = (AdView) findViewById(R.id.adViewMainActivity);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        startChantBtn.setOnClickListener(this);
        readMoreTxt.setOnClickListener(this);

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    startChant();
                }
                return false;
            }
        });

    }

    private void startChant() {
        if (editText.getText().toString().trim() != null && !editText.getText().toString().trim().equals("")) {
            count = Integer.parseInt(editText.getText().toString().trim());
            if (count>0 && count < 1000) {
                Intent intent = new Intent(MainActivity.this, MediaPlayerActivity.class);
                intent.putExtra(INTENT_COUNT, count);
                startActivity(intent);
            } else {
                Helper.showDialog(MainActivity.this,"ERROR","please enter count value betweeen 0 and 1000","OK",android.R.drawable.stat_sys_warning);
            }
        } else {
            Helper.showDialog(MainActivity.this,"ERROR","please enter valid count value before proceeding","OK",android.R.drawable.stat_sys_warning);
        }
    }

    private void initViews() {
        startChantBtn = (Button) findViewById(R.id.startChantBtn);
        editText = (EditText) findViewById(R.id.editText);
        readMoreTxt = (TextView) findViewById(R.id.readMoreTextView);

    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        MainActivity.this.setTitle("");
        toolbar.findViewById(R.id.toolbar_title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.openLink(MainActivity.this,"http://www.saihere.com/more-app.html");
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startChantBtn:
                startChant();
                break;
            case R.id.readMoreTextView:
                startActivity(new Intent(MainActivity.this,About.class));
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
                Helper.shareApp(MainActivity.this);
                break;
            case R.id.menu_about_us:
                startActivity(new Intent(MainActivity.this,About.class));
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
        count = 0;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adView != null) {
            adView.destroy();
        }
    }

    private void setNotification() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,6);
        calendar.set(Calendar.MINUTE,30);

        Intent intent = new Intent(getApplicationContext(),NotificationReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),100,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

}
