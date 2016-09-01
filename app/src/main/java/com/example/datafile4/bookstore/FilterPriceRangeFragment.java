package com.example.datafile4.bookstore;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.datafile4.bookstore.Config.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;

public class FilterPriceRangeFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final int count = 2;
    private HashMap<Integer,String> values;
    private String mParam1;
    private String mParam2;

    private EditText highPriceText;
    private EditText lowPriceText;


    private OnFragmentInteractionListener mListener;

    public FilterPriceRangeFragment() {
        // Required empty public constructor
    }

    public static FilterPriceRangeFragment newInstance(String param1, String param2) {
        FilterPriceRangeFragment fragment = new FilterPriceRangeFragment();
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
        values = new HashMap<>();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_filter_price_range, container, false);
        highPriceText = (EditText)rootView.findViewById(R.id.filter_highprice_picker);
        lowPriceText = (EditText)rootView.findViewById(R.id.filter_lowprice_picker);

        File file = new File(getActivity().getDir("data",0),Constants.PREF_FILTER_PRICERANGEVALUES);
        if(file.exists()){
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
                values = (HashMap<Integer,String>)objectInputStream.readObject();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            lowPriceText.setText(values.get(0));
            highPriceText.setText(values.get(1));
        }

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
    public void onStop() {
        values.put(0,lowPriceText.getText().toString());
        values.put(1,highPriceText.getText().toString());
        CommonMethods.writeDataFile(Constants.PREF_FILTER_PRICERANGEVALUES,getActivity(),values);
        super.onStop();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public int getHighPrice(){
        int value = 9999;
        if(highPriceText != null){
            String price = highPriceText.getText().toString();
            if(!price.isEmpty()){
                value = Integer.valueOf(price);
            }
        }
        return value;
    }
    public int getLowPrice(){
        int value = 0;
        if(lowPriceText !=null){
            String price = lowPriceText.getText().toString();
            if(!price.isEmpty()){
                value = Integer.valueOf(price);
            }
        }
        return value;
    }

}
