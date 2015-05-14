// MainActivity.java
// Yasmin Akbari, PGY-1
// This is the main class for the POPXray application

package com.example.yasminakbari.popxray;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import java.util.*;

public class MainActivity extends Activity {

    public static final int THUMBNAIL_HEIGHT = 200;
    public static final int THUMBNAIL_WIDTH = 200; // These are used to generate thumbnails in the example_selector layout; just roll with it.

    // ~~~~~~~ CONSTANTS - RESPONSES ~~~~~~~
    public static Map<String,String> user_responses = new HashMap<String,String>(50); // this is used to store the user's responses throughout the course of runtime; this map is used at the "Summary" page
    // to generate an Impression; maps names of answer choices (i.e. "CIQ2S2") to whether or not it was selected ("true/false")
    public static Map<String,String> response_dictionary = new HashMap<String,String>(50); // this is a dictionary mapping the names of the answer choices (i.e. "CIQ2S2") to its text ('i.e. "The image appears underpenetrated.");
    public static Queue<Question> selected_categories = new LinkedList<Question>(); // this is used to store which categories the user chose to address in the algorithm (ex. I,A,B,C, etc.)


    // ~~~~~~~ CONSTANTS - QUESTIONS ~~~~~~~
    // Naming convention for question objects: qC_Q_ (C = category which is I,A,B,C etc., Q = question which is 1,2,3 etc.)
    // Naming convention for answer choices/responses: rC_Q_S_ (S = selections which are a,b,c,d, etc.)

    // The format is: Question(String the_qnum, int the_cnum, String the_question_text, String[] the_responses)
    public static Question qCIQ2 =
            new Question("qCIQ2", 3, Strings.text_qCIQ2, new String[]{"rCIQ2Sa","rCIQ2Sb","rCIQ2Sc"});
    public static Question qCIQ1 =
            new Question("qCIQ1", 2, Strings.text_qCIQ1, new String[]{"rCIQ1Sa","rCIQ2Sb"});
    public static Question cCI =
            new Question("cCI", 0, Strings.text_cCI, null); // this is technically a Begin Category, but is still constructed as a Question object

    // ~~~~~~~ CONSTANTS - EXAMPLES ~~~~~~~
    // Naming convention for examples: eC_Q_S_E_ (E = example # which is 1,2,3 etc.)

    // The format is: Example(String the_qnum, int index, int the_example, String the_example_text, String the_reference_text, Question the_back)
    public static Example eCIQ2S1E1 =
            new Example("eCIQ2S1E1", 0, R.drawable.e_ciq2s1r1, Strings.text_eCIQ2S1E1, Strings.reference_1);
    public static Example eCIQ2S2E1 =
            new Example("eCIQ2S2E1", 0, R.drawable.e_ciq2s2r2, Strings.text_eCIQ2S2E1, Strings.reference_2);

    // ~~~~~~~ CONSTANTS - EXAMPLE SETS ~~~~~~~
    // Initialize the example sets (set of all examples associated with a particular answer choice)
    // Naming convention for example sets: esC_Q_S_

    // The format is: ExampleSet(Example[] the_example_set, Question the_parent, int the_num_exes)
    public static ExampleSet esCIQ2S1 = new ExampleSet(new Example[]{eCIQ2S1E1}, qCIQ2, 1);
    public static ExampleSet esCIQ2S2 = new ExampleSet(new Example[]{eCIQ2S2E1}, qCIQ2, 1);

