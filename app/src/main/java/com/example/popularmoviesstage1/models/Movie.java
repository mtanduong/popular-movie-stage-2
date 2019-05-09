package com.example.popularmoviesstage1.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Movie implements Parcelable {

    @SerializedName("id")
    private String mId;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("overview")
    private String mOverview;
    @SerializedName("release_date")
    private String mReleaseDate;
    @SerializedName("vote_average")
    private String mUserRating;
    @SerializedName("poster_path")
    private String mThumbnailImgUrl;
    //@SerializedName("baseImgUrl")
    private String mPosterUrl = "";

    private String BASE_URL_THUMB = "https://image.tmdb.org/t/p/w185";
    private String BASE_URL_POSTER = "https://image.tmdb.org/t/p/w300";

    protected Movie(Parcel in) {
        mId = in.readString();
        mTitle = in.readString();
        mOverview = in.readString();
        mReleaseDate = in.readString();
        mUserRating = in.readString();
        mThumbnailImgUrl = in.readString();
        //mPosterUrl = in.readString();
        //BASE_URL_THUMB = in.readString();
        //BASE_URL_POSTER = in.readString();
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

    public String getmId() { return mId; }

    public String getmTitle() {
        return mTitle;
    }

    public String getmOverview() {
        return mOverview;
    }

    public String getmReleaseDate() {
        String baseDate = mReleaseDate;
        String[] parts = baseDate.split("-");
        String releaseDate = parts[1] + "/" + parts[2] + "/" + parts[0];
        return releaseDate;
    }

    public String getmUserRating() {
        return mUserRating;
    }

    public String getmThumbnailImgUrl() {
        return "https://image.tmdb.org/t/p/w185" + mThumbnailImgUrl;
    }

    public String getmPosterUrl() {
        return "https://image.tmdb.org/t/p/w300" + mThumbnailImgUrl;
    }

    public String getmYear() {
        String baseDate = mReleaseDate;
        String[] parts = baseDate.split("-");
        String releaseYear = parts[0];
        return releaseYear;
    }

    public void setmId(String mId) { this.mId = mId; }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public void setmOverview(String mOverview) {
        this.mOverview = mOverview;
    }

    public void setmReleaseDate(String mReleaseDate) {
        this.mReleaseDate = mReleaseDate;
    }

    public void setmUserRating(String mUserRating) {
        this.mUserRating = mUserRating;
    }

    public void setmThumbnailImgUrl(String mThumbnailImgUrl) {
        this.mThumbnailImgUrl = mThumbnailImgUrl;
    }

    public void setmPosterUrl(String mPosterUrl) {
        this.mPosterUrl = mPosterUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mId);
        parcel.writeString(mTitle);
        parcel.writeString(mOverview);
        parcel.writeString(mReleaseDate);
        parcel.writeString(mUserRating);
        parcel.writeString(mThumbnailImgUrl);
        //parcel.writeString(mPosterUrl);
        //parcel.writeString(BASE_URL_THUMB);
        //parcel.writeString(BASE_URL_POSTER);
    }
}
