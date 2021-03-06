package com.companywesbite.myucdquiz;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.companywesbite.myucdquiz.questionClasses.question;
import com.companywesbite.myucdquiz.questionClasses.quiz;
import com.companywesbite.myucdquiz.utilUI.AnswerQuestionDialogBox;
import com.companywesbite.myucdquiz.utilUI.CreateQuestionDialogBox;
import com.companywesbite.myucdquiz.utils.DatabaseHelper;
import com.companywesbite.myucdquiz.utils.ONGOINGQuestionListViewAdapter;

import java.util.List;


/***
 *
 *
 *
 * Team: Flashcards Pro
 * Date: 12/09/2018
 * Name:  QuizDetailActivity
 * Functionality: This activity was accessed from the quizmainpagefragment
 *                It shows all the necessary details of a quiz using a nice UI
 *                It allows the user to add a question or make changes on the quiz jusing floating action buttons
 *                It allows the user to start answering questions by loading up the lightweight answerquestion alert box
 *                When a question is clicked
 *
 *                It also contains shuffle functionality to start the answer question alert box from any question.
 *
 *                It uses the database object to fetch all this information.
 *
 *
 */



public class QuizDetailActivity extends AppCompatActivity {


    private long quizId;
    private quiz thisQuiz;

    // UI elements
    private TextView quizDescription;
    private TextView quizNumQuestions;
    private ListView list;
    private List<question> questionList;
    private List<question> questions;
    private ONGOINGQuestionListViewAdapter adapter;

    private Button deleteQuiz;
    private Button changeQuiz;
    private Button startQuiz;
    private Button shuffleButton;
    private FloatingActionButton addQuestion;
    private FloatingActionButton editQuiz;
    private FloatingActionButton deleteButton, menuButton;
    private TextView percentage;
    private ProgressBar progressBar;
    private AnswerQuestionDialogBox dialogBox;

    private static final int MY_PERMISSIONS_REQUEST_GET_IMAGE = 1000;
    private static final int RESULT_LOAD_IMAGE = 1;

    private CreateQuestionDialogBox alert;
    private boolean deleted = false;


    private boolean menuOpen = false;

