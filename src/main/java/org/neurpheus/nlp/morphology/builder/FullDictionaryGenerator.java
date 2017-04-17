/*
 *  © 2015 Jakub Strychowski
 */

package org.neurpheus.nlp.morphology.builder;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.neurpheus.nlp.morphology.builder.MorphologicalAnalyserBuildStep.STEP_GENERATE_FULL_DICTIONARY;
import static org.neurpheus.nlp.morphology.builder.MorphologicalAnalyserBuilderParameters.EXTENSION_DICTIONARY;
import static org.neurpheus.nlp.morphology.builder.MorphologicalAnalyserBuilderParameters.EXTENSION_FULL_DICTIONARY;
import static org.neurpheus.nlp.morphology.builder.MorphologicalAnalyserBuilderParameters.EXTENSION_FULL_DICTIONARY_ADD;
import static org.neurpheus.nlp.morphology.builder.MorphologicalAnalyserBuilderParameters.EXTENSION_FULL_DICTIONARY_REMOVE;
import org.neurpheus.nlp.myspell.FullDictionaryWriter;
import org.neurpheus.nlp.myspell.MySpellDictionary;

/**
 *
 * @author Jakub Strychowski
 */
public class FullDictionaryGenerator {
    
    private static Logger logger = Logger.getLogger(FullDictionaryGenerator.class.getName());
    
    /**
     * Generates a full dictionary file from a MySpell dictionary. Full dictionary file is a text
     * file where each line of the file corresponds to a lexeme. Each lexeme is represented by
     * lexeme forms separated by spaces. First form is a base form.
     *
     *
     * @param dictionariesPath
     * @param symbol
     * @throws MorphologicalAnalyserBuildException if any processing error occurred.
     */
    public static void generateFullDictionary(String dictionariesPath, String symbol) throws
            MorphologicalAnalyserBuildException {

        // read myspell dictionary
        MySpellDictionary dict = new MySpellDictionary();
        try {
            dict.load(dictionariesPath, symbol);
        } catch (Exception e) {
            throw new MorphologicalAnalyserBuildException("Cannot load a MySpell dictionary.", e);
        }

        // read forms which should be omitted from file ..._toremove.txt
        Set<String> ignoredForms = readFormsToIgnore();

        String outPath = getPath(EXTENSION_FULL_DICTIONARY);
        logger.log(Level.INFO, "Generating the full dictionary to the file: {0}", outPath);
        try (PrintStream out = new PrintStream(new BufferedOutputStream(
                new FileOutputStream(outPath)), false, "UTF-8")) {

            // add forms from ..._toadd.txt file
            writeSpecialForms(out);

            dict.processAllForms(new FullDictionaryWriter(out, ignoredForms), false);
            
        } catch (Exception ex) {
            throw new MorphologicalAnalyserBuildException("Cannot generate the full dictionary.", ex);
        }
    }

    /**
     * Adds forms from ..._toadd.txt file.
     * 
     * @param out Stream where special forms should be rewritten.
     * 
     * @throws MorphologicalAnalyserBuildException 
     */
    private static void writeSpecialForms(PrintStream out) throws
            MorphologicalAnalyserBuildException {
        String addPath = getPath(EXTENSION_FULL_DICTIONARY_ADD);
        File addFile = new File(addPath);
        if (addFile.exists() && addFile.canRead()) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(addFile), "UTF-8"))) {
                String line;
                do {
                    line = reader.readLine();
                    if (line != null && line.trim().length() > 0 && !line.startsWith("//")) {
                        out.println(line.trim());
                    }
                } while (line != null);
            } catch (Exception ex) {
                throw new MorphologicalAnalyserBuildException(
                        "Cannot write special forms from file " + addPath, ex);
            }
        }
    }

    /**    
     * Reads from file ..._toremove.txt forms which should be omitted.
     * 
     * @return Collection of forms to ignore while the analyzer build process.
     * 
     * @throws MorphologicalAnalyserBuildException 
     */
    private static Set<String> readFormsToIgnore() throws MorphologicalAnalyserBuildException {
        String removePath = getPath(EXTENSION_FULL_DICTIONARY_REMOVE);
        File removeFile = new File(removePath);
        Set<String> result = new HashSet<>();
        if (removeFile.exists() && removeFile.canRead()) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(removeFile), "UTF-8"))) {
                String line;
                do {
                    line = reader.readLine();
                    if (line != null && line.trim().length() > 0 && !line.startsWith("//")) {
                        String[] forms = line.split(",");
                        for (String form : forms) {
                            String formTrimmed = form.trim();
                            if (formTrimmed.length() > 0) {
                                result.add(formTrimmed);
                            }
                        }
                    }
                } while (line != null);
            } catch (Exception ex) {
                throw new MorphologicalAnalyserBuildException(
                        "Cannot read forms to remove from file: " + removePath, ex);
            }
        }
        return result;
    }
    
    
    /**
     * Generates a full dictionary file from a MySpell dictionary. Full dictionary file is a text
     * file where each line of the file correspondes to a lexeme. Each lexeme is represendted by
     * lexeme forms separated by spaces. First form is a base form.
     *
     *
     * @throws MorpologicalAnalyserBuildException if any processing error occurred.
     */
    public static void regenerateFullDictionary() throws MorphologicalAnalyserBuildException {
        
        String outPath = getPath(EXTENSION_FULL_DICTIONARY);
        
        try (PrintStream out = new PrintStream(new BufferedOutputStream(new FileOutputStream(outPath)), false, "UTF-8")) {
            logger.info("Generating the full dictionary to the file: " + outPath);

            // add forms from ..._toadd.txt file
            writeSpecialForms(out);

            // add forms from ..._toadd.txt file
            rewriteForms(out);

        } catch (Exception e) {
            throw new MorphologicalAnalyserBuildException("Cannot generate the full dictionary.", e);
        }
    }

    private static String getPath(String suffix) {
        return MorphologicalAnalyserBuilder.getPath(suffix);
    }

    private static void rewriteForms(PrintStream out) throws IOException, MorphologicalAnalyserBuildException {
        
        String inPath = getPath(EXTENSION_DICTIONARY);
        
        // read forms which should be omitted from file ..._toremove.txt
        Set<String> ignoredForms = readFormsToIgnore();
        
        File inFile = new File(inPath);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inFile), "UTF-8"))) {
            String line;
            do {
                line = reader.readLine();
                if (line != null && line.trim().length() > 0) {
                    String[] tab = line.split(",");
                    if (tab.length > 0) {
                        String baseForm = tab[0].trim();
                        boolean hasWhitespace = baseForm.indexOf(' ') > 0;
                        if (!hasWhitespace && !ignoredForms.contains(baseForm)) {
                            StringBuilder buffer = new StringBuilder();
                            for (String form : tab) {
                                String formTrimmed = form.trim();
                                if (formTrimmed.length() > 0) {
                                    if (buffer.length() > 0) {
                                        buffer.append(',');
                                    }
                                    buffer.append(formTrimmed);
                                    hasWhitespace |= formTrimmed.indexOf(' ') > 0;
                                }
                            }
                            if (!hasWhitespace && buffer.length() > 0) {
                                out.println(buffer.toString());
                            }
                        }
                    }
                }
            } while (line != null);
        }
    }
    
    
}
