package com.example.darkdark.app;

import android.annotation.TargetApi;
import android.app.Activity;

import android.app.NotificationManager;
//Notification imports -->
import android.app.AlarmManager;
import android.app.Notification;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
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

import com.example.darkdark.app.CircularProgressBar;
import java.util.Calendar;
import java.util.Random;


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
    private TextView textLifeTimeToday;

    private SensorManager mSensorManager;
    private Sensor mStepCounterSensor;
    private CircularProgressBar c1;
    private int cSteps = -1;
    private int dayyear=-1;
    private int year = -1;
    private int hourofday = -1;
    private int month=-1;
    private int date = -1;
    private int lastSteps;
    private int colorSelector=1;
    private int maxMultiple=1;

    Random rand = new Random();

   // CircularProgressBar c3 = (CircularProgressBar) findViewById(R.id.circularprogressbar3);


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
        		c1 = (CircularProgressBar) view.findViewById(R.id.circularprogressbar1);
                c1.setMax(333);


        //lastSteps = 0;
        textLifeTimeToday = (TextView) view.findViewById(R.id.lifetime_today);
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

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onSensorChanged(SensorEvent event) {
        Calendar calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);
        dayyear = calendar.get(Calendar.DAY_OF_YEAR);

        //month = calendar.get(Calendar.MONTH);
        //date = calendar.get(Calendar.DATE);

        if(isAdded()) {

           // Toast.makeText(getActivity(),"5 second delay bitches",Toast.LENGTH_LONG).show();
            scheduleNotification(getNotification("Move a bit!"), 1800000);
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


            else {
                if (lastSteps != (int) event.values[0]) {

                    //Toast.makeText(getActivity(), "LASTSTEPS: "+lastSteps+" VALUE[0]: "+(int) event.values[0], Toast.LENGTH_LONG).show();
                    int offsetCount = ((int) event.values[0] - lastSteps);
                    if (offsetCount <= 0) {
                        offsetCount = 0;
                    } else {


                        editor.putInt("Count" + year + dayyear, Integer.parseInt(String.valueOf(textCount.getText())) + offsetCount);
                        editor.commit();
                        textCount.setText(String.valueOf(mPrefs.getInt("Count" + year + dayyear, 0)));

                        int curr_step = Integer.parseInt(String.valueOf(textCount.getText())) + offsetCount;
                        //textCount.setText(String.valueOf(mPrefs.getInt("Count" + year + dayyear, 0)));
                        Double curr_lifetime = curr_step * 30.0 / 10000.0;
                        editor.putInt("Count" + year + dayyear, curr_step);
                        editor.putString("Lifetime" + year + dayyear, Double.toString(curr_lifetime));

                        editor.commit();


                        textLifeTimeToday.setText(mPrefs.getString("Lifetime" + year + dayyear, "0.0") + " Minutes");


                    }

                    //Toast.makeText(getActivity(), "::"+c1.getMax(), Toast.LENGTH_SHORT).show();
                    //because this is on sensor changed this will do this even when the app isn't open !
                    //so i don't need to worry about color in the next animating function
                    //Toast.makeText(getActivity( ), "TEXTCOUNT!@ "+Integer.parseInt(String.valueOf(textCount.getText()))%c1.getMax(), Toast.LENGTH_SHORT).show();
                    if (Integer.parseInt(String.valueOf(textCount.getText())) % c1.getMax() == 0) {
                        c1.setProgress(0);

                        c1.setmBackgroundColorPaint(c1.getmProgressColorPaint());

                        //  Toast.makeText(getActivity(), "::COLOR SELECTOR: "+colorSelector, Toast.LENGTH_LONG).show();
                        //maxMultiple++;


                        colorSelector = mPrefs.getInt("color", 0);
                        colorSelector++;

                        switch (colorSelector) {
                            case 2:
                                c1.setmProgressColorPaint(Color.rgb(0xff, 0xbf, 0xff));
                                c1.setTitleColor(c1.getmProgressColorPaint());
                                break;

                            case 3:
                                c1.setmProgressColorPaint(Color.rgb(0x48, 0x3d, 0x8b));
                                c1.setTitleColor(c1.getmProgressColorPaint());
                                break;
                            case 4:
                                c1.setmProgressColorPaint(Color.rgb(0xff, 0x00, 0xff));
                                c1.setTitleColor(c1.getmProgressColorPaint());
                                break;
                            case 5:
                                c1.setmProgressColorPaint(Color.rgb(0xbf, 0x00, 0xff));
                                c1.setTitleColor(c1.getmProgressColorPaint());
                                break;
                            default:
                                colorSelector = 1;
                                int r = rand.nextInt(255);
                                int g =rand.nextInt(255);
                                int b = rand.nextInt(255);

                                c1.setmProgressColorPaint(Color.rgb(r,g,b));
                                c1.setTitleColor(c1.getmProgressColorPaint());

                        }
                        if (c1.getmBackgroundColorPaint() == c1.getmProgressColorPaint()) {
                            c1.setmBackgroundColorPaint(Color.rgb(0x12, 0xbb, 0xff));
                        }
                        editor.putInt("color", colorSelector);
                        editor.commit();

                    }
                    //this is not working fully but i don't care right now because who's going
                    c1.setProgress(Integer.parseInt(String.valueOf(textCount.getText())) % c1.getMax());
                    c1.setTitle(textCount.getText() + "");
//                c1.animateProgressTo(Integer.parseInt(String.valueOf(textCount.getText()))%c1.getMax() - offsetCount%c1.getMax(), Integer.parseInt(String.valueOf(textCount.getText()))%c1.getMax(), new CircularProgressBar.ProgressAnimationListener() {
//
//                    @Override
//                    public void onAnimationStart() {
//                    }
//
//                    @Override
//                    public void onAnimationProgress(int progress) {
//                        c1.setTitle(textCount.getText() + "");
//                       // Toast.makeText(getActivity(), "progress streps: "+ progress , Toast.LENGTH_SHORT).show();
//
//                    }
//
//                    @Override
//                    public void onAnimationFinish() {
//                        c1.setSubTitle("");
//                    }
//                });


                }
            }


            //c1.setProgress(Integer.parseInt(String.valueOf(textCount.getText())));
           // c1.setTitle(String.valueOf(textCount.getText()));

            //this will basically tell us how much data we have so we can back track
            // and search until we find all of that data.
            // needs some work.

            editor.putInt("Amount of saved data",mPrefs.getInt("Amount of saved data", 0)+1);

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
        builder.setSmallIcon(R.drawable.ic_notif);

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


        //  Toast.makeText(getActivity(), "COLOR S"+colorSelector, Toast.LENGTH_SHORT).show();


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

                Double lifetimetoday = checkCount * 30.0 / 10000.0;
                textLifeTimeToday.setText(Double.toString(lifetimetoday) + " Minutes");

            }

            int checkTotal = Integer.parseInt(mPrefs.getString("textTotal" + year + dayyear, "-1"));
            if (checkTotal == -1) {
                //Toast.makeText(getActivity(), "No data found", Toast.LENGTH_SHORT).show();
                //textCount.setText("0");
            } else {
                textTotal.setText(String.valueOf(checkTotal));
            }


            //c1.setmBackgroundColorPaint(c1.getmProgressColorPaint());
            colorSelector = mPrefs.getInt("color", 0);

