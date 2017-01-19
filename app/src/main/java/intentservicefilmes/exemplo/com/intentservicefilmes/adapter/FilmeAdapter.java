package intentservicefilmes.exemplo.com.intentservicefilmes.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import intentservicefilmes.exemplo.com.intentservicefilmes.R;
import intentservicefilmes.exemplo.com.intentservicefilmes.entity.Filme;

/**
 * Created by BPardini on 22/12/2016.
 */

public class FilmeAdapter extends RecyclerView.Adapter<FilmeAdapter.ViewHolder> {
    private List<Filme> movies;

    public FilmeAdapter(List<Filme> movies){
        this.movies = movies;
    }

    @Override
    public FilmeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.filme_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(FilmeAdapter.ViewHolder holder, int position) {
        Filme filme = movies.get(position);
        if(filme != null){
            holder.apply(filme);
        }
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imgMovie;
        TextView tvTitle;
        TextView tvOverview;

        ViewHolder(View itemView) {
            super(itemView);

            imgMovie = (ImageView) itemView.findViewById(R.id.img_movie);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvOverview = (TextView) itemView.findViewById(R.id.tv_description);
        }

        void apply(Filme filme){
            imgMovie.setImageBitmap(filme.getPoster());
            tvTitle.setText(filme.getOriginalTitle());
            tvOverview.setText(filme.getOverview());
        }
    }
}
