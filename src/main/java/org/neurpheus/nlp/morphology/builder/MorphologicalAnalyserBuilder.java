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

package org.neurpheus.nlp.morphology.builder;

import org.neurpheus.collections.hashing.BloomFilter;
import org.neurpheus.collections.tree.Tree;
import org.neurpheus.collections.tree.linkedlist.LinkedListTree;
import org.neurpheus.collections.tree.linkedlist.LinkedListTreeFactory;
import org.neurpheus.collections.tree.util.TreeHelper;
import org.neurpheus.core.io.FilePath2Object;
import org.neurpheus.core.io.Object2FilePath;
import org.neurpheus.machinelearning.neuralnet.NeuralNetwork;
import org.neurpheus.machinelearning.neuralnet.NeuralNetworkException;
import org.neurpheus.machinelearning.neuralnet.impl.NeuralNetworkImpl;
import org.neurpheus.machinelearning.training.BaseTrainer;
import org.neurpheus.machinelearning.training.FileTrainingSet;
import org.neurpheus.machinelearning.training.MemoryTrainingSet;
import org.neurpheus.machinelearning.training.Trainer;
import org.neurpheus.machinelearning.training.TrainingExample;
import org.neurpheus.machinelearning.training.TrainingException;
import org.neurpheus.machinelearning.training.TrainingSet;
import org.neurpheus.machinelearning.training.TrainingSetException;
import org.neurpheus.machinelearning.training.TrainingSetUtils;
import org.neurpheus.machinelearning.training.gui.TrainingProgressListener;
import org.neurpheus.nlp.morphology.BaseFormsDictionary;
import org.neurpheus.nlp.morphology.DefaultMorphologyFactory;
import org.neurpheus.nlp.morphology.ExtendedInflectionPattern;
import org.neurpheus.nlp.morphology.MorphologicalAnalysisResult;
import org.neurpheus.nlp.morphology.MorphologyException;
import org.neurpheus.nlp.morphology.VowelCharactersImpl;
import org.neurpheus.nlp.morphology.baseimpl.GrammaticalPropertiesListImpl;
import org.neurpheus.nlp.morphology.builder.xml.Replacement;
import org.neurpheus.nlp.morphology.builder.xml.TaggingInfo;
import org.neurpheus.nlp.morphology.impl.CompactBaseFormsDictionary;
import org.neurpheus.nlp.morphology.impl.ExtendedMorphologicalAnalysisResult;
import org.neurpheus.nlp.morphology.impl.MorphologicalAnalyserImpl;
import org.neurpheus.nlp.morphology.impl.SimpleBaseFormsDictionary;
import org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalyserInfo;
import org.neurpheus.nlp.morphology.impl.xml.MorphologicalAnalysers;
import org.neurpheus.nlp.morphology.impl.xml.SupportedLocales;
import org.neurpheus.nlp.morphology.inflection.FormPattern;
import org.neurpheus.nlp.morphology.inflection.InflectionPatternsBase;
import org.neurpheus.nlp.morphology.inflection.XMLInflectionPatternBaseHelper;
import org.neurpheus.nlp.morphology.tagset.GrammaticalPropertiesList;
import org.neurpheus.nlp.morphology.tagset.Tagset;
import org.neurpheus.nlp.myspell.FullDictionaryWriter;
import org.neurpheus.nlp.myspell.MySpellDictionary;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.neurpheus.core.charset.DynamicCharset;

/**
 * Generates a morphological analyser from a MySpell dictionary.
 *
 * @author Jakub Strychowski
 */
public final class MorphologicalAnalyserBuilder implements Runnable {
    
    /** The logger used by this class. */
    private static Logger logger = Logger.getLogger(MorphologicalAnalyserBuilder.class.getName());
    private static Thread processingThread;

    public static NeuralNetworkLearningProperties getNeuralNetworkLearningProperties() {
        return neuralNetworkLearningProperties;
    }

    public static void setNeuralNetworkLearningProperties(NeuralNetworkLearningProperties aNeuralNetworkLearningProperties) {
        neuralNetworkLearningProperties = aNeuralNetworkLearningProperties;
    }

    
    /**
     * Private constructor prevents instance creation.
     */
    private MorphologicalAnalyserBuilder() {
    }

    /** 
     * Holds the minimal numer of lexemes which should be covered by an inflection pattern
     * processed by the neural network. Inflection patterns which covers less then
     * this value are ignored by the neural network.
     */
    public static final int MIN_LEXEMES_COUNT_FOR_NEURAL_IP = 10;
    
    
    /** In this step this class generates a full dictionary from a MySpell dictionary. */
    public static final int STEP_GENERATE_FULL_DICTIONARY = 0;
    
    /** In this step this class creates an inflection patterns base from a full dictionary. */
    public static final int STEP_CREATE_INFLECTION_PATTERNS_BASE = 1;
    
    /** In this step this class logs out a statistic about morpheme usage in an analysed language.*/
    public static final int STEP_MORPHEMES_STATISTICS = 2;

    /** In this step this class logs out a statistic about morpheme usage in an analysed language.*/
    public static final int STEP_TAG_INFLECTION_PATTERNS = 3;
    
    /** In this step this class determines a set of core patterns for wach inflection pattern. */
    public static final int STEP_DETERMINE_CORE_PATTERNS = 4;
    
    /** In this step this class creates an inflection tree for the inflection pattern base. */
    public static final int STEP_CREATE_ANALYSER = 5;
    
    /** In this step this class creates a training set for a neural network learning process. */
    public static final int STEP_CREATE_TRAINING_SET = 6;

    /** 
     * In this step this class learns a neural network which selects best inflection pattern for an
     * analysed word. 
     */
    public static final int STEP_LEARN_NEURAL_NETWORK = 7;

    /** In this step this class test an analyser. */
    public static final int STEP_CREATE_JAR_PACKAGE = 8;
    
    /** In this step this class test an analyser. */
    public static final int STEP_TEST_ANALYSER = 9;
    
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
    
    /** The extension of a file where inflection pattern base with determined core patterns
     * is stored. */
    public static final String EXTENSION_IPB_WITH_PATTERNS = "_patterns.ipb";
    
    /** The extension of a file where inflection pattern base with determined core patterns
     * is logged out. */
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
    
    /** The step from which start processing. */
    private static int fromStep = STEP_GENERATE_FULL_DICTIONARY;
    
    /** The step on which end procesing. */
    private static int toStep = STEP_TEST_ANALYSER;
    
    /** Holds the number of hash functions used by a bloom filter. */
    private static int NUMBER_OF_HASH_FUNCTIONS_FOR_BLOOM_FILTER = 2;
    
    /** Holds the maximum number of training examples processed while neural network learning process. */
    private static NeuralNetworkLearningProperties neuralNetworkLearningProperties = new NeuralNetworkLearningProperties();
    
    /** The path to the directory where MySpell and output files are stored. */
    private static String dictionariesPath = "C:\\projekty\\neurpheus\\data\\dictionaries\\";
    
    /** Symbols of dictionaries to process. */
    private static String[] dictionaries = {"test_pl_PL"};
    
    /** Processed languages. */
    private static Locale[] languages = {new Locale("pl", "PL")};
    
//    /** Symbols of dictionaries to process. */
//    private static String[] dictionaries = {"en_GB"};
//    
//    /** Processed languages. */
//    private static Locale[] languages = {new Locale("en", "GB")};
    
    /** Holds the symbol of currently processed dictionary. */
    private static String symbol;
    
    /** Holds the locale information about currently processed language. */
    private static Locale language;
    
    /** Holds the representation of the vowels of a curently processed language. */
    private static VowelCharactersImpl vowels;
    
    /** Denotes if infixes are allowed for inflection patterns. */
    private static boolean acceptInfixes = false;
    
    /** Denotes if prefixes are allowed for inflection patterns. */
    private static boolean acceptPrefixes = true;
    
    
    public static String INFO_AUTHORS = "Jakub Strychowski";
    public static String INFO_COPYRIGHT = "Copyright (C) 2008 Jakub Strychowski";
    public static String INFO_DESCRIPTION = "Definition of the morfological analyser.";
    public static String INFO_LICENCE = "LGPL (GNU Lesser General Public License";
    public static String INFO_NAME = "Neurpheus Morphological Analyser";
    public static String INFO_VENDOR = "Neurpheus";
    public static String INFO_WEB_PAGE = "www.neurpheus.org";
    
    
    /**
     * Returns a path to a file having given suffix.
     *
     * @param   suffix  The extension of a file.
     *
     * @return The path to the file.
     */
    private static String getPath(final String suffix) {
        return dictionariesPath + symbol + suffix;
    }
    
