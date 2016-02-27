package net.icymelon.pokedroid_pe.pokedroidpe;

import android.content.pm.PackageManager;

/**
 * Created by aditya on 1/18/2016.
 */
public class Utils {
    public static String getBlockLauncherPackageName(){
        String pkg;
        if(isAppInstalled("net.zhuoweizhang.mcpelauncher.pro")){
            pkg = "net.zhuoweizhang.mcpelauncher.pro";
        }
        else if(isAppInstalled("net.zhuoweizhang.mcpelauncher")){
            pkg = "net.zhuoweizhang.mcpelauncher";
        }
        else{
            pkg = null;
            ErrorActivity.show(App.getContext(),"Error","Block Launcher was not found on this device. Please download the latest version from Google Play.");
        }
        return pkg;
    }

    public static boolean isAppInstalled(String packageName) {
        PackageManager pm = App.getContext().getPackageManager();
        boolean installed = false;
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }
        return installed;
    }
}
