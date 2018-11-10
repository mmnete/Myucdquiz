package com.companywesbite.myucdquiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.companywesbite.myucdquiz.questionClasses.question;
import com.companywesbite.myucdquiz.questionClasses.quiz;
import com.companywesbite.myucdquiz.utilUI.EditQuestionDialogBox;
import com.companywesbite.myucdquiz.utils.DatabaseHelper;
import com.companywesbite.myucdquiz.utils.QuestionListViewAdapter;

import java.util.List;

public class ViewQuestionsActivity extends AppCompatActivity  {




    private long quizId;
    private quiz thisQuiz;

    private ListView lstview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_questions);


        // get the information about this quiz
        Intent i = getIntent();

        quizId = i.getLongExtra("quizId", 1000);


        // now let us get our quiz
        DatabaseHelper db = new DatabaseHelper(this);
        thisQuiz = db.getQuiz((long)quizId);

        lstview = (ListView) findViewById(R.id.questionsListView);

        getSupportActionBar().setTitle(thisQuiz.getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        updateQuizListView();



    }


    @Override
    public void onResume() {
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


    private void updateQuizListView()
    {

        final List<question> questions = thisQuiz.getQuestions();


        QuestionListViewAdapter adapter=new QuestionListViewAdapter(this,R.layout.question_item,R.id.questionTitle,questions, thisQuiz);
        // Bind data to the ListView

        lstview.setAdapter(adapter);
        lstview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                editQuestion(questions.get(position).getId());
            }
        });

    }


    private void editQuestion(long questionId)
    {
        EditQuestionDialogBox alert = new EditQuestionDialogBox(questionId, getApplicationContext());
        alert.showDialog(this, "Edit Question");
    }


}

