package intentservicefilmes.exemplo.com.intentservicefilmes.remote;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import intentservicefilmes.exemplo.com.intentservicefilmes.entity.Filme;
import intentservicefilmes.exemplo.com.intentservicefilmes.utils.ComunicacaoRest;

/**
 * Created by BPardini on 21/12/2016.
 */

public class FilmesPopularesIntentService extends IntentService {

    public FilmesPopularesIntentService(){
        super(FilmesPopularesIntentService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //Baixa a imagem e envia o resultado através do ResultReceiver para a MainActivity
        String url = intent.getStringExtra("url");
        final ResultReceiver receiver = intent.getParcelableExtra("receiver");
        Bundle bundle = new Bundle();

        try {
            Filme[] movies = ComunicacaoRest.downloadData(url);
            bundle.putParcelableArray("movies", movies);
            receiver.send(ComunicacaoRest.SUCCESS, bundle);
        } catch (Exception e) {
            receiver.send(ComunicacaoRest.ERROR, Bundle.EMPTY);
            e.printStackTrace();
        }
    }
}
