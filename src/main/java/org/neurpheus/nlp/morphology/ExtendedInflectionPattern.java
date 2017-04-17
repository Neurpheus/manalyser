/*
 * Neurpheus - Morfological Analyser
 *
 * Copyright (C) 2006 Jakub Strychowski
 *
 *  This library is free software; you can redistribute it and/or modify it
 *  under the terms of the GNU Lesser General License as published by the Free
 *  Software Foundation; either version 2.1 of the License, or (at your option)
 *  any later version.
 *
 *  This library is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *  or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General License
 *  for more details.
 */
package org.neurpheus.nlp.morphology;

import java.io.PrintStream;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.neurpheus.nlp.morphology.inflection.CorePattern;
import org.neurpheus.nlp.morphology.inflection.FormPattern;

/**
 *  Represents an inflection pattern.
 *  <p>
 *  An inflection patterns is a set of rules which allow to create any form of a word covered by this pattern.
 *  The inflection pattern consist of a set of morpheme groups called supplements. When a particular supplement
 *  is combined with the core of a word, a particular form of the word is created. A base supplement is a
 *  supplement which creates the base form of a word.
 *  </p>
 *  <p>
 *  Objects belongign to this class stores also cores of words which are covered by the inflection patterns.
 *  Similar cores covered by the inflection pattern are described by a common core pattern.
 *  All core patterns are also stored in instances of this class.
 *  </p>
 *
 * @author Jakub Strychowski
 */
public interface ExtendedInflectionPattern {
    
    /**
     *  Returns a set of cores covered by this inflection pattern.
     *
     * @return a collection of String objects.
     */
    Collection getCoveredCores();
    
    /**
     *  Sets a set of cores covered by this inflection pattern.
     *
     * @param newCoveredCores  All known cores covered by this inflection pattern in the form
     *                         of a Strings collection.
     */
    void setCoveredCores(Collection newCoveredCores);
    
    /**
     * Returns an unique code of this inflection pattern.
     * Two inflection patterns with the same code are merged in an inflection pattern base.
     *
     * In this implementation the code consist of sorted supplements of this inflection pattern.
     *
     * @return The String object which represents this inflection patern.
     */
    String getCode();

    /**
     * Returns a string containg all affixes from this pattern separated by space character.
     *
     * @return All affixes of this pattern separated by space character.
     */
    String getAffixesString();
    
    /**
     *  Adds the given cores to the set of all known cores covered by this inflection pattern.
     *
     * @param newCores new cores covered by this inflection pattern.
     */
    void addCoveredCores(Collection newCores);
    
    
    /**
     * Returns a collection of supplements belonging to this
     * inflection pattern without the bass supplement.
     *
     * @return The collection of String objects.
     */
    List getOtherFormPatterns();
    
    
    /**
     * Sets the set of the supplements (without the base supplement) which belong
     * to this inflection pattern. This method should store supplements as a sorted list
     * available by the {@link getOtherSupplements} method.
     *
     * @param newSupplements The collection of String objects.
     */
    void setOtherFormPatterns(Collection newSupplements);
    
    /**
     * Returns the base supplement of this inflection pattern.
     *
     * @return The base supplement.
     */
    FormPattern getBaseFormPattern();
    
    /**
     * Sets new base supplement for this inflection pattern.
     *
     * @param newBaseSupplement The new value of the base supplement.
     */
    void setBaseFormPattern(FormPattern newBaseFormPattern);
    
    /**
     * Returns all supplements of this inflection pattern (including a base supplement).
     * The base supplements occurrs at the first position in the list.
     * Other supplements are ordered alphabetically.
     *
     * @return Collection of String objects.
     */
    Collection getAllFormPatterns();
    
    /**
     * Determines core patterns analysing words covered by this inflection pattern.
     *
     *
     * @param  vowels  Represents the vowels of a natural language.
     * @return The set of strings representing determined core patterns.
     */
    Set determineCorePatterns(VowelCharacters vowels);
    
    
    /**
     * Returns a collection of core patterns covered by this inflection pattern.
     *
     * @return a collection of {@link CorePattern} objects.
     */
    Collection getCorePatterns();
    
    
    /**
     * Sets a new collection of core patterns covered by this inflection pattern.
     *
     * @param newCorePatterns a collection of {@link CorePattern} objects.
     */
    void setCorePatterns(Collection newCorePatterns);
    
    /**
     * Returns a number of lexemes covered by this inflection pattern.
     *
     * @return the number of covered lexemes.
     */
    int getNumberOfCoveredLexemes();
    
    /**
     * Sets a new value for the a number of lexemes covered by this inflection pattern.
     *
     * @param newNumberOfCoveredLexemes The new value of the number of covered lexemes.
     */
    void setNumberOfCoveredLexemes(int newNumberOfCoveredLexemes);
    
    
    /**
     * Returns the integer identifier of this inflection pattern.
     *
     * @return The unique identifier of the IP in the scope of a IPB.
     */
    int getId();
    
    
    /**
     * Sets the new value for the integer identifier of this inflection pattern.
     *
     * @param newId The new value of the identifier in the scope of a IPB.
     */
    void setId(int newId);
    
    /**
     *  Logs out the inflection pattern.
     *
     * @param out           The output stream.
     * @param withCores     if <code>true</code>, logs out also cores covered by
     *                      this inflection pattern.
     * @param number        The position of this inflection pattern.
     * @param sumCores      The number of cores covered by already logged out inflection patterns.
     * @param totalCores    The total number of cores.
     */
    void print(PrintStream out, boolean withCores, int number, int sumCores, int totalCores);
    
    
    /**
     * Returns the length of a longest core pattern.
     *
     * @return the length of a longest core pattern.
     */
    int getMaxLengthOfCorePattern();
    
    /**
     * Returns the length of a longest supplement.
     *
     * @return the length of a longest supplement.
     */
    int getMaxLengthOfSupplement();
    
}
