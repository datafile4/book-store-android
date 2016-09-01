package com.example.datafile4.bookstore;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.datafile4.bookstore.Config.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilterReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class FilterGenreFragment extends Fragment {
    private String urlGenres = Constants.HOST + "/api/BookStore/GetGenres";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    protected RecyclerView mRecyclerView;
    protected GenreListAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    private List<Genre> mGenres;
    HashMap<Integer,Boolean> checks;
    private final String TAG = "FilterGenreRecyclerViewFragment";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private static List<Genre> currentSelectedItems = new ArrayList<>();

    public FilterGenreFragment() {
        // Required empty public constructor
    }

    public static FilterGenreFragment newInstance(String param1, String param2) {
        FilterGenreFragment fragment = new FilterGenreFragment();
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
        mGenres = new ArrayList<>();
        checks = new HashMap<>();

        File fileFilterGenresList = new File(getActivity().getDir("data",0), Constants.PREF_FILTER_GENRES);
        File fileChecks = new File(getActivity().getDir("data",0),Constants.PREF_FILTER_GENRESCHECKS);

        try {
            ObjectInputStream inputStream1 = new ObjectInputStream(new FileInputStream(fileFilterGenresList));
            mGenres = (ArrayList<Genre>)inputStream1.readObject();
            Log.v("GenresFragment",String.valueOf(mGenres.size()));
            if(fileChecks.exists()){
                ObjectInputStream inputStream2 = new ObjectInputStream(new FileInputStream(fileChecks));
                checks = (HashMap<Integer,Boolean>)inputStream2.readObject();
                inputStream2.close();
            } else {
                for (int i = 0; i<mGenres.size(); i++){
                    checks.put(mGenres.get(i).getGenreID(),false);
                }
            }
            inputStream1.close();

        }catch (IOException e){
            Log.e("Serialize",e.getMessage());
        } catch (ClassNotFoundException e){
            Log.e("Serialize",e.getMessage());
        }

        for(int i = 0; i<mGenres.size(); i++){
            mGenres.get(i).setSelected(checks.get(mGenres.get(i).getGenreID()));
        }

        mAdapter = new GenreListAdapter(getActivity(), mGenres, new GenreListAdapter.OnItemCheckListener() {
            @Override
            public void onItemCheck(Genre genre) {
                currentSelectedItems.add(genre);
                checks.put(genre.getGenreID(),true);
            }

            @Override
            public void onItemUncheck(Genre genre) {
                currentSelectedItems.remove(genre);
                checks.put(genre.getGenreID(),false);
            }
        });

//        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlGenres, null, new Response.Listener<JSONArray>() {
//            @Override
//            public void onResponse(JSONArray response) {
//                //HashMap<Integer,String> genres = new HashMap<>();
//                for(int i = 0 ;i<response.length();i++){
//                    try {
//                        JSONObject object = response.getJSONObject(i);
//                        mGenres.add(new Genre(object.getInt(Constants.KEY_ID), object.getString("Name")));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//                mAdapter.updateList(mGenres);
//            }
//        }, new Response.ErrorListener(){
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e("Volley error",error.getMessage());
//            }
//        });
//        MySingleton.getInstance(getActivity()).addToRequestQueue(jsonArrayRequest);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_filter_genre, container, false);
        rootView.setTag(TAG);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.filter_checklist_genres);
        //mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
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
    public void onStop() {
//        HashMap<Integer,Boolean> checks = new HashMap<>();
//        for(int i = 0; i<mGenres.size(); i++){
//            checks.put(mGenres.get(i).getGenreID(),mGenres.get(i).isSelected());
//        }
        CommonMethods.writeDataFile(Constants.PREF_FILTER_GENRESCHECKS,getActivity(),checks);
        currentSelectedItems.clear();
        mGenres.clear();
        super.onStop();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        currentSelectedItems.clear();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    public JSONArray getSelectedItems()  {
        //List <Integer> Ids = new ArrayList<>();
        JSONArray Ids = new JSONArray();
        for(int i = 0;i<currentSelectedItems.size();i++){
            Genre genre = currentSelectedItems.get(i);
            Ids.put(genre.getGenreID());
        }
        return Ids;
    }
}
