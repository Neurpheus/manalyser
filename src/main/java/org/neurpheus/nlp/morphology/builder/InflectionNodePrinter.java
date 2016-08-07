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
package org.neurpheus.nlp.morphology.builder;

import org.neurpheus.collections.tree.util.DefaultNodePrinter;



public class InflectionNodePrinter extends DefaultNodePrinter {
    
    public String getValueString(Object value) {
        char c = (char) Integer.parseInt(value.toString());
        return "" + c;
    }

}