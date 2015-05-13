package com.example.yasminakbari.popxray;

/**
 * Created by YasminAkbari on 5/13/15.
 */

public class Strings {
    // QUESTION TEMPLATE
    // public static String text_question_** = new String("");

    // ANSWER CHOICE TEMPLATE
    // public static String text_choice_** = new String("");

    // SUMMARY TEMPLATE (these are the lines that are placed in the "summary" page, corresponding to chosen answers)
    // public static String text_sumchoice_** = new String("");

    // Naming convention (associated with POPXRAY outline):
    // C = category (I, A, B, C, D, E, F, G, H)
    // Q = question # (1, 2, 3, etc.)
    // S = selection # (1, 2, 3, etc.)
    // E = example # (1, 2, 3, etc.)

    // CATEGORY I
    public static String text_cCI = new String(
            "Let's assess the type and quality of the image.");
    // QUESTION CIQ1
    public static String text_qCIQ1 = new String(
            "Is there a previous CXR study available for comparison?");
    public static String text_rCIQ1S1 = new String("Yes.");
    public static String text_rCIQ1S2 = new String("No.");
    public static String text_sCIQ1S1 = new String("There is a previous film for comparison.");
    public static String text_sCIQ1S2 = new String("There is no previous film for comparison.");
    // QUESTION CIQ2
    public static String text_qCIQ2 = new String(
            "Is the image well-penetrated?");
    public static String text_rCIQ2S1 = new String("Yes.");
    public static String text_rCIQ2S2 = new String(
            "No, it appears underpenetrated.");
    public static String text_rCIQ2S3 = new String(
            "No, it appears overpenetrated.");
    public static String text_sCIQ2S1 = new String("The image appears well-penetrated.");
    public static String text_sCIQ2S2 = new String("The image appears underpenetrated.");
    public static String text_sCIQ2S3 = new String("The image appears overpenetrated.");
    public static String text_eCIQ2S1E1 = new String(
            "This is an example of a normal, well-penetrated radiograph. Notice that 1) the vertebrae are visible behind the heart, 2) the left hemidiaphragm is visible to the edge of the spine, and 3) the lungs are appropriately visualized and the vascular markings are not prominent.");
    public static String text_eCIQ2S2E1 = new String(
            "This is an example of an underpenetrated study.  Notice that the left lung base and left hemidiaphragm are both not visible, and the pulmonary vasculature appear abnormally prominent. Underpenetration is important to recognize as it can falsely mimic a variety of lung pathologies.");

    // CATEGORY A
    public static String text_cCA = new String(
            "Let's assess the airway.");

    // CATEGORY B
    public static String text_cCB = new String(
            "Let's assess the ribs, spine, and other bony structures.");

    // CATEGORY C
    public static String text_cCC = new String(
            "Let's assess the cardiac structures/mediastinum.");

    // CATEGORY D
    public static String text_cCD = new String(
            "Let's assess the diaphragm and costophrenic angles.");

    // REFERENCES
    public static String reference_1 = new String("Reference unknown");
    public static String reference_2 = new String("Figure 6. From Raoof et al., 2012. Interpretation of plain chest roentgenogram.  CHEST 141(2): 545-548.");
}