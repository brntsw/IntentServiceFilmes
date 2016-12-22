package intentservicefilmes.exemplo.com.intentservicefilmes.remote;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.ResultReceiver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import intentservicefilmes.exemplo.com.intentservicefilmes.entity.Movie;
import intentservicefilmes.exemplo.com.intentservicefilmes.utils.NetworkUtils;

/**
 * Created by BPardini on 21/12/2016.
 */

public class PopularMoviesIntentService extends IntentService {

    public PopularMoviesIntentService(){
        super(PopularMoviesIntentService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //Baixa a imagem e envia o resultado através do ResultReceiver para a MainActivity
        String url = intent.getStringExtra("url");
        final ResultReceiver receiver = intent.getParcelableExtra("receiver");
        Bundle bundle = new Bundle();

        try {
            Movie[] movies = NetworkUtils.downloadData(url);
            bundle.putParcelableArray("movies", movies);
            receiver.send(NetworkUtils.SUCCESS, bundle);
        } catch (Exception e) {
            receiver.send(NetworkUtils.ERROR, Bundle.EMPTY);
            e.printStackTrace();
        }
    }
}
