/*
 * Neurpheus - Morfological Analyser
 *
 * Copyright (C) 2006 Jakub Strychowski
 *
 *  This library is free software; you can redistribute it and/or modify it
 *  under the terms of the GNU Lesser General Public License as published by the Free
 *  Software Foundation; either version 2.1 of the License, or (at your option)
 *  any later version.
 *
 *  This library is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *  or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 *  for more details.
 */
package org.neurpheus.nlp.morphology.impl;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.neurpheus.collections.hashing.BloomFilter;
import org.neurpheus.collections.hashing.LRUCache;
import org.neurpheus.collections.tree.TreeNodeWithData;
import org.neurpheus.collections.tree.linkedlist.LinkedListTree;
import org.neurpheus.collections.tree.linkedlist.LinkedListTreeFactory;
import org.neurpheus.collections.tree.linkedlist.LinkedListTreeNode;
import org.neurpheus.core.charset.DynamicCharset;
import org.neurpheus.core.io.DataOutputStreamPacker;
import org.neurpheus.core.string.LocaleHelper;
import org.neurpheus.machinelearning.classification.ClassificationException;
import org.neurpheus.machinelearning.neuralnet.NeuralNetwork;
import org.neurpheus.nlp.morphology.DefaultMorphologyFactory;
import org.neurpheus.nlp.morphology.ExtendedInflectionPattern;
import org.neurpheus.nlp.morphology.MorphologicalAnalyser;
import org.neurpheus.nlp.morphology.MorphologicalAnalysisResult;
import org.neurpheus.nlp.morphology.MorphologicalComponent;
import org.neurpheus.nlp.morphology.MorphologyException;
import org.neurpheus.nlp.morphology.MorphologyFactory;
import org.neurpheus.nlp.morphology.PropertyConst;
import org.neurpheus.nlp.morphology.QualityConst;
import org.neurpheus.nlp.morphology.SpeedConst;
import org.neurpheus.nlp.morphology.inflection.InflectionPatternsBase;
import org.neurpheus.nlp.morphology.inflection.InflectionPatternsMap;
import org.neurpheus.nlp.morphology.VowelCharactersImpl;
import org.neurpheus.nlp.morphology.WordFormGenerator;
import org.neurpheus.nlp.morphology.baseimpl.AbstractMorphologicalAnalyser;
import org.neurpheus.nlp.morphology.baseimpl.MorphologicalAnalysisResultImpl;
import org.neurpheus.nlp.morphology.baseimpl.TagsetImpl;
import org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalyserInfo;
import org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalysers;
import org.neurpheus.nlp.morphology.inflection.FormPattern;
import org.neurpheus.nlp.morphology.tagset.GrammaticalProperties;
import org.neurpheus.nlp.morphology.tagset.GrammaticalPropertiesList;
import org.neurpheus.nlp.morphology.tagset.Tagset;

/**
 *
 * TODO Nie właściwie generują się przedrostki w MySpell.
 *
 * @author Jakub Strychowski
 */
