package com.example.yasminakbari.popxray;

/**
 * Created by YasminAkbari on 5/13/15.
 */
import android.app.Activity;

public class Example extends Activity {
    String qnum; // the question number and the answer choice number that directed you to this example (ex. "rCIQ1S1")
    int image_ref; // the featured example image (from the R.drawable section)
    String example_text; // the example description
    String reference_text; // the reference for the image
    ExampleSet back; // the pointer to where the back button should go (basically the parent example_selector)
    int index; // the index of this Example within the ExampleSet (ex. if this is the fourth example in the parent set, then index = 3)

    public Example(String the_qnum, int the_index, int the_image_ref, String the_example_text, String the_reference_text) {
        qnum = the_qnum;
        index = the_index;
        image_ref = the_image_ref;
        example_text = the_example_text;
        reference_text = the_reference_text;
    }

    public void setBack(ExampleSet the_back){
        back = the_back;
    }
}