    /**
     * Generates a full dictionary file from a MySpell dictionary.
     * Full dictionary file is a text file where each line of the file
     * correspondes to a lexeme. Each lexeme is represendted by lexeme forms
     * separated by spaces. First form is a base form.
     *
     *
     * @throws MorpologicalAnalyserBuildException if any processing error occurred.
     */
    public static void generateFullDictionary() throws MorphologicalAnalyserBuildException {
        logger.info("STEP " + STEP_GENERATE_FULL_DICTIONARY
                + " (FULL DICTIONARY GENERATION) started.");
        
        MySpellDictionary dict = new MySpellDictionary();
        try {
            dict.load(dictionariesPath, symbol);
        } catch (Exception e) {
            throw new MorphologicalAnalyserBuildException("Cannot load a MySpell dictionary.", e);
        }
        String outPath = getPath(EXTENSION_FULL_DICTIONARY);
        PrintStream out = null;
        BufferedReader reader = null;
        HashSet ignoredForms = new HashSet();
        try {
            logger.info("Generating the full dictionary to the file: " + outPath);
            out = new PrintStream(new BufferedOutputStream(new FileOutputStream(outPath)), false, "UTF-8");
            
            // add forms from ..._toadd.txt file
            String addPath = getPath(EXTENSION_FULL_DICTIONARY_ADD);
            File addFile = new File(addPath);
            if (addFile.exists() && addFile.canRead()) {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(addFile), "UTF-8"));
                String line;
                do {
                    line = reader.readLine();
                    if (line != null && line.trim().length() > 0 && !line.startsWith("//")) {
                        out.println(line.trim());
                    }
                } while (line != null);
                reader.close();
                reader = null;
            }
            
