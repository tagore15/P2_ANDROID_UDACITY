package com.example.android.popmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class DetailActivity extends AppCompatActivity {

    private DetailFragment detailFragment;
    public void markAsFavourite(View v)
    {
        detailFragment.markAsFavourite(v);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            MovieInfo mb1 = getIntent().getParcelableExtra("movie");
            Bundle args = new Bundle();
            args.putParcelable("movie", mb1);

            detailFragment = new DetailFragment();
            detailFragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, detailFragment).commit();
        }
    }
}
