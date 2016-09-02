package com.example.datafile4.bookstore;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.datafile4.bookstore.Config.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CartFragment.OnFragmentInteractionListener,AccountFragment.OnFragmentInteractionListener,BooksFragment.OnFragmentInteractionListener {

    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;
    private String PREF = "user_data";
    private String genresUrl = Constants.HOST + "api/BookStore/GetGenres";
    private String langsUrl = Constants.HOST + "api/BookStore/GetLanguages";
    private List<Genre> mGenres;
    private List<Language> mLanguages;
    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //delete past filters
        File fileChecks = new File(getDir("data",0),Constants.PREF_FILTER_GENRESCHECKS);
        boolean deleted = fileChecks.delete();

        fileChecks = new File(getDir("data",0),Constants.PREF_FILTER_LANGSCHECKS);
        deleted = fileChecks.delete();

        fileChecks = new File(getDir("data",0),Constants.PREF_FILTER_PRICERANGEVALUES);
        deleted = fileChecks.delete();
        settings = getSharedPreferences(Constants.PREF,Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = settings.edit();
        editor.putString(Constants.KEY_FILTER_VALUES,CommonMethods.createFilterJSONString(new JSONArray(),
                new JSONArray(),0,999,new JSONArray(),0,30));
        editor.commit();

        //if setting does not exists, getBoolean will return false
        if(!settings.contains(Constants.KEY_COOKIE)){
            Intent intent = new Intent(MainActivity.this,Login.class);
            startActivity(intent);
        }


        mGenres = new ArrayList<>();
        mLanguages = new ArrayList<>();

        //we will fetch information for filter
        JsonArrayRequest filterGenresRequest = new JsonArrayRequest(genresUrl, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i = 0 ;i<response.length();i++){
                    try {
                        JSONObject object = response.getJSONObject(i);
                        mGenres.add(new Genre(object.getInt(Constants.KEY_ID), object.getString("Name")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                CommonMethods.writeDataFile(Constants.PREF_FILTER_GENRES,getApplicationContext(),mGenres);
                mGenres.clear();
                Log.v("GenresMain",String.valueOf(mGenres.size()));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("FilterOptionsFetch",error.getMessage());
            }
        });
        MySingleton.getInstance(MainActivity.this).addToRequestQueue(filterGenresRequest);

        //we will fetch information for filter
        JsonArrayRequest filterLanguagesRequest = new JsonArrayRequest(langsUrl, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i = 0 ;i<response.length();i++){
                    try {
                        JSONObject object = response.getJSONObject(i);
                        mLanguages.add(new Language(object.getInt(Constants.KEY_ID), object.getString("Name")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                CommonMethods.writeDataFile(Constants.PREF_FILTER_LANGS,getApplicationContext(),mLanguages);
                mLanguages.clear();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("FilterOptionsFetch",error.getMessage());
            }
        });

        MySingleton.getInstance(MainActivity.this).addToRequestQueue(filterGenresRequest);
        MySingleton.getInstance(MainActivity.this).addToRequestQueue(filterLanguagesRequest);

        //Set a Toolbar to replace the ActionBar
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //find drawer view
        mDrawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();
        nvDrawer = (NavigationView)findViewById(R.id.nvView);
        setupDrawerContent(nvDrawer);
        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.addDrawerListener(drawerToggle);

        ///Log.v("FilterValuesActivity:",message+" ");
        //start the default fragment
        Class defaultFragmentClass = BooksFragment.class;
        try {
            Fragment defaultFragment = (Fragment)defaultFragmentClass.newInstance();
//            Bundle bundle = new Bundle();
//            bundle.putString(Constants.KEY_FILTER_VALUES, message);
//            defaultFragment.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().add(R.id.flContent, defaultFragment).commit();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        // The action bar home/up action should open or close the drawer.
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private void setupDrawerContent(NavigationView navigationView){
            navigationView.setNavigationItemSelectedListener(
                    new NavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(MenuItem item) {
                            selectDrawerItem(item);
                            return true;
                        }
                    }
            );
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;

        if(menuItem.getItemId() == R.id.nav_logout_fragment){
            logoutFromApp();
        } else {
            Class fragmentClass;
            switch(menuItem.getItemId()) {
                case R.id.nav_account_fragment:
                    fragmentClass = AccountFragment.class;
                    break;
                case R.id.nav_books_fragment:
                    fragmentClass = BooksFragment.class;
                    break;
                case R.id.nav_cart_fragment:
                    fragmentClass = CartFragment.class;
                    break;
                default:
                    fragmentClass = AccountFragment.class;
            }

            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

            // Highlight the selected item has been done by NavigationView
            menuItem.setChecked(true);
            // Set action bar title
            setTitle(menuItem.getTitle());
            // Close the navigation drawer
            mDrawer.closeDrawers();
        }
    }
    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open,  R.string.drawer_close);
    }


    //We must implement interface from fragments
    public void onFragmentInteraction(Uri uri){

    }

    @Override
    protected void onStart() {
        if(!settings.contains(Constants.KEY_FILTER_VALUES)){
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(Constants.KEY_FILTER_VALUES,CommonMethods.createFilterJSONString(new JSONArray(),
                    new JSONArray(),0,999,new JSONArray(),0,30));
            editor.commit();
        }
        super.onStart();
    }

    @Override
    protected void onStop() {
        SharedPreferences.Editor editor = settings.edit();
        editor.remove(Constants.KEY_FILTER_VALUES);
        //editor.commit();
        super.onStop();
    }



    //Method that performs logout from app
    public void logoutFromApp(){
        SharedPreferences sharedPrefs = getSharedPreferences(PREF,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.remove(Constants.KEY_COOKIE);
        editor.commit();

        Intent intent = new Intent(MainActivity.this,Login.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
}
