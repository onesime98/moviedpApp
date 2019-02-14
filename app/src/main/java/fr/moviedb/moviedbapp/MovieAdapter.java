package fr.moviedb.moviedbapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import fr.moviedb.moviedbapp.model.Movie;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>{
    private List<Movie> MoviesItems;
    private Context context;
    private static final String BASE_URL = "http://image.tmdb.org/t/p/w185";
    private ItemClickListener clickListener;


    public MovieAdapter(List<Movie> movieLists, Context context) {

        // generate constructors to initialise the List and Context objects

        this.MoviesItems= movieLists;
        this.context = context;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View view = inflater.inflate(R.layout.item_row, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(MovieAdapter.ViewHolder viewHolder, int i) {
        final Movie movies= MoviesItems.get(i);
        viewHolder.movie_title.setText(movies.getTitle());
        Picasso.with(context)
                .load(BASE_URL+movies.getImage())
                .into(viewHolder.image);
    }

    @Override
    public int getItemCount() {
        return MoviesItems.size();
    }
    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView movie_title;
        public  ImageView image;


        public ViewHolder(View itemView) {
            super(itemView);

            movie_title = (TextView) itemView.findViewById(R.id.movie_title);
            image = (ImageView) itemView.findViewById(R.id.movie_image);
            image.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onClick(view, getAdapterPosition());
        }
    }
}
