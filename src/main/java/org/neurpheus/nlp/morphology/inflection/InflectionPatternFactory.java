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

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import org.neurpheus.nlp.morphology.ExtendedInflectionPattern;

/**
 * Creates inflection patterns and manages inflection patterns bases (IPBs).
 *
 * @author Jakub Strychowski
 */
public final class InflectionPatternFactory {
    
    /** This class cannot be instancied. */
    private InflectionPatternFactory() {
    }
    
    /** Holds inflection patterns bases for different languages. */
    private static Map ipBases = Collections.synchronizedMap(new HashMap());
    
    /**
     * Returns an inflection patterns base for the given natural language.
     * 
     * @param language The language for which IPB should be returned.
     * @return The IPB which was created for the given language.
     */
    public static InflectionPatternsBase getInflectionPatternsBase(final Locale language) {
        return (InflectionPatternsBase) ipBases.get(language);
    }
    
    /**
     * Registers the given inflection patterns base.
     * Registered IPBs can be obtained later by the {@link getInflectionPatternsBase(Locale)} 
     * method. 
     * 
     * @param ipBase The inflection patterns base which should registered.
     */
    public static void registerInflectionPatternsBase(final InflectionPatternsBase ipBase) {
        ipBases.put(ipBase.getLanguage(), ipBase);
    }
    
    /**
     * Unregisters the given inflection patterns base.
     * After calling this method there want be avaiable any IPB for the language of the given IPB. 
     *
     * @param ipBase The inflection patterns base which should be unregistered.
     */
    public static void unregisterInflectionPatternsBase(final InflectionPatternsBase ipBase) {
        ipBases.remove(ipBase.getLanguage());
    }

    /**
     * Unregisters any inflection patterns base for the given language.
     * 
     * @param language The language for which unregister IPB.
     * @return The IPB which was unregistered or <code>null</code> if there wasn't any 
     *         IPB registered for the language.
     */
    public static InflectionPatternsBase unregisterInflectionPatternsBase(final Locale language) {
        return (InflectionPatternsBase) ipBases.remove(language);
    }
    
    /**
     * Unregisters all inflection patterns bases.
     */
    public static void unregisterAllInflectionPatternsBases() {
        ipBases.clear();
    }
    
    /**
     * Returns a collection of languages of registered inflectin patterns bases. 
     *
     * @return The collection of {@link java.util.Locale} objects.
     */
    public static Collection getLanguagesOfInflectionPatternsBases() {
        Collection res = new HashSet();
        res.addAll(ipBases.keySet());
        return res;
    }
    
    /**
     * Returns a collection of registered inflectin patterns bases. 
     *
     * @return The collection of {@link InflectionPatternsBase} objects.
     */
    public static Collection getAllInflectionPatternsBases() {
        Collection res = new HashSet();
        res.addAll(ipBases.values());
        return res;
    }
    
    /**
     * Creates an inflection pattern analysing given list of word forms.
     *
     * @param   forms       The array of all forms of a lexeme.
     * @param   ipb         The inflection patterns base where created IP should be stored.
     *
     * @return The created inflection pattern.
     */
    public static ExtendedInflectionPattern createInflectionPattern(
            final String[] forms, final InflectionPatternsBase ipb) {
        ExtendedInflectionPattern ip = new InflectionPatternImpl(forms, ipb.isAcceptInfixes(), ipb.isAcceptPrefixes());
        return ipb.addInflectionPattern(ip);
    }
    
    /**
     * Creates an inflection pattern analysing given list of word forms.
     *
     * @param   forms           The array of all forms of a lexeme.
     * @param   acceptInfixes   If true, pattern can contain infixes.
     * @param   acceptInfixes   If true, pattern can contain prefixes.
     *
     * @return The created inflection pattern.
     */
    public static ExtendedInflectionPattern createInflectionPattern(
            final String[] forms, final boolean acceptInfixes, final boolean acceptPrefixes) {
        ExtendedInflectionPattern ip = new InflectionPatternImpl(forms, acceptInfixes, acceptPrefixes);
        return ip;
    }
    
    protected void finalize() throws Throwable {
        ipBases.clear();
    }
}
