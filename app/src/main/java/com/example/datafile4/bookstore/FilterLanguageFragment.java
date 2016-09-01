package com.example.datafile4.bookstore;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
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
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class FilterLanguageFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    private String urlLangs = Constants.HOST + "/api/BookStore/GetLanguages";
    protected RecyclerView mRecyclerView;
    protected LanguageListAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    private List<Language> mLanguages;
    private HashMap<Integer,Boolean> checks;
    private static final String TAG = "FilterLanguageRecyclerViewFragment";
    private static List<Language> currentSelectedItems = new ArrayList<>();
    private OnFragmentInteractionListener mListener;

    public FilterLanguageFragment() {
        // Required empty public constructor
    }

    public static FilterLanguageFragment newInstance(String param1, String param2) {
        FilterLanguageFragment fragment = new FilterLanguageFragment();
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
        mLanguages = new ArrayList<>();
        checks = new HashMap<>();

        File fileFilterGenresList = new File(getActivity().getDir("data",0), Constants.PREF_FILTER_LANGS);
        File fileChecks = new File(getActivity().getDir("data",0),Constants.PREF_FILTER_LANGSCHECKS);
        try {
            ObjectInputStream inputStream1 = new ObjectInputStream(new FileInputStream(fileFilterGenresList));
            mLanguages = (ArrayList<Language>)inputStream1.readObject();
            Log.v("GenresFragment",String.valueOf(mLanguages.size()));
            if(fileChecks.exists()){
                ObjectInputStream inputStream2 = new ObjectInputStream(new FileInputStream(fileChecks));
                checks = (HashMap<Integer,Boolean>)inputStream2.readObject();
                inputStream2.close();
            } else {
                for (int i = 0; i<mLanguages.size(); i++){
                    checks.put(mLanguages.get(i).getId(),false);
                }
            }
            inputStream1.close();

        }catch (IOException e){
            Log.e("Serialize",e.getMessage());
        } catch (ClassNotFoundException e){
            Log.e("Serialize",e.getMessage());
        }


        for(int i = 0; i<mLanguages.size(); i++){
            mLanguages.get(i).setSelected(checks.get(mLanguages.get(i).getId()));
        }

        mAdapter = new LanguageListAdapter(getActivity(), mLanguages, new LanguageListAdapter.OnItemCheckListener() {
            @Override
            public void onItemCheck(Language language) {
                currentSelectedItems.add(language);
                checks.put(language.getId(),true);
            }

            @Override
            public void onItemUncheck(Language language) {
                currentSelectedItems.remove(language);
                checks.put(language.getId(),false);
            }
        });

//        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlLangs, null, new Response.Listener<JSONArray>() {
//            @Override
//            public void onResponse(JSONArray response) {
//                //HashMap<Integer,String> genres = new HashMap<>();
//                for(int i = 0 ;i<response.length();i++){
//                    try {
//                        JSONObject object = response.getJSONObject(i);
//                        mLanguages.add(new Language(object.getInt(Constants.KEY_ID), object.getString("Name")));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//                mAdapter.updateList(mLanguages);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_filter_language, container, false);
        rootView.setTag(TAG);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.filter_checklist_languages);
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
    public void onStop() {
        currentSelectedItems.clear();
        CommonMethods.writeDataFile(Constants.PREF_FILTER_LANGSCHECKS,getActivity(),checks);
        super.onStop();
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
        mListener = null;
    }

    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }

    public JSONArray getSelectedItems() {
        JSONArray Ids = new JSONArray();
        for(int i = 0;i<currentSelectedItems.size();i++){
            Language lang = currentSelectedItems.get(i);
            Ids.put(lang.getId());
        }
        return Ids;
    }
}
