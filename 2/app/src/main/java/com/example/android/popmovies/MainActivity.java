package com.example.android.popmovies;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;

// create a fragment in P2
public class MainActivity extends ActionBarActivity {

    private Boolean mTabletMode = false;

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

/*    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.sort_by_pop) {
            sort_string = SORTING_BY_POPULARITY;
            updateMovie();
        }
        if (id == R.id.sort_by_rate) {
            sort_string = SORTING_BY_RATING;
            updateMovie();
        }
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPref.edit();
        edit.putString("SORT_BY", sort_string);
        edit.commit();

        return super.onOptionsItemSelected(item);
    }*/

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

