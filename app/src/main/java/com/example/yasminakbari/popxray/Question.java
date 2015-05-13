package com.example.yasminakbari.popxray;

/**
 * Created by YasminAkbari on 5/13/15.
 */
import android.widget.RadioButton;
import android.widget.ImageButton;
import android.app.Activity;

public class Question extends Activity {
    String qnum; // the question number (ex. Question qCIQ1)
    int cnum; // the number of answer choices (if == 0 then this is a begin_category page)
    String question_text; // the text for the question
    String[] choices; // the text for the answer choices
    String[] summary_texts; // the text for each answer choice for the summary page
    String [] responses; // strings of the names of the response vars (ex. ["rCIQ1S1", "rCIQ1S2"])
    Question[] nexts; // the set of Questions that this Question links to,
    // based on which answer choice is selected (ex. for Question qCIQ1, nexts[1] = "qCIQ2"
    // because nexts[1] refers to answer choice rCIQ1S2, and the next question to go to
    // if this answer is chosen is qCIQ2).
    // This is used in Main for finding which next question to link to
    // and which answer choice in the "responses" matrix to make "true" based on user selection;
    // If the "Question" object is referring to a begin_category page, then nexts[0] indicates the
    // pointer for the "Continue" button and nexts[1] indicates the pointer for the "Skip" button
    ExampleSet[] examples; // the set of ExampleSets associated with this question. This is a 1D array;
    // each item is the ExampleSet associated with each answer choice

    // array of the RadioButtons in the layout (yeah I know this is clunky, let it go)
    RadioButton[] choice_views = new RadioButton[4];
    // array of the ExampleButtons in the layout
    ImageButton[] example_views = new ImageButton[4];

    public Question(String the_qnum, int the_cnum, String the_question_text, String[] the_responses)
    {
        qnum = the_qnum;
        cnum = the_cnum;
        question_text = the_question_text;
        choices = null; // set these separately, to make the code easier to read
        responses = the_responses;
        nexts = null;
        examples = null; // since "nexts" and "examples" are separate objects that need to be created at runtime,
        // we will set these with the "set" functions below after all the objects are constructed
    }

    public void setChoiceTexts(String[] the_choices){
        choices = the_choices;
    }

    public void setSummaryTexts(String[] the_summary_texts){
        summary_texts = the_summary_texts;
    }

    public void setNexts(Question[] the_nexts){
        nexts = the_nexts;
    }

    public void setExamples(ExampleSet[] the_examples){
        examples = the_examples;
    }

    public boolean isCategory(){ // just a little helper function that returns whether or not the Question is actually a category object
        return (cnum==0);
    }

}
