package com.appdeveloper.rh.giphyapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Roman on 11/4/2017.
 */

public class GifObject implements Parcelable{

    String title;
    String imageUrl;
   // Bitmap gifImage;

    GifObject(String title, String imageUrl){//} Bitmap photo){
        this.title = title;
        this.imageUrl = imageUrl;
      //  this.gifImage = photo;
    }

    protected GifObject(Parcel in) {
        title = in.readString();
        imageUrl = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(imageUrl);
    }

    public static final Creator<GifObject> CREATOR = new Creator<GifObject>() {
        @Override
        public GifObject createFromParcel(Parcel in) {
            return new GifObject(in);
        }

        @Override
        public GifObject[] newArray(int size) {
            return new GifObject[size];
        }
    };
}