//            if(c1.getmBackgroundColorPaint()!=c1.getmProgressColorPaint()){
//
//
          //  if(c1.getmBackgroundColorPaint()==c1.getmProgressColorPaint()) {

                switch (colorSelector) {
                    //c1.getmProgressColorPaint()==c1.getmBackgroundColorPaint()

                    case 2:
                        //c1.setmBackgroundColorPaint(c1.getmProgressColorPaint());
                        c1.setmProgressColorPaint(Color.rgb(0x00, 0xbf, 0xff));

                        break;

                    case 3:
                        // c1.setmBackgroundColorPaint(c1.getmProgressColorPaint());
                        c1.setmProgressColorPaint(Color.rgb(0x48, 0x3d, 0x8b));
                        break;
                    case 4:
                        //c1.setmBackgroundColorPaint(c1.getmProgressColorPaint());
                        c1.setmProgressColorPaint(Color.rgb(0xff, 0x00, 0xff));
                        break;

                    case 5:
                        //c1.setmBackgroundColorPaint(c1.getmProgressColorPaint());
                        c1.setmProgressColorPaint(Color.rgb(0xbf, 0x00, 0xff));
                        break;
                    default:
                        colorSelector = 1;
                        // c1.setmBackgroundColorPaint(c1.getmProgressColorPaint());
                        c1.setmProgressColorPaint(Color.rgb(0xFE, 0xbf, 0xff));
                }

            if(c1.getmBackgroundColorPaint()==c1.getmProgressColorPaint()){
                c1.setmBackgroundColorPaint(Color.rgb(0x12,0xbb,0xff));
            }
            //}
       // }

           // if(Integer.parseInt(String.valueOf(textCount.getText()))/c1.getMax()>0) {
               // c1.setProgress(0);
                //c1.setmBackgroundColorPaint(c1.getmProgressColorPaint());
                //colorSelector=Integer.parseInt(String.valueOf(textCount.getText()))/c1.getMax();


                //maxMultiple++
                //
                // Toast.makeText(getActivity(), "COLOR S"+colorSelector, Toast.LENGTH_SHORT).show();
               //olorSelector++;


//            else {
//                switch (colorSelector) {
//                    case 2:
//                        // c1.setmBackgroundColorPaint(c1.getmProgressColorPaint());
//                        c1.setmProgressColorPaint(Color.rgb(0x00, 0xbf, 0xff));
//
//                        break;
//
//                    case 3:
//                        c1.setmBackgroundColorPaint(c1.getmProgressColorPaint());
//                        c1.setmProgressColorPaint(Color.rgb(0x48, 0x3d, 0x8b));
//                        break;
//                    case 4:
//                        c1.setmBackgroundColorPaint(c1.getmProgressColorPaint());
//                        c1.setmProgressColorPaint(Color.rgb(0xff, 0x00, 0xff));
//                        break;
//
//                    case 5:
//                        c1.setmBackgroundColorPaint(c1.getmProgressColorPaint());
//                        c1.setmProgressColorPaint(Color.rgb(0xbf, 0x00, 0xff));
//                        break;
//                    default:
//                        colorSelector = 1;
//                        c1.setmBackgroundColorPaint(c1.getmProgressColorPaint());
//                        c1.setmProgressColorPaint(Color.rgb(0xFE, 0xbf, 0xff));
//
//                }

            //}





            c1.animateProgressTo(0, Integer.parseInt(String.valueOf(textCount.getText()))%c1.getMax(), new CircularProgressBar.ProgressAnimationListener() {

                @Override
                public void onAnimationStart() {
                }

                @Override
                public void onAnimationProgress(int progress) {
                    c1.setTitle(textCount.getText() + "");
                    //Toast.makeText(getActivity(), "progress steps: "+ progress , Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onAnimationFinish() {
                    c1.setSubTitle("");
                }
            });
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
