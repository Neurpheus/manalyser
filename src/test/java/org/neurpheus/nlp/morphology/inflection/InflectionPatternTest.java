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

import junit.framework.*;

/**
 *
 * @author Jakub Strychowski
 */
public class InflectionPatternTest extends TestCase {
    
    public InflectionPatternTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(InflectionPatternTest.class);
        
        return suite;
    }

    public void testInit() {
        String line = "ktokolwiek kogokolwiek kimkolwiek komukolwiek";
        String[] forms = line.split("\\s");
        InflectionPatternImpl ip = new InflectionPatternImpl(forms, false, true);
        assertEquals(1, ip.getCoveredCores().size());
        assertEquals("*kolwiek", ip.getCoveredCores().iterator().next().toString());
    }

    public void testGetCoveredCores() {
    }

    public void testSetCoveredCores() {
    }

    public void testGetCode() {
    }

    public void testAddCoveredCores() {
    }

    public void testCompareTo() {
    }

    public void testPrint() {
    }

    public void testGetOtherSupplements() {
    }

    public void testSetOtherSupplements() {
    }

    public void testSave() {
    }

    public void testLoad() {
    }

    public void testGetBaseSupplement() {
    }

    public void testSetBaseSupplement() {
    }

    public void testGetAllSupplements() {
    }

    public void testDetermineCorePatterns() {
    }

    public void testGetCorePatterns() {
    }

    public void testSetCorePatterns() {
    }

    public void testGetNumberOfCoveredLexemes() {
    }

    public void testSetNumberOfCoveredLexemes() {
    }

    
}
