package com.example.popularmoviesstage1.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorites_table")
public class Movie implements Parcelable {

    @PrimaryKey
    @NonNull
    @SerializedName("id")
    private String id;

    @SerializedName("title")
    private String title;

    @SerializedName("overview")
    private String overview;

    @SerializedName("release_date")
    private String releaseDate;

    @SerializedName("vote_average")
    private String userRating;

    @SerializedName("poster_path")
    private String thumbnailImgUrl;

    public Movie(String id, String title, String overview, String releaseDate, String userRating, String thumbnailImgUrl) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.userRating = userRating;
        this.thumbnailImgUrl = thumbnailImgUrl;
    }

    @Ignore
    protected Movie(Parcel in) {
        id = in.readString();
        title = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        userRating = in.readString();
        thumbnailImgUrl = in.readString();
    }

    @Ignore
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

    public String getId() {
        return id;
    }

    public String getTitle() {

        return title;
    }

    public String getOverview() {

        return overview;
    }

    public String getReleaseDate() {

        return releaseDate;
    }

    public String getUserRating() {

        return userRating;
    }

    public String getThumbnailImgUrl() {
        return thumbnailImgUrl;
    }

    public String getPosterUrl() {
        return thumbnailImgUrl;
    }

    public String getmYear() {
        String baseDate = releaseDate;
        String[] parts = baseDate.split("-");
        String releaseYear = parts[0];
        return releaseYear;
    }

    public void setId(String id) {

        this.id = id;
    }

    public void setTitle(String title) {

        this.title = title;
    }

    public void setmOverview(String overview) {

        this.overview = overview;
    }

    public void setmReleaseDate(String mReleaseDate) {

        this.releaseDate = mReleaseDate;
    }

    public void setmUserRating(String mUserRating) {

        this.userRating = mUserRating;
    }

    public void setmThumbnailImgUrl(String mThumbnailImgUrl) {

        this.thumbnailImgUrl = mThumbnailImgUrl;
    }

    @Override
    public int describeContents() {

        return 0;
    }

    @Ignore
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(overview);
        parcel.writeString(releaseDate);
        parcel.writeString(userRating);
        parcel.writeString(thumbnailImgUrl);
    }
}
