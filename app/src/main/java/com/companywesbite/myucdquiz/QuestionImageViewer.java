package com.companywesbite.myucdquiz;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.companywesbite.myucdquiz.questionClasses.question;
import com.companywesbite.myucdquiz.utils.DatabaseHelper;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

/***
 *
 *
 *
 * Team: Flashcards Pro
 * Date: 12/09/2018
 * Name:  QuestionImageViewer
 * Functionality: This activity was accessed from the answerquestion alert box
 *                This activity allows the user to view the image associated with a specific question if present
 *                The activity uses the database object to get the url of the image
 *                and it uses the subsampling-scale-image API to load UI elements to handle drag and zoom events
 *
 *
 */




public class QuestionImageViewer extends AppCompatActivity {


    private long questionId;
    private question thisQuestion;
    private SubsamplingScaleImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_image_viewer);


        // get the information about this quiz
        Intent i = getIntent();

        questionId = i.getLongExtra("questionId", 1000);



        // now let us get our quiz
        DatabaseHelper db = new DatabaseHelper(this);
        thisQuestion = db.getQuestion((long)questionId);

        getSupportActionBar().setTitle(thisQuestion.getTitle());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        imageView = (SubsamplingScaleImageView) findViewById(R.id.questionImage);
        Log.d("TAG","SHHHH"+thisQuestion.getPicturePath());
        imageView.setImage(ImageSource.bitmap(BitmapFactory.decodeFile(thisQuestion.getPicturePath())));

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
}
