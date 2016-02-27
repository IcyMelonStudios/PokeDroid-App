package net.icymelon.pokedroid_pe.pokedroidpe;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.loopj.android.http.HttpGet;




import org.apache.commons.io.FileUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.core.ZipFile;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.entity.BufferedHttpEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

/**
 * Created by aditya on 9/20/2015.
 */
public class ServerManager {





}


class GetStringFromUrl extends AsyncTask<String, Void, String> {

    ProgressDialog dialog ;
    String url;
    Context ctx;
    TextView textView;

    public GetStringFromUrl(String url, Context ctx, TextView textView){
        this.url = url;
        this.ctx = ctx;
        this.textView = textView;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        // show progress dialog when downloading
        //RegisterActivity ra = new RegisterActivity();


        //dialog = ProgressDialog.show(ctx, null, "Downloading...");
    }

    @Override
    protected String doInBackground(String... params) {

        // @BadSkillz codes with same changes
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();

            BufferedHttpEntity buf = new BufferedHttpEntity(entity);

            InputStream is = buf.getContent();

            BufferedReader r = new BufferedReader(new InputStreamReader(is));

            StringBuilder total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line + "\n");
            }
            String result = total.toString();
            Log.i("Get URL", "Downloaded string: " + result);
            return result;
        } catch (Exception e) {
            Log.e("Get Url", "Error in downloading: " + e.toString());
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        // TODO change text view id for yourself





        // show result in textView


        // close progresses dialog

    }
}

   class Decompress {
    private String _zipFile;
    private String _location;
    Context context;

    public Decompress(String zipFile, String location, Context ctx) {
        _zipFile = zipFile;
        _location = location;
        context = ctx;

        _dirChecker("");
    }

    public void unzip() {
        try  {
            FileInputStream fin = new FileInputStream(_zipFile);
            ZipInputStream zin = new ZipInputStream(fin);

            byte b[] = new byte[1024];

            ZipEntry ze = null;
            while ((ze = zin.getNextEntry()) != null) {
                Log.v("Decompress", "Unzipping " + ze.getName());

                if(ze.isDirectory()) {
                    _dirChecker(ze.getName());
                } else {
                    FileOutputStream fout = new FileOutputStream(_location + ze.getName());

                    BufferedInputStream in = new BufferedInputStream(zin);
                    BufferedOutputStream out = new BufferedOutputStream(fout);

                    int n;
                    while ((n = in.read(b,0,1024)) >= 0) {
                        out.write(b,0,n);
                    }

                    zin.closeEntry();
                    out.close();
                }

            }
            zin.close();


        } catch(Exception e) {
            Log.e("Decompress", "unzip", e);
        }

    }

    private void _dirChecker(String dir) {
        File f = new File(_location + dir);

        if(!f.isDirectory()) {
            f.mkdirs();
        }
    }}




class Unzipper extends AsyncTask<String, Void, String> {

    Activity context;
    int mode;

    public Unzipper(Activity ctx,int playMode){
        context = ctx;
        mode = playMode;
        Log.i("Unzipper",Integer.toString(mode));
    }

