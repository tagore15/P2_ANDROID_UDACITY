package com.example.android.popmovies;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by jmd on 3/6/2016.
 */
public class MovieInfo implements Parcelable {
    String id;
    String title;
    String overview;
    String vote_average;
    String release_date;
    String poster;

    public int describeContents() {
        return 0;
    }
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(id);
        out.writeString(title);
        out.writeString(overview);
        out.writeString(vote_average);
        out.writeString(release_date);
        out.writeString(poster);
    }
    public static final Parcelable.Creator<MovieInfo> CREATOR
            = new Parcelable.Creator<MovieInfo>() {
        public MovieInfo createFromParcel(Parcel in) {
            return new MovieInfo(in);
        }
        public MovieInfo[] newArray(int size) {
            return new MovieInfo[size];
        }
    };
    private MovieInfo(Parcel in) {
        id = in.readString();
        title = in.readString();
        overview = in.readString();
        vote_average = in.readString();
        release_date = in.readString();
        poster = in.readString();
    }
    public MovieInfo(){}
}

class MovieAdapter extends ArrayAdapter<MovieInfo>
{
    public MovieAdapter(Context context, int resource, List<MovieInfo> objects)
    {
        super(context, resource, objects);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ImageView iv;
        if (convertView == null) {
            iv = new ImageView(getContext());
            iv.setAdjustViewBounds(true);
        }
        else {
            iv = (ImageView)convertView;
        }
        MovieInfo mb = getItem(position);
        String baseImageUrl = "http://image.tmdb.org/t/p/w185/";
        baseImageUrl += mb.poster;
        Picasso.with(getContext()).load(baseImageUrl).into(iv);
        return iv;
    }
}

class TrailerAdapter extends ArrayAdapter<String>
{
    private Context mContext;
    public TrailerAdapter(Context context, int resource, List<String> objects)
    {
        super(context, resource, objects);
        mContext = context;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ImageView iv;
        if (convertView == null) {
            //iv = new ImageView(mContext);
            LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
            iv = (ImageView)(inflater.inflate(R.layout.trailer_image, null));
           // iv.setAdjustViewBounds(true);
        }
        else {
            iv = (ImageView)convertView;
        }
        String key = getItem(position);
        String baseImageUrl = "http://img.youtube.com/vi/";
        baseImageUrl += key;
        baseImageUrl += "/1.jpg";
        Picasso.with(mContext).load(baseImageUrl).placeholder(R.mipmap.ic_launcher).into(iv);
        return iv;
    }
}
