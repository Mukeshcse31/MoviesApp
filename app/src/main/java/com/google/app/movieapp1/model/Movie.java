package com.google.app.movieapp1.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {

    private int id;
    private String title;
    private String original_title;
    private String release_date;
    private String poster_path;
    private double vote_average;
    private String overview;
private byte[] image;

    public Movie(){

    }
    public Movie(int id, String title, String original_title,
                 String release_date, String poster_path, Double vote_average, String overview, byte[] image) {

        this.id = id;
        this.title = title;
        this.original_title = original_title;
        this.release_date = release_date;
        this.poster_path = poster_path;
        this.vote_average = vote_average;
        this.overview = overview;
        this.image = image;
    }

    protected Movie(Parcel in) {
        id = in.readInt();
        title = in.readString();
        original_title = in.readString();
        release_date = in.readString();
        poster_path = in.readString();
        vote_average = in.readDouble();
        overview = in.readString();
        image = new byte[in.readInt()];
        in.readByteArray(image);
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };


    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginalTitle() {
        return original_title;
    }

    public void setOriginalTitle(String original_title) {
        this.original_title = original_title;
    }


    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setImage(byte[] image){
        this.image = image;
    }

    public byte[] getImage(){ return image;}


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(original_title);
        dest.writeString(release_date);
        dest.writeString(poster_path);
        dest.writeDouble(vote_average);
        dest.writeString(overview);
        dest.writeInt(image.length);
        dest.writeByteArray(image);
    }
}
