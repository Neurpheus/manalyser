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

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.neurpheus.core.io.DataOutputStreamPacker;
import org.neurpheus.core.string.LocaleHelper;
import org.neurpheus.nlp.morphology.BaseFormsDictionary;
import org.neurpheus.nlp.morphology.ExtendedInflectionPattern;
import org.neurpheus.nlp.morphology.VowelCharactersImpl;
import org.neurpheus.nlp.morphology.baseimpl.TagsetImpl;
import org.neurpheus.nlp.morphology.tagset.Tagset;

/**
 * Represents a managable set of inflection patterns - Inflection Patterns Base (IPB).
 *
 * @author Jakub Strychowski
 */
public class InflectionPatternsBase implements Serializable {
    
    /** Holds the logger of this class. */
    private static Logger logger = Logger.getLogger(InflectionPatternsBase.class.getName());
    
    /** Unique serialization identifier of this class. */
    static final long serialVersionUID = 770608060903220146L;
    
    /** Holds the natural language for which the IPB was created. */
    private Locale language;
    
    /** If <code>true</code> inflection patterns can contain prefixes. */
    private boolean acceptPrefixes;
    
    /** If <code>true</code> inflection patterns can contain infixes. */
    private boolean acceptInfixes;
    
    /** Holds mapping between arrays of inflection patterns and integer values. */
    private InflectionPatternsMap inflectionPatternsMap;
    
    /** Holds a mapping between inflection pattern codes and inflection patterns. */
    private Map patterns;
    
    /** Holds an array of inflection pattern in this base. */
    private ArrayList patternsArray;
    
    /** Holds a tagset used for taggin inflection patterns in this base. */
    private Tagset tagset;

    /**
     * Creates a new instance of InflectionPatternsBase.
     */
    public InflectionPatternsBase() {
    }
    
    
    /**
     * Creates a new instance of InflectionPatternsBase.
     *
     * @param capacity The estimated number of inflection patterns stored in this IPB.
     */
    public InflectionPatternsBase(final int capacity) {
        patterns = null;
        acceptInfixes = false;
        acceptPrefixes = true;
        patternsArray = new ArrayList(capacity);
        inflectionPatternsMap = new InflectionPatternsMap();
    }
    
    /**
     * Creates a new instance of InflectionPatternsBase.
     *
     * @param   acceptInf   If <code>true</code>, inflection patterns stored in this base
     *                      may contain infixes.
     * @param   acceptPref  If <code>true</code>, inflection patterns stored in this base
     *                      may contain prefixes.
     * @param capacity The estimated number of inflection patterns stored in this IPB.
     */
    public InflectionPatternsBase(
            final boolean acceptInf, final boolean acceptPref, final int capacity) {
        patterns = null;
        acceptInfixes = acceptInf;
        acceptPrefixes = acceptPref;
        patternsArray = new ArrayList(capacity);
        inflectionPatternsMap = new InflectionPatternsMap();
    }
    
    /**
     * Adds the given inflection pattern to this base.
     * If a similar inflection pattern already exists in the base, the given IP is merged with
     * the existing one.
     *
     * @param   ip  The inflection pattern which should be added or merged with similar IP.
     *
     * @return The proper implementation of the IP in this IPB - this can be added IP or a result
     *  of a inflection pattern merging process.
     */
    public ExtendedInflectionPattern addInflectionPattern(final ExtendedInflectionPattern ip) {
        if (patterns == null) {
            patterns = new HashMap();
        }
        String code = ip.getCode();
        ExtendedInflectionPattern current = (ExtendedInflectionPattern) patterns.get(code);
        if (current != null) {
            // similar pattern already exist in the base - merged it with the given pattern.
            current.addCoveredCores(ip.getCoveredCores());
            return current;
        } else {
            // adds new IP into the base
            patterns.put(code, ip);
            ip.setId(patternsArray.size());
            patternsArray.add(ip);
            return ip;
        }
    }
    
