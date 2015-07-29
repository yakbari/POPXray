package com.example.yasminakbari.popxray;

/**
 * Created by dx4 on 5/15/15.
 */

import java.util.ArrayList;

public class Question {

    String question_text; // the text for the question
    ArrayList<String> answer_texts; // the text for the answer choices
    ArrayList<String> summary_texts; // the text for each answer choice for the summary page
    ArrayList<ExampleSet> examples; // the set of ExampleSets associated with this question. This is a 1D array;

    public Question()
    {
        question_text = null;
        answer_texts = null; // set these separately, to make the code easier to read
        examples = null;
    }

    public Question(String the_question_text, ArrayList<String> the_responses)
    {
        question_text = the_question_text;
        answer_texts = null; // set these separately, to make the code easier to read
        examples = null; // since "nexts" and "examples" are separate objects that need to be created at runtime,
        // we will set these with the "set" functions below after all the objects are constructed
    }

    public int getNumAnswers() {
        return answer_texts.size();
    }

    public void setQuestionText(String the_question_text) {question_text = the_question_text;}

    public void setAnswerTexts(ArrayList<String> the_answer_texts){
        answer_texts = the_answer_texts;
    }

    public void setSummaryTexts(ArrayList<String> the_summary_texts){
        summary_texts = the_summary_texts;
    }

    // TODO
    public void setNexts(ArrayList<Question> the_nexts){
        //nexts = the_nexts;
    }

    public void setExamples(ArrayList<ExampleSet> the_examples){
        examples = the_examples;
    }

}