    private DatabaseHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_detail);

        // get the information about this quiz
        Intent i = getIntent();

        quizId = i.getLongExtra("quizId", 1000);

       // Log.d("TAG","Recieved"+)

        // now let us get our quiz
         db = new DatabaseHelper(this);

        thisQuiz = db.getQuiz(quizId);

     //Log.d("TAG",Double.toString(thisQuiz.getErrorTolerance()));

        // set the top bar information
        // to display back button
        getSupportActionBar().setTitle(thisQuiz.getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        quizDescription = (TextView) findViewById(R.id.quizDescription);
        quizNumQuestions = (TextView) findViewById(R.id.numQuestions);
         addQuestion = (FloatingActionButton) findViewById(R.id.addQuestion);
         editQuiz = (FloatingActionButton) findViewById(R.id.editQuizFloatingButton);
         deleteButton = (FloatingActionButton) findViewById(R.id.deleteQuizButton);
         menuButton = (FloatingActionButton) findViewById(R.id.menuButton);
         deleteQuiz = (Button) findViewById(R.id.deleteQuiz);
         changeQuiz = (Button) findViewById(R.id.editQuiz);
         startQuiz = (Button) findViewById(R.id.takeQuiz);
         percentage = (TextView) findViewById(R.id.percentage);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        shuffleButton = (Button) findViewById(R.id.shuffleButton);


         //update the quiz table
        updateQuizTable();

         // when the user wants to delete the quiz
        deleteQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });

        editQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit();
            }
        });

        addQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createQuestion();
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

        shuffleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(questionList.size() > 0)
                {
                    adapter.startRandomQuiz();
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!menuOpen)
                {
                    makeButtonsVisible();
                } else
                {
                    makeButtonsInvisible();
                }

            }
        });
    }

    private void makeButtonsInvisible(){
        addQuestion.setVisibility((View.INVISIBLE));

        startQuiz.setVisibility((View.INVISIBLE));

        editQuiz.setVisibility((View.INVISIBLE));

        deleteButton.setVisibility((View.INVISIBLE));
        menuOpen = false;
    }

    private void makeButtonsVisible(){
        addQuestion.setVisibility((View.VISIBLE));

        startQuiz.setVisibility((View.VISIBLE));

        editQuiz.setVisibility((View.VISIBLE));

        deleteButton.setVisibility((View.VISIBLE));
        menuOpen = true;
    }



    private void createQuestion()
    {
        alert = new CreateQuestionDialogBox(thisQuiz.getId(), getApplicationContext());
        alert.showDialog(this, "New Question");
    }

    // This is done to update any changes made when the activity comes into view
    // Like when an answer is submitted or when a question is deleted
    @Override
    public void onWindowFocusChanged(boolean hasFocus){
        // Update the quiz table
        if(!deleted)
        {
            updateQuizTable();
        }
        // When the focus changes (e.g. to a new fragment), the three FloatingActionbuttons should disappear
        if (menuOpen) makeButtonsInvisible();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // start the display here...
        checkPermission();

        // Update the quiz table
        updateQuizTable();
    }

    public void updateQuizTable(){


        thisQuiz = db.getQuiz(quizId);
        questions = thisQuiz.getQuestions();
        int perCorrect = (int) (thisQuiz.getPercentCorrect()*100);
        //Set title that shows how many questions are in the Flashcards Collection
        quizNumQuestions.setText("Questions: "+Integer.toString(thisQuiz.getQuestionNumber())+
                                    "\nError Tolerance: "+Integer.toString(thisQuiz.getErrorTolerance()) +"%");
        //list is the ListView on this screen
        progressBar.setProgress(perCorrect);
        //Set percentage inside progress bar to right amount
        percentage.setText(perCorrect+ "%");

        list = (ListView) findViewById(R.id.listView);
        //questionList is a List<> with the questions of this Flashcards Collection
        questionList = thisQuiz.getQuestions();
        //Go through all the questions...
        for (int i = 0; i<thisQuiz.getQuestionNumber(); i++) {
            //If (100 - current score), so the error percentage is below the error tolerance
            if (100 - questions.get(i).getCurrScore() < thisQuiz.getErrorTolerance()) {
                //Question is accepted
                questions.get(i).setCorrect(1);
            } else {
                //Question is not accepted
                questions.get(i).setCorrect(0);
            }
           // db2.updateQuestion(questions.get(i));
            adapter = new ONGOINGQuestionListViewAdapter(this, this, questionList, thisQuiz, db);
            list.setAdapter(adapter);
        }
    }

    private void makeDisplay()
    {
        // now let us get our quiz
        thisQuiz = db.getQuiz(quizId);
        quizDescription.setText(thisQuiz.getDescription());
        int perCorrect = (int) thisQuiz.getPercentCorrect();
        quizNumQuestions.setText("Questions: "+Integer.toString(thisQuiz.getQuestionNumber())+
                                    "\nError Tolerance: "+Integer.toString(thisQuiz.getErrorTolerance())+"%");
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //  super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            //quizImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            this.alert.questionImage = picturePath;
            this.alert.addImage.setText("Image selected");

        }
    }

    // deleting this quiz
    private void delete()
    {
        deleted = true;
        db.deleteQuiz(thisQuiz.getId());
        Toast.makeText(getApplicationContext(),"Flashcards Collection Deleted!",Toast.LENGTH_LONG).show();
        finish();
    }

    // edit this quiz
    private void edit()
    {
        Intent i = new Intent(this, EditQuiz.class);
        i.putExtra("quizId", (long)thisQuiz.getId());
        startActivity(i);
    }



}
