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
package org.neurpheus.nlp.morphology;

import org.neurpheus.nlp.morphology.*;
import java.util.Arrays;
import java.util.Locale;
import org.neurpheus.core.string.MutableString;

/**
 *  This class checks if a given character is a vowel or consonant in a given language.
 *
 * @author Jakub Strychowski
 */
public class VowelCharactersImpl implements VowelCharacters {
    
 
    /** Holds vowels of Polish language. */
    public static final String POLISH_VOWEL_CHARACTERS = "a\u0105e\u0119io\u00f3uy";
    
    /** Holds vowel of English language. */
    public static final String DEFAULT_VOWEL_CHARACTERS = "aeiouy";

    /**
     * Holds array of flags dentoing if characters are vowel or not.
     * Value of <code>charFlags[ch]</code> denotes if the character ch is a vowel or not.
     */
    private boolean[] charFlags;
    
    /**
     * Holds array of flags dentoing if character codes represent vowels or not.
     * Value of <code>byteFlags[b]</code> denotes if the character code <code>b</code> is a vowel or not.
     */
    private boolean[] byteFlags;
    
    /** Holds a character used as a wildcard for vowels in patterns. */
    private char vowelSign;
    
    /** Holds a character code used as a wildcard for vowels in patterns. */
    private byte vowelSignCode;
    
    /** Holds a character used as a wildcard for consonants in patterns. */
    private char consonantSign;
    
    /** Holds a character code used as a wildcard for consonants in patterns. */
    private byte consonantSignCode;
    
    /**
     * Creates a new instance of VowelCharacters.
     *
     * @param language    A language for which this class 
     *                    will supports the checking of vowel characters.
     */
    public VowelCharactersImpl(final Locale language) {
        if (language.getLanguage().equals("pl")) {
            init(POLISH_VOWEL_CHARACTERS);
        } else {
            init(DEFAULT_VOWEL_CHARACTERS);
        }
    }
    
    /**
     *  Initializes this class with the given vowel characters.
     *
     *  @param  vowelCharacters     the string of vowel characters in a specific language.
     */
    private void init(final String vowelCharacters) {
        char lastChar = 0;
        byte lastByte = 0;
        for (int i = vowelCharacters.length() - 1; i >= 0; i--) {
            char c = vowelCharacters.charAt(i);
            if (c > lastChar) {
                lastChar = c;
            }
            byte b = MutableString.getByte(c);
            if (b > lastByte) {
                lastByte = b;
            }
        }
        charFlags = new boolean[lastChar + 1];
        Arrays.fill(charFlags, false);
        byteFlags = new boolean[lastByte + 1];
        Arrays.fill(byteFlags, false);
        for (int i = vowelCharacters.length() - 1; i >= 0; i--) {
            char c = vowelCharacters.charAt(i);
            charFlags[c] = true;
            byte b = MutableString.getByte(c);
            if (b >= 0) {
                byteFlags[b] = true;
            }
        }
        vowelSign = '$';
        vowelSignCode = MutableString.getByte(vowelSign);
        consonantSign = '#';
        consonantSignCode = MutableString.getByte(consonantSign);
        
    }
    
    /**
     * Checks if the given character is a vowel.
     * 
     * @param   c   The character to check.
     *
     * @return  <code>true</code> if the character <code>c</code> is a vowel.
     */
    public boolean isVowel(final char c) {
        return (c >= charFlags.length) ? false : charFlags[c];
    }
    
    /**
     * Checks if the given character code is a code of vowel.
     * 
     * @param   b   The character code to check.
     *
     * @return  <code>true</code> if the code <code>b</code> is a code of vowel.
     */
    public boolean isVowel(final byte b) {
        return (b >= byteFlags.length) ? false : byteFlags[b];
    }
    
    /** 
     * Gets the code of a character used as a wildcard for vowels in patterns.
     * 
     * @return a code of character denoting any vowel.
     */
    public byte getVowelSignCode() {
        return vowelSignCode;
    }
    
    /** 
     * Gets the character used as a wildcard for vowels in patterns.
     * 
     * @return the character denoting any vowel.
     */
    public char getVowelSign() {
        return vowelSign;
    }
    
    /**
     * Checks if the given character is a consonant.
     * 
     * @param   c   The character to check.
     *
     * @return  <code>true</code> if the character <code>c</code> is a consonant.
     */
    public boolean isConsonant(final char c) {
        return (c >= charFlags.length) ? true : !charFlags[c];
    }
    
    /**
     * Checks if the given character code is a code of consonant.
     * 
     * @param   b   The character code to check.
     *
     * @return  <code>true</code> if the code <code>b</code> is a code of consonant.
     */
    public boolean isConsonant(final byte b) {
        return (b >= byteFlags.length) ? true : !byteFlags[b];
    }
    
    /** 
     * Gets the code of a character used as a wildcard for consonants in patterns.
     * 
     * @return a code of character denoting any consonant.
     */
    public byte getConsonantSignCode() {
        return consonantSignCode;
    }
    
    /** 
     * Gets the character used as a wildcard for consonants in patterns.
     * 
     * @return the character denoting any consonant.
     */
    public char getConsonantSign() {
        return consonantSign;
    }
    
}
