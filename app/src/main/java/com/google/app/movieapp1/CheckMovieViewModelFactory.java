package com.google.app.movieapp1;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import com.google.app.movieapp1.database.AppDatabase;

// COMPLETED (1) Make this class extend ViewModel ViewModelProvider.NewInstanceFactory
public class CheckMovieViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    // COMPLETED (2) Add two member variables. One for the database and one for the taskId
    private final AppDatabase mDb;
    private final int mTaskId;

    // COMPLETED (3) Initialize the member variables in the constructor with the parameters received
    public CheckMovieViewModelFactory(AppDatabase database, int taskId) {
        mDb = database;
        mTaskId = taskId;
    }

    // COMPLETED (4) Uncomment the following method
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new CheckMovieViewModel(mDb, mTaskId);
    }
}
