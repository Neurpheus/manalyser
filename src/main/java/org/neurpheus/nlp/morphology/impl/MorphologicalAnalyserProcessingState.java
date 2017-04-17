/*
 *  © 2015 Jakub Strychowski
 */

package org.neurpheus.nlp.morphology.impl;

import org.neurpheus.collections.tree.linkedlist.LinkedListTreeNode;

/**
 *
 * @author Jakub Strychowski
 */
public class MorphologicalAnalyserProcessingState {
    
    public int stateType;
    public int chPos;
    public char c;
    public Integer cint;
    public LinkedListTreeNode node;
    public int suffixPos;
    public int prefixPos;
    public int corePos;

    public MorphologicalAnalyserProcessingState() {
    }

    public void set(MorphologicalAnalyserProcessingState state) {
        stateType = state.stateType;
        chPos = state.chPos;
        c = state.c;
        cint = state.cint;
        node = state.node;
        suffixPos = state.suffixPos;
        prefixPos = state.prefixPos;
        corePos = state.corePos;
    }
    
}
