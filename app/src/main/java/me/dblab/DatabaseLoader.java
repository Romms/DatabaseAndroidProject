package me.dblab;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import me.dblab.common.DatabaseSerializer;

interface DatabaseLoaderListener {
    void onDatabaseLoaded();
}

public class DatabaseLoader extends AsyncTask<URL, Integer, String> {
    private DatabaseLoaderListener mListener;

    public DatabaseLoader(DatabaseLoaderListener listener) {
        mListener = listener;
    }

    public void stop() {
        mListener = null;
    }

    public static String convertStreamToString(InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    @Override
    protected String doInBackground(URL... urls) {
        String responseString = null;
        try {
            HttpURLConnection conn = (HttpURLConnection) urls[0].openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            conn.connect();
            int response = conn.getResponseCode();
            Log.i("MAKSYM", "The response code is: " + response);
            InputStream inputStream = conn.getInputStream();

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                responseString = convertStreamToString(inputStream);
                Log.i("MAKSYM", String.valueOf(responseString.length()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseString;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Log.i("MAKSYM", result);
        try {
            DatabaseHolder.setDatabase(DatabaseSerializer.deserializeBase64(result));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (mListener != null) {
            mListener.onDatabaseLoaded();
        }
    }
}
