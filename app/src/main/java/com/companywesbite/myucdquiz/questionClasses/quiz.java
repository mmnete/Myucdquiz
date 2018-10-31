package com.companywesbite.myucdquiz.questionClasses;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class quiz {

    private int id;
    private String name;
    private String description;
    private String quizPictureFileName;
    private Map<Integer, question> questions; // We use a map so that we can allow fast access
    public int numberOfQuestions = 0;
    private int grade = 0;

    public quiz(String name, String description, String quizPictureFileName, Map<Integer, question> questions)
    {
        this.id = id;
        this.name = name;
        this.description = description;
        this.quizPictureFileName = quizPictureFileName;
        this.questions = questions;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
    public void deleteQuestion(int id)
    {
        this.numberOfQuestions--;
        this.questions.remove(id);
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getQuizPictureFileName() {
        return quizPictureFileName;
    }

    public void setQuizPictureFileName(String quizPictureFileName) {
        this.quizPictureFileName = quizPictureFileName;
    }

    public List<question> getQuestions() {
        List<question> tempQuestionCollection = new ArrayList<>();
        // Looping through the map of questions
        for(Map.Entry<Integer, question> entry : this.questions.entrySet()){
            tempQuestionCollection.add(entry.getValue());
        }
        return tempQuestionCollection;
    }
}
