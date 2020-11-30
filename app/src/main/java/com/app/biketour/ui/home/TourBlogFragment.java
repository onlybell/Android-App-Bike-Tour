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

import java.util.ArrayList;
import java.util.HashMap;

public class TourBlogFragment extends Fragment {
    private static String IP_ADDRESS = "http://192.168.1.64/jsonForApp/";
    private static String TAG = "BikeTour App - Blog List";
    private String mJsonString;

    ImageButton ibtnTopPopupMenu;
    ListView lvBlogList;
    ArrayList<HashMap<String,String>> tourBlogList;

    public static TourBlogFragment newInstance() {
        return new TourBlogFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_blog, container, false);

        // Visible top popup menu icon
        ibtnTopPopupMenu = (ImageButton) (getActivity()).findViewById(R.id.ibtnMenu);
        ibtnTopPopupMenu.setVisibility(View.VISIBLE);

        lvBlogList = (ListView) root.findViewById(R.id.lv_tour_blog);
        tourBlogList = new ArrayList<HashMap<String,String>>();
        getData("BlogAll");

        lvBlogList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get JSON of clicked data
                JSONObject data = ((JSONObject) lvBlogList.getItemAtPosition(position));
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

    private void showResultTourBlog(){
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);

            // Display News
            JSONArray jsonArrayBlog = jsonObject.getJSONArray("BlogAll");

            if (jsonArrayBlog.length() > 0) {

                Log.d(TAG, "showResult : " + jsonArrayBlog);
                TourBlogAdapter blogViewAdapter = new TourBlogAdapter(getActivity(), jsonArrayBlog);

                lvBlogList.setAdapter(blogViewAdapter);
            }

        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }
}
