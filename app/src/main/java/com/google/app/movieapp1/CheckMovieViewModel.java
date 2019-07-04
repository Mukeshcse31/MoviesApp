package com.google.app.movieapp1;

import android.arch.lifecycle.ViewModel;

import com.google.app.movieapp1.database.AppDatabase;
import com.google.app.movieapp1.database.MovieEntry;

public class CheckMovieViewModel extends ViewModel {

    // COMPLETED (6) Add a task member variable for the TaskEntry object wrapped in a LiveData
    private MovieEntry movie;

    // COMPLETED (8) Create a constructor where you call loadTaskById of the taskDao to initialize the tasks variable
    // Note: The constructor should receive the database and the taskId
    public CheckMovieViewModel(AppDatabase database, int taskId) {
        movie = database.movieDao().findMovieById(taskId);
    }

    // COMPLETED (7) Create a getter for the task variable
    public MovieEntry getTask() {
        return movie;
    }
}
