package com.companywesbite.myucdquiz;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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
    private static final int RESULT_LOAD_IMAGE = 1;





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

        // declare all the UI elements
         quizImage = (ImageView) findViewById(R.id.quizImage);
         changeImage = (Button) findViewById(R.id.updateImage);
         quizName = (EditText) findViewById(R.id.quizName);
         changeQuizName = (Button) findViewById(R.id.updateQuizName);
         quizDescription = (EditText) findViewById(R.id.quizDescription);
         changeQuizDescription = (Button) findViewById(R.id.updateQuizDescription);
         viewQuizQuestions = (Button) findViewById(R.id.editQuestions);
         addQuestion = (Button) findViewById(R.id.addQuestion);

         changeImage.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 selectNewImage();
             }
         });

         changeQuizName.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 String newName = quizName.getText().toString().trim();
                 if(newName.length() < 5)
                 {
                     Toast.makeText(getBaseContext(),"Quiz Name Too Short!",Toast.LENGTH_LONG).show();
                     return;
                 }
                 thisQuiz.setName(newName);
                 DatabaseHelper db = new DatabaseHelper(getBaseContext());
                 db.updateQuiz(thisQuiz);
                 Toast.makeText(getBaseContext(),"Change Made",Toast.LENGTH_LONG).show();
             }
         });

         changeQuizDescription.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 String newDescription = quizDescription.getText().toString().trim();
                 if(newDescription.length() < 10)
                 {
                     Toast.makeText(getBaseContext(),"Quiz Description Too Short!",Toast.LENGTH_LONG).show();
                     return;
                 }
                 thisQuiz.setDescription(newDescription);
                 DatabaseHelper db = new DatabaseHelper(getBaseContext());
                 db.updateQuiz(thisQuiz);
                 Toast.makeText(getBaseContext(),"Change Made",Toast.LENGTH_LONG).show();
             }
         });

         addQuestion.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 //Toast.makeText(getBaseContext(),"Hello",Toast.LENGTH_LONG).show();
                 createQuestion();
             }
         });

         viewQuizQuestions.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent i = new Intent(getApplicationContext(), ViewQuestionsActivity.class);
                 i.putExtra("quizId",(long)thisQuiz.getId());
                 startActivity(i);
             }
         });


    }


    @Override
    protected void onResume() {
        super.onResume();


        // the buttons are in active until we have permission to view storage
        changeImage.setActivated(false);
        changeQuizName.setActivated(false);
        changeQuizDescription.setActivated(false);
        viewQuizQuestions.setActivated(false);
        addQuestion.setActivated(false);

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

            Toast.makeText(getApplicationContext(),"This page is inactive without storage permission",Toast.LENGTH_LONG).show();

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
                    Toast.makeText(getApplicationContext(),"This page is inactive without storage permission",Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }


    private void makeDisplay()
    {

        // once we allowed to access memory all the buttons will enabled...
        // the buttons are in active until we have permission to view storage
        changeImage.setActivated(true);
        changeQuizName.setActivated(true);
        changeQuizDescription.setActivated(true);
        viewQuizQuestions.setActivated(true);
        addQuestion.setActivated(true);

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
        quizName.setText(thisQuiz.getName());

    }

    private void createQuestion()
    {
        CreateQuestionDialogBox alert = new CreateQuestionDialogBox(thisQuiz.getId(), getApplicationContext());
        alert.showDialog(this, "New Question");
    }

    private void selectNewImage()
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
            Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, RESULT_LOAD_IMAGE);
        }


    }

    // Now that the user is back from choosing an image
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            // String picturePath contains the path of selected Image
            quizImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            thisQuiz.setQuizPictureFileName(picturePath);
            DatabaseHelper db = new DatabaseHelper(getApplicationContext());
            db.updateQuiz(thisQuiz);
            Toast.makeText(getApplicationContext(),"New Image added", Toast.LENGTH_LONG).show();
        }

    }


}
