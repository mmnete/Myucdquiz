package com.companywesbite.myucdquiz;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.companywesbite.myucdquiz.questionClasses.quiz;
import com.companywesbite.myucdquiz.utilUI.CreateQuestionDialogBox;
import com.companywesbite.myucdquiz.utils.DatabaseHelper;

public class EditQuiz extends AppCompatActivity {


    private long quizId;
    private quiz thisQuiz;

    // The UI elements
    private ImageView quizImage;
    private Button changeImage;
    private EditText quizName;
    private Button changeQuizName;
    private EditText quizDescription;
    private Button changeQuizDescription;
    private Button viewQuizQuestions;
    private Button addQuestion;


    private static final int MY_PERMISSIONS_REQUEST_GET_IMAGE = 1000;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_quiz);

        // get the information about this quiz
        Intent i = getIntent();

        quizId = i.getLongExtra("quizId", 1000);


        // now let us get our quiz
        DatabaseHelper db = new DatabaseHelper(this);
        thisQuiz = db.getQuiz(quizId);

        // allow the user to go back
        // set the top bar information
        // to display back button
        getSupportActionBar().setTitle("Edit Quiz");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        // declare all the UI elements
         quizImage = (ImageView) findViewById(R.id.quizImage);
         changeImage = (Button) findViewById(R.id.updateImage);
         quizName = (EditText) findViewById(R.id.quizName);
         changeQuizName = (Button) findViewById(R.id.updateQuizName);
         quizDescription = (EditText) findViewById(R.id.quizDescription);
         changeQuizDescription = (Button) findViewById(R.id.updateQuizDescription);
         viewQuizQuestions = (Button) findViewById(R.id.editQuestions);
         addQuestion = (Button) findViewById(R.id.addQuestion);


         addQuestion.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 //Toast.makeText(getBaseContext(),"Hello",Toast.LENGTH_LONG).show();
                 createQuestion();
             }
         });
    }


    @Override
    protected void onResume() {
        super.onResume();
        checkPermission();
    }

    // when the user clicks the back button .. we cannot let the user come back here
    // This is for the back button
    public boolean onOptionsItemSelected(MenuItem item){
       /* Intent myIntent = new Intent(getApplicationContext(), QuizDetailActivity.class);
        myIntent.putExtra("quizId",(long)thisQuiz.getId());
        startActivity(myIntent);
        */
        finish();
        return true;
    }

    // Do we have permission to read from images though....
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
            makeDisplay();
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
                    makeDisplay();
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


    private void makeDisplay()
    {

    }

    private void createQuestion()
    {
        CreateQuestionDialogBox alert = new CreateQuestionDialogBox(thisQuiz.getId(), getApplicationContext());
        alert.showDialog(this, "New Question");
    }

}
