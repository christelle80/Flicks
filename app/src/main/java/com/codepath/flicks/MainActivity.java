package com.codepath.flicks;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.codepath.flicks.models.Config;
import com.codepath.flicks.models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    //constant
    // the bas URL for the API
    public final static String API_BASE_URL = "https://api.themoviedb.org/3";
    //the parameter name for the API
    public final static String API_KEY_PARAM = "api_key";
    // the API key --TODO move to a secure location
   // public final static String API_Key = "a07e22bc18f5cb106bfe4cc1f83ad8ed";
    //tag for logging from this activity
    public final static String TAG = "MainActivity";

    //instance fields
    AsyncHttpClient client;
   // where the imagebaseurl and the postersize
    //the list of currently playing movies
    ArrayList<Movie> movies;
    // the recycler view
    RecyclerView rvMovie;
    //the adapter
    MovieAdapter adapter;
    //image config
    Config config;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initialize the client
        client = new AsyncHttpClient();
        //initialize the list of movies
        movies = new ArrayList<>();
        //initialize the adapter
        adapter = new MovieAdapter(movies);
        //resolve the recycler view
        rvMovie = (RecyclerView) findViewById(R.id.rvMovies);
        rvMovie.setLayoutManager(new LinearLayoutManager(this));
        rvMovie.setAdapter(adapter);

        //get the configuration on app creation
        getConfiguration();
        //get the now playing movie list
      //  getNowPlaying();

    }

    //get the list of currently playing movies from the API
    private void getNowPlaying (){
        //create the URL
        String url = API_BASE_URL + "/movie/now_playing";
        // set the request parameters
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key)); //API Key, always required
        //execute a GET request expecting a JSON object response
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
               // super.onSuccess(statusCode, headers, response);
                //load the results
                try {
                    JSONArray results = response.getJSONArray("results");
                    //iterate through result
                    for (int i = 0; i < results.length(); i++) {
                        Movie movie = new Movie(results.getJSONObject(i));
                        //Movie movie = new Movie(results.getJSONObject(i));
                        movies.add(movie);
                        //notify adapter
                        adapter.notifyItemInserted(movies.size() -1);

                    }
                    Log.i(TAG, String.format("Loaded %s movies", results.length()));
                } catch (JSONException e) {
                    logError("failed to parse now playing movies", e, true);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
               // super.onFailure(statusCode, headers, responseString, throwable);
                logError("failed to get data from now_playing", throwable, true);
            }
        });
    }
    // get the configure from the API
    private void getConfiguration(){
        //create the URL
        String url = API_BASE_URL + "/configuration";
        // set the request parameters
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key)); //API Key, always required
        //execute a GET request expecting a JSON object reponse
        client.get(url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
               // super.onSuccess(statusCode, headers, response);

                try {
                   //imagebaseurl and the postersize
                    config = new Config(response);

                    Log.i(TAG, String.format("Loaded configuration with imageBaseUrl %s and posterSize %s",
                            config.getImageBaseUrl(),
                            config.getPosterSize()));
                    //pass config to adapter
                    adapter.setConfig(config);
                    //get the now playing movie list
                   getNowPlaying();


                }catch (JSONException e) {
                    logError("Failed parsing configuration", e, true);
                }
                  }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
               // super.onFailure(statusCode, headers, responseString, throwable);
                logError("Failed getting configuration", throwable,true);
            }
        });
    }
//handle errors, log and alert user
    private void logError(String message, Throwable error, boolean alertUser) {
        //always log the error
        Log.e(TAG, message, error);
        //alert the user to avoid silent errors
        if (alertUser) {
            // long toast with the error message
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    }

}
