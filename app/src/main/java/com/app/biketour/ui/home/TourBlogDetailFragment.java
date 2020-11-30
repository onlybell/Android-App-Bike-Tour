package com.app.biketour.ui.home;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TourBlogDetailFragment extends Fragment {
    private static String TAG = "BikeTour App - Tour News Detail";
    private String mJsonString;

    ImageButton ibtnTopPopupMenu;
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param";
    private String mParam;

    public static TourBlogDetailFragment newInstance(String param) {

        TourBlogDetailFragment fragment = new TourBlogDetailFragment();
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

        View root = inflater.inflate(R.layout.fragment_blog_detail, container, false);

        if (getArguments() != null) {
            mParam = getArguments().getString(ARG_PARAM1);
        }

        getData("Blog" , mParam);

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
                    showResultTourBlog();
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

    private void showResultTourBlog(){

        try {
            JSONObject jsonObject = new JSONObject(mJsonString);

            // Display Tour Product
            JSONArray jsonArrayBlog = jsonObject.getJSONArray("Blog");

            if (jsonArrayBlog.length() > 0) {

                ImageView blogImage = (ImageView) getActivity().findViewById(R.id.blog_image);
                TextView blogTitle = (TextView) getActivity().findViewById(R.id.blog_title);
                TextView blogRegisterDate = (TextView) getActivity().findViewById(R.id.blog_register_date);
                TextView blogContent = (TextView) getActivity().findViewById(R.id.tv_blog_detail);

                JSONObject jsonList = jsonArrayBlog.getJSONObject(0);
                Log.d(TAG, "showResult : " + jsonArrayBlog);

                new GetImageFromUrl(blogImage).execute(jsonList.getString("imgSrc"));
                blogTitle.setText(jsonList.getString("blogTitle"));
                blogRegisterDate.setText(jsonList.getString("registerDate"));
                blogContent.setText(jsonList.getString("blogContent"));
            }

        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }
}
