package com.app.biketour.ui.tour;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.biketour.GetImageFromUrl;
import com.app.biketour.R;

import org.json.JSONArray;
import org.json.JSONObject;

public class TourProductAdapter extends BaseAdapter {
    private final android.content.Context Context;
    private final JSONArray jsonList;

    // Step 1
    public TourProductAdapter(Context context,JSONArray jsonList) {
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
        View  gridView = inflater.inflate(R.layout.adapter_tour_product, null);
        // For double column
        // View  gridView = inflater.inflate(R.layout.grid_adapter_double_col, null);
        JSONObject SingleProduct = getItem(position);
        // Set adopter element into variable
        ImageView tourImage  = (ImageView) gridView.findViewById(R.id.tour_image);
        TextView  tourTitle  = (TextView) gridView.findViewById(R.id.tour_title);

        try {
            // Set into adabter imageview and textview
            new GetImageFromUrl(tourImage).execute(SingleProduct.getString("imgSrc"));
            tourTitle.setText( SingleProduct.getString("tourTitle") );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gridView;
    }
}
