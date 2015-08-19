package com.example.yasminakbari.popxray;

import java.util.LinkedHashMap;
import java.util.Stack;

/**
 * Created by dx4 on 5/15/15.
 */

public class QuestionAdapter {
    private Stack <Question> mQuestionHistory;
    private LinkedHashMap<Integer, Question> mLastQuestionByCategory;
    private int mCurrCategory = 0;
    private Question mCurrQuestion = null;

    QuestionAdapter(/* categories, data model, etc */) {
        mQuestionHistory = new Stack<Question> ();
        mLastQuestionByCategory = new LinkedHashMap<Integer,Question>();

    }

    /* 1. Returns question object which should follow after user presses Next-> button, this is
     * dependent on the ansewrChoice since there could be specific followup questions
     * 2. Also saves the answerChoice to the question */
    Question next(int answerChoice) {
        return null;
    }

    /* Returns question object which should follow after user presses <-Prev button
     * If this was the first question of the category, it should return to the last answered
     * question in the previous category.
     */
    Question previous() {
        return null;
    }

    boolean hasNext(int answerChoice) {
        return false;
    }

    /* Returns question object which should follow after user presses skip button, no answer is stored */
    Question skip() {
        return null;
    }

    boolean hasPrevious() {
        return false;
    }

    /* Returns question object which should be the result of the user pressing the back button
     */
    Question history() {
        return null;
    }

    /* Start the first question */
    Question begin() {
        return questionByCategory(0);
    }

    /* Returns the last question which was done by the user in that particular category, if there were
     * questions that were done in that category, it returns the first one.
     */
    Question questionByCategory(int category) {
        return null;
    }

    /* Returns ExampleSet for a specific answer choice */
    Object getExampleSet(int answerChoice) {
        return null;
    }
}


