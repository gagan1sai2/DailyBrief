package com.example.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

public class NewsItem implements Parcelable {
    public String title;
    public String description;
    public String url;

    public NewsItem(String title, String description, String url) {
        this.title = title;
        this.description = description;
        this.url = url;
    }

    protected NewsItem(Parcel in) {
        title = in.readString();
        description = in.readString();
        url = in.readString();
    }

    public static final Creator<NewsItem> CREATOR = new Creator<NewsItem>() {
        @Override
        public NewsItem createFromParcel(Parcel in) {
            return new NewsItem(in);
        }

        @Override
        public NewsItem[] newArray(int size) {
            return new NewsItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(url);
    }
}