    /**
     * Creates a new inflection pattern from the given word forms and puts it into this IPB.
     * If a similar inflection pattern already exists in the base, the created IP is merged with
     * the existing one.
     *
     * @param forms     The word forms from which inflection pattern should be created.
     *
     * @return The proper implementation of the IP in this IPB - this can be added IP or a result
     *  of a inflection pattern merging process.
     */
    public ExtendedInflectionPattern addInflectionPattern(final String[] forms) {
        return InflectionPatternFactory.createInflectionPattern(forms, this);
    }
    
    /**
     * Creates a new inflection pattern from the word forms written in the line of text
     * and puts it into this IPB.
     * If a similar inflection pattern already exists in the base, the created IP is merged with
     * the existing one.
     *
     * @param line  The line of text which contains all forms of a word. he forms should be
     *              separated by the space character. The line should begin from the base form
     *              of a word.
     *
     * @return The proper implementation of the IP in this IPB - this can be added IP or a result
     *  of a inflection pattern merging process.
     */
    public ExtendedInflectionPattern addInflectionPattern(final String line) {
        // Parse the line parameter and extract an array of forms.
        String[] tab = line.split("\\s");
        int pos = 0;
        for (int i = 0; i < tab.length; i++) {
            if (tab[i].length() > 0) {
                tab[pos++] = tab[i];
            }
        }
        String[] tmp = new String[pos];
        System.arraycopy(tab, 0, tmp, 0, pos);
        tab = tmp;
        return addInflectionPattern(tab);
    }
    
    /**
     * Returns an existing inflection pattern which vocers the word forms written in the line of text.
     *
     * @param line  The line of text which contains all forms of a word. The forms should be
     *              separated by the space character. The line should begin from the base form
     *              of a word.
     *
     * @return The infleciton pattern which covers the given lexeme forms or null if the IP cannot be found.
     */
    public ExtendedInflectionPattern getInflectionPattern(final String line) {
        // Parse the line parameter and extract an array of forms.
        String[] tab = line.split("\\s");
        int pos = 0;
        for (int i = 0; i < tab.length; i++) {
            if (tab[i].length() > 0) {
                tab[pos++] = tab[i];
            }
        }
        String[] tmp = new String[pos];
        System.arraycopy(tab, 0, tmp, 0, pos);
        tab = tmp;
        return getInflectionPattern(tab);
    }
    
    /**
     * Retursn an inflection pattern which covers the given word forms
     *
     * @param forms     The word forms from which inflection pattern should be returned.
     *
     * @return The proper implementation of the IP or null if the IP does not exist.
     */
    public ExtendedInflectionPattern getInflectionPattern(final String[] forms) {
        if (patterns == null) {
            patterns = new HashMap();
            for (Iterator it = getInflectionPatterns().iterator(); it.hasNext();) {
                ExtendedInflectionPattern ip = (ExtendedInflectionPattern) it.next();
                patterns.put(ip.getCode(), ip);
            }
        }
        ExtendedInflectionPattern ip = InflectionPatternFactory.createInflectionPattern(
                forms, isAcceptInfixes(), isAcceptPrefixes());
        String code = ip.getCode();
        return (ExtendedInflectionPattern) patterns.get(code);
    }

    /**
     * Ensures that each form pattern will be used in single inflection pattern only.
     */
    public void recreateFormPatterns() {
        for (Iterator it = getInflectionPatterns().iterator(); it.hasNext();) {
            ExtendedInflectionPattern ip = (ExtendedInflectionPattern) it.next();
            FormPattern fp = ip.getBaseFormPattern();
            ip.setBaseFormPattern(new FormPattern(fp));
            ArrayList tmp = new ArrayList(ip.getOtherFormPatterns().size());
            for (final Iterator it2 = ip.getOtherFormPatterns().iterator(); it2.hasNext();) {
                fp = (FormPattern) it2.next();
                tmp.add(new FormPattern(fp));
            }
            ip.setOtherFormPatterns(tmp);
        }
    }

