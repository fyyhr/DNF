package com.example.darkdark.app;

import android.app.Activity;

import android.app.NotificationManager;
//Notification imports -->
import android.app.AlarmManager;
import android.app.Notification;
import android.media.RingtoneManager;
import android.os.SystemClock;
import android.content.Intent;

// <--
import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
//import android.app.Fragment;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SecFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SecFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SecFragment extends Fragment implements SensorEventListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    boolean activityRunning;
    private TextView textCount;
    private TextView textTotal;
    private SensorManager mSensorManager;
    private Sensor mStepCounterSensor;
    private int cSteps = -1;
    private int dayyear=-1;
    private int year = -1;
    private int hourofday = -1;
    private int month=-1;
    private int date = -1;
    private int lastSteps;


    //Internal state
    private boolean moving = false;
    // NOTIFICATION RELATED
    // The PendingIntent to launch our activity if the user selects this notification
    protected PendingIntent mPendingIntent;
    protected int iNotificationId = 123;

    protected float fSince = 0;
    protected int iIntervals = 0;
    protected Notification mNotification;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    public SecFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SecFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SecFragment newInstance(String param1, String param2) {
        SecFragment fragment = new SecFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Toast.makeText(getActivity(),"ONCREATE CALLLED!", Toast.LENGTH_LONG).show();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //Toast.makeText(getActivity(),"ONCREATVIEW CALLLED!", Toast.LENGTH_LONG).show();
        View view = inflater.inflate(R.layout.fragment_sec,
                container, false);

        //lastSteps = 0;
        textCount = (TextView) view.findViewById(R.id.count);
        textTotal = (TextView) view.findViewById(R.id.total);
        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mStepCounterSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        //mStepDetectorSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);



//        SharedPreferences mPrefs = getActivity().getSharedPreferences("MyData", Context.MODE_PRIVATE);
        return view;
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
        //Toast.makeText(getActivity(),"ONATTACHED CALLLED!", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onStart(){
        super.onStart();
        //Toast.makeText(getActivity(),"ONSTART CALLLED!", Toast.LENGTH_LONG).show();
        try {
            mListener = (OnFragmentInteractionListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        //Toast.makeText(getActivity(),"OnDETACH CALLLED!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Calendar calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);
        dayyear = calendar.get(Calendar.DAY_OF_YEAR);
        //month = calendar.get(Calendar.MONTH);
        //date = calendar.get(Calendar.DATE);

        if(isAdded()) {

           // Toast.makeText(getActivity(),"5 second delay bitches",Toast.LENGTH_LONG).show();
            scheduleNotification(getNotification("Move a bit!"), 900000);
           // Toast.makeText(getActivity(),"ISADDED", Toast.LENGTH_LONG).show();

            SharedPreferences mPrefs = getActivity().getSharedPreferences("MyData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = mPrefs.edit();

        if(activityRunning) {

            if (lastSteps == 0) {
                lastSteps = (int) event.values[0]; //check

                //Toast.makeText(getActivity(), "!!!!laststeps was 0 so:" + lastSteps, Toast.LENGTH_SHORT).show();

                int checkCount = mPrefs.getInt("Count" + year + dayyear, -1);
                //this is for the case right after oncreateview
                //if its default -10 that means its the first step of the day
                //else set it to whatever it was earlier today. this is for the
                //case that the app is closed and onresume is recalled, setting laststeps.
                if (checkCount == -1) {
                    //Toast.makeText(getActivity(), "No data found", Toast.LENGTH_SHORT).show();
                    textCount.setText("0");
                } else {
                    textCount.setText(String.valueOf(checkCount));

                }
            }


            else if (lastSteps != (int) event.values[0]) {

                //Toast.makeText(getActivity(), "LASTSTEPS: "+lastSteps+" VALUE[0]: "+(int) event.values[0], Toast.LENGTH_LONG).show();
                int offsetCount = ((int) event.values[0] - lastSteps);
                if (offsetCount <= 0) {
                    offsetCount = 0;
                } else {

                    editor.putInt("Count" + year + dayyear, Integer.parseInt(String.valueOf(textCount.getText())) + offsetCount);
                    editor.commit();
                    textCount.setText(String.valueOf(mPrefs.getInt("Count" + year + dayyear, 0)));

                }


            }


            textTotal.setText(String.valueOf((int) event.values[0]));
            editor.putString("textTotal" + year + dayyear, textTotal.getText().toString());
            editor.commit();


            lastSteps = (int) event.values[0];
            editor.putInt("Last Steps" + year + dayyear, lastSteps);
            editor.commit();

            //added (int) so no decimal



        }


        }
    }
    private void scheduleNotification(Notification notification, int delay) {

        Intent notificationIntent = new Intent(getActivity(), NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);//this -> getActivity

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE); //getActivity
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);



    }

    private Notification getNotification(String content) {


        Calendar calendar = Calendar.getInstance();
        hourofday = calendar.get(Calendar.HOUR_OF_DAY);


        Notification.Builder builder = new Notification.Builder(getActivity()); //this to getActivity
        builder.setContentTitle("Scheduled Notification");
        builder.setContentText(content);
        builder.setSmallIcon(R.mipmap.ic_launcher);

        if(!(hourofday >=22||hourofday<=5)) {
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            builder.setSound(alarmSound);
        }

        return builder.build();
    }



    @Override
    public void onResume() {
        super.onResume();
        //Toast.makeText(getActivity(),"ONRESUME CALLLED!", Toast.LENGTH_LONG).show();

        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        dayyear = calendar.get(Calendar.DAY_OF_YEAR);

        if(isAdded()) {

            SharedPreferences mPrefs = getActivity().getSharedPreferences("MyData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = mPrefs.edit();
            int checkLastSteps = mPrefs.getInt("Last Steps" + year + dayyear, 0);

            //Toast.makeText(getActivity(), "LASTSTEPS: "+lastSteps+" CheckLastSteos: "+checkLastSteps, Toast.LENGTH_LONG).show();
            if (checkLastSteps == 0) {
                lastSteps = 0;
            } else {

                lastSteps = checkLastSteps;
            }

            //textCount.setText("");
            int checkCount = mPrefs.getInt("Count" + year + dayyear, -1);

            if (checkCount == -1) {
                //Toast.makeText(getActivity(), "No data found", Toast.LENGTH_SHORT).show();
                textCount.setText("0");
            } else {
                textCount.setText(String.valueOf(checkCount));
            }

            int checkTotal = Integer.parseInt(mPrefs.getString("textTotal" + year + dayyear, "-1"));
            if (checkTotal == -1) {
                //Toast.makeText(getActivity(), "No data found", Toast.LENGTH_SHORT).show();
                //textCount.setText("0");
            } else {
                textTotal.setText(String.valueOf(checkTotal));

            }
        }

        activityRunning = true;
        //setMoving(activityRunning);

       mStepCounterSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (mStepCounterSensor != null) {
            mSensorManager.registerListener(this,mStepCounterSensor, SensorManager.SENSOR_DELAY_FASTEST );
        }
        //mSensorManager.registerListener(this, mStepDetectorSensor, SensorManager.SENSOR_DELAY_FASTEST);
        else {
            //Toast.makeText(getActivity(),"Count sensor not available!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //activityRunning=false;

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public int getcount()
    {
        return cSteps;
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
