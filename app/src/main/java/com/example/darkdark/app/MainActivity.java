package com.example.darkdark.app;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.hardware.*;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;


import org.w3c.dom.Text;

import java.io.Console;


public class MainActivity extends AppCompatActivity/*FragmentActivity*/ implements SecFragment.OnFragmentInteractionListener,
                                                                                SecondFragment.OnFragmentInteractionListener,
                                                                                ThirdFragment.OnFragmentInteractionListener,
                                                                                Animation.OnFragmentInteractionListener{

    public static final String DEFAULT="N/A";
//    private TextView textCount;
//    private TextView textTotal;
    public SensorManager mSensorManager;
    public Sensor mStepCounterSensor;
    public Sensor mStepDetectorSensor;
    // 10/29 --->
    boolean activityRunning;
    EditText mHeight;
    EditText mWeight;
    TextView mCount;
    TextView mTotal;
    private int steps = -1;


    // <---- 10/29
    private String PREV_S;
    private Toolbar mToolbar; //v7 widget supports older devices
    private TabLayout mTabLayout;
    private ViewPager mPager;
    private MyPagerAdapter mAdapter;
    //11/7 ---->
    private SharedPreferences mPrefs;


    // <---- 11/7

    //----> 11/28
    private int[] tabIcons = {
            R.drawable.ic_tab_home,
            R.drawable.ic_tab_his, //Yo, my icon is UUUUGGGLLLEEEE. maybe we'll change it back later
            R.drawable.ic_tab_biometrics,
            R.drawable.ic_tab_poring
    };

    // 11/28 <---

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
//        if (savedInstanceState != null)
//        {
//            steps = savedInstanceState.getInt(PREV_S) - 1;
//        }
        setContentView(R.layout.fragment_main);//changed from activity_main cause it was just calling fragment_main anyway

        mToolbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(mToolbar);

        mAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
        mTabLayout.setTabsFromPagerAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mPager);

        mPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        setupTabIcons();







        //fd
        SharedPreferences mPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);


    }

    private void setupTabIcons() {



            mTabLayout.getTabAt(0).setIcon(tabIcons[0]);
            mTabLayout.getTabAt(1).setIcon(tabIcons[1]);
            mTabLayout.getTabAt(2).setIcon(tabIcons[2]);
            mTabLayout.getTabAt(3).setIcon(tabIcons[3]);

    }


    @Override
    protected void onResume() {
        super.onResume();


    }
    @Override
    protected void onPause() {
        super.onPause();
       // activityRunning = false;
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    protected void onSaveInstanceState(Bundle savedInstanceState) {
        //SecFragment fragment = (SecFragment) getSupportFragmentManager().findFragmentById(R.id.frag1);
        //int total = fragment.gettotal();
        //savedInstanceState.putInt(PREV_S, total);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        //Toast.makeText(this,"Success",Toast.LENGTH_SHORT).show();

    }


    //For TABLAYOUT CRAP
    class MyPagerAdapter extends FragmentStatePagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        //gives position and ask to return fragment at that position
        {
            switch(position) {

                case 1:
                    return SecondFragment.newInstance("2ndFragment", "2");
                case 2:
                    return ThirdFragment.newInstance("3rdFragment", "Biometrics");
                case 3:
                    return Animation.newInstance("4thFragment","Animation");
                default:
                    return SecFragment.newInstance("FirstFragment", "Main");
            }
            //return fragment at the position
        }

        @Override
        public int getCount() {
            return 4;
        }
        
//        public Drawable getPageTitle(int position){
//
//        }





        public CharSequence getPageTitle(int position){

            return null;
//            switch(position){
//                case 0: return "Main";
//                case 1: return "History";
//                case 2: return "Biometrics";
//                case 3: return "Poring";
//                default: return "0";
//            }

        }
    }




}