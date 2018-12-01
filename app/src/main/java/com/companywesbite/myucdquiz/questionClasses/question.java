package com.companywesbite.myucdquiz.questionClasses;

import org.json.JSONException;
import org.json.JSONObject;


/***
 *
 * Team: Flashcards Pro
 * Date: 2018-11-01
 * Name: question
 * Functionality: This is a class that is a blue print of what a question is.
 *                It contains all the getters and setters of what a question is in our project.
 *                What should a question have?
 *                A question should contain an id, title [optionally used], description, answer
 *                isAnswered function, isCorrect function, a path to a picture [optionally used].
 *
 *                The question also contains a method to construct it to and from a json string so
 *                that we can easily create new objects when a quiz [a question collection] is being shared
 *
 */


public class question {

    // What defines a question
    private long id; // every question is unique.
    private String title;
    private String description;
    private String answer;
    private Integer answered; // Says whether the question has been answered correctly at least one time
    private Integer correct;
    private String picturePath;

    public double currScore = 0.0;

    public question(long id, String title, String description, String answer, String picturePath)
    {
        this.id = id;
        this.title = title;
        this.description = description;
        this.answer = answer;
        this.answered = 0;
        this.correct = 0;
        this.picturePath = picturePath;
    }


    public question(String title, String description, String answer, String picturePath)
    {
        this.id = 10;
        this.title = title;
        this.description = description;
        this.answer = answer;
        this.answered = 0;
        this.correct = 0;
        this.picturePath = picturePath;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setAnswered(Integer answered) {
        this.answered = answered;
    }

    public Integer getAnswered() {
        return answered;
    }

    public void setCorrect(Integer correct) {
        this.correct = correct;
    }

    public Integer getCorrect() {
        return correct;
    }

    public double getCurrScore() {
        return currScore;
    }

    public void setCurrScore(double currScore) {
        this.currScore = currScore;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public boolean isCorrect()
    {
        return getCorrect() == 1;
    }

    public boolean isAnswered()
    {
        return  getAnswered() == 1;
    }


    // Used when we want to share or recieve a quiz [ question collection ] through email or import
    public JSONObject toJSON(){

        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put("title", this.title);
            jsonObject.put("description", this.description);
            jsonObject.put("answer", this.answer);

            return jsonObject;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

    }
}
