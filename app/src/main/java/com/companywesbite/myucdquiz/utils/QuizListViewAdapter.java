package com.companywesbite.myucdquiz.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.companywesbite.myucdquiz.R;
import com.companywesbite.myucdquiz.questionClasses.quiz;

import java.util.ArrayList;
import java.util.List;

public class QuizListViewAdapter extends ArrayAdapter<quiz> {

    int groupid;
    List<quiz> item_list;
    ArrayList<String> desc;
    Context context;

    ProgressBar progressBar;

    public QuizListViewAdapter(Context context, int vg, int id, List<quiz> item_list){
        super(context, vg, id, item_list);
        this.context=context;
        groupid=vg;
        this.item_list=item_list;
    }

    // Hold views of the ListView to improve its scrolling performance
    static class ViewHolder {
        public TextView textview;
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
            rowView.setTag(viewHolder);
        }
        // Set text to each TextView of ListView item
        ViewHolder holder = (ViewHolder) rowView.getTag();
        holder.textview.setText(item_list.get(position).getName());
        // for now we just this as the image but later we will display the image the user chose
      /*  if(item_list.get(position).getQuizPictureFileName().equals("default"))
        {
            holder.imageView.setImageResource(R.drawable.defaultquizimage);
        } else
        {
            holder.imageView.setImageBitmap(BitmapFactory.decodeFile(item_list.get(position).getQuizPictureFileName()));
        }
        */


        // PROGRESS BAR
        int quizProgress = (int) item_list.get(position).getPercentCorrect();
        holder.progressBar.setProgress(quizProgress);

        // holder.imageView.setImageResource(R.drawable.defaultquizimage);
        holder.button.setText("OPEN");
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ListView) parent).performItemClick(v, position, 0);
            }
        });


        return rowView;
    }

}
