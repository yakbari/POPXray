package com.example.yasminakbari.popxray;

/**
 * Created by YasminAkbari on 5/13/15.
 */

import android.app.Activity;

public class ExampleSet extends Activity {

    Example[] example_set;
    Question parent; // question from which this example set was born
    int num_exes; // number of examples in this set

    public ExampleSet(Example[] the_example_set, Question the_parent, int the_num_exes) {
        example_set = the_example_set;
        parent = the_parent;
        num_exes = the_num_exes;
    }

    public Example getNext(Example current){ // returns the next Example in the set after the current Example
        int i = current.index;
        int next_i = i+1;
        if (next_i < num_exes) { // this means that "current" is not the last Example in the set
            return example_set[next_i];
        }
        else {
            System.out.println("~~~~Returning null!~~~~");
            return null;
        }
    }

    public Example getPrev(Example current){ // returns the next Example in the set after the current Example
        int i = current.index;
        if (i!=0) { // this means that "current" is not the first Example in the set
            int prev_i = i-1;
            return example_set[prev_i];
        }
        else{
            System.out.println("~~~~Returning null!~~~~");
            return null;
        }
    }
}
