package com.companywesbite.myucdquiz.utilUI;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.companywesbite.myucdquiz.R;
import com.companywesbite.myucdquiz.SmartAnswerAnalysisEngine;
import com.companywesbite.myucdquiz.questionClasses.question;
import com.companywesbite.myucdquiz.utils.DatabaseHelper;
import com.companywesbite.myucdquiz.utils.ShakeListener;


public class AnswerQuestionDialogBox {

    private long questionId;
    private question thisQuestion;
    private Context context;

    private ShakeListener mShaker;


    public AnswerQuestionDialogBox(question question, Context context)
    {
        this.thisQuestion = question;
        this.questionId = question.getId();
        this.context = context;
    }

    public void showDialog(Activity activity){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.answer_question);


        final TextView questionDescription = (TextView) dialog.findViewById(R.id.questionDescription);
        final EditText questionAnswer = (EditText) dialog.findViewById(R.id.questionAnswer);
        final Button closeButton = (Button) dialog.findViewById(R.id.closeButton);
        final TextView currScore = (TextView) dialog.findViewById(R.id.currScore);


        questionDescription.setText(thisQuestion.getDescription());
        currScore.setText(""+thisQuestion.getCurrScore());

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        mShaker = new ShakeListener(this.context);
        mShaker.setOnShakeListener(new ShakeListener.OnShakeListener () {
            public void onShake()
            {

               String userAnswer =  questionAnswer.getText().toString().trim();

                Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

               if(userAnswer.length() < 1)
               {

                   // Vibrate for 500 milliseconds
                   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                       v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                   } else {
                       //deprecated in API 26
                       v.vibrate(500);
                   }

                   Toast.makeText(context,"Answer too short",Toast.LENGTH_LONG).show();
                   return;
               }


                double score = Math.round(answerQuestion(userAnswer) * 100.0) / 100.0;
                thisQuestion.setCurrScore(score*100);
                DatabaseHelper db = new DatabaseHelper(context);
                db.updateQuestion(thisQuestion);
                currScore.setText(score*100+"");
            }
        });


        dialog.show();
    }


    private double answerQuestion(String userAnswer)
    {
        return SmartAnswerAnalysisEngine.calcScore(thisQuestion.getAnswer(),userAnswer);
    }
}

