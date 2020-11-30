package com.app.biketour.ui.home;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.app.biketour.HomeActivity;
import com.app.biketour.R;
import com.app.biketour.Utillity;

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

public class TourNewsFragment extends Fragment {

    private static String TAG = "BikeTour App - News List";
    private String mJsonString;

    ImageButton ibtnTopPopupMenu;
    ListView lvNewsList;
    ArrayList<HashMap<String,String>> tourNewsList;

    public static TourNewsFragment newInstance() {
        return new TourNewsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_news, container, false);

        // Visible top popup menu icon
        ibtnTopPopupMenu = (ImageButton) (getActivity()).findViewById(R.id.ibtnMenu);
        ibtnTopPopupMenu.setVisibility(View.VISIBLE);

        lvNewsList = (ListView) root.findViewById(R.id.lv_tour_news);
        tourNewsList = new ArrayList<HashMap<String,String>>();
        getData("NewsAll");

        lvNewsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get JSON of clicked data
                JSONObject data = ((JSONObject) lvNewsList.getItemAtPosition(position));
                ((HomeActivity)getActivity()).replaceFragment(TourNewsDetailFragment.newInstance(data.optString("nIdx")));
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
                    showResultTourNews();
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

    private void showResultTourNews(){
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);

            // Display News
            JSONArray jsonArrayNews = jsonObject.getJSONArray("NewsAll");

            if (jsonArrayNews.length() > 0) {
                TourNewsAdapter newsViewAdapter = new TourNewsAdapter(getActivity(), jsonArrayNews);

                lvNewsList.setAdapter(newsViewAdapter);
                //Utillity.setListViewHeightBasedOnChildren(lvNewsList);
            }

        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }

}
