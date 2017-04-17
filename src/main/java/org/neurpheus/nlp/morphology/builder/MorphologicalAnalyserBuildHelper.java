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

import java.util.HashSet;
import java.util.logging.Logger;
import static org.neurpheus.nlp.morphology.impl.MorphologicalAnalyserImpl.WILDCARD_CHARACTER;


/**
 *
 * @author Jakub Strychowski
 */
public class MorphologicalAnalyserBuildHelper {
    
    private Logger logger = Logger.getLogger(MorphologicalAnalyserBuildHelper.class.getName());
    
    public static char NULLCHAR_CHARACTER = '~';
    
    public static String getLongestSubString(String[] forms, boolean acceptInfixes, boolean acceptPrefixes) {
        if (forms.length == 0) {
            return "";
        }
        String w = forms[0];
        int lMax = acceptPrefixes ? w.length() - 1 : 0;
        String result = "";
        int resultLen = 0;
        for (int l = 0; l <= lMax; l++) {
            // for (int p = w.length() - 1; p >= l && p > (l + res.length() - 1); p--) {
            for (int p = w.length() - 1; p >= l + resultLen; p--) {
                String substring = w.substring(l, p + 1);
                // check if substring is correct for all forms
                boolean isCommon = true;
                for (int i = forms.length - 1; i > 0 && isCommon; i--) {
                    if (acceptPrefixes) {
                        isCommon = forms[i].indexOf(substring) >= 0;
                    } else {
                        isCommon = forms[i].startsWith(substring);
                    }
                }
                if (isCommon) {
                    resultLen = substring.length();
                    result = substring;
                    p = -1; // break loop
                }
            }
        }
        return result == null ? "" : result;
    }
    
    /**
     *  Determines best core for given forms of a word.
     *  Method generates all possible cores and selected the best one, evaluating a mark for each acceptable core.
     *
     *  @param forms all posible forms of a word for each a core shoule be determined
     *  @param acceptInfixes a flag which denotes if infixes are acceptable within a result core.
     *  @param acceptPrefixes a flag which denotest if prefix is acceptable at the begining of a result core.
     *
     *  @return the best core for given forms.
     */
    public static String determineCore(String[] forms, boolean acceptInfixes, boolean acceptPrefixes) {
        // Create candidates set.
        HashSet cores = new HashSet();
        // Get longest substring of all forms.
        String longest = getLongestSubString(forms, acceptInfixes, acceptPrefixes);
        boolean prefix = false;
        boolean suffix = false;
        for (int i = forms.length - 1; i >= 0 && (!prefix || !suffix); i--) {
            if (!forms[i].startsWith(longest)) {
                prefix = true;
            }
            if (!forms[i].endsWith(longest)) {
                suffix = true;
            }
        }
        if (suffix) {
            longest += WILDCARD_CHARACTER;
        }
        if (prefix) {
            longest = WILDCARD_CHARACTER + longest;
        }
        return longest;
        /* 
        // The following code determines cores which deal with infixes.
        // For the Polish word "pies" (eng. dog), which can have 
        // forms like "pies", "psa", "psem", "psami", "psach", ..., 
        // this algorithm determines core "p*s*", because
        // infix "*ie*" occurs in this paradigm.
        // Infixes allows to create a better paradigm, but
        // they make computation more complex and time consuming.
        // For many language infixes occur rearly therefore
        // this algorithm is turned off in this version of analyser.
        // More over this code needs refactorisation.
        if (longest.length() < 3) {
            longest = null;
        }

        byte NULLCHAR = String.getByte(NULLCHAR_CHARACTER);
        byte WILDCARD = String.getByte(WILDCARD_CHARACTER);
        
        // Iterate through the all forms.
        for (int i = forms.length - 1; i >= 0; i--) {
            byte[] form = forms[i].getBackingArray();
            int longestPositionStart = longest == null ? -1 : forms[i].indexOf(longest);
            int longestPositionEnd = longest == null ? -1 : longestPositionStart + longest.length() - 1;
            // add all possible cores of an analysed form using following algorithm:
            // 1. Declare a temporary array of characters.
            // 2. Fill temporary array with unknow character flag.
            // 3. Go from the begining position of the analysed form to the end position:
            //  3.1. If a character at current position is unknow, set a wildcard character on this place.
            //  3.2. If the current character is the wildcard character set a character from the analysed form 
            //      on this position.
            //  3.3. If the current character is any character from the analysed form, go back to previus position.
            // 4. If a current positions is at the end of the string, 
            //      create a core candidate using the temporary array.
            byte[] tmp = new byte[form.length + 1];
            Arrays.fill(tmp, NULLCHAR);
            int pos = 0;
            boolean skipped = false;
            do {
                if (pos == longestPositionStart && !skipped) {
                    // skip longest string which is shared between all forms
                    while (pos <= longestPositionEnd) {
                        tmp[pos] = form[pos];
                        pos++;
                    }
                    tmp[pos] = NULLCHAR;
                    skipped = true;
                } else if (tmp[pos] == NULLCHAR) {
                    tmp[pos] = WILDCARD;
                    pos++;
                    tmp[pos] = NULLCHAR;
                } else if (tmp[pos] == WILDCARD) {
                    tmp[pos] = form[pos];
                    pos++;
                    tmp[pos] = NULLCHAR;
                } else {
                    pos--;
                }
                // 4. If a current positions is at the end of the string, 
                //      create a core candidate using the temporary array.
                if (pos == form.length) {
                    // Check if there is an unacceptable prefix.
                    if (acceptPrefixes || tmp[0] != WILDCARD) {
                        // Create the candidating core using the temporary array.
                        String buffer = new String();
                        for (int j = 0; j < tmp.length - 1; j++) {
                            if (tmp[j] == WILDCARD) {
                                if (j == 0 || tmp[j - 1] != WILDCARD) {
                                    buffer.append(new String(".*"));
                                }
                            } else {
                                buffer.append(tmp[j]);
                            }
                        }
                        // Check if there are unacceptable infixes.
                        if (!acceptInfixes) {
                            boolean hasInfix = false;
                            for (int j = 0; j < buffer.length(); j++) {
                                hasInfix |= buffer.charAt(j) == WILDCARD_CHARACTER && j > 1 && j < buffer.length() - 1;
                            }
                            if (hasInfix) {
                                buffer = null;
                            }
                        }
                        // Add candidating core to the candidates set.
                        if (buffer != null) {
                            cores.add(buffer.compact());
                        }
                    }
                    pos--;
                }
            } while (pos >= 0);
        }
        // select the best candidate
        int bestMark = -1;
        String bestCore = String.newEmptyString();
        for (Iterator it = cores.iterator(); it.hasNext(); ) {
            String core=(String)(it.next());
            int mark = 0;
            for (int j = core.length() - 1; j >= 0; j--) {
                if (core.charAt(j) == WILDCARD_CHARACTER) {
                    mark--;
                    j--;
                } else {
                    mark++;
                }
            }
            mark *= forms.length;
            Pattern pattern = Pattern.compile(core.toString());
            for (int i = forms.length - 1; i >= 0; i--) {
                if (pattern.matcher(forms[i]).matches()) {
                    mark += forms[i].length();
                } else {
                    mark = -1;
                    break;
                }
            }
            if (mark > bestMark) {
                bestCore=core;
                bestMark = mark; 
            }
        }
        StringBuffer res = new StringBuffer();
        for (int i = 0; i < bestCore.length(); i++) {
            if (bestCore.charAt(i) != '.') {
                res.append(bestCore.charAt(i));
            }
        }
        return res.toString();
         */
    }

