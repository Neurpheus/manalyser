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
package org.neurpheus.nlp.morphology.inflection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import java.util.logging.Level;
import org.neurpheus.core.io.DataOutputStreamPacker;
import org.neurpheus.nlp.morphology.ExtendedInflectionPattern;
import org.neurpheus.nlp.morphology.VowelCharacters;
import org.neurpheus.nlp.morphology.builder.MorphologicalAnalyserBuildHelper;
import org.neurpheus.nlp.morphology.impl.MorphologicalAnalyserImpl;
import org.neurpheus.nlp.morphology.VowelCharactersImpl;
import static org.neurpheus.nlp.morphology.impl.MorphologicalAnalyserImpl.WILDCARD_CHARACTER;

/**
 * Represents an inflection pattern (IP).
 * <p>
 * An inflection patterns is a set of rules which allow to create any form of a lexeme (word) which
 * is covered by this pattern. The inflection pattern consist of a set of morpheme groups called
 * supplements. When a particular supplement is combined with the core of a lexeme, a particular
 * form of the lexeme is created. A base supplement is a supplement which creates the base form of a
 * lexeme (a lemma).
 * </p>
 * <p>
 * An inflection pattern can hold also a collection of cores of lexemes which are covered by this
 * IP. An IP holds also core patterns which represents similar cores covered by this IP.
 * </p>
 *
 * @author Jakub Strychowski
 */
public class InflectionPatternImpl implements Comparable, ExtendedInflectionPattern, Serializable {

    /** Unique serialization identifier of this class. */
    static final long serialVersionUID = 770608060903120420L;

    /** Holds logger for this class. */
    private static Logger logger = Logger.getLogger(InflectionPatternImpl.class.getName());

    /** Holds a base supplement of the inflection pattern. */
    private FormPattern baseFormPattern;

    /** Holds all other supplements belonging to this inflection pattern. */
    private List otherFormPatterns;

    /** Holds all cores covered by this inflection pattern as collection of Strings. */
    private Collection coveredCores;

    /** Holds core patterns. */
    private Collection corePatterns;

    /** Holds the number of lexemes covered by this pattern. */
    private int numberOfCoveredLexemes;

    /**
     * Holds the identifier of the inflection pattern. Identifier should be unique in the scope of
     * an inflection patterns base holding this object.
     */
    private int id;

    private static AtomicInteger idGenerator = new AtomicInteger();
    
    /**
     * Creates a new instance of InflectionPattern.
     */
    public InflectionPatternImpl() {
    }

    /**
     * Creates a new instance of InflectionPattern analysing given array of all forms of a lexeme.
     *
     * @param forms          An array which contains all forms of a lexeme.
     * @param acceptInfixes  if <code>true</code>, created inflection pattern can contain infixes in
     *                       the supplements.
     * @param acceptPrefixes if <code>true</code>, created inflection pattern can contain prefioxes
     *                       in the supplements.
     */
    public InflectionPatternImpl(
            final String[] forms, final boolean acceptInfixes, final boolean acceptPrefixes) {
        init(forms, acceptInfixes, acceptPrefixes);
    }

    public InflectionPatternImpl(
            final String line, final boolean acceptInfixes, final boolean acceptPrefixes) {
        String[] forms = line.split("\\s");
        init(forms, acceptInfixes, acceptPrefixes);
        
    }

    /**
     * Initializes this inflection pattern analysing the given array of all forms of a lexeme.
     *
     * @param forms          array which contains all forms of a word.
     * @param acceptInfixes  if <code>true</code>, the inflection pattern can contain infixes in the
     *                       supplements
     * @param acceptPrefixes if <code>true</code>, the inflection pattern can contain prefixes in
     *                       the supplements
     */
    private void init(
            final String[] forms, final boolean acceptInfixes, final boolean acceptPrefixes) {
        id = idGenerator.getAndIncrement();
        HashSet tmp = new HashSet();
        coveredCores = new HashSet();
        corePatterns = new HashSet();
        baseFormPattern = null;
        // Determine the core of a given forms and add it to the set of cores covered by this
        // inflection pattern. This code determines also the base supplement and other supplements
        // which can be created from the forms and the determined core.
        if (forms.length > 0) {
            String core = MorphologicalAnalyserBuildHelper.determineCore(forms, acceptInfixes,
                                                                         acceptPrefixes);
            coveredCores.add(core);
            for (int i = 0; i < forms.length; i++) {
                String supp = MorphologicalAnalyserBuildHelper.getSupplement(forms[i], core);
                if (i == 0) {
                    baseFormPattern = new FormPattern(supp);
                } else {
                    tmp.add(new FormPattern(supp));
                }
            }
        }
        if (baseFormPattern == null) {
            baseFormPattern = new FormPattern();
        }
        // Sets the other supplements - sorts the other supplements.
        setOtherFormPatterns(tmp);
        numberOfCoveredLexemes = coveredCores.size();
    }

