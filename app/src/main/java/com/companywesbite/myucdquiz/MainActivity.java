package com.companywesbite.myucdquiz;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.companywesbite.myucdquiz.utils.QuizListViewAdapter;


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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context=this;
        ListView lstview=(ListView)findViewById(R.id.quizList);
        lstview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Toast.makeText(context, "An item of the ListView is clicked.", Toast.LENGTH_LONG).show();
            }
        });

        String[] items={"1","2","3","4","5","6","7","8","9","10","11","12"};
        QuizListViewAdapter adapter=new QuizListViewAdapter(this,R.layout.quiz_item,R.id.quizName,items);
        // Bind data to the ListView
        lstview.setAdapter(adapter);




    }

    public void clickMe(View view){
        Button bt=(Button)view;
        Toast.makeText(this, "Button "+bt.getText().toString(),Toast.LENGTH_LONG).show();
    }



}
