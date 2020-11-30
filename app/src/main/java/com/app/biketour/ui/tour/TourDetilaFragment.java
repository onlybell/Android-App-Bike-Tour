package com.app.biketour.ui.tour;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.app.biketour.GetImageFromUrl;
import com.app.biketour.R;
import com.app.biketour.Utillity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TourDetilaFragment extends Fragment {

    /**
     * History:
     *      Create - 20/04/2020, Jongil
     * Description:
     *      Detail information of Tour Service Fragment
     * Function:
     *      TourDetilaFragment()
     *      onCreateView()
     */
    private static String IP_ADDRESS = "http://192.168.1.64/jsonForAppDetail/";
    private static String TAG = "BikeTour App - Tour Product List";
    private String mJsonString;

    ImageButton ibtnTopPopupMenu;
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param";
    private String mParam;

    public static TourDetilaFragment newInstance(String param) {

        TourDetilaFragment fragment = new TourDetilaFragment();
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

        View root = inflater.inflate(R.layout.fragment_tour_detail, container, false);

        if (getArguments() != null) {
            mParam = getArguments().getString(ARG_PARAM1);
        }

        getData("TourProduct" , mParam);

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
                    showResultTourProduct();
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

    private void showResultTourProduct(){

        try {
            JSONObject jsonObject = new JSONObject(mJsonString);

            // Display Tour Product
            JSONArray jsonArrayTourProduct = jsonObject.getJSONArray("TourProduct");

            if (jsonArrayTourProduct.length() > 0) {

                ImageView tourImage = (ImageView) getActivity().findViewById(R.id.tour_image);
                TextView tourTitle = (TextView) getActivity().findViewById(R.id.tour_title);
                TextView tourSubTitle = (TextView) getActivity().findViewById(R.id.tour_sub_title);
                TextView tourContent = (TextView) getActivity().findViewById(R.id.tv_tour_detail);

                JSONObject jsonList = jsonArrayTourProduct.getJSONObject(0);

                new GetImageFromUrl(tourImage).execute(jsonList.getString("imgSrc"));
                tourTitle.setText(jsonList.getString("tourTitle"));
                tourSubTitle.setText(jsonList.getString("tourSubTitle"));
                tourContent.setText(jsonList.getString("tourContent"));
            }

        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }
}
