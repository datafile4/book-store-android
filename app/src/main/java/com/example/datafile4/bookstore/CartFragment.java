package com.example.datafile4.bookstore;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.datafile4.bookstore.Config.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class CartFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String url = Constants.HOST + "api/BookStore/GetCartItems";
    int bookId;
    private ArrayList<Book> books;
    private static final String TAG = "RecyclerViewFragmentCart";
    private static final String KEY_LAYOUT_MANAGER = "layoutManagerCart";
    private static final int SPAN_COUNT = 1;
    protected ProgressBar progressBar;
    protected RecyclerView mRecyclerView;
    protected CartAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    private SharedPreferences settings;

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CartFragment() {
        // Required empty public constructor
    }

    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        books = new ArrayList<Book>();
        mAdapter = new CartAdapter(getActivity(), books);
        settings =  this.getActivity().getSharedPreferences(Constants.PREF, Context.MODE_PRIVATE);

//        mAdapter.setOnItemClickListener(new CartAdapter.ClickListener() {
//            @Override
//            public void onItemClick(int position, View v) {
//                Log.d("Click", "position" + position);
//                bookId = books.get(position).getBookID();
//                Intent intent = new Intent(getActivity(), BookActivity.class);
//                intent.putExtra(Constants.KEY_ID, bookId);
//                startActivity(intent);
//            }
//        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_cart, container, false);
        rootView.setTag(TAG);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rvCart);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);


        return rootView;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onStart() {
        books.clear();
        final String uid = settings.getString(Constants.KEY_COOKIE,"");
        try {
            makeRequest(uid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        super.onStart();
    }

    @Override
    public void onStop() {
        mAdapter.removeGrid();
        super.onStop();
    }

    @Override
    public void onDetach() {
        books.clear();
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public void makeRequest(final String uid) throws JSONException {
        Log.v("Cookie","user-g="+uid);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject;
                int id;
                String bookName;
                String Url;
                try {
                    for (int i = 0; i < response.length(); i++) {
                        jsonObject = response.getJSONObject(i);
                        id = jsonObject.getInt(Constants.KEY_ID);
                        bookName = jsonObject.getString(Constants.KEY_BOOKNAME);
                        Url = jsonObject.getString(Constants.KEY_IMG_URL);
                        books.add(new Book(id, bookName, Url));
                    }
                    Log.v("myResponse","ping!!");
                } catch (JSONException e) {
                    e.getMessage();
                }
                mAdapter.updateGrid(books);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> mHeaders = new HashMap<String, String>();
                mHeaders.put("Accept", "application/json");
                Log.v("CookieMap","user-g="+uid);
                mHeaders.put("Cookie","user-g="+uid);
                return mHeaders;
            }
        };
        MySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }
}
