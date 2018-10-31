package com.companywesbite.myucdquiz.questionClasses;

public class question {

    // What defines a question
    private int id; // every question is unique.
    private String title;
    private String description;
    private String answer;
    private Integer answered;
    private Integer correct;

    public question(int id, String title, String description, String answer)
    {
        this.id = id;
        this.title = title;
        this.description = description;
        this.answer = answer;
        this.answered = 0;
        this.correct = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
}