public class MorphologicalAnalyserImpl extends AbstractMorphologicalAnalyser implements Serializable,
        WordFormGenerator {

    /** The logger for this class. */
    private static final Logger logger = Logger.getLogger(MorphologicalAnalyserImpl.class.getName());

    /** Unique serialization identifier of this class. */
    static final long serialVersionUID = 770608061005084934L;

    public static final String PROPERTY_IS_TAGGED = "isTagged";

    public static final String PROPERTY_DATA_PATH = "dataPath";

    public static String PATH_MORPHOLOGICAL_ANALYSERS_INFO = "data/morphological-analyser-info.xml";

    public static String PATH_MORPHOLOGICAL_ANALYSER_DATA = "data/morphological-analyser.bin";

    private static int CACHE_SIZE = 1000;

    private static HashMap instances = null;

    /** Holds the inflection tree. */
    private LinkedListTree tree;

    /** Holds all inflection patterns. */
    private InflectionPatternsBase ipb;

    /** Holds the false positive set of base forms. */
    private BloomFilter baseFormsBloomFilter;

    /** Holds the neural network used for the inflection pattern selection. */
    private NeuralNetwork neuralNetwork;

    /** Holds results for most frequently used word forms. */
    private transient HashMap frequentFormsCache;

    private transient boolean useNeuralNetwork;

    private transient VowelCharactersImpl vowelCharacters = null;

    private transient boolean useBaseFormsDictionary;

    private transient LRUCache cache;

    /** Holds the index of a last inflection pattern which is involved in neural network processing. */
    private int maxIndexOfNeuralInflectionPattern;

    public static final double PERFECT_MATCHING_WEIGHT = 1.0;

    /** Holds the weight multipler for a supplement length. */
    private double supplementWeight = 22;

    /** Holds the weight mulipler for a core pattern length. */
    private double corePatternWeight = 11;

    /** Holds the weight multipler for a number of lexemes covered by an inflection pattern. */
    private double ipWeight = 4;

    /** Holds the maximum number of lexemes covered by a single IP. */
    private int maxIPWeight;

    /** Holds the maximum length of a core pattern. */
    private int maxCorePatternLength;

    /** Holds the maximum length of a supplement. */
    private int maxSupplementLength;

    private transient double weightMultipler;

    private transient Deque<MorphologicalAnalyserProcessingObject> processingObjects;
    
    private DynamicCharset charset;

    /** Creates a new instance of MorphologicalAnalyserImpl */
    public MorphologicalAnalyserImpl() {
        charset = new DynamicCharset("international", new String[0]);
        charset.setInternational();
        useBaseFormsDictionary = true;
        setUseNeuralNetwork(true);
        setSupportedLocales(new ArrayList(instances.keySet()));
        processingObjects = new ConcurrentLinkedDeque<>();
        frequentFormsCache = new HashMap();
        cache = new LRUCache(CACHE_SIZE);
    }

    public final static char BASE_FORM_TAG = '!';
    public final static char CONSONANT = '#';
    public final static char VOWEL = '$';
    public final static char WILDCARD = '%';
    public final static char CORE_TAG = '&';
    public final static Character BASE_FORM_TAG_CHARACTER = BASE_FORM_TAG;
    public final static Character CORE_TAG_CHARACTER = CORE_TAG;
    public final static Character WILDCARD_CHARACTER = WILDCARD;
    public final static Character VOWEL_CHARACTER = VOWEL;
    public final static Character CONSONANT_CHARACTER = CONSONANT;
    public final static Integer BASE_FORM_TAG_INTEGER = new Integer((int) BASE_FORM_TAG);
    public final static Integer CORE_TAG_INTEGER = new Integer((int) CORE_TAG);
    public final static Integer WILDCARD_INTEGER = new Integer((int) WILDCARD);
    public final static Integer VOWEL_INTEGER = new Integer((int) VOWEL);
    public final static Integer CONSONANT_INTEGER = new Integer((int) CONSONANT);
    
    public final static int[] MATCH_SUFFIX_KEYS = {WILDCARD_INTEGER, CORE_TAG_INTEGER, 0};
    public final static int[] MATCH_CORE_KEYS = {CONSONANT_INTEGER, VOWEL_INTEGER, WILDCARD_INTEGER, CORE_TAG_INTEGER, 0};

    private static final int STATE_MATCH_SUFFIX = 20;
    private static final int STATE_MATCH_BASE_FORM = 30;
    private static final int STATE_MATCH_CORE = 40;
    private static final int STATE_MATCH_REMAINING_CHARACTERS = 50;
    private static final int STATE_MATCH_PREFIX = 60;
    private static final int STATE_CHECK_CREATED_FORM = 70;

    public HashMap getFrequentFormsCache() {
        return frequentFormsCache;
    }

    public void setFrequentFormsCache(HashMap frequentFormsCache) {
        this.frequentFormsCache = frequentFormsCache;
    }

    /**
     * @return the charset
     */
    public DynamicCharset getCharset() {
        return charset;
    }

    /**
     * @param charset the charset to set
     */
    public void setCharset(DynamicCharset charset) {
        this.charset = charset;
    }

    private void logState(String str, MorphologicalAnalyserProcessingState state) {
        StringBuilder traceInfo = new StringBuilder(100);
        traceInfo.append(str);
        traceInfo.append("|state=");
        traceInfo.append(state.stateType);
        traceInfo.append("|pos=");
        traceInfo.append(state.chPos);
        traceInfo.append("|c=");
        traceInfo.append(state.c);
        traceInfo.append("|spos=");
        traceInfo.append(state.suffixPos);
        traceInfo.append("|ppos=");
        traceInfo.append(state.prefixPos);
        traceInfo.append("|cpos=");
        traceInfo.append(state.corePos);
        logger.log(Level.FINEST, traceInfo.toString());
    }


    public static String makeForm(String core, String supplement) {
        char[] cTab = core.toCharArray();
        char[] sTab = supplement.toCharArray();
        //StringBuffer res = new StringBuffer();
        char[] result = new char[cTab.length + sTab.length];
        int resultPos = 0;
        int sPos = sTab.length - 1;
        int cPos = cTab.length - 1;
        while (sPos >= 0) {
            char sb = sTab[sPos--];
            if (sb == WILDCARD) {
                if (cPos >= 0) {
                    if (cTab[cPos] == WILDCARD) {
                        --cPos;
                    }
                    if (cPos >= 0) {
                        char cb;
                        do {
                            cb = cTab[cPos--];
                            if (cb != WILDCARD) {
                                //res.append(cb);
                                result[resultPos++] = cb;
                            }
                        } while (cPos >= 0 && cb != WILDCARD);
                    }
                }
            } else {
                //res.append(sb);
                result[resultPos++] = sb;
            }
        }
        if (resultPos > 0) {
            //res.reverse();
            int i = 0;
            int j = resultPos - 1;
            while (i < j) {
                char tmpC = result[i];
                result[i] = result[j];
                result[j] = tmpC;
                ++i;
                --j;
            }
            return new String(result, 0, resultPos);
        } else {
            return "";
        }
        //return res.toString();
    }


    public Tagset getTagset() {
        Tagset res = ipb.getTagset();
        if (res == null) {
            res = DefaultMorphologyFactory.getInstance().createTagset();
            ipb.setTagset(res);
        }
        return res;
    }

    public final List analyse2list(final String inputString) throws MorphologyException {
        return analyse2list(inputString, false);
    }

    public final List analyse2list(final String inputString, final boolean forGeneration) throws
            MorphologyException {
        if (!isInitialized()) {
            init();
        }
        if (inputString == null) {
            throw new NullPointerException("The 'inputString' argument cannot be null.");
        }
        final int length = inputString.length();
        if (length == 0) {
            throw new IllegalArgumentException("The 'inputString' argument cannot be empty");
        }

        List freqRes = (List) frequentFormsCache.get(inputString);
        if (freqRes != null) {
            return freqRes;
        }

        boolean startsFromUppercase = Character.isUpperCase(inputString.charAt(0));
        boolean endsWithUppercase = Character.isUpperCase(inputString.charAt(length - 1));
        boolean isUppercased = startsFromUppercase && endsWithUppercase && inputString.equals(
                inputString.toUpperCase());

        Tagset tagset = getTagset();
        if (isUppercased || endsWithUppercase) {
            // do not process words containing uppercase characters only
            ExtendedMorphologicalAnalysisResult res
                    = new ExtendedMorphologicalAnalysisResult(inputString, 1, inputString, "",
                                                              tagset.
                                                              getUnknownGrammaticalPropertiesList(),
                                                              null);
            return Collections.singletonList(res);
        }

        List cachedResult = (List) cache.get(inputString);
        if (cachedResult != null) {
            return cachedResult;
        }

        //String s = startsFromUppercase ? inputString.toLowerCase() : inputString;
        String str = inputString;

        char fistCharLowercased = str.charAt(0);
        if (startsFromUppercase) {
            fistCharLowercased = Character.toLowerCase(fistCharLowercased);
        }
        Integer firtCharCode = new Integer(fistCharLowercased);

        InflectionPatternsMap ipm = ipb.getInflectionPatternsMap();
        VowelCharactersImpl vch = getVowelCharacters();

        ArrayList result;
        int[] intStack;
        MorphologicalAnalyserProcessingState[] stack;
        HashMap checkedForms;
        MorphologicalAnalyserProcessingObject po;
        if (processingObjects.isEmpty()) {
            po = new MorphologicalAnalyserProcessingObject();
        } else {
            po = processingObjects.pop();
            po.clear();
        }
        po.startsFromUppercase = startsFromUppercase;
        po.lowercasedFirstCharacter = fistCharLowercased;
        result = po.result;
        intStack = po.intStack;
        stack = po.stack;
        checkedForms = po.checkedForms;

        int stackPos = 0;

        MorphologicalAnalyserProcessingState state = new MorphologicalAnalyserProcessingState();
        state.stateType = STATE_MATCH_SUFFIX;
        state.chPos = length - 1;
        state.c = str.charAt(state.chPos);
        state.cint = (int) state.c;
        state.node = tree.getRoot();
        boolean perfectMatching = false;

        LinkedListTreeNode tmpNode;
        LinkedListTreeNode[] foundChildren = new LinkedListTreeNode[5];
        MorphologicalAnalyserProcessingState ps;

        StringBuilder core = new StringBuilder(100);
        
        boolean finish = false;
        while (!finish) {
            if (logger.isLoggable(Level.FINEST)) {
                logState(str, state);
            }
            switch (state.stateType) {
                case STATE_MATCH_SUFFIX:

                    MATCH_SUFFIX_KEYS[2] = state.cint;
                    state.node.findChildren(MATCH_SUFFIX_KEYS, foundChildren, intStack, 0);

                    // check wildcard
                    //tmpNode = (LinkedListTreeNode) state.node.getChild(WILDCARD_INTEGER, intStack, 0);
                    tmpNode = foundChildren[0];
                    if (tmpNode != null) {
                        ps = stack[stackPos++];
                        ps.suffixPos = state.chPos + 1;
                        ps.corePos = ps.suffixPos;
                        ps.stateType = STATE_MATCH_PREFIX;
                        ps.node = tmpNode;
                        ps.chPos = 0;
                        ps.c = str.charAt(0);
                        ps.cint = (int) ps.c;
                    }


                    // check core tag
                    //tmpNode = (LinkedListTreeNode) state.node.getChild(CORE_TAG_INTEGER, intStack, 0);
                    tmpNode = foundChildren[1];

                    
                    if (tmpNode != null) {
                        ps = stack[stackPos++];
                        ps.suffixPos = state.chPos + 1;
                        ps.stateType = STATE_MATCH_CORE;
                        ps.node = tmpNode;
                        ps.chPos = state.chPos;
                        ps.c = state.c;
                        ps.cint = state.cint;
                    }

                    // check succeding character
                    //tmpNode = (LinkedListTreeNode) state.node.getChild(state.cint, intStack, 0);
                    tmpNode = foundChildren[2];
                    
                    if (tmpNode == null && startsFromUppercase && state.chPos == 0) {
                        tmpNode = state.node.getChild(firtCharCode, intStack, 0);
                        
                    }
                    if (tmpNode != null) {
                        state.chPos--;
                        if (state.chPos < 0) {
                            if (tmpNode.hasExtraData()) {
                                // get index of an array of inflection patterns.
                                addResult(result, ipm, tmpNode, null, PERFECT_MATCHING_WEIGHT, null, po, perfectMatching);
                                perfectMatching = true;
                            }
                            // end of the word - check if this is a base form
                            if (useBaseFormsDictionary) {
                                tmpNode = tmpNode.getChild(BASE_FORM_TAG_INTEGER, intStack, 0);
                                if (tmpNode != null) {
                                    // get index of an array of inflection patterns.
                                    addResult(result, ipm, tmpNode, str, PERFECT_MATCHING_WEIGHT, null, po, perfectMatching);
                                    perfectMatching = true;
                                }
                            }
                            finish = true;
                        } else {
                            state.c = str.charAt(state.chPos);
                            state.cint = (int) state.c;
                            state.node = tmpNode;
                        }
                    } else {
                        finish = true;
                    }

                    break;

                case STATE_MATCH_CORE:

                    MATCH_CORE_KEYS[4] = state.cint;
                    state.node.findChildren(MATCH_CORE_KEYS, foundChildren, intStack, 0);
                    
                    if (state.chPos >=0) {
                        // match vowel or consonant
                        if (vch.isVowel(state.c)) {
                            //tmpNode = (LinkedListTreeNode) state.node.getChild(VOWEL_INTEGER, intStack,0);
                            tmpNode = foundChildren[1];
                        } else {
                            //tmpNode = (LinkedListTreeNode) state.node.getChild(CONSONANT_INTEGER, intStack, 0);
                            tmpNode = foundChildren[0];
                        }
                        if (tmpNode != null) {
                            ps = stack[stackPos++];
                            ps.set(state);
                            ps.chPos--;
                            if (ps.chPos >= 0) {
                                ps.c = str.charAt(ps.chPos);
                                ps.cint = (int) ps.c;
                                ps.node = tmpNode;
                            } else {
                                ps.node = tmpNode;
                                ps.c = 0;
                                ps.cint = 0;
                            }
                        }
                    } else {
                        tmpNode = null;
                    }

                    // check wildcard
                    //tmpNode = (LinkedListTreeNode) state.node.getChild(WILDCARD_INTEGER, intStack, 0);
                    tmpNode = foundChildren[2];
                    
                    if (tmpNode != null) {
                        ps = stack[stackPos++];
                        ps.stateType = STATE_MATCH_PREFIX;
                        ps.node = tmpNode;
                        ps.chPos = 0;
                        ps.c = str.charAt(0);
                        ps.cint = (int) ps.c;
                        ps.suffixPos = state.suffixPos;
                        ps.corePos = state.chPos + 1;
                    }

                    // check core tag
                    //tmpNode = (LinkedListTreeNode) state.node.getChild(CORE_TAG_INTEGER, intStack, 0);
                    tmpNode = foundChildren[3];
                    
                    
                    if (tmpNode != null) {
                        ps = stack[stackPos++];
                        ps.stateType = STATE_MATCH_REMAINING_CHARACTERS;
                        ps.node = tmpNode;
                        ps.chPos = state.chPos;
                        ps.c = state.c;
                        ps.cint = state.cint;
                        ps.suffixPos = state.suffixPos;
                        ps.corePos = state.chPos + 1;
                    }

                    // match character
                    //tmpNode = (LinkedListTreeNode) state.node.getChild(state.cint, intStack, 0);
                    tmpNode = foundChildren[4];
                    
                    
                    if (tmpNode == null && startsFromUppercase && state.chPos == 0) {
                        tmpNode = state.node.getChild(firtCharCode, intStack, 0);
                    }
                    if (tmpNode != null) {
                        state.chPos--;
                        if (state.chPos >= 0) {
                            state.c = str.charAt(state.chPos);
                            state.cint = (int) state.c;
                            state.node = tmpNode;
                        } else {
                            state.node = tmpNode;
                            state.c = 0;
                            state.cint = 0;
                        }
                    } else {
                        finish = true;
                    }
                    break;
                case STATE_MATCH_REMAINING_CHARACTERS:
                    while (state.chPos >= 0 && state.node != null) {
                        LinkedListTreeNode tnode = state.node;
                        state.node = tnode.getChild(state.cint, intStack, 0);
                        if (state.node == null && startsFromUppercase && state.chPos == 0) {
                            state.node = tnode.getChild(firtCharCode, intStack, 0);
                        }
                        state.chPos--;
                        if (state.chPos >= 0) {
                            state.c = str.charAt(state.chPos);
                            state.cint = (int) state.c;
                        }
                    }
                    if (state.node != null && state.chPos < 0 && state.node.hasExtraData()) {
                        // get index of an array of inflection patterns.
                        core.setLength(0);
                        core.append(str, state.corePos, state.suffixPos);
                        core.append(WILDCARD_CHARACTER);
                        addResult(result, ipm, state.node, null, PERFECT_MATCHING_WEIGHT, core.toString(), po, perfectMatching);
                        perfectMatching = true;
                    }
                    finish = true;
                    break;
                case STATE_MATCH_PREFIX:
                    if (state.node.hasExtraData()) {
                        // get index of an array of inflection patterns.
                        int ipaIndex = ((Integer) ((TreeNodeWithData) state.node).getData()).intValue();
                        core.setLength(0);
                        if (state.chPos > 0) {
                            core.append(WILDCARD_CHARACTER);
                            core.append(str, state.chPos, state.suffixPos);
                            core.append(WILDCARD_CHARACTER);
                        } else {
                            core.append(str, 0, state.suffixPos);
                            core.append(WILDCARD_CHARACTER);
                        }
                        
                        double weight = weightMultipler * 
                                + ((str.length() - state.suffixPos + state.chPos) * supplementWeight
                                + (state.suffixPos - state.corePos) * corePatternWeight);

                        perfectMatching |= addResult(result, ipm, state.node, null, -weight, core.toString(), po, perfectMatching);
                        
                        
/*                        
                        for (int i = 0; i < ipa.length; i++) {
                            ExtendedInflectionPattern ip = ipa[i];
                            String baseForm = makeForm(core, ip.getBaseFormPattern().getAffixes());
                            
                            double weight = checkInBaseFormDictionary(baseForm, ip.getId(), po);
                            if (weight != PERFECT_MATCHING_WEIGHT) {
                                // calculate weight
                                weight = weightMultipler
                                    * (ip.getNumberOfCoveredLexemes() * ipWeight
                                    + (str.length() - state.suffixPos + state.chPos) * supplementWeight
                                    + (state.suffixPos - state.corePos) * corePatternWeight);
                                
                            } else {
                                perfectMatching = true;
                            }
                            
                            if (!perfectMatching || weight == PERFECT_MATCHING_WEIGHT) {
                                // add a single result to the list of result forms.
                                ExtendedMorphologicalAnalysisResult res = new ExtendedMorphologicalAnalysisResult(
                                        baseForm,
                                        weight,
                                        core,
                                        affixes,
                                        null,
                                        ip);
                                result.add(res);
                            }
                        }
                        */
                    }
                    // check succeding character
                    if (state.chPos >= state.corePos || state.chPos >= state.suffixPos) {
                        finish = true;
                    } else {
                        tmpNode = state.node.getChild(state.cint, intStack, 0);
                        if (tmpNode == null && startsFromUppercase && state.chPos == 0) {
                            tmpNode = state.node.getChild(firtCharCode, intStack, 0);
                        }
                        if (tmpNode != null) {
                            state.chPos++;
                            state.c = str.charAt(state.chPos);
                            state.cint = (int) state.c;
                            state.node = tmpNode;
                        } else {
                            finish = true;
                        }
                    }
                    break;
                case STATE_CHECK_CREATED_FORM:
                    break;
                default:
                    throw new IllegalStateException(
                            "Invalid morphological analysis state : " + state);
            }
            if (finish && stackPos > 0) {
                stackPos--;
                state.set(stack[stackPos]);
                finish = false;
            }
        }

        postProcessResult(str, result, perfectMatching);
        

        processingObjects.push(po);

        result = new ArrayList(result);
        cache.put(inputString, result);
        return result;
    }
    
    private boolean addResult(
            ArrayList<ExtendedMorphologicalAnalysisResult> result, 
            InflectionPatternsMap ipm,
            LinkedListTreeNode tmpNode, 
            String baseForm, 
            final double weight, 
            String core,
            MorphologicalAnalyserProcessingObject po,
            boolean perfectMatching) {
        boolean perfectWeight = perfectMatching;
        int ipaIndex = ((Integer) ((TreeNodeWithData) tmpNode).getData());
        ExtendedInflectionPattern[] ipa = ipm.get(ipaIndex);
        double weight2 = weight;
        for (ExtendedInflectionPattern ip : ipa) {
            FormPattern fp = ip.getBaseFormPattern();
            String resultForm = baseForm != null ? baseForm : 
                    (core == null ? fp.getAffixes() : makeForm(core, fp.getAffixes()));
            if (weight <= 0) {
                if (checkInBaseFormDictionary(resultForm, ip.getId(), po) == PERFECT_MATCHING_WEIGHT) {
                    weight2 = PERFECT_MATCHING_WEIGHT;
                } else {
                    weight2 = weightMultipler * ip.getNumberOfCoveredLexemes() * ipWeight - weight;
                }
            }

            perfectWeight |= weight2 == PERFECT_MATCHING_WEIGHT;
            
            if (!perfectWeight || (weight2 == PERFECT_MATCHING_WEIGHT)) {
            
                ExtendedMorphologicalAnalysisResult mar = new ExtendedMorphologicalAnalysisResult(
                        resultForm,
                        weight2,
                        core,
                        fp.getAffixes(),
                        fp.getGrammaticalPropertiesList(),
                        ip);
                result.add(mar);
            }
            
        }
        return perfectWeight;
    }
    

    private double checkInBaseFormDictionary(String baseForm, int ipId, MorphologicalAnalyserProcessingObject processingObject) {
        // check if the base form exists in well known forms dictionary
        if (useBaseFormsDictionary) {
            Object matchingData;
            Map checkedForms = processingObject.checkedForms;
            if (checkedForms.containsKey(baseForm)) {
                matchingData = checkedForms.get(baseForm);
            } else // check in bloom filter
            if (baseFormsBloomFilter == null || baseFormsBloomFilter.contains(
                    baseForm) || (processingObject.startsFromUppercase && baseFormsBloomFilter.
                    contains(baseForm.toLowerCase()))) {
                // base form found by bloom filter, 
                // possible false positive - check in tree
                StringBuilder buffer = new StringBuilder(30);
                buffer.append(BASE_FORM_TAG);
                buffer.append(baseForm);
                buffer.reverse();
                matchingData = ((LinkedListTreeNode) tree.getRoot()).getData(
                        buffer.toString(), processingObject.intStack, 0);
                if (matchingData == null && processingObject.startsFromUppercase) {
                    buffer.setCharAt(buffer.length() - 2, processingObject.lowercasedFirstCharacter);
                    matchingData = ((LinkedListTreeNode) tree.getRoot()).
                            getData(buffer.toString(), processingObject.intStack, 0);
                }
                checkedForms.put(baseForm, matchingData);
            } else {
                checkedForms.put(baseForm, null);
                matchingData = null;
            }
            if (matchingData != null) {
                int ipaIndex2 = ((Integer) matchingData).intValue();
                ExtendedInflectionPattern[] ipa2 = ipb.getInflectionPatternsMap().get(ipaIndex2);
                for (int k = ipa2.length - 1; k >= 0; k--) {
                    if (ipId == ipa2[k].getId()) {
                        return PERFECT_MATCHING_WEIGHT;
                    }
                }
            }
        }
        return 0;
    }
    
    public void clearCache() {
        cache.clear();
    }

    public LinkedListTree getTree() {
        return tree;
    }

    public void setTree(LinkedListTree tree) {
        this.tree = tree;
    }

    public InflectionPatternsBase getIpb() {
        return ipb;
    }

    public void setIpb(InflectionPatternsBase ipb) {
        this.ipb = ipb;
    }

    public VowelCharactersImpl getVowelCharacters() {
        if (vowelCharacters == null) {
            vowelCharacters = new VowelCharactersImpl(ipb.getLanguage(), charset);
        }
        return vowelCharacters;
    }

    private void writeFrequentFormsCache(DataOutputStream out) throws IOException {
        int size = frequentFormsCache == null ? 0 : frequentFormsCache.size();
        DataOutputStreamPacker.writeInt(size, out);
        if (frequentFormsCache != null) {
            for (final Iterator it = frequentFormsCache.entrySet().iterator(); it.hasNext();) {
                Map.Entry entry = (Map.Entry) it.next();
                String form = (String) entry.getKey();
                List resultList = (List) entry.getValue();
                DataOutputStreamPacker.writeString(form, out);
                DataOutputStreamPacker.writeInt(resultList.size(), out);
                for (final Iterator it2 = resultList.iterator(); it2.hasNext();) {
                    MorphologicalAnalysisResult mar = (MorphologicalAnalysisResult) it2.next();
                    mar.write(out);
                }

            }
        }
    }

    public final static int DATA_FORMAT_VERSION = 1;
    public final static String DATA_FORMAT_PREFIX = "neurpheus-morphological-analyser";

    public void write(final DataOutputStream out) throws IOException {
        byte[] tmp = DATA_FORMAT_PREFIX.getBytes("US-ASCII");
        out.writeByte(tmp.length);
        out.write(tmp);
        out.writeInt(DATA_FORMAT_VERSION);
        
        // charset.write(out);

        out.writeInt(maxIndexOfNeuralInflectionPattern);
        out.writeDouble(supplementWeight);
        out.writeDouble(corePatternWeight);
        out.writeDouble(ipWeight);
        out.writeInt(maxIPWeight);
        out.writeInt(maxCorePatternLength);
        out.writeInt(maxSupplementLength);
        out.writeBoolean(tree != null);
        if (tree != null) {
            tree.write(out);
        }
        out.writeBoolean(ipb != null);
        if (ipb != null) {
            ipb.write(out);
        }
        out.writeBoolean(baseFormsBloomFilter != null);
        if (baseFormsBloomFilter != null) {
            baseFormsBloomFilter.write(out);
        }
        out.writeBoolean(neuralNetwork != null);
        if (neuralNetwork != null) {
            neuralNetwork.write(out);
        }
        out.writeBoolean(frequentFormsCache != null);
        if (frequentFormsCache != null) {
            writeFrequentFormsCache(out);
        }
    }

    private void writeObject(final ObjectOutputStream out) throws IOException,
                                                                  ClassNotFoundException {
        DataOutputStream dout = new DataOutputStream(out);
        write(dout);
    }

    private void readFrequentFormsCache(DataInputStream in) throws IOException {
        int size = DataOutputStreamPacker.readInt(in);
        frequentFormsCache = new HashMap(size + 1, 1.0f);
        for (int i = 0; i < size; i++) {
            String form = DataOutputStreamPacker.readString(in);
            int resultSize = DataOutputStreamPacker.readInt(in);
            List resultList;
            if (resultSize == 1) {
                MorphologicalAnalysisResultImpl mar = new MorphologicalAnalysisResultImpl();
                mar.read(in);
                resultList = Collections.singletonList(mar);
            } else {
                resultList = new ArrayList(resultSize);
                for (int j = 0; j < resultSize; j++) {
                    MorphologicalAnalysisResultImpl mar = new MorphologicalAnalysisResultImpl();
                    mar.read(in);
                    resultList.add(mar);
                }

            }
            frequentFormsCache.put(form, resultList);
        }
    }

    public void read(final DataInputStream in) throws IOException {
        processingObjects = new ConcurrentLinkedDeque<>();
        int len = in.readByte();
        byte[] tmp = new byte[len];
        in.readFully(tmp);
        String prefix = new String(tmp, "US-ASCII");
        if (!prefix.equals(DATA_FORMAT_PREFIX)) {
            throw new IOException("Invalid data format prefix: " + prefix);
        }
        int version = in.readInt();
        if (version != DATA_FORMAT_VERSION) {
            throw new IOException("Invalid data format version: " + version);
        }

        maxIndexOfNeuralInflectionPattern = in.readInt();
        supplementWeight = in.readDouble();
        corePatternWeight = in.readDouble();
        ipWeight = in.readDouble();
        maxIPWeight = in.readInt();
        maxCorePatternLength = in.readInt();
        maxSupplementLength = in.readInt();
        tree = null;
        if (in.readBoolean()) {
            tree = LinkedListTreeFactory.getInstance().createTree();
            tree.read(in);
        }
        if (in.readBoolean()) {
            ipb = new InflectionPatternsBase();
            ipb.read(in);
        }
        if (in.readBoolean()) {
            baseFormsBloomFilter = new BloomFilter();
            baseFormsBloomFilter.read(in);
        }

        if (in.readBoolean()) {
            // neuralNetwork.write(out);
            // TODO
        }

        if (in.readBoolean()) {
            synchronized (TagsetImpl.getDeserializationLockObject()) {
                TagsetImpl.setDeserializationTagset(ipb.getTagset());
                long startTime = System.currentTimeMillis();
                DataInputStream din = new DataInputStream(in);
                readFrequentFormsCache(din);
                //frequentFormsCache = (HashMap) in.readObject();
                logger.info(
                        "Frequently forms cache read in " + (System.currentTimeMillis() - startTime) + " ms.");
                // TODO - test memory
                // TODO - test memory
                //frequentFormsCache.clear();
                // TODO - test memory
                // TODO - test memory
                TagsetImpl.setDeserializationTagset(null);
            }
        } else {
            frequentFormsCache = new HashMap();
        }

        weightMultipler
                = 0.9 * PERFECT_MATCHING_WEIGHT / (double) (maxCorePatternLength * corePatternWeight
                + maxSupplementLength * supplementWeight
                + maxIPWeight * ipWeight);
        useBaseFormsDictionary = true;
        NeuralNetwork ann = getNeuralNetwork();
        setUseNeuralNetwork(false);
        if (ann != null) {
            try {
                ann.initialize();
                setUseNeuralNetwork(true);
            } catch (ClassificationException e) {
                logger.log(Level.SEVERE, "Cannot initialize neural network.", e);
            }
        }
        cache = new LRUCache(CACHE_SIZE);
        setInitialized(true);

//        // list morphemes
//        HashSet set = new HashSet();
//        for (Iterator it = ipb.getInflectionPatterns().iterator(); it.hasNext();){
//            InflectionPatternImpl ip = (InflectionPatternImpl) it.next();
//            FormPattern fp = ip.getBaseFormPattern();
//            set.add(fp.getAffixes());
//            for (Iterator it2 = ip.getOtherFormPatterns().iterator(); it2.hasNext();) {
//                fp = (FormPattern) it2.next();
//                set.add(fp.getAffixes());
//            }
//        }
//        ArrayList list = new ArrayList(set);
//        Collections.sort(list);
//        logger.info("NUMBER OF DIFFERENT AFFIXES:" + list.size());
//        StringBuffer buffer = new StringBuffer();
//        for (Iterator it = list.iterator(); it.hasNext();) {
//            buffer.append(it.next().toString());
//            buffer.append(' ');
//        }
//        logger.info("NUMBER OF CHARACTERS:" + buffer.length());
    }

    /**
     * Reads this object data from the given input stream.
     *
     * @param in The input stream where this IPB is stored.
     *
     * @throws IOException            if any read error occurred.
     * @throws ClassNotFoundException if this object cannot be instantied.
     */
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        DataInputStream din = new DataInputStream(in);
        charset = new DynamicCharset("international", new String[0]);
        charset.setInternational();
        read(din);
    }

    public BloomFilter getBaseFormsBloomFilter() {
        return baseFormsBloomFilter;
    }

    public void setBaseFormsBloomFilter(BloomFilter baseFormsBloomFilter) {
        this.baseFormsBloomFilter = baseFormsBloomFilter;
    }

    public boolean isUseBaseFormsDictionary() {
        return useBaseFormsDictionary;
    }

    public void setUseBaseFormsDictionary(boolean useBaseFormsDictionary) {
        this.useBaseFormsDictionary = useBaseFormsDictionary;
    }

    public int getMaxIndexOfNeuralInflectionPattern() {
        return maxIndexOfNeuralInflectionPattern;
    }

    public void setMaxIndexOfNeuralInflectionPattern(int maxIndexOfNeuralInflectionPattern) {
        this.maxIndexOfNeuralInflectionPattern = maxIndexOfNeuralInflectionPattern;
    }

    public boolean isUseNeuralNetwork() {
        return useNeuralNetwork;
    }

    public void setUseNeuralNetwork(boolean useNeuralNetwork) {
        this.useNeuralNetwork = useNeuralNetwork;
    }

    public NeuralNetwork getNeuralNetwork() {
        return neuralNetwork;
    }

    public void setNeuralNetwork(NeuralNetwork neuralNetwork) {
        this.neuralNetwork = neuralNetwork;
    }

    public int getMaxIPWeight() {
        return maxIPWeight;
    }

    public void setMaxIPWeight(int maxIPWeight) {
        this.maxIPWeight = maxIPWeight;
    }

    public int getMaxCorePatternLength() {
        return maxCorePatternLength;
    }

    public void setMaxCorePatternLength(int maxCorePatternLength) {
        this.maxCorePatternLength = maxCorePatternLength;
    }

    public int getMaxSupplementLength() {
        return maxSupplementLength;
    }

    public void setMaxSupplementLength(int maxSupplementLength) {
        this.maxSupplementLength = maxSupplementLength;
    }

    public double getSupplementWeight() {
        return supplementWeight;
    }

    public void setSupplementWeight(double supplementWeight) {
        this.supplementWeight = supplementWeight;
    }

    public double getCorePatternWeight() {
        return corePatternWeight;
    }

    public void setCorePatternWeight(double corePatternWeight) {
        this.corePatternWeight = corePatternWeight;
    }

    public double getIpWeight() {
        return ipWeight;
    }

    public void setIpWeight(double ipWeight) {
        this.ipWeight = ipWeight;
    }

    /**
     * Generates a word form described by the given grammatical properties.
     *
     * @param baseForm                    The base form of a word (lemma of a lexeme).
     *
     * @param targetGrammaticalProperties The object representing the grammatical properties of the
     *                                    target word form.
     *
     * @return The generated form.
     *
     * @throws org.neurpheus.nlp.morphology.MorphologyException if a serious error occurred during
     *                                                          the generation.
     */
    @Override
    public String generateWordForm(String baseForm,
                                   GrammaticalProperties targetGrammaticalProperties)
            throws MorphologyException {
        List result = analyse2list(baseForm, true);
        for (final Iterator it = result.iterator(); it.hasNext();) {
            ExtendedMorphologicalAnalysisResult mar = (ExtendedMorphologicalAnalysisResult) it.
                    next();
            if (baseForm.equals(mar.getForm())) {
                ExtendedInflectionPattern ip = mar.getInflectionPattern();
                if (ip != null) {
                    GrammaticalPropertiesList gpl = ip.getBaseFormPattern().
                            getGrammaticalPropertiesList();
                    if (gpl.covers(targetGrammaticalProperties)) {
                        return baseForm;
                    } else {
                        for (final Iterator fit = ip.getOtherFormPatterns().iterator(); fit.
                                hasNext();) {
                            FormPattern fp = (FormPattern) fit.next();
                            gpl = fp.getGrammaticalPropertiesList();
                            if (gpl.covers(targetGrammaticalProperties)) {
                                String res = makeForm(mar.getCore(), fp.getAffixes());
                                return res;
                            }
                        }
                    }
                }
            }
        }
        return baseForm;
    }

    @Override
    public String generateWordForm(String baseForm, String targetGrammaticalProperties) throws
            MorphologyException {
        GrammaticalProperties gp = getTagset().getGrammaticalProperties(targetGrammaticalProperties);
        return generateWordForm(baseForm, gp);
    }

    private final static MorphologicalAnalysisResult[] EMPTY_ARRAY = new MorphologicalAnalysisResult[0];
            
    @Override
    public final MorphologicalAnalysisResult[] analyse(String wordForm) throws MorphologyException {
        List list = analyse2list(wordForm);
        return (MorphologicalAnalysisResult[]) list.toArray(EMPTY_ARRAY);
    }

    private transient int quality = QualityConst.VERY_GOOD;
    private transient int speed = SpeedConst.MEDIUM;

    @Override
    public int getQuality() {
        return quality;
    }

    @Override
    public int getSpeed() {
        return speed;
    }

    private static List getMorphologicalAnalysers(Enumeration en) throws MorphologyException {
        ArrayList result = new ArrayList();
        while (en.hasMoreElements()) {
            URL url = (URL) en.nextElement();
            InputStream in = null;
            try {
                in = url.openStream();
                MorphologicalAnalysers analysers = MorphologicalAnalysers.read(in);
                if (analysers != null) {
                    MorphologicalAnalyserInfo[] tab = analysers.getMorphologicalAnalyserInfo();
                    for (int i = 0; i < tab.length; i++) {
                        result.add(tab[i]);
                    }
                }
            } catch (Exception e) {
                throw new MorphologyException("Cannot read morphological analysers definition file",
                                              e);
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        logger.log(Level.WARNING, "Cannot close stream", e);
                    }
                }
            }
        }
        return result;
    }

    private static void initInstances() {
        instances = new HashMap();
        String resourcePath = PATH_MORPHOLOGICAL_ANALYSERS_INFO;
        try {
            Enumeration en = MorphologicalAnalyserImpl.class.getClassLoader().getResources(
                    resourcePath);
            ArrayList infos = new ArrayList(getMorphologicalAnalysers(en));
            en = ClassLoader.getSystemResources(resourcePath);
            infos.addAll(getMorphologicalAnalysers(en));
            for (final Iterator it = infos.iterator(); it.hasNext();) {
                MorphologicalAnalyserInfo info = (MorphologicalAnalyserInfo) it.next();
                MorphologicalAnalyserImpl instance = new MorphologicalAnalyserImpl(info);
                for (final Iterator lit = instance.getSupportedLocales().iterator(); lit.hasNext();) {
                    Locale loc = (Locale) lit.next();
                    instances.put(loc, instance);
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Cannot read definition of morphological analysers.", e);
        }
    }

    static {
        initInstances();
    }

    @Override
    public MorphologicalComponent getInstance(Locale locale) throws MorphologyException {
        MorphologicalComponent res = (MorphologicalComponent) instances.get(locale);
        if (res == null) {
            throw new MorphologyException("Cannot find component for locale " + locale.toString());
        }
        return res;
    }

    /** Creates a new instance of MorphologicalAnalyserImpl */
    public MorphologicalAnalyserImpl(MorphologicalAnalyserInfo info) {
        charset = new DynamicCharset("international", new String[0]);
        charset.setInternational();
        cache = new LRUCache(CACHE_SIZE);
        useBaseFormsDictionary = true;
        setUseNeuralNetwork(true);
        processingObjects = new ConcurrentLinkedDeque<>();
        setProperty(PropertyConst.AUTHORS, info.getAuthors());
        setProperty(PropertyConst.BUILD_DATE, info.getBuildData());
        setProperty(PropertyConst.COPYRIGHT, info.getCopyright());
        setProperty(PropertyConst.DESCRIPTION, info.getDescription());
        setProperty(PropertyConst.LICENCE, info.getLicence());
        setProperty(PropertyConst.NAME, info.getName());
        setProperty(PropertyConst.VENDOR, info.getVendor());
        setProperty(PropertyConst.VERSION, info.getVersion());
        setProperty(PropertyConst.WEB_PAGE, info.getWebPage());
        setProperty(PROPERTY_IS_TAGGED, Boolean.valueOf(info.getTagged()));
        setProperty(PROPERTY_DATA_PATH, info.getDataPath());

        String[] localesArray = info.getSupportedLocales().getSupportedlocale();
        ArrayList locales = new ArrayList(localesArray.length);
        for (int i = 0; i < localesArray.length; i++) {
            Locale l = LocaleHelper.toLocale(localesArray[i]);
            locales.add(l);
        }
        setSupportedLocales(locales);
        try {
            quality = Integer.parseInt(info.getQuality());
        } catch (NumberFormatException e) {
            logger.log(Level.WARNING, "Cannot parse quality - using default value", e);
        }
        try {
            speed = Integer.parseInt(info.getSpeed());
        } catch (NumberFormatException e) {
            logger.log(Level.WARNING, "Cannot parse speed- using default value", e);
        }

    }

    public void init(MorphologicalAnalyserImpl mai) throws MorphologyException {
        tree = mai.tree;
        ipb = mai.ipb;
        baseFormsBloomFilter = mai.baseFormsBloomFilter;
        neuralNetwork = mai.neuralNetwork;
        maxIndexOfNeuralInflectionPattern = mai.maxIndexOfNeuralInflectionPattern;
        supplementWeight = mai.supplementWeight;
        corePatternWeight = mai.corePatternWeight;
        ipWeight = mai.ipWeight;
        maxIPWeight = mai.maxIPWeight;
        maxCorePatternLength = mai.maxCorePatternLength;
        maxSupplementLength = mai.maxSupplementLength;
        useNeuralNetwork = mai.useNeuralNetwork;
        vowelCharacters = mai.vowelCharacters;
        useBaseFormsDictionary = mai.useBaseFormsDictionary;
        weightMultipler = mai.weightMultipler;
        frequentFormsCache = mai.frequentFormsCache;
    }

    /**
     * Forces an initialisation of this component. The component should initialise itself
     * automaticly before processing, but a client may use this method to perform the initialisation
     * in particular conditions (for example when the system starts up).
     *
     * @throws org.neurpheus.nlp.morphology.MorphologyException if the component cannot be
     *                                                          initialised.
     */
    @Override
    public void init() throws MorphologyException {
        if (isInitialized()) {
            return;
        }
        String dataPath = (String) getProperty(PROPERTY_DATA_PATH);
        if (dataPath == null) {
            throw new MorphologyException("Path to the morphological analyser data is not specified");
        }
        InputStream in = null;
        DataInputStream din = null;
        try {
            in = getClass().getClassLoader().getResourceAsStream(dataPath);
            if (in == null) {
                in = ClassLoader.getSystemResourceAsStream(dataPath);
            }
            if (in == null) {
                throw new MorphologyException(
                        "Cannot read data for morphological analyser from resource" + dataPath);
            }
            din = new DataInputStream(new BufferedInputStream(in, 256 * 1024));
            //try {
            read(din);
            //init(mai);
            //} catch (Exception e) {
            //    throw new MorphologyException("Invalid format of data for morphological analyser.", e);
            //}
        } catch (IOException e) {
            throw new MorphologyException("Cannot read data for morphological analyser.", e);
        } finally {
            if (din != null) {
                try {
                    din.close();
                } catch (IOException e) {
                    logger.log(Level.WARNING, "Cannot close stream.", e);
                }
            }
        }
        super.init();
    }

    public static void main(String[] args) {
        try {
            MorphologyFactory factory = DefaultMorphologyFactory.getInstance();
            MorphologicalAnalyser analyser;
            analyser = factory.
                    getMorphologicalAnalyser(new Locale("pl", "PL"), PropertyConst.VENDOR,
                                             "Neurpheus");
            long duration = System.currentTimeMillis();
            analyser.init();
            duration = System.currentTimeMillis() - duration;
            System.out.println("Initialization time = " + duration + " ms.");
            MorphologicalAnalysisResult[] result = analyser.analyse("testowa");
            for (int i = 0; i < result.length; i++) {
                System.out.println(result[i].toString());
            }
        } catch (MorphologyException ex) {
            logger.log(Level.SEVERE, "cannot perform analysis",  ex);
        }
    }

    private void postProcessResult(String str, ArrayList result, boolean perfectMatching) {
        if (result.size() > 0) {
            if (perfectMatching) {
                for (final Iterator it = result.iterator(); it.hasNext();) {
                    ExtendedMorphologicalAnalysisResult res = (ExtendedMorphologicalAnalysisResult) it.
                            next();
                    if (res.getAccuracy() < PERFECT_MATCHING_WEIGHT) {
                        it.remove();
                    }
                }
            } else if (result.size() > 1) {
                if (isUseNeuralNetwork() && getNeuralNetwork() != null) {
                    double[] inputs = getNeuralNetwork().getInputs();
                    Arrays.fill(inputs, 0);
                    int maxIPId = getMaxIndexOfNeuralInflectionPattern();
                    double extraNeuronWeight = 0;
                    for (final Iterator it = result.iterator(); it.hasNext();) {
                        ExtendedMorphologicalAnalysisResult res = (ExtendedMorphologicalAnalysisResult) it.
                                next();
                        ExtendedInflectionPattern ip = res.getInflectionPattern();
                        int id = ip.getId();
                        if (id > maxIPId) {
                            extraNeuronWeight += res.getAccuracy();
                        } else {
                            inputs[id] = res.getAccuracy();
                        }
//                        ExtendedInflectionPattern[] ipa = res.getIpa();
//                        for (int i = 0; i < ipa.length; i++) {
//                            int id = ipa[i].getId();
//                            if (id > maxIPId) {
//                                extraNeuronWeight += res.getAccuracy();
//                            } else {
//                                inputs[id] = res.getAccuracy();
//                            }
//                        }
                    }
                    inputs[maxIPId + 1] = extraNeuronWeight;
                    for (int i = str.length() - 1; i >= 0; i--) {
                        int c = str.charAt(i) & 0x1F;
                        inputs[maxIPId + 2 + c] += 0.2;
                    }

                    try {
                        getNeuralNetwork().classify(inputs);
                        double[] weights = getNeuralNetwork().getOutputs();
                        //neuralNetwork.normalizeResult(weights);
                        // check if extra neuron wins
                        double maxValue = weights[maxIPId + 1];
                        boolean extraNeuronWins = true;
                        for (int i = weights.length - 2; extraNeuronWins && i >= 0; i--) {
                            if (inputs[i] > 0 && weights[i] >= maxValue) {
                                extraNeuronWins = false;
                            }
                        }
//                        ArrayList nnResult = new ArrayList();
                        for (final Iterator it = result.iterator(); it.hasNext();) {
                            ExtendedMorphologicalAnalysisResult res = (ExtendedMorphologicalAnalysisResult) it.
                                    next();
                            ExtendedInflectionPattern ip = res.getInflectionPattern();
                            int id = ip.getId();
                            double newWeight;
                            if (extraNeuronWins) {
                                newWeight = id > maxIPId ? 0.5 * res.getAccuracy() + 0.5 : 0.499 * weights[id] + 0.001 * res.
                                        getAccuracy();
                            } else {
                                newWeight = id > maxIPId ? 0.5 * res.getAccuracy() : 0.5 + 0.499 * weights[id] + 0.001 * res.
                                        getAccuracy();
                            }
                            res.setAccuracy(newWeight);
//                            ExtendedInflectionPattern[] ipa = res.getIpa();
//                            for (int i = 0; i < ipa.length; i++) {
//                                int id = ipa[i].getId();
//                                ExtendedInflectionPattern[] newIPA = new ExtendedInflectionPattern[1];
//                                newIPA[0] = ipa[i];
//                                double newWeight;
//                                if (extraNeuronWins) {
//                                    newWeight = id > maxIPId ? 0.5 * res.getAccuracy() + 0.5 : 0.5 * weights[id];
//                                } else {
//                                    newWeight = id > maxIPId ? 0.5 * res.getAccuracy() : 0.5 * weights[id] + 0.5;
//                                }
//                                ExtendedMorphologicalAnalysisResult newRes = new ExtendedMorphologicalAnalysisResult(res.getForm(), newWeight, res.getCore(), "", null, newIPA);
//                                nnResult.add(newRes);
//                            }
                        }
//                        result.clear();
//                        result = nnResult;
                    } catch (ClassificationException e) {
                        logger.log(Level.SEVERE, "Cannot clasify inflection patterns using neural network.", e);
                    }
                }
                Collections.sort(result);
            }
        }
        if (result.size() == 0) {
            ExtendedMorphologicalAnalysisResult res = new ExtendedMorphologicalAnalysisResult(
                    str,
                    PERFECT_MATCHING_WEIGHT,
                    str,
                    "",
                    getTagset().getUnknownGrammaticalPropertiesList(),
                    null);
            result.add(res);
        } else if (result.size() == 1) {
            ExtendedMorphologicalAnalysisResult res = (ExtendedMorphologicalAnalysisResult) result.
                    get(0);
            res.setAccuracy(PERFECT_MATCHING_WEIGHT);
//        } else {
//            // normalize weights
//            long weightsSum = 0;
//            for (final Iterator it = result.iterator(); it.hasNext();) {
//                MorphologicalAnalysisResultImpl res = (MorphologicalAnalysisResultImpl) it.next();
//                weightsSum += res.getWeight();
//            }
//            for (final Iterator it = result.iterator(); it.hasNext();) {
//                MorphologicalAnalysisResultImpl res = (MorphologicalAnalysisResultImpl) it.next();
//                res.setWeight(100 * res.getWeight() / weightsSum);
//            }
        }
        if (!perfectMatching && str.length() <= 3 && !((ExtendedMorphologicalAnalysisResult) result.
                get(0)).getForm().equals(str)) {
            ExtendedMorphologicalAnalysisResult res = new ExtendedMorphologicalAnalysisResult(
                    str,
                    PERFECT_MATCHING_WEIGHT,
                    str,
                    "",
                    getTagset().getUnknownGrammaticalPropertiesList(),
                    null);
            result.add(0, res);
        }
    }

}
