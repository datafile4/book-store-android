package com.example.datafile4.bookstore;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by datafile4 on 8/26/16.
 */
public class JsonArrayRequestExtended extends JsonArrayRequest {

    public JsonArrayRequestExtended(String url, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
    }
}
