package com.tama.chat.gcm;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.tama.chat.BuildConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Locale;

/**
 * Created by Avo on 27-Apr-17.
 */

public class NearObjectsFinder {
    private static final String DIRECTION_URL_API = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
    private static final String TAG = "myLogs";
    private NearObjectsFinderListener listener;
    private Location location;
    private String type;
    private String language;
    private String title;

    public NearObjectsFinder(NearObjectsFinderListener listener, Location location, String type, String title) {
        this.listener = listener;
        this.location = location;
        this.type = type;
        this.title = title;
        language = Locale.getDefault().getLanguage();// getDisplayLanguage();
    }

    public void execute() throws UnsupportedEncodingException {
        new NearObjectsFinder.DownloadRawData().execute(createUrl());
    }

    private String createUrl() throws UnsupportedEncodingException {
        String urlLat = URLEncoder.encode(String.valueOf(location.getLatitude()), "utf-8");
        String urlLng = URLEncoder.encode(String.valueOf(location.getLongitude()), "utf-8");
        String location = urlLat+"," + urlLng;
        String search = "";
        String pagetoken = "";
        Log.d(TAG,"location = " + location);
        Log.d(TAG,"DIRECTION_URL_API = "
                + DIRECTION_URL_API + "location=" + location + "&radius=1000&types=" + type + "&language=" + language + "&key=" + BuildConfig.GOOGLE_API_KEY);


        return DIRECTION_URL_API + "location=" + location + "&radius=1000&types=" + type + "&keyword=&language=" + language + "&name=" + search + "&pagetoken=" + pagetoken + "&key=" + BuildConfig.GOOGLE_API_KEY;
    }

    private class DownloadRawData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String link = params[0];
            try {
                URL url = new URL(link);
                InputStream is = url.openConnection().getInputStream();
                StringBuffer buffer = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String res) {
            listener.onNearObjectsFinderSuccess(res, title);
        }
    }



}
