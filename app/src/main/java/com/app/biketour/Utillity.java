package com.app.biketour;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class Utillity {

    /**
     * Description:
     *      Utitility
     * Function:
     *      setListViewHeightBasedOnChildren() - Bottom navigation
     *      showMessage() - Display toasty message
     *      getJsonData() - Get Json data from URL
     *      FinalDataParse() -
     */

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            //listItem.measure(0, 0);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight;
        listView.setLayoutParams(params);

        listView.requestLayout();
    }


    public static void showMessage(Context context, String messageType, String txtMessage) {
        switch (messageType) {
            case "ERROR":
                Toasty.error(context , txtMessage, Toast.LENGTH_SHORT).show();
                break;
            case "SUCCESS":
                Toasty.success(context, txtMessage, Toast.LENGTH_SHORT).show();
                break;
            case "INFO":
                Toasty.info(context, txtMessage, Toast.LENGTH_SHORT).show();
                break;
            case "WARNING":
                Toasty.warning(context, txtMessage, Toast.LENGTH_SHORT).show();
                break;
            case "NORMAL":
                Toasty.normal(context, txtMessage, Toast.LENGTH_SHORT, ContextCompat.getDrawable(context, R.drawable.ic_bike)).show();
                break;
        }
    }

    public static String getJsonData(String jsonType, @Nullable String param1, @Nullable String param2, @Nullable List<String> params) {
        String serverURL = "";
        String responseParams;

        if (jsonType.equals("List")) {
            serverURL = "http://192.168.1.64/jsonForApp/" + param1;
        }
        else if (jsonType.equals("Detail")) {
            serverURL = "http://192.168.1.64/jsonForAppDetail/" + param1 + "/" + param2;
        }
        else if (jsonType.equals("Login")) {
            serverURL = "http://192.168.1.64/jsonForAppLogin/" + param1 + "/" + param2;
        }
        else if (jsonType.equals("Signup")) {
            responseParams = FinalDataParse(params);
            serverURL = "http://192.168.1.64/jsonForAppSignup" + responseParams;
        }
        else if (jsonType.equals("Profile")) {
            responseParams = FinalDataParse(params);
            serverURL = "http://192.168.1.64/jsonForAppProfile" + responseParams;
        }
        else if (jsonType.equals("Password")) {
            serverURL = "http://192.168.1.64/jsonForAppPassword/" + param1 + "/" + param2;
        }

        Log.d("=======> Server URL : ", serverURL);

        try {
            URL url = new URL(serverURL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setReadTimeout(5000);
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.connect();

            int responseStatusCode = httpURLConnection.getResponseCode();

            InputStream inputStream;
            if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                inputStream = httpURLConnection.getInputStream();
            } else {
                inputStream = httpURLConnection.getErrorStream();
            }

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }

            bufferedReader.close();

            return sb.toString().trim();

        } catch (Exception e) {
            Log.d("Get Json Data Error", "Error ", e);

            return null;
        }
    }

    public static String FinalDataParse(List<String> listParams) {
        String Result = "";
        StringBuilder stringBuilder = new StringBuilder();

        try{
            for(int i=0; i < listParams.size(); i++) {
                stringBuilder.append("/");
                stringBuilder.append(URLEncoder.encode(listParams.get(i), "UTF-8"));
            }

            Result = stringBuilder.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return Result ;
    }

}
