package fr.moviedb.moviedbapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import fr.moviedb.moviedbapp.model.Movie;

public class DetailsMovies  extends AppCompatActivity {
    final String API_KEY = "c5414adee641d2cf6d559db0b94be18c";
    private static final String BASE_URL = "http://image.tmdb.org/t/p/w185";
    String DETAILS_MOVIES_URL ;
    String SIMILAR_MOVIES_URL ;
    String id;
    TextView title,overview,rating,season;
    ImageView image;
    private RecyclerView recyclerview;
    private MovieAdapter adapter;
    private List<Movie> moviesLists;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_movies);
        recyclerview=(RecyclerView)findViewById(R.id.recyclerViewSimilar);
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        try {
            Bundle bundle = getIntent().getExtras();
            if (bundle.containsKey("id"))
                id = String.valueOf(bundle.getInt("id"));

        } catch (NullPointerException e) {
        }
        DETAILS_MOVIES_URL = "https://api.themoviedb.org/3/tv/"+id+"?api_key="+API_KEY;
        SIMILAR_MOVIES_URL = " https://api.themoviedb.org/3/tv/"+id+"/similar?api_key="+API_KEY;
        title=(TextView)findViewById(R.id.tv_movie_title_detail);

        overview=(TextView)findViewById(R.id.tv_movie_overview);
        rating=(TextView)findViewById(R.id.tv_movie_rating);
        season=(TextView)findViewById(R.id.tv_movie_seasons);
        image=(ImageView)findViewById(R.id.iv_movie_poster_detail);


        moviesLists = new ArrayList<>();
        displayData();
        displayDataSimilar();
    }

    private void displayDataSimilar() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                SIMILAR_MOVIES_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject obj = new JSONObject(response);

                    //we have the array named movies inside the object
                    //so here we are getting that json array
                    JSONArray moviesArray = obj.getJSONArray("results");


                    //now looping through all the elements of the json array
                    for (int i = 0; i < moviesArray.length(); i++) {
                        //getting the json object of the particular index inside the array
                        JSONObject newsObject = moviesArray.getJSONObject(i);
                       //  Toast.makeText(getApplicationContext(),newsObject.toString(),Toast.LENGTH_LONG).show();


                        Movie movie = new Movie(newsObject.getInt("id"),newsObject.getString("original_name"),newsObject.getString("poster_path"),newsObject.getString("overview"),newsObject.getString("first_air_date"),newsObject.getString("original_language"),newsObject.getString("vote_count"));

                        //adding the Movie to movieList
                        moviesLists.add(movie);

                    }

                    adapter = new MovieAdapter(moviesLists, getApplicationContext());
                    recyclerview.setAdapter(adapter);

                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(DetailsMovies.this, "Error" + error.toString(), Toast.LENGTH_SHORT).show();

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void displayData() {
        //creating a string request to send request to the url
        StringRequest stringRequest = new StringRequest(Request.Method.GET, DETAILS_MOVIES_URL,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);
                            // get value from LL Json Object
                            String name=obj.getString("original_name");
                            title.setText(name);
                            String summary=obj.getString("overview");
                            overview.setText(summary);
                            String rate= obj.getString("vote_average");
                            rating.setText("Ratings : "+rate);
                            String numberSeason=obj.getString("number_of_seasons");
                            season.setText("Seasons : "+numberSeason);
                            String img=obj.getString("poster_path");
                            Picasso.with(getApplicationContext())
                                    .load(BASE_URL+img)
                                    .into(image);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //adding the string request to request queue
        requestQueue.add(stringRequest);
    }

}
