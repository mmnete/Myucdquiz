package com.companywesbite.myucdquiz;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.companywesbite.myucdquiz.questionClasses.question;
import com.companywesbite.myucdquiz.questionClasses.quiz;
import com.companywesbite.myucdquiz.utils.DatabaseHelper;

import java.util.HashMap;
import java.util.Map;

/***
 *
 * Team: Flashcards Pro
 * Date: 12/09/2018
 * Name: CreateNewQuiz
 * Functionality: It is the activity that gets called from the
 *                quizmainpagefragment that just allows the user to create a new quiz
 *                All the changes are propagated to the sqllite database using the databasehelper object
 *                It also has an object for error tolerance
 *                The error tolerance allows us to set the degree in which the
 *                similarity between the user answer and the actual answer should be compared on
 *                some answers should be exact while other should just be very close.
 *
 */



public class CreateNewQuiz extends AppCompatActivity {



    private EditText quizName;
    private EditText quizDescription;
    private String quizImageName = "default";
    private Button submitButton;
    private SeekBar errorToleranceBar;
    private TextView percent;
    private TextView sensitivity; //shows if error tolerance is high, average or low
    private TextView sensitivityDescription; //Explains what high, average or low means



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_quiz);

        // to display back button
        getSupportActionBar().setTitle("New Quiz");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        quizName = (EditText) findViewById(R.id.quizName);
        quizDescription = (EditText) findViewById(R.id.quizDescription);
        submitButton = (Button) findViewById(R.id.createQuizButton);
        errorToleranceBar = (SeekBar) findViewById(R.id.errorTolerance);
        percent = (TextView) findViewById(R.id.percent);
        sensitivity = (TextView) findViewById(R.id.sensitivity);
        sensitivityDescription = (TextView) findViewById(R.id.sensitivityDescription);
        errorToleranceBar.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
        errorToleranceBar.getThumb().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);

        errorToleranceBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                percent.setText(Integer.toString(progress) + "%");
                //If progress is below 10%, error sensitivity is low
                if (progress <= 0) {
                    sensitivity.setText("No Error tolerance");
                    sensitivity.setTextColor(Color.RED);
                    sensitivityDescription.setText("No deviation from correct answer is accepted. Good for learning vocabulary.");
                } else if (progress <= 20) {
                    sensitivity.setText("Low Error tolerance");
                    sensitivity.setTextColor(Color.parseColor("#FF8C00"));
                    sensitivityDescription.setText("Minor deviation from correct answer is accepted. Good for allowing typos.");
                } else {
                    sensitivity.setText("High Error tolerance");
                    sensitivity.setTextColor(Color.GREEN);
                    sensitivityDescription.setText("Big deviation from correct answer is accepted. Good for anything that just needs to be vaguely correct.");
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        // when the user is done filling in the form..
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createQuiz();
            }
        });
    }

    // This is for the back button
    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;
    }


    // when the button is clicked
    private void createQuiz()
    {
        // check if all th
        if(quizName.getText().toString().trim().length() < 1)
        {
            Toast.makeText(this,"Quiz Name can't be empty!",Toast.LENGTH_LONG).show();
        } else if(quizDescription.getText().toString().trim().length() < 1)
        {
            Toast.makeText(this,"Quiz Description can't be empty!",Toast.LENGTH_LONG).show();
        }  else
        {
            Map<Long, question> newQuestions = new HashMap<>();
            quiz newQuiz = new quiz(quizName.getText().toString(),quizDescription.getText().toString(),newQuestions,errorToleranceBar.getProgress());
            DatabaseHelper db = new DatabaseHelper(this);
            db.createQuiz(newQuiz);
            Toast.makeText(this,"Quiz Created Successfully!",Toast.LENGTH_LONG).show();
            finish();
        }
    }

}
