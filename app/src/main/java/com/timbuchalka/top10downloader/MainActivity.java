package com.timbuchalka.top10downloader;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: starting Asynctask");
        DownloadData downloadData = new DownloadData();
        downloadData.execute("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml");
        Log.d(TAG, "onCreate: done");

    }

    private class DownloadData extends AsyncTask<String, Void, String> {
        private static final String TAG = "DownloadData";

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG, "onPostExecute: parameter is " + s);
        }

        @Override
        protected String doInBackground(String... urlPath) {
            Log.d(TAG, "doInBackground: starts with " + urlPath[0]);
            String rssFeed = downloadXML(urlPath[0]);
            if(rssFeed == null) {
                Log.e(TAG, "doInBackground: Error downloading");
            }
            return rssFeed;
        }

        private String downloadXML(String urlPath) {
            StringBuilder stringBuilder = new StringBuilder();

            try {
                URL url = new URL(urlPath);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                int response = httpURLConnection.getResponseCode();
                Log.d(TAG, "downloadXML: The response code was " + response);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

                int charsRead;
                char[] maxChar = new char[500];
                while(true) {
                    charsRead = bufferedReader.read(maxChar);
                    if(charsRead < 0) {
                        break;
                    }
                    if(charsRead > 0) {
                        stringBuilder.append(String.copyValueOf(maxChar, 0, charsRead));
                    }
                }
                bufferedReader.close();
                return stringBuilder.toString();
            } catch(MalformedURLException e) {
                Log.e(TAG, "downloadXML: Invalid URL " + e.getMessage());
            } catch(IOException e) {
                Log.e(TAG, "downloadXML: IO Exception reading data: " + e.getMessage());
            } catch(SecurityException e) {
                Log.e(TAG, "downloadXML: Security Exception.  Needs permisson? " + e.getMessage());
//                e.printStackTrace();
            }

            return null;
        }
    }
}


