    /**
     * Returns a set of cores covered by this inflection pattern.
     *
     * @return a collection of String objects.
     */
    public Collection getCoveredCores() {
        return coveredCores;
    }

    /**
     * Sets a set of cores covered by this inflection pattern.
     *
     * @param newCoveredCores All known cores covered by this inflection pattern in the form of a
     *                        Strings collection.
     */
    public void setCoveredCores(final Collection newCoveredCores) {
        coveredCores = newCoveredCores;
        if (coveredCores != null) {
            setNumberOfCoveredLexemes(coveredCores.size());
        }
    }

    /**
     * Returns an unique code of this inflection pattern. Two inflection patterns with the same code
     * are merged in an inflection pattern base.
     *
     * In this implementation the code consist of sorted supplements of this inflection pattern.
     *
     * @return The String object which represents this inflection patern.
     */
    public String getCode() {
        StringBuilder res = new StringBuilder();
        if (baseFormPattern != null) {
            res.append(baseFormPattern.getAffixes());
        }
        ArrayList tmp = new ArrayList(getOtherFormPatterns());
        Collections.sort(tmp);
        for (Iterator it = tmp.iterator(); it.hasNext();) {
            res.append(((FormPattern) it.next()).getAffixes());
        }
        return res.toString();
    }

    /**
     * Returns a string containg all affixes from this pattern separated by space character.
     *
     * @return All affixes of this pattern separated by space character.
     */
    public String getAffixesString() {
        StringBuffer res = new StringBuffer();
        if (baseFormPattern != null) {
            res.append(baseFormPattern.getAffixes());
        }
        ArrayList tmp = new ArrayList(getOtherFormPatterns());
        Collections.sort(tmp);
        for (Iterator it = tmp.iterator(); it.hasNext();) {
            if (res.length() > 0) {
                res.append(' ');
            }
            res.append(((FormPattern) it.next()).getAffixes());
        }
        return res.toString();
    }

    /**
     * Adds the given cores to the set of all known cores covered by this inflection pattern.
     *
     * @param newCores new cores covered by this inflection pattern.
     */
    public void addCoveredCores(final Collection newCores) {
        coveredCores.addAll(newCores);
        setNumberOfCoveredLexemes(coveredCores.size());
    }

    /**
     * Compares this inflection pattern with other one.
     *
     * @param o The inflection pattern which has to be compared with this object.
     *
     * @return <code>0</code> if both inflection patterns are equal, <code>1</code> if this pattern
     *         is before the given pattern in sort order, and <code>-1</code> if this pattern if
     *         after the given pattern in sort order.
     */
    public int compareTo(final Object o) {
        if (o != null && o instanceof ExtendedInflectionPattern) {
            ExtendedInflectionPattern y = (ExtendedInflectionPattern) o;
            int res = y.getNumberOfCoveredLexemes() - getNumberOfCoveredLexemes();
            if (res == 0) {
                return -1 * getCode().compareTo(y.getCode());
            } else if (res < 0) {
                return -1;
            } else {
                return 1;
            }
        } else {
            return -1;
        }
    }

    /**
     * Uses this constant to convert fractions into values in percents.
     */
    private static final double CONVERT_TO_PERCENT = 100.0;

