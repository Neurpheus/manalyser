/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.neurpheus.nlp.morphology;

/**
 *
 * @author jstrychowski
 */
public interface VowelCharacters {

    /**
     *
     * Gets the character used as a wildcard for consonants in patterns.
     *
     * @return the character denoting any consonant.
     */
    char getConsonantSign();

    /**
     *
     * Gets the code of a character used as a wildcard for consonants in patterns.
     *
     * @return a code of character denoting any consonant.
     */
    byte getConsonantSignCode();

    /**
     *
     * Gets the character used as a wildcard for vowels in patterns.
     *
     * @return the character denoting any vowel.
     */
    char getVowelSign();

    /**
     *
     * Gets the code of a character used as a wildcard for vowels in patterns.
     *
     * @return a code of character denoting any vowel.
     */
    byte getVowelSignCode();

    /**
     * Checks if the given character is a consonant.
     *
     * @param   c   The character to check.
     *
     * @return  <code>true</code> if the character <code>c</code> is a consonant.
     */
    boolean isConsonant(final char c);

    /**
     * Checks if the given character code is a code of consonant.
     *
     * @param   b   The character code to check.
     *
     * @return  <code>true</code> if the code <code>b</code> is a code of consonant.
     */
    boolean isConsonant(final byte b);

    /**
     * Checks if the given character is a vowel.
     *
     * @param   c   The character to check.
     *
     * @return  <code>true</code> if the character <code>c</code> is a vowel.
     */
    boolean isVowel(final char c);

    /**
     * Checks if the given character code is a code of vowel.
     *
     * @param   b   The character code to check.
     *
     * @return  <code>true</code> if the code <code>b</code> is a code of vowel.
     */
    boolean isVowel(final byte b);

}
