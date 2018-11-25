package com.companywesbite.myucdquiz.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.companywesbite.myucdquiz.R;
import com.companywesbite.myucdquiz.questionClasses.question;
import com.companywesbite.myucdquiz.utilUI.AnswerQuestionDialogBox;

import java.util.List;

public class ONGOINGQuestionListViewAdapter extends BaseAdapter {

    // Declare Variables

    Context mContext;
    LayoutInflater inflater;
    private List<question> arraylist;
    Activity activity;
    private  AnswerQuestionDialogBox dialogBox;

    public ONGOINGQuestionListViewAdapter(Context context,Activity activity, List<question> arraylist) {
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = arraylist;
        this.activity = activity;
    }

    public class ViewHolder {
        TextView name;
        Button answerButton;
        TextView score;
    }

    @Override
    public int getCount() {
        return arraylist.size();
    }

    @Override
    public question getItem(int position) {
        return arraylist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.ongoing_question_item, null);
            // Locate the TextViews in listview_item.xml
            holder.name = (TextView) view.findViewById(R.id.questionName);
            holder.answerButton = (Button) view.findViewById(R.id.answerButton);
            holder.score = (TextView) view.findViewById(R.id.questionScore);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.name.setText("Q"+Integer.toString(position)+". "+arraylist.get(position).getTitle());
        holder.score.setText(arraylist.get(position).getCurrScore()+"");

        holder.answerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              dialogBox = new AnswerQuestionDialogBox(arraylist.get(position),arraylist,position,mContext);
              dialogBox.showDialog(activity);
            }
        });

        return view;
    }


}