    /**
     * Logs out the inflection pattern.
     *
     * @param out        The output stream.
     * @param withCores  if <code>true</code>, logs out also cores covered by this inflection
     *                   pattern.
     * @param number     The position of this inflection pattern.
     * @param sumCores   The number of cores covered by already logged out inflection patterns.
     * @param totalCores The total number of cores.
     */
    public void print(final PrintStream out, final boolean withCores,
                      final int number, final int sumCores, final int totalCores) {

        out.print(number);
        out.print(". ");
        out.print(toString());
        out.println();

        int count = getNumberOfCoveredLexemes();
        out.println("number of covered lexemes : " + count + " -> "
                + (CONVERT_TO_PERCENT * count / totalCores) + "%");
        out.println("number of lexemes covered by this an previous IPs : "
                + (CONVERT_TO_PERCENT * (count + sumCores) / totalCores) + "%");

        if (withCores) {
            out.print("cores of covered lexemes :");
            for (Iterator it = getCoveredCores().iterator(); it.hasNext();) {
                out.print(it.next().toString());
                out.print(' ');
            }
            out.println();
        }

        out.print("core patterns of covered lexemes : ");
        for (Iterator it = getCorePatterns().iterator(); it.hasNext();) {
            CorePattern cp = (CorePattern) it.next();
            cp.print(out);
            out.print(' ');
        }
        out.println();
    }

    /**
     * Returns a collection of supplements belonging to this inflection pattern without the bass
     * supplement.
     *
     * @return The collection of String objects.
     */
    public List getOtherFormPatterns() {
        return otherFormPatterns;
    }

    /**
     * Sets the set of the supplements (without the base supplement) which belong to this inflection
     * pattern. This method should store supplements as a sorted list available by the
     * {@link getOtherSupplements} method.
     *
     * @param newSupplements The collection of String objects.
     */
    public void setOtherFormPatterns(final Collection newFormPatterns) {
        ArrayList tmp = new ArrayList(newFormPatterns);
        Collections.sort(tmp);
        tmp.trimToSize();
        otherFormPatterns = tmp;
    }

    /**
     * Returns the base supplement of this inflection pattern.
     *
     * @return The base supplement.
     */
    public FormPattern getBaseFormPattern() {
        return baseFormPattern;
    }

    /**
     * Sets new base supplement for this inflection pattern.
     *
     * @param newBaseSupplement The new value of the base supplement.
     */
    public void setBaseFormPattern(final String newBaseFormPattern) {
        baseFormPattern = new FormPattern(newBaseFormPattern);
    }

    /**
     * Sets new base supplement for this inflection pattern.
     *
     * @param newBaseSupplement The new value of the base supplement.
     */
    public void setBaseFormPattern(final FormPattern newBaseFormPattern) {
        baseFormPattern = newBaseFormPattern;
    }

    /**
     * Returns all supplements of this inflection pattern (including a base supplement). The base
     * supplements occurrs at the first position in the list. Other supplements are ordered
     * alphabetically.
     *
     * @return Collection of String objects.
     */
    public Collection getAllFormPatterns() {
        ArrayList res = new ArrayList(otherFormPatterns.size() + 1);
        res.add(baseFormPattern);
        res.addAll(otherFormPatterns);
        return res;
    }

    /**
     * Determines core patterns analyzing words covered by this inflection pattern.
     *
     * Patterns Determination Algorithm:
     * <ul>
     * <li>1. Create an empty set R which will contain result patterns.</li> 
     * <li>2. Get list of cores which will be analyzed. </li> 
     * <li>3. If number of analyzed words is too small, finish with empty result. </li> 
     * <li>4. Get as a current pattern, the pattern which covers all words (empty pattern) </li> 
     * <li>5. Find the best subset of patterns which can replace current pattern and add all patterns from the subset to the
     * result set. Subset containing only one empty pattern is not allowed. </li> 
     * <li>6. Remove useless lemma patterns. </li> 
     * <li>7. Return the result.</li> 
     * </ul>
     *
     * @param vowels Represents the vowels of a natural language.
     *
     * @return A set of strings.
     */
    @Override
    public Set determineCorePatterns(final VowelCharacters vowels) {

        // 1.  Create an empty set R which will contain result patterns.
        Set result = new HashSet();

        // 2.  Get list of words wich will be analysed.
        ArrayList cores = new ArrayList();
        cores.addAll(getCoveredCores());

        // 3.  If number of analysed words is too small, finish with empty result.
        if (cores.size() <= 2) {
            return result;
        }

        // 4.  Get as a current pattern, the pattern which covers all words ("*").
        CorePattern cp = new CorePattern();
        cp.setPattern("");
        cp.setWeight(cores.size());

        // 5.  Find the best subset of patterns which can replace current pattern and add
        //     all patterns from the subset to the result set.
        //     Subset containng only one empty pattern is not allowed.
        findBestPatternSubset(result, cp, cores);
        if (result.size() == 1 && result.contains(cp)) {
            result = Collections.EMPTY_SET;
        }

        setCorePatterns(result);

        // 6. Remove useless lemma patterns.
        removeUseless(vowels);

        // 7.  Return the result.
        return result;
    }

