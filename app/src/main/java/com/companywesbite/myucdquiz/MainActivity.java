package com.companywesbite.myucdquiz;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.companywesbite.myucdquiz.utils.Constant;
import com.companywesbite.myucdquiz.utils.DbManager;
import com.companywesbite.myucdquiz.utils.MySqliteHelper;
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
    private MySqliteHelper helper;
    Context context;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        helper = DbManager.getInstance(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Button bt = (Button) findViewById(R.id.addQuizButton);
        bt.setOnClickListener(new View.OnClickListener(){
             public void onClick(View v){
                 SQLiteDatabase db = helper.getWritableDatabase();
                 String sql1 = "insert into"+ Constant.TABLE_NAME +"valuse(1,'English','English Words',10)";
                 DbManager.execSQL(db,sql1);
                 String sql2 = "insert into"+ Constant.TABLE_NAME +"valuse(2,'History','History Problems',5)";
                DbManager.execSQL(db,sql2);
                db.close();
             }});
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
