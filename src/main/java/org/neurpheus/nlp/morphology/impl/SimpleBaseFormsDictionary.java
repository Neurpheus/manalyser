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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.neurpheus.nlp.morphology.BaseFormsDictionary;
import org.neurpheus.nlp.morphology.ExtendedInflectionPattern;
import org.neurpheus.nlp.morphology.inflection.InflectionPatternsMap;

/**
 * Represents a collection of base forms of a natural language.
 *
 * This is a simple implementation of the {@link BaseFormsDictionary} interface.
 * In the final application, this class should be replaced by an optimized implementation
 * of the interface, for example {@link CompactBaseFormsDictionary}.
 *
 * @author Jakub Strychowski
 */
public class SimpleBaseFormsDictionary implements BaseFormsDictionary {
    
    /** Holds a mapping between base forms and inflection patterns of these forms. */
    private Map<String, ExtendedInflectionPattern[]> mapping;
    
    /** Creates a new instance of SimpleBaseFormsDictionary. */
    public SimpleBaseFormsDictionary() {
        mapping = new HashMap<>();
    }
    
    /**
     * Adds new base form the the dictionary. 
     *
     * @param baseForm The new base form which should be stored in the dictionary.
     * @param ip The inflectio pattern of the given lexeme.
     */
    public void addBaseForm(final String baseForm, final ExtendedInflectionPattern ip) {
        ExtendedInflectionPattern[] ipa = mapping.get(baseForm);
        if (ipa != null) {
            for (int i = 0; i < ipa.length; i++) {
                if (ipa[0] == ip) {
                    return;
                }
            }
            ExtendedInflectionPattern[] newArray = new ExtendedInflectionPattern[ipa.length + 1];
            System.arraycopy(ipa, 0, newArray, 0, ipa.length);
            newArray[ipa.length] = ip;
            mapping.put(baseForm, newArray);
        } else {
            ipa = new ExtendedInflectionPattern[1];
            ipa[0] = ip;
            mapping.put(baseForm, ipa);
        }
    }
    
    /**
     * Checks if the dictionary contains the given base forms.
     *
     * @param baseForm The base form which may be in the dictionary.
     * @return <code>true</code> if the given form exists in the dictionary.
     */
    public boolean contains(final String baseForm) {
        return mapping.containsKey(baseForm);
    }

    /**
     * Returns all base forms stored in the dictionary.
     *
     * @return A collection of base forms in a particular language.
     */
    public Collection<String> getBaseForms() {
        return mapping.keySet();
    }
    
    /**
     * Returns an array of inflection patters related with the given base form. 
     *
     * @param baseForm The base form of a lexeme
     *                 for which this method should return inflection patterns.
     *
     * @return         An array of inflection patterns related with the given lexeme.
     */
    public ExtendedInflectionPattern[] getInflectionPatterns(final String baseForm) {
        return mapping.get(baseForm); 
    }

    /**
     * Returns an inflection patterns map used by this dictionary.
     *
     * @return The IP mapping.
     */
    public InflectionPatternsMap getInflectionPatternsMap() {
        InflectionPatternsMap result = new InflectionPatternsMap();
        for (ExtendedInflectionPattern[] patterns : mapping.values()) {
            result.add(patterns);
        }
        return result;
    }

    /**
     * Sets a new inflection pattern map which is used by this dictionary.
     *
     * @param   newIPMap    The new IP mapping.
     */
    public void setInflectionPatternsMap(final InflectionPatternsMap newIPMap) {
        // do nothing 
    }

    /**
     * Returns all base forms mapped to corresponding inflection pattern arrays.
     *
     * @return The mapping between base forms and arrays of inflection patterns.
     */
    public Map getBaseFormsMap() {
        throw new UnsupportedOperationException();
    }
    
}
