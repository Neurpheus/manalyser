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
package org.neurpheus.nlp.morphology.util;

import junit.framework.*;
import org.neurpheus.nlp.morphology.builder.MorphologicalAnalyserBuildHelper;
import org.neurpheus.core.string.MutableString;


/**
 *
 * @author Jakub Strychowski
 */
public class MorphologyHelperTest extends TestCase {
    
    public final static String[] forms1 = { 
        "abcdefgh1", 
        "abcdefgh", 
        "abcdefgh33", 
        "abcdefgh444", 
        "abcdefgh5"
    };
    public final static String[] forms2 = { 
        "abcdefgh1", 
        "abcdefgh2", 
        "33abcdefgh22"
    };
    public final static String[] forms3 = { 
        "abcdefgh1", 
        "4abcde33fgh2", 
        "33abcdefgh22"
    };
    public final static String[] forms4 = { 
        "abcdefgh1", 
        "abcde33fgh2", 
        "abcdefgh22"
    };
    public final static String[] forms5 = { 
        "ktokolwiek", 
        "kogokolwiek",
        "kimkolwiek",
        "komukolwiek"
    };
    
    public MorphologyHelperTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(MorphologyHelperTest.class);
        
        return suite;
    }

    public void testGetLongestSubString() {
        System.out.println("testGetLongestSubString");

//        assertEquals("abcdefgh", MorphologicalAnalyserBuildHelper.getLongestSubString(forms1, true, true).toString());
//        assertEquals("abcdefgh", MorphologicalAnalyserBuildHelper.getLongestSubString(forms1, true, false).toString());
        assertEquals("abcdefgh", MorphologicalAnalyserBuildHelper.getLongestSubString(forms1, false, true).toString());
        assertEquals("abcdefgh", MorphologicalAnalyserBuildHelper.getLongestSubString(forms1, false, false).toString());
        
//        assertEquals("abcdefgh", MorphologicalAnalyserBuildHelper.getLongestSubString(forms2, true, true).toString());
//        assertEquals("", MorphologicalAnalyserBuildHelper.getLongestSubString(forms2, true, false).toString());
        assertEquals("abcdefgh", MorphologicalAnalyserBuildHelper.getLongestSubString(forms2, false, true).toString());
        assertEquals("", MorphologicalAnalyserBuildHelper.getLongestSubString(forms2, false, false).toString());
        
//        assertEquals("abcde", MorphologicalAnalyserBuildHelper.getLongestSubString(forms3, true, true).toString());
//        assertEquals("", MorphologicalAnalyserBuildHelper.getLongestSubString(forms3, true, false).toString());
        assertEquals("abcde", MorphologicalAnalyserBuildHelper.getLongestSubString(forms3, false, true).toString());
        assertEquals("", MorphologicalAnalyserBuildHelper.getLongestSubString(forms3, false, false).toString());

        assertEquals("kolwiek", MorphologicalAnalyserBuildHelper.getLongestSubString(forms5, false, true).toString());
    
    }

    public void testDetermineCore() {
        System.out.println("testDetermineCore");
        
//        assertEquals("abcdefgh*", MorphologicalAnalyserBuildHelper.determineCore(forms1, true, true).toString());
//        assertEquals("abcdefgh*", MorphologicalAnalyserBuildHelper.determineCore(forms1, true, false).toString());
        assertEquals("abcdefgh*", MorphologicalAnalyserBuildHelper.determineCore(forms1, false, true).toString());
        assertEquals("abcdefgh*", MorphologicalAnalyserBuildHelper.determineCore(forms1, false, false).toString());
        
//        assertEquals("*abcdefgh*", MorphologicalAnalyserBuildHelper.determineCore(forms2, true, true).toString());
//        assertEquals("", MorphologicalAnalyserBuildHelper.determineCore(forms2, true, false).toString());
        assertEquals("*abcdefgh*", MorphologicalAnalyserBuildHelper.determineCore(forms2, false, true).toString());
        assertEquals("", MorphologicalAnalyserBuildHelper.determineCore(forms2, false, false).toString());
        
//        assertEquals("*abcde*fgh*", MorphologicalAnalyserBuildHelper.determineCore(forms3, true, true).toString());
//        assertEquals("", MorphologicalAnalyserBuildHelper.determineCore(forms3, true, false).toString());
        assertEquals("*abcde*", MorphologicalAnalyserBuildHelper.determineCore(forms3, false, true).toString());
        assertEquals("", MorphologicalAnalyserBuildHelper.determineCore(forms3, false, false).toString());

//        assertEquals("abcde*fgh*", MorphologicalAnalyserBuildHelper.determineCore(forms4, true, true).toString());
//        assertEquals("abcde*fgh*", MorphologicalAnalyserBuildHelper.determineCore(forms4, true, false).toString());
        assertEquals("abcde*", MorphologicalAnalyserBuildHelper.determineCore(forms4, false, true).toString());
        assertEquals("abcde*", MorphologicalAnalyserBuildHelper.determineCore(forms4, false, false).toString());
        
        assertEquals("*kolwiek", MorphologicalAnalyserBuildHelper.determineCore(forms5, false, true).toString());
        
    }

    public void testGetSuplement() {
        System.out.println("testGetSuplement");
        String core;
        
//        core = MorphologicalAnalyserBuildHelper.determineCore(forms1, true, true); // "abcdefgh*"
//        assertEquals("*1", MorphologicalAnalyserBuildHelper.getSupplement(forms1[0], core).toString()); // "abcdefgh1"
//        assertEquals("*", MorphologicalAnalyserBuildHelper.getSupplement(forms1[1], core).toString()); // "abcdefgh"
//        assertEquals("*33", MorphologicalAnalyserBuildHelper.getSupplement(forms1[2], core).toString()); // "abcdefgh33"
//        assertEquals("*444", MorphologicalAnalyserBuildHelper.getSupplement(forms1[3], core).toString()); // "abcdefgh444"
//        assertEquals("*5", MorphologicalAnalyserBuildHelper.getSupplement(forms1[4], core).toString()); // "abcdefgh5"
        
//        core = MorphologicalAnalyserBuildHelper.determineCore(forms2, true, true); // "*abcdefgh*"
//        assertEquals("*1", MorphologicalAnalyserBuildHelper.getSupplement(forms2[0], core).toString()); // "abcdefgh1"
//        assertEquals("*2", MorphologicalAnalyserBuildHelper.getSupplement(forms2[1], core).toString()); // "abcdefgh2"
//        assertEquals("33*22", MorphologicalAnalyserBuildHelper.getSupplement(forms2[2], core).toString()); // "33abcdefgh22"
//        core = MorphologicalAnalyserBuildHelper.determineCore(forms2, true, false); // ""
//        assertEquals("abcdefgh1", MorphologicalAnalyserBuildHelper.getSupplement(forms2[0], core).toString()); // "abcdefgh1"
//        assertEquals("abcdefgh2", MorphologicalAnalyserBuildHelper.getSupplement(forms2[1], core).toString()); // "abcdefgh2"
//        assertEquals("33abcdefgh22", MorphologicalAnalyserBuildHelper.getSupplement(forms2[2], core).toString()); // "33abcdefgh22"

        
//        core = MorphologicalAnalyserBuildHelper.determineCore(forms3, true, true); // "*abcde*fgh*"
//        assertEquals("*1", MorphologicalAnalyserBuildHelper.getSupplement(forms3[0], core).toString()); // "abcdefgh1"
//        assertEquals("4*33*2", MorphologicalAnalyserBuildHelper.getSupplement(forms3[1], core).toString()); // "4abcde33fgh2"
//        assertEquals("33*22", MorphologicalAnalyserBuildHelper.getSupplement(forms3[2], core).toString()); // "33abcdefgh22"
//        core = MorphologicalAnalyserBuildHelper.determineCore(forms3, true, false); // ""
//        assertEquals("abcdefgh1", MorphologicalAnalyserBuildHelper.getSupplement(forms3[0], core).toString()); // "abcdefgh1"
//        assertEquals("4abcde33fgh2", MorphologicalAnalyserBuildHelper.getSupplement(forms3[1], core).toString()); // "4abcde33fgh2"
//        assertEquals("33abcdefgh22", MorphologicalAnalyserBuildHelper.getSupplement(forms3[2], core).toString()); // "33abcdefgh22"
        core = MorphologicalAnalyserBuildHelper.determineCore(forms3, false, true); // "*abcde*"
        assertEquals("*fgh1", MorphologicalAnalyserBuildHelper.getSupplement(forms3[0], core).toString()); // "abcdefgh1"
        assertEquals("4*33fgh2", MorphologicalAnalyserBuildHelper.getSupplement(forms3[1], core).toString()); // "4abcde33fgh2"
        assertEquals("33*fgh22", MorphologicalAnalyserBuildHelper.getSupplement(forms3[2], core).toString()); // "33abcdefgh22"

//        core = MorphologicalAnalyserBuildHelper.determineCore(forms4, true, false); // "abcde*fgh*"
//        assertEquals("*1", MorphologicalAnalyserBuildHelper.getSupplement(forms4[0], core).toString()); // "abcdefgh1"
//        assertEquals("*33*2", MorphologicalAnalyserBuildHelper.getSupplement(forms4[1], core).toString()); // "abcde33fgh2"
//        assertEquals("*22", MorphologicalAnalyserBuildHelper.getSupplement(forms4[2], core).toString()); // "abcdefgh22"
        core = MorphologicalAnalyserBuildHelper.determineCore(forms4, false, true); // "abcde*"
        assertEquals("*fgh1", MorphologicalAnalyserBuildHelper.getSupplement(forms4[0], core).toString()); // "abcdefgh1"
        assertEquals("*33fgh2", MorphologicalAnalyserBuildHelper.getSupplement(forms4[1], core).toString()); // "abcde33fgh2"
        assertEquals("*fgh22", MorphologicalAnalyserBuildHelper.getSupplement(forms4[2], core).toString()); // "abcdefgh22"

        // tests infixes        
//        assertEquals("11*22*33*44", MorphologicalAnalyserBuildHelper.getSupplement(
//                "11abc22d33ef44", "*abc*d*ef*").toString()); 
//        assertEquals("11*33*44", MorphologicalAnalyserBuildHelper.getSupplement(
//                "11abcd33ef44", "*abc*d*ef*").toString()); 
//        assertEquals("11*22*44", MorphologicalAnalyserBuildHelper.getSupplement(
//                "11abc22def44", "*abc*d*ef*").toString()); 
//        assertEquals("11*44", MorphologicalAnalyserBuildHelper.getSupplement(
//                "11abcdef44", "*abc*d*ef*").toString()); 
//        assertEquals("*44", MorphologicalAnalyserBuildHelper.getSupplement(
//                "abcdef44", "*abc*d*ef*").toString()); 
//        assertEquals("11*", MorphologicalAnalyserBuildHelper.getSupplement(
//                "11abcdef", "*abc*d*ef*").toString()); 
//        assertEquals("*", MorphologicalAnalyserBuildHelper.getSupplement(
//                "abcdef", "*abc*d*ef*").toString()); 
//
        assertEquals("kto*", MorphologicalAnalyserBuildHelper.getSupplement(
                forms5[0], "*kolwiek").toString()); 
        assertEquals("kogo*", MorphologicalAnalyserBuildHelper.getSupplement(
                forms5[1], "*kolwiek").toString()); 
        assertEquals("kim*", MorphologicalAnalyserBuildHelper.getSupplement(
                forms5[2], "*kolwiek").toString()); 
        assertEquals("komu*", MorphologicalAnalyserBuildHelper.getSupplement(
                forms5[3], "*kolwiek").toString()); 
        
    }

    public void testMakeForm() {
        assertEquals("narwany", MorphologicalAnalyserBuildHelper.makeForm("*narwan*", "*y").toString());
        assertEquals("nienarwanych", MorphologicalAnalyserBuildHelper.makeForm("*narwan*", "nie*ych").toString());
        assertEquals("nienarwanych", MorphologicalAnalyserBuildHelper.makeForm("narwan*", "nie*ych").toString());
        assertEquals("narwany", MorphologicalAnalyserBuildHelper.makeForm("narwany*", "*").toString());
        assertEquals("narwany", MorphologicalAnalyserBuildHelper.makeForm("narwany", "*").toString());
        assertEquals("narwany", MorphologicalAnalyserBuildHelper.makeForm("*narwany*", "*").toString());
    }
    
}
