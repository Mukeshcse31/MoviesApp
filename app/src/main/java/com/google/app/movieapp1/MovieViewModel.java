package com.google.app.movieapp1;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.util.Log;
import com.google.app.movieapp1.database.AppDatabase;
import com.google.app.movieapp1.database.MovieEntry;

import java.util.List;

public class MovieViewModel extends AndroidViewModel {

    // Constant for logging
    private static final String TAG = MovieViewModel.class.getSimpleName();

    private LiveData<List<MovieEntry>> movies;

    public MovieViewModel(Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving the tasks from the DataBase");
        movies = database.movieDao().loadAllFavMovies();
    }

    public LiveData<List<MovieEntry>> getMovies() {
        return movies;
    }
}
