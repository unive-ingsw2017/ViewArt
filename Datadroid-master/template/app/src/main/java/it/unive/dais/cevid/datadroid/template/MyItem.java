package it.unive.dais.cevid.datadroid.template;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import java.io.Serializable;


public class MyItem implements ClusterItem, Serializable, Parcelable {
    private LatLng mPosition;
    private String mTitle;
    private String mSnippet;
    private String url;

    public MyItem(double lat, double lng) {
        mPosition = new LatLng(lat, lng);
    }

    public MyItem(double lat, double lng, String title, String snippet, String url) {
        mPosition = new LatLng(lat, lng);
        mTitle = title;
        mSnippet = snippet;
        this.url = url;
    }

    protected MyItem(Parcel in) {
        mPosition = in.readParcelable(LatLng.class.getClassLoader());
        mTitle = in.readString();
        mSnippet = in.readString();
        url = in.readString();
    }

    public static final Creator<MyItem> CREATOR = new Creator<MyItem>() {
        @Override
        public MyItem createFromParcel(Parcel in) {
            return new MyItem(in);
        }

        @Override
        public MyItem[] newArray(int size) {
            return new MyItem[size];
        }
    };

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public String getSnippet() {
        return mSnippet;
    }

    public String getUrl() {return url;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(mPosition, flags);
        dest.writeString(mTitle);
        dest.writeString(mSnippet);
        dest.writeString(url);
    }
}
