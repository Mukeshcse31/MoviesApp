package com.google.app.movieapp1.utils;

import android.content.Context;
import android.util.Log;

import com.google.app.movieapp1.MainActivity;
import com.google.app.movieapp1.R;
import com.google.app.movieapp1.database.MovieEntry;
import com.google.app.movieapp1.model.Movie;
import com.google.app.movieapp1.model.Review;
import com.google.app.movieapp1.model.Video;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JSONUtils {

    public static List<Movie> getMoviesFromJSON(Context context, String json) {

        List<Movie> movies = new ArrayList<>();
        if (json == null || json.isEmpty())
            return null;
        else {

            try {
                JSONObject jsonObject = new JSONObject(json);

               // Log.i(MainActivity.APP_NAME, String.format(" JSON util %s",json));

                String resultsAttr = context.getString(R.string.results);
                JSONArray results = jsonObject.getJSONArray(resultsAttr);

                if(results == null || results.length() == 0){//when the result is not available, return null
                    return null;
                }
                else{
                    Movie movie = null;

                    for( int i = 0; i < results.length(); i++){

                        movie = new Movie();
                        if(results.get(i) == null) continue;

                        try {
                            String id = "id";
                            String title = context.getString(R.string.title);
                            String original_title = context.getString(R.string.original_title);
                            String release_date = context.getString(R.string.release_date);
                            String poster_path = context.getString(R.string.poster_path);
                            String vote_average = context.getString(R.string.vote_average);
                            String overview = context.getString(R.string.overview);

                            JSONObject result = (JSONObject) results.get(i);

                            //id
                            int idValue = result.getInt(id);
                            movie.setId(idValue);

                            //title
                            String titleValue = result.getString(title);
                            if (titleValue == null || titleValue.isEmpty())
                                movie.setTitle("");
                            else
                                movie.setTitle(titleValue);

                            //title
                            String original_titleValue = result.getString(original_title);
                            if (original_titleValue == null || original_titleValue.isEmpty())
                                movie.setOriginalTitle("");
                            else
                                movie.setOriginalTitle(original_titleValue);

                            //release date
                            String release_dateValue = result.getString(release_date);
                            if (release_dateValue == null || release_dateValue.isEmpty())
                                movie.setRelease_date("");
                            else
                                movie.setRelease_date(release_dateValue);

                            //poster_path
                            String poster_pathValue = result.getString(poster_path);
                            if (poster_pathValue == null || poster_path.isEmpty())
                                movie.setPoster_path("");
                            else
                                movie.setPoster_path(poster_pathValue);

                            //vote
                            double vote_averageValue = result.getDouble(vote_average);
                            movie.setVote_average(vote_averageValue);

                            //overview
                            String overviewValue = result.getString(overview);
                            if (overviewValue == null || overviewValue.isEmpty())
                                movie.setOverview("");
                            else
                                movie.setOverview(overviewValue);

                            Log.i(MainActivity.APP_NAME, String.format(" title of %d movie is %s ", i, titleValue));
                            Log.i(MainActivity.APP_NAME, String.format(" release date of %d movie is %s ", i, release_dateValue));
                            Log.i(MainActivity.APP_NAME, String.format(" poster path of %d movie is %s ", i, poster_pathValue));
                            Log.i(MainActivity.APP_NAME, String.format(" vote average of %d movie is %f ", i, vote_averageValue));
                            Log.i(MainActivity.APP_NAME, String.format(" overview of %d movie is %s ", i, overviewValue));

                            movies.add(i, movie);
                        }
                        catch (Exception e){ // TODO goto the next iteration
                            e.printStackTrace();
                            //continue;
                        }
                    }
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }


        return movies;
    }

    public static List<Review> getReviewsFromJSON(Context context, String json) {

        List<Review> reviews = new ArrayList<>();
        if (json == null || json.isEmpty())
            return null;
        else {

            try {
                JSONObject jsonObject = new JSONObject(json);

                // Log.i(MainActivity.APP_NAME, String.format(" JSON util %s",json));

                String resultsAttr = context.getString(R.string.results);
                JSONArray results = jsonObject.getJSONArray(resultsAttr);

                if(results == null || results.length() == 0){//when the result is not available, return null
                    return null;
                }
                else{
                    Review review = null;

                    for( int i = 0; i < results.length(); i++){

                        review = new Review();
                        if(results.get(i) == null) continue;

                        try {
                            String author = context.getString(R.string.author);
                            String content = context.getString(R.string.content);

                            JSONObject result = (JSONObject) results.get(i);

                            //title
                            String authorValue = result.getString(author);
                            if (authorValue == null || authorValue.isEmpty())
                                review.setAuthor("");
                            else
                                review.setAuthor(authorValue);

                            //title
                            String contentValue = result.getString(content);
                            if (contentValue == null || contentValue.isEmpty())
                                review.setContent("");
                            else
                                review.setContent(contentValue);
                            Log.i(MainActivity.APP_NAME, String.format(" title of %d movie is %s ", i, authorValue));
                            Log.i(MainActivity.APP_NAME, String.format(" release date of %d movie is %s ", i, contentValue));

                            reviews.add(i, review);
                        }
                        catch (Exception e){ // TODO goto the next iteration
                            e.printStackTrace();
                            //continue;
                        }
                    }
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }


        return reviews;
    }

    public static List<Video> getVideosFromJSON(Context context, String json) {

        List<Video> videos = new ArrayList<>();
        if (json == null || json.isEmpty())
            return null;
        else {

            try {
                JSONObject jsonObject = new JSONObject(json);

                String resultsAttr = context.getString(R.string.results);
                JSONArray results = jsonObject.getJSONArray(resultsAttr);

                if(results == null || results.length() == 0){//when the result is not available, return null
                    return null;
                }
                else{
                    Video video = null;

                    for( int i = 0; i < results.length(); i++){

                        video = new Video();
                        if(results.get(i) == null) continue;

                        try {

                            String id = context.getString(R.string.id);
                            String iso_639_1 = context.getString(R.string.iso_639_1);
                            String iso_3166_1 = context.getString(R.string.iso_3166_1);
                            String key = context.getString(R.string.key);
                            String name = context.getString(R.string.name);
                            String site = context.getString(R.string.site);
                            String size = context.getString(R.string.size);
                            String type = context.getString(R.string.type);

                            JSONObject result = (JSONObject) results.get(i);

                            //id
                            String idValue = result.getString(id);
                            if (idValue == null || idValue.isEmpty())
                                video.setId("");
                            else
                                video.setId(idValue);

                            //iso_639_1
                            String iso_639_1Value = result.getString(iso_639_1);
                            if (iso_639_1Value == null || iso_639_1Value.isEmpty())
                                video.setIso_639_1("");
                            else
                                video.setIso_639_1(iso_639_1Value);

//                            //iso_3166_1
//                            String iso_3166_1Value = result.getString(iso_3166_1);
//                            if (iso_3166_1Value == null || iso_3166_1Value.isEmpty())
//                                video.setIso_3166_1("");
//                            else
//                                video.setIso_3166_1(iso_3166_1Value);

                            //key
                            String keyValue = result.getString(key);
                            if (keyValue == null || keyValue.isEmpty())
                                video.setKey("");
                            else
                                video.setKey(keyValue);

                            //name
                            String nameValue = result.getString(name);
                            if (nameValue == null || nameValue.isEmpty())
                                video.setName("");
                            else
                                video.setName(nameValue);

                            //size
                            Double sizeValue = result.getDouble(size);
                            if (sizeValue == null)
                                video.setSize(0);
                            else
                                video.setSize(sizeValue);

                            //site
                            String siteValue = result.getString(site);
                            if (siteValue == null || siteValue.isEmpty())
                                video.setSite("");
                            else
                                video.setSite(siteValue);

                            //type
                            String typeValue = result.getString(type);
                            if (typeValue == null || typeValue.isEmpty())
                                video.setType("");
                            else
                                video.setType(typeValue);


                            Log.i(MainActivity.APP_NAME, String.format(" title of %d movie is %s ", i, sizeValue));
                            Log.i(MainActivity.APP_NAME, String.format(" release date of %d movie is %s ", i, typeValue));

                            videos.add(video);
                        }
                        catch (Exception e){ // TODO goto the next iteration
                            e.printStackTrace();
                            //continue;
                        }
                    }
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }


        return videos;
    }


    public static List<Movie> getMovieFromEntry(List<MovieEntry> movieEntries) {

        List<Movie> movies = new ArrayList<>();
        for (int i = 0; i < movieEntries.size(); i++) {
            Movie movie = new Movie();
            movie.setId(movieEntries.get(i).getId());
            movie.setPoster_path(movieEntries.get(i).getPoster_path());
            movie.setTitle(movieEntries.get(i).getTitle());
            movie.setImage(movieEntries.get(i).getPoster());
            movie.setOverview(movieEntries.get(i).getOverview());
            movie.setVote_average(movieEntries.get(i).getVote_average());
            movie.setOriginalTitle(movieEntries.get(i).getOriginal_title());
            movie.setRelease_date(movieEntries.get(i).getRelease_date());

            movies.add(movie);

        }
        return movies;
    }
}
