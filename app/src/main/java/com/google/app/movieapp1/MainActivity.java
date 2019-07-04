/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.app.movieapp1;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.app.movieapp1.database.MovieEntry;
import com.google.app.movieapp1.model.Movie;
import com.google.app.movieapp1.utils.JSONUtils;
import com.google.app.movieapp1.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>,
        SharedPreferences.OnSharedPreferenceChangeListener {

    public static String DATABASE_NAME;
    public static String APP_NAME = "MainActivity";
    public static String MOVIEPASS = "clickedMovie";
    private static String SEARCH_QUERY_URL_EXTRA = "SEARCH_QUERY_URL_EXTRA";
    private static int GITHUB_SEARCH_LOADER = 33;
    public static String jsonResponse = "";
    public String previousPreference;
    private ImageAdapter mImageAdapter;
    private GridView mGridView;
    static List<Movie> allMovies, fav_movies;
    private TextView mErrorMessageDisplay;
    public static int sort_order;
    private ProgressBar mLoadingIndicator;
    static Context mainActivity;
    Toast toast;
//    private AppDatabase mDb;
    List<MovieEntry> mMovieEntries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);

        mainActivity = this;
        DATABASE_NAME = getString(R.string.DATABASE_NAME);
//        mDb = AppDatabase.getInstance(getApplicationContext());
        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);

        mGridView = findViewById(R.id.gv_movies);

        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);

        Log.i("sort order", "back  " + sort_order);

        mImageAdapter = new ImageAdapter(MainActivity.this);
        mGridView.setAdapter(mImageAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                final Intent intent = new Intent(MainActivity.this, MovieDetails.class);

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            String imagePath = MainActivity.mainActivity.getResources().getString(R.string.imagePath);
                            String path = imagePath + allMovies.get(position).getPoster_path();
                            Bitmap bitmap = Picasso.with(MainActivity.mainActivity)
                                    .load(path)
                                    .placeholder(R.drawable.imagenotfound)
                                    .get();

                            byte[] poster = NetworkUtils.getPictureByteOfArray(bitmap);
                            allMovies.get(position).setImage(poster);

                            intent.putExtra(MOVIEPASS, allMovies.get(position));
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
            }
        });
        Log.i(APP_NAME, "retrieved from DB on create");
        setupSharedPreferences();
        setupViewModel();

        if (sort_order != 2) {
            getSupportLoaderManager().initLoader(GITHUB_SEARCH_LOADER, null, this);

            loadMovieData();
        }
    }


    private void setupViewModel() {

        MovieViewModel viewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        viewModel.getMovies().observe(this, new Observer<List<MovieEntry>>() {
            @Override
            public void onChanged(@Nullable List<MovieEntry> taskEntries) {
                Log.i(APP_NAME, "Updating list of tasks from LiveData in ViewModel " + taskEntries.size());
                fav_movies = JSONUtils.getMovieFromEntry(taskEntries);

                if (sort_order == 2) {
                    mErrorMessageDisplay.setVisibility(View.INVISIBLE);
                    mGridView.setVisibility(View.VISIBLE);
                    allMovies = fav_movies;
                    mImageAdapter.setMovieData(allMovies);
                }
            }
        });

    }

    private void populateUI() {

        showMovieDataView();
        if (sort_order != 2) {
            allMovies = JSONUtils.getMoviesFromJSON(MainActivity.this, jsonResponse);
            mImageAdapter = new ImageAdapter(this, allMovies);
            mGridView.setAdapter(mImageAdapter);
        }
    }

    private void loadMovieData() {

        if (!NetworkUtils.isOnline(this)) {
            mGridView.setVisibility(View.INVISIBLE);
            showErrorMessage(getResources().getString(R.string.no_internet_connection));
            showToast(getResources().getString(R.string.no_internet_connection));

            return;
        }

        mGridView.setVisibility(View.VISIBLE);
        String[] movie_url_details = getResources().getStringArray(R.array.movie_url_details);

        showMovieDataView();

        Bundle queryBundle = new Bundle();

        if (sort_order == 2) {
            jsonResponse = "";
            queryBundle.putString(SEARCH_QUERY_URL_EXTRA, "fav");
        } else
            queryBundle.putString(SEARCH_QUERY_URL_EXTRA, movie_url_details[sort_order]);

        if (jsonResponse.isEmpty() && sort_order != 2) {
            Log.i(APP_NAME, "jsonResponse is empty");

            LoaderManager loaderManager = getSupportLoaderManager();
            Loader<String> githubSearchLoader = loaderManager.getLoader(GITHUB_SEARCH_LOADER);

            if (githubSearchLoader == null) {
                loaderManager.initLoader(GITHUB_SEARCH_LOADER, queryBundle, this);
            } else {
                loaderManager.restartLoader(GITHUB_SEARCH_LOADER, queryBundle, this);
            }

        } else {

            Log.i(APP_NAME, "jsonResponse is " + jsonResponse);
            populateUI();
            //Complete test odd number of images

        }
    }
    //Complete show the loading message/image - DONE

    private void showMovieDataView() {

        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
    }

    private void showErrorMessage(String message) {

        mErrorMessageDisplay.setText(message);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    @NonNull
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
                    mLoadingIndicator.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            @Override
            public String loadInBackground() {

                String searchQueryUrlString = bundle.getString(SEARCH_QUERY_URL_EXTRA, "pop");

                /* If the user didn't enter anything, there's nothing to search for */
                if (TextUtils.isEmpty(searchQueryUrlString)) {
                    return null;
                }

                URL movieURL = NetworkUtils.buildURL(searchQueryUrlString, getResources().getString(R.string.api_key));

                try {
                    jsonRaw = NetworkUtils.getResponseFromHttpUrl(movieURL);
                    return jsonRaw;

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            public void deliverResult(String data) {
                jsonResponse = data;
                super.deliverResult(data);
            }
        };
    }


    @Override
    public void onLoadFinished(@NonNull Loader loader, String movieData) {

        mLoadingIndicator.setVisibility(View.INVISIBLE);

        if (allMovies != null || (movieData != null && !movieData.isEmpty())) {
            jsonResponse = movieData;
            populateUI();
        } else {
            showErrorMessage(getResources().getString(R.string.error_fetch));
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings, menu);
        return true;
    }

    private void showToast(String message) {

        if (toast != null) toast.cancel();
        toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister VisualizerActivity as an OnPreferenceChangedListener to avoid any memory leaks.
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {

        Log.i("savedInstanceState", "saved the jsonResponse " + jsonResponse);
        bundle.putString(getResources().getString(R.string.jsonResponse), jsonResponse);
        super.onSaveInstanceState(bundle);
    }

    private void setupSharedPreferences() {

        // Get all of the values from shared preferences to set it up
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean bol_Pop = sharedPreferences.getBoolean(getString(R.string.pref_sort_pop_value), true);
        boolean bol_Rate = sharedPreferences.getBoolean(getString(R.string.pref_sort_rate_value), false);
        boolean bol_Fav = sharedPreferences.getBoolean(getString(R.string.pref_sort_fav_value), false);

        if (bol_Pop) {
            previousPreference = getString(R.string.pref_sort_pop_value);
            sort_order = 0;
        } else if (bol_Rate) {
            previousPreference = getString(R.string.pref_sort_rate_value);
            sort_order = 1;
        } else {
            previousPreference = getString(R.string.pref_sort_fav_value);
            sort_order = 2;

        }
//        loadMovieData();
        // Register the listener
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String
            key) {

        boolean selection = sharedPreferences.getBoolean(key, false);

        if (selection)
            if (previousPreference != key) {

                if (TextUtils.equals(key, "pop"))
                    sort_order = 0;
                else if (TextUtils.equals(key, "rate"))
                    sort_order = 1;
                else {
                    sort_order = 2;
                }
                jsonResponse = "";
                if (sort_order == 2) {
                    setupViewModel();
                } else
                    loadMovieData();


                previousPreference = key;
                Log.i(APP_NAME, "preference changed " + key + " Value : " + sharedPreferences.getBoolean(key, false));
            }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        String jjj = savedInstanceState.getString(getResources().getString(R.string.jsonResponse));
        Log.v("savedInstanceState", "Inside of onRestoreInstanceState " + jjj);

        super.onRestoreInstanceState(savedInstanceState);

    }


    @Override
    public void onResume() {
        super.onResume();
        Log.i(APP_NAME, "the activity is resumed");
    }

}