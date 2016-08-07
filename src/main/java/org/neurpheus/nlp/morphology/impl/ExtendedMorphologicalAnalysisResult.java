/*
 * MorphologicalAnalysisResultImpl.java
 *
 * Created on 28 wrzesieñ 2006, 10:01
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.neurpheus.nlp.morphology.impl;

import java.util.Iterator;
import org.neurpheus.nlp.morphology.ExtendedInflectionPattern;
import org.neurpheus.nlp.morphology.baseimpl.MorphologicalAnalysisResultImpl;
import org.neurpheus.nlp.morphology.inflection.FormPattern;
import org.neurpheus.nlp.morphology.tagset.GrammaticalPropertiesList;

/**
 *
 * @author Jakub Strychowski
 */
public class ExtendedMorphologicalAnalysisResult extends MorphologicalAnalysisResultImpl {
    
    /** The unique identifier of this class. */
    public static final long serialVersionUID = -770608080505111114L;
    
    //private ExtendedInflectionPattern[] ipa;
    private ExtendedInflectionPattern ip;
    
    /** Creates a new instance of MorphologicalAnalysisResultImpl */
    public ExtendedMorphologicalAnalysisResult() {
    }
    
    public ExtendedMorphologicalAnalysisResult(
            final String baseFormValue, 
            final double weightValue, 
            final String cor,
            final String supplement,
            final GrammaticalPropertiesList gramPropList,
            final ExtendedInflectionPattern ipValue
            ) {
        super(baseFormValue, weightValue, cor, supplement, gramPropList);
        ip = ipValue;
    }

    public ExtendedInflectionPattern getIP() {
        return ip;
    }

    public void setIpa(ExtendedInflectionPattern ipValue) {
        this.ip = ipValue;
    }


    public String toString() {
        StringBuffer res = new StringBuffer(super.toString());
        if (ip != null) {
//            for (int i = 0 ; i < ipa.length; i++) {
                res.append("\n    ");
                res.append(ip.getId());
                res.append(" : ");
                res.append(ip.toString());
//            }
        }
        return res.toString();
    }
    

    public ExtendedInflectionPattern getInflectionPattern() {
        return ip;
    }

    /**
     * Returns grammatical properties of an analysed form.
     * 
     * @return The grammatical properties determined from morphemes of the analysed form.
     */
    public GrammaticalPropertiesList getGrammaticalPropertiesList() {
        if (grammaticalPropertiesList == null && ip != null && grammaticalMorphemes != null) {
            if (grammaticalMorphemes.equals(ip.getBaseFormPattern().getAffixes())) {
                grammaticalPropertiesList = ip.getBaseFormPattern().getGrammaticalPropertiesList();
            } else {
                for (final Iterator it = ip.getOtherFormPatterns().iterator(); it.hasNext();) {
                    FormPattern fp = (FormPattern) it.next();
                    if (grammaticalMorphemes.equals(fp.getAffixes())) {
                        grammaticalPropertiesList = fp.getGrammaticalPropertiesList();
                        return grammaticalPropertiesList;
                    }
                }
            }
        }
        return grammaticalPropertiesList;
    }
    
    
    
}
