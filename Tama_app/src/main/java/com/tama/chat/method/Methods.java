package com.tama.chat.method;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.tama.chat.utils.image.ImageLoaderUtils;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;

/**
 * Created by Avo on 20-Mar-17.
 */

public class Methods {
    private static final String PREF = "pref";
    private static final String TAG = "myLogs";

    public static int convertDpToPixel(int dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int px = (int)(dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    public static Bitmap retriveVideoFrameFromVideo(String videoPath) throws Throwable
    {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try
        {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime();

        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable("Exception in retriveVideoFrameFromVideo(String videoPath)" + e.getMessage());

        }
        finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }

    public static int dpToPixel(Context context,int dp){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int px = (int)(dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    public static void loadImageByUri(String logoUrl,final ImageView v) {
        ImageLoader.getInstance().loadImage(logoUrl,
            ImageLoaderUtils.UIL_USER_AVATAR_DISPLAY_OPTIONS,
            new SimpleImageLoadingListener(){
              @Override
              public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                super.onLoadingComplete(imageUri, view, loadedImage);
                v.setImageBitmap(loadedImage);
              }
            });
    }

//    public static Uri getImageUri(Context inContext, Bitmap inImage) {
    public static String getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "ankap.jpg", null);
//        return Uri.parse(path);
        return path;
    }

    public static void setBitmapToExternalStorag(Context context, Bitmap bitmap){
        try {

            File cachePath = new File(context.getCacheDir(), "images");
            cachePath.mkdirs(); // don't forget to make the directory
            FileOutputStream stream = new FileOutputStream(cachePath + "/image.png"); // overwrites this image every time
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getBitmapUrlFromExternalStorag(Context context){
        File imagePath = new File(context.getCacheDir(), "images");
        File newFile = new File(imagePath, "image.png");
        Uri contentUri = FileProvider.getUriForFile(context, "com.example.myapp.fileprovider", newFile);
        return contentUri.getPath();
    }

    public static File createImageFileFromView(String name, Bitmap image) {
        File imageFile;
        File imagePath = new File(Environment.getExternalStorageDirectory() + "/" + name + "/");
        if (!imagePath.exists())
            imagePath.mkdir();
        imageFile = new File(imagePath, name+".jpg");
        try {

            FileOutputStream fos = new FileOutputStream(imageFile);
            image.compress(Bitmap.CompressFormat.JPEG,100, fos);
            fos.close();
        } catch (IOException e) {
            Log.d(TAG,e.getMessage());
        }
        return imageFile;
    }

    public static Uri createUriFromBitmap(Context context, Bitmap bitmap){
        try {
            File cachePath = new File(context.getCacheDir(), "images");
            cachePath.mkdirs(); // don't forget to make the directory
            FileOutputStream stream = new FileOutputStream(cachePath + "/image.png"); // overwrites this image every time
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        File imagePath = new File(context.getCacheDir(), "images");
        File newFile = new File(imagePath, "image.png");
        return FileProvider.getUriForFile(context, "com.quickblox.q_municate.fileprovider", newFile);
    }

    public static String getStringFromFile (File fl) throws Exception {
//        File fl = new File(filePath);
        FileInputStream fin = new FileInputStream(fl);
        String ret = convertStreamToString(fin);
        //Make sure you close all streams.
        fin.close();
        return ret;
    }

    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    public static String makeBitmapPath(Context inContext,Bitmap bmp){
        // TimeStamp
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        String path = Environment.getExternalStorageDirectory().toString();
        OutputStream fOut = null;
        File file = new File(path, "Photo"+ "5555" +".jpg"); // the File to save to
        try {
            fOut = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close(); // do not forget to close the stream

            MediaStore.Images.Media.insertImage(inContext.getContentResolver(),file.getAbsolutePath(),file.getName(),
                    file.getName());
        } catch (IOException e){
            // whatever
        }
        return file.getPath();
    }

    public static void setValueToPref(Context context, String key, String value, String name){
        SharedPreferences sPref = context.getSharedPreferences(name,Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(key, value);
        ed.apply();
    }

    public static String getValueFromPref(Context context, String key, String name){
        SharedPreferences sPref = context.getSharedPreferences(name,Context.MODE_PRIVATE);
        return sPref.getString(key,"empty");
    }

    public static void putBitmapInDiskCache(Context context, String url, Bitmap avatar) {
        // Create a path pointing to the system-recommended cache dir for the app, with sub-dir named
        // thumbnails
        File cacheDir = new   File(context.getCacheDir(), "thumbnails");
        // Create a path in that dir for a file, named by the default hash of the url
        File cacheFile = new File(cacheDir, url);
        try {
            // Create a file at the file path, and open it for writing obtaining the output stream
            cacheFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(cacheFile);
            // Write the bitmap to the output stream (and thus the file) in PNG format (lossless compression)
            avatar.compress(Bitmap.CompressFormat.PNG, 100, fos);
            // Flush and close the output stream
            fos.flush();
            fos.close();
        } catch (Exception e) {
            // Log anything that might go wrong with IO to file
        }
    }
}
