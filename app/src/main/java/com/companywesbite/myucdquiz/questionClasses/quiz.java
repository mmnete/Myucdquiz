package com.companywesbite.myucdquiz.questionClasses;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class quiz {

    private long id;
    private String name;
    private String description;

    private Map<Long, question> questions; // We use a map so that we can allow fast access
    private int numberOfQuestions = 0;
    private int grade = 0;
    private int errorTolerance = 0;
    public boolean isNull = false;

    public quiz(String name, String description, Map<Long, question> questions, int errorTolerance)
    {
        this.name = name;
        this.description = description;
        this.questions = questions;
        this.errorTolerance = errorTolerance;
    }

    public quiz(String jsonString)
    {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            this.name = jsonObject.getString("name");
            this.description = jsonObject.getString("description");
            this.errorTolerance = Integer.valueOf(jsonObject.getString("errorTolerance"));
            this.questions = new HashMap<>();
        } catch (JSONException e) {
            e.printStackTrace();
            isNull = true;
        }
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // This will have to interract with the database class to propagate changes.
    public void addQuestion(question newQuestion)
    {
        this.numberOfQuestions++;
        this.questions.put(newQuestion.getId(), newQuestion);
    }

    // This will have to interract with the database class to propagate changes.
    public void updateQuestion(question newQuestion)
    {
        this.questions.put(newQuestion.getId(), newQuestion);
    }

    // This will have to interact with the database class to propagate changes.
    public void deleteQuestion()
    {
        this.numberOfQuestions--;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getQuestionNumber() {return numberOfQuestions;}

    public void setQuestionNumber(int questionNumber) {this.numberOfQuestions=questionNumber;}


    public List<question> getQuestions() {
        List<question> tempQuestionCollection = new ArrayList<>();
        // Looping through the map of questions
        for(Map.Entry<Long, question> entry : this.questions.entrySet()){
            tempQuestionCollection.add(entry.getValue());
        }
        return tempQuestionCollection;
    }

    public int getErrorTolerance() {
        return errorTolerance;
    }

    public void setErrorTolerance(int errorTolerance) {
        this.errorTolerance = errorTolerance;
    }

    // returns how many questions are correct...
    public double getPercentCorrect()
    {
        double out = 0;
        List<question> currQuestions = getQuestions();
        for(int i = 0; i < currQuestions.size(); i++)
        {
            if(currQuestions.get(i).isCorrect())
            {
                out++;
            }
        }
        return out/currQuestions.size();
    }

    public String toJSON(){

        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put("name", this.name);
            jsonObject.put("description", this.description);
            jsonObject.put("numberOfQuestions", Integer.toString(this.numberOfQuestions));
            jsonObject.put("errorTolerance", Integer.toString(this.getErrorTolerance()));

            JSONArray questionArray = new JSONArray();
            List<question> questions = this.getQuestions();
            for(int i = 0; i < questions.size(); i++) {
                // 1st object
                JSONObject questionJson = questions.get(i).toJSON();
                questionArray.put(questionJson);
            }

            jsonObject.put("questions",questionArray);

            return jsonObject.toString();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }

    }


}