    public static String getSupplement(String form, String core) {
        if (core == null || core.length() == 0 || core.equals("*")) {
            return form;
        }
        int coreLen = core.length();
        boolean allowPrefix = core.charAt(0) == WILDCARD_CHARACTER;
        boolean allowSuffix = core.charAt(coreLen - 1) == WILDCARD_CHARACTER;
        String pureCore = core.substring(allowPrefix ? 1 : 0, allowSuffix ? coreLen - 1 : coreLen);
        int pos = form.lastIndexOf(pureCore);
        if (pos >= 0) {
            StringBuffer buffer = new StringBuffer();
            if (pos > 0) {
                buffer.append(form.substring(0, pos));
            }
            buffer.append(WILDCARD_CHARACTER);
            buffer.append(form.substring(pos + pureCore.length()));
            return buffer.toString();
        } else {
            return form;
        }
    }
    
// works for infixes    
//    public static String getSupplement(String form, String core) {
//        if (core == null || core.length() == 0 || core.equals("*")) {
//            return form;
//        }
//        char[] cTab = core.toCharArray();
//        char[] fTab = form.toCharArray();
//        char[] rTab = new char[fTab.length];
//        char WILDCARD = WILDCARD_CHARACTER;
//        int cPos = cTab.length - 1;
//        int fPos = fTab.length - 1;
//        int bPos = fPos;
//        int bcPos = cPos;
//        while (cPos >= 0) {
//            if (cTab[cPos] != WILDCARD) {
//                if (cTab[cPos] == fTab[fPos]) {
//                    rTab[fPos] = WILDCARD;
//                    --fPos;
//                    --cPos;
//                } else {
//                    fPos = bPos;
//                    rTab[fPos] = fTab[fPos];
//                    cPos = bcPos;
//                    --fPos;
//                }
//            } else {
//                bPos = fPos;
//                bcPos = cPos;
//                --cPos;
//            }
//        }
//        while (fPos >= 0) {
//            rTab[fPos] = fTab[fPos];
//            --fPos;
//        }
//        StringBuffer res = new StringBuffer();
//        for (int i = 0; i < rTab.length; i++) {
//            if (rTab[i] != WILDCARD || i == 0 || rTab[i - 1] != WILDCARD) {
//                res.append(rTab[i]);
//            }
//        }
//        return res.toString();
//    }
    
    public static String makeForm(String core, String supplement) {
        char[] cTab = core.toCharArray();
        char[] sTab = supplement.toCharArray();
        char WILDCARD = WILDCARD_CHARACTER;
        StringBuffer res = new StringBuffer();
        int sPos = sTab.length - 1;
        int cPos = cTab.length - 1;
        while (sPos >= 0) {
            char sb = sTab[sPos--];
            if (sb == WILDCARD) {
                if (cPos >= 0) {
                    if (cTab[cPos] == WILDCARD) {
                        --cPos;
                    }
                    if (cPos >= 0) {
                        char cb;
                        do {
                            cb =  cTab[cPos--];
                            if (cb != WILDCARD) {
                                res.append(cb);
                            } 
                        } while (cPos >= 0 && cb != WILDCARD);
                    }
                }
            } else {
                res.append(sb);
            }
        }
        res.reverse();
        return res.toString();
    }
    
    
}
