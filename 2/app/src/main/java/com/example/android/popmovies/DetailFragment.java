package com.example.android.popmovies;


import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

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
import java.util.ArrayList;
import java.util.List;

//import android.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {

    private final String REVIEW_URL  = "http://api.themoviedb.org/3/movie/";

    private MovieInfo mb1;
    List<String> ls = null;
    List<String> vs = null;

    public DetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rv = inflater.inflate(R.layout.fragment_detail, container, false);
        if (getArguments() == null) return rv;
        mb1 = getArguments().getParcelable("movie");
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

        new FetchMovieReviews().execute();
        return rv;
    }

    public void markAsFavourite(View v)
    {
        Type type = new TypeToken<List<MovieInfo>>(){}.getType();
        Gson gson = new Gson();
        //SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        String fav_list_string = sharedPref.getString("FAVOURITES", null);
        List<MovieInfo> favMovies = null;
        if (fav_list_string == null)
        {
            favMovies = new ArrayList<MovieInfo>();
        }
        else
        {
            favMovies = gson.fromJson(fav_list_string, type);
        }

        favMovies.add(mb1);

        fav_list_string = gson.toJson(favMovies);
        Log.e("FAVOURITE MOVIES", fav_list_string);
        Log.e("TITLE1", mb1.title);
        SharedPreferences.Editor edit = sharedPref.edit();
        edit.putString("FAVOURITES", fav_list_string);
        edit.commit();
    }
    class FetchMovieReviews extends AsyncTask<Void, Void, Void>
    {
        FetchMovieReviews()
        {}
        @Override
        protected void onPreExecute()
        {}

        @Override
        protected void onPostExecute(Void v)
        {
            ListView tv = (ListView) (getActivity().findViewById(R.id.trailerList));
            TrailerAdapter tvAdapter = new TrailerAdapter(getActivity().getApplicationContext(), -1, vs);
            tv.setAdapter(tvAdapter);
            tv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + vs.get(position)));
                    startActivity(intent);
                }
            });

            ListView lv = (ListView) (getActivity().findViewById(R.id.reviewList));
            // ReviewAdapter rvAdapter = new ReviewAdapter(getActivity().this, -1, ls);
            ArrayAdapter<String> rvAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.custom_text_view, ls);
            lv.setAdapter(rvAdapter);
        };

        @Override
        protected Void doInBackground (Void...v)
        {
            try {
                // get reviews JSON through internet
                String ur = REVIEW_URL;
                ur += mb1.id;
                ur += "/reviews";
                ur += "?api_key=";
                ur += getString(R.string.api_key);
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
                Log.e("REVIEW_FETCHING", webPage);
                JSONObject jsObj = new JSONObject(webPage);
                JSONArray jsArr = jsObj.getJSONArray("results");
                ls = new ArrayList<String>();
                for (int i = 0; i < jsArr.length(); i++) {
                    String st = new String();
                    JSONObject js_arr_obj = jsArr.getJSONObject(i);
                    st = js_arr_obj.getString("content");
                    ls.add(st);
                }

                ur = REVIEW_URL;
                ur += mb1.id;
                ur += "/videos";
                ur += "?api_key=";
                ur += getString(R.string.api_key);
                Log.e("DEBUGGING", ur);

                url = new URL(ur);
                conn = (HttpURLConnection) url.openConnection();
                conn.connect();

                in = null;
                resCode = conn.getResponseCode();
                if (resCode == HttpURLConnection.HTTP_OK) {
                    in = conn.getInputStream();
                }
                reader = new BufferedReader(new InputStreamReader(in));
                webPage = "";
                data = "";
                while ((data = reader.readLine()) != null) {
                    webPage += data + '\n';
                }
                Log.e("TRAILER_FETCHING", webPage);
                jsObj = new JSONObject(webPage);
                jsArr = jsObj.getJSONArray("results");
                vs = new ArrayList<String>();
                for (int i = 0; i < jsArr.length(); i++) {
                    String st = new String();
                    JSONObject js_arr_obj = jsArr.getJSONObject(i);
                    if (js_arr_obj.getString("type").equals(("Trailer"))) {
                        st = js_arr_obj.getString("key");
                        Log.e("TRAILER", st);
                        vs.add(st);
                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
