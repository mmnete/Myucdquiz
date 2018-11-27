package com.companywesbite.myucdquiz;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.support.design.widget.TabLayout;
import android.widget.Toast;


/*********
 * This is the class that displays the items with the numbers
 * Those will be transformed to the different quizes the user has saved
 * We can simply query information from the database and display them in this listview
 * Each item has a button so that the user can press to view the quiz and do all other stuff
 * There is another button to take a quiz.
 *
 */






public class MainActivity extends AppCompatActivity {


    private static final String TAG = "MainActivity";

    private SectionsPageAdapter mSectionsPageAdapter;

    private ViewPager mViewPager;

    private static final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: Starting.");

        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        // Remove shadow from Action Bar
        this.getSupportActionBar().setElevation(0);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        checkPermission();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new QuizMainPageFragment(), "Collections");
        adapter.addFragment(new SnapNotesFragment(), "SNAP-NOTES");
        adapter.addFragment(new AskFragment(), "ASK QUESTIONS");
        viewPager.setAdapter(adapter);
    }


    // Ask for permission to read images unless close the application
    private void checkPermission()
    {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Permission is not granted
            // Should we show an explanation?

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
        }else
        {

        }
    }

    // Does the user allow us to check for images
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this,"We need write permission to be able to share quizes",Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }





}
