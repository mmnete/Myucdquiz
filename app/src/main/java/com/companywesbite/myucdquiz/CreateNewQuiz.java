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

import com.companywesbite.myucdquiz.questionClasses.question;
import com.companywesbite.myucdquiz.questionClasses.quiz;
import com.companywesbite.myucdquiz.utils.DatabaseHelper;

import java.util.HashMap;
import java.util.Map;

public class CreateNewQuiz extends AppCompatActivity {


    private ImageView quizImage;
    private EditText quizName;
    private EditText quizDescription;
    private String quizImageName = "default";
    private Button submitButton;
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int MY_PERMISSIONS_REQUEST_GET_IMAGE = 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_quiz);

        // to display back button
        getSupportActionBar().setTitle("New Quiz");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        quizImage = (ImageView) findViewById(R.id.quizImageChooser);
        quizName = (EditText) findViewById(R.id.quizName);
        quizDescription = (EditText) findViewById(R.id.quizDescription);
        submitButton = (Button) findViewById(R.id.createQuizButton);

        // set the default value of the quizImage
        quizImage.setImageResource(R.drawable.defaultquizimage);

        // when the user wants to choose an image
        quizImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
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
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(myIntent);
        finish();
        return true;
    }

    // now let the user choose his own image...
    // we have to ask for permissions
    private void chooseImage()
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
                    chooseImage();
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
            quizImageName = picturePath;
        }
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
            Map<Integer, question> newQuestions = new HashMap<>();
            quiz newQuiz = new quiz(quizName.getText().toString(),quizDescription.getText().toString(),quizImageName,newQuestions);
            DatabaseHelper db = new DatabaseHelper(this);
            db.createQuiz(newQuiz);
            Toast.makeText(this,"Quiz Created Successfully!",Toast.LENGTH_LONG).show();
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }

}
