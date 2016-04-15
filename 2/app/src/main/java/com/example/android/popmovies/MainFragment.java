package com.example.android.popmovies;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {


    public MainFragment() {
        // Required empty public constructor
    }

    // Please insert your key to run here from github project
    private final String API_KEY = "";
//    private final String SORTING_BY_POPULARITY = "popularity.desc";
//    private final String SORTING_BY_RATING = "vote_average.desc&vote_count.gte=50";
    private final String SORTING_BY_POPULARITY = "popular";
    private final String SORTING_BY_RATING = "top_rated";

    //private final String BASE_URL_STRING = "http://api.themoviedb.org/3/discover/movie?sort_by=";
    private final String BASE_URL_STRING = "http://api.themoviedb.org/3/movie/";
    private final String BASE_URL_IMAGE_STRING = "http://image.tmdb.org/t/p/w185/";
    String sort_string = SORTING_BY_POPULARITY;
    MovieInfo[] mb;
    List<MovieInfo> mf; // favourite movie list

    void updateMovie()
    {
        new FetchMovieTask().execute();
    }
    void showFavourites()
    {
        GridView gv = (GridView)(getActivity().findViewById(R.id.gv));
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        String fav_list_string = sharedPref.getString("FAVOURITES", null);

        Type type = new TypeToken<List<MovieInfo>>(){}.getType();
        Gson gson = new Gson();
        mf = gson.fromJson(fav_list_string, type);

        MovieAdapter mvAdapter = new MovieAdapter(getActivity().getApplicationContext(), -1, mf);
        gv.setAdapter(mvAdapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if (!((MainActivity) getActivity()).isTabletMode()) {
                    Intent i = new Intent(getActivity().getApplicationContext(), DetailActivity.class);
                    i.putExtra("movie", mf.get(position));
                    startActivity(i);
                } else {
                    ((MainActivity) getActivity()).replaceFragment(mf.get(position));
                }
            }
        });
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        //SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        sort_string = sharedPref.getString("SORT_BY", SORTING_BY_POPULARITY);
        updateMovie();
        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();
        if (id == R.id.show_fav) {
            showFavourites();
        }
        else {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
            //noinspection SimplifiableIfStatement
            if (id == R.id.sort_by_pop) {
                sort_string = SORTING_BY_POPULARITY;
                updateMovie();
            }
            if (id == R.id.sort_by_rate) {
                sort_string = SORTING_BY_RATING;
                updateMovie();
            }
            SharedPreferences.Editor edit = sharedPref.edit();
            edit.putString("SORT_BY", sort_string);
            edit.commit();
        }
        //SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        //SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                //getSharedPreferences()getPreferences(Context.MODE_PRIVATE)

        return super.onOptionsItemSelected(item);
    }
    class FetchMovieTask extends AsyncTask<Void, Void, Void>
    {
        private static final String TAG_FETCH = "FETCH_MOVIE_TASK";

        FetchMovieTask()
        {
        }

        @Override
        protected void onPreExecute()
        {
            Log.d(TAG_FETCH, "PRE_EXECUTED");
        }
        @Override
        protected void onPostExecute(Void v) {
            MovieInfo[] obj = mb;
            if (obj != null) {
                for (int i = 0; i < obj.length; i++) {
                    Log.e(TAG_FETCH, obj[i].id);
                    Log.e(TAG_FETCH, obj[i].title);
                    Log.e(TAG_FETCH, obj[i].release_date);
                    Log.e(TAG_FETCH, obj[i].vote_average);
                    Log.e(TAG_FETCH, obj[i].overview);
                    Log.e(TAG_FETCH, obj[i].poster);
                }

                String baseImageUrl = BASE_URL_IMAGE_STRING;
                baseImageUrl += mb[0].poster;
                Log.e("DEBUGGING", baseImageUrl);

                GridView gv = (GridView)(getActivity().findViewById(R.id.gv));
                MovieAdapter mvAdapter = new MovieAdapter(getActivity().getApplicationContext(), -1, Arrays.asList(mb));
                gv.setAdapter(mvAdapter);
                gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View v, int position, long id)
                    {
                        if (! ((MainActivity)getActivity()).isTabletMode()) {
                            Intent i = new Intent(getActivity().getApplicationContext(), DetailActivity.class);
                            i.putExtra("movie", mb[position]);
                            startActivity(i);
                        }
                        else {
                            ((MainActivity)getActivity()).replaceFragment(mb[position]);
                        }
                    }
                });
            } else {
                Log.e(TAG_FETCH, "POST EXECUTE - NULL");
            }
        }

        private boolean checkInternetConnection() {
            ConnectivityManager check = (ConnectivityManager)getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo[] info = check.getAllNetworkInfo();
            Log.e(TAG_FETCH, "NUMBER OF CONNECTIONS:" + info.length);
            for (int i = 0; i < info.length; i++) {
                if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
            return false;
        }
        @Override
        protected Void doInBackground(Void... v)
        {
            if (checkInternetConnection() == false)
            {
                Log.e(TAG_FETCH, "NOT CONNECTED");
                return null;
            }
            else
            {
                Log.e(TAG_FETCH, "CONNECTED");
                try {
                    String api_key = API_KEY;
                    String ur = BASE_URL_STRING;
                    ur += sort_string;
                    ur += "?api_key=";
                    ur += api_key;
                    Log.e("DEBUGGING", ur);

                    URL url = new URL(ur);

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();

                    InputStream in = null;
                    int resCode = conn.getResponseCode();
                    if (resCode == HttpURLConnection.HTTP_OK) {
                        in = conn.getInputStream();
                    }
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    String webPage = "", data = "";
                    while ((data = reader.readLine()) != null) {
                        webPage += data + '\n';
                    }
                    Log.e(TAG_FETCH, webPage);
                    JSONObject jsObj = new JSONObject(webPage);
                    JSONArray jsArr = jsObj.getJSONArray("results");

                    mb = new MovieInfo[jsArr.length()];

                    for (int i = 0; i < jsArr.length(); i++) {
                        mb[i] = new MovieInfo();
                        JSONObject js_arr_obj = jsArr.getJSONObject(i);
                        mb[i].id = js_arr_obj.getString("id");
                        mb[i].title = js_arr_obj.getString("title");
                        mb[i].poster = js_arr_obj.getString("poster_path");
                        mb[i].overview = js_arr_obj.getString("overview");
                        mb[i].release_date = js_arr_obj.getString("release_date");
                        mb[i].vote_average = js_arr_obj.getString("vote_average");
                    }
                }
                catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

}
