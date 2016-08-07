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
package org.neurpheus.nlp.morphology.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.log4j.Logger;
import org.neurpheus.collections.tree.Tree;
import org.neurpheus.collections.tree.TreeLeaf;
import org.neurpheus.collections.tree.TreeNode;
import org.neurpheus.collections.tree.linkedlist.LinkedListTree;
import org.neurpheus.collections.tree.linkedlist.LinkedListTreeFactory;
import org.neurpheus.collections.tree.objecttree.ObjectTreeFactory;
import org.neurpheus.nlp.morphology.BaseFormsDictionary;
import org.neurpheus.nlp.morphology.ExtendedInflectionPattern;
import org.neurpheus.nlp.morphology.inflection.InflectionPatternsMap;

/**
 * Represents a collection of base forms of a natural language.
 *
 * This is a compact implementation of the {@link BaseFormsDictionary} interface.
 *
 * @author Jakub Strychowski
 */
public class CompactBaseFormsDictionary implements BaseFormsDictionary, Serializable {

    /** Unique serialization identifier of this class. */
    static final long serialVersionUID = 770608060908140005L;
    
    /** Holds logger for this class. */
    private static Logger logger = Logger.getLogger(CompactBaseFormsDictionary.class);
    
    /** Represents the dictionary data. */
    private LinkedListTree lltree;
    
    /** Maps the integer values to the arrays of inflection patterns. */
    private transient InflectionPatternsMap ipmap;
    
    /** 
     * Creates a new instance of SimpleBaseFormsDictionary. 
     *
     * @param baseDict  The base dictionary from with create this dictionary.
     * @param ipm       The inflection patterns map used by this dictionary.
     */
    public CompactBaseFormsDictionary(final BaseFormsDictionary baseDict, final InflectionPatternsMap ipm) {
        ipmap = ipm;
        Tree objTree = ObjectTreeFactory.getInstance().createTree();
        ArrayList baseForms = new ArrayList();
        baseForms.addAll(baseDict.getBaseForms());
        Collections.sort(baseForms);
        for (Iterator it = baseForms.iterator(); it.hasNext();) {
            String baseForm = (String) it.next();
            ExtendedInflectionPattern[] ipa = baseDict.getInflectionPatterns(baseForm);
            addBaseForm(baseForm, ipa, objTree);
        }
        lltree = (LinkedListTree) LinkedListTreeFactory.getInstance().createTree(objTree, true, true);
    }
    
    /**
     * Adds the given base form described by the given array of 
     * inflection patterns to the dictionary. 
     *
     * @param baseForm The base form to add.
     * @param ipa The array of inflection patters which describe given form.
     * @param tree The tree which represents the dictionary.
     */
    private void addBaseForm(final String baseForm, final ExtendedInflectionPattern[] ipa, final Tree tree) {
        int ipaIndex = ipmap.add(ipa);
        Object data = new Integer(ipaIndex);
        char[] array = baseForm.toCharArray();
        TreeNode parentNode = null;
        TreeNode node = tree.getRoot();
        for (int i = 0; i < array.length; i++) {
            parentNode = node;
            char c = array[i];
            node = parentNode.getChild(new Character(c));
            if (node == null) {
                node = tree.getFactory().createTreeNode(new Character(c));
                parentNode.addChild(node);
            }
        }
        if (node.isLeaf()) {
            throw new IllegalStateException("Tree node should not be a leaf here.");
        } else {
            TreeLeaf leaf = tree.getFactory().createTreeLeaf(node.getValue(), data);
            leaf.setChildren(node.getChildren());
            parentNode.replaceChild(node, leaf);
        }
    }
    
    /**
     * Adds new base form the the dictionary. 
     *
     * @param baseForm The new base form which should be stored in the dictionary.
     * @param ip The inflectio pattern of the given lexeme.
     */
    public void addBaseForm(final String baseForm, final ExtendedInflectionPattern ip) {
        throw new UnsupportedOperationException("This dictionary can be built only by the constructor.");
    }
    
