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

import com.example.datafile4.bookstore.Config.Constants;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements CartFragment.OnFragmentInteractionListener,AccountFragment.OnFragmentInteractionListener,BooksFragment.OnFragmentInteractionListener {

    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;
    private String PREF = "user_data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        Intent intent = getIntent();
        String message = intent.getStringExtra(Constants.KEY_FILTER_VALUES);
        if(message == null){
            JSONObject defaultParameter = new JSONObject();
            try {
                defaultParameter.put(Constants.KEY_FILTER_LANGIDS,"");
                defaultParameter.put(Constants.KEY_FILTER_GENREIDS,"");
                defaultParameter.put(Constants.KEY_FILTER_LOWPRICE,0);
                defaultParameter.put(Constants.KEY_FILTER_HIGHPRICE,9999);
                defaultParameter.put(Constants.KEY_FILTER_SEARCHTERMS,"");
                JSONObject pagination = new JSONObject();
                pagination.put(Constants.KEY_FILTER_PAGENUMBER,0);
                pagination.put(Constants.KEY_FILTER_PAGELENGTH,10);
                defaultParameter.put("Pagination",pagination);
                message = defaultParameter.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

       // Log.v("FilterValues:",message+" ");
        //start the default fragment
        Class defaultFragmentClass = BooksFragment.class;
        try {
            Fragment defaultFragment = (Fragment)defaultFragmentClass.newInstance();
            Bundle bundle = new Bundle();
            bundle.putString(Constants.KEY_FILTER_VALUES, message);
            defaultFragment.setArguments(bundle);
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
    //Method that performs logout from app
    public void logoutFromApp(){
        SharedPreferences sharedPrefs = getSharedPreferences(PREF,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.clear();
        editor.commit();

        Intent intent = new Intent(MainActivity.this,Login.class);
        startActivity(intent);
    }

}
