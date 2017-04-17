/*
 *  © 2015 Jakub Strychowski
 */

package org.neurpheus.nlp.morphology.builder;

import java.util.Locale;

/**
 *
 * @author Jakub Strychowski
 */
public class MorphologicalAnalyserBuilderParameters {

    /** The path to the directory where MySpell and output files are stored. */
    public static String dictionariesPath = "C:\\projekty\\neurpheus\\data\\dictionaries\\";

    /** Symbols of dictionaries to process. */
    public static String[] dictionaries = {"test_pl_PL"};

    /** Processed languages. */
    public static Locale[] languages = {new Locale("pl", "PL")};

    /**
     * Holds the minimal numer of lexemes which should be covered by an inflection pattern processed
     * by the neural network. Inflection patterns which covers less then this value are ignored by
     * the neural network.
     */
    public static final int MIN_LEXEMES_COUNT_FOR_NEURAL_IP = 10;


    /** The extension of a file where full dictionary is stored. */
    public static final String EXTENSION_FULL_DICTIONARY = ".all";

    public static final String EXTENSION_DICTIONARY = ".dic";

    public static final String EXTENSION_FULL_DICTIONARY_ADD = "_toadd.txt";

    public static final String EXTENSION_FULL_DICTIONARY_REMOVE = "_toremove.txt";
    

    /** The extension of a file where inflection pattern base is stored. */
    public static final String EXTENSION_IPB = ".ipb";

    /** The extension of a file where inflection pattern base is stored. */
    public static final String EXTENSION_IPB_INTEGRATED = ".small.ipb";

    /** The extension of a file where tagset is. */
    public static final String EXTENSION_TAGSET = ".tags";

    /** The extension of a file which provided info about tagging file. */
    public static final String EXTENSION_TAGGING_INFO = "_tagging.xml";

    /** The extension of a file which provided example tagging of forms. */
    public static final String EXTENSION_TAGGING_FILE = "_tagging.txt";

    /** The extension of a file where inflection pattern base is stored as xml. */
    public static final String EXTENSION_IPB_OUT_XML = ".ipb.out.xml";

    /** The extension of a file from which system reads tags of inflections patterns. */
    public static final String EXTENSION_IPB_IN_XML = ".ipb.in.xml";

    /** The extension of a file where inflection pattern base is logged out. */
    public static final String EXTENSION_IPB_LOG = ".ipb.log";

    /** The extension of a file where base forms dictionary is stored. */
    public static final String EXTENSION_BASE_FORMS_DICTIONARY = ".bfd";

    /** The extension of a file where inflection pattern base with determined core patterns is
     * stored. */
    public static final String EXTENSION_IPB_WITH_PATTERNS = "_patterns.ipb";

    /** The extension of a file where inflection pattern base with determined core patterns is
     * logged out. */
    public static final String EXTENSION_IPB_WITH_PATTERN_LOG = "_patterns.ipb.log";

    /** The extension of a file where morpheme statistic is printed. */
    public static final String EXTENSION_MORPHEMES_STATISTIC = "_morphemes.txt";

    /** The extension of a file where top words of a language are defined. */
    public static final String EXTENSION_FREQUENT_WORDS = "_top_words.txt";

    /** The extension of a file where inflection tree is stored. */
    public static final String EXTENSION_INFLECTION_TREE = ".ipt";

    /** The extension of a file where inflection tree is logged out. */
    public static final String EXTENSION_INFLECTION_TREE_LOG = ".ipt.log";

    /** The extension of a file where trainign set is stored. */
    public static final String EXTENSION_TRAINING_SET = ".trn";

    /** The extension of a file where neural network is stored. */
    public static final String EXTENSION_NEURAL_NETWORK = ".neu";

    /** The extension of a file where an anlyser is sored. */
    public static final String EXTENSION_ANALYSER = ".ana";

    /** The extension of a file where an error statistic is sored. */
    public static final String EXTENSION_ERROR_STATISTIC = "_err_statistic.csv";
    
    /** Holds the number of hash functions used by a bloom filter. */
    public static int NUMBER_OF_HASH_FUNCTIONS_FOR_BLOOM_FILTER = 2;
    

    public static String INFO_AUTHORS = "Jakub Strychowski";
    public static String INFO_COPYRIGHT = "Copyright (C) 2008 Jakub Strychowski";
    public static String INFO_DESCRIPTION = "Definition of the morfological analyser.";
    public static String INFO_LICENCE = "LGPL (GNU Lesser General Public License";
    public static String INFO_NAME = "Neurpheus Morphological Analyser";
    public static String INFO_VENDOR = "Neurpheus";
    public static String INFO_WEB_PAGE = "www.neurpheus.org";

    
}
