package com.example.datafile4.bookstore;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import com.example.datafile4.bookstore.Config.Constants;

public class BookActivity extends AppCompatActivity {
    private String url = "http://amiraslan.azurewebsites.net/api/BookStore/GetBookInfo?bookid=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        try{
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        } catch (NullPointerException e){
            Log.e("Error",e.getMessage());
        }
        Context context = getBaseContext();
        Intent intent = getIntent();
        int id = intent.getIntExtra(BooksFragment.EXTRA_MESSAGE,0);
        HashMap<String,Integer> params = new HashMap<String, Integer>();
        params.put(Constants.KEY_ID,id);
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST, url+String.valueOf(id),null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONObject jsonObject = response.getJSONObject(0);
                    String name = jsonObject.getString(Constants.KEY_BOOKNAME);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley",error.getMessage());
            }
        });
        MySingleton.getInstance(BookActivity.this).addToRequestQueue(jsonObjectRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
            default:return super.onOptionsItemSelected(item);
        }
    }

    public void setElements(JSONObject response){

    }
}