    public void reduceNumberOfFormPatterns() {
        logger.info("Searching for duplicate form patterns.");
        HashMap mapping = new HashMap();
        int counter = 0;
        for (Iterator it = getInflectionPatterns().iterator(); it.hasNext();) {
            ExtendedInflectionPattern ip = (ExtendedInflectionPattern) it.next();
            FormPattern fp = ip.getBaseFormPattern();
            counter++;
            String code = fp.toString();
            FormPattern newfp = (FormPattern) mapping.get(code);
            if (newfp == null) {
                mapping.put(code, fp);
                newfp = fp;
            } else {
                ip.setBaseFormPattern(newfp);
            }
            ArrayList tmp = new ArrayList(ip.getOtherFormPatterns().size());
            for (final Iterator it2 = ip.getOtherFormPatterns().iterator(); it2.hasNext();) {
                counter++;
                fp = (FormPattern) it2.next();
                code = fp.toString();
                newfp = (FormPattern) mapping.get(code);
                if (newfp == null) {
                    mapping.put(code, fp);
                    newfp = fp;
                }
                tmp.add(newfp);
            }
            ip.setOtherFormPatterns(tmp);
        }
        logger.info("Number of form patterns in the inflection patterns base: " + counter);
        logger.info("Number of unique form patterns in the inflection patterns base: " + mapping.size());
        mapping.clear();
    }
    
    /**
     * Returns the number of inflection patters already stored in this IPB.
     *
     * @return The number of Ip in the base.
     */
    public int size() {
        return patternsArray.size();
    }
    
    /**
     * Returns a list of inflection patterns stored in this IPB.
     * The result list is ordered according to number of lexemes covered by each inflection pattern.
     *
     * @return The collection of {@link InflectionPattern} objects.
     */
    public List getInflectionPatterns() {
        return patternsArray;
    }

    /**
     * Returns an inflection patter having the given identifier.
     *
     * @param   The identfier of an inflection pattern.
     *
     * @return The inflection pattern identifed by the given value.
     */
    public ExtendedInflectionPattern getInflectionPattern(final int id) {
        return (ExtendedInflectionPattern) patternsArray.get(id);
    }
    
    /**
     * Returns <code>true</code>, if inflection patterns stored in this IPB may contain prefixes.
     *
     * @return The "accept prefixes" flag state.
     */
    public boolean isAcceptPrefixes() {
        return acceptPrefixes;
    }
    
    /**
     * Sets the new value for the flag which denotes if inflection patterns stored in this
     * IPB may contain prefixes.
     *
     * @param newAcceptPrefixes The new value of the "acceppt prefixes" flag.
     */
    public void setAcceptPrefixes(final boolean newAcceptPrefixes) {
        this.acceptPrefixes = newAcceptPrefixes;
    }
    
    /**
     * Returns <code>true</code>, if inflection patterns stored in this IPB may contain infixes.
     *
     * @return The "accept infixes" flag state.
     */
    public boolean isAcceptInfixes() {
        return acceptInfixes;
    }
    
    /**
     * Sets the new value for the flag which denotes if inflection patterns stored in this
     * IPB may contain infixes.
     *
     * @param newAcceptInfixes The new value of the "acceppt infixes" flag.
     */
    public void setAcceptInfixes(final boolean newAcceptInfixes) {
        this.acceptInfixes = newAcceptInfixes;
    }
    
