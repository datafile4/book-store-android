package com.example.datafile4.bookstore;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.HashMap;

import com.example.datafile4.bookstore.Config.Constants;

public class BookActivity extends AppCompatActivity {
    private String url = Constants.HOST + "api/BookStore/GetBookInfo?id=";
    private ProgressBar progressBar;
    private ScrollView scrollView;
    private Bitmap cover;
    private static int SCALE_SIZE = 200;

    public static ImageView bmImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        } catch (NullPointerException e) {
            Log.e("Error", e.getMessage());
        }
        Context context = getBaseContext();
        Intent intent = getIntent();
        int id = intent.getIntExtra(Constants.KEY_ID, 0);
        scrollView = (ScrollView) findViewById(R.id.bookScrollView);
        scrollView.setVisibility(View.INVISIBLE);
        progressBar = (ProgressBar) findViewById(R.id.bookActivityProgressBar);
        progressBar.setVisibility(ProgressBar.VISIBLE);
        HashMap<String, Integer> params = new HashMap<String, Integer>();
        params.put(Constants.KEY_ID, id);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url + String.valueOf(id), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    setElements(response);
                    new DownloadImageTask((ImageView) findViewById(R.id.outside_imageview)).execute(response.getString(Constants.KEY_IMG_URL));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

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
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setElements(JSONObject response) throws JSONException {
        String nameText = response.getString(Constants.KEY_BOOKNAME);
        TextView bookName = (TextView) findViewById(R.id.book_name2);
        TextView author = (TextView) findViewById(R.id.author_name);
        TextView genre = (TextView) findViewById(R.id.genre_text);
        TextView language = (TextView) findViewById(R.id.language_text);
        TextView bookPrice = (TextView) findViewById(R.id.book_price_number);

        language.setText(getString(R.string.language, response.getString(Constants.KEY_LANG)));
        bookPrice.setText(getString(R.string.book_price, response.getInt(Constants.KEY_PRICE)));
        genre.setText(getString(R.string.genre, response.getString(Constants.KEY_GENRE)));
        bookName.setText(nameText);
        author.setText(getString(R.string.by, response.getString(Constants.KEY_AUTHOR)));

    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        // ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            BookActivity.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];

            try {
                cover = fetchImage(urldisplay);
            } catch (Exception e) {
                cover = BitmapFactory.decodeResource(getResources(), R.drawable.defaultcover);
                e.printStackTrace();
            } catch (OutOfMemoryError e) {
                cover = BitmapFactory.decodeResource(getResources(), R.drawable.defaultcover);
                e.printStackTrace();
            }
            return cover;
        }

        public Bitmap fetchImage(String urldisplay) throws IOException {
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new java.net.URL(urldisplay).openStream(), null, o);

            // The new size we want to scale to
            final int REQUIRED_SIZE = SCALE_SIZE;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new java.net.URL(urldisplay).openStream(), null, o2);
        }
        protected void onPostExecute(Bitmap result) {


            bmImage.setImageBitmap(result);

            final RelativeLayout layout = (RelativeLayout) findViewById(R.id.inside_backgdound);
            Palette.from(result).generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(Palette palette) {
                    Palette.Swatch swatch = palette.getVibrantSwatch();
                    if (swatch != null) {
                        layout.setBackgroundColor(swatch.getRgb());
                    }
                }
            });
            result = null;
            // out = null;
            progressBar.setVisibility(ProgressBar.INVISIBLE);
            scrollView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void onStop() {
        cover.recycle();
        bmImage.setImageBitmap(null);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
