package com.companywesbite.myucdquiz.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.companywesbite.myucdquiz.R;
import com.companywesbite.myucdquiz.questionClasses.quiz;

import java.util.ArrayList;
import java.util.List;


/***
 *
 * Team: Flashcards Pro
 * Date: 2018-11-30
 * Name: QuizListViewAdapter
 * Functionality: This is a class is the one responsible for displaying the quiz list in the first
 *                page of the application. It displays every quiz name and how much of it is done
 *                It also helps the user understand the progress of each quiz using a progressbar UI
 *
 *
 *
 */


public class QuizListViewAdapter extends ArrayAdapter<quiz> {

    int groupid;
    List<quiz> item_list;
    ArrayList<String> desc;
    Context context;

    ProgressBar progressBar;
    int quizProgress;

    public QuizListViewAdapter(Context context, int vg, int id, List<quiz> item_list){
        super(context, vg, id, item_list);
        this.context=context;
        groupid=vg;
        this.item_list=item_list;
    }

    // Hold views of the ListView to improve its scrolling performance
    static class ViewHolder {
        public TextView textview;
        public TextView description;
        public Button button;
        public ImageView imageView;
        public ProgressBar progressBar;
    }

    public View getView(final int position, View convertView, final ViewGroup parent) {

        View rowView = convertView;
        // Inflate the list_item.xml file if convertView is null
        if(rowView==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView= inflater.inflate(groupid, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.textview= (TextView) rowView.findViewById(R.id.quizName);
            viewHolder.imageView = (ImageView) rowView.findViewById(R.id.quizImage);
            viewHolder.progressBar = (ProgressBar) rowView.findViewById(R.id.quizProgressBar);
            viewHolder.button= (Button) rowView.findViewById(R.id.openQuizButton);
            viewHolder.description = (TextView) rowView.findViewById(R.id.quizDescription);
            rowView.setTag(viewHolder);
        }
        // Set text to each TextView of ListView item
        ViewHolder holder = (ViewHolder) rowView.getTag();
        holder.textview.setText(item_list.get(position).getName());

        quizProgress = (int)(100*item_list.get(position).getPercentCorrect());
        // Set text of description

        holder.description.setText(item_list.get(position).questionsLeft() + " questions left");

        // PROGRESS BAR
        holder.progressBar.setProgress(quizProgress);

        // holder.imageView.setImageResource(R.drawable.defaultquizimage);

        /*
        holder.button.setText("OPEN");
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ListView) parent).performItemClick(v, position, 0);
            }
        });
        */

        return rowView;
    }

}
