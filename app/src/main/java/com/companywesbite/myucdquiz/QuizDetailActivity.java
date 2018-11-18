package com.companywesbite.myucdquiz;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.companywesbite.myucdquiz.questionClasses.quiz;
import com.companywesbite.myucdquiz.utils.DatabaseHelper;

public class QuizDetailActivity extends AppCompatActivity {


    private long quizId;
    private quiz thisQuiz;

    // UI elements
    private ImageView quizImage;
    private TextView quizDescription;
    private TextView quizNumQuestions;

    private Button deleteQuiz;
    private Button changeQuiz;
    private Button startQuiz;



    private static final int MY_PERMISSIONS_REQUEST_GET_IMAGE = 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_detail);

        // get the information about this quiz
        Intent i = getIntent();

        quizId = i.getLongExtra("quizId", 1000);


        // now let us get our quiz
        DatabaseHelper db = new DatabaseHelper(this);
        thisQuiz = db.getQuiz((long)quizId);

        // set the top bar information
        // to display back button
        getSupportActionBar().setTitle(thisQuiz.getName());

         quizImage = (ImageView) findViewById(R.id.quizImage);
         quizDescription = (TextView) findViewById(R.id.quizDescription);
         quizNumQuestions = (TextView) findViewById(R.id.numQuestions);

         deleteQuiz = (Button) findViewById(R.id.deleteQuiz);
         changeQuiz = (Button) findViewById(R.id.editQuiz);
         startQuiz = (Button) findViewById(R.id.takeQuiz);

         // when the user wants to delete the quiz
        deleteQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });

        changeQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit();
            }
        });

        startQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), OngoingQuizActivity.class);
                i.putExtra("quizId", quizId);
                startActivity(i);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        // start the display here...
        checkPermission();
    }

    private void makeDisplay()
    {
        // now let us get our quiz
        DatabaseHelper db = new DatabaseHelper(this);
        thisQuiz = db.getQuiz((long)quizId);

        if(thisQuiz.getQuizPictureFileName().equals("default"))
        {
            quizImage.setImageResource(R.drawable.defaultquizimage);
        } else
        {
            quizImage.setImageBitmap(BitmapFactory.decodeFile(thisQuiz.getQuizPictureFileName()));
        }

        quizDescription.setText(thisQuiz.getDescription());
        quizNumQuestions.setText("Questions:  "+Integer.toString(thisQuiz.numberOfQuestions));
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


    // deleting this quiz
    private void delete()
    {

        AlertDialog alertDialog =
        new AlertDialog.Builder(this)
                // Set Dialog Title
                .setTitle("ALERT!")
                // Set Dialog Message
                .setMessage("Alert are you sure you want to delete this quiz?")

                // Positive button
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Do something else
                        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
                        db.deleteQuiz(thisQuiz.getId());
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                })

                // Negative Button
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Do something else
                        Toast.makeText(getApplicationContext(),"Good Choice, keep trying dont stop :-)",Toast.LENGTH_LONG).show();
                    }
                }).create();
        alertDialog.show();
    }

    // edit this quiz
    private void edit()
    {
        Intent i = new Intent(this, EditQuiz.class);
        i.putExtra("quizId", (long)thisQuiz.getId());
        startActivity(i);
    }



}
