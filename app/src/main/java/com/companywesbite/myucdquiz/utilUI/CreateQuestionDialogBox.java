package com.companywesbite.myucdquiz.utilUI;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.companywesbite.myucdquiz.R;
import com.companywesbite.myucdquiz.questionClasses.question;
import com.companywesbite.myucdquiz.questionClasses.quiz;
import com.companywesbite.myucdquiz.utils.DatabaseHelper;

public class CreateQuestionDialogBox {

    private long quizId;
    private quiz thisQuiz;
    private Context context;


    private static final int MY_PERMISSIONS_REQUEST_GET_IMAGE = 1000;
    private static final int RESULT_LOAD_IMAGE = 1;

    public String questionImage = "default";
    public Button addImage;

    private  Activity activity;


    public CreateQuestionDialogBox(long quizId, Context context)
    {
         this.quizId = quizId;
         this.context = context;
    }

    public void showDialog(Activity activity, String msg){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.new_question);
        this.activity = activity;

        // UI elements
        final EditText questionName = (EditText) dialog.findViewById(R.id.questionTitle);
        final EditText questionDescription = (EditText) dialog.findViewById(R.id.questionDescription);
        final EditText questionAnswer = (EditText) dialog.findViewById(R.id.questionAnswer);
        Button cancel = (Button) dialog.findViewById(R.id.cancelCreation);
        Button add = (Button) dialog.findViewById(R.id.createQuestion);
         addImage = (Button) dialog.findViewById(R.id.addImage);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               boolean state = createQuestion(questionName.getText().toString(),questionDescription.getText().toString(),questionAnswer.getText().toString());
              if(state)
              {
                  dialog.dismiss();
              }
            }
        });



        /*
        TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        text.setText(msg);

        Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        */

        dialog.show();

    }

    // now let the user choose his own image...
    // we have to ask for permissions
    private void chooseImage()
    {
        if (ContextCompat.checkSelfPermission(this.context, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Permission is not granted
            // Should we show an explanation?

            ActivityCompat.requestPermissions((Activity) this.context,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_GET_IMAGE);
        }else
        {
            // we are good to go
            Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            this.activity.startActivityForResult(i,RESULT_LOAD_IMAGE);


        }
    }

    // Does the user allow us to check for images


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



    private boolean createQuestion(String questionName, String questionDescription, String questionAnswer)
    {
        if(questionName.trim().length() < 5)
        {
            Toast.makeText(this.context,"Question title too short!",Toast.LENGTH_LONG).show();
            return false;
        }
        if(questionDescription.trim().length() < 10)
        {
            Toast.makeText(this.context,"Question description too short!",Toast.LENGTH_LONG).show();
            return false;
        }
        if(questionAnswer.trim().length() < 5)
        {
            Toast.makeText(this.context,"Question answer too short!",Toast.LENGTH_LONG).show();
            return false;
        }
        question question1 = new question(questionName,questionDescription,questionAnswer,this.questionImage);
        DatabaseHelper db = new DatabaseHelper(this.context);
        question1.setId(db.createQuestion(question1, quizId));
        this.thisQuiz = db.getQuiz(quizId);
        this.thisQuiz.addQuestion(question1);
        db.updateQuiz(thisQuiz);
        Toast.makeText(this.context,"New Question Added",Toast.LENGTH_LONG).show();
        return true;
    }

}
