package com.example.yasminakbari.popxray;

import android.util.JsonReader;
import android.util.JsonToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by YasminAkbari on 7/23/15.
 */
public class CXRJsonReader {

    public List<Question> JsonToQuestionList(JsonReader reader) throws IOException {
        // This function grabs all the question objects from the supplied JSON
        // It populates all_the_questions for a certain category using "readQuestion()" as a helper function
        // It returns a list of all question objects in that category
        List<Question> questions = new ArrayList();
        reader.beginObject(); // begin the JSON
        while (reader.hasNext()) { // while reader has a next question
            String name = reader.nextName(); // should be "question_x"
            questions.add(readQuestion(reader));
        }
        reader.endObject();
        return questions;
    }

    public Question readQuestion(JsonReader reader) throws IOException {
        // This function reads the next question object from the supplied JSON
        Question the_question = new Question();
        ArrayList<ExampleSet> the_example_sets = new ArrayList<ExampleSet>();

        reader.beginObject();
        while (reader.hasNext()) { // while the reader has a field in the Question object to read
            String name = reader.nextName();
            if (name.equals("text")) {
                String qtext = reader.nextString();
                the_question.setQuestionText(qtext);
            } else if (name.equals("answer_set")) {
                ArrayList<String> answers = readStringArray(reader);
                the_question.setAnswerTexts(answers);
            } else if (name.equals("summary_set")) {
                ArrayList<String> summaries = readStringArray(reader);
                the_question.setSummaryTexts(summaries);
            } else if (name.equals("example_set_1") && reader.peek() != JsonToken.NULL) {
                ExampleSet example_set = readExampleSet(reader);
                the_example_sets.add(example_set);
            } else if (name.equals("example_set_2") && reader.peek() != JsonToken.NULL) {
                ExampleSet example_set = readExampleSet(reader);
                the_example_sets.add(example_set);
            } else if (name.equals("example_set_3") && reader.peek() != JsonToken.NULL) {
                ExampleSet example_set = readExampleSet(reader);
                the_example_sets.add(example_set);
            } else if (name.equals("example_set_4") && reader.peek() != JsonToken.NULL) {
                ExampleSet example_set = readExampleSet(reader);
                the_example_sets.add(example_set);
            } else {
                reader.skipValue();
            }
        }
        reader.endObject(); // close the current question object
        the_question.setExamples(the_example_sets);
        return the_question;
    }

    public ArrayList<String> readStringArray(JsonReader reader) throws IOException{
        ArrayList array = new ArrayList();

        reader.beginArray();
        while (reader.hasNext()){
            array.add(reader.nextString());
        }
        reader.endArray();
        return array;
    }

    public ExampleSet readExampleSet(JsonReader reader) throws IOException{
        ArrayList examples = new ArrayList();

        reader.beginArray();
        while (reader.hasNext()){
            Example current_ex = readExample(reader);
            examples.add(current_ex);
        }
        reader.endArray();
        String thing = "";
        thing = "now";
        return new ExampleSet(examples);

    }

    public Example readExample(JsonReader reader) throws IOException{
        Example example = new Example();

        reader.beginObject();
        while (reader.hasNext()){
            String name = reader.nextName();
            if (name.equals("text")){
                example.setExampleText(reader.nextString());
            }
            else if (name.equals("image")){
                example.setImageRef(reader.nextString());
            }
            else if (name.equals("ref")){ // only if the Example actually has a reference (not all do)
                example.setReferenceText(reader.nextString());
            }
            else {
                reader.skipValue();
            }
        }
        reader.endObject();

        return example;
    }
}