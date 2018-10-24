package com.companywesbite.myucdquiz.questionClasses;

public class question {

    // What defines a question
    private int id; // every question is unique.
    private String title;
    private String description;
    private String imageName;
    private String answer;
    private boolean answered;
    private boolean correct;

    public question(int id, String title, String description, String imageName, String answer)
    {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imageName = imageName;
        this.answer = answer;
        this.answered = false;
        this.correct = false;
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

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isAnswered() {
        return answered;
    }

    public void setAnswered(boolean answered) {
        this.answered = answered;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }
}
