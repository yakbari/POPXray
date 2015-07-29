package com.example.yasminakbari.popxray;

/**
 * Created by YasminAkbari on 5/13/15.
 */
import android.app.Activity;

public class Example extends Activity {
    String image_ref; // the name of the featured example image
    String example_text; // the example description
    String reference_text; // the reference for the image, if there is one
    ExampleSet parent; // the pointer to where the back button should go (basically the parent example_selector)

    public Example(){
        image_ref = null;
        example_text = null;
        reference_text = null;
    }

    public Example(String the_image_ref, String the_example_text, String the_reference_text) {
        image_ref = the_image_ref;
        example_text = the_example_text;
        reference_text = the_reference_text;
    }

    public void setParent(ExampleSet the_parent) { parent = the_parent; }

    public void setImageRef(String the_image_ref) { image_ref = the_image_ref; }

    public void setExampleText(String the_example_text) { example_text = the_example_text; }

    public void setReferenceText(String the_reference_text) {reference_text = the_reference_text; }
}