    /**
     * Holds the maximum size of a subset which covers the same cores which are covered by the core
     * pattern from with the subset was created. The size of a subset can be greated if a large set
     * of cores is covered. In fact real number of elements in a subset cannot be greater then this
     * parameter plus number of covered cores divaded by the CORES_PATTERN_SIZE_DIVIDE_BY parameter. */
    private static final int MAXIMUM_SIZE_OF_CORE_PATTERNS_SUBSET = 20;

    /**
     * Holds the value by with the number of elements in a subset is divaded to check if the subset
     * is not too specialized. See {@link MAXIMUM_SIZE_OF_CORE_PATTERNS_SUBSET).
     */
    private static final int CORES_PATTERN_SIZE_DIVIDE_BY = 1000;

    /**
     * If a mean number of cores covered by a single core patterns in a subset is less then this
     * parameter the subset is too specialized.
     */
    private static final float MINIMUM_NUMBER_OF_CORES_COVERED_BY_SINGLE_PATERN_IN_SUBSET = 3.0f;

    /**
     * If the size of a subset is greater then this value the mean number of cores covered by a
     * single pattern from subset cannot be smaller then {@Link LONG_PATTERN_MEAN_COVERED_CORES} and
     * patterns canno be longer then {@link LONG_PATTERN_LENGTH parameter}.
     */
    private static final int LONG_PATTERN_MAXIMUM_SIZE_OF_SUBSET = 4;

    /**
     * Maximum length of patterns for a subset having many elements. See
     * {@link LONG_PATTERN_MAXIMUM_SIZE_OF_SUBSET}.
     */
    private static final int LONG_PATTERN_LENGTH = 4;

    /**
     * Minmum number of cores covered by a single pattern in a subset having many elements. See
     * {@link LONG_PATTERN_MAXIMUM_SIZE_OF_SUBSET}.
     */
    private static final float LONG_PATTERN_MEAN_COVERED_CORES = 4.0f;

