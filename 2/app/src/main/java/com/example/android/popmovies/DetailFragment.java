package com.example.android.popmovies;


import android.os.Bundle;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {


    public DetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rv = inflater.inflate(R.layout.fragment_detail, container, false);
        if (getArguments() == null) return rv;
        MovieInfo mb1 = getArguments().getParcelable("movie");
        Log.e("TITLE1", mb1.title);
        //setContentView(R.layout.activity_detail);
        TextView tv = (TextView)(rv.findViewById(R.id.title));
        tv.setText(mb1.title);

        ImageView iv = (ImageView)rv.findViewById(R.id.poster);
        String baseImageUrl = "http://image.tmdb.org/t/p/w185/";
        baseImageUrl += mb1.poster;
        Picasso.with(getActivity().getApplicationContext()).load(baseImageUrl).into(iv);

        tv = (TextView)rv.findViewById(R.id.plot);
        tv.setText(mb1.overview);

        String userLabel = "USER RATING: ";
        tv = (TextView)rv.findViewById(R.id.user_rating);
        tv.setText(userLabel + mb1.vote_average);

        String releaseLabel = "RELEASE DATE: ";
        tv = (TextView)rv.findViewById(R.id.release_date);
        tv.setText(releaseLabel + mb1.release_date);
        return rv;
    }
}
