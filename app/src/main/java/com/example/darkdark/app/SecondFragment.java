package com.example.darkdark.app;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import com.akexorcist.roundcornerprogressbar.TextRoundCornerProgressBar;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SecondFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SecondFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SecondFragment extends Fragment  {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public static final String DEFAULT="N/A";

    private TextRoundCornerProgressBar progressOne;
    private TextRoundCornerProgressBar progressTwo;
    private TextRoundCornerProgressBar progressThree;
    private TextRoundCornerProgressBar progressFour;
    private TextRoundCornerProgressBar progressFive;

    private TextView textCount;
    private TextView textTotal;

    private TextView dateOne;
    private TextView dateTwo;
    private TextView dateThree;
    private TextView dateFour;
    private TextView dateFive;

    private TextView caloriesOne;
    private TextView caloriesTwo;
    private TextView caloriesThree;
    private TextView caloriesFour;
    private TextView caloriesFive;

    private TextView distanceOne;
    private TextView distanceTwo;
    private TextView distanceThree;
    private TextView distanceFour;
    private TextView distanceFive;



    private TextView tLifeTotal;

    private Spinner previousSpinner;

    private double totallifetime=0;


    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SecondFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SecondFragment newInstance(String param1, String param2) {
        SecondFragment fragment = new SecondFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public SecondFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        SharedPreferences mPrefs = getActivity().getSharedPreferences("MyData", Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_second,
                container, false);

        previousSpinner = (Spinner) view.findViewById(R.id.spinner2);
// Create an ArrayAdapter using the string array
// and a default spinner layout
        final ArrayAdapter<CharSequence> adapter;
        adapter = ArrayAdapter.createFromResource(view.getContext(), R.array.Previous, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        previousSpinner.setAdapter(adapter);


        progressOne = (TextRoundCornerProgressBar) view.findViewById(R.id.progressOne);
        progressTwo = (TextRoundCornerProgressBar) view.findViewById(R.id.progressTwo);
        progressThree = (TextRoundCornerProgressBar) view.findViewById(R.id.progressThree);
        progressFour = (TextRoundCornerProgressBar) view.findViewById(R.id.progressFour);
        progressFive = (TextRoundCornerProgressBar) view.findViewById(R.id.progressFive);

        dateOne = (TextView) view.findViewById(R.id.date1);
        dateTwo = (TextView) view.findViewById(R.id.date2);
        dateThree = (TextView) view.findViewById(R.id.date3);
        dateFour = (TextView) view.findViewById(R.id.date4);
        dateFive = (TextView) view.findViewById(R.id.date5);

        caloriesOne = (TextView) view.findViewById(R.id.calories1);
        caloriesTwo = (TextView) view.findViewById(R.id.calories2);
        caloriesThree = (TextView) view.findViewById(R.id.calories3);
        caloriesFour = (TextView) view.findViewById(R.id.calories4);
        caloriesFive = (TextView) view.findViewById(R.id.calories5);

        distanceOne = (TextView) view.findViewById(R.id.distance1);
        distanceTwo = (TextView) view.findViewById(R.id.distance2);
        distanceThree = (TextView) view.findViewById(R.id.distance3);
        distanceFour = (TextView) view.findViewById(R.id.distance4);
        distanceFive = (TextView) view.findViewById(R.id.distance5);


        previousSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View vi,
            int pos, long id) {
            // An item was selected. You can retrieve the selected item using
            // parent.getItemAtPosition(pos)
            if (parent.getItemAtPosition(pos).toString().equals("5 days")) {
                calculation(view, false);
                }
            }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
                }
            }
        );
        textCount = (TextView) view.findViewById(R.id.count);
        textTotal = (TextView) view.findViewById(R.id.total);
        tLifeTotal = (TextView) view.findViewById(R.id.textLifeTotal);

        SharedPreferences mPrefs = getActivity().getSharedPreferences("MyData", Context.MODE_PRIVATE);
        //SharedPreferences.Editor editor = mPrefs.edit();
        Calendar calendar = Calendar.getInstance();
        int dayyear = calendar.get(Calendar.DAY_OF_YEAR);
        int year = calendar.get(Calendar.YEAR);
        double totallifetime1 =0;
        for(int i=dayyear; i>0; i--){


        totallifetime1 = Double.parseDouble(String.valueOf(mPrefs.getString("Lifetime"+year+i, "0")))+ totallifetime1;


        }
        for(int i=365; i>0; i--){


          totallifetime = Double.parseDouble(String.valueOf(mPrefs.getString("Lifetime"+(year-1)+i, "0")))+ totallifetime;

        }

            totallifetime=totallifetime1+totallifetime;
            tLifeTotal.setText(""+totallifetime+" Minutes");

        return view;


    }




    public void calculation(View p,boolean bMonth) {

        //have to change all the month+day to day_of_year
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyData", Context.MODE_PRIVATE);
        Calendar calendar = Calendar.getInstance();

        int dayyear = calendar.get(Calendar.DAY_OF_YEAR);


        int month = calendar.get(Calendar.MONTH);
        int date = calendar.get(Calendar.DATE);
        int last_month = month -1;
        Double dstep_length = null;
        Double dweight = null;
        int multiplier;



       int fdayyear;
        int fyear = calendar.get(Calendar.YEAR);

        for(int i=0;i<6; i++){

            if(bMonth){
                multiplier = 30 ;
            }
            else{
                multiplier = 1;
            }
            calendar.add(Calendar.DAY_OF_YEAR, -i*multiplier);
            fdayyear= calendar.get(Calendar.DAY_OF_YEAR);



            if(fdayyear==0)
            {
               calendar.add(Calendar.YEAR,-1);
                fyear = calendar.get(Calendar.YEAR);

            }


            //getting stuff from Shared Preference ---->>>>>
           String step_length = sharedPreferences.getString("step_stride", DEFAULT);
            if(!step_length.equals(DEFAULT)){
               dstep_length = Double.parseDouble(step_length);

            }

            String weight = sharedPreferences.getString("weight",DEFAULT);
            if(!weight.equals(DEFAULT)){

                dweight = Double.parseDouble(weight);
            }



            // <<<<<----

            int yester_steps = sharedPreferences.getInt("Count" + fyear + fdayyear, -1);
            switch (i){
                case 0:
                    if(yester_steps==-1){
                        //add in step_length info.
                        //if default make them invisible
                        p.findViewById(R.id.date1).setVisibility(View.GONE);
                        p.findViewById(R.id.progressOne).setVisibility(View.GONE);
                        p.findViewById(R.id.calories1).setVisibility(View.GONE);
                        p.findViewById(R.id.distance1).setVisibility(View.GONE);

                        }
                    else{

                        progressOne.setProgressText(Integer.toString(yester_steps));

                        progressOne.setProgress(yester_steps);
                       if( yester_steps%500 == progressOne.getMax())
                        dateOne.setText((calendar.get(Calendar.MONTH)+1)+"/" +calendar.get(Calendar.DATE));
                        if(!step_length.equals(DEFAULT)) {
                            distanceOne.setText("Distance: " + String.format("%.4f", (yester_steps * dstep_length)) + " mi");

                            //calculate calories:

                            double calcalc = 0.625*dweight*yester_steps*dstep_length;
                            caloriesOne.setText("Calories: "+ String.format("%.4f",calcalc)+" kCal");

                        }
                        else{
                            distanceOne.setText("Distance: "+step_length);
                            caloriesOne.setText("Calories: "+step_length);

                        }






                    }
                case 1:
                    if(yester_steps==-1){
                        //if default make them invisible

                        p.findViewById(R.id.date2).setVisibility(View.GONE);
                        p.findViewById(R.id.progressTwo).setVisibility(View.GONE);
                        p.findViewById(R.id.calories2).setVisibility(View.GONE);
                        p.findViewById(R.id.distance2).setVisibility(View.GONE);
                        p.findViewById(R.id.lineTwo);

                    }
                    else{
                        progressTwo.setProgressText(Integer.toString(yester_steps));
                        progressTwo.setProgress(yester_steps);

                        dateTwo.setText((calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.DATE));
                        if(!step_length.equals(DEFAULT)) {
                            distanceTwo.setText("Distance: "+ String.format("%.4f",(yester_steps * dstep_length))+" mi");

                            //calculate calories:

                            double calcalc = 0.625*dweight*yester_steps*dstep_length;
                            caloriesTwo.setText("Calories: "+ String.format("%.4f",calcalc)+" kCal");

                        }
                        else{
                            distanceTwo.setText("Distance: "+step_length);
                            caloriesOne.setText("Calories: "+step_length);
                        }

                    }
                case 2:
                    if(yester_steps == -1){
                        //if default make them invisible
                        p.findViewById(R.id.date3).setVisibility(View.GONE);
                        p.findViewById(R.id.progressThree).setVisibility(View.GONE);
                        p.findViewById(R.id.calories3).setVisibility(View.GONE);
                        p.findViewById(R.id.distance3).setVisibility(View.GONE);
                        p.findViewById(R.id.lineThree);

                    }
                    else{
                        progressThree.setProgressText(Integer.toString(yester_steps));
                        progressThree.setProgress(yester_steps);
                        dateThree.setText((calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.DATE));
                        if(!step_length.equals(DEFAULT)) {
                            distanceThree.setText("Distance: "+ String.format("%.4f",(yester_steps * dstep_length))+" mi");

                            //calculate calories:

                            double calcalc = 0.625*dweight*yester_steps*dstep_length;
                            caloriesThree.setText("Calories: "+ String.format("%.4f",calcalc)+" kCal");

                        }
                        else{
                            distanceThree.setText("Distance: "+step_length);
                            caloriesOne.setText("Calories: "+step_length);
                        }

                    }
                case 3:
                    if(yester_steps == -1){
                        //if default make them invisible

                        p.findViewById(R.id.date4).setVisibility(View.GONE);
                        p.findViewById(R.id.progressFour).setVisibility(View.GONE);
                        p.findViewById(R.id.calories4).setVisibility(View.GONE);
                        p.findViewById(R.id.distance4).setVisibility(View.GONE);
                        p.findViewById(R.id.lineFourth);

                    }
                    else{
                        progressFour.setProgressText(Integer.toString(yester_steps));
                        progressFour.setProgress(yester_steps);
                        dateFour.setText((calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.DATE));
                        if(!step_length.equals(DEFAULT)) {
                            distanceFour.setText("Distance: "+ String.format("%.4f",(yester_steps * dstep_length))+" mi");

                            //calculate calories:

                            double calcalc = 0.625*dweight*yester_steps*dstep_length;
                            caloriesFour.setText("Calories: "+ String.format("%.4f",calcalc)+" kCal");

                        }
                        else{
                            distanceFour.setText("Distance: " + step_length);
                            caloriesOne.setText("Calories: "+step_length);
                        }

                    }
                case 4:
                    if(yester_steps == -1 ){
                        //if default make them invisible

                        p.findViewById(R.id.date5).setVisibility(View.GONE);
                        p.findViewById(R.id.progressFive).setVisibility(View.GONE);
                        p.findViewById(R.id.calories5).setVisibility(View.GONE);
                        p.findViewById(R.id.distance5).setVisibility(View.GONE);

                    }
                    else{

                        progressFive.setProgressText(Integer.toString(yester_steps));
                        progressFive.setProgress(yester_steps);
                        dateFive.setText((calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.DATE));
                        if(!step_length.equals(DEFAULT)) {
                            distanceFive.setText("Distance: "+ String.format("%.4f",(yester_steps * dstep_length))+" mi");

                            //calculate calories:

                            double calcalc = 0.625*dweight*yester_steps*dstep_length;
                            caloriesFive.setText("Calories: "+ String.format("%.4f",calcalc)+" kCal");

                        }
                        else{
                            distanceFive.setText("Distance: " + step_length);
                            caloriesOne.setText("Calories: "+step_length);
                        }

                    }




            }


            if(fdayyear==0)
            {
                calendar.add(Calendar.YEAR,1);
                fyear = calendar.get(Calendar.YEAR);

            }
            calendar.add(Calendar.DAY_OF_YEAR, i*multiplier);

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
