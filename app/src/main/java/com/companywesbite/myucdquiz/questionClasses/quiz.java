package com.companywesbite.myucdquiz.questionClasses;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/***
 *
 * Team: Flashcards Pro
 * Date: 2018-11-01
 * Name: quiz
 * Functionality: This is a class that is a blue print of what a quiz is.
 *                A quiz is just a collection of questions.
 *                It contains all the getters and setters of what a quiz is in our project.
 *                What should a quiz have?
 *                A quiz should contain an id, title, description, a map of id and questions
 *                for fast access to questions, number of questions, error tolerance - this enables
 *                us to grade questions by knowing how similar an answer provided should be to the true
 *                answer of all the questions in a particular quiz.
 *
 *                The quiz class also contains a method to construct it to and from a json string so
 *                that we can easily create new objects when a quiz is being shared or imported
 *
 */



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

    // Constructor when the quiz is imported or shared
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

    // returns the ratio of questions that are correct...
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

    public int questionsLeft(){
        int out = 0;
        List<question> currQuestions = getQuestions();
        for(int i = 0; i < currQuestions.size(); i++)
        {
            if(currQuestions.get(i).isCorrect())
            {
                out++;
            }
        }

        return currQuestions.size()-out;
    }


    // used in the import or export of a quiz to share.
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