    /**
     * Finds the bes subset of paterns which effectivly replaces given pattern. This method analysys
     * given cores and tries to find the best set of more specialised patterns which can replace the
     * input pattern.
     *
     * Following algorithm is used:
     *
     * 1. Create an empty subset of patterns.
     *
     * 2. For each analysed core do:
     *
     * 2.1. If the analysed core is covered by the input pattern and is is possible to create a new
     * pattern which extends the input one, then:
     *
     * 2.1.1. Create the new pattern adding one letter from the core to the input pattern.
     *
     * 2.1.2. If the subset of patterns contains input pattern, increase the weight of the pattern
     * in the subset, if not, add new pattern to the subset.
     *
     * 3. Check if the created subset of patterns can effectivly replace current pattern. The subset
     * can replace the current pattern if current pattern dose not contain any letter. The subset
     * cannot replace the current pattern if one of the following conditions is true :
     *
     * 3.a. An average number of cores covered by single pattern from the subset is lower then 3.
     * 3.b. Number of patterns in the subset is greater then 9. 3.c. Number of patterns in the
     * subset is greater then 4 and an average number of cores covered by single pattern from the
     * subset is lower then 4. 3.d. Subset is empty.
     *
     * 4. If the subset effectivly replaces the current pattern :
     *
     * 4.1. Remove the current pattern form the result set
     *
     * 4.2. Add all patterns from the subset to the result set
     *
     * 4.3. For each pattern from the subset try to find its subset
     *
     * @param result the global set of result patterns
     * @param cp     input pattern to replace
     * @param cores  list of cores covered by the pattern
     */
    private void findBestPatternSubset(
            final Set result, final CorePattern cp, final ArrayList cores) {

        String pattern = cp.getPattern();

        //  1.  Create an empty subset of patterns.
        int weightSum = 0;
        HashMap subset = new HashMap();

        //  2.  For each analysed core do:
        for (Iterator it = cores.iterator(); it.hasNext();) {
            String core = (String) it.next();
            //  2.1.  If the analysed core is covered by the input pattern and is is possible
            //        to create a new pattern which extends the input one, then:
            if ((core.length() > pattern.length()) && (core.endsWith(pattern))) {
                weightSum++;

                // 2.1.1.  Create the new pattern adding one letter from the core
                // to the input pattern.
                CorePattern newcp = new CorePattern();
                String newPattern
                        = core.substring(core.length() - pattern.length() - 1, core.length());
                newcp.setPattern(newPattern);
                newcp.setWeight(1);

                // 2.1.2.  If the subset of patterns contains input pattern, increase the weight of
                //         the pattern in the subset, if not, add new pattern to the subset.
                if (subset.containsKey(newPattern)) {
                    CorePattern tmpcp = (CorePattern) subset.get(newPattern);
                    tmpcp.setWeight(tmpcp.getWeight() + 1);
                } else {
                    subset.put(newPattern, newcp);
                }
            }
        }

        //  3.  Check if the created subset of patterns can effectivly replace current pattern.
        //      The subset can replace the current pattern if current pattern dose not contain
        //      any letter. The subset cannot replace the current pattern if one of the
        //      following conditions is true :
        //
        //      3.a. An average number of cores covered by single pattern from the subset
        //           is lower then 3.
        //      3.b. Number of patterns in the subset is greater then 9.
        //      3.c. Number of patterns in the subset is greater then 4 and an average number of
        //           cores covered by single pattern from the subset is lower then 4.
        //      3.d. Subset is empty.
        boolean subsetRight = true;
        if (!(pattern.equals("") || pattern.equals(WILDCARD_CHARACTER))) {
            if (subset.size() == 0
                    || weightSum < cp.getWeight()
                    || subset.size() > MAXIMUM_SIZE_OF_CORE_PATTERNS_SUBSET
                    + cp.getWeight() / CORES_PATTERN_SIZE_DIVIDE_BY) {
                subsetRight = false;
            } else {
                float medCores = ((float) (cp.getWeight())) / subset.size();
                if (medCores < MINIMUM_NUMBER_OF_CORES_COVERED_BY_SINGLE_PATERN_IN_SUBSET) {
                    subsetRight = false;
                } else if (subset.size() > LONG_PATTERN_MAXIMUM_SIZE_OF_SUBSET
                        && (medCores < LONG_PATTERN_MEAN_COVERED_CORES
                        || pattern.length() >= LONG_PATTERN_LENGTH)) {
                    subsetRight = false;
                }
            }
        }

        //  4. If the subset effectivly replaces the current pattern :
        if (subsetRight) {
            // 4.1. Remove the current pattern form the result set
            result.remove(cp);
            // 4.2. Add all patterns from the subset to the result set
            result.addAll(subset.values());
            // 4.3. For each pattern from the subset try to find its subset
            for (Iterator it = subset.values().iterator(); it.hasNext();) {
                findBestPatternSubset(result, (CorePattern) (it.next()), cores);
            }
        }
    }

    /**
     * Removes lemmas and lemma patterns which are useless.
     *
     * This method removes lemma patterns which covers less then two words. After that, the method
     * removes all lemmas covered by throws remaining lemma patterns. Finall product og this
     * algorithm is a set of lemma patterns and a set of lemmas which are not covered by any lemma
     * pattern.
     *
     * @param vowels Represents the vowels in a natural language.
     */
    private void removeUseless(final VowelCharacters vowels) {
        // Removing lemma patterns which covers less then two lemmas.
        for (Iterator it = corePatterns.iterator(); it.hasNext();) {
            CorePattern lemmaPattern = (CorePattern) it.next();
            if (lemmaPattern.getWeight() <= 1) {
                if (logger.isLoggable(Level.FINE)) {
                    logger.fine("--- Removing lemma pattern : "
                            + lemmaPattern.getPattern().toString());
                }
                it.remove();
            } else {
                lemmaPattern.removeCoveredCores(getCoveredCores(), vowels);
            }
        }
    }

