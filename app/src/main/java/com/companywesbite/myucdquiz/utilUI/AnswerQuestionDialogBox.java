package com.companywesbite.myucdquiz.utilUI;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.companywesbite.myucdquiz.QuestionImageViewer;
import com.companywesbite.myucdquiz.R;
import com.companywesbite.myucdquiz.SmartAnswerAnalysisEngine;
import com.companywesbite.myucdquiz.questionClasses.question;
import com.companywesbite.myucdquiz.questionClasses.quiz;
import com.companywesbite.myucdquiz.utils.DatabaseHelper;
import com.companywesbite.myucdquiz.utils.ShakeListener;

import java.util.List;


public class AnswerQuestionDialogBox {

    private long questionId;
    private question thisQuestion;
    private Context context;
    private int pos;
    private List<question> col;

    private ShakeListener mShaker;
    private boolean displaying = false;

    private quiz thisQuiz;


    public AnswerQuestionDialogBox(question question, List<question> col, int pos, Context context, quiz thisQuiz)
    {
        this.thisQuestion = question;
        this.questionId = question.getId();
        this.context = context;
        this.pos = pos;
        this.col = col;
        this.thisQuiz = thisQuiz;
    }

    public void showDialog(Activity activity){
        displaying = true;
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.answer_question);


        final TextView questionDescription = (TextView) dialog.findViewById(R.id.questionDescription);
        final EditText questionAnswer = (EditText) dialog.findViewById(R.id.questionAnswer);
        final Button closeButton = (Button) dialog.findViewById(R.id.closeButton);
        final Button nextButton = (Button) dialog.findViewById(R.id.nextButton);
        final Button prevButton = (Button) dialog.findViewById(R.id.prevButton);
        final Button imageButton = (Button) dialog.findViewById(R.id.imageButton);
        final TextView currScore = (TextView) dialog.findViewById(R.id.currScore);


        imageButton.setEnabled(false);
        if(!thisQuestion.getPicturePath().equals("default"))
        {
            imageButton.setText("VIEW IMAGE");
            imageButton.setEnabled(true);
        }

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, QuestionImageViewer.class);
                i.putExtra("questionId", thisQuestion.getId());
                context.startActivity(i);
            }
        });

        questionDescription.setText(thisQuestion.getDescription());
        if(thisQuestion.isCorrect())
        {
            currScore.setText("ACCEPTED PREVIOUS ANSWER");
        } else
        {
            currScore.setText("REJECTED PREVIOUS ANSWER");
        }

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
                dialog.cancel();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos = pos + 1;
                if(pos == col.size())
                {
                    pos--;
                }
                thisQuestion = col.get(pos);
                questionDescription.setText(thisQuestion.getDescription());
                if(thisQuestion.isCorrect())
                {
                    currScore.setText("ACCEPTED PREVIOUS ANSWER");
                } else
                {
                    currScore.setText("REJECTED PREVIOUS ANSWER");
                }
            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos = pos - 1;
                if(pos == -1)
                {
                    pos++;
                }
                thisQuestion = col.get(pos);
                questionDescription.setText(thisQuestion.getDescription());
                if(thisQuestion.isCorrect())
                {
                    currScore.setText("ACCEPTED PREVIOUS ANSWER");
                } else
                {
                    currScore.setText("REJECTED PREVIOUS ANSWER");
                }

            }
        });

        mShaker = new ShakeListener(this.context);
        mShaker.setOnShakeListener(new ShakeListener.OnShakeListener () {
            public void onShake()
            {

                if(!displaying)
                {
                    return;
                }

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
                if (100 - thisQuestion.getCurrScore() < thisQuiz.getErrorTolerance()) {
                    //Question is accepted
                    thisQuestion.setCorrect(1);
                    currScore.setText("ACCEPTED");
                } else {
                    //Question is not accepted
                    thisQuestion.setCorrect(0);
                    currScore.setText("REJECTED");
                }
                DatabaseHelper db = new DatabaseHelper(context);
                db.updateQuestion(thisQuestion);


            }
        });


        dialog.show();
    }

    public void close()
    {
        displaying = false;
    }


    private double answerQuestion(String userAnswer)
    {
        return SmartAnswerAnalysisEngine.calcScore(thisQuestion.getAnswer(),userAnswer);
    }
}

