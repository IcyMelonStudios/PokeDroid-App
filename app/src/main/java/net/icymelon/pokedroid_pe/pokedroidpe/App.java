package net.icymelon.pokedroid_pe.pokedroidpe;

import android.app.Application;
import android.content.Context;

import com.parse.Parse;

/**
 * Created by aditya on 10/25/2015.
 */
public class App extends Application{
    static Context ctx;
    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        ctx = getApplicationContext();

        // Register any ParseObject subclass. Must be done before calling Parse.initialize()
        //ParseObject.registerSubclass(RegisterActivity.class);


        Parse.initialize(this, "bDYkTKODvWj85KbPa6UbeDUAA4WJBH9RMsqr2Z45", "eGjsf6WLWmEgCVmGJJWetLS6W63plGMkLszICkCQ");
    }
    public static Context getContext(){
        return ctx;
    }
}
