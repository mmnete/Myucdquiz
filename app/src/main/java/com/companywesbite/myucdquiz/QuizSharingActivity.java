package com.companywesbite.myucdquiz;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class QuizSharingActivity extends AppCompatActivity {


    String infoToShare = "";


    private static final int MY_PERMISSIONS_REQUEST_WRITE = 2000;

    private Button shareButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bliuetooth_sharing);


        // get the information about this quiz
        Intent i = getIntent();

        infoToShare = i.getStringExtra("quizJSON");


        getSupportActionBar().setTitle("SHARING PORTAL");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        shareButton = (Button) findViewById(R.id.shareButton);


        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFile();
            }
        });


    }

    @Override
    protected void onResume() {
        checkPermission();
        super.onResume();
    }

    // Ask for permission to read images unless close the application
    private void checkPermission()
    {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Permission is not granted
            // Should we show an explanation?

            Toast.makeText(this,"We need to write files, we cant share without that",Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(QuizSharingActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_WRITE);
        } else
        {
            // we are good to go
            shareButton.setEnabled(true);

        }
    }

    // Does the user allow us to check for images
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    shareButton.setEnabled(true);

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




    private void sendFile()
    {
        File fileToSend = createFileWithContent(infoToShare);

        sendIntentToGmailApp(fileToSend);
    }

    private void sendIntentToGmailApp(File fileToSend) {
        if(fileToSend != null){
            Uri screenshot = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext()
                            .getPackageName() + ".provider", fileToSend);

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");

            intent.putExtra(Intent.EXTRA_SUBJECT, "A NEW QUIZ FILE ");
            intent.putExtra(Intent.EXTRA_TEXT, "Note for the reciever: Please make sure you do not have a default app to open" +
                    " attachments so that you can select FlashcardPro to open this file when you download it. Thank you for beliveing in "+
                  "out product. \n\nFlashCardPro team.");
            if (screenshot != null) {
                intent.putExtra(Intent.EXTRA_STREAM, screenshot);
            }
            startActivity(Intent.createChooser(intent, "Send quiz..."));
        }
    }

    private File createFileWithContent(String content) {
        FileWriter writer;
        if(TextUtils.isEmpty(content)){
            content = "Someone shared a quiz file with you. Click it to load it up";
        }
        File file = null;
        try{
            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "FLASHCARDSPRO"+(new Date()).toString()+".txt");
            writer = new FileWriter(file);
            writer.write(content);
            writer.close();
            Toast.makeText(getBaseContext(), "Temporarily saved contents in " + file.getPath(), Toast.LENGTH_LONG).show();
        }catch(IOException e){
            Toast.makeText(getBaseContext(), "Unable create temp file. Check logcat for stackTrace", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        return file;
    }



    // when the user clicks the back button .. we cannot let the user come back here
    // This is for the back button
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(myIntent);
        finish();
        return true;
    }




}