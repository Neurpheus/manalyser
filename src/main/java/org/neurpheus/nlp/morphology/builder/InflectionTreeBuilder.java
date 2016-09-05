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

import java.util.Iterator;
import java.util.Map;
import org.neurpheus.nlp.morphology.inflection.InflectionPatternImpl;
import org.neurpheus.nlp.morphology.inflection.InflectionPatternsBase;
import org.neurpheus.collections.tree.Tree;
import org.neurpheus.collections.tree.TreeFactory;
import org.neurpheus.collections.tree.TreeNodeWithData;
import org.neurpheus.collections.tree.TreeNode;
import org.neurpheus.collections.tree.objecttree.ObjectTreeFactory;
import org.neurpheus.core.string.StringHelper;
import org.neurpheus.nlp.morphology.BaseFormsDictionary;
import org.neurpheus.nlp.morphology.ExtendedInflectionPattern;
import org.neurpheus.nlp.morphology.inflection.CorePattern;
import org.neurpheus.nlp.morphology.inflection.FormPattern;
import org.neurpheus.nlp.morphology.inflection.InflectionPatternsMap;

/**
 * Constructs an inflection tree from an inflection pattern base and base 
 * forms dictionary.
 *
 * @author Jakub Strychowski
 */
public class InflectionTreeBuilder {

    public final static char BASE_FORM_TAG = '!';
    public final static char CORE_TAG = '/';
    
    /** If <code>true</code> (default value), the builder adds base forms to the inflection tree */
    private final static boolean addBaseFormsToTree = true;
    
    /** If <code>true</code> (default value), the builder adds supplements to the inflection tree */
    private final static boolean addSupplementsToTree = true;
    
    /** If <code>true</code> (default value), the builder adds core patterns to the inflection tree */
    private final static boolean addCorePatternsToTree = true;

    /** If <code>true</code> (default value), the builder adds cores to the inflection tree */
    private final static boolean addCoresToTree = true;
   
    /**
     * Construct an inflection tree from the given inflection pattern base 
     * and the base forms dictionary.
     *
     * @param   ipb     The inflection pattern base for which the inflection tree will be built.
     * @param   dict    The base forms dictionary which will be added to the inflection tree.
     *
     * @return The constructed tree.
     *
     * @throws MorphologicalAnalyserBuildException if any error occurred during tree construction.
     */
    public static Tree buildTree(final InflectionPatternsBase ipb, final BaseFormsDictionary dict) 
    throws MorphologicalAnalyserBuildException {

        TreeFactory treeFactory = ObjectTreeFactory.getInstance();
        Tree tree = treeFactory.createTree();
        
        if (addSupplementsToTree) {
            // for each inflection pattern add its supplements and cores or core patterns.
            for (Iterator it = ipb.getInflectionPatterns().iterator(); it.hasNext(); ) {
                InflectionPatternImpl ip = (InflectionPatternImpl) it.next();
                addSupplement(ip.getBaseFormPattern().getAffixes(), ip, tree, true);
                for (Iterator sit = ip.getOtherFormPatterns().iterator(); sit.hasNext(); ) {
                    FormPattern fp = (FormPattern) sit.next();
                    addSupplement(fp.getAffixes(), ip, tree, false);
                }
            }
        }
        
        if (addBaseFormsToTree) {
            // add each base form
            Map bfMap = dict.getBaseFormsMap();
            for (Iterator it = bfMap.keySet().iterator(); it.hasNext(); ) {
                String baseForm = (String) it.next();
                ExtendedInflectionPattern[] ipa = (ExtendedInflectionPattern[]) bfMap.get(baseForm);
                addBaseForm(baseForm, ipa, tree);
            }
        }
        
        // map arrays of inflection patterns to integers
        InflectionPatternsMap ipmap = ipb.getInflectionPatternsMap();
        mapNodeData(tree.getRoot(), ipmap);
        ipmap.compact();
        
        ObjectTreeFactory.getInstance().sortTree(tree);
        return tree;
    }
    
    private static void mapNodeData(TreeNode node, InflectionPatternsMap ipmap) {
        if (node.hasExtraData()) {
            TreeNodeWithData nodeWithData = (TreeNodeWithData) node;
            Object obj = nodeWithData.getData();
            ExtendedInflectionPattern[] patterns;
            if (obj instanceof ExtendedInflectionPattern) {
                patterns = new ExtendedInflectionPattern[1];
                patterns[0] = (ExtendedInflectionPattern) obj;
            } else {
                patterns = (ExtendedInflectionPattern[]) obj;
            }
            int index = ipmap.add(patterns);
            nodeWithData.setData(new Integer(index));
        }
        for (Iterator it = node.getChildren().iterator(); it.hasNext();) {
            TreeNode child = (TreeNode) it.next();
            mapNodeData(child, ipmap);
        }
    }

