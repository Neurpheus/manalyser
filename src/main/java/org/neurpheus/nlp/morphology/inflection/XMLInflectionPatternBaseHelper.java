/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.neurpheus.nlp.morphology.inflection;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.neurpheus.nlp.morphology.ExtendedInflectionPattern;
import org.neurpheus.nlp.morphology.builder.MorphologicalAnalyserBuildException;
import org.neurpheus.nlp.morphology.inflection.xml.CoveredPattern;
import org.neurpheus.nlp.morphology.inflection.xml.CoveredPatterns;
import org.neurpheus.nlp.morphology.inflection.xml.InflectionPatternDefinition;
import org.neurpheus.nlp.morphology.inflection.xml.Pattern;
import org.neurpheus.nlp.morphology.tagset.GrammaticalProperties;
import org.neurpheus.nlp.morphology.tagset.GrammaticalPropertiesList;
import org.neurpheus.nlp.morphology.tagset.Tagset;
import org.xml.sax.SAXException;

/**
 *
 * @author jstrychowski
 */
public class XMLInflectionPatternBaseHelper {
    
    /** The logger used by this class. */
    private static Logger logger = Logger.getLogger(XMLInflectionPatternBaseHelper.class.getName());
    
    
    public static String cores2String(ExtendedInflectionPattern ip, String separator) {
        StringBuffer buffer = new StringBuffer();
        for (final Iterator it = ip.getCoveredCores().iterator(); it.hasNext();) {
            String core = it.next().toString();
            if (buffer.length() > 0) {
                buffer.append(separator);
            }
            buffer.append(core);
        }
        return buffer.toString();
    }
    
    public static InflectionPatternDefinition createInflectionPatternDefinition(ExtendedInflectionPattern ip) {
        InflectionPatternDefinition result = new InflectionPatternDefinition();
        String code = ip.getAffixesString();
        result.setPatternCode(code);
        result.setCores(cores2String(ip, " "));

        CoveredPatterns cps = new CoveredPatterns();
        CoveredPattern cp = new CoveredPattern();
        cp.setPatternCode(code);
        cps.addCoveredPattern(cp);
        result.setCoveredPatterns(cps);
        
        Pattern pattern = new Pattern();
        for (final Iterator it = ip.getAllFormPatterns().iterator(); it.hasNext();) {
            FormPattern fp = (FormPattern) it.next();
            org.neurpheus.nlp.morphology.inflection.xml.FormPattern xfp = 
                    new org.neurpheus.nlp.morphology.inflection.xml.FormPattern();
            xfp.setAffixes(fp.getAffixes());
            org.neurpheus.nlp.morphology.inflection.xml.GrammaticalPropertiesList xgpl = 
                    new org.neurpheus.nlp.morphology.inflection.xml.GrammaticalPropertiesList();
            GrammaticalPropertiesList gpl = fp.getGrammaticalPropertiesList();
            if (gpl == null || gpl.getGrammaticalProperties().size() == 0) {
                xgpl.addGrammaticalPropertiesMark("xxx");
            } else {
                for (final Iterator it2 = gpl.getGrammaticalProperties().iterator(); it2.hasNext(); ) {
                    GrammaticalProperties gp = (GrammaticalProperties) it2.next();
                    xgpl.addGrammaticalPropertiesMark(gp.getMark());
                }
            }
            xfp.setGrammaticalPropertiesList(xgpl);
            pattern.addFormPattern(xfp);
        }
        result.setPattern(pattern);
        return result;
        
    }

