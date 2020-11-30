package com.app.biketour.ui.tour;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.app.biketour.HomeActivity;
import com.app.biketour.R;
import com.app.biketour.Utillity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TourFragment extends Fragment {

    /**
     * History:
     *      Create - 17/04/2020, Jongil
     * Description:
     *      Tour Service Fragment
     * Function:
     *      TourFragment()
     *      onCreateView()
     */

    ImageButton ibtnTopPopupMenu;
    GridView gvTourProduct;

    private static String TAG = "BikeTour App - Tour Product List";
    private String mJsonString;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_tour, container, false);

        // Visible top popup menu icon
        ibtnTopPopupMenu = (ImageButton) (getActivity()).findViewById(R.id.ibtnMenu);
        ibtnTopPopupMenu.setVisibility(View.VISIBLE);

        gvTourProduct = (GridView) root.findViewById(R.id.gv_tourproduct);
        getData("TourProductAll");

        gvTourProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get JSON of clicked data
                JSONObject data = ((JSONObject) gvTourProduct.getItemAtPosition(position));
                ((HomeActivity)getActivity()).replaceFragment(TourDetilaFragment.newInstance(data.optString("pIdx")));
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
            JSONArray jsonArrayTourProduct = jsonObject.getJSONArray("TourProductAll");

            if (jsonArrayTourProduct.length() > 0) {
                //Creating GridViewAdapter Object
                TourProductAdapter gridViewAdapter = new TourProductAdapter(getActivity(), jsonArrayTourProduct);

                //Adding adapter to gridview
                gvTourProduct.setAdapter(gridViewAdapter);
                //Log.d(TAG, "==========> Tour Product Result : " + jsonArrayTourProduct);

            }

        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }

}