    public static void addBaseForm(String baseForm, ExtendedInflectionPattern[] ipTab, Tree tree) {
        addString(BASE_FORM_TAG + baseForm, ipTab, tree);
    }
    
    public static void addSupplement(String supplement, ExtendedInflectionPattern ip, Tree tree, boolean isBase) {
        final char WILDCARD = MorphologicalAnalyserBuildHelper.WILDCARD_CHARACTER;
        boolean added = false;
        if (addCorePatternsToTree && ip.getCorePatterns().size() > 0) {
            for (Iterator it = ip.getCorePatterns().iterator(); it.hasNext(); ) {
                CorePattern cp = (CorePattern) it.next();
                String corePattern = cp.getPattern();
                int pos = supplement.indexOf(WILDCARD);
                if (pos >= 0) {
                    String pureCorePattern = corePattern.charAt(corePattern.length() -1) == WILDCARD ? corePattern.substring(0, corePattern.length() -1) : corePattern;
                    String prefix = supplement.substring(0, pos);
                    if (prefix.length() > 0) {
                        // prefixes are stored in reverse order to speed up searching process.
                        prefix = StringHelper.reverseString(prefix);
                    }
                    String form = prefix + WILDCARD + pureCorePattern + CORE_TAG;
                    if (pos + 1 < supplement.length()) {
                        form += supplement.substring(pos + 1);
                    }
                    addString(form, ip, tree);
                    added = true;
                }
            }
        }
        if (addCoresToTree && ip.getCoveredCores().size() > 0 && !(addBaseFormsToTree && isBase)) {
            for (Iterator it = ip.getCoveredCores().iterator(); it.hasNext(); ) {
                String core = (String) it.next();
                if (core.length() == 0) {
                    added = false;
                } else {
                    int pos = supplement.indexOf(WILDCARD);
                    if (pos >= 0) {
                        String pureCore = core.charAt(core.length() -1) == WILDCARD ? core.substring(0, core.length() -1) : core;
                        if (pureCore.length() > 0 && pureCore.charAt(0) == WILDCARD) {
                            pureCore = pureCore.substring(1);
                        }
                        String prefix = supplement.substring(0, pos);
                        String form = prefix + CORE_TAG + pureCore + CORE_TAG;
                        if (pos + 1 < supplement.length()) {
                            form += supplement.substring(pos + 1);
                        }
                        addString(form, ip, tree);
                    } 
                    added = true;
                }
            }
        }
        if (!added && !(addBaseFormsToTree && isBase)) {    
            addString(supplement, ip, tree);
        }
    }

    
    public static void addString(String s, ExtendedInflectionPattern[] ipTab, Tree tree) {
        for (int i = 0; i < ipTab.length; i++) {
            addString(s, ipTab[i], tree);
        }
    }
    
    public static void addString(String s, ExtendedInflectionPattern data, Tree tree) {
        char[] array = s.toCharArray();
        TreeNode parentNode = null;
        TreeNode node = tree.getRoot();
        for (int i = s.length() - 1; i >= 0; i--) {
            parentNode = node;
            char c = array[i];
            node = parentNode.getChild(new Character(c));
            if (node == null) {
                node = tree.getFactory().createTreeNode(new Character(c));
                parentNode.addChild(node);
            }
        }
        if (node.hasExtraData()) {
            TreeNodeWithData nodeWithData = (TreeNodeWithData) node;
            Object obj = nodeWithData.getData();
            if (obj instanceof ExtendedInflectionPattern[]) {
                ExtendedInflectionPattern[] oldArray = (ExtendedInflectionPattern[]) obj;
                ExtendedInflectionPattern[] newArray = new ExtendedInflectionPattern[oldArray.length + 1];
                System.arraycopy(oldArray, 0, newArray, 0, oldArray.length);
                newArray[oldArray.length] = data;
                nodeWithData.setData(newArray);
            } else {
                ExtendedInflectionPattern[] newArray = new ExtendedInflectionPattern[2];
                newArray[0] = (ExtendedInflectionPattern) obj;
                newArray[1] = data;
                nodeWithData.setData(newArray);
            }
        } else if (data != null) {
            TreeNodeWithData nodeWithData = tree.getFactory().createTreeNodeWithAdditionalData(node.getValue(), data);
            nodeWithData.setChildren(node.getChildren());
            parentNode.replaceChild(node, nodeWithData);
        }
    }
    
}
