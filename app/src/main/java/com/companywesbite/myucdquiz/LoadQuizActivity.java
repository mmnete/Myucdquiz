package com.companywesbite.myucdquiz;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.companywesbite.myucdquiz.questionClasses.question;
import com.companywesbite.myucdquiz.questionClasses.quiz;
import com.companywesbite.myucdquiz.utils.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LoadQuizActivity extends AppCompatActivity {



    private TextView status;
    private Button home;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_quiz);

        status = (TextView) findViewById(R.id.status);
        home = (Button) findViewById(R.id.homeButton);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
                finish();
            }
        });


        Uri data = getIntent().getData();
        if (data != null) {
            getIntent().setData(null);
            try {
                importData(data);
            } catch (Exception e) {
                // warn user about bad data here
                finish();
                return;
            }

            // launch home Activity (with FLAG_ACTIVITY_CLEAR_TOP) here…
        } else
        {
            home.performClick();
        }
    }

        private void importData(Uri data) {
            final String scheme = data.getScheme();

            if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
                try {
                    ContentResolver cr = getContentResolver();
                    InputStream is = cr.openInputStream(data);
                    if (is == null) return;

                    StringBuffer buf = new StringBuffer();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    String str;
                    if (is != null) {
                        while ((str = reader.readLine()) != null) {
                            buf.append(str + "\n");
                        }
                    }
                    is.close();


                    String jsonString = buf.toString();
                    JSONObject jsonObject = new JSONObject(jsonString);
                    // perform your data import here…
                    quiz newQuiz = new quiz(jsonString);

                    // time to save the questions
                    if(!newQuiz.isNull)
                    {
                        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
                        newQuiz.setId(db.createQuiz(newQuiz));
                        JSONArray questionJSON = jsonObject.getJSONArray("questions");
                        for (int i = 0; i < questionJSON.length(); i++) {
                            JSONObject obj = questionJSON.getJSONObject(i);
                            Log.d("TAG",Long.toString(newQuiz.getId()));
                            String title = obj.getString("title");
                            String description = obj.getString("description");
                            String answer = obj.getString("answer");
                            question temp = new question(title,description,answer,"default");
                            temp.setId(db.createQuestion(temp, newQuiz.getId()));
                            newQuiz = db.getQuiz(newQuiz.getId());
                            newQuiz.addQuestion(temp);
                            db.updateQuiz(newQuiz);
                        }
                        status.setText("QUIZ RECIEVED :-)");
                    } else
                    {
                        status.setText("COULDNT LOAD FILE :-(");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


}
