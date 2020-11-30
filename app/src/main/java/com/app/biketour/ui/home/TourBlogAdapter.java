package com.app.biketour.ui.home;

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

public class TourBlogAdapter extends BaseAdapter {
    private final Context Context;
    private final JSONArray jsonList;

    // Step 1
    public TourBlogAdapter(Context context, JSONArray jsonList) {
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
        View  blogView = inflater.inflate(R.layout.adapter_tour_blog, null);
        // For double column
        JSONObject BlogProduct = getItem(position);
        // Set adopter element into variable
        ImageView BlogImageView  = (ImageView) blogView.findViewById(R.id.main_blog_image);
        TextView  BlogTitle      = (TextView) blogView.findViewById(R.id.tv_blogTitle);

        try {
            // Set into adabter imageview and textview
            new GetImageFromUrl(BlogImageView).execute(BlogProduct.getString("imgSrc"));
            BlogTitle.setText( BlogProduct.getString("blogTitle") );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return blogView;
    }
}