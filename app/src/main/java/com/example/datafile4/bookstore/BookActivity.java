package com.example.datafile4.bookstore;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;
import com.example.datafile4.bookstore.Config.Constants;

public class BookActivity extends AppCompatActivity {
    private String url = "http://amiraslan.azurewebsites.net/api/BookStore/GetBookInfo?id=";

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
        int id = intent.getIntExtra(Constants.KEY_ID,0);
        HashMap<String,Integer> params = new HashMap<String, Integer>();
        params.put(Constants.KEY_ID,id);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url+String.valueOf(id),null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    setElements(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new DownloadImageTask((ImageView)findViewById(R.id.outside_imageview)).execute("http://i.imgur.com/v8hVVdF.jpg");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Log.e("Volley",error.getMessage());
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

    public void setElements(JSONObject response) throws JSONException {
        String nameText = response.getString(Constants.KEY_BOOKNAME);
        TextView bookName = (TextView)findViewById(R.id.book_name2);
        TextView author = (TextView)findViewById(R.id.author_name);
        TextView genre = (TextView)findViewById(R.id.genre_text);
        TextView language = (TextView)findViewById(R.id.language_text);

        language.setText(getString(R.string.language,response.getString(Constants.KEY_LANG)));
        genre.setText(getString(R.string.genre,response.getString(Constants.KEY_GENRE)));
        bookName.setText(nameText);
        author.setText(getString(R.string.by, response.getString(Constants.KEY_AUTHOR)));
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
            final RelativeLayout layout = (RelativeLayout)findViewById(R.id.inside_backgdound);
            Palette.from(result).generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(Palette palette) {
                    Palette.Swatch swatch = palette.getVibrantSwatch();
                    if(swatch != null){
                        layout.setBackgroundColor(swatch.getRgb());
                    }
                }
            });
        }
    }
}