            // read forms which should be omitted from file ..._toremove.txt
            String removePath = getPath(EXTENSION_FULL_DICTIONARY_REMOVE);
            File removeFile = new File(removePath);
            if (removeFile.exists() && removeFile.canRead()) {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(removeFile), "UTF-8"));
                String line;
                do {
                    line = reader.readLine();
                    if (line != null && line.trim().length() > 0 && !line.startsWith("//")) {
                        String[] forms = line.split("\\s");
                        for (int i = 0 ;i < forms.length; i++) {
                            String form = forms[i].trim();
                            if (form.length() > 0) {
                                ignoredForms.add(form);
                            }
                        }
                    }
                } while (line != null);
                reader.close();
                reader = null;
            }
            dict.processAllForms(new FullDictionaryWriter(out, ignoredForms), false);
        } catch (Exception e) {
            throw new MorphologicalAnalyserBuildException("Cannot generate the full dictionary.", e);
        } finally {
            if (out != null) {
                out.close();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ex) {
                    logger.log(Level.WARNING, "Cannot close stream", ex);
                }
            }
        }
        logger.info("STEP " + STEP_GENERATE_FULL_DICTIONARY
                + " (FULL DICTIONARY GENERATION) finished.");
    }

    /**
     * Generates a full dictionary file from a MySpell dictionary.
     * Full dictionary file is a text file where each line of the file
     * correspondes to a lexeme. Each lexeme is represendted by lexeme forms
     * separated by spaces. First form is a base form.
     *
     *
     * @throws MorpologicalAnalyserBuildException if any processing error occurred.
     */
    public static void regenerateFullDictionary() throws MorphologicalAnalyserBuildException {
        logger.info("STEP " + STEP_GENERATE_FULL_DICTIONARY
                + " (FULL DICTIONARY GENERATION) started.");
        
//        MySpellDictionary dict = new MySpellDictionary();
//        try {
//            dict.load(dictionariesPath, symbol);
//        } catch (Exception e) {
//            throw new MorphologicalAnalyserBuildException("Cannot load a MySpell dictionary.", e);
//        }
        String inPath = getPath(EXTENSION_DICTIONARY);
        String outPath = getPath(EXTENSION_FULL_DICTIONARY);
        PrintStream out = null;
        BufferedReader reader = null;
        HashSet ignoredForms = new HashSet();
        try {
            logger.info("Generating the full dictionary to the file: " + outPath);
            out = new PrintStream(new BufferedOutputStream(new FileOutputStream(outPath)), false, "UTF-8");
            
            // add forms from ..._toadd.txt file
            String addPath = getPath(EXTENSION_FULL_DICTIONARY_ADD);
            File addFile = new File(addPath);
            if (addFile.exists() && addFile.canRead()) {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(addFile), "UTF-8"));
                String line;
                do {
                    line = reader.readLine();
                    if (line != null && line.trim().length() > 0 && !line.startsWith("//")) {
                        out.println(line.trim());
                    }
                } while (line != null);
                reader.close();
                reader = null;
            }
            
            // read forms which should be omitted from file ..._toremove.txt
            String removePath = getPath(EXTENSION_FULL_DICTIONARY_REMOVE);
            File removeFile = new File(removePath);
            if (removeFile.exists() && removeFile.canRead()) {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(removeFile), "UTF-8"));
                String line;
                do {
                    line = reader.readLine();
                    if (line != null && line.trim().length() > 0 && !line.startsWith("//")) {
                        String[] forms = line.split("\\s");
                        for (int i = 0 ;i < forms.length; i++) {
                            String form = forms[i].trim();
                            if (form.length() > 0) {
                                ignoredForms.add(form);
                            }
                        }
                    }
                } while (line != null);
                reader.close();
                reader = null;
            }
            
            // add forms from ..._toadd.txt file
            File inFile = new File(inPath);
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(inFile), "UTF-8"));
            String line;
            do {
                line = reader.readLine();
                if (line != null && line.trim().length() > 0) {
                    String[] tab;
                    if (line.indexOf(',') >= 0) {
                        tab = line.split(",");
                    } else {
                        tab = line.split("\\s");
                    }
                    if (tab.length > 0) {
                        String baseForm = tab[0].trim();
                        if (baseForm.indexOf(' ') < 0 && !ignoredForms.contains(baseForm)) {
                            StringBuffer buffer = new StringBuffer();
                            for (int i = 0; i < tab.length; i++) {
                                String form = tab[i].trim();
                                if (form.length() > 0) {
                                    if (buffer.length() > 0) {
                                        buffer.append(' ');
                                    }
                                    buffer.append(form);
                                }
                            }
                            if (buffer.length() > 0) {    
                                out.println(buffer.toString());
                            }
                        } 
                    }
                }
            } while (line != null);
            reader.close();
            reader = null;
            
        } catch (Exception e) {
            throw new MorphologicalAnalyserBuildException("Cannot generate the full dictionary.", e);
        } finally {
            if (out != null) {
                out.close();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ex) {
                    logger.log(Level.WARNING, "Cannot close stream", ex);
                }
            }
        }
        logger.info("STEP " + STEP_GENERATE_FULL_DICTIONARY
                + " (FULL DICTIONARY GENERATION) finished.");
    }
    
    public static void saveInflectionPatternsBase(final InflectionPatternsBase ipb, final String ipbPath) 
            throws MorphologicalAnalyserBuildException {
        if (ipbPath == null) {            
            throw new NullPointerException("The [ipbPath] argument cannot be null.");            
        }
        try {
            logger.info("Saving the created inflection pattern base to the file: " + ipbPath);
            Object2FilePath.writeObject(ipbPath, ipb, false);
        } catch (IOException e) {
            throw new MorphologicalAnalyserBuildException(
                    "Cannot store inflection pattern base in path: " + ipbPath, e);
        }
    }
    
    public static InflectionPatternsBase loadInflectionPatternsBase(final String ipbPath) 
            throws MorphologicalAnalyserBuildException {
        if (ipbPath == null) {            
            throw new NullPointerException("The [ipbPath] argument cannot be null.");            
        }
        InflectionPatternsBase ipb = null;
        try {
            logger.info("Loading inflection pattern base from the file: " + ipbPath);
            ipb = (InflectionPatternsBase) FilePath2Object.readObject(ipbPath, false);
        } catch (IOException e) {
            throw new MorphologicalAnalyserBuildException(
                    "Cannot load an inflection pattern base from the file: " + ipbPath, e);
        }
        return ipb;
    }
    
    /**
     * Creates an inflection pattern base.
     *
     *
     * @throws MorpologicalAnalyserBuildException if any processing error occurred.
     */
    public static void createInflectionPatternsBase() throws MorphologicalAnalyserBuildException {
        logger.info("STEP " + STEP_CREATE_INFLECTION_PATTERNS_BASE
                + " (INFLECTION PATTERN BASE CREATION) started.");
        
        // Receives base forms during the IPB creation process.
        BaseFormsDictionary dict = new SimpleBaseFormsDictionary();
        
        // Open the full dictionary.
        InflectionPatternsBase ipb = null;
        String inPath = getPath(EXTENSION_FULL_DICTIONARY);
        InputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(inPath));
        } catch (IOException e) {
            throw new MorphologicalAnalyserBuildException("Cannot open a full dictionary from file:" + inPath, e);
        }
        
        // Create the inflection patterns base.
        try {
            logger.info("Creating inflection pattern base analysing the file: " + inPath);
            ipb = InflectionPatternsBase.createFromFullDictionary(
                    in, "UTF-8", acceptInfixes, acceptPrefixes, dict, language);
        } catch (Exception e) {
            throw new MorphologicalAnalyserBuildException("Cannot create inflection pattern base.", e);
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Cannot properly close input stream.", e);
            }
        }
        
        // Saves the base forms dictionary in a compact form.
        String dictPath = getPath(EXTENSION_BASE_FORMS_DICTIONARY);
        logger.info("Creating a compact form of a base forms dictionary.");
        dict = new CompactBaseFormsDictionary(dict, ipb.getInflectionPatternsMap());
        try {
            logger.info("Saving the base forms dictionary to the file: " + dictPath);
            Object2FilePath.writeObject(dictPath, dict, false);
        } catch (IOException e) {
            throw new MorphologicalAnalyserBuildException(
                    "Cannot save the base forms dictionary to the file: " + dictPath, e);
        }

        // apply tags automatically
        applyTags(ipb);
        
        // Save the inflection patterns base.
        saveInflectionPatternsBase(ipb, getPath(EXTENSION_IPB));
        
        // Log the inflection patterns base.
        String logPath = getPath(EXTENSION_IPB_LOG);
        try {
            logger.info("Logging out the created inflection pattern base to the file: " + logPath);
            ipb.print(logPath, true);
        } catch (IOException e) {
            throw new MorphologicalAnalyserBuildException(
                    "Cannot log out the inflection pattern base to the file: " + logPath, e);
        }
        
        // Save inflection patterns base for tagging.
        String tagPath = getPath(EXTENSION_IPB_OUT_XML);
        try {
            logger.info("Saving the created inflection pattern base to the xml file " + tagPath);
            XMLInflectionPatternBaseHelper.writeAsXML(ipb, tagPath);
        } catch (IOException e) {
            throw new MorphologicalAnalyserBuildException(
                    "Cannot save the inflection pattern base to the file: " + tagPath, e);
        }
        tagPath = getPath(EXTENSION_IPB_IN_XML);
        File testFile = new File(tagPath);
        if (!testFile.exists()) {
            try {
                logger.info("Saving the created inflection pattern base to the xml file " + tagPath);
                XMLInflectionPatternBaseHelper.writeAsXML(ipb, tagPath);
            } catch (IOException e) {
                throw new MorphologicalAnalyserBuildException(
                        "Cannot save the inflection pattern base to the file: " + tagPath, e);
            }
        }
        
        
        logger.info("STEP " + STEP_CREATE_INFLECTION_PATTERNS_BASE
                + " (INFLECTION PATTERN BASE CREATION) finished.");
    }
    
    public static void applyTags(InflectionPatternsBase ipb) throws MorphologicalAnalyserBuildException {
        logger.info("APPLING TAGS AUTOMATICALY started");
        
        String taggingInfoPath = getPath(EXTENSION_TAGGING_INFO);
        String taggingFilePath = getPath(EXTENSION_TAGGING_FILE);
        String tagsetPath = getPath(EXTENSION_TAGSET);
        File taggingInfoFile = new File(taggingInfoPath);
        File taggingFile = new File(taggingFilePath);
        File tagsetFile = new File(tagsetPath);
        if (taggingInfoFile.exists() && taggingInfoFile.canRead() 
                && taggingFile.exists() && taggingFile.canRead()
                && tagsetFile.exists() && tagsetFile.canRead()) {
            
            // read tagset
            Tagset tagset = loadTagset(tagsetPath);
            ipb.setTagset(tagset);
            
            // read info
            logger.info("Reading information about automatic tagging.");
            InputStream in = null;
            String encoding = "utf-8";
            HashMap replacements = new HashMap();
            try {
                in = new BufferedInputStream(new FileInputStream(taggingInfoFile));
                TaggingInfo taggingInfo = TaggingInfo.read(in);
                encoding = taggingInfo.getEncoding();
                Replacement[] replacementsTab = taggingInfo.getTagModification().getReplacement();
                for (int i = 0; i < replacementsTab.length; i++) {
                    String sourceTag = replacementsTab[i].getSourceTag();
                    String targetTag = replacementsTab[i].getTargetTag();
                    replacements.put(sourceTag, targetTag);
                }
            } catch (Exception e) {
                throw new MorphologicalAnalyserBuildException(
                        "Cannot read tagging info from file " + taggingInfoPath, e);
            } finally {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.log(Level.WARNING, "Cannot close stream.", e);
                }
            }

            ipb.recreateFormPatterns();
            
            // hash inflection patterns base
            logger.info("Hashin inflection patterns base");
            HashMap formsMap = new HashMap();
            for (final Iterator it = ipb.getInflectionPatterns().iterator(); it.hasNext();) {
                ExtendedInflectionPattern ip = (ExtendedInflectionPattern) it.next();
                FormPattern baseFormPattern = ip.getBaseFormPattern();
                Collection allFormPatterns = ip.getAllFormPatterns();
                Iterator coresIt = ip.getCoveredCores().iterator();
                int counter = 3;
                while (coresIt.hasNext() && counter-- > 0) {
                    String core = coresIt.next().toString();
                    String baseForm = MorphologicalAnalyserImpl.makeForm(core, baseFormPattern.getAffixes());
                    for (final Iterator fit = allFormPatterns.iterator(); fit.hasNext();) {
                        FormPattern fp = (FormPattern) fit.next();
                        String form = MorphologicalAnalyserImpl.makeForm(core, fp.getAffixes());
                        String id = baseForm + "|" + form;
                        formsMap.put(id, fp);
                    }
                }
            }
            
            // scan tagging file
            logger.info("Scaning a file with tagging information");
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(taggingFile), encoding));
                String line = reader.readLine();
                while (line != null) {
                    String[] elements = line.split("\\s");
                    if (elements.length >= 3) {
                        // getexample tagging from the file
                        String form = elements[0];
                        String baseForm = elements[1];
                        String marks = elements[2];
                        // get list of grammatical properties for taggin
                        marks = applyReplacements(marks, replacements);
                        GrammaticalPropertiesList gpl = new GrammaticalPropertiesListImpl(marks, tagset);
                        //tagset.getGrammaticalPropertiesList(marks);
                        //GrammaticalPropertiesList
                        
                        // apply tags to inflection patterns
                        String id = baseForm + "|" + form;
                        FormPattern fp = (FormPattern) formsMap.get(id);
                        if (fp != null) {
                            GrammaticalPropertiesList tmp = fp.getGrammaticalPropertiesList();
                            if (tmp != null) {
                                tmp.merge(gpl);
                            } else {
                                fp.setGrammaticalPropertiesList(gpl);
                            }
                            
                        }
                    }
                    line = reader.readLine();
                }
            } catch (Exception e) {
                throw new MorphologicalAnalyserBuildException(
                        "Error during reading the tagging file: " + taggingFilePath, e);
            } finally {
                try {
                    reader.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            // normalize gpls
            logger.info("Normalizing grammatial properties lists.");
            for (final Iterator it = ipb.getInflectionPatterns().iterator(); it.hasNext();) {
                ExtendedInflectionPattern ip = (ExtendedInflectionPattern) it.next();
                Collection allFormPatterns = ip.getAllFormPatterns();
                for (final Iterator fpit = allFormPatterns.iterator(); fpit.hasNext();) {
                    FormPattern fp = (FormPattern) fpit.next();
                    GrammaticalPropertiesList gpl = fp.getGrammaticalPropertiesList();
                    if (gpl != null) {
                        gpl.normalize(tagset);
                        fp.setGrammaticalPropertiesList(tagset.getGrammaticalPropertiesList(gpl.toString()));
                    }
                }
            }
        }
        logger.info("APPLING TAGS AUTOMATICALY finished");
    }

     private static String applyReplacements(String marks, Map replacements) {
         StringBuffer res = new StringBuffer();
         StringBuffer tmp = new StringBuffer();
         int length = marks.length();
         for (int i = 0; i <= length; i++) {
             char c = i < length ? marks.charAt(i) : '\n';
             switch (c) {
                 case '.' :
                 case ':' :
                 case '+' :
                 case '\n' :
                     String tag = tmp.toString();
                     tmp.setLength(0);
                     String target = (String) replacements.get(tag);
                     if (target != null) {
                         tag = target;
                     }
                     res.append(tag);
                     if (c != '\n') {
                         res.append(c);
                     }
                     break;
                 default :
                     tmp.append(c);
                     break;
             }
         }
         return res.toString();
     }
    
    /**
     * Prints the statistic about a morphemes ambiguity.
     *
     *
     * @throws MorpologicalAnalyserBuildException if any processing error occurred.
     */
    public static void printMorphemesStatistic() throws MorphologicalAnalyserBuildException {
        logger.info("STEP " + STEP_MORPHEMES_STATISTICS
                + " (PRINT MORPHEME STATISTIC) started.");

        // load inflection patterns base
        InflectionPatternsBase ipb = loadInflectionPatternsBase(getPath(EXTENSION_IPB));
        
        String outPath = getPath(EXTENSION_MORPHEMES_STATISTIC);
        try {
            logger.info("Printing morpheme statistics to the file: " + outPath);
            ipb.printMorphemesStatistic(outPath);
        } catch (Exception e) {
            throw new MorphologicalAnalyserBuildException(
                    "Cannot print the morpheme statistic to the file: " + outPath, e);
        }
        
        logger.info("STEP " + STEP_MORPHEMES_STATISTICS
                + " (PRINT MORPHEME STATISTIC) finished.");
    }

    public static Tagset loadTagset(final String tagsetPath) 
        throws MorphologicalAnalyserBuildException {
        if (tagsetPath == null) {            
            throw new NullPointerException("The [tagsetPath] argument cannot be null.");            
        }
        Tagset tagset  = null;
        InputStream in = null;
        try {
            logger.info("Loading tagset from the file: " + tagsetPath);
            in = new BufferedInputStream(new FileInputStream(new File(tagsetPath)));
            tagset = DefaultMorphologyFactory.getInstance().createTagset();
            tagset.readFromXML(in);
        } catch (IOException e) {
            throw new MorphologicalAnalyserBuildException(
                    "Cannot load a tagset from the file: " + tagsetPath, e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.log(Level.SEVERE, "Cannot close stream", e);
                }
            }
        }
        return tagset;
    }
    
    /**
     * Prints the statistic about a morphemes ambiguity.
     *
     *
     * @throws MorpologicalAnalyserBuildException if any processing error occurred.
     */
    public static void tagInflectionPatterns() throws MorphologicalAnalyserBuildException {
        logger.info("STEP " + STEP_TAG_INFLECTION_PATTERNS
                + " (TAG INFLECTION PATTERNS) started.");
        

        // load tagset
        String tagsetPath = getPath(EXTENSION_TAGSET);
        File f = new File(tagsetPath);
        if (f.exists() && f.canRead()) {
            Tagset tagset = loadTagset(tagsetPath);

            
            // check if tagged inflection patterns exists
            String ipPath = getPath(EXTENSION_IPB_IN_XML);
            f = new File(ipPath);
            if (f.exists() && f.canRead()) {

                // load tagged inflection patterns
                logger.info("Loading tagged inflection patterns from the file: " + ipPath);
                org.neurpheus.nlp.morphology.inflection.xml.InflectionPatternsBase xipb = null;
                try {
                    xipb = XMLInflectionPatternBaseHelper.readFromXml(ipPath);
                } catch (IOException e) {
                    throw new MorphologicalAnalyserBuildException(
                            "Cannot load tagged inflection patterns from path " + ipPath, e);
                }

                // load inflection patterns base
                InflectionPatternsBase ipb = loadInflectionPatternsBase(getPath(EXTENSION_IPB));
                ipb.setTagset(tagset);
                ipb.recreateFormPatterns();

                // apply tags
                XMLInflectionPatternBaseHelper.applyTags(ipb, xipb, tagset);

                // Save the inflection patterns base.
                saveInflectionPatternsBase(ipb, getPath(EXTENSION_IPB));

                // load inflection patterns base
                ipb = loadInflectionPatternsBase(getPath(EXTENSION_IPB));
                
                // Log the inflection patterns base.
                String logPath = getPath(EXTENSION_IPB_LOG);
                try {
                    logger.info("Logging out the created inflection pattern base to the file: " + logPath);
                    ipb.print(logPath, true);
                } catch (IOException e) {
                    throw new MorphologicalAnalyserBuildException(
                            "Cannot log out the inflection pattern base to the file: " + logPath, e);
                }
                
            } else {
                logger.info("Cannot find tagget inflection patterns at location " + ipPath);
            }
        } else {
            logger.info("Cannot find tagset at location " + tagsetPath);
        }
        
        
        logger.info("STEP " + STEP_TAG_INFLECTION_PATTERNS
                + " (TAG INFLECTION PATTERNS) finished.");
    }
    
    
    /**
     * Determines core pattern for inflection patterns.
     *
     *
     * @throws MorpologicalAnalyserBuildException if any processing error occurred.
     */
    public static void determineCorePatterns() throws MorphologicalAnalyserBuildException {
        logger.info("STEP " + STEP_DETERMINE_CORE_PATTERNS
                + " (DETERMINE CORE PATTERNS) started.");
        
        // load inflection patterns base
        InflectionPatternsBase ipb = loadInflectionPatternsBase(getPath(EXTENSION_IPB));
        
        logger.info("Determing core pattern in the inflection pattern base.");
        ipb.determineCorePatterns(vowels);
        
        // Save the inflection patterns base.
        saveInflectionPatternsBase(ipb, getPath(EXTENSION_IPB_WITH_PATTERNS));
        
        // Log the inflection patterns base.
        String logPath = getPath(EXTENSION_IPB_WITH_PATTERN_LOG);
        try {
            logger.info(
                    "Logging out the IPB with the created core patterns to the file: " + logPath);
            ipb.print(logPath, true);
        } catch (IOException e) {
            throw new MorphologicalAnalyserBuildException(
                    "Cannot log out the inflection pattern base to the file: " + logPath, e);
        }
        
        logger.info("STEP " + STEP_DETERMINE_CORE_PATTERNS
                + " (DETERMINE CORE PATTERNS) finished.");
    }
    
    /**
     * Creates an inflection tree from an inflection patterns base.
     *
     * @throws MorpologicalAnalyserBuildException if any processing error occurred.
     */
    public static void createAnalyser(DynamicCharset charset) throws MorphologicalAnalyserBuildException {
        logger.info("STEP " + STEP_CREATE_ANALYSER
                + " (CREATE ANALYSER) started.");
        
        BaseFormsDictionary dict;
        
        // load inflection patterns base
        InflectionPatternsBase ipb = loadInflectionPatternsBase(getPath(EXTENSION_IPB_WITH_PATTERNS));
        
        // load inflection pattern base.
        String ipbPath = getPath(EXTENSION_IPB_WITH_PATTERNS);
        try {
            logger.info("Loading inflection pattern base from the file: " + ipbPath);
            ipb = (InflectionPatternsBase) FilePath2Object.readObject(ipbPath, false);
        } catch (IOException e) {
            throw new MorphologicalAnalyserBuildException(
                    "Cannot load an inflection pattern base from the file: " + ipbPath, e);
        }
        
        // load base form dictionary.
        String dictPath = getPath(EXTENSION_BASE_FORMS_DICTIONARY);
        try {
            logger.info("Loading base forms dictionary from the file: " + dictPath);
            dict = (BaseFormsDictionary) FilePath2Object.readObject(dictPath, false);
            dict.setInflectionPatternsMap(ipb.getInflectionPatternsMap());
        } catch (IOException e) {
            throw new MorphologicalAnalyserBuildException(
                    "Cannot load a base forms dictionary from the file: " + dictPath, e);
        }
        
        // create tree
        logger.info("Inflection tree generation.");
        Tree tree = InflectionTreeBuilder.buildTree(ipb, dict);
        logger.info("Inflection tree compression.");
        LinkedListTreeFactory factory = LinkedListTreeFactory.getInstance();
        tree = factory.createTree(tree, true, true, false);


        // Save the inflection tree.
        String treePath = getPath(EXTENSION_INFLECTION_TREE);
        try {
            logger.info("Saving the inflection tree to the file: " + treePath);
            Object2FilePath.writeObject(treePath, tree, false);
        } catch (IOException e) {
            throw new MorphologicalAnalyserBuildException(
                    "Cannot store inflection tree in path: " + treePath, e);
        }

        // Log the inflection tree.
        String logPath = getPath(EXTENSION_INFLECTION_TREE_LOG);
        try {
            logger.info(
                    "Logging out the inflection tree to the file: " + logPath);
            TreeHelper.printTree(tree, logPath, new InflectionNodePrinter());
        } catch (IOException e) {
            throw new MorphologicalAnalyserBuildException(
                    "Cannot log out the inflection tree to the file: " + logPath, e);
        }
        
        
        // create analyser
        MorphologicalAnalyserImpl analyser = new MorphologicalAnalyserImpl();
        analyser.setCharset(charset);
        
        // change weights of inflection patterns
        // and determine a number of inflection patterns involved in the nerual network processing
        int maxIndexOfNeuralInflectionPattern = -1;
        int index = 0;
        int max = 0;
        for (final Iterator it = ipb.getInflectionPatterns().iterator(); it.hasNext(); index++) {
            ExtendedInflectionPattern ip = (ExtendedInflectionPattern) it.next();
            int lexemesCount = ip.getNumberOfCoveredLexemes();
            int ipWeight = (int) Math.round(Math.log((double) lexemesCount));
            ip.setNumberOfCoveredLexemes(ipWeight);
            if (ipWeight > max) {
                max = ipWeight;
            }
            //if (ip.getCorePatterns().size() > 0) {
            if (lexemesCount >= MIN_LEXEMES_COUNT_FOR_NEURAL_IP && ip.getCorePatterns().size() > 0) {
                maxIndexOfNeuralInflectionPattern = index;
            }
        }
        analyser.setMaxIPWeight(max);
        analyser.setMaxCorePatternLength(ipb.getMaxLengthOfCorePattern());
        analyser.setMaxSupplementLength(ipb.getMaxLengthOfSupplement());
        analyser.setMaxIndexOfNeuralInflectionPattern(maxIndexOfNeuralInflectionPattern);
        ipb.compact(true);
        analyser.setIpb(ipb);
        analyser.setTree((LinkedListTree) tree);
        
        // create bloom filter for base forms
        Collection baseForms = dict.getBaseForms();
        BloomFilter filter = new BloomFilter(baseForms.size(), NUMBER_OF_HASH_FUNCTIONS_FOR_BLOOM_FILTER);
        for (Iterator it = baseForms.iterator(); it.hasNext();) {
            filter.add(it.next().toString());
        }
        analyser.setBaseFormsBloomFilter(filter);
        logger.info("Base forms count: " + baseForms.size());
        logger.info("Base forms bloom filter size: " + filter.size());
        logger.info("Base forms bloom filter fill ratio: " + (100.0 * filter.size() / baseForms.size()) + "%");
        logger.info("Base forms bloom filter allocation size: " + filter.getAllocationSize() + "B");
        
        
        // Save the analyser.
        String analyserPath = getPath(EXTENSION_ANALYSER);
        try {
            logger.info("Saving the analyser to the file: " + analyserPath);
            Object2FilePath.writeObject(analyserPath, analyser, false);
        } catch (IOException e) {
            throw new MorphologicalAnalyserBuildException(
                    "Cannot store analyser in path: " + analyserPath, e);
        }
        
        logger.info("STEP " + STEP_CREATE_ANALYSER
                + " (CREATE ANALYSER) finished.");
    }
    
    /**
     * Creates a training set for a neural network.
     *
     * @throws MorpologicalAnalyserBuildException if any processing error occurred.
     */
    public static void createTrainingSet() throws MorphologicalAnalyserBuildException {
        logger.info("STEP " + STEP_CREATE_TRAINING_SET
                + " (CREATE TRAINING SET) started.");
        // open analyser
        String path = getPath(EXTENSION_ANALYSER);
        MorphologicalAnalyserImpl analyser = null;
        try {
            analyser = (MorphologicalAnalyserImpl) FilePath2Object.readObject(path, path.endsWith(".anz"));
            analyser.setUseBaseFormsDictionary(false);
            analyser.setUseNeuralNetwork(false);
        } catch (IOException e) {
            throw new MorphologicalAnalyserBuildException("Cannot open analyser.", e);
        }
        
        // open training set
        String outPath = getPath(EXTENSION_TRAINING_SET);
        TrainingSet trainingSet;
        try {
            trainingSet = new FileTrainingSet(outPath, new InflectionTrainingExample());
            trainingSet.clear();
        } catch (Exception e) {
            throw new MorphologicalAnalyserBuildException("Cannot open training set.", e);
        }
        
        String inPath = getPath(EXTENSION_FULL_DICTIONARY);
        BufferedReader reader = null; 
        try {
            reader = new BufferedReader(new InputStreamReader(new BufferedInputStream(new FileInputStream(inPath)), "UTF-8"));
        } catch (IOException e) {
            throw new MorphologicalAnalyserBuildException("Cannot open a full dictionary from file:" + inPath, e);
        }
        
        InflectionPatternsBase ipb = analyser.getIpb();
        
        String line;
        int number = 0;
        final int numberOfLinesBetweenInfoMessages = 1000;
        long startTime = System.currentTimeMillis();
        int maxIPId = analyser.getMaxIndexOfNeuralInflectionPattern();
        int counter = 0;
        try {
            do {
                line = reader.readLine();
                if (line != null && line.trim().length() > 0) {
                    line = line.trim();
                    ExtendedInflectionPattern ip = ipb.getInflectionPattern(line);
                    if (ip != null) {
                        String[] tab = line.split("\\s");
                        String baseForm = tab[0];
                        for (int i = 0; i < tab.length; i++) {
                            String form = tab[i];
                            if (form.length() > 0) {
                                try {
                                    List result = analyser.analyse2list(form);
                                    TrainingExample example = InflectionTrainingExample.createTrainingExample(form, result, ip, maxIPId);
                                    if (example != null) {
                                        try {
                                            trainingSet.addTrainingExample(example);
                                            counter++;
                                        } catch (TrainingSetException e) {
                                            throw new MorphologicalAnalyserBuildException(
                                                    "Cannot add training example.", e);
                                        }
                                    }
                                } catch (MorphologyException e) {
                                    throw new MorphologicalAnalyserBuildException(
                                            "Cannot create training example.", e);
                                }
                            }
                        }
                    }
                    number++;
                    if (logger.isLoggable(Level.FINE) && (number % numberOfLinesBetweenInfoMessages == 0)) {
                        logger.fine("Number of processed words : " + number);
                    }
                }
            } while (line != null);
        } catch (IOException e) {
            throw new MorphologicalAnalyserBuildException("Cannot create training set.", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    logger.log(Level.SEVERE, "Cannot properly close input stream.", e);
                }
            }
        }
        
        
        // randomize order
        try {
            logger.info("Randomize the order of trianing examples.");
            trainingSet.mix();
        } catch (TrainingSetException e) {
            throw new MorphologicalAnalyserBuildException("Cannot randomize order of training examples.", e);
        }
        
        // export training set to csv file.
        exportTrainingSet(trainingSet, maxIPId + 2);
        
        // close training set
        try {
            trainingSet.close();
        } catch (TrainingSetException e) {
            logger.log(Level.WARNING, "Cannot close training set.", e);
        }
            
        
        logger.info("STEP " + STEP_CREATE_TRAINING_SET
                + " (CREATE TRAINING SET) finished.");
    }
    
    /**
     * Export training set to the CSV file.
     *
     * @param trainingSet   The training set which should be exported.
     *
     * @throws MorphologicalAnalyserBuildException if any error occurred while exporting. 
     */
    public static void exportTrainingSet(TrainingSet trainingSet, final int categoriesCount) throws MorphologicalAnalyserBuildException {
        // write training data to csv file
        String path = getPath(EXTENSION_TRAINING_SET + ".csv");
        OutputStream out = null;
        try {
            out = new BufferedOutputStream(new FileOutputStream(path));
            TrainingSetUtils.exportToCSV(trainingSet, out, false, 10000, categoriesCount);
        } catch (Exception e) {
            throw new MorphologicalAnalyserBuildException("Cannot export training set to the CSV file", e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ioe) {
                    logger.log(Level.SEVERE, "Cannot close CSV stream.", ioe);
                }
            }
        }
        
    }
    
    /**
     * Learns a neural network using created training set.
     *
     * @throws MorpologicalAnalyserBuildException if any processing error occurred.
     */
    public static void learnNeuralNetwork() throws MorphologicalAnalyserBuildException {
        logger.info("STEP " + STEP_LEARN_NEURAL_NETWORK
                + " (CREATE LEARN NEURAL NETWORK) started.");

        
        // load training set
        String path = getPath(EXTENSION_TRAINING_SET);
        TrainingSet trainingSet;
        try {
            trainingSet = new FileTrainingSet(path, new InflectionTrainingExample());
        } catch (IOException e) {
            throw new MorphologicalAnalyserBuildException("Cannot open training set.", e);
        }
        
        
        
        
        // load analyser
        path = getPath(EXTENSION_ANALYSER);
        MorphologicalAnalyserImpl analyser = null;
        try {
            analyser = (MorphologicalAnalyserImpl) FilePath2Object.readObject(path, path.endsWith(".anz"));
        } catch (IOException e) {
            throw new MorphologicalAnalyserBuildException("Cannot open analyser.", e);
        }
        
        NeuralNetworkLearningProperties nnp = getNeuralNetworkLearningProperties();
        
        // Determine a number of neurons. 
        // Neural network contains following neurons:
        // x neurons for x inflection patterns involved in a neural network pocessing.
        int neuronsCount = (analyser.getMaxIndexOfNeuralInflectionPattern() + 1);
        // special neuron for all remaining inflection patterns
        neuronsCount++;
        
        // Determine a number of input.
        // Neural network contains following inputs:
        // x inputs for x inflection patterns involved in a neural network processing.
        // special input for all remaining inflection patterns
        // 32 inputs for characters presented in the analysed form - if the character x is present in the 
        //      ananlysed form, an input (neuronsCount + (x mod 32)) is activated
        int inputsCount = neuronsCount + 32;
        

        logger.info("Number of neurons in the neural network: " + neuronsCount);
        logger.info("Number of inputs in the neural network: " + inputsCount);
    
        Properties variables = new Properties();
        variables.setProperty("I", Integer.toString(inputsCount));
        variables.setProperty("O", Integer.toString(neuronsCount));
        NeuralNetwork ann;
//            
//            NeuralNetworkLayer layer1 = new LinearLayer("inputLayer", inputsCount);
//            NeuralNetworkLayer layer2 = new SigmoidLayer("outputLayer", neuronsCount);
//            ann.addLayer(layer1);
//            ann.addLayer(layer2);
//            layer1.connectWith(layer2, FullSynapse.class);
//            
//            NeuralNetworkLayer inputLayer = new LinearLayer("inputLayer", inputsCount);
//            NeuralNetworkLayer hiddenLayer1 = new TanhLayer("hiddenLayer1", neuronsCount * 2);
//            NeuralNetworkLayer hiddenLayer2 = new TanhLayer("hiddenLayer2", neuronsCount);
//            NeuralNetworkLayer outputLayer = new SigmoidLayer("outputLayer", neuronsCount);
//            ann.addLayer(inputLayer);
//            ann.addLayer(hiddenLayer1);
//            ann.addLayer(hiddenLayer2);
//            ann.addLayer(outputLayer);
//            inputLayer.connectWith(hiddenLayer1, FullSynapse.class);
//            hiddenLayer1.connectWith(hiddenLayer2, FullSynapse.class);
//            hiddenLayer2.connectWith(outputLayer, FullSynapse.class);
            
            
//            NeuralNetworkLayer layer1 = new LinearLayer("inputLayer", inputsCount);
//            NeuralNetworkLayer layer2 = new TanhLayer("hiddenLayer", inputsCount);
//            NeuralNetworkLayer layer3 = new TanhLayer("smallLayer", neuronsCount / 4);
//            NeuralNetworkLayer layer4 = new SigmoidLayer("outputLayer", neuronsCount);
//            ann.addLayer(layer1);
//            ann.addLayer(layer2);
//            ann.addLayer(layer3);
//            ann.addLayer(layer4);
//            layer1.connectWith(layer2, FullSynapse.class);
//            layer1.connectWith(layer3, FullSynapse.class);
//            layer2.connectWith(layer4, FullSynapse.class);
//            layer3.connectWith(layer4, FullSynapse.class);
            
            
//            NeuralNetworkLayer layer1 = new LinearLayer("inputLayer", inputsCount);
//            NeuralNetworkLayer layer3 = new SigmoidLayer("outputLayer", neuronsCount);
//            ann.addLayer(layer1);
//            ann.addLayer(layer3);
//            layer1.connectWith(layer3, FullSynapse.class);
            
        try {
            ann = new NeuralNetworkImpl(nnp.getArchitecture(), variables);
        } catch (NeuralNetworkException e) {
            throw new MorphologicalAnalyserBuildException(
                    "Canno create neural network.", e);
        }

//      ann.makeTransmiter();
        ann.random(nnp.getWeightsAmplitude());

        
        int numberOfEpochs = nnp.getNumberOfEpochs();
        double etha = nnp.getLearningFactor();
        double ethaMultipler = nnp.getLearningFactorMultipler() - nnp.getLearningFactorMultiplerByNumberOfEpochs() / numberOfEpochs;
        
        
        Properties trainingProperties = new Properties();
        trainingProperties.setProperty(NeuralNetworkImpl.PROPERTY_ETHA, Double.toString(etha));
        trainingProperties.setProperty(NeuralNetworkImpl.PROPERTY_ETHA_MULTIPLER, Double.toString(ethaMultipler));
        trainingProperties.setProperty(NeuralNetworkImpl.PROPERTY_LEARNING_ALGORITHM, Integer.toString(nnp.getLearningAlgorithm()));
        trainingProperties.setProperty(NeuralNetworkImpl.PROPERTY_MOMENTUM_RATE, Double.toString(nnp.getMomentumFactor()));
        trainingProperties.setProperty(NeuralNetworkImpl.PROPERTY_MAX_JUMP_FACTOR, Double.toString(nnp.getMaxJumpFactor()));
        trainingProperties.setProperty(NeuralNetworkImpl.PROPERTY_MINIMUM_ERROR_VALUE, Double.toString(nnp.getMimimumErrorValue()));
        
        ann.setClassificationMode(nnp.isClassificationMode());

        int max = nnp.getMaximumNumberOfTrainingExamples() + nnp.getNumberOfTestExamples();
        if (max <= 110000) {
            logger.info("Buffereing training examples to speed up processing...");
            int counter = 0;
            MemoryTrainingSet memTrainingSet = new MemoryTrainingSet();
            try {
                for (final Iterator it = trainingSet.iterator(); it.hasNext() && counter < max; counter++) {
                    TrainingExample example = (TrainingExample) it.next();
                    memTrainingSet.addTrainingExample(example);
                }
                trainingSet.close();
                trainingSet = memTrainingSet;
                logger.info(counter + " training examples are buffered.");
            } catch (TrainingSetException e) {
                throw new MorphologicalAnalyserBuildException(
                        "Cannot readtraining set.", e);
            }
        }
        
        Trainer trainer;
        trainer = new BaseTrainer(ann, trainingSet, numberOfEpochs, trainingProperties, nnp.getMaximumNumberOfTrainingExamples());
        trainer.addListener(new TrainingProgressListener());
        ann.setProperty(TrainingProgressListener.SHOW_PROGRESS_FORM, Boolean.TRUE);
        try {
            trainer.train();
        } catch (TrainingException e) {
            throw new MorphologicalAnalyserBuildException(
                    "Cannot learn neural network because of errors caused by the training set.", e);
        }
        
        try {
            logger.info("Measuring the quality of the neural network.");
            double quality = trainer.test(nnp.getNumberOfTestExamples(), nnp.getMaximumNumberOfTrainingExamples());
            ann.setQuality(quality);
            logger.info("Neural network quality: " + quality);
        } catch (Exception e) {
            throw new MorphologicalAnalyserBuildException("Cannot test neural network", e);
        }
        
        try {
            trainingSet.close();
        } catch (TrainingSetException e) {
            throw new MorphologicalAnalyserBuildException(
                    "Cannot close training set.", e);
        }
        
        // Save the analyser.
        analyser.setNeuralNetwork(ann);
        analyser.setUseNeuralNetwork(true);
        String analyserPath = getPath(EXTENSION_ANALYSER);
        try {
            logger.info("Saving the analyser to the file: " + analyserPath);
            Object2FilePath.writeObject(analyserPath, analyser, false);
        } catch (IOException e) {
            throw new MorphologicalAnalyserBuildException(
                    "Cannot store analyser in path: " + analyserPath, e);
        }
        
        
//        stemmer.USE_NEURONS = true;
//        stemmer.USE_BASE_FORMS_DICTIONARY = false;
//        //stemmer.neuronsNet.makeTransmiter();
//        ArrayList trainingSet = null;
//        if (buffer){
//            trainingSet = readTrainingSet(MAX_WORDS);
//            System.out.println("Quality before learning ="+testNeuralNetwork(stemmer, trainingSet, TEST_CASES));
//        }
//        double eta = ETA;
//        double alfa = 0;
//        double quality;
//        for (int x=0;x<LEARN_ITERATIONS_COUNT;x++){
//            if (x >= START_ITERATION_NUMBER) {
//                System.out.println("Start neurons network learn with eta = "+eta);
//                quality = stemmerNeuralLearn(stemmer, trainingSet, eta, alfa);
//                System.out.println(" Neurons net quality = "+quality);//+" ; w tym Ľle tworzonych form wyrazów = "+100.0*wrongFormsCount/testCount
//                try{
//                    stemmer.write(PATH_STEMMER);
//                } catch (IOException e){
//                    e.printStackTrace();
//                }
//            }
//            eta = ITERATION_DIVIDER * eta;
//        }
        
        
        logger.info("STEP " + STEP_LEARN_NEURAL_NETWORK
                + " (CREATE LEARN NEURAL NETWORK) finished.");
    }

    /**
     * Test a built analyser.
     *
     * @throws MorpologicalAnalyserBuildException if any processing error occurred.
     */
    public static void createJarPackage() throws MorphologicalAnalyserBuildException {
        logger.info("STEP " + STEP_CREATE_JAR_PACKAGE
                + " (CREATE JAR PACKAGE) started.");
        // open analyser
        String path = getPath(EXTENSION_ANALYSER);
        MorphologicalAnalyserImpl analyser = null;
        try {
            analyser = (MorphologicalAnalyserImpl) FilePath2Object.readObject(path, path.endsWith(".anz"));
        } catch (IOException e) {
            throw new MorphologicalAnalyserBuildException("Cannot open analyser.", e);
        }
        // Save the inflection patterns base.
        saveInflectionPatternsBase(analyser.getIpb(), getPath(EXTENSION_IPB_INTEGRATED));
        
        // load frequent word
        analyser.setUseBaseFormsDictionary(true);
        analyser.setUseNeuralNetwork(true);
        HashMap topWords = new HashMap();
        path = getPath(EXTENSION_FREQUENT_WORDS);
        File f = new File(path);
        if (f.exists() && f.canRead()) {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "utf-8"));
                String line;
                do {
                    line = reader.readLine();
                    if (line != null) {
                        line = line.trim();
                        if (line.length() > 0) {
                            String[] tokens = line.split("\\s|\uFEFF");
                            for (int i = 0; i < tokens.length; i++) {
                                String form = tokens[i].trim();
                                if (form.length() > 0 && !topWords.containsKey(form)) {
                                    MorphologicalAnalysisResult[] result;
                                    try {
                                        result = analyser.analyse(form);
                                        if (result.length == 0) {
                                            logger.log(Level.SEVERE, "There is no result for form : " + form);
                                        } else {
                                            topWords.put(form, Arrays.asList(result));
                                        }
                                        if (result[0].getAccuracy() != MorphologicalAnalyserImpl.PERFECT_MATCHING_WEIGHT) {
                                            logger.log(Level.WARNING, "There is no dictionary entry for the form [" + form 
                                                    + "] which is very popular.");
                                        }
                                    } catch (MorphologyException ex) {
                                        logger.log(Level.SEVERE, "Error while analysing form : " + form, ex);
                                    }
                                }
                            }
                        }
                    }
                } while (line != null);
            } catch (IOException e) {
                throw new MorphologicalAnalyserBuildException("Cannot process a file with most frequently used words", e);
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException ex) {
                        logger.log(Level.WARNING, "Cannot close reader", ex);
                    }
                }
            }
        }
        if (topWords.size() > 0) {
            analyser.setFrequentFormsCache(topWords);
        }
        
        
        MorphologicalAnalyserInfo info = new MorphologicalAnalyserInfo();
        info.setAuthors(INFO_AUTHORS);
        info.setCopyright(INFO_COPYRIGHT);
        info.setDescription(INFO_DESCRIPTION);
        info.setLicence(INFO_LICENCE);
        info.setName(INFO_NAME);
        info.setVendor(INFO_VENDOR);
        info.setWebPage(INFO_WEB_PAGE);
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minutes = cal.get(Calendar.MINUTE);
        int seconds = cal.get(Calendar.SECOND);
        info.setBuildData(Integer.toString(year) + "-" + Integer.toString(month) + "-" + Integer.toString(day) 
                + "T" + Integer.toString(hour) + ":" + Integer.toString(minutes) + ":" + Integer.toString(seconds));
        info.setVersion("" + Integer.toString(year - 2000) + "." + Integer.toString(month) + "." + Integer.toString(day));
        info.setTagged(analyser.getTagset() != null ? "true" : "false");
        info.setQuality(Integer.toString(analyser.getQuality()));
        info.setSpeed(Integer.toString(analyser.getSpeed()));
        info.setDataPath(MorphologicalAnalyserImpl.PATH_MORPHOLOGICAL_ANALYSER_DATA);
        SupportedLocales supl = new SupportedLocales();
        String loc1 = language.toString();
        supl.setSupportedlocale(new String[] {loc1});
        info.setSupportedLocales(supl);
        
        MorphologicalAnalysers xml = new MorphologicalAnalysers();
        xml.addMorphologicalAnalyserInfo(info);
        
        path = getPath(".jar");
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(new FileOutputStream(new File(path)));
            zos.setLevel(9);
            zos.putNextEntry(new ZipEntry(MorphologicalAnalyserImpl.PATH_MORPHOLOGICAL_ANALYSERS_INFO));
            xml.write(zos);
            zos.putNextEntry(new ZipEntry(MorphologicalAnalyserImpl.PATH_MORPHOLOGICAL_ANALYSER_DATA));
            
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            analyser.write(dos);
            dos.close();
            byte[] data = baos.toByteArray();
            zos.write(data);
            zos.closeEntry();
        } catch (Exception e) {
            throw new MorphologicalAnalyserBuildException("Cannot write jar package", e);
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException ex) {
                    logger.log(Level.WARNING, "Cannot close stream", ex);
                }
            }
        }
        
        
        logger.info("STEP " + STEP_CREATE_JAR_PACKAGE
                + " (CREATE JAR PACKAGE) finished.");
    }
    
    /**
     * Test a built analyser.
     *
     * @throws MorpologicalAnalyserBuildException if any processing error occurred.
     */
    public static void testAnalyser() throws MorphologicalAnalyserBuildException {
        logger.info("STEP " + STEP_TEST_ANALYSER + " (TEST ANALYSER) started.");
        // open analyser
        String path = getPath(EXTENSION_ANALYSER);
        MorphologicalAnalyserImpl analyser = null;
        try {
            analyser = (MorphologicalAnalyserImpl) FilePath2Object.readObject(path, path.endsWith(".anz"));
        } catch (IOException e) {
            throw new MorphologicalAnalyserBuildException("Cannot open analyser.", e);
        }
        String inPath = getPath(EXTENSION_FULL_DICTIONARY);
        BufferedReader reader = null; 
        try {
            reader = new BufferedReader(new InputStreamReader(new BufferedInputStream(new FileInputStream(inPath)), "UTF-8"));
        } catch (IOException e) {
            throw new MorphologicalAnalyserBuildException("Cannot open a full dictionary from file:" + inPath, e);
        }

        analyser.setSupplementWeight(22);
        analyser.setCorePatternWeight(11);
        analyser.setIpWeight(4);
        
        analyser.setUseBaseFormsDictionary(false);
        analyser.setUseBaseFormsDictionary(true);
        analyser.setUseNeuralNetwork(false);
//        analyser.setUseNeuralNetwork(true);


//        analyser.setBaseFormsBloomFilter(null);
        
        String line;
        int allFormsCount = 0;
        int correctFormsCount = 0;
        int correctBaseFormsCount = 0;
        int number = 0;
        final int numberOfLinesBetweenInfoMessages = 1000;
        long startTime = System.currentTimeMillis();
        HashMap errorStatistic = new HashMap();
        try {
            do {
                line = reader.readLine();
                if (line != null && line.trim().length() > 0) {
                    line = line.trim();
                    String[] tab = line.split(" ");
                    String baseForm = tab[0];
                    for (int i = 0; i < tab.length; i++) {
                        String form = tab[i];
                        List result;
                        try {
                            result = analyser.analyse2list(form);
                        } catch (MorphologyException ex) {
                            throw new MorphologicalAnalyserBuildException("Cannot perform analysis.", ex);
                        }
                        allFormsCount++;
                        ExtendedMorphologicalAnalysisResult resultPos;
                        String resultForm;
                        int pos = 0;
                        do {
                            resultPos = (ExtendedMorphologicalAnalysisResult) result.get(pos++);
                            resultForm = resultPos.getForm();
                        } while ((!baseForm.equals(resultForm)) && (resultPos.getAccuracy() == MorphologicalAnalyserImpl.PERFECT_MATCHING_WEIGHT) && (pos < result.size()));
                        
                        if (baseForm.equals(resultForm) && (pos == 1 || resultPos.getAccuracy() == MorphologicalAnalyserImpl.PERFECT_MATCHING_WEIGHT)) {
                            correctFormsCount++;
                            if (i == 0) {
                                correctBaseFormsCount++;
                            }
                        } else {
//                            ExtendedInflectionPattern[] ipa = resultPos.getIpa();
//                            for (int j = ipa.length - 1; j >= 0; j--) {
//                                ExtendedInflectionPattern ip = ipa[j];
//                                Integer code = new Integer(ip.getId());
//                                Integer count = (Integer) errorStatistic.get(code);
//                                if (count == null) {
//                                    count = new Integer(1);
//                                } else {
//                                    count = new Integer(count.intValue() + 1);
//                                }
//                                errorStatistic.put(code, count);
//                            }
                            ExtendedInflectionPattern ip = resultPos.getInflectionPattern();
                            if (ip != null) {
                                Integer code = new Integer(ip.getId());
                                Integer count = (Integer) errorStatistic.get(code);
                                if (count == null) {
                                    count = new Integer(1);
                                } else {
                                    count = new Integer(count.intValue() + 1);
                                }
                                errorStatistic.put(code, count);
                            }
                            
                            if (logger.isLoggable(Level.FINE)) {
                                logger.fine("Bad analysis: " +  form + " -> " + 
                                        ((ExtendedMorphologicalAnalysisResult) result.get(0)).getForm());
                            }
                        }
                    }
                    number++;
                    if (logger.isLoggable(Level.FINE) && (number % numberOfLinesBetweenInfoMessages == 0)) {
                        logger.fine("Number of processed words : " + number);
                    }
                    if (logger.isLoggable(Level.INFO) && (number % numberOfLinesBetweenInfoMessages == 0)) {
                        if (allFormsCount > 0) {
                            logger.info(" Accurracy: " + 100.0 * ((double) correctFormsCount) / allFormsCount + "%");
                        }
                        if (number > 0) {
                            logger.info(" Base forms accurracy: " + 100.0 * ((double) correctBaseFormsCount) / number + "%");
                        }
                        long duration = System.currentTimeMillis() - startTime;
                        if (duration > 0) {
                            double speed = 1000.0 * allFormsCount / duration;
                            logger.info("Speed = " + speed);
                        }
                    }
                }
            } while (line != null);
            logger.info("****************** TEST RESULTS ****************");
            if (allFormsCount > 0) {
                logger.info(" Accurracy: " + 100.0 * ((double) correctFormsCount) / allFormsCount + "%");
            }
            if (number > 0) {
                logger.info(" Base forms accurracy: " + 100.0 * ((double) correctBaseFormsCount) / number + "%");
            }
            long duration = System.currentTimeMillis() - startTime;
            if (duration > 0) {
                double speed = 1000.0 * allFormsCount / duration;
                logger.info("Speed = " + speed);
            }
            
            // write error statistic
            String statisticPath = getPath(EXTENSION_ERROR_STATISTIC);
            PrintWriter writer = null; 
            try {
                writer = new PrintWriter(new BufferedOutputStream(new FileOutputStream(statisticPath)));
                for (final Iterator eit = errorStatistic.entrySet().iterator(); eit.hasNext();) {
                    Map.Entry entry = (Map.Entry) eit.next();
                    Integer code = (Integer) entry.getKey();
                    Integer count = (Integer) entry.getValue();
                    writer.println(code.toString() + ',' + count.toString());
                }
            } catch (IOException e) {
                throw new MorphologicalAnalyserBuildException("Cannot write error statistic to file: " + statisticPath, e);
            } finally {
                if (writer != null) {
                    writer.close();
                }
            }
            
            
            
            
        } catch (IOException e) {
            throw new MorphologicalAnalyserBuildException("Cannot create inflection pattern base.", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    logger.log(Level.SEVERE, "Cannot properly close input stream.", e);
                }
            }
        }
        logger.info("STEP " + STEP_TEST_ANALYSER + " (TEST ANALYSER) finished.");
    }
    
    
    /**
     * Check if the given step should be executed.
     *
     * @param step  The step to check.
     *
     * @return <code>true</code> if the given step should be executed according to {@link fromStep}
     *      and {@link toStep} constants.
     */
    private static boolean isStepExecuted(final int step) {
        return step >= fromStep && step <= toStep;
    }
    
    /**
     * Runs steps of the generation process of a morphological analyser.
     *
     *
     * @throws MorpologicalAnalyserBuildException if any processing error occurred.
     */
    public static void processLanguage() throws MorphologicalAnalyserBuildException {
        DynamicCharset charset = new DynamicCharset("international", new String[0]);
        charset.setInternational();
        vowels = new VowelCharactersImpl(language, charset);
        if (isStepExecuted(STEP_GENERATE_FULL_DICTIONARY)) {
            if (symbol.startsWith("full_")) {
                regenerateFullDictionary();
            } else {
                generateFullDictionary();
            }
        }
        if (isStepExecuted(STEP_CREATE_INFLECTION_PATTERNS_BASE)) {
            createInflectionPatternsBase();
        }
        if (isStepExecuted(STEP_MORPHEMES_STATISTICS)) {
            printMorphemesStatistic();
        }
        if (isStepExecuted(STEP_TAG_INFLECTION_PATTERNS)) {
            tagInflectionPatterns();
        }
        if (isStepExecuted(STEP_DETERMINE_CORE_PATTERNS)) {
            determineCorePatterns();
        }
        if (isStepExecuted(STEP_CREATE_ANALYSER)) {
            createAnalyser(charset);
        }
        if (isStepExecuted(STEP_CREATE_TRAINING_SET)) {
            createTrainingSet();
        }
        if (isStepExecuted(STEP_LEARN_NEURAL_NETWORK)) {
            learnNeuralNetwork();
        }
        if (isStepExecuted(STEP_CREATE_JAR_PACKAGE)) {
            createJarPackage();
        }
        if (isStepExecuted(STEP_TEST_ANALYSER)) {
            testAnalyser();
        }
    }
    
    public static void processLanguage(String dirPath, String symb, Locale lang, int from, int to) {
        language = lang;
        symbol = symb;
        dictionariesPath = dirPath;
        fromStep = from;
        toStep = to;
        processingThread = new Thread(new MorphologicalAnalyserBuilder(), "Morphological Ananlysis Processing Thread");
        processingThread.start();
    }
    
    /**
     * Performs the generation process of a morphological analyser.
     *
     * @param   args    The array of command line arguments.
     */
    public static void main(final String[] args) {
        for (int i = 0; i < dictionaries.length; i++) {
            language = languages[i];
            symbol = dictionaries[i];
            try {
                processLanguage();
            } catch (MorphologicalAnalyserBuildException e) {
                logger.log(Level.SEVERE, "Cannot process dictionary " + symbol
                        + ". The processing exception occurred.", e);
            }
        }
    }

    public void run() {
        try {
            processLanguage();
        } catch (MorphologicalAnalyserBuildException e) {
            logger.log(Level.SEVERE, "Cannot process dictionary " + symbol
                    + ". The processing exception occurred.", e);
        }
    }
    
}