package com.codepath.flicks.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Config {

    //the base url for loading images
    String imageBaseUrl;
    //the poster size
    String posterSize;
    // the backdrop size
    String backdropSize;

    public Config(JSONObject object) throws JSONException {
        JSONObject images = object.getJSONObject("images");
        //get the image base URL
        imageBaseUrl = images.getString("secure_base_url");
// get the poster size
        JSONArray posterSizeOptions = images.getJSONArray("poster_sizes");
        //use the option at index w342 as a fallback
        posterSize = posterSizeOptions.optString(3, "w342");
        //parse the backdrop
        JSONArray backdropSizeOptions = images.getJSONArray("backdrop_sizes");
        backdropSize = backdropSizeOptions.optString(1, "w780");
    }

    //helper methodfor creating urls
    public String getImageUrl(String posterSize, String path) {
        return String.format("%s%s%s", imageBaseUrl, posterSize, path); //concatenate all the three
    }

    public String getImageBaseUrl() {
        return imageBaseUrl;
    }

    public String getPosterSize() {
        return posterSize;
    }

    public String getBackdropSize() {
        return backdropSize;
    }
}
//android:roundIcon="@mipmap/ic_launcher_round"