    // ~~~~~~~ END CONSTANTS ~~~~~~~


// I dunno what this function is for but I'm afraid to delete it
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // I dunno what this function is for either but I'm afraid to delete it
    //@Override
    /*public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/

    // ~~~~~~~ FUNCTIONS - MAIN ~~~~~~~
    public void main(View view) {
        // This function gets called once the user starts the program and clicks "Begin".

        // ~~~~~~~ SETUP - RELATIONSHIPS BETWEEN OBJECTS ~~~~~~~
        // Set the answer choice strings
        qCIQ1.setChoiceTexts(new String[]{Strings.text_rCIQ1S1, Strings.text_rCIQ1S2});
        qCIQ2.setChoiceTexts(new String[]{Strings.text_rCIQ2S1, Strings.text_rCIQ2S2, Strings.text_rCIQ2S3});

        // Set the answer choice summary strings
        // (this is different from the answer choice strings; ex. CIQ1S1, q.choices = "Yes", but q.summary_texts = "There is a previous film for comparison.")
        qCIQ1.setSummaryTexts(new String[]{Strings.text_sCIQ1S1, Strings.text_sCIQ1S2});
        qCIQ2.setSummaryTexts(new String[]{Strings.text_sCIQ2S1, Strings.text_sCIQ2S2, Strings.text_sCIQ2S3});

        // Set the "nexts" for what question to link to next depending on which answer choice is picked
        // The length of "nexts" is basically the number of answer choices +1 (to account for the skip button)
        // The last entry in "nexts" is the question to go to in case user chooses "skip this step"
        // If all the entries in "nexts" are null, this means that this question was the last in the category.
        // In this case, "doTheQuestion" will detect this and instead go to "doTheCategory",
        // which pops the next desired category off the "selected_categories" queue and goes from there.
        cCI.setNexts(new Question[]{qCIQ1, null});
        qCIQ1.setNexts(new Question[]{qCIQ2, qCIQ2});
        qCIQ2.setNexts(new Question[]{null,null});

        // Set the Example Sets associated with each question (each question has an ExampleSet[] array where each
        // index refers to a answer choice and the ExampleSet associated with it (so exampleset[0] = the set of
        // Example objects associated with the first answer choice)
        qCIQ2.setExamples(new ExampleSet[]{esCIQ2S1,esCIQ2S2});

        // Set the parent ExampleSet for each Example
        eCIQ2S1E1.setBack(esCIQ2S1);
        eCIQ2S2E1.setBack(esCIQ2S1);

        // ~~~~~~~ END SETUP ~~~~~~~

        // Start this party
        select_categories();

        String hi = "";
        hi = "hea";
    }

    public void select_categories(){
        setContentView(R.layout.select_categories);

        // Activate "Continue" button"
        Button cont = (Button) findViewById(R.id.Continue);
        cont.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                // Figure out which boxes were checked and push those categories to the selected_categories queue
                CheckBox chkBoxI = (CheckBox) findViewById(R.id.Outline_I);
                if (chkBoxI.isChecked()) {selected_categories.add(cCI);}
				/*CheckBox chkBoxA = (CheckBox) findViewById(R.id.Outline_A);
				if (chkBoxA.isChecked()) {selected_categories.add(cCA);}
				CheckBox chkBoxB = (CheckBox) findViewById(R.id.Outline_B);
				if (chkBoxB.isChecked()) {selected_categories.add(cCB);}
				CheckBox chkBoxC = (CheckBox) findViewById(R.id.Outline_C);
				if (chkBoxC.isChecked()) {selected_categories.add(cCC);}
				CheckBox chkBoxD = (CheckBox) findViewById(R.id.Outline_D);
				if (chkBoxD.isChecked()) {selected_categories.add(cCD);}
				CheckBox chkBoxE = (CheckBox) findViewById(R.id.Outline_E);
				if (chkBoxE.isChecked()) {selected_categories.add(cCE);}
				CheckBox chkBoxF = (CheckBox) findViewById(R.id.Outline_F);
				if (chkBoxF.isChecked()) {selected_categories.add(cCF);}
				CheckBox chkBoxH = (CheckBox) findViewById(R.id.Outline_H);
				if (chkBoxH.isChecked()) {selected_categories.add(cCH);}*/ //uncomment these when these objects have been added

                // If the queue is empty then the user didn't select any categories
                // Ideally this would cause a popup error message, but for now, I'll just have onClick do nothing
                // So if the queue isn't empty, do the first category

                Question first_category = selected_categories.poll();
                if (first_category != null) {
                    doTheCategory(first_category);
                }
            }
        });
    }

    public void doTheCategory(final Question cat){ // to avoid a major headache, "categories" are still Question objects; I know this is confusing but just deal with it k?
        // We already know the user selected this category because it was pushed onto the "selected_categories" queue by "select_categories()"
        // So now let's just do the category!

        // Setup layout
        setContentView(R.layout.begin_category);

        // Setup major dialogue text
        TextView question = (TextView) findViewById(R.id.Question);
        question.setText(cat.question_text);

        // Activate "Skip this step" button (only if the page has this button, i.e. is a "begin_category" page)
        // Actually this shouldn't be a thing anymore because user shouldn't be skipping a category once the selections have been made
        // Maybe this can be used for something else
		/*if (q.isCategory()) {
			Button skip = (Button) findViewById(R.id.Skip);
			skip.setOnClickListener(new Button.OnClickListener() {
				public void onClick(View v) {
					try {
						doTheQuestion(q.nexts[1]); // last choice in the "nexts" array for a begin_category-type question is always the pointer for the skip button
						// in this case, since this is a "begin_category" page, the index is nexts[1]
					}
					catch (Exception e){
						// Do nothing; "skip" location hasn't been set up yet.
						System.out.println("Error");
					}
				}
			});
		}*/

        // Highlight the current category in orange
        // This is really annoying repetitive code yay!
        if (cat.qnum.equals("cCI")){
            TextView cat_text = (TextView) findViewById(R.id.Outline_I);
            cat_text.setTextColor(Color.parseColor("#FF7730"));
        }
        else if (cat.qnum.equals("cCA")){
            TextView cat_text = (TextView) findViewById(R.id.Outline_A);
            cat_text.setTextColor(Color.parseColor("#FF7730"));
        }
        else if (cat.qnum.equals("cCB")){
            TextView cat_text = (TextView) findViewById(R.id.Outline_B);
            cat_text.setTextColor(Color.parseColor("#FF7730"));
        }
        else if (cat.qnum.equals("cCC")){
            TextView cat_text = (TextView) findViewById(R.id.Outline_C);
            cat_text.setTextColor(Color.parseColor("#FF7730"));
        }
        else if (cat.qnum.equals("cCD")){
            TextView cat_text = (TextView) findViewById(R.id.Outline_D);
            cat_text.setTextColor(Color.parseColor("#FF7730"));
        }
        else if (cat.qnum.equals("cCE")){
            TextView cat_text = (TextView) findViewById(R.id.Outline_E);
            cat_text.setTextColor(Color.parseColor("#FF7730"));
        }
        else if (cat.qnum.equals("cCF")){
            TextView cat_text = (TextView) findViewById(R.id.Outline_F);
            cat_text.setTextColor(Color.parseColor("#FF7730"));
        }
        else if (cat.qnum.equals("cCH")){
            TextView cat_text = (TextView) findViewById(R.id.Outline_H);
            cat_text.setTextColor(Color.parseColor("#FF7730"));
        }

        // Activate Continue button
        Button cont = (Button) findViewById(R.id.Continue);
        cont.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                try {
                    doTheQuestion(cat.nexts[0]); // nexts[0] is always the Continue choice for a "begin_category" page
                }
                catch (Exception e){
                    // Do nothing; "continue" location hasn't been set up yet.
                    System.out.println("Error");
                }
            }
        });


    }

    public void doTheQuestion(final Question q){
        // First, set the screen layout; there are different
        // question layouts depending on the number of answer choices
        // All the available xml layouts are viewable in the /res/layout folder
        if (q.cnum == -1) { // this is a "summary" page
            doTheSummary();
            return;
        }
        else if (q.cnum == 2) {
            setContentView(R.layout.question_layout_2);
        }
        else if (q.cnum == 3) {
            setContentView(R.layout.question_layout_3);
        }
        else if (q.cnum == 4) {
            setContentView(R.layout.question_layout_4);
        }

        // Set up the response_dictionary map entries for this question (which maps answer choices (ex. "CIQ1S1")
        // to their summary text if chosen ("There is a previous film for comparison.")
        // This map gets used later in doTheSummary()
        for (int i = 0; i<q.cnum; i++) {
            String k = q.responses[i];
            String v = q.summary_texts[i];
            response_dictionary.put(k, v);
        }

        // Set question text
        TextView question = (TextView) findViewById(R.id.Question);
        question.setText(q.question_text);

        // Gather the answer choice views and the example button views
        // I know this is ugly style. Don't judge. Making apps is hard, okay?
        if (q.cnum == 2) {
            q.choice_views[0]=(RadioButton) findViewById(R.id.Option1);
            q.choice_views[1]=(RadioButton) findViewById(R.id.Option2);
            q.example_views[0]=(ImageButton) findViewById(R.id.Example1);
            q.example_views[1]=(ImageButton) findViewById(R.id.Example2);
        }
        else if (q.cnum == 3) {
            q.choice_views[0]=(RadioButton) findViewById(R.id.Option1);
            q.choice_views[1]=(RadioButton) findViewById(R.id.Option2);
            q.choice_views[2]=(RadioButton) findViewById(R.id.Option3);
            q.example_views[0]=(ImageButton) findViewById(R.id.Example1);
            q.example_views[1]=(ImageButton) findViewById(R.id.Example2);
            q.example_views[2]=(ImageButton) findViewById(R.id.Example3);
        }
        else if (q.cnum == 4) {
            q.choice_views[0]=(RadioButton) findViewById(R.id.Option1);
            q.choice_views[1]=(RadioButton) findViewById(R.id.Option2);
            q.choice_views[2]=(RadioButton) findViewById(R.id.Option3);
            q.choice_views[3]=(RadioButton) findViewById(R.id.Option4);
            q.example_views[0]=(ImageButton) findViewById(R.id.Example1);
            q.example_views[1]=(ImageButton) findViewById(R.id.Example2);
            q.example_views[2]=(ImageButton) findViewById(R.id.Example3);
            q.example_views[3]=(ImageButton) findViewById(R.id.Example4);
        }

        // Set the answer choice texts
        for (int i=0; i<q.cnum; i++) {
            RadioButton choice = q.choice_views[i];
            choice.setText(q.choices[i]);
        }

        // Initialize the HashMap of all the user's responses to THIS question
        // The item mapped with the chosen answer will be changed to "true" upon
        // user clicking the "Continue" button.  I initialize the mappings to false just
        // in case the user presses the back button
        for (int i = 0; i<q.cnum; i++) {
            String choice = q.responses[i];
            user_responses.put(choice, "false");
        }

        // Activate the "Example" buttons
        for (int i=0; i<q.cnum; i++) {
            ImageButton example = q.example_views[i];
            final int index = i;
            example.setOnClickListener(new ImageButton.OnClickListener() {
                public void onClick(View v) {
                    try {
                        // Get the example set associated with the button clicked
                        System.out.println(index);
                        ExampleSet ex = q.examples[index];
                        pickTheExample(ex);
                    }
                    catch (Exception e) {
                        // Do nothing; button hasn't been set up yet.
                        System.out.println("Error");
                    }
                }
            });
        }

        // Activate "Continue" button"
        Button cont = (Button) findViewById(R.id.Continue);
        cont.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                try{
                    for (int i = 0; i<q.cnum; i++){
                        if (q.choice_views[i].isChecked()){
                            // A answer choice has been selected and the user is ready to continue.
                            // First, save the choice so it can be included in the "summary" page at the end.
                            // Choices are saved in a "user_responses" map structure
                            String choice = q.responses[i];
                            user_responses.remove(choice); // erase the previous "false" association
                            user_responses.put(choice, "true");

                            // Next, invoke the next question based on that answer choice.
                            // The next question may vary based on what answer is chosen.
                            Question next_question = q.nexts[i];
                            if (next_question == null) { // this implies that the current question was the last in the category; so now, begin the next category
                                Question next_category = selected_categories.poll();
                                if (next_category != null){
                                    doTheCategory(next_category);
                                }
                                else { // this implies that the selected_categories queue was empty, so there are no more categories to address
                                    doTheSummary();
                                }
                            }
                            doTheQuestion(q.nexts[i]);
                        }
                    }
                }
                catch (Exception e){
                    // Do nothing; q.nexts[i] might be null
                    System.out.println("Error");
                }
            }
        });

    }

    public void pickTheExample(ExampleSet the_examples){
        setContentView(R.layout.example_selector);
        final Question parent = the_examples.parent;

        // Grab the images from all the Example objects in the ExampleSet
        // and display them in the scrolling image view as thumbnails
        for (int i=0; i<the_examples.example_set.length; i++){
            // Get each Example from the set that needs to be displayed
            final Example current = the_examples.example_set[i];

            // Get the LinearLayout (within the HorizontalScrollView) from the layout
            LinearLayout ll = (LinearLayout)findViewById(R.id.linearLayoutForExamples);

            // Create the ImageButton
            ImageButton exbut = new ImageButton(this);
            exbut.setId(i);

            // Create the thumbnail
            ImageView expic = new ImageView(this);
            expic.setImageResource(current.image_ref);
            BitmapDrawable drawable = (BitmapDrawable) expic.getDrawable();
            Bitmap exbitmap = drawable.getBitmap();
            Float width  = Float.valueOf(exbitmap.getWidth());
            Float height = Float.valueOf(exbitmap.getHeight());
            Float ratio = width/height;
            exbitmap = Bitmap.createScaledBitmap(exbitmap, (int)(THUMBNAIL_HEIGHT*ratio), THUMBNAIL_HEIGHT, false);

            int padding = (THUMBNAIL_WIDTH - exbitmap.getWidth())/2;
            exbut.setPadding(padding, 0, padding, 0);
            exbut.setImageBitmap(exbitmap);

            // Add the ImageButton to the HorizontalScrollView
            ll.addView(exbut);

            // Activate the ImageButton
            exbut.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v){
                    try{
                        doTheExample(current);
                    }
                    catch(Exception e){
                        // Do nothing; something is wrong with the Example
                        System.out.println("Error");
                    }
                }
            });
        }

        // Setup "Back" button - goes back to parent question
        Button back = (Button) findViewById(R.id.Back);
        back.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v){
                try{
                    doTheQuestion(parent);
                }
                catch(Exception e){
                    // Do nothing; the parent hasn't been established yet
                    System.out.println("Error");
                }
            }
        });

    }

    public void doTheExample(Example the_example){
        setContentView(R.layout.image_example);

        // Set example text
        TextView text = (TextView) findViewById(R.id.text_explanation);
        text.setText(the_example.example_text); // lol wut

        // Set reference text
        TextView ref = (TextView) findViewById(R.id.text_ref);
        ref.setText(the_example.reference_text);

        // Set image
        ImageView image = (ImageView) findViewById(R.id.image_example);
        image.setImageResource(the_example.image_ref);

        // Set "back to list" button
        Button back = (Button) findViewById(R.id.Back);
        final ExampleSet the_back = the_example.back;
        back.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v){
                try {
                    pickTheExample(the_back);
                }
                catch (Exception e){
                    // Do nothing; something is wrong with accessing the parent ExampleSet
                    System.out.println("Error");
                }

            }
        });

        // Setup next button
        Button next = (Button) findViewById(R.id.NextImage);
        final Example the_next = (the_example.back).getNext(the_example); // get the next Example from the list
        if (the_next != null) {
            next.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v){
                    try {
                        doTheExample(the_next);
                    }
                    catch (Exception e){
                        // Do nothing; something is wrong with picking the next example
                        System.out.println("Error");
                    }
                }
            });
        }
        else {
            next.setVisibility(View.INVISIBLE); // "getNext" returned NULL because there is no next; so hide the effing button
        }

        // Setup previous button
        Button prev = (Button) findViewById(R.id.PreviousImage);
        final Example the_prev = (the_example.back).getPrev(the_example); // get the next Example from the list
        if (the_prev != null){
            prev.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v){
                    try {
                        doTheExample(the_prev);
                    }
                    catch (Exception e){
                        // Do nothing; something is wrong with picking the next example
                        System.out.println("Error");
                    }
                }
            });
        }
        else {
            prev.setVisibility(View.INVISIBLE); // "getPrev" returned NULL because there is no previous; so hide the effing button
        }
    }

    public void doTheSummary(){
        setContentView(R.layout.summary);
        TextView summ = (TextView) findViewById(R.id.Summary);

        // Intialize a string to print as the summary
        String the_summary = new String();

        // Get a set of the entries in the user_responses hashmap
        Set set = user_responses.entrySet();

        // Make an iterator and iterate that list, bro
        Iterator i = set.iterator();
        while (i.hasNext()) {
            Map.Entry me = (Map.Entry)i.next();
            System.out.println((String)me.getValue());
            if ((String) me.getValue() == "true") { // i.e. the user chose this answer choice
                // Get the text for the answer choice associated with this response identifier
                // I.e. if user_responses indicates that the user picked answer "CIQ1S2", then we are
                // locating the string associated with that answer choice
                String text = response_dictionary.get(me.getKey());
                System.out.println(text);

                // Concat the string to the_summary
                the_summary = the_summary.concat(text);
                the_summary = the_summary.concat("\n");
                System.out.println(the_summary);
            }
        }
        // NOT ROBUST
        summ.setText(the_summary);
        //summ.setText("There is a previous chest x-ray for comparison.  The image is well-penetrated.");
    }


/*	// ~~~~~~~ FINAL REPORT ~~~~~~~
	public void summarize_findings(View view){
		setContentView(R.layout.summary);
		String findings = new String();
		// Question1b
		if (resp_1bi){
			findings = findings.concat("There is a previous CXR study available for comparison.\n");
		}
		else if (resp_1bii){
			findings = findings.concat("There is no previous CXR study available for comparison\n");
		}
		// Question1c
		if (resp_1ci){
			findings = findings.concat("The image appears well-penetrated.\n");
		}
		else if (resp_1cii){
			findings = findings.concat("The image appears underpenetrated.\n");
		}
		else if (resp_1ciii){
			findings = findings.concat("The image appears overpenetrated.\n");
		}

		// Set findings text
		TextView all_findings = (TextView) findViewById(R.id.Summary);
		all_findings.setText(findings);
	}*/
}