    /**
     * Checks if the dictionary contains the given base forms.
     *
     * @param baseForm The base form which may be in the dictionary.
     * @return <code>true</code> if the given form exists in the dictionary.
     */
    public boolean contains(final String baseForm) {
        TreeNode node = lltree.getRoot();
        for (int i = 0; i < baseForm.length(); i++) {
            char c = baseForm.charAt(i);
            node = node.getChild(new Integer(c));
            if (node == null) {
                return false;
            }
        }
        return node.isLeaf();
    }

    /**
     * Returns all base forms mapped to corresponding inflection pattern arrays.
     *
     * @return The mapping between base forms and arrays of inflection patterns.
     */
    public Map getBaseFormsMap() {
        ArrayList bfa = new ArrayList();
        ArrayList ipa = new ArrayList();
        for (Iterator it = lltree.getRoot().getChildren().iterator(); it.hasNext();) {
            TreeNode child = (TreeNode) it.next();
            getBaseForms(child, new StringBuffer(), bfa, ipa);
        }
        Map res = new HashMap();
        for (int i = bfa.size() - 1; i >= 0; i--) {
            String baseForm = (String) bfa.get(i);
            res.put(baseForm, ipa.get(i));
        }
        return res;
    }
    
    /**
     * Returns all base forms stored in the dictionary.
     *
     * @return A collection of base forms in a particular language.
     */
    public Collection getBaseForms() {
        ArrayList res = new ArrayList();
        for (Iterator it = lltree.getRoot().getChildren().iterator(); it.hasNext();) {
            TreeNode child = (TreeNode) it.next();
            getBaseForms(child, new StringBuffer(), res, null);
        }
        return res;
    }
    
    /**
     * Collects base forms walking through thegiven tree node.
     *
     * @param node The node in the tree holding all forms.
     * @param form The string constructed from the parent nodes.
     * @param res The list of all forms.
     */
    private void getBaseForms(final TreeNode node, final StringBuffer form, final ArrayList res, final ArrayList ipRes) {
        form.append((char) (Integer.parseInt(node.getValue().toString())));
        if (node.isLeaf()) {
            res.add(form.toString());
            if (ipRes != null) {
                int ipaIndex = ((Integer) ((TreeLeaf) node).getData()).intValue();
                ExtendedInflectionPattern[] ipa = (ExtendedInflectionPattern[]) ipmap.get(ipaIndex);
                ipRes.add(ipa);
            }
        }
        for (Iterator it = node.getChildren().iterator(); it.hasNext();) {
            TreeNode child = (TreeNode) it.next();
            getBaseForms(child, form, res, ipRes);
        }
        form.setLength(form.length() - 1);
    }
    
    /**
     * Returns an array of inflection patters related with the given base form. 
     *
     * @param baseForm The base form of a lexeme
     *                 for which this method should return inflection patterns.
     *
     * @return         An array of inflection patterns related with the given lexeme.
     */
    public ExtendedInflectionPattern[] getInflectionPatterns(final String baseForm) {
        TreeNode node = lltree.getRoot();
        for (int i = 0; i < baseForm.length(); i++) {
            char c = baseForm.charAt(i);
            node = node.getChild(new Integer(c));
            if (node == null) {
                return null;
            }
        }
        if (node.isLeaf()) {
            int ipaIndex = ((Integer) ((TreeLeaf) node).getData()).intValue();
            ExtendedInflectionPattern[] ipa = (ExtendedInflectionPattern[]) ipmap.get(ipaIndex);
            return ipa;
        } else {
            return null;
        }
    }

    /**
     * Returns an inflection patterns map used by this dictionary.
     *
     * @return The IP mapping.
     */
    public InflectionPatternsMap getInflectionPatternsMap() {
        return ipmap;
    }

    /**
     * Sets a new inflection pattern map which is used by this dictionary.
     *
     * @param   newIPMap    The new IP mapping.
     */
    public void setInflectionPatternsMap(final InflectionPatternsMap newIPMap) {
        this.ipmap = newIPMap;
    }
            
    
}
