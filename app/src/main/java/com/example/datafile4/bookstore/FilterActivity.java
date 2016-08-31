package com.example.datafile4.bookstore;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.datafile4.bookstore.Config.Constants;
import com.github.clans.fab.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FilterActivity extends AppCompatActivity implements FilterGenreFragment.OnFragmentInteractionListener,FilterPriceRangeFragment.OnFragmentInteractionListener,FilterLanguageFragment.OnFragmentInteractionListener {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FilterGenreFragment genreFragment;
    private FilterPriceRangeFragment priceRangeFragment;
    private FilterLanguageFragment languageFragment;

    private String urlLanguages = Constants.HOST + "/api/BookStore/GetLanguages";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        //we must create toolbar instead ActionBar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);
        getSupportActionBar().setTitle("Filter by");

        //view pager for swipe
        viewPager = (ViewPager) findViewById(R.id.filter_viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.filter_tabs);
        tabLayout.setupWithViewPager(viewPager);

        //Confirm floating button onclick
        FloatingActionButton confirmButton = (FloatingActionButton)findViewById(R.id.filter_confirm_floatingbutton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject toSend = new JSONObject();
                    toSend.put(Constants.KEY_FILTER_LANGIDS,languageFragment.getSelectedItems());
                    toSend.put(Constants.KEY_FILTER_GENREIDS,genreFragment.getSelectedItems());
                    toSend.put(Constants.KEY_FILTER_LOWPRICE,priceRangeFragment.getLowPrice());
                    toSend.put(Constants.KEY_FILTER_HIGHPRICE,priceRangeFragment.getHighPrice());
                    //Search currently doesn't work. Sorry :(
                    toSend.put(Constants.KEY_FILTER_SEARCHTERMS,"");
                    JSONObject pagination = new JSONObject();
                    pagination.put(Constants.KEY_FILTER_PAGENUMBER,0);
                    pagination.put(Constants.KEY_FILTER_PAGELENGTH,10);
                    toSend.put("Pagination",pagination);
                    String parsedJson = toSend.toString();
                    //Sending to MainActivity
                    Intent intent = new Intent(FilterActivity.this, MainActivity.class);
                    intent.putExtra(Constants.KEY_FILTER_VALUES, parsedJson);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
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

    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        languageFragment = new FilterLanguageFragment();
        genreFragment = new FilterGenreFragment();
        priceRangeFragment = new FilterPriceRangeFragment();

        adapter.addFragment(genreFragment, getString(R.string.genre2));
        adapter.addFragment(languageFragment, getString(R.string.language2));
        adapter.addFragment(priceRangeFragment, getString(R.string.price));
        viewPager.setAdapter(adapter);
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
    }
}
