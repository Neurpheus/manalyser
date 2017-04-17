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
import java.util.Collection;
import java.util.Iterator;
import org.neurpheus.core.io.DataOutputStreamPacker;
import org.neurpheus.nlp.morphology.VowelCharacters;
import org.neurpheus.nlp.morphology.VowelCharactersImpl;

/**
 * Represents a core pattern which is a common part of some cores covered by this pattern.
 *
 * @author Jakub Strychowski
 */
public class CorePattern implements Serializable {
    
    
    /** Unique serialization identifier of this class. */
    static final long serialVersionUID = 770608060903220741L;
    
    /** Holds the pattern template. */
    private String pattern;
    
    /** Holds the number of cores covered by this pattern. */
    private int coveredCoresCount;
    
    /** Holds the weight of this pattern in a morphological analysis process. */
    private int weight;
    
    /**
     * Creates a new instance of CorePattern.
     */
    public CorePattern() {
    }
    
    /**
     * Returns a template of this pattern.
     *
     * @return A common fragment of cores.
     */
    public String getPattern() {
        return pattern;
    }
    
    /**
     * Sets a new template for this code pattern.
     *
     * @param newPattern The new value of the template of this core pattern.
     */
    public void setPattern(final String newPattern) {
        this.pattern = newPattern;
    }
    
    /**
     * Returns a number of cores covered by this core pattern.
     *
     * @return The number of covered cores.
     */
    public int getCoveredCoresCount() {
        return coveredCoresCount;
    }
    
    /**
     * Sets the number of cores covered by this core pattern.
     *
     * @param newCoveredCoresCount The new number of covered cores.
     */
    public void setCoveredCoresCount(final int newCoveredCoresCount) {
        this.coveredCoresCount = newCoveredCoresCount;
    }
    
    /**
     * Returns the weight value which is used by a morphological analysis process.
     *
     * @return The weight of this core pattern.
     */
    public int getWeight() {
        return weight;
    }
    
    /**
     * Sets the weight value which is used by morphological analysis process.
     *
     * @param newWeight The new value of this core pattern weight.
     */
    public void setWeight(final int newWeight) {
        this.weight = newWeight;
    }
    
    /**
     * Compares twis object with other core pattern.
     *
     * @param o The core pattern with which compare to.
     *
     * @return <code>0</code> if core patterns are equal, <code>-1</code> if this core pattern
     *      is before the given core pattern, and <code>1</code> if this core pattern
     *      is after the given core pattern.
     */
    public int compareTo(final Object o) {
        CorePattern p = (CorePattern) o;
        return getPattern().compareTo(p.getPattern());
    }
    
    /**
     * Returns a hash code value for the object. This method is
     * supported for the benefit of hashtables such as those provided by
     * <code>java.util.HashMap</code>.
     *
     * @return  a hash code value for this object.
     */
    public int hashCode() {
        super.hashCode();
        return getPattern().hashCode();
    }
    
    /**
     * Indicates whether some other object is "equal to" this core pattern.
     *
     * @param   obj   The reference object with which to compare.
     * @return  <code>true</code> if this object is the same as the obj
     *          argument; <code>false</code> otherwise.
     */
    public boolean equals(final Object obj) {
        return compareTo(obj) == 0;
    }
    
    /**
     * Prints this core pattern.
     *
     * @param out The output stream where this object should be printed.
     */
    public void print(final PrintStream out) {
        out.print(pattern);
        out.print('(');
        out.print(weight);
        out.print(')');
    }
    
    /**
     * Checks if a given core matches this core pattern.
     *
     * @param core The core to check.
     * @param vowels The object representing vowels of a language of the core.
     *
     * @return <code>true</code> if this core pattern covers the given core.
     */
    public boolean matches(final String core, final VowelCharacters vowels) {
        if (pattern == null) {
            return core == null;
        }
        if (pattern.length() > core.length()) {
            return false;
        }
        char[] ca = core.toCharArray();
        char[] pa = pattern.toCharArray();
        int cix = ca.length - 1;
        for (int pix = pa.length - 1; pix >= 0; pix--, cix--) {
            if (ca[cix] != pa[pix]) {
                char c = pa[pix];
                if (c == vowels.getVowelSign() && Character.isLetter(ca[cix])) {
                    if (!vowels.isVowel(ca[cix])) {
                        return false;
                    }
                } else if (c == vowels.getConsonantSign() && Character.isLetter(ca[cix])) {
                    if (vowels.isVowel(ca[cix])) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        }
        return true;
    }
    
    
    /**
     * Removes from the given collection all cores covered by this core pattern.
     *
     * @param cores The collection of cores which should be filtered.
     * @param vowels The object representing vowels of a language of cores.
     */
    public void removeCoveredCores(final Collection cores, final VowelCharacters vowels) {
        boolean containsVowel = false;
        boolean containsConsonant = false;
        int minCoreLength = Integer.MAX_VALUE;
        for (Iterator it = cores.iterator(); it.hasNext();) {
            String core = (String) it.next();
            if (core.equals("%inn%") && pattern.equals("inn%")) {
                core = core + "";
            }
            if (matches(core, vowels)) {
                int clen = core.length();
                if (core.startsWith("%")) {
                    clen--;
                }
                if (clen < minCoreLength) {
                    minCoreLength = clen;
                }
                it.remove();
                // check if core contains a vowel or a consonant at the position of
                // a character where core pattern starts
                if (pattern.length() < core.length()) {
                    char c = core.charAt(core.length() - pattern.length() - 1);
                    if (vowels.isVowel(c)) {
                        containsVowel = true;
                    } else {
                        containsConsonant = true;
                    }
                }
                
            }
        }
        if (pattern.length() < minCoreLength) {
            if (containsVowel && !containsConsonant) {
                pattern = vowels.getVowelSign() + pattern;
            } else if (containsConsonant && !containsVowel) {
                pattern = vowels.getConsonantSign() + pattern;
            }
        }
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
        in.defaultReadObject();
        pattern = pattern.intern();
    }

    public void write(final DataOutputStream out) throws IOException {
        DataOutputStreamPacker.writeString(pattern, out);
        DataOutputStreamPacker.writeInt(coveredCoresCount, out);
        DataOutputStreamPacker.writeInt(weight, out);
    }
    
    public void read(final DataInputStream in) throws IOException {
        pattern = DataOutputStreamPacker.readString(in);
        coveredCoresCount = DataOutputStreamPacker.readInt(in);
        weight = DataOutputStreamPacker.readInt(in);
    }
}
