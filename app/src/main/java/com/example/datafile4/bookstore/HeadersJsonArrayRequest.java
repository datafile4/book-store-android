package com.example.datafile4.bookstore;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by datafile4 on 9/2/16.
 */
public class HeadersJsonArrayRequest extends JsonObjectRequest
{
    public HeadersJsonArrayRequest(int method, String url, JSONObject jsonRequest, Response.Listener listener, Response.ErrorListener errorListener)
    {
        super(method, url, jsonRequest, listener, errorListener);
    }

    @Override
    public Map getHeaders() throws AuthFailureError {
        Map headers = new HashMap();
        headers.put("appId", "MYAPP_ID_HERE");
        return headers;
    }

}
