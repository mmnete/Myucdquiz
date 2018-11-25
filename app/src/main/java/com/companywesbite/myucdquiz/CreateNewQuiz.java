package com.companywesbite.myucdquiz;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.companywesbite.myucdquiz.questionClasses.question;
import com.companywesbite.myucdquiz.questionClasses.quiz;
import com.companywesbite.myucdquiz.utils.DatabaseHelper;

import java.util.HashMap;
import java.util.Map;

public class CreateNewQuiz extends AppCompatActivity {



    private EditText quizName;
    private EditText quizDescription;
    private String quizImageName = "default";
    private Button submitButton;



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

        // set the default value of the quizImage
      //  quizImage.setImageResource(R.drawable.defaultquizimage);



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
        if(quizName.getText().toString().trim().length() < 3)
        {
            Toast.makeText(this,"Quiz Name too short!",Toast.LENGTH_LONG).show();
        } else if(quizDescription.getText().toString().trim().length() < 5)
        {
            Toast.makeText(this,"Quiz Description too short!",Toast.LENGTH_LONG).show();
        } else
        {
            Map<Long, question> newQuestions = new HashMap<>();
            quiz newQuiz = new quiz(quizName.getText().toString(),quizDescription.getText().toString(),newQuestions);
            DatabaseHelper db = new DatabaseHelper(this);
            db.createQuiz(newQuiz);
            Toast.makeText(this,"Quiz Created Successfully!",Toast.LENGTH_LONG).show();
            finish();
        }
    }

}
