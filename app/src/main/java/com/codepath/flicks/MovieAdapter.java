package com.codepath.flicks;

import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.flicks.models.Config;
import com.codepath.flicks.models.Movie;

import java.util.ArrayList;

public class MovieAdapter extends  RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    //list of movies
    ArrayList<Movie> movies;
    //config need for images urls
    Config config;
    //context for rendering
    Context context;
    //initialize with list


    public MovieAdapter(ArrayList<Movie> movies) {
        this.movies = movies;
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    //creates and inflates the new view
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //get the context and create the inflater
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        //create the view using the item_movie layout
        View movieView = inflater.inflate(R.layout.item_movie, viewGroup, false);

        //return a new viewHolder

        return new ViewHolder(movieView);
    }

    //bind an inflate view to a new item
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        //get the movie
        Movie movie = movies.get(i);
        //populate the view with the movie data
        viewHolder.tvTitle.setText(movie.getTitle());
        viewHolder.tvOverview.setText(movie.getOverview());
        //determine the current orientation
        boolean isPortrait = context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;







         //build url for poster image
       // String imageUrl = config.getImageUrl(config.getPosterSize(), movie.getPosterPath());
        String imageUrl = null;
        //if in portrait mode; load the poster image
        if (isPortrait) {

            imageUrl = config.getImageUrl(config.getPosterSize(), movie.getPosterPath());
        } else {
            //load the backdrop image
            imageUrl = config.getImageUrl(config.getBackdropSize(), movie.getBackdropPath());
        }

        //get the correct placeholder and image view for the current orientation
        int placeholderId = isPortrait ? R.drawable.flicks_movie_placeholder : R.drawable.flicks_backdrop_placeholder;
        ImageView imageView = isPortrait ? viewHolder.ivPosterImage : viewHolder.ivBackdropImage;

        //load image using the glide
        Glide.with(context)
                .load(imageUrl)


                //.bitmapTransform(new RoundedCornersTransformation(context, 25, 0))


               // .placeholder(placeholderId)
               // .error(placeholderId)
                .into(imageView);
               // .placeholder(R.drawable.flicks_movie_placeholder)
                // .error(R.drawable.flicks_movie_placeholder)
               // .into(viewHolder.ivPosterImage);





    }

    //return the total number of item in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    //create the view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        //track view objects
        ImageView ivPosterImage;
        ImageView ivBackdropImage;
        TextView tvTitle;
        TextView tvOverview;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //lookup view object by id
            ivPosterImage = (ImageView) itemView.findViewById(R.id.ivPosterImage);
            ivBackdropImage = (ImageView) itemView.findViewById(R.id.ivBackdropImage);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvOverview = (TextView) itemView.findViewById(R.id.tvOverview);
        }
    }


}
