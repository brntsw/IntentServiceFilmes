package intentservicefilmes.exemplo.com.intentservicefilmes.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import intentservicefilmes.exemplo.com.intentservicefilmes.entity.Movie;

/**
 * Created by BPardini on 22/12/2016.
 */

public class NetworkUtils {

    public static final int SUCCESS = 200;
    public static final int ERROR = 500;
    public static final int NOT_FOUND = 404;

    public static final String BASE_URL = "https://api.themoviedb.org/3";
    public static final String API_KEY = "3d62bcc6c5dec00b1e0eb1532ad18bb1";
    public static String URL_POPULAR_MOVIES = BASE_URL + "/movie/popular?api_key=" + API_KEY;

    public static Movie[] downloadData(String requestUrl) throws Exception {
        InputStream inputStream;
        HttpURLConnection urlConnection;

        /* forming th java.net.URL object */
        URL url = new URL(requestUrl);
        urlConnection = (HttpURLConnection) url.openConnection();

        /* optional request header */
        urlConnection.setRequestProperty("Content-Type", "application/json");

        /* optional request header */
        urlConnection.setRequestProperty("Accept", "application/json");

        /* for Get request */
        urlConnection.setRequestMethod("GET");
        int statusCode = urlConnection.getResponseCode();

        /* 200 represents HTTP OK */
        if (statusCode == 200) {
            inputStream = new BufferedInputStream(urlConnection.getInputStream());
            String response = convertInputStreamToString(inputStream);
            return parseResult(response);
        } else {
            throw new Exception("Failed to fetch data!!");
        }
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        String result = "";

        while ((line = bufferedReader.readLine()) != null) {
            result += line;
        }

            /* Close Stream */
        if (null != inputStream) {
            inputStream.close();
        }

        return result;
    }

    private static Bitmap downloadImage(String url){
        Bitmap bmp = null;

        try{
            URL downloadURL = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) downloadURL.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream input = conn.getInputStream();
            bmp = BitmapFactory.decodeStream(input);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return bmp;
    }

    private static Movie[] parseResult(String result){
        Movie[] arrayMovies = null;

        try {
            JSONObject response = new JSONObject(result);
            JSONArray arrayResults = response.getJSONArray("results");

            arrayMovies = new Movie[arrayResults.length()];

            for(int i = 0; i < arrayResults.length(); i++){
                Movie movie = new Movie();

                JSONObject objMovie = arrayResults.getJSONObject(i);
                movie.setId(objMovie.getInt("id"));
                movie.setOriginalTitle(objMovie.getString("original_title"));
                movie.setOverview(objMovie.getString("overview"));

                String posterPath = objMovie.getString("poster_path");

                Bitmap imgPoster = downloadImage("https://image.tmdb.org/t/p/w500/" + posterPath);
                movie.setPoster(imgPoster);

                movie.setReleaseDate(objMovie.getString("release_date"));

                Log.d("Title", movie.getOriginalTitle());
                Log.d("Overview", movie.getOverview());
                Log.d("Poster path", BASE_URL + posterPath);
                Log.d("Release date", movie.getReleaseDate());

                arrayMovies[i] = movie;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return arrayMovies;
    }

}
