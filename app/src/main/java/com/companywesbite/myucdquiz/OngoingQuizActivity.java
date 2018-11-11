package com.companywesbite.myucdquiz;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.view.MenuItem;
import android.widget.TextView;

import com.companywesbite.myucdquiz.questionClasses.question;
import com.companywesbite.myucdquiz.questionClasses.quiz;
import com.companywesbite.myucdquiz.utils.DatabaseHelper;
import com.companywesbite.myucdquiz.utils.ShakeListener;

import java.util.List;

public class OngoingQuizActivity extends AppCompatActivity{


    private long quizId;
    private quiz thisQuiz;
    private List<question> questions;
    private PageAdapter adapter;
    private ViewPager pager;
    private ShakeListener mShaker;
    UpdateFrag updatfrag;

    int unansweredQuestions = 0;
    private TextView numberOfUnasweredQuestions;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ongoing_quiz);



        // get the information about this quiz
        Intent i = getIntent();

        quizId = i.getLongExtra("quizId", 1000);


        // now let us get our quiz
        DatabaseHelper db = new DatabaseHelper(this);
        thisQuiz = db.getQuiz((long)quizId);

        questions = thisQuiz.getQuestions();

        pager = (ViewPager) findViewById(R.id.vpPager);
        adapter = new PageAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);

        unansweredQuestions = thisQuiz.numberOfQuestions;

        numberOfUnasweredQuestions = (TextView) findViewById(R.id.answered);

        numberOfUnasweredQuestions.setText(Integer.toString(unansweredQuestions)+" Questions");



        // set the top bar information
        // to display back button

        getSupportActionBar().setTitle(thisQuiz.getName() + " (ON-GOING)");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);



        mShaker = new ShakeListener(this);
        mShaker.setOnShakeListener(new ShakeListener.OnShakeListener () {
            public void onShake()
            {
                updatfrag.updatefrag();
            }
        });

        // This is where the quiz is going on
        // we need a notification when the activity is interrupted or the user clicks home button
        // when the notification is clicked, this activity does onResume()
        // In the notification the title can be .. ongoing quiz

    }

	@Override
    protected void onPause() {
        super.onPause();
		// Set the startup program, if it exists, find out, otherwise new startup
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.setComponent(new ComponentName(this, OngoingQuizActivity.class));//Get the class object with ComponentName
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);// The key step is to set the startup mode, two cases
		
		NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
			.setContentTitle("QUIZ")
            .setContentText("ongoing quiz.");
        Notification notification = builder.build();
	    
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);//Bind the set Intent to the PendingIntent
		notification.contentIntent = contentIntent;// Notification binding PendingIntent
		notification.flags=Notification.FLAG_AUTO_CANCEL;//Set auto cancel
		NotificationManager manager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		manager.notify(1, notification);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    // when the user clicks the back button .. we cannot let the user come back here
    // This is for the back button
    public boolean onOptionsItemSelected(MenuItem item){
       /* Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(myIntent);
        */
        finish();
        return true;
    }


    public class PageAdapter extends FragmentPagerAdapter {

        public PageAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return questions.size();
        }

        @Override
        public Fragment getItem(int position) {
            return new SlidingQuestionPageFragment(questions.get(position));
        }

    }

    public void updateApi(UpdateFrag listener) {
        updatfrag = listener;
    }





}

interface UpdateFrag {
    public void updatefrag();
}
