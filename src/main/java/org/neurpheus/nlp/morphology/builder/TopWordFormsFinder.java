/*
 * TopWordFormsFinder.java
 *
 * Created on 12 listopad 2006, 10:38
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.neurpheus.nlp.morphology.builder;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Search for most frequenty used word forms analysisng a set of text (corpus) written in a given language.
 *
 * @author Jakub Strychowski
 */
public class TopWordFormsFinder {
    
    /** Creates a new instance of TopWordFormsFinder */
    public TopWordFormsFinder() {
    }
    
    private static void processFile(HashMap forms, File f, String charsetName) throws IOException {
        InputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(f));
            int fileSize = (int) f.length();
            ByteArrayOutputStream buffer = new ByteArrayOutputStream(fileSize);
            byte[] tab = new byte[fileSize < 65536 ? fileSize : 65536];
            int count = -1;
            do {
                count = in.read(tab);
                if (count > 0) {
                    buffer.write(tab, 0, count);
                }
            } while (count != -1);
            String text = buffer.toString(charsetName);
            tab = null;
            buffer.close();
            buffer = null;
            // process string
            String[] tokens = text.split("\\s|\\p{Punct}|\\d|_|—|–|\\?");
            for (int i = tokens.length - 1; i >= 0; i--) {
                String token = tokens[i].trim().toLowerCase();
                if (token.length() > 0) {
                    Integer occurrences = (Integer) forms.get(token);
                    if (occurrences == null) {
                        occurrences = new Integer(1);
                    } else {
                        occurrences = new Integer(occurrences.intValue() + 1);
                    }
                    forms.put(token, occurrences);
                }
            }
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }

    private static void processDirectory(HashMap forms, File dir, String charsetName) throws IOException {
        File[] items = dir.listFiles();
        for (int i = 0; i < items.length; i++) {
            File f = items[i];
            if (f.isFile()) {
                processFile(forms, f, charsetName);
            } else {
                processDirectory(forms, f, charsetName);
            }
        }
    }
    
    public static void findTopWordForms(
            final String corpusPath, 
            final String resultFilePath, 
            final int numberOfWords,
            final String charsetName
            ) throws IOException {
        
        File corpusDir = new File(corpusPath);
        if (!corpusDir.isDirectory()) {
            throw new IOException("Given path to corpus file is not a valid directory.");
        }
        
        File resultFile = new File(resultFilePath);
        if (resultFile.exists() && !resultFile.canWrite()) {
            throw new IOException("Cannot write to the result file.");
        } 
        HashMap forms= new HashMap();
        
        // process all files in the corpus didj
        processDirectory(forms, corpusDir, charsetName);
        
        // count number of all forms
        int allFormsCount = 0;
        for (Iterator it = forms.values().iterator(); it.hasNext();) {
            allFormsCount += ((Integer) it.next()).intValue();
        }
        
        // sort forms according to the number of occurrences
        ArrayList list = new ArrayList();
        list.addAll(forms.entrySet());
        Collections.sort(list, new Comparator() {
            public int compare(Object a, Object b) {
                Integer ia = (Integer) ((Map.Entry) a).getValue();
                Integer ib = (Integer) ((Map.Entry) b).getValue();
                return -ia.compareTo(ib);
            }
        });
        
        // output top forms
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new BufferedOutputStream(new FileOutputStream(resultFile)));
            int index = 0;
            int topOccurrences = 0;
            for (Iterator it = list.iterator(); it.hasNext() && index < numberOfWords; index++) {
                Map.Entry entry = (Map.Entry) it.next();
                String form = (String) entry.getKey();
                int occurrences = ((Integer) entry.getValue()).intValue();
                topOccurrences += occurrences;
                writer.print(index + 1);
                writer.print('\t');
                writer.print(form);
                writer.print('\t');
                writer.print(occurrences);
                writer.print('\t');
                writer.print(100.0 * occurrences / allFormsCount);
                writer.print("%\t");
                writer.print(100.0 * topOccurrences / allFormsCount);
                writer.println('%');
            }
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
    
    
    public static void main(String[] args) {
        try {
            findTopWordForms("korpus", "topwordforms.txt", 3000, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
