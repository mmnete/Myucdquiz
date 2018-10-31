package com.companywesbite.myucdquiz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
        updateQuizListView();
    }

    public void clickMe(View view){
        Button bt=(Button)view;
        Toast.makeText(this, "Button "+bt.getText().toString(),Toast.LENGTH_LONG).show();
    }

    private void updateQuizListView()
    {
        DatabaseHelper db = new DatabaseHelper(this);
        List<quiz> quizes = db.getAllQuizes();


        lstview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Toast.makeText(context, "An item of the ListView is clicked.", Toast.LENGTH_LONG).show();
            }
        });

        QuizListViewAdapter adapter=new QuizListViewAdapter(this,R.layout.quiz_item,R.id.quizName,quizes);
        // Bind data to the ListView

        lstview.setAdapter(adapter);
       lstview.setEmptyView((TextView)findViewById(R.id.emptyElement));

    }


}
