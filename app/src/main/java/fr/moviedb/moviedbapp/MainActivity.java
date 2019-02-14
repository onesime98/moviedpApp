package fr.moviedb.moviedbapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.moviedb.moviedbapp.model.Movie;

public class MainActivity extends AppCompatActivity {
    final String API_KEY = "c5414adee641d2cf6d559db0b94be18c";
    final String POPULAR_MOVIES_URL = "https://api.themoviedb.org/3/tv/popular?api_key="+API_KEY;
    private RecyclerView recyclerview;
    private MovieAdapter adapter;
    private List<Movie> moviesLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerview=(RecyclerView)findViewById(R.id.recyclerView);
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerview.setLayoutManager(mLayoutManager);
        recyclerview.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerview.setItemAnimator(new DefaultItemAnimator());

        moviesLists = new ArrayList<>();
        displayData();
    }

    private void displayData() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                POPULAR_MOVIES_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();

                try {
                    JSONObject obj = new JSONObject(response);

                    //we have the array named movies inside the object
                    //so here we are getting that json array
                    JSONArray moviesArray = obj.getJSONArray("results");


                    //now looping through all the elements of the json array
                    for (int i = 0; i < moviesArray.length(); i++) {
                        //getting the json object of the particular index inside the array
                        JSONObject newsObject = moviesArray.getJSONObject(i);
                       // Toast.makeText(getApplicationContext(),newsObject.toString(),Toast.LENGTH_LONG).show();


                        Movie movie = new Movie(newsObject.getInt("id"),newsObject.getString("original_name"),newsObject.getString("poster_path"),newsObject.getString("overview"),newsObject.getString("first_air_date"),newsObject.getString("original_language"),newsObject.getString("vote_count"));
                        //adding the Movie to movieList
                        moviesLists.add(movie);

                    }

                    adapter = new MovieAdapter(moviesLists, getApplicationContext());
                    recyclerview.setAdapter(adapter);
                    adapter.setClickListener(new ItemClickListener() {
                        @Override
                        public void onClick(View view, int position) {
                            Movie objet = moviesLists.get(position);
                            Intent intent = new Intent(getApplicationContext(), DetailsMovies.class);
                            intent.putExtra("id", objet.getId());
                            startActivity(intent);
                        }
                    });


                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MainActivity.this, "Error" + error.toString(), Toast.LENGTH_SHORT).show();

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }



    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