    /**
     * Creates a new inflection patterns base analysing a full dictionary of a language.
     *
     * @param in                The input stream providing data from full dictionary.
     * @param charsetName       The name of a charset which is used for storing characters in the
     *                          dictary stream.
     * @param acceptInfixes     If <code>true</code> infixes in inflection patterns are allowed.
     * @param acceptPrefixes    If <code>true</code> prefixes in inflection patterns are allowed.
     * @param dict              Thedictionary where base forms (lemmas) should stored.
     * @param language          The natural language of a full dictionary.
     *
     * @throws java.io.IOException if any error occurred while dictionary reading.
     *
     * @return Create inflection patterns base.
     */
    public static InflectionPatternsBase createFromFullDictionary(
            final InputStream in, final String charsetName, final boolean acceptInfixes,
            final boolean acceptPrefixes, final BaseFormsDictionary dict,
            Locale language) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, charsetName));
        String line;
        InflectionPatternsBase ipb = new InflectionPatternsBase(acceptInfixes, acceptPrefixes, 30);
        ipb.setLanguage(language);
        int number = 0;
        final int numberOfLinesBetweenInfoMessages = 1000;
        do {
            line = reader.readLine();
            if (line != null && line.trim().length() > 0) {
                line = line.trim();
                if (logger.isLoggable(Level.FINE)) {
                    logger.fine("Processing line : " + line);
                }
                ExtendedInflectionPattern ip = ipb.addInflectionPattern(line);
                int pos = line.indexOf(' ');
                dict.addBaseForm(pos < 0 ? line : line.substring(0, pos), ip);
                number++;
                if (logger.isLoggable(Level.FINE) && (number % numberOfLinesBetweenInfoMessages == 0)) {
                    logger.fine("Number of processed words : " + number);
                }
            }
        } while (line != null);
        // order by number of covered lexemes
        Collections.sort(ipb.patternsArray);
        int id = 0;
        for (final Iterator it = ipb.patternsArray.iterator(); it.hasNext(); ) {
            ExtendedInflectionPattern ip = (ExtendedInflectionPattern) it.next();
            ip.setId(id++);
        }
        ipb.compact(false);
        return ipb;
    }
    
