package com.example.android.popmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

// create a fragment in P2
public class MainActivity extends AppCompatActivity {

    private Boolean mTabletMode = false;
  //  private DetailFragment detailFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.container) != null)
        {
            mTabletMode = true;
        }
    }

    public boolean isTabletMode()
    {
        return mTabletMode;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void replaceFragment(MovieInfo mb) {

        Bundle args = new Bundle();
        args.putParcelable("movie", mb);

        DetailFragment detailFragment = new DetailFragment();
        detailFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, detailFragment).commit();
    }
}

