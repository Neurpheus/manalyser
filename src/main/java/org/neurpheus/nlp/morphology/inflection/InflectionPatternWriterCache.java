/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.neurpheus.nlp.morphology.inflection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.log4j.Logger;
import org.neurpheus.collections.array.CompactArray;
import org.neurpheus.core.charset.DynamicCharset;
import org.neurpheus.nlp.morphology.baseimpl.TagsetImpl;
import org.neurpheus.nlp.morphology.tagset.GrammaticalPropertiesList;
import org.neurpheus.nlp.morphology.tagset.Tagset;

/**
 *
 * @author jstrychowski
 */
public class InflectionPatternWriterCache {

    private final static Logger logger = Logger.getLogger(InflectionPatternWriterCache.class);

    public Map dictionary;
    public CompactArray fpDefArray;
    public CompactArray formPatternsArray;
    public String[] affixes;
    public Map formPatterns2Int;
    public int formPatternCounter = 0;
    private FormPattern[] int2FormPatterns;

    public InflectionPatternWriterCache() {
        dictionary = new HashMap();
        formPatterns2Int = new HashMap();
        fpDefArray = new CompactArray();
        formPatternsArray = new CompactArray();
    }

    public final static String charsetName = "InflectionPattern";

    // Fields used for reading
    private int readPos = 0;
    private Tagset deserializationTagset;

    public void read(final DataInputStream in) throws IOException {
        long duration = java.lang.System.nanoTime();
        readPos = 0;
        // read charset
        deserializationTagset = TagsetImpl.getDeserializationTagset();
        DynamicCharset dynCharset = new DynamicCharset(charsetName, null);
        dynCharset.read(in);

        // read dictionary
        int dictSize = in.readInt();
        int maxLen = in.readShort();
        affixes = new String[dictSize + 1];
        CompactArray tmpArray = new CompactArray();
        tmpArray.read(in);
        byte[] buffer = new byte[maxLen + 1];
        char[] cBuffer = new char[maxLen + 1];
        int index = 0;
        for (int i = 1; i <= dictSize; i++) {
            int len = tmpArray.getIntValue(index++);
            for (int j = 0; j < len; j++) {
                buffer[j] = (byte) tmpArray.getIntValue(index++);
            }
            dynCharset.getChars(buffer, 0, len, cBuffer, 0);
            affixes[i] = new String(cBuffer, 0, len).intern();
        }
        tmpArray.dispose();


        fpDefArray = new CompactArray();
        fpDefArray.read(in);

        formPatternsArray = new CompactArray();
        formPatternsArray.read(in);

        int numberOfFormPatters = fpDefArray.size() / 2;
        int2FormPatterns = new FormPattern[numberOfFormPatters];
        int j = 0;
        for (int i = 0; i < numberOfFormPatters; i++) {
            int affixId = fpDefArray.getIntValue(j++);
            int gplId = fpDefArray.getIntValue(j++);
            GrammaticalPropertiesList gpl = null;
            if (deserializationTagset != null) {
                gpl = deserializationTagset.getGrammaticalPropertiesListById(gplId);
            } else {
                //throw new IOException("Deserialization tagset has not been set.");
            }
            FormPattern fp = new FormPattern(affixes[affixId], gpl);
            int2FormPatterns[i] = fp;
        }
        fpDefArray.dispose();
        fpDefArray = null;


        if (logger.isDebugEnabled()) {
            double time = (java.lang.System.nanoTime() - duration) / 1000000.0;
            logger.debug("   inflection pattern writer cache read in " + time + " ms");
        }

    }

    public void write(final DataOutputStream out) throws IOException {
        int dictSize = dictionary.size();
        DynamicCharset dynCharset = new DynamicCharset(charsetName, null);
        CompactArray tmpArray = new CompactArray(dictSize * 5, 32);
        String[] sa = new String[dictSize + 1];
        int maxLen = 0;
        for (final Iterator it = dictionary.entrySet().iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            int id = ((Integer) entry.getValue()).intValue();
            String word = entry.getKey().toString();
            sa[id] = word;
            if (word.length() > maxLen) {
                maxLen = word.length();
            }
        }
        byte[] buffer = new byte[maxLen + 1];
        for (int i = 1; i <= dictSize; i++) {
            String word = sa[i];
            int len = word.length();
            dynCharset.getBytes(word, 0, len, buffer, 0);
            tmpArray.addIntValue(len);
            for (int j = 0; j < len; j++) {
                tmpArray.addIntValue(buffer[j]);
            }
        }
        // write charset
        dynCharset.write(out);
        // write dictionary
        out.writeInt(dictSize);
        out.writeShort(maxLen);
        tmpArray.compact();
        tmpArray.write(out);
        tmpArray.dispose();
        // write form pattern definitions
        fpDefArray.compact();
        fpDefArray.write(out);
        // write form patters
        formPatternsArray.compact();
        formPatternsArray.write(out);
    }

    public FormPattern deserializeFormPattern() {
        int id = formPatternsArray.getIntValue(readPos++);
        return int2FormPatterns[id];
    }

    public void serializeFormPatter(FormPattern fp) {
        Integer index = (Integer) formPatterns2Int.get(fp);
        if (index == null) {
            String affixes = fp.getAffixes();
            int len = affixes.length();
            if (affixes != null && len > 0) {
                Integer id = (Integer) dictionary.get(affixes);
                if (id == null) {
                    id = new Integer(dictionary.size() + 1);
                    dictionary.put(affixes, id);
                }
                fpDefArray.addIntValue(id.intValue());
            } else {
                throw new NullPointerException();
            }
            int gplId = fp.getGrammaticalPropertiesList() == null ? 0 : fp.getGrammaticalPropertiesList().getId();
            fpDefArray.addIntValue(gplId);
            index = new Integer(formPatternCounter++);
            formPatterns2Int.put(fp, index);
        }
        formPatternsArray.addIntValue(index.intValue());
    }

    public void dispose() {
        if (formPatternsArray != null) {
            formPatternsArray.dispose();
            formPatternsArray = null;
        }
        if (fpDefArray != null) {
            fpDefArray.dispose();
            fpDefArray = null;
        }
        if (formPatterns2Int != null) {
            formPatterns2Int.clear();
            formPatterns2Int = null;
        }
        affixes = null;
        if (dictionary != null) {
            dictionary.clear();
            dictionary = null;
        }
    }
}
