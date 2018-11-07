package com.companywesbite.myucdquiz.questionClasses;

public class note {

    // What defines a question
    private int id; // every question is unique.
    private String title;
    private String notes;


    public note(int id, String title, String notes)
    {
        this.id = id;
        this.title = title;
        this.notes = notes;

    }

    public note(String title, String notes)
    {

        this.title = title;
        this.notes = notes;

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

    public String getNotes() {
        return this.notes;
    }

    public void setNotes(String description) {
        this.notes = description;
    }

}
