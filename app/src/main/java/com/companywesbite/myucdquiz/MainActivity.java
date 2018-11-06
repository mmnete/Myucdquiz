package com.companywesbite.myucdquiz;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.companywesbite.myucdquiz.questionClasses.quiz;
import com.companywesbite.myucdquiz.utils.DatabaseHelper;
import com.companywesbite.myucdquiz.utils.QuizListViewAdapter;

import java.util.List;


/*********
 * This is the class that displays the items with the numbers
 * Those will be transformed to the different quizes the user has saved
 * We can simply query information from the database and display them in this listview
 * Each item has a button so that the user can press to view the quiz and do all other stuff
 * There is another button to take a quiz.
 *
 */






public class MainActivity extends AppCompatActivity {

    Context context;

    private Button newQuizButton;
    private ListView lstview;
    private static final int MY_PERMISSIONS_REQUEST_GET_IMAGE = 1000;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context=this;
        lstview=(ListView)findViewById(R.id.quizList);

        // creating a new quiz.
        newQuizButton = (Button) findViewById(R.id.addQuizButton);
        newQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), CreateNewQuiz.class);
                startActivity(i);
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        // if the user does not allow us to read any images we cannot display anything
        newQuizButton.setEnabled(false);
        checkPermission();
    }

    // Ask for permission to read images unless close the application
    private void checkPermission()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Permission is not granted
            // Should we show an explanation?

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_GET_IMAGE);
        }else
        {
            // we are good to go
            newQuizButton.setEnabled(true);
            updateQuizListView();
        }
    }

    // Does the user allow us to check for images
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_GET_IMAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    newQuizButton.setEnabled(true);
                    updateQuizListView();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }



    public void clickMe(long quizId){
        Intent i = new Intent(this, QuizDetailActivity.class);
        i.putExtra("quizId", quizId);
        startActivity(i);
    }

    private void updateQuizListView()
    {
        DatabaseHelper db = new DatabaseHelper(this);
        final List<quiz> quizes = db.getAllQuizes();


        QuizListViewAdapter adapter=new QuizListViewAdapter(this,R.layout.quiz_item,R.id.quizName,quizes);
        // Bind data to the ListView

        lstview.setAdapter(adapter);
       lstview.setEmptyView((TextView)findViewById(R.id.emptyElement));
        lstview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                clickMe(quizes.get(position).getId());
            }
        });

    }


}