    @Override
    protected String doInBackground(String... params) {

        if(mode==0 || mode==1) {

            File file = new File(Environment.getExternalStorageDirectory(), "PokeDroidPE/versions/v1_0_0.zip");
            File file2 = new File(Environment.getExternalStorageDirectory(), "PokeDroidPE/versions/v1_0_0");
            String filePath = Environment.getExternalStorageDirectory().toString() + "/PokeDroidPE/versions/v1_0_0.zip";

            file2.mkdirs();


            try {
                ZipFile zipFile = new ZipFile(filePath);
                zipFile.extractAll(file2.getAbsolutePath() + "/");
            } catch (ZipException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        Intent i = new Intent(context, MenuActivity.class);
        context.startActivity(i);


        if(mode==0){
            PackageManager manager = context.getPackageManager();
            Intent intent = manager.getLaunchIntentForPackage(Utils.getBlockLauncherPackageName());
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            context.startActivity(intent);

            File scriptFile = new File(Environment.getExternalStorageDirectory(), "PokeDroidPE/versions/v1_0_0/build/script.js");
            Intent scriptIntent = new Intent();
            scriptIntent.setPackage(Utils.getBlockLauncherPackageName());
            scriptIntent.setAction("net.zhuoweizhang.mcpelauncher.action.IMPORT_SCRIPT");
            scriptIntent.setDataAndType(Uri.fromFile(scriptFile), "text/javascript");
            context.startActivity(scriptIntent);
            Log.i("PokeDroid PE", "Started Script Import");
            File tpFile = new File(Environment.getExternalStorageDirectory(), "PokeDroidPE/versions/v1_0_0/build/textures.zip");
            Intent tpIntent = new Intent();
            tpIntent.setPackage(Utils.getBlockLauncherPackageName());
            tpIntent.setAction("net.zhuoweizhang.mcpelauncher.action.SET_TEXTUREPACK");
            tpIntent.setDataAndType(Uri.fromFile(tpFile), "application/zip");
            context.startActivity(tpIntent);
            Log.i("PokeDroid PE", "Started Texture Import");

        }
        else if(mode==1){
            File scriptFile = new File(Environment.getExternalStorageDirectory(), "PokeDroidPE/versions/v1_0_0/build/script.js");
            Intent scriptIntent = new Intent();
            scriptIntent.setPackage(Utils.getBlockLauncherPackageName());
            scriptIntent.setAction("net.zhuoweizhang.mcpelauncher.action.IMPORT_SCRIPT");
            scriptIntent.setDataAndType(Uri.fromFile(scriptFile), "text/javascript");
            context.startActivity(scriptIntent);
            Log.i("PokeDroid PE", "Started Script Import");
            File tpFile = new File(Environment.getExternalStorageDirectory(), "PokeDroidPE/versions/v1_0_0/build/textures.zip");
            Intent tpIntent = new Intent();
            tpIntent.setPackage(Utils.getBlockLauncherPackageName());
            tpIntent.setAction("net.zhuoweizhang.mcpelauncher.action.SET_TEXTUREPACK");
            tpIntent.setDataAndType(Uri.fromFile(tpFile), "application/zip");
            context.startActivity(tpIntent);
            Log.i("PokeDroid PE", "Started Texture Import");
        }
        else{
            PackageManager manager = context.getPackageManager();
            Intent intent = manager.getLaunchIntentForPackage(Utils.getBlockLauncherPackageName());
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            context.startActivity(intent);
        }







    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onProgressUpdate(Void... values) {
    }
}




class DownloadFileFromURL extends AsyncTask<String, String, String> {

    Activity context;
    ProgressBar progressBar;
    int mode;

    /**
     * Before starting background thread
     * Show Progress Bar Dialog
     * */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    public DownloadFileFromURL(Activity ctx, ProgressBar pb, int playMode){
        context = ctx;
        progressBar = pb;
        mode = playMode;
        Log.i("DownloadFileFromURL",Integer.toString(mode));
    }



    /**
     * Downloading file in background thread
     * */
    @Override
    protected String doInBackground(String... params) {

        if(mode==0 || mode==1) {

            try {

                URL url = new URL("https://github.com/IcyMelonStudios/PokeDroidPE/raw/master/builds/v1_0_0.zip");
                //https://www.dropbox.com/s/e7xoc5zdx0l1pmk/v1_0_0.zip?dl=1
                File SDCardRoot = Environment.getExternalStorageDirectory();
                File file = new File(SDCardRoot, "PokeDroidPE/versions/v1_0_0.zip");
                File file2 = new File(SDCardRoot, "PokeDroidPE/versions/");

                file2.mkdirs();
                file.createNewFile();

                FileUtils.copyURLToFile(url, file);


            } catch (MalformedURLException err) {
            } catch (IOException err) {
            }

        }
        return null;
    }

    /**
     * Updating progress bar
     * */
    protected void onProgressUpdate(String... progress) {
        // setting progress percentage

    }


    /**
     * After completing background task
     * Dismiss the progress dialog
     * **/
    @Override
    protected void onPostExecute(String file_url) {
        new Unzipper(context,mode).execute();
    }

}
