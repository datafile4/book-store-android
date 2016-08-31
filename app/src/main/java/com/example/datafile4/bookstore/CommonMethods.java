package com.example.datafile4.bookstore;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.datafile4.bookstore.Config.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by datafile4 on 8/15/16.
 */
public class CommonMethods {
    public static void showToast(String message, Context context) {
        CharSequence text = message;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public static String createFilterJSONString(JSONArray langs, JSONArray genres, int lowPrice, int highPrice, JSONArray searchItems, int pageNum, int pageLength){
        JSONObject defaultParameter = new JSONObject();
        try {
            defaultParameter.put(Constants.KEY_FILTER_LANGIDS,langs);
            defaultParameter.put(Constants.KEY_FILTER_GENREIDS,genres);
            defaultParameter.put(Constants.KEY_FILTER_LOWPRICE,lowPrice);
            defaultParameter.put(Constants.KEY_FILTER_HIGHPRICE,highPrice);
            defaultParameter.put(Constants.KEY_FILTER_SEARCHTERMS,searchItems);
            JSONObject pagination = new JSONObject();
            pagination.put(Constants.KEY_FILTER_PAGENUMBER,pageNum);
            pagination.put(Constants.KEY_FILTER_PAGELENGTH,pageLength);
            defaultParameter.put("Pagination",pagination);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return defaultParameter.toString();
    }
}