    /**
     * Returns a collection of core patterns covered by this inflection pattern.
     *
     * @return a collection of {@link CorePattern} objects.
     */
    public Collection getCorePatterns() {
        return corePatterns;
    }

    /**
     * Sets a new collection of core patterns covered by this inflection pattern.
     *
     * @param newCorePatterns a collection of {@link CorePattern} objects.
     */
    public void setCorePatterns(final Collection newCorePatterns) {
        corePatterns = newCorePatterns;
    }

    /**
     * Returns a number of lexemes covered by this inflection pattern.
     *
     * @return the number of covered lexemes.
     */
    public int getNumberOfCoveredLexemes() {
        return numberOfCoveredLexemes;
    }

    /**
     * Sets a new value for the a number of lexemes covered by this inflection pattern.
     *
     * @param newNumberOfCoveredLexemes The new value of the number of covered lexemes.
     */
    public void setNumberOfCoveredLexemes(final int newNumberOfCoveredLexemes) {
        numberOfCoveredLexemes = newNumberOfCoveredLexemes;
    }

    /**
     * Returns the string representation of this inflection pattern. This method returns a string
     * containing succeeding supplements.
     *
     * @return The string representation of the inflection pattern.
     */
    public String toString() {
        StringBuffer res = new StringBuffer();
        res.append("{");
        res.append(getBaseFormPattern().toString());
        res.append("}");
        for (Iterator it = getOtherFormPatterns().iterator(); it.hasNext();) {
            res.append(", {");
            res.append(it.next().toString());
            res.append("}");
        }
        return res.toString();
    }

    /**
     * Returns the integer identifier of this inflection pattern.
     *
     * @return The unique identifier of the IP in the scope of a IPB.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the new value for the integer identifier of this inflection pattern.
     *
     * @param newId The new value of the identifier in the scope of a IPB.
     */
    public void setId(final int newId) {
        id = newId;
    }

    /**
     * Returns the length of a longest core pattern.
     *
     * @return the length of a longest core pattern.
     */
    public int getMaxLengthOfCorePattern() {
        int res = 0;
        for (Iterator it = getCorePatterns().iterator(); it.hasNext();) {
            CorePattern cp = (CorePattern) it.next();
            int len = cp.getPattern().length();
            if (cp.getPattern().indexOf(MorphologicalAnalyserImpl.WILDCARD) >= 0) {
                --len;
            }
            if (len > res) {
                res = len;
            }
        }
        return res;
    }

