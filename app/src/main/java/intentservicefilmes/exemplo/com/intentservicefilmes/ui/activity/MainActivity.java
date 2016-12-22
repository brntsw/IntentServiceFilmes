package intentservicefilmes.exemplo.com.intentservicefilmes.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import intentservicefilmes.exemplo.com.intentservicefilmes.R;
import intentservicefilmes.exemplo.com.intentservicefilmes.adapter.MovieAdapter;
import intentservicefilmes.exemplo.com.intentservicefilmes.entity.Movie;
import intentservicefilmes.exemplo.com.intentservicefilmes.remote.PopularMoviesIntentService;
import intentservicefilmes.exemplo.com.intentservicefilmes.utils.MoviesResultReceiver;
import intentservicefilmes.exemplo.com.intentservicefilmes.utils.NetworkUtils;

public class MainActivity extends AppCompatActivity implements MoviesResultReceiver.Receiver {
    private ProgressBar pd;
    private RecyclerView recyclerMovies;
    private MoviesResultReceiver resultReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultReceiver = new MoviesResultReceiver(new Handler());
        pd = (ProgressBar) findViewById(R.id.downloadPD);
        recyclerMovies = (RecyclerView) findViewById(R.id.recycler_movies);

        resultReceiver.setReceiver(this);
        Intent intent = new Intent(Intent.ACTION_SYNC, null, this, PopularMoviesIntentService.class);

        intent.putExtra("url", NetworkUtils.URL_POPULAR_MOVIES);
        intent.putExtra("receiver", resultReceiver);

        startService(intent);

        pd.setVisibility(View.VISIBLE);
        pd.setIndeterminate(true);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode){
            case NetworkUtils.SUCCESS:
                pd.setVisibility(View.GONE);

                List<Movie> movies = new ArrayList<>();

                Parcelable[] parcelableMovies = resultData.getParcelableArray("movies");

                if(parcelableMovies != null) {
                    for (Parcelable parcelableMovie : parcelableMovies) {
                        Movie movie = (Movie) parcelableMovie;

                        movies.add(movie);
                    }
                }

                recyclerMovies.setLayoutManager(new LinearLayoutManager(this));
                recyclerMovies.setItemAnimator(new DefaultItemAnimator());
                recyclerMovies.setAdapter(new MovieAdapter(movies));

                break;
            case NetworkUtils.ERROR:
                Log.d("ERRO", "Ocorreu um erro");
                pd.setVisibility(View.GONE);
                break;
        }
    }
}
