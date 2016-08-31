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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FilterPriceRangeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FilterPriceRangeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FilterPriceRangeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText highPriceText;
    private EditText lowPriceText;

    private OnFragmentInteractionListener mListener;

    public FilterPriceRangeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FilterPriceRangeFragment.
     */
    // TODO: Rename and change types and number of parameters
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


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_filter_price_range, container, false);
        highPriceText = (EditText)rootView.findViewById(R.id.filter_highprice_picker);
        lowPriceText = (EditText)rootView.findViewById(R.id.filter_lowprice_picker);

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
