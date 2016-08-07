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

/**
 *  This class performs ... TODO
 *
 * @author Jakub Strychowski
 */
public final class JavaTypesMemoryAllocation {
    
    
    public static final int BYTE_SIZE = 1;
    public static final int CHAR_SIZE = 2;
    public static final int SHORT_SIZE = 2;
    public static final int INT_SIZE = 4;
    public static final int LONG_SIZE = 8;
    public static final int FLOAT_SIZE = 4;
    public static final int DOUBLE_SIZE = 8;
    public static final int BOOLEAN_SIZE = 1;
    
    public static final int EMPTY_STRING_SIZE = 38;
    public static final int EMPTY_ARRAY_SIZE = 14;
    
    public static final int NULL_SIZE = 4;
    public static final int REFERENCE_SIZE = 4;
    public static final int EMPTY_OBJECT_SIZE = 8;
    
    public static final int OBYTE_OBJECT_SIZE = 16;
    public static final int OCHAR_SIZE = 16;
    public static final int OINT_SIZE = 16;
    public static final int OSHORT_SIZE = 16;
    public static final int OLONG_SIZE = 16;
    public static final int OFLOAT_SIZE = 16;
    public static final int ODOUBLE_SIZE = 16;
    public static final int OBOOLEAN_SIZE = 16;

    public static int getStringSize(String s) {
        if (s == null) {
            return NULL_SIZE;
        } else {
            return EMPTY_STRING_SIZE + s.length() * 2;
        }
    }
    
   
}
