package com.example.popularmoviesstage1.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Video implements Parcelable {

    @SerializedName("id")
    private String mId;
    @SerializedName("key")
    private String mKey;
    @SerializedName("name")
    private String mName;
    @SerializedName("site")
    private String mSite;
    @SerializedName("size")
    private String mSize;
    @SerializedName("type")
    private String mType;

    protected Video(Parcel in) {
        mId = in.readString();
        mKey = in.readString();
        mName = in.readString();
        mSite = in.readString();
        mSize = in.readString();
        mType = in.readString();
    }

    public static final Creator<Video> CREATOR = new Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel in) {
            return new Video(in);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };

    public String getmId() {
        return mId;
    }

    public String getmKey() {
        return mKey;
    }

    public String getmName() {
        return mName;
    }

    public String getmSite() {
        return mSite;
    }

    public String getmSize() {
        return mSize;
    }

    public String getmType() {
        return mType;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public void setmKey(String mKey) {
        this.mKey = mKey;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public void setmSite(String mSite) {
        this.mSite = mSite;
    }

    public void setmSize(String mSize) {
        this.mSize = mSize;
    }

    public void setmType(String mType) {
        this.mType = mType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mKey);
        dest.writeString(mName);
        dest.writeString(mSite);
        dest.writeString(mSize);
        dest.writeString(mType);
    }
}
