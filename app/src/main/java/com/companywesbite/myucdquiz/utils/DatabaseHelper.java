package com.companywesbite.myucdquiz.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.companywesbite.myucdquiz.questionClasses.note;
import com.companywesbite.myucdquiz.questionClasses.question;
import com.companywesbite.myucdquiz.questionClasses.quiz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Android Group team.
 * This is the class that creates all the tables for our database.
 *
 * */


public class DatabaseHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 3;

    // Database Name
    private static final String DATABASE_NAME = "test.db"; //Test Database

    // Table Names
    private static final String TABLE_QUIZ = "TBquiz"; // This is a table containing all quizes
    private static final String TABLE_QUESTION = "TBquestion"; // This is a table for just questions
    private static final String TABLE_QUIZ_QUESTION = "TBquizquestion"; // How we know which quiz a question belongs to
    private static final String TABLE_NOTE = "TBnotes"; // How we know which quiz a question belongs to

    // Common column names. This column is in all the three tables.
    private static final String KEY_ID = "id";

    // QUIZ Table - column names.
    private static final String QUIZ_NAME = "quiz_name";
    private static final String QUIZ_DESCRIPTION = "quiz_description";
    private static final String QUIZ_NUM_OF_QUES = "quiz_num_of_ques";
    private static final String QUIZ_GRADE = "quiz_grade";
    private static final String QUIZ_TOLERANCE = "quiz_tolerance";

    // QUESTION Table - column names
    private static final String QUESTION_NAME = "question_name";
    private static final String QUESTION_DESCRIPTION = "question_description";
    private static final String QUESTION_ANSWER = "question_answer";
    private static final String QUESTION_ANSWERED = "question_answered";
    private static final String QUESTION_CORRECT = "question_correct";
    private static final String QUESTION_CURR_SCORE = "question_curr_score";
    private static final String QUESTION_IMAGE_DIRECTORY = "question_image_directory";

    // NOTES Table - column names
    private static final String NOTE_NAME = "note_name";
    private static final String NOTE_DESCRIPTION = "note_description";

    // TABLE_QUIZ_QUESTION Table - column names
    private static final String KEY_QUIZ_ID = "key_quiz_id";
    private static final String KEY_QUESTION_ID = "key_question_id";

    // Table Create Statements
    // Todo table create statement
    private static final String CREATE_TABLE_QUIZ = "CREATE TABLE "
            + TABLE_QUIZ + "(" + KEY_ID + " INTEGER PRIMARY KEY," + QUIZ_NAME
            + " TEXT," + QUIZ_DESCRIPTION + " TEXT," + QUIZ_NUM_OF_QUES + " INTEGER," + QUIZ_GRADE
            + " INTEGER," + QUIZ_TOLERANCE + " INTEGER" + ")";

    // Tag table create statement
    private static final String CREATE_TABLE_QUESTION = "CREATE TABLE " + TABLE_QUESTION
            + "(" + KEY_ID + " INTEGER PRIMARY KEY," + QUESTION_NAME
            + " TEXT," + QUESTION_DESCRIPTION + " TEXT," + QUESTION_ANSWER
            + " TEXT," + QUESTION_ANSWERED + " BOOLEAN," + QUESTION_CURR_SCORE + " DOUBLE," + QUESTION_CORRECT
            + " BOOLEAN," + QUESTION_IMAGE_DIRECTORY + " TEXT"  + ")";

    // todo_tag table create statement
    private static final String CREATE_TABLE_TODO_TAG = "CREATE TABLE "
            + TABLE_QUIZ_QUESTION + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_QUIZ_ID + " INTEGER," + KEY_QUESTION_ID + " INTEGER)";

    // note table create statement
    private static final String CREATE_TABLE_NOTE = "CREATE TABLE "
            + TABLE_NOTE + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + NOTE_NAME + " TEXT," + NOTE_DESCRIPTION + " TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_QUIZ);
        db.execSQL(CREATE_TABLE_QUESTION);
        db.execSQL(CREATE_TABLE_TODO_TAG);
        db.execSQL(CREATE_TABLE_NOTE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUIZ);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUIZ_QUESTION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTE);

        // create new tables
        onCreate(db);
    }

    /*
     * Creating a question
     */
    public long createQuestion(question question, long quiz_id) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(QUESTION_NAME, question.getTitle());
        values.put(QUESTION_DESCRIPTION, question.getDescription());
        values.put(QUESTION_ANSWER, question.getAnswer());
        values.put(QUESTION_ANSWERED, question.getAnswered());
        values.put(QUESTION_CORRECT, question.getCorrect());
        values.put(QUESTION_CURR_SCORE, question.getCurrScore());
        values.put(QUESTION_IMAGE_DIRECTORY, question.getPicturePath());

        Log.d("TAG","Added to database "+question.getPicturePath());
        // insert row
        long question_id = db.insert(TABLE_QUESTION, null, values);

        // Which quiz is the question assigned to
        createQuestionQuizRow(quiz_id, question_id);

        return question_id;
    }

    /*
    Creating new question to quiz relationship
     */
    public long createQuestionQuizRow(long quiz_id, long question_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_QUIZ_ID, quiz_id);
        values.put(KEY_QUESTION_ID, question_id);


        // insert row
        long quizquestion_id = db.insert(TABLE_QUIZ_QUESTION, null, values);


        return quizquestion_id;
    }

    /*
     * Deleting a question from specific quiz ... deleting the relationship
     */
    public void deleteQuestionFromQuiz(long question_id, long quiz_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_QUIZ_QUESTION, KEY_QUESTION_ID + " = ? AND " + KEY_QUIZ_ID + " = ?" ,
                new String[] { String.valueOf(question_id), String.valueOf(quiz_id) });
        quiz temp = getQuiz(quiz_id);
        temp.deleteQuestion();
        updateQuiz(temp);
    }

    /*
     * get single question
     */
    public question getQuestion(long question_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_QUESTION + " WHERE "
                + KEY_ID + " = " + question_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();



        long questionId = c.getLong(c.getColumnIndex(KEY_ID));
        String questionName = c.getString(c.getColumnIndex(QUESTION_NAME));
        String questionDescription = c.getString(c.getColumnIndex(QUESTION_DESCRIPTION));
        String questionAnswer = c.getString(c.getColumnIndex(QUESTION_ANSWER));
        Integer questionAnswered = c.getInt(c.getColumnIndex(QUESTION_ANSWERED));
        Integer questionCorrect = c.getInt(c.getColumnIndex(QUESTION_CORRECT));
        double questionCurrScore = c.getDouble(c.getColumnIndex(QUESTION_CURR_SCORE));
        String questionPicture = c.getString(c.getColumnIndex(QUESTION_IMAGE_DIRECTORY));
        // We need to create a map of questions here that are associated with this quiz....

        question newQuestion = new question(questionId,questionName,questionDescription,questionAnswer,questionPicture);
        newQuestion.setCurrScore(questionCurrScore);
        newQuestion.setAnswered(questionAnswered);
        newQuestion.setCorrect(questionCorrect);
        return newQuestion;
    }



    /*
     * Updating a question
     */
    public int updateQuestion(question question) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(QUESTION_NAME, question.getTitle());
        values.put(QUESTION_DESCRIPTION, question.getDescription());
        values.put(QUESTION_ANSWER, question.getAnswer());
        values.put(QUESTION_ANSWERED, question.getAnswered());
        values.put(QUESTION_CORRECT, question.getCorrect());
        values.put(QUESTION_CURR_SCORE, question.getCurrScore());
        values.put(QUESTION_IMAGE_DIRECTORY, question.getPicturePath());

        // updating row
        return db.update(TABLE_QUESTION, values, KEY_ID + " = ?",
                new String[] { String.valueOf(question.getId())});
    }

    /*
     * Deleting a question
     */
    public void deleteQuestion(long question_id, long quiz_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_QUESTION, KEY_ID + " = ?",
                new String[] { String.valueOf(question_id) });
        // delete the question and quiz relationship
        deleteQuestionFromQuiz(question_id, quiz_id);
    }

    /*
     * Creating a quiz
     */
    public long createQuiz(quiz quiz) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(QUIZ_NAME, quiz.getName());
        values.put(QUIZ_DESCRIPTION, quiz.getDescription());
        values.put(QUIZ_NUM_OF_QUES, quiz.getQuestionNumber());
        values.put(QUIZ_GRADE, quiz.getGrade());
        values.put(QUIZ_TOLERANCE, quiz.getErrorTolerance());


        // insert row
        long quiz_id = db.insert(TABLE_QUIZ, null, values);


        return quiz_id;
    }

    /*
     * get single quiz
     */
    public quiz getQuiz(long quiz_id) {

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_QUIZ + " WHERE "
                + KEY_ID + " = " + quiz_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();


        long quizId = (long) c.getInt(c.getColumnIndex(KEY_ID));
        String quizName = c.getString(c.getColumnIndex(QUIZ_NAME));
        String quizDescription = c.getString(c.getColumnIndex(QUIZ_DESCRIPTION));
        Integer quizNumOfQues = c.getInt(c.getColumnIndex(QUIZ_NUM_OF_QUES));
        Integer quizGrade = c.getInt(c.getColumnIndex(QUIZ_GRADE));
        Integer quizTolerane = c.getInt(c.getColumnIndex(QUIZ_TOLERANCE));


        // now we just need to get the list of questions in this quiz
        String selectQuestionQuery = "SELECT  * FROM " + TABLE_QUIZ_QUESTION + " WHERE "
                + KEY_QUIZ_ID + " = " + quiz_id;

        Log.e(LOG, selectQuery);

        Cursor c1 = db.rawQuery(selectQuestionQuery, null);

        // We need to create a map of questions here that are associated with this quiz....

        Map<Long, question> questionCollection = new HashMap<>();

        if (c1.moveToFirst()) {
            do {
                // get each question..
                long questionId = c1.getInt(c1.getColumnIndex(KEY_QUESTION_ID));
                questionCollection.put(questionId, getQuestion(questionId));
            } while (c1.moveToNext());
        }


        // now we just construct the quiz
        quiz newQuiz = new quiz(quizName,quizDescription,questionCollection,quizTolerane);
        newQuiz.setId(quizId);
        newQuiz.setGrade(quizGrade);
        newQuiz.setQuestionNumber(quizNumOfQues);

        return newQuiz;
    }

    /**
     * Fetching all the saved quizes
     * */
    public List<quiz> getAllQuizes() {
        List<quiz> quizes = new ArrayList<quiz>();
        String selectQuery = "SELECT  * FROM " + TABLE_QUIZ;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                long quizId = c.getInt(c.getColumnIndex(KEY_ID));
                quizes.add(getQuiz(quizId));
            } while (c.moveToNext());
        }

        return quizes;
    }




    /*
     * Updating a quiz
     */
    public int updateQuiz(quiz quiz) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(QUIZ_NAME, quiz.getName());
        values.put(QUIZ_DESCRIPTION, quiz.getDescription());
        values.put(QUIZ_NUM_OF_QUES, quiz.getQuestionNumber());
        values.put(QUIZ_GRADE, quiz.getGrade());
        values.put(QUIZ_TOLERANCE, quiz.getErrorTolerance());



        // updating row
        return db.update(TABLE_QUIZ, values, KEY_ID + " = ?",
                new String[] { String.valueOf(quiz.getId())});
    }

    /*
     * Deleting a question
     */
    public void deleteQuiz(long quiz_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_QUIZ, KEY_ID + " = ?",
                new String[] { String.valueOf(quiz_id) });
        // delete the question and quiz relationship
        db.delete(TABLE_QUIZ_QUESTION, KEY_QUIZ_ID + " = ?",
                new String[] { String.valueOf(quiz_id) });
    }


    /*
     * Creating a Note
     */
    public long createNote(note note) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NOTE_NAME, note.getTitle());
        values.put(NOTE_DESCRIPTION, note.getNotes());


        // insert row
        long note_id = db.insert(TABLE_NOTE, null, values);


        return note_id;
    }

    /*
     * get single note
     */
    public note getNote(long note_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_NOTE + " WHERE "
                + KEY_ID + " = " + note_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();



        Integer noteId = c.getInt(c.getColumnIndex(KEY_ID));
        String noteName = c.getString(c.getColumnIndex(NOTE_NAME));
        String noteDescription = c.getString(c.getColumnIndex(NOTE_DESCRIPTION));


        // We need to create a map of questions here that are associated with this quiz....

        note note = new note(noteId,noteName,noteDescription);
        return note;
    }


    /*
     * Updating a note
     */
    public int updateNote(note note ) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NOTE_NAME, note.getTitle());
        values.put(NOTE_DESCRIPTION, note.getNotes());



        // updating row
        return db.update(TABLE_NOTE, values, KEY_ID + " = ?",
                new String[] { String.valueOf(note.getId())});
    }


    /*
     * Deleting a note
     */
    public void deleteNote(long note_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTE, KEY_ID + " = ?",
                new String[] { String.valueOf(note_id) });
    }


    /**
     * Fetching all the saved notes
     * */
    public List<note> getAllNotes() {
        List<note> notes = new ArrayList<note>();
        String selectQuery = "SELECT  * FROM " + TABLE_NOTE;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                long noteId = c.getInt(c.getColumnIndex(KEY_ID));
                notes.add(getNote(noteId));
            } while (c.moveToNext());
        }

        return notes;
    }





}