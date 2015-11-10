package com.example.darkdark.app;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ThirdFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ThirdFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ThirdFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String DEFAULT="N/A";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private EditText edit_Weight;
    private EditText edit_Height;

    private TextView show_Weight;
    private TextView show_Height;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ThirdFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ThirdFragment newInstance(String param1, String param2) {
        ThirdFragment fragment = new ThirdFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ThirdFragment() {
        // Required empty public constructor
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
        View view = inflater.inflate(R.layout.fragment_third, container, false);
        edit_Weight = (EditText) view.findViewById(R.id.editWeight);
        edit_Height = (EditText) view.findViewById(R.id.editHeight);
        show_Weight = (TextView) view.findViewById(R.id.showWeight);
        show_Height = (TextView)  view.findViewById(R.id.showHeight);
        return view;
    }

    public void applyChange(View view) {
            switch(view.getId()) {

                case R.id.buttonWH:
                if (isAdded()) {
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyData", Context.MODE_PRIVATE);
                    //must use SharedPreferences.Editor to save values
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("height", edit_Height.getText().toString()); //stores height data with key "height"
                    editor.putString("weight", edit_Weight.getText().toString()); //stores weight data with key "weight"

                    editor.commit(); //commits the data

                    Toast.makeText(getContext(), "data was saved", Toast.LENGTH_LONG).show();

                    String height = sharedPreferences.getString("height", DEFAULT); //initialized default height data with key "height"
                    String weight = sharedPreferences.getString("weight", DEFAULT); //initialized default weight data with key "weight"

                    if (height.equals(DEFAULT) || weight.equals(DEFAULT)) {

                        Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();

                    }

                    //for testing
                    else {
                        Toast.makeText(getContext(), "Data Loaded", Toast.LENGTH_SHORT).show();
                        show_Height.setText(height);
                        show_Weight.setText(weight);

                    }
                }break;
            }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
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
        public void onFragmentInteraction(Uri uri);
    }

}
