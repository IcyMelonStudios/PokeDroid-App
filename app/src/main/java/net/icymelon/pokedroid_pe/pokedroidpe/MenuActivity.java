package net.icymelon.pokedroid_pe.pokedroidpe;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.parse.ParseInstallation;


public class MenuActivity extends ActionBarActivity {
    boolean exitApp = false;
    InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        ParseInstallation.getCurrentInstallation().saveInBackground();

        launchInter();
        requestNewInterstitial();
    }

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }
        catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    public void click_play(View v){
        /*Intent intent = new Intent(this,LoadingActivity.class);
        startActivity(intent);*/

        if(appInstalledOrNot("com.mojang.minecraftpe"))
        {
            if (appInstalledOrNot("net.zhuoweizhang.mcpelauncher.pro") == true) {
                showPlayDialog();

            } else if (appInstalledOrNot("net.zhuoweizhang.mcpelauncher") == true) {
                showPlayDialog();
            } else {
                Toast.makeText(MenuActivity.this, "[ERROR] Block Launcher not found on this device. Download the latest version of it from Google Play.", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(MenuActivity.this, "[ERROR] Minecraft PE not found on this device. Download the latest version of it from Google Play.", Toast.LENGTH_SHORT).show();
        }

    }
    public void click_server(View v){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
    public void click_shop(View v){
        ErrorActivity.show(this,"Error","The page you are trying to access has not been added yet. Please wait for a future update to use this feature.");
    }
    public void click_links(View v){

        Intent intent = new Intent(this,AboutActivity.class);
        startActivity(intent);

    }
    public void click_donate(View v){

        ErrorActivity.show(this, "Error", "The page you are trying to access has not been added yet. Please wait for a future update to use this feature.");

    }


    public void showPlayDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setTitle("");
        builder.setItems(new CharSequence[]
                        {"Install mod", "Launch"},
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        switch (which) {
                            case 0:

                                Intent intent2 = new Intent(MenuActivity.this,LoadingActivity.class);
                                intent2.putExtra("PLAY_MODE",1);
                                startActivity(intent2);

                                break;
                            case 1:

                                Intent intent3 = new Intent(MenuActivity.this,LoadingActivity.class);
                                intent3.putExtra("PLAY_MODE",2);
                                startActivity(intent3);

                                break;
                        }
                    }
                });
        builder.create().show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu, menu);
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