    public static org.neurpheus.nlp.morphology.inflection.xml.InflectionPatternsBase createXMLBase(InflectionPatternsBase ipb)  {
        org.neurpheus.nlp.morphology.inflection.xml.InflectionPatternsBase result =
                new org.neurpheus.nlp.morphology.inflection.xml.InflectionPatternsBase();
        List patterns = ipb.getInflectionPatterns();
        InflectionPatternDefinition[] definitions = new InflectionPatternDefinition[patterns.size()];
        int index = 0;
        for (final Iterator it = patterns.iterator(); it.hasNext(); index++) {
            ExtendedInflectionPattern ip = (ExtendedInflectionPattern) it.next();
            InflectionPatternDefinition ipd = createInflectionPatternDefinition(ip);
            definitions[index] = ipd;
        }
        result.setInflectionPatternDefinition(definitions);
        return result;
    }
    
    
    public static void writeAsXML(InflectionPatternsBase ipb, String path) throws IOException {
        if (ipb == null) {            
            throw new NullPointerException("The [ipb] argument cannot be null.");            
        } else if (path == null) {            
            throw new NullPointerException("The [path] argument cannot be null.");            
        }
        org.neurpheus.nlp.morphology.inflection.xml.InflectionPatternsBase target =
                createXMLBase(ipb);
        OutputStream out = null;
        try {
            out = new BufferedOutputStream(new FileOutputStream(new File(path)));
            target.write(out, "utf-8");
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
    
    public static org.neurpheus.nlp.morphology.inflection.xml.InflectionPatternsBase readFromXml(final String path) throws IOException {
        InputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(new File(path)));
            try {
                return org.neurpheus.nlp.morphology.inflection.xml.InflectionPatternsBase.read(in);
            } catch (SAXException e) {
                logger.log(Level.SEVERE, "XML parsing error", e);
                throw new IOException(e.getMessage());
            } catch (ParserConfigurationException e) {
                logger.log(Level.SEVERE, "XML parser configuration error", e);
                throw new IOException(e.getMessage());
            }
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }

    public static void applyTags(
            InflectionPatternsBase ipb, 
            org.neurpheus.nlp.morphology.inflection.xml.InflectionPatternsBase xipb,
            Tagset tagset) 
            throws MorphologicalAnalyserBuildException {
        
        HashMap patternsMap = new HashMap();
        for (final Iterator it = ipb.getInflectionPatterns().iterator(); it.hasNext();) {
            ExtendedInflectionPattern ip = (ExtendedInflectionPattern) it.next();
            patternsMap.put(ip.getAffixesString(), ip);
        }
        
        InflectionPatternDefinition[] ipdefs = xipb.getInflectionPatternDefinition();
        for (int i = 0; i < ipdefs.length; i++) {
            InflectionPatternDefinition ipdef = ipdefs[i];
            CoveredPattern[] cps = ipdef.getCoveredPatterns().getCoveredPattern();
            for (int j = 0; j < cps.length; j++) {
                CoveredPattern cp = cps[j];
                String code = cp.getPatternCode();
                ExtendedInflectionPattern ip = (ExtendedInflectionPattern) patternsMap.get(code);
                if (ip != null) {
                    applyTags(ip, ipdef, tagset);
                } else {
                    logger.warning("Cannot find an inflection pattern in the inflection pattern base." 
                            + "The inflection pattern code = " + code);
                    
                }
            }
        }
    }

    private static FormPattern createFormPattern(org.neurpheus.nlp.morphology.inflection.xml.FormPattern fp, Tagset tagset) {
        String[] marksTab = fp.getGrammaticalPropertiesList().getGrammaticalPropertiesMark();
        StringBuffer marks = new StringBuffer();
        for (int i = 0; i < marksTab.length; i++) {
            if (marks.length() > 0) {
                marks.append(GrammaticalPropertiesList.MARK_SEPARATOR);
            }
            marks.append(marksTab[i]);
        }
        GrammaticalPropertiesList gpl = tagset.getGrammaticalPropertiesList(marks.toString());
        FormPattern res = new FormPattern(fp.getAffixes(), gpl);
        return res;
    }
    
    public static void applyTags(ExtendedInflectionPattern ip, InflectionPatternDefinition ipdef, Tagset tagset) {
        Pattern pattern = ipdef.getPattern();
        org.neurpheus.nlp.morphology.inflection.xml.FormPattern[] forms = pattern.getFormPattern();
        if (forms.length > 0) {
            int index = 0;
            FormPattern fp = createFormPattern(forms[index++], tagset);
            ip.setBaseFormPattern(fp);
            ArrayList other = new ArrayList();
            while (index < forms.length) {
                fp = createFormPattern(forms[index++], tagset);
                other.add(fp);
            }
            ip.setOtherFormPatterns(other);
        }
    }
    
}
