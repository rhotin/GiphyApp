package com.appdeveloper.rh.giphyapp;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Roman on 11/4/2017.
 */

public class DownloadGifs extends AsyncTask<URL, Integer, Void> {

    ArrayList<GifObject> gifsArrayList = new ArrayList<>();
    Communicator context;

    DownloadGifs(TrendingFragment c) {
        this.context = (Communicator) c;
    }

    DownloadGifs(FavouriteFragment c) {
        this.context = (Communicator) c;
    }

    @Override
    protected Void doInBackground(URL... urls) {
        try {
            URL theUrl = new URL(urls[0].toString());
            BufferedReader reader = new BufferedReader
                    (new InputStreamReader(theUrl.openConnection().getInputStream(), "UTF-8"));
            String gifs_json = reader.readLine();

            Log.e("json", theUrl + gifs_json);

            JSONObject gifsObject = new JSONObject(gifs_json);
            JSONArray data_arr = gifsObject.getJSONArray("data");
            int totalGifs = data_arr.length();

            for (int i = 0; i < totalGifs; i++) {
                String gifTitle = data_arr.getJSONObject(i).getString("title");
                String gifURL = data_arr.getJSONObject(i).getJSONObject("images").
                        getJSONObject("fixed_height").getString("url");

             /*   URL downloadURL = new URL (gifURL);
                HttpURLConnection conn = (HttpURLConnection) downloadURL.openConnection();
                InputStream inputStream = conn.getInputStream();
                Bitmap bmp = BitmapFactory.decodeStream(inputStream);
                */

                GifObject obj = new GifObject(gifTitle, gifURL);// bmp);
                gifsArrayList.add(obj);
                Log.e("Obj",obj.toString());
                publishProgress((int) (((i + 1.0) / totalGifs) * 100.0));
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        context.updateProgressTo(values[0]);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        context.updateUI(gifsArrayList);
    }

    interface Communicator {
        public void updateProgressTo(int progress);

        public void updateUI(ArrayList<GifObject> photosArrayList);
    }
}
