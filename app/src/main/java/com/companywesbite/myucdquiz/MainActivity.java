package com.companywesbite.myucdquiz;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.support.design.widget.TabLayout;


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



}
