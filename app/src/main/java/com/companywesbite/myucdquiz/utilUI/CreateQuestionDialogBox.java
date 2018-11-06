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

public class CreateQuestionDialogBox {

    private long quizId;
    private Context context;

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

        // UI elements
        final EditText questionName = (EditText) dialog.findViewById(R.id.questionTitle);
        final EditText questionDescription = (EditText) dialog.findViewById(R.id.questionDescription);
        final EditText questionAnswer = (EditText) dialog.findViewById(R.id.questionAnswer);
        Button cancel = (Button) dialog.findViewById(R.id.cancelCreation);
        Button add = (Button) dialog.findViewById(R.id.createQuestion);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
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
        question question1 = new question(questionName,questionDescription,questionAnswer);
        DatabaseHelper db = new DatabaseHelper(this.context);
        question1.setId((int) db.createQuestion(question1, (int)quizId));
        Toast.makeText(this.context,"New Question Added",Toast.LENGTH_LONG).show();
        return true;
    }
}
