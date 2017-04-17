/*
 *  © 2015 Jakub Strychowski
 */

package org.neurpheus.nlp.morphology.impl;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Jakub Strychowski
 */
public class MorphologicalAnalyserProcessingObject {
    
    public int[] intStack;
    public MorphologicalAnalyserProcessingState[] stack;
    public HashMap checkedForms;
    public ArrayList result;
    public boolean startsFromUppercase;
    public char lowercasedFirstCharacter;

    public MorphologicalAnalyserProcessingObject() {
        stack = new MorphologicalAnalyserProcessingState[40];
        for (int i = 0; i < stack.length; i++) {
            stack[i] = new MorphologicalAnalyserProcessingState();
        }
        intStack = new int[1000];
        checkedForms = new HashMap(50);
        result = new ArrayList(30);
    }

    public void clear() {
        checkedForms.clear();
        result.clear();
    }
    
}
