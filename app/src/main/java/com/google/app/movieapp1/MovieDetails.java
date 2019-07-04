package com.google.app.movieapp1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.app.movieapp1.database.AppDatabase;
import com.google.app.movieapp1.database.MovieEntry;
import com.google.app.movieapp1.model.Movie;
import com.google.app.movieapp1.model.Review;
import com.google.app.movieapp1.model.Video;
import com.google.app.movieapp1.utils.JSONUtils;
import com.google.app.movieapp1.utils.NetworkUtils;
import com.google.app.movieapp1.utils.ReviewAdapter;
import com.google.app.movieapp1.utils.TrailerAdapter;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.Date;
import java.util.List;

public class MovieDetails extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>, TrailerAdapter.TrailerAdapterOnClickHandler {

    private static final String TAG = "MOVIE DETAILS";

    private static final int DEFAULT_INDEX = -1;
    private static final int DEFAULT_TASK_ID = 0;
    private static final String MOVIE_ID = "MOVIE_ID";
    private static final int REVIEW_LOADER = 11;
    //    Intent intent;
    private TextView mTitle, mReleaseDate, mUserRating, mSynopsis, mReview, mTrailer, error_message_tv;
    private ImageView image_iv;

    //    private int sort_order;
    private Movie clickedMovie;
    private int movieID;
    private ImageButton image_ib;

    private AppDatabase mDb;
    private RecyclerView reviews_rv, trailer_rv;
    private ReviewAdapter mReviewAdapter;
    private TrailerAdapter mTrailerAdapter;
    private List<Review> allReviews;
    private List<Video> allVideos;
    private List<Movie> all_Fav_Movies;

    Bitmap bitmap;
    private String videoJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);

        mDb = AppDatabase.getInstance(getApplicationContext());
        mTitle = findViewById(R.id.tv_title);
        mReleaseDate = findViewById(R.id.tv_release_date);
        mUserRating = findViewById(R.id.tv_rating);
        mSynopsis = findViewById(R.id.tv_synopsis);
        image_iv = findViewById(R.id.image_iv);
        image_ib = findViewById(R.id.markAsFav_ib);
        mReview = findViewById(R.id.tv_reviews);
        mTrailer = findViewById(R.id.tv_trailer);
        error_message_tv = findViewById(R.id.tv_error_message_display);

        Bundle bundle = getIntent().getExtras();
        clickedMovie = bundle.getParcelable(MainActivity.MOVIEPASS);

        if (clickedMovie == null) {
        } else {
            if (clickedMovie.getTitle() == null)
                setTitle("");
            else
                setTitle(clickedMovie.getTitle());

            String imagePath = MainActivity.mainActivity.getResources().getString(R.string.imagePath);
            imagePath += clickedMovie.getPoster_path();

            if (NetworkUtils.isOnline(MovieDetails.this)) {
                Picasso.with(MainActivity.mainActivity)
                        .load(imagePath)
                        .placeholder(R.drawable.imagenotfound)
                        .into(image_iv);
            } else {
                byte[] img = clickedMovie.getImage();
                Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
                image_iv.setImageBitmap(bitmap);
            }
            mTitle.setText(clickedMovie.getOriginalTitle());
            mTitle.setContentDescription(clickedMovie.getOriginalTitle());

            mReleaseDate.setText(clickedMovie.getRelease_date());
            mReleaseDate.setContentDescription(clickedMovie.getRelease_date());

            mUserRating.setText("" + clickedMovie.getVote_average() + "/10");
            mUserRating.setContentDescription("" + clickedMovie.getVote_average());

            mSynopsis.setText(getString(R.string.overview_space)  + clickedMovie.getOverview());
            mSynopsis.setContentDescription(clickedMovie.getOverview());

            movieID = clickedMovie.getId();

            //set fav image
            Drawable res = getResources().getDrawable(R.drawable.unfav_image);
            image_ib.setImageDrawable(res);
            image_ib.setTag(getString(R.string.reset));

            for(int i = 0; i < MainActivity.fav_movies.size(); i++){

                if(movieID == MainActivity.fav_movies.get(i).getId()){

                    res = getResources().getDrawable(R.drawable.fav_image);
                    image_ib.setImageDrawable(res);
                    image_ib.setTag(getString(R.string.set));
                    break;
                }
            }

            //set reviews
            reviews_rv = (RecyclerView) findViewById(R.id.rv_reviews);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            reviews_rv.setLayoutManager(layoutManager);
            Bundle queryBundle = new Bundle();
            queryBundle.putInt(MOVIE_ID, movieID);

            //set trailers
            trailer_rv = findViewById(R.id.rv_trailer);
            LinearLayoutManager trailer_lm = new LinearLayoutManager(this);
            trailer_rv.setLayoutManager(trailer_lm);

            mReview.setVisibility(View.INVISIBLE);
            mTrailer.setVisibility(View.INVISIBLE);

            if (NetworkUtils.isOnline(this)) {
                error_message_tv.setVisibility(View.INVISIBLE);
                mReview.setVisibility(View.VISIBLE);
                mTrailer.setVisibility(View.VISIBLE);
                getSupportLoaderManager().initLoader(REVIEW_LOADER, queryBundle, this);
            } else {
                error_message_tv.setVisibility(View.VISIBLE);

            }
        }
    }

    //Complete favorite star button

    @Override
    public Loader onCreateLoader(int i, final Bundle bundle) {
        return new AsyncTaskLoader<String>(this) {

            String jsonRaw;

            @Override
            protected void onStartLoading() {

                if (bundle == null) return;

                if (jsonRaw != null) {
                    deliverResult(jsonRaw);
                } else {
//                    mLoadingIndicator.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            @Override
            public String loadInBackground() {

                int movie_id = bundle.getInt(MOVIE_ID, 0);

                /* If the user didn't enter anything, there's nothing to search for */
                if (movie_id == 0) {
                    return null;
                }

                String ii = getResources().getString(R.string.review_url);
                String vv = getResources().getString(R.string.videos_url);

                ii = ii.replace("{api_key}", getResources().getString(R.string.api_key));
                ii = ii.replace("{movie_ID}", "" + movie_id);


                vv = vv.replace("{api_key}", getResources().getString(R.string.api_key));
                vv = vv.replace("{movie_ID}", "" + movie_id);

                Log.i(TAG, "URL is " + ii);

                try {
                    URL movieURL = new URL(ii);
                    URL videoURL = new URL(vv);

                    jsonRaw = NetworkUtils.getResponseFromHttpUrl(movieURL);
                    videoJson = NetworkUtils.getResponseFromHttpUrl(videoURL);

                    return jsonRaw;

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            public void deliverResult(String data) {
                jsonRaw = data;
//                Log.i(TAG, "" + jsonRaw);
                super.deliverResult(data);
            }
        };
    }


    @Override
    public void onLoadFinished(@NonNull Loader loader, String reviewData) {

        if (reviewData != null && !reviewData.isEmpty()) {
            allReviews = JSONUtils.getReviewsFromJSON(MovieDetails.this, reviewData);

            if (allReviews == null) {

                mReview.setText(getString(R.string.no_review));
                reviews_rv.setVisibility(View.INVISIBLE);
            } else {
                mReview.setText(getString(R.string.review));
                mReview.setVisibility(View.VISIBLE);
                reviews_rv.setVisibility(View.VISIBLE);
                mReviewAdapter = new ReviewAdapter(allReviews, this);
                reviews_rv.setAdapter(mReviewAdapter);
            }

        } else {
//            showErrorMessage(getResources().getString(R.string.error_fetch));
        }

        // TRAILERs
        if (videoJson != null && !videoJson.isEmpty()) {

            allVideos = JSONUtils.getVideosFromJSON(MovieDetails.this, videoJson);//TODO

            if (allVideos == null) {

                mTrailer.setText(getString(R.string.no_trailer));
                trailer_rv.setVisibility(View.INVISIBLE);
            } else {
                mTrailer.setText(getString(R.string.trailer));
                mTrailer.setVisibility(View.VISIBLE);
                trailer_rv.setVisibility(View.VISIBLE);
                mTrailerAdapter = new TrailerAdapter(allVideos, this);
                trailer_rv.setAdapter(mTrailerAdapter);

            }

        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) {

    }

    @Override
    public void onBackPressed() {
        //remove the activity from the stack
        Log.i(TAG, "the activity is cleaned");
        Intent intent = new Intent(MovieDetails.this, MainActivity.class);
        startActivity(intent);

        finish();
    }


    public void add_remove_Fav(View view) {

        ImageButton ib = (ImageButton) view;
        if(ib.getTag() == getString(R.string.set)){
            Drawable res = getResources().getDrawable(R.drawable.unfav_image);
            image_ib.setTag(getString(R.string.reset));
            image_ib.setImageDrawable(res);
        }
        else{
            Drawable res = getResources().getDrawable(R.drawable.fav_image);
            image_ib.setImageDrawable(res);
            image_ib.setTag(getString(R.string.set));

    }

        Log.i(TAG, "favorite movie started ");

        //add or update

        Date date = new Date();

        final MovieEntry fav_movie = new MovieEntry(movieID, clickedMovie.getTitle(), clickedMovie.getOriginalTitle(),
                clickedMovie.getImage(), clickedMovie.getPoster_path(), clickedMovie.getOverview(), clickedMovie.getRelease_date(), clickedMovie.getVote_average(), date);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                MovieEntry m1 = mDb.movieDao().findMovieById(movieID);

                if (m1 == null) {//not in the favorites table
                    // insert new task
                    mDb.movieDao().insertMovie(fav_movie);

                } else {
                    // update task
                    m1 = mDb.movieDao().findMovieById(movieID);
                    mDb.movieDao().deleteMovie(m1);
                }
            }
        });

    }

    @Override
    public void onClick(int trailerNo, String tag) {

        Log.i(TAG, "Trailer clicked is " + trailerNo);
        String key = allVideos.get(trailerNo).getKey();
        String url = getString(R.string.youtube).replace("{key}", key);

        if (tag.contentEquals(getString(R.string.reset))) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
//        intent.setPackage("com.google.android.youtube");
            startActivity(intent);
        } else if (tag.contentEquals(getString(R.string.set))) {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_TEXT, url);
            startActivity(Intent.createChooser(share, getString(R.string.share_url)));
        }
    }

}
