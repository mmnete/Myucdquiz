package com.companywesbite.myucdquiz.utilUI;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.companywesbite.myucdquiz.R;
import com.companywesbite.myucdquiz.questionClasses.question;
import com.companywesbite.myucdquiz.utils.DatabaseHelper;


/***
 *
 *
 * No Longer used....
 * The reason is because the user can simply create a new question instead
 * And it will take the same duration as filling this form
 *
 * Team: Flashcards Pro
 * Date: 2018-11-30
 * Name: EditQuestionDialogBox
 * Functionality: This is our own custom dialog box that pops up as an input when the user wants to make changes
 *                 to a specific question in a quiz...
 *
 *
 *
 *
 */



public class EditQuestionDialogBox {

    private long questionId;
    private question thisQuestion;
    private Context context;



    public EditQuestionDialogBox(long questionId, Context context)
    {
        this.questionId = questionId;
        this.context = context;
        DatabaseHelper db = new DatabaseHelper(this.context);
        this.thisQuestion = db.getQuestion(this.questionId);
    }

    public void showDialog(final Activity activity, String msg){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.edit_question);


        // UI elements
      //  final EditText questionName = (EditText) dialog.findViewById(R.id.questionTitle);
        final EditText questionDescription = (EditText) dialog.findViewById(R.id.questionDescription);
        final EditText questionAnswer = (EditText) dialog.findViewById(R.id.questionAnswer);
        Button cancel = (Button) dialog.findViewById(R.id.cancelCreation);
        Button saveChanges = (Button) dialog.findViewById(R.id.saveChanges);

       // questionName.setText(thisQuestion.getTitle());
        questionDescription.setText(thisQuestion.getDescription());
        questionAnswer.setText(thisQuestion.getAnswer());

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean state = updateQuestion(questionDescription.getText().toString(),questionAnswer.getText().toString());
                if(state)
                {
                    dialog.dismiss();
                }
            }
        });



        dialog.show();

    }




    private boolean updateQuestion(String questionDescription, String questionAnswer)
    {

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

        thisQuestion.setDescription(questionDescription);
        thisQuestion.setAnswer(questionAnswer);

        DatabaseHelper db = new DatabaseHelper(this.context);
        db.updateQuestion(thisQuestion);
        Toast.makeText(this.context,"Update Made",Toast.LENGTH_LONG).show();
        return true;
    }
}

