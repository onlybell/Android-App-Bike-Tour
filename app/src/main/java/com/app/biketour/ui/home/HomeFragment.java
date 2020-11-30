package com.app.biketour.ui.home;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.app.biketour.HomeActivity;
import com.app.biketour.R;
import com.app.biketour.Utillity;
import com.app.biketour.ui.tour.TourDetilaFragment;
import com.app.biketour.ui.tour.TourProductAdapter;
import com.app.biketour.ui.user.UserContactFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class HomeFragment extends Fragment {

    /**
     * Description:
     *      Main Fragment
     * Function:
     *      HomeFragment()
     *      onCreateView()
     */

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    private static String TAG = "BikeTour App";
    private String mJsonString;

    TextView btnAbout, btnTour, btnNews, btnBlog, btnContact;
    ImageButton ibtnTopPopupMenu;
    ListView lvMainNews, lvMainBlog;
    GridView gvMainTourProduct;
    ArrayList<HashMap<String,String>> tourNewsList, tourBlogList;
    BottomNavigationView bottomNavigationView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        bottomNavigationView = (getActivity()).findViewById(R.id.nav_view);

        // Invisible top popup menu icon
        ibtnTopPopupMenu = (ImageButton) (getActivity()).findViewById(R.id.ibtnMenu);
        ibtnTopPopupMenu.setVisibility(View.INVISIBLE);

        btnAbout = (TextView) root.findViewById(R.id.btnMainTopAbout);
        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity)getActivity()).replaceFragment(AboutFragment.newInstance());
            }
        });

        btnTour = (TextView) root.findViewById(R.id.btnMainTopTour);
        btnTour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomNavigationView.getMenu().findItem(R.id.navigation_tour).setChecked(true);
                bottomNavigationView.setSelectedItemId(R.id.navigation_tour);
            }
        });

        btnNews = (TextView) root.findViewById(R.id.btnMainTopNews);
        btnNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity)getActivity()).replaceFragment(TourNewsFragment.newInstance());
            }
        });

        btnBlog = (TextView) root.findViewById(R.id.btnMainTopBlog);
        btnBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity)getActivity()).replaceFragment(TourBlogFragment.newInstance());
            }
        });

        btnContact = (TextView) root.findViewById(R.id.btnMainTopContact);
        btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity)getActivity()).replaceFragment(UserContactFragment.newInstance());
            }
        });

        gvMainTourProduct = (GridView) root.findViewById(R.id.gv_main_tourproduct);
        getData("TourProduct");

        gvMainTourProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bottomNavigationView.getMenu().findItem(R.id.navigation_tour).setChecked(true);

                // Get JSON of clicked data
                JSONObject data = ((JSONObject) gvMainTourProduct.getItemAtPosition(position));
                ((HomeActivity)getActivity()).replaceFragment(TourDetilaFragment.newInstance(data.optString("pIdx")));
            }
        });

        lvMainNews = (ListView) root.findViewById(R.id.lv_main_news);
        tourNewsList = new ArrayList<HashMap<String,String>>();
        getData("News");

        lvMainNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bottomNavigationView.getMenu().findItem(R.id.navigation_home).setChecked(true);

                // Get JSON of clicked data
                JSONObject data = ((JSONObject) lvMainNews.getItemAtPosition(position));
                ((HomeActivity)getActivity()).replaceFragment(TourNewsDetailFragment.newInstance(data.optString("nIdx")));
            }
        });

        lvMainBlog = (ListView) root.findViewById(R.id.lv_main_blog);
        tourBlogList = new ArrayList<HashMap<String,String>>();
        getData("Blog");

        lvMainBlog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bottomNavigationView.getMenu().findItem(R.id.navigation_home).setChecked(true);

                // Get JSON of clicked data
                JSONObject data = ((JSONObject) lvMainBlog.getItemAtPosition(position));
                ((HomeActivity)getActivity()).replaceFragment(TourBlogDetailFragment.newInstance(data.optString("bIdx")));
            }
        });

        return root;
    }

    public void getData(String typeUrl){

        class GetDataJSON extends AsyncTask<String, Void, String> {
            String errorString = null;

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                if (result == null) {
                    Log.d(TAG, " Result is null ");
                } else {
                    mJsonString = result;
                    showResultTourProduct();
                    showResultTourNews();
                    showResultTourBlog();
                }
            }

            @Override
            protected String doInBackground(String... params) {

                return Utillity.getJsonData("List", params[0],null, null);
            }
        }

        GetDataJSON g = new GetDataJSON();
        g.execute(typeUrl);
    }

    private void showResultTourProduct(){

        try {
            JSONObject jsonObject = new JSONObject(mJsonString);

            // Display Tour Product
            JSONArray jsonArrayTourProduct = jsonObject.getJSONArray("TourProduct");

            if (jsonArrayTourProduct.length() > 0) {
                //Log.d(TAG, "showResult : " + jsonArrayTourProduct);
                //Creating GridViewAdapter Object
                TourProductAdapter gridViewAdapter = new TourProductAdapter(getActivity(), jsonArrayTourProduct);
                //Adding adapter to gridview
                gvMainTourProduct.setAdapter(gridViewAdapter);
            }

        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }


    private void showResultTourNews(){
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);

            // Display News
            JSONArray jsonArrayNews = jsonObject.getJSONArray("News");

            if (jsonArrayNews.length() > 0) {
                TourNewsAdapter newsViewAdapter = new TourNewsAdapter(getActivity(), jsonArrayNews);

                lvMainNews.setAdapter(newsViewAdapter);
                Utillity.setListViewHeightBasedOnChildren(lvMainNews);
            }

        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }

    private void showResultTourBlog(){
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);

            // Display News
            JSONArray jsonArrayBlog = jsonObject.getJSONArray("Blog");

            if (jsonArrayBlog.length() > 0) {
                TourBlogAdapter blogViewAdapter = new TourBlogAdapter(getActivity(), jsonArrayBlog);

                lvMainBlog.setAdapter(blogViewAdapter);
                Utillity.setListViewHeightBasedOnChildren(lvMainBlog);
            }

        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }
}