    /**
     * Returns the length of a longest supplement.
     *
     * @return the length of a longest supplement.
     */
    public int getMaxLengthOfSupplement() {
        int res = baseFormPattern.getAffixes().length();
        if (baseFormPattern.getAffixes().indexOf(MorphologicalAnalyserImpl.WILDCARD) >= 0) {
            --res;
        }
        for (Iterator it = getOtherFormPatterns().iterator(); it.hasNext();) {
            String s = ((FormPattern) it.next()).getAffixes();
            int len = s.length();
            if (s.indexOf(MorphologicalAnalyserImpl.WILDCARD) >= 0) {
                --len;
            }
            if (len > res) {
                res = len;
            }
        }
        return res;
    }

//    /**
//     *  Saves the inflection pattern to the output stream in the binary form.
//     *
//     *  @param out output stram
//     *  @throws IOException when error occurred while saving
//     */
//    public void save(DataOutputStream out) throws IOException {
//        // write base form supplement
//        out.writeUTF(getBaseSupplement());
//
//        // write other supplements
//        out.writeInt(otherSupplements.size());
//        for (Iterator it = otherSupplements.iterator(); it.hasNext(); ) {
//            out.writeUTF((String) it.next());
//        }
//
//        // write cores covered by the inflection pattern
//        out.writeInt(coveredCores.size());
//        for (Iterator it = coveredCores.iterator(); it.hasNext(); ) {
//            out.writeUTF((String)it.next());
//        }
//
//        // write core patterns
//        out.writeInt(corePatterns.size());
//        for (Iterator it = corePatterns.iterator(); it.hasNext(); ) {
//            ((CorePattern)it.next()).save(out);
//        }
//
//        // write number of covered lexemes
//        out.writeInt(getNumberOfCoveredLexemes());
//    }
//
//    /**
//     *  Loads the inflection pattern base from the input stream.
//     *
//     *  @param in input stream providing IPB in the binary form.
//     *  @throws IOException when error occurred while loading.
//     */
//    public void load(DataInputStream in) throws IOException {
//        // load base form supplement
//        setBaseSupplement(in.readUTF());
//
//        // load other supplements
//        otherSupplements.clear();
//        int count = in.readInt();
//        while (count-- > 0) {
//            otherSupplements.add(in.readUTF());
//        }
//
//        // load covered cores
//        coveredCores.clear();
//        count = in.readInt();
//        while (count-- > 0) {
//            coveredCores.add(in.readUTF());
//        }
//
//        // load core patterns
//        corePatterns.clear();
//        count = in.readInt();
//        while (count-- > 0) {
//            CorePattern cp = new CorePattern();
//            cp.load(in);
//        }
//
//        // reads number of covered lexemes
//        setNumberOfCoveredLexemes(in.readInt());
//
//    }
    /**
     * Reads this object data from the given input stream.
     *
     * @param in The input stream where this IPB is stored.
     *
     * @throws IOException            if any read error occurred.
     * @throws ClassNotFoundException if this object cannot be instantied.
     */
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
    }

    /**
     *
     * @param out
     * @param cache Packed structure of the inflection patterns.
     *
     * @throws java.io.IOException
     */
    public void write(InflectionPatternWriterCache cache) throws IOException {
        cache.serializeFormPatter(baseFormPattern);
        if (otherFormPatterns != null) {
            for (final Iterator it = otherFormPatterns.iterator(); it.hasNext();) {
                FormPattern fp = (FormPattern) it.next();
                cache.serializeFormPatter(fp);
            }
        }
    }

    /**
     *
     * @param out
     * @param cache Packed structure of the inflection patterns.
     *
     * @throws java.io.IOException
     */
    public void write(final DataOutputStream out) throws IOException {
        DataOutputStreamPacker.writeInt(id, out);
        DataOutputStreamPacker.writeInt(numberOfCoveredLexemes, out);
        DataOutputStreamPacker.writeInt(otherFormPatterns == null ? -1 : otherFormPatterns.size(),
                                        out);
        DataOutputStreamPacker.writeInt(coveredCores == null ? -1 : coveredCores.size(), out);
        if (coveredCores != null) {
            for (final Iterator it = coveredCores.iterator(); it.hasNext();) {
                String core = (String) it.next();
                DataOutputStreamPacker.writeString(core, out);
            }
        }
        DataOutputStreamPacker.writeInt(corePatterns == null ? -1 : corePatterns.size(), out);
        if (corePatterns != null) {
            for (final Iterator it = corePatterns.iterator(); it.hasNext();) {
                CorePattern cp = (CorePattern) it.next();
                cp.write(out);
            }
        }
    }

    public static InflectionPatternImpl readInstance(final DataInputStream in,
                                                     InflectionPatternWriterCache cache) throws
            IOException {
        InflectionPatternImpl result = new InflectionPatternImpl();
        result.read(in, cache);
        return result;
    }

    public void read(final DataInputStream in, InflectionPatternWriterCache cache) throws
            IOException {
        id = DataOutputStreamPacker.readInt(in);
        numberOfCoveredLexemes = DataOutputStreamPacker.readInt(in);

        baseFormPattern = cache.deserializeFormPattern();

        int count = DataOutputStreamPacker.readInt(in);
        if (count < 0) {
            otherFormPatterns = null;
        } else {
            otherFormPatterns = new ArrayList(count);
            for (int i = 0; i < count; i++) {
                FormPattern fp = cache.deserializeFormPattern();
                otherFormPatterns.add(fp);
            }
        }

        count = DataOutputStreamPacker.readInt(in);
        if (count < 0) {
            coveredCores = null;
        } else {
            coveredCores = new ArrayList(count);
            for (int i = 0; i < count; i++) {
                String core = DataOutputStreamPacker.readString(in);
                coveredCores.add(core);
            }
        }

        count = DataOutputStreamPacker.readInt(in);
        if (count < 0) {
            corePatterns = null;
        } else {
            corePatterns = new ArrayList(count);
            for (int i = 0; i < count; i++) {
                CorePattern cp = new CorePattern();
                cp.read(in);
                corePatterns.add(cp);
            }
        }

    }

}
