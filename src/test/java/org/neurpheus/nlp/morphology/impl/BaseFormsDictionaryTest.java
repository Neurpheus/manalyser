/*
 * BaseFormsDictionaryTest.java
 * JUnit based test
 *
 * Created on 27 sierpieñ 2006, 23:40
 */

package org.neurpheus.nlp.morphology.impl;

import java.util.Collection;
import junit.framework.*;
import org.neurpheus.nlp.morphology.BaseFormsDictionary;
import org.neurpheus.nlp.morphology.ExtendedInflectionPattern;
import org.neurpheus.nlp.morphology.inflection.InflectionPatternImpl;
import org.neurpheus.nlp.morphology.inflection.InflectionPatternsMap;

/**
 *
 * @author Jakub
 */
public class BaseFormsDictionaryTest extends TestCase {

    private static ExtendedInflectionPattern ipA = new InflectionPatternImpl("krowa krowy krowie krowami krowach", false, true);
    private static ExtendedInflectionPattern ipB = new InflectionPatternImpl("bic bicie bili biliby bilibysmy bily", false, true);
    private static ExtendedInflectionPattern ipC = new InflectionPatternImpl("kot kota kocie kotu kotami kotach koty", false, true);
    private static ExtendedInflectionPattern ipD = new InflectionPatternImpl("niebieski niebieskiego niebieskiemu", false, true);
    
    
    public BaseFormsDictionaryTest(String testName) {
        super(testName);
    }

    private BaseFormsDictionary createDict(int pass) {
        BaseFormsDictionary dict = new SimpleBaseFormsDictionary();
        InflectionPatternsMap map = new InflectionPatternsMap();
        dict.addBaseForm("abcdef", ipA);
        dict.addBaseForm("abc", ipA);
        dict.addBaseForm("abcdef", ipA);
        dict.addBaseForm("abcdef", ipB);
        dict.addBaseForm("fg", ipA);
        dict.addBaseForm("fg", ipB);
        dict.addBaseForm("fg", ipC);
        dict.addBaseForm("fgabcdef", ipD);
        
        if (pass == 1) {
            dict = new CompactBaseFormsDictionary(dict, map);
        }
        
        return dict;
    }
    
    public void testDictionary() {
        
        for (int pass = 0; pass < 2; pass++) {
        
            BaseFormsDictionary dict = createDict(pass);

            Collection baseForms = dict.getBaseForms();
            assertEquals(4, baseForms.size());
            assertTrue(baseForms.contains("abcdef"));
            assertTrue(baseForms.contains("abc"));
            assertTrue(baseForms.contains("fg"));
            assertTrue(baseForms.contains("fgabcdef"));


            assertTrue(dict.contains("abcdef"));
            assertTrue(dict.contains("abc"));
            assertTrue(dict.contains("fg"));
            assertTrue(dict.contains("fgabcdef"));
            assertFalse(dict.contains("abcd"));
            assertFalse(dict.contains("abcdefg"));

            ExtendedInflectionPattern[] ipa;

            ipa = dict.getInflectionPatterns("abcdef");
            assertEquals(2, ipa.length);
            assertTrue(ipa[0] == ipA);
            assertTrue(ipa[1] == ipB);

            ipa = dict.getInflectionPatterns("abc");
            assertEquals(1, ipa.length);
            assertTrue(ipa[0] == ipA);

            ipa = dict.getInflectionPatterns("fg");
            assertEquals(3, ipa.length);
            assertTrue(ipa[0] == ipA);
            assertTrue(ipa[1] == ipB);
            assertTrue(ipa[2] == ipC);

            ipa = dict.getInflectionPatterns("fgabcdef");
            assertEquals(1, ipa.length);
            assertTrue(ipa[0] == ipD);

            assertNull(dict.getInflectionPatterns("abcd"));
            assertNull(dict.getInflectionPatterns("abcdefg"));
            assertNull(dict.getInflectionPatterns("ab"));
            assertNull(dict.getInflectionPatterns("a"));
            assertNull(dict.getInflectionPatterns(""));
            
        }
        
    }

    
}