//    /**
//     * Returns all inflection patterns stored in this IPB in sorted order.
//     *
//     * @return A list of {@link InflectionPattern} objects.
//     */
//    public List getSortedInflectionPatterns() {
//        ArrayList res = new ArrayList();
//        res.addAll(getInflectionPatterns());
//        Collections.sort(res);
//        return res;
//    }
    
    /**
     * Prints all inflection patterns stored in this IPB.
     *
     * @param out       The output stream where data should be printed.
     * @param withCores If <code>true</code>, this method prints also cores covered by each
     *                  inflection pattern.
     */
    public void print(final PrintStream out, final boolean withCores) {
        List list = getInflectionPatterns();
        int totalCores = 0;
        for (Iterator it = list.iterator(); it.hasNext();) {
            totalCores += ((ExtendedInflectionPattern) it.next()).getNumberOfCoveredLexemes();
        }
        int number = 1;
        int sumCores = 0;
        for (Iterator it = list.iterator(); it.hasNext(); number++) {
            ExtendedInflectionPattern ip = (ExtendedInflectionPattern) it.next();
            ip.print(out, withCores, number, sumCores, totalCores);
            sumCores += ip.getNumberOfCoveredLexemes();
            out.println();
        }
    }
    
    /**
     * Prints all inflection patterns stored in this IPB to the specified file.
     *
     * @param path      The path of the file where data should be printed.
     * @param withCores If <code>true</code>, this method prints also cores covered by each
     *                  inflection pattern.
     * @throws java.io.IOException if any error occurred while writin data to the file.
     */
    public void print(final String path, final boolean withCores) throws IOException {
        PrintStream log = null;
        try {
            log = new PrintStream(
                    new BufferedOutputStream(new FileOutputStream(path)), 
                    false, "UTF-8");
            print(log, true);
        } finally {
            if (log != null) {
                log.close();
            }
        }
    }
    
    /**
     * Prints the statistics about morhemes usage to file.
     *
     * @param path the path to the file where statistic should be printed.
     *
     * @throws java.io.IOException if any error occurred while statistics printing.
     */
    public void printMorphemesStatistic(final String path) throws IOException {
        PrintStream out = null;
        try {
            out = new PrintStream(
                    new BufferedOutputStream(new FileOutputStream(path)), 
                    false, "UTF-8");
            printMorphemesStatistic(out);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
    
    /**
     * Prints the statistics about morhemes usage to output stream.
     *
     * @param out The output stream where statistics should be printed.
     *
     * @throws java.io.IOException if any error occurred while statistics printing.
     */
    public void printMorphemesStatistic(final PrintStream out) throws IOException {
        HashMap statistic = new HashMap();
        String arrow = new String("->");
        for (Iterator it = getInflectionPatterns().iterator(); it.hasNext();) {
            ExtendedInflectionPattern ip = (ExtendedInflectionPattern) it.next();
            for (Iterator sit = ip.getAllFormPatterns().iterator(); sit.hasNext();) {
                String tranform = ((FormPattern) sit.next()).getAffixes() + arrow + ip.getBaseFormPattern().getAffixes();
                Integer i = (Integer) statistic.get(tranform);
                if (i == null) {
                    i = new Integer(1);
                } else {
                    i = new Integer(i.intValue() + 1);
                }
                statistic.put(tranform, i);
            }
        }
        ArrayList list = new ArrayList();
        list.addAll(statistic.entrySet());
        Collections.sort(list,
                new Comparator() {
            public int compare(final Object a, final Object b) {
                String sa = (String) ((Map.Entry) a).getKey();
                Integer ca = (Integer) ((Map.Entry) a).getValue();
                String sb = (String) ((Map.Entry) b).getKey();
                Integer cb = (Integer) ((Map.Entry) b).getValue();
                if (sa.equals(sb)) {
                    return -1 * ca.compareTo(cb);
                } else {
                    return sa.compareTo(sb);
                }
            }
        }
        );
        int lp = 1;
        for (Iterator it = list.iterator(); it.hasNext(); lp++) {
            Map.Entry entry = (Map.Entry) it.next();
            String supplement = (String) entry.getKey();
            Integer count = (Integer) entry.getValue();
            out.print(lp);
            out.print(". ");
            out.print(supplement);
            out.print(" : ");
            out.println(count.intValue());
        }
    }
    
    /**
     * Dermines core patterns for all inflection patterns stored in this IPB.
     *
     * @param vowels Represents the vowel characters of a analysed language.
     */
    public void determineCorePatterns(final VowelCharactersImpl vowels) {
        final int numberOfIPsBetweenInfoMessage = 100;
        int number = 0;
        for (Iterator it = getInflectionPatterns().iterator(); it.hasNext(); number++) {
            ExtendedInflectionPattern ip = (ExtendedInflectionPattern) it.next();
            ip.determineCorePatterns(vowels);
            if (logger.isLoggable(Level.FINE) && (number % numberOfIPsBetweenInfoMessage == 0)) {
                logger.fine("Number of processed inflection patterns : " + number);
            }
        }
    }
    
    /**
     * Returns a mapping between integer identifiers and arrays of inflection patterns.
     *
     * @return The object representing mapping.
     */
    public InflectionPatternsMap getInflectionPatternsMap() {
        return inflectionPatternsMap;
    }
    
    /**
     * Sets a mapping between integer identifiers and arrays of inflection patterns.
     *
     * @param newInflectionPatternsMap The objet representing mapping.
     */
    public void setInflectionPatternsMap(final InflectionPatternsMap newInflectionPatternsMap) {
        this.inflectionPatternsMap = newInflectionPatternsMap;
    }
    
    /**
     * Returns the language of this IPB.
     *
     * @return An object which identifies the natural language.
     */
    public Locale getLanguage() {
        return language;
    }
    
    /**
     * Sets the language of this IPB.
     *
     * @param newLanguage An object which identifies a new language for this IPB.
     */
    public void setLanguage(final Locale newLanguage) {
        this.language = newLanguage;
        InflectionPatternsBase tmpIPB = 
                InflectionPatternFactory.getInflectionPatternsBase(language);
        if (tmpIPB != this && tmpIPB != null) {
            InflectionPatternFactory.registerInflectionPatternsBase(this);
        }
        if (inflectionPatternsMap != null) {
            inflectionPatternsMap.setLanguage(newLanguage);
        }
    }

    public int getMaxLengthOfCorePattern() {
        int max = 0;
        for (final Iterator it = getInflectionPatterns().iterator(); it.hasNext();) {
            ExtendedInflectionPattern ip = (ExtendedInflectionPattern) it.next();
            int len = ip.getMaxLengthOfCorePattern();
            if (len > max) {
                max = len;
            }
        } 
        return max;
    }
    
    public int getMaxLengthOfSupplement() {
        int max = 0;
        for (final Iterator it = getInflectionPatterns().iterator(); it.hasNext();) {
            ExtendedInflectionPattern ip = (ExtendedInflectionPattern) it.next();
            int len = ip.getMaxLengthOfSupplement();
            if (len > max) {
                max = len;
            }
        } 
        return max;
    }
    
    /**
     * Otimizes a memory consumtion for this IPB.
     * Clears temporary objects used by an IPB creation process.
     */
    public void compact(boolean clearCores) {
        if (tagset != null) {
            tagset.compact();
        }
        if (clearCores) {
            for (final Iterator it = getInflectionPatterns().iterator(); it.hasNext();) {
                ExtendedInflectionPattern ip = (ExtendedInflectionPattern) it.next();
                ip.setCorePatterns(null);
                ip.setCoveredCores(null);
            }
        }
        if (patterns != null) {
            patterns.clear();
            patterns = null;
        }
        if (inflectionPatternsMap != null) {
            inflectionPatternsMap.compact();
        }
        patternsArray.trimToSize();
    }
    
    /**
     * Writes this object into the given output stream.
     *
     * @param out   The output stream where this IPB should be stored.
     *
     * @throws IOException if any write error occurred.
     */
    private void writeObject(final ObjectOutputStream out) throws IOException {
        DataOutputStream dout = new DataOutputStream(out);
        write(dout);
//        out.writeObject(language);
//        out.writeBoolean(acceptPrefixes);
//        out.writeBoolean(acceptInfixes);
//        out.writeBoolean(tagset != null);
//        if (tagset != null) {
//            out.writeObject(tagset);
//        }
//        out.writeObject(patternsArray);
//        out.writeBoolean(inflectionPatternsMap != null);
//        if (inflectionPatternsMap != null) {
//            InflectionPatternsBase tmpIPB =
//                    InflectionPatternFactory.getInflectionPatternsBase(language);
//            if (tmpIPB != this) {
//                InflectionPatternFactory.registerInflectionPatternsBase(this);
//            }
//            out.writeObject(inflectionPatternsMap);
//            if (tmpIPB != null && tmpIPB != this) {
//                InflectionPatternFactory.registerInflectionPatternsBase(tmpIPB);
//            }
//        }
    }
    
    /**
     * Reads this object data from the given input stream.
     *
     * @param in   The input stream where this IPB is stored.
     *
     * @throws IOException if any read error occurred.
     * @throws ClassNotFoundException if this object cannot be instantied.
     */
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        DataInputStream din = new DataInputStream(in);
        read(din);
//        language = (Locale) in.readObject();
//        acceptPrefixes = in.readBoolean();
//        acceptInfixes = in.readBoolean();
//        boolean hasTagset = in.readBoolean();
//        if (hasTagset) {
//            tagset = (Tagset) in.readObject();
//        } else {
//            tagset = null;
//        }
//        synchronized (TagsetImpl.getDeserializationLockObject()) {
//            TagsetImpl.setDeserializationTagset(tagset);
//            patternsArray = (ArrayList) in.readObject();
//            TagsetImpl.setDeserializationTagset(null);
//        }
//
//
//        boolean hasIPMap = in.readBoolean();
//        InflectionPatternsBase tmpIPB =
//                InflectionPatternFactory.getInflectionPatternsBase(language);
//        if (tmpIPB != this) {
//            InflectionPatternFactory.registerInflectionPatternsBase(this);
//        }
//        inflectionPatternsMap = hasIPMap ? (InflectionPatternsMap) in.readObject() : null;
//        if (tmpIPB != null && tmpIPB != this) {
//            InflectionPatternFactory.registerInflectionPatternsBase(tmpIPB);
//        }
    }

    public Tagset getTagset() {
        return tagset;
    }

    public void setTagset(Tagset tagset) {
        this.tagset = tagset;
    }

    /**
     * Writes this object into the given data output stream.
     *
     * @param out   The output stream where this IPB should be stored.
     *
     * @throws IOException if any write error occurred.
     */
    public void write(final DataOutputStream out) throws IOException {
        //reduceNumberOfFormPatterns();
        DataOutputStreamPacker.writeString(language.toString(), out);
        out.writeBoolean(acceptPrefixes);
        out.writeBoolean(acceptInfixes);
        out.writeBoolean(tagset != null);
        if (tagset != null) {
            tagset.write(out);
        }

        InflectionPatternWriterCache cache = new InflectionPatternWriterCache();
        out.writeInt(patternsArray.size());
        HashMap dictionary = new HashMap();
        for (final Iterator it = patternsArray.iterator(); it.hasNext();) {
            InflectionPatternImpl ip = (InflectionPatternImpl) it.next();
            ip.write(cache);
        }
        cache.write(out);
        for (final Iterator it = patternsArray.iterator(); it.hasNext();) {
            InflectionPatternImpl ip = (InflectionPatternImpl) it.next();
            ip.write(out);
        }

        out.writeBoolean(inflectionPatternsMap != null);
        if (inflectionPatternsMap != null) {
            InflectionPatternsBase tmpIPB =
                    InflectionPatternFactory.getInflectionPatternsBase(language);
            if (tmpIPB != this) {
                InflectionPatternFactory.registerInflectionPatternsBase(this);
            }
            inflectionPatternsMap.write(out);
            if (tmpIPB != null && tmpIPB != this) {
                InflectionPatternFactory.registerInflectionPatternsBase(tmpIPB);
            }
        }
    }

    public void read(final DataInputStream in) throws IOException {
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("Reading inflection pattern base...");
        }
        long duration = java.lang.System.nanoTime();
        language = LocaleHelper.toLocale(DataOutputStreamPacker.readString(in));
        acceptPrefixes = in.readBoolean();
        acceptInfixes = in.readBoolean();
        tagset = null;
        if (in.readBoolean()) {
            tagset = new TagsetImpl();
            tagset.read(in);
        }

        int numberOfPatterns = in.readInt();
        InflectionPatternWriterCache cache = new InflectionPatternWriterCache();
        cache.read(in);
        synchronized (TagsetImpl.getDeserializationLockObject()) {
            TagsetImpl.setDeserializationTagset(tagset);
            patternsArray = new ArrayList(numberOfPatterns);
            for (int i = 0; i < numberOfPatterns; i++) {
                InflectionPatternImpl ip = InflectionPatternImpl.readInstance(in, cache);
                patternsArray.add(ip);
            }

            inflectionPatternsMap = null;
            if (in.readBoolean()) {
                InflectionPatternsBase tmpIPB =
                        InflectionPatternFactory.getInflectionPatternsBase(language);
                if (tmpIPB != this) {
                    InflectionPatternFactory.registerInflectionPatternsBase(this);
                }
                inflectionPatternsMap = new InflectionPatternsMap();
                inflectionPatternsMap.read(in);
                if (tmpIPB != null && tmpIPB != this) {
                    InflectionPatternFactory.registerInflectionPatternsBase(tmpIPB);
                }
            }
            TagsetImpl.setDeserializationTagset(null);
        }
        if (logger.isLoggable(Level.FINE)) {
            double time = (java.lang.System.nanoTime() - duration) / 1000000.0;
            logger.fine("Inflection pattern base read in " + time + " ms");
        }

    }

    
}
