package net.icymelon.pokedroid_pe.pokedroidpe;

import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ViewSwitcher;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class LoadingActivity extends ActionBarActivity {
    boolean exitApp = false;
    InterstitialAd mInterstitialAd;
    private AdView mAdView;
    int playMode = 0;
    ImageSwitcher imgSwitcher;
    int[] screens = {R.drawable.screen1,R.drawable.screen2,R.drawable.screen3,R.drawable.screen4,R.drawable.screen5};
    int currPic = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        launchInter();
        requestNewInterstitial();

        mAdView = (AdView) findViewById(R.id.ad_view);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);


            imgSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitcher);
            imgSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
                @Override
                public View makeView() {
                    ImageView myView = new ImageView(getApplicationContext());
                    myView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    myView.setLayoutParams(new ImageSwitcher.LayoutParams(ImageSwitcher.LayoutParams.FILL_PARENT, ImageSwitcher.LayoutParams.FILL_PARENT));
                    return myView;
                }
            });
            imgSwitcher.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left));
            imgSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right));
            imgSwitcher.setImageResource(screens[0]);

            updateDisplay();


            File temp = new File(Environment.getExternalStorageDirectory() + "/PokeDroidPE/v1_0_0.zip");
            playMode = getIntent().getIntExtra("PLAY_MODE", 0);
            //temp.mkdirs();
            try {
                temp.createNewFile();
            } catch (IOException err) {
            }
            ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar);

            pb.setIndeterminate(true);


            new DownloadFileFromURL(this, pb, playMode).execute("http://sys81184.leet.cc/versions/v1_0_0.zip", temp.getAbsolutePath());
            Log.i("DownloadFileFromURL", Integer.toString(playMode));
/*http://sys81184.leet.cc/versions/v1_0_0.zip*/

    }
    public void updateImg(){
        imgSwitcher.setImageResource(screens[currPic]);
    }

    private void updateDisplay() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {

                currPic += 1;
                if (currPic > screens.length - 1) {
                    currPic = 0;
                }
                LoadingActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateImg();
                    }
                });

            }

        }, 3000, 3000);//Update text every second
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_loading, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    /** Called when returning to the activity */
    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    /** Called before the activity is destroyed */
    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }


    private void launchInter()
    {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-6403892120774730/5826005201");

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                showAdInter();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {

            }

            @Override
            public void onAdClosed() {
                if (exitApp)
                    finish();
            }

        });


    }

    private void showAdInter()
    {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
        else
        {
            Log.d("", "Failed to load Interstitial Ad");
        }
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .build();

        mInterstitialAd.loadAd(adRequest);
    }

}
