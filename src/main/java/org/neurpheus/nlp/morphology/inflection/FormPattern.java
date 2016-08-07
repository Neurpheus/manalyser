/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.neurpheus.nlp.morphology.inflection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.logging.Logger;
import org.neurpheus.nlp.morphology.baseimpl.TagsetImpl;
import org.neurpheus.nlp.morphology.tagset.GrammaticalPropertiesList;
import org.neurpheus.nlp.morphology.tagset.Tagset;

/**
 *
 * @author jstrychowski
 */
public class FormPattern implements Serializable, Comparable {
    
    /** The logger of this class. */
    private static Logger logger = Logger.getLogger(FormPattern.class.getName());
    
    /** Identifier of this class. */
    protected static final long serialVersionUID = -770608080501121253L;
    
    private String affixes;
    
    private GrammaticalPropertiesList grammaticalPropertiesList;

    public FormPattern() {
    }

    public FormPattern(String aff) {
        affixes = aff;
    }

    public FormPattern(String aff, GrammaticalPropertiesList gpList) {
        affixes = aff;
        grammaticalPropertiesList = gpList;
    }

    public FormPattern(FormPattern otherFP) {
        affixes = otherFP.getAffixes();
        grammaticalPropertiesList = otherFP.getGrammaticalPropertiesList();
    }

    
    /** 
     * Returns a string representation of the grammatical properties.
     * 
     * @return The mark of the grammatical properties.
     */
    public String toString() {
        if (grammaticalPropertiesList == null) {
            return affixes;
        } else {
            return affixes + GrammaticalPropertiesList.MARK_SEPARATOR + grammaticalPropertiesList.toString();
        }
    }

    /**
     * Checks if this grammatical properties are equals to the given one.
     * 
     * @param obj The object to compare with.
     * 
     * @return <code>true</code> if grammatical properties are equal.
     */
    public boolean equals(final Object obj) {
        if (obj != null && obj instanceof FormPattern) {
            FormPattern fp = (FormPattern) obj;
            return affixes.equals(fp.getAffixes()) && grammaticalPropertiesList == fp.getGrammaticalPropertiesList();
        } else {
            return false;
        }
    }

    /**
     * Almost unique identifier of this object.
     * 
     * @return The identifier.
     */
    public int hashCode() {
        return toString().hashCode();
    }

    /**
     * Compares this grammatical properties with another one.
     * 
     * @param obj The object which have to be compared.
     * 
     * @return result of a marks comparison.
     */
    public int compareTo(final Object obj) {
        if (obj != null && obj instanceof FormPattern) {
            FormPattern fp = (FormPattern) obj;
            return toString().compareTo(fp.toString());
        } else {
            return 1;
        }
    }

    public String getAffixes() {
        return affixes;
    }

    public void setAffixes(String affixes) {
        this.affixes = affixes == null ? null : affixes.intern();
    }

    public GrammaticalPropertiesList getGrammaticalPropertiesList() {
        return grammaticalPropertiesList;
    }

    public void setGrammaticalPropertiesList(GrammaticalPropertiesList grammaticalPropertiesList) {
        this.grammaticalPropertiesList = grammaticalPropertiesList;
    }
    
    /**
     * Writes this object into the given output stream.
     *
     * @param out   The output stream where this IPB should be stored.
     *
     * @throws IOException if any write error occurred.
     */
    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.writeUTF(affixes);
        out.writeBoolean(grammaticalPropertiesList != null);
        if (grammaticalPropertiesList != null) {
            out.writeShort(grammaticalPropertiesList.getId());
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
        affixes = in.readUTF().intern();
        boolean hasGPL = in.readBoolean();
        grammaticalPropertiesList = null;
        if (hasGPL) {
            int gplId = in.readShort();
            Tagset deserializationTagset = TagsetImpl.getDeserializationTagset();
            if (deserializationTagset != null) {
                grammaticalPropertiesList = deserializationTagset.getGrammaticalPropertiesListById(gplId);
            }
        }
    }

//    /**
//     * Writes this object into the given structure.
//     *
//     * @param cache Packed structure of the inflection patterns.
//     */
//    public void write(final InflectionPatternWriterCache cache) {
//        int len = affixes.length();
//        if (affixes != null && len > 0) {
//            Integer id = (Integer) cache.dictionary.get(affixes);
//            if (id == null) {
//                id = new Integer(cache.dictionary.size() + 1);
//                cache.dictionary.put(affixes, id);
//            }
//            cache.stringsArray.addIntValue(id.intValue());
//        } else {
//            throw new NullPointerException();
//        }
//        int gplId = grammaticalPropertiesList == null ? 0 : grammaticalPropertiesList.getId();
//        cache.gplIdentifiers.addIntValue(gplId);
//    }


//    /**
//     * Reads object's data from the given structure.
//     *
//     * @param cache Packed structure of the inflection patterns.
//     */
//    public void read(final InflectionPatternWriterCache cache) {
//        affixes = cache.getAffixes();
//        grammaticalPropertiesList = cache.getGrammaticalPropertiesList();
//    }



}
