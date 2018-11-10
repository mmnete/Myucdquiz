package com.companywesbite.myucdquiz;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.companywesbite.myucdquiz.questionClasses.question;

@SuppressLint("ValidFragment")
public class SlidingQuestionPageFragment extends Fragment {

    private question thisQuestion;


    @SuppressLint("ValidFragment")
    public SlidingQuestionPageFragment(question question)
    {
        this.thisQuestion = question;


    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.sliding_question_item, container, false);

        TextView questionDescription = (TextView) view.findViewById(R.id.questionDescription);
        final TextView answeredGrade = (TextView) view.findViewById(R.id.answered);
        questionDescription.setText(thisQuestion.getDescription());

        final EditText userAnswer = (EditText) view.findViewById(R.id.userAnswer);



        ((OngoingQuizActivity)getActivity()).updateApi(new UpdateFrag() {
            @Override
            public void updatefrag() {
                String currentAnswer = userAnswer.getText().toString().trim();
                if(currentAnswer.length() == 0)
                {
                    Toast.makeText(getContext(),"Please Enter Answer",Toast.LENGTH_LONG).show();
                }
                double score = submitAnswer(currentAnswer);
                answeredGrade.setText("SCORE: "+score);
                Log.d("TAG","Hello World");
            }
        });






        return view;
    }


    private double submitAnswer(String userAnswer)
    {

        return SmartAnswerAnalysisEngine.calcDistance(userAnswer,thisQuestion.getAnswer());
    }


}