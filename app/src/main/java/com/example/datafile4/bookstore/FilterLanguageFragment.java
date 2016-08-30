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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FilterLanguageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FilterLanguageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FilterLanguageFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String urlLangs = Constants.HOST + "/api/BookStore/GetLanguages";
    protected RecyclerView mRecyclerView;
    protected LanguageListAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    private List<Language> mLanguages;
    private static final String TAG = "FilterLanguageRecyclerViewFragment";
    private static List<Language> currentSelectedItems = new ArrayList<>();
    private OnFragmentInteractionListener mListener;

    public FilterLanguageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FilterLanguageFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        mAdapter = new LanguageListAdapter(getActivity(), mLanguages, new LanguageListAdapter.OnItemCheckListener() {
            @Override
            public void onItemCheck(Language language) {
                currentSelectedItems.add(language);
            }

            @Override
            public void onItemUncheck(Language language) {
                currentSelectedItems.remove(language);
            }
        });

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlLangs, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                //HashMap<Integer,String> genres = new HashMap<>();
                for(int i = 0 ;i<response.length();i++){
                    try {
                        JSONObject object = response.getJSONObject(i);
                        mLanguages.add(new Language(object.getInt(Constants.KEY_ID), object.getString("Name")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                mAdapter.updateList(mLanguages);
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley error",error.getMessage());
            }
        });
        MySingleton.getInstance(getActivity()).addToRequestQueue(jsonArrayRequest);
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
    public List<Language> getSelectedItems(){
        return currentSelectedItems;
    }

}
