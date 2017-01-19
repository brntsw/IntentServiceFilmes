package intentservicefilmes.exemplo.com.intentservicefilmes.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import intentservicefilmes.exemplo.com.intentservicefilmes.entity.Filme;

/**
 * Created by BPardini on 22/12/2016.
 */

public class ComunicacaoRest {

    public static final int SUCCESS = 200;
    public static final int ERROR = 500;
    public static final int NOT_FOUND = 404;

        public static final String BASE_URL = "https://api.themoviedb.org/3";
    public static final String API_KEY = "3d62bcc6c5dec00b1e0eb1532ad18bb1";
    public static String URL_POPULAR_MOVIES = BASE_URL + "/movie/popular?api_key=" + API_KEY;

    public static Filme[] downloadData(String requestUrl) throws Exception {
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

    private static Filme[] parseResult(String result){
        Filme[] arrayMovies = null;

        try {
            JSONObject response = new JSONObject(result);
            JSONArray arrayResults = response.getJSONArray("results");

            arrayMovies = new Filme[arrayResults.length()];

            for(int i = 0; i < arrayResults.length(); i++){
                Filme filme = new Filme();

                JSONObject objMovie = arrayResults.getJSONObject(i);
                filme.setId(objMovie.getInt("id"));
                filme.setOriginalTitle(objMovie.getString("original_title"));
                filme.setOverview(objMovie.getString("overview"));

                String posterPath = objMovie.getString("poster_path");

                Bitmap imgPoster = downloadImage("https://image.tmdb.org/t/p/w500/" + posterPath);
                filme.setPoster(imgPoster);

                filme.setReleaseDate(objMovie.getString("release_date"));

                Log.d("Title", filme.getOriginalTitle());
                Log.d("Overview", filme.getOverview());
                Log.d("Poster path", BASE_URL + posterPath);
                Log.d("Release date", filme.getReleaseDate());

                arrayMovies[i] = filme;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return arrayMovies;
    }

}
