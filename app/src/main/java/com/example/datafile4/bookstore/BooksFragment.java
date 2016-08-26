package com.example.datafile4.bookstore;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.datafile4.bookstore.Config.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BooksFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BooksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BooksFragment extends Fragment {
    // public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    private String url = "https://amiraslan.azurewebsites.net/api/BookStore/GetBooks";
    private static String imgUrl = "https://amiraslan.azurewebsites.net/";
    int bookId;
    private ArrayList<Book> books;
    private static final String TAG = "RecyclerViewFragment";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 2;

    protected RecyclerView mRecyclerView;
    protected BookAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    private JSONArray responseArray;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public BooksFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BooksFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BooksFragment newInstance(String param1, String param2) {
        BooksFragment fragment = new BooksFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        books = new ArrayList<Book>();
        mAdapter = new BookAdapter(getActivity(), books);
        //Volley request
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject;
                int id;
                String bookName;
                String Url;
                try {
                    responseArray = new JSONArray(response);
                } catch (JSONException e) {

                }

                // HashMap<String,String> book = new HashMap<String, String>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = responseArray.getJSONObject(i);
                        id = jsonObject.getInt(Constants.KEY_ID);
                        bookName = jsonObject.getString(Constants.KEY_BOOKNAME);
                        //    Url = imgUrl + jsonObject.getString(KEY_IMG_URL);
                        Url = "http://starecat.com/content/wp-content/uploads/copying-and-pasting-from-stack-overflow-essential-book-oreilly.jpg";//
//                        //I receive urls in images/img.jpg format
                        books.add(new Book(id, bookName, Url));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                mAdapter.updateGrid(books);
                Log.v("Response", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Log.e("Error:",error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put(Constants.KEY_PAGENUMBER, String.valueOf(0));
                params.put(Constants.KEY_PAGELENGTH, String.valueOf(10));
                return params;
            }
        };
//        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
//        requestQueue.add(jsonObjectRequest);

        MySingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest);
        mAdapter.setOnItemClickListener(new BookAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Log.d("Click", "position" + position);
                bookId = books.get(position).getBookID();
                Intent intent = new Intent(getActivity(), BookActivity.class);
                intent.putExtra(Constants.KEY_ID, bookId);
                startActivity(intent);
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_books, container, false);

        View rootView = inflater.inflate(R.layout.fragment_books, container, false);
        rootView.setTag(TAG);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rvBooks);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT);


        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
//        mAdapter.updateGrid(books);

        return rootView;
    }


    // TODO: Rename method, update argument and hook method into UI event
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
    public void onDetach() {
        super.onDetach();
        books.clear();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
