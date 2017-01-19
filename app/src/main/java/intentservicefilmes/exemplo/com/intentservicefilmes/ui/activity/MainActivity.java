package intentservicefilmes.exemplo.com.intentservicefilmes.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import intentservicefilmes.exemplo.com.intentservicefilmes.R;
import intentservicefilmes.exemplo.com.intentservicefilmes.adapter.FilmeAdapter;
import intentservicefilmes.exemplo.com.intentservicefilmes.entity.Filme;
import intentservicefilmes.exemplo.com.intentservicefilmes.remote.FilmesPopularesIntentService;
import intentservicefilmes.exemplo.com.intentservicefilmes.receiver.FilmesResultReceiver;
import intentservicefilmes.exemplo.com.intentservicefilmes.utils.ComunicacaoRest;

public class MainActivity extends AppCompatActivity implements FilmesResultReceiver.Receiver {
    private ProgressBar pd;
    private RecyclerView recyclerMovies;
    private FilmesResultReceiver resultReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultReceiver = new FilmesResultReceiver(new Handler());
        pd = (ProgressBar) findViewById(R.id.downloadPD);
        recyclerMovies = (RecyclerView) findViewById(R.id.recycler_movies);

        resultReceiver.setReceiver(this);
        Intent intent = new Intent(Intent.ACTION_SYNC, null, this, FilmesPopularesIntentService.class);

        intent.putExtra("url", ComunicacaoRest.URL_POPULAR_MOVIES);
        intent.putExtra("receiver", resultReceiver);

        startService(intent);

        pd.setVisibility(View.VISIBLE);
        pd.setIndeterminate(true);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode){
            case ComunicacaoRest.SUCCESS:
                pd.setVisibility(View.GONE);

                List<Filme> movies = new ArrayList<>();

                Parcelable[] parcelableMovies = resultData.getParcelableArray("movies");

                if(parcelableMovies != null) {
                    for (Parcelable parcelableMovie : parcelableMovies) {
                        Filme filme = (Filme) parcelableMovie;

                        movies.add(filme);
                    }
                }

                recyclerMovies.setLayoutManager(new LinearLayoutManager(this));
                recyclerMovies.setItemAnimator(new DefaultItemAnimator());
                recyclerMovies.setAdapter(new FilmeAdapter(movies));

                break;
            case ComunicacaoRest.ERROR:
                Log.d("ERRO", "Ocorreu um erro");
                pd.setVisibility(View.GONE);
                break;
        }
    }
}
