package com.google.app.movieapp1.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import java.util.Date;


/*
private String title;
private String original_title;
private String release_date;
private String poster_path;
private double vote_average;
private String overview;
*/
@Entity(tableName = "movie")
public class MovieEntry {

    @PrimaryKey(autoGenerate = false)
    private int id;
    private String title;
    private String original_title;
    private byte[] poster;
    private String poster_path;
    private String overview;
    private String release_date;
    private double vote_average;

    @ColumnInfo(name = "added_at")
    private Date addedAt;

//    @Ignore
//    public MovieEntry(String description, int priority, Date updatedAt) {
//        this.description = description;
//        this.priority = priority;
//        this.updatedAt = updatedAt;
//    }

    public MovieEntry(int id, String title, String original_title, byte[] poster, String poster_path,
                      String overview, String release_date, Double vote_average,Date addedAt) {

        this.id = id;
        this.title = title;
        this.original_title = original_title;
        this.poster = poster;
        this.poster_path = poster_path;
        this.overview = overview;
        this.release_date = release_date;
        this.vote_average = vote_average;
        this.addedAt = addedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }


    public byte[] getPoster() {
        return poster;
    }

    public void setPoster(byte[] poster) {
        this.poster = poster;
    }

    public String getPoster_path(){ return poster_path;}

    public void setPoster_path(String poster_path){ this.poster_path = poster_path;}

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }


    public Date getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(Date updatedAt) {
        this.addedAt = updatedAt;
    }
}
