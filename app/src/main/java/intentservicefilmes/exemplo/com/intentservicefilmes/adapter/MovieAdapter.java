package intentservicefilmes.exemplo.com.intentservicefilmes.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import intentservicefilmes.exemplo.com.intentservicefilmes.R;
import intentservicefilmes.exemplo.com.intentservicefilmes.entity.Movie;

/**
 * Created by BPardini on 22/12/2016.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private List<Movie> movies;

    public MovieAdapter(List<Movie> movies){
        this.movies = movies;
    }

    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MovieAdapter.ViewHolder holder, int position) {
        Movie movie = movies.get(position);
        if(movie != null){
            holder.apply(movie);
        }
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imgMovie;
        TextView tvTitle;
        TextView tvDate;
        TextView tvOverview;

        public ViewHolder(View itemView) {
            super(itemView);

            imgMovie = (ImageView) itemView.findViewById(R.id.img_movie);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            tvOverview = (TextView) itemView.findViewById(R.id.tv_description);
        }

        void apply(Movie movie){
            imgMovie.setImageBitmap(movie.getPoster());
            tvTitle.setText(movie.getOriginalTitle());
            tvDate.setText(movie.getReleaseDate());
            tvOverview.setText(movie.getOverview());
        }
    }
}
