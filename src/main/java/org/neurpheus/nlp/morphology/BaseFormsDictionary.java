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

import java.util.Collection;
import java.util.Map;
import org.neurpheus.nlp.morphology.inflection.InflectionPatternsMap;

/**
 * Represents a collection of base forms of a natural language.
 *
 * @author Jakub Strychowski
 */
public interface BaseFormsDictionary {
    /**
     * Adds new base form the the dictionary. 
     * 
     * 
     * @param baseForm The new base form which should be stored in the dictionary.
     * @param ip The inflectio pattern of the given lexeme.
     */
    void addBaseForm(final String baseForm, final ExtendedInflectionPattern ip);

    /**
     * Checks if the dictionary contains the given base forms.
     * 
     * 
     * @param baseForm The base form which may be in the dictionary.
     * @return <code>true</code> if the given form exists in the dictionary.
     */
    boolean contains(final String baseForm);

    /**
     * Returns all base forms stored in the dictionary.
     * 
     * 
     * @return A collection of base forms in a particular language.
     */
    Collection<String> getBaseForms();

    /**
     * Returns an array of inflection patters related with the given base form. 
     * 
     * 
     * @param baseForm The base form of a lexeme
     *                 for which this method should return inflection patterns.
     * @return An array of inflection patterns related with the given lexeme.
     */
    ExtendedInflectionPattern[] getInflectionPatterns(final String baseForm);

    /**
     * Returns an inflection patterns map used by this dictionary.
     *
     * @return The IP mapping.
     */
    InflectionPatternsMap getInflectionPatternsMap();

    /**
     * Sets a new inflection pattern map which is used by this dictionary.
     *
     * @param   newIPMap    The new IP mapping.
     */
    void setInflectionPatternsMap(final InflectionPatternsMap newIPMap);

    /**
     * Returns all base forms mapped to corresponding inflection pattern arrays.
     *
     * @return The mapping between base forms and arrays of inflection patterns.
     */
    Map getBaseFormsMap();
    
}
