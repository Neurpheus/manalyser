/*
 *  © 2017 Jakub Strychowski
 */

package org.neurpheus.nlp.morphology.builder;

/**
 * Sequence of steps executed by the analyzer build process.
 *
 * @author Jakub Strychowski
 */
public enum MorphologicalAnalyserBuildStep {

    /** Generation of  a full dictionary from a MySpell dictionary or other input dictionary. */
    STEP_GENERATE_FULL_DICTIONARY,
    
    /** Builder creates an inflection patterns base from a full dictionary. */
    STEP_CREATE_INFLECTION_PATTERNS_BASE,

    /** Builder logs out a statistic about morpheme usage in an analyzed language. */
    STEP_MORPHEMES_STATISTICS,
    
    /** Builder logs out a statistic about morpheme usage in an analyzed language. */
    STEP_TAG_INFLECTION_PATTERNS,
    
    /** Builder determines a set of core patterns for each inflection pattern. */
    STEP_DETERMINE_CORE_PATTERNS,
    
    /** Builder creates an inflection tree for the inflection pattern base. */
    STEP_CREATE_ANALYSER,
    
    /** Builder creates a training set for a neural network learning process. */
    STEP_CREATE_TRAINING_SET,
    
    /** Builder trains a neural network. */
    STEP_LEARN_NEURAL_NETWORK,
    
    /** Builder creates a distribution file with the analyzer. */
    STEP_CREATE_JAR_PACKAGE,
    
    /** Builder tests built analyzer. */
    STEP_TEST_ANALYSER;

}
