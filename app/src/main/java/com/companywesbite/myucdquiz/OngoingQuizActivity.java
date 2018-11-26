package com.companywesbite.myucdquiz;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.companywesbite.myucdquiz.questionClasses.question;
import com.companywesbite.myucdquiz.questionClasses.quiz;
import com.companywesbite.myucdquiz.utils.DatabaseHelper;
import com.companywesbite.myucdquiz.utils.ONGOINGQuestionListViewAdapter;

import java.util.List;

public class OngoingQuizActivity extends AppCompatActivity{



    private long quizId;
    private quiz thisQuiz;
    private List<question> questions;

    private ListView list;
    private ONGOINGQuestionListViewAdapter adapter;
    private List<question> questionList;
    private NotificationManager mNotificationManager;

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

        // set the top bar information

        getSupportActionBar().setTitle(thisQuiz.getName() + " (ON-GOING)");
        list = (ListView) findViewById(R.id.listView);
        questionList = thisQuiz.getQuestions();
        adapter = new ONGOINGQuestionListViewAdapter(this,this,questionList,quizId);
        list.setAdapter(adapter);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(!NotificationManagerCompat.from(getApplicationContext()).areNotificationsEnabled())
        {
            Toast.makeText(this,"Please turn on notifications!",Toast.LENGTH_LONG).show();
            Intent intent = new Intent();
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");

           //for Android 5-7
            intent.putExtra("app_package", getPackageName());
            intent.putExtra("app_uid", getApplicationInfo().uid);

            // for Android O
            intent.putExtra("android.provider.extra.APP_PACKAGE", getPackageName());

            startActivity(intent);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        sendNotification();
    }


    public void sendNotification() {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getApplicationContext(), "notify_001");

        Intent intent = new Intent(getApplicationContext(), OngoingQuizActivity.class);
        intent.putExtra("quizId", quizId);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), (int)System.currentTimeMillis(), intent, 0);


        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText("MYUCD[ON GOING] Quiz");
        bigText.setBigContentTitle("SUBJECT: "+thisQuiz.getName());
        bigText.setSummaryText("Let us get back at it");

        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher_round);
        mBuilder.setContentTitle("Quizer");
        mBuilder.setContentText("Go back");
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        mBuilder.setStyle(bigText);
        mBuilder.setAutoCancel(true);

        mNotificationManager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("notify_001",
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(channel);
        }

        mNotificationManager.notify(0, mBuilder.build());
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


}


