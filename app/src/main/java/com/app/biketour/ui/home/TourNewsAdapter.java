package com.app.biketour.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app.biketour.R;

import org.json.JSONArray;
import org.json.JSONObject;

public class TourNewsAdapter extends BaseAdapter {
    private final Context Context;
    private final JSONArray jsonList;

    // Step 1
    public TourNewsAdapter(Context context, JSONArray jsonList) {
        this.Context = context;
        this.jsonList = jsonList;
    }

    // Step 2
    @Override
    public int getCount() {
        return this.jsonList.length();
    }

    // Step 3
    @Override
    public long getItemId(int position) {
        return 0;
    }

    // Step 4
    @Override
    public JSONObject getItem(int position)  {
        JSONObject curJson = new JSONObject();
        try {
            curJson = this.jsonList.getJSONObject( position );
        } catch( Exception e ){
            e.printStackTrace();
        }
        return curJson;
    }

    // Step 5
    @Override
    public View getView(int position, View convertView, ViewGroup parent)  {
        LayoutInflater inflater = (LayoutInflater) Context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        // For single column
        View  blogView = inflater.inflate(R.layout.adapter_tour_news, null);
        // For double column
        JSONObject BlogProduct = getItem(position);
        // Set adopter element into variable
        TextView  newsTitle     = (TextView) blogView.findViewById(R.id.tv_newsTitle);
        TextView  registerDate  = (TextView) blogView.findViewById(R.id.tv_newsRegisterDate);

        try {
            // Set into adabter imageview and textview
            newsTitle.setText( BlogProduct.getString("newsTitle") );
            registerDate.setText( BlogProduct.getString("registerDate") );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return blogView;
    }
}