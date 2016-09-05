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
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.neurpheus.core.io.DataOutputStreamPacker;
import org.neurpheus.nlp.morphology.ExtendedInflectionPattern;

/**
 * Represents a mapping between integer identifiers and arrays of inflection patterns.
 *
 * @author Jakub Strychowski
 */
public class InflectionPatternsMap implements Serializable {
    
    /** Unique serialization identifier of this class. */
    static final long serialVersionUID = 770608060828184716L;
    
    /** Logging module used by this class. */
    private static Logger logger = Logger.getLogger(InflectionPatternsMap.class.getName());
    
    /** Holds entries of this mapping. */
    private ArrayList items;
    
    /**
     * Defines the language for which inflection patterns where created.
     * This value is need to obtain a proper inflection patterns base.
     */
    private Locale language;
    
    /** Maps arrays of inflection patterns into integers. */
    private transient Map reverseMap;
    
    /** Creates a new instance of InflectionPatternsMap. */
    public InflectionPatternsMap() {
        items = new ArrayList();
        reverseMap = null;
        language = null;
    }
    
    
    /**
     * Adds the given array of inflection patterns to this mapping and returns the identifier of
     * this array.
     * If the array already exists in this mapping, this method rens an identifier of an existing
     * array.
     *
     * @param infPatterns The array of inflection patterns which should be addet to this mapping.
     *
     * @return The identifier of added or existing array of inflection patterns.
     */
    public int add(final ExtendedInflectionPattern[] infPatterns) {
        String code = getInflectionPatternsCode(infPatterns);
        int index = getIndex(code);
        if (index < 0) {
            items.add(infPatterns);
            index = items.size() - 1;
            reverseMap.put(code, new Integer(index));
        }
        return index;
    }
    
    /**
     * Returns an array of inflection patterns identified by the given identifier.
     *
     * @param index The identifier of an array of inflection patterns.
     *
     * @return The result array.
     */
    public ExtendedInflectionPattern[] get(final int index) {
        return (ExtendedInflectionPattern[]) items.get(index);
    }

    /**
     * Returns an unique code of an array of inflection patterns.
     * This method creates a code joining codes of all inflection patterns from the array. 
     *
     * @param infPatterns The array of inflection patterns for which this method creates a code.
     *
     * @return The code of the array.
     */
    private String getInflectionPatternsCode(final ExtendedInflectionPattern[] infPatterns) {
        StringBuffer buffer = new StringBuffer();
        boolean isFirst = true;
        for (int i = 0; i < infPatterns.length; i++) {
            String code = infPatterns[i].getCode();
            if (!isFirst) {
                buffer.append('_');
            }
            buffer.append(code.charAt(0));
            buffer.append(code.hashCode());
            buffer.append(code.charAt(code.length() - 1));
            isFirst = false;
        }
        return buffer.toString();
    }
    
    /**
     * Returns the identifier of an array of inflection patterns identified by the given code. 
     *
     * @param infPatternsCode The code of the array of inflection patterns.
     *
     * @return The indetifier of the array or <code>-1</code> if this object does not map 
     *      the array identified by the given code.
     */
    private int getIndex(final String infPatternsCode) {
        if (reverseMap == null) {
            reverseMap = new HashMap();
        }
        Integer index = (Integer) reverseMap.get(infPatternsCode);
        if (index == null) {
            return -1;
        } else {
            return index.intValue();
        }
    }
    
    /**
     * Otimizes a memory consumtion for this IPB.
     * Clears temporary objects used by a mapping creation process.
     */
    public void compact() {
        if (reverseMap != null) {
            reverseMap.clear();
            reverseMap = null;
        }
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("Number of differen inflection patterns arrays = " + items.size());
        }
    }
    
    /**
     * Returns the language of inflection patterns mapped by this object.
     *
     * @return An object which identifies the natural language.
     */
    public Locale getLanguage() {
        return language;
    }
    
    /**
     * Sets the language of inflection patterns mapped by this object.
     *
     * @param newLanguage An object which identifies a new language for this IPB.
     */
    public void setLanguage(final Locale newLanguage) {
        this.language = newLanguage;
    }
    
    /**
     * Writes this object into the given output stream.
     *
     * @param out   The output stream where this map should be stored.
     *
     * @throws IOException if any write error occurred.
     */
    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.writeObject(language);
        InflectionPatternsBase ipb = InflectionPatternFactory.getInflectionPatternsBase(language);
        if (ipb == null) {
            throw new IOException(
                    "Cannot get inflection patterns base for the language " + language.toString());
        }
        out.writeInt(items.size());
        for (Iterator it = items.iterator(); it.hasNext();) {
            ExtendedInflectionPattern[] ipa = (ExtendedInflectionPattern[]) it.next();
            out.writeInt(ipa.length);
            for (int i = 0; i < ipa.length; i++) {
                ExtendedInflectionPattern ip = ipa[i];
                out.writeInt(ip.getId());
            }
        }
    }
    
    /**
     * Reads this object data from the given input stream.
     *
     * @param in   The input stream where this map is stored.
     *
     * @throws IOException if any read error occurred.
     * @throws ClassNotFoundException if this object cannot be instantied.
     */
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        language = (Locale) in.readObject();
        InflectionPatternsBase ipb = InflectionPatternFactory.getInflectionPatternsBase(language);
        if (ipb == null) {
            throw new IOException(
                    "Cannot get inflection patterns base for the language " + language.toString());
        }
        int itemsCount = in.readInt();
        items = new ArrayList(itemsCount);
        for (int i = 0; i < itemsCount; i++) {
            int tabLength = in.readInt();
            ExtendedInflectionPattern[] tab = new ExtendedInflectionPattern[tabLength];
            items.add(tab);
            for (int j = 0; j < tabLength; j++) {
                int ipId = in.readInt();
                ExtendedInflectionPattern ip = ipb.getInflectionPattern(ipId);
                tab[j] = ip;
            }
        }
    }
    
    /**
     * Returns the number of mappings stored in this IP Map.
     *
     * @return The number of mappings.
     */
    public int size() {
        return items.size();
    }

    /**
     * Writes this object into the given data output stream.
     *
     * @param out   The output stream where this IPB should be stored.
     *
     * @throws IOException if any write error occurred.
     */
    public void write(final DataOutputStream out) throws IOException {
        DataOutputStreamPacker.writeString(language.toString(), out);
        InflectionPatternsBase ipb = InflectionPatternFactory.getInflectionPatternsBase(language);
        if (ipb == null) {
            throw new IOException(
                    "Cannot get inflection patterns base for the language " + language.toString());
        }
        DataOutputStreamPacker.writeInt(items.size(), out);
        for (Iterator it = items.iterator(); it.hasNext();) {
            ExtendedInflectionPattern[] ipa = (ExtendedInflectionPattern[]) it.next();
            DataOutputStreamPacker.writeInt(ipa.length, out);
            for (int i = 0; i < ipa.length; i++) {
                ExtendedInflectionPattern ip = ipa[i];
                out.writeShort(ip.getId());
            }
        }
    }

    public void read(final DataInputStream in) throws IOException {
        language = new Locale(DataOutputStreamPacker.readString(in));
        InflectionPatternsBase ipb = InflectionPatternFactory.getInflectionPatternsBase(language);
        if (ipb == null) {
            throw new IOException(
                    "Cannot get inflection patterns base for the language " + language.toString());
        }
        int count = DataOutputStreamPacker.readInt(in);
        items = new ArrayList(count);
        for (int index = 0; index < count; index++) {
            int len = DataOutputStreamPacker.readInt(in);
            ExtendedInflectionPattern[] ipa = new ExtendedInflectionPattern[len];
            for (int i = 0; i < len; i++) {
                int ipId = in.readShort();
                ExtendedInflectionPattern ip = ipb.getInflectionPattern(ipId);
                ipa[i] = ip;
            }
            items.add(ipa);
        }
    }
    
}
