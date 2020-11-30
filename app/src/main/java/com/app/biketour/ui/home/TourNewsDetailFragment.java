package com.app.biketour.ui.home;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.app.biketour.R;
import com.app.biketour.Utillity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TourNewsDetailFragment extends Fragment {
    private static String TAG = "BikeTour App - Tour News Detail";
    private String mJsonString;

    ImageButton ibtnTopPopupMenu;
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param";
    private String mParam;

    public static TourNewsDetailFragment newInstance(String param) {

        TourNewsDetailFragment fragment = new TourNewsDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Visible top popup menu icon
        ibtnTopPopupMenu = (ImageButton) (getActivity()).findViewById(R.id.ibtnMenu);
        ibtnTopPopupMenu.setVisibility(View.VISIBLE);

        View root = inflater.inflate(R.layout.fragment_news_detail, container, false);

        if (getArguments() != null) {
            mParam = getArguments().getString(ARG_PARAM1);
        }

        getData("News" , mParam);

        return root;
    }

    public void getData(String typeUrl, String resParam){

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

                return Utillity.getJsonData("Detail", params[0], mParam, null);
            }
        }

        GetDataJSON g = new GetDataJSON();
        g.execute(typeUrl);
    }

    private void showResultTourNews(){

        try {
            JSONObject jsonObject = new JSONObject(mJsonString);

            // Display Tour Product
            JSONArray jsonArrayNews = jsonObject.getJSONArray("News");

            if (jsonArrayNews.length() > 0) {
                Log.d(TAG, "showResult : " + jsonArrayNews);

                TextView newsTitle = (TextView) getActivity().findViewById(R.id.news_title);
                TextView newsRegisterDate = (TextView) getActivity().findViewById(R.id.news_register_date);
                TextView newsContent = (TextView) getActivity().findViewById(R.id.tv_news_detail);

                JSONObject jsonList = jsonArrayNews.getJSONObject(0);

                newsTitle.setText(jsonList.getString("newsTitle"));
                newsRegisterDate.setText(jsonList.getString("registerDate"));
                newsContent.setText(jsonList.getString("newsContent"));
            }

        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }
}
