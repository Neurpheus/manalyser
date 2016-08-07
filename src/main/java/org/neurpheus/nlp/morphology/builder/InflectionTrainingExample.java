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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.neurpheus.classification.ClassificationResult;
import org.neurpheus.classification.Classifier;
import org.neurpheus.classification.neuralnet.NeuralNetwork;
import org.neurpheus.classification.training.TrainingExample;
import org.neurpheus.classification.training.TrainingExampleFactory;
import org.neurpheus.classification.training.TrainingSetException;
import org.neurpheus.nlp.morphology.ExtendedInflectionPattern;
import org.neurpheus.nlp.morphology.impl.MorphologicalAnalyserImpl;
import org.neurpheus.nlp.morphology.impl.ExtendedMorphologicalAnalysisResult;

/**
 * Represents a single training example for a neural network which selects inflection patterns.
 *
 * @author Jakub Strychowski
 */
public class InflectionTrainingExample implements Serializable, TrainingExample, TrainingExampleFactory {
    
    
    /** The logger for this class. */
    private static Logger logger = Logger.getLogger(InflectionTrainingExample.class);
    
    /** Unique serialization identifier of this class. */
    static final long serialVersionUID = 770608061101180213L;
    
    private int[] activatedNeuronIds;
    private double[] activationWeights;
    private int resultNeuronId;
    private int categoriesCount;
    
    /** Creates a new instance of TrainingExample */
    public InflectionTrainingExample() {
    }
    
    /** Creates a new instance of TrainingExample */
    public InflectionTrainingExample(int[] indexes, double[] weights, 
            int resultIndex, int numberOfCategories) {
        activatedNeuronIds = indexes;
        activationWeights = weights;
        resultNeuronId = resultIndex;
        categoriesCount = numberOfCategories;
    }
    
    public static InflectionTrainingExample createTrainingExample(
            final String wordForm, final List analysisResult, 
            final ExtendedInflectionPattern correctResult, final int maxIPId) {
        
        // if the list contains only one element training is unnecessary.
        if (analysisResult.size() == 1) {
            return null;
        }
        // if there is a perfect matching training is unnecessary.
        if (((ExtendedMorphologicalAnalysisResult) analysisResult.get(0)).getAccuracy() == MorphologicalAnalyserImpl.PERFECT_MATCHING_WEIGHT) {
            return null;
        }
        
        int count = 0;
        boolean extraNeuronActivated = false;
        for (final Iterator it = analysisResult.iterator(); it.hasNext();) {
            ExtendedInflectionPattern ip = ((ExtendedMorphologicalAnalysisResult) it.next()).getInflectionPattern();
            int id = ip.getId();
            if (id > maxIPId) {
                extraNeuronActivated = true;
            } else {
                count++;
            }
//            ExtendedInflectionPattern[] ipa = ((ExtendedMorphologicalAnalysisResult) it.next()).getIpa();
//            for (int i = 0; i < ipa.length; i++) {
//                int id = ipa[i].getId();
//                if (id > maxIPId) {
//                    extraNeuronActivated = true;
//                } else {
//                    count++;
//                }
//            }
        }
        if (extraNeuronActivated) {
            count++;
        }
        
        int[] charInputs = new int[32];
        Arrays.fill(charInputs, 0);
        
        for (int i = wordForm.length() - 1; i >= 0; i--) {
            int c = (wordForm.charAt(i)) & 0x1F;
            if (charInputs[c] == 0) {
                count++;
            }
            charInputs[c]++;
        }
        
        
        int[] ipIndexes = new int[count];
        double[] weights = new double[count];
        double extraNeuronWeight = 0;
        int index = 0;
        for (final Iterator it = analysisResult.iterator(); it.hasNext();) {
            ExtendedMorphologicalAnalysisResult res = (ExtendedMorphologicalAnalysisResult) it.next();
            ExtendedInflectionPattern ip = res.getInflectionPattern();
            int id = ip.getId();
            if (id > maxIPId) {
                extraNeuronWeight += res.getAccuracy();
            } else {
                ipIndexes[index] = id;
                weights[index] = res.getAccuracy();
                index++;
            }
            
//            ExtendedInflectionPattern[] ipa = res.getIpa();
//            for (int i = 0; i < ipa.length; i++) {
//                int id = ipa[i].getId();
//                if (id > maxIPId) {
//                    extraNeuronWeight += res.getAccuracy();
//                } else {
//                    ipIndexes[index] = id;
//                    weights[index] = res.getAccuracy();
//                    index++;
//                }
//            }
        }
        if (extraNeuronActivated) {
            ipIndexes[index] = maxIPId + 1;
            weights[index] = extraNeuronWeight;
            index++;
        }
        
        for (int i = 0; i < charInputs.length; i++) {
            if (charInputs[i] > 0) {
                ipIndexes[index] = maxIPId + 2 + i;
                weights[index] = charInputs[i] / 5.0;
                index++;
            }
        }
        
        
        int resNeuronId = correctResult.getId();
        if (resNeuronId > maxIPId) {
            resNeuronId = maxIPId + 1;
        } 
        
        return new InflectionTrainingExample(ipIndexes, weights, resNeuronId, maxIPId + 2);
    }
    
    public void activate(double[] inputs) {
        for (int i = activatedNeuronIds.length - 1; i>= 0; i--) {
            inputs[activatedNeuronIds[i]] = activationWeights[i] ;
        }
        //outputs[resultNeuronId] = NeuralNetworkLayerImplOld.maxValue * 0.9;
    }
    
    public boolean isCorrect(int selectedNeuronId) {
        return resultNeuronId == selectedNeuronId;
    }
    
    /**
     * Activates the given classifier with input arguments represented by this training example.
     *
     * @param classifier    The classifier which perform classification using input arguments represented
     *                      by this training example.
     *
     * @throws TrainingSetException if this training example does not support the given classifier.
     */
    public void activate(Classifier classifier) throws TrainingSetException {
        if (classifier instanceof NeuralNetwork) {
            NeuralNetwork ann = (NeuralNetwork) classifier;
            activate(ann.getInputs());
        } else {
            throw new TrainingSetException("The given classifier is not supported by this training example");
        }
    }
    
    /**
     * Checks if the given classification result is a correct result for this training example.
     *
     * @param result    The result of a classification.
     *
     * @return <code>true</code> if the given classification result is correct.
     *
     * @throws TrainingSetException if this training example does not support the type of the given result.
     */
    public boolean isCorrect(final Object result) throws TrainingSetException {
        if (result instanceof List) {
            List list = (List) result;
            if (list.size() == 0) {
                return false;
            }
            ClassificationResult res = (ClassificationResult) list.get(0);
            Object id = res.getId();
            if (id instanceof Integer) {
                return resultNeuronId == ((Integer) id).intValue();
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    
    /** Holds the buffer size for the {@link #getData()} method. */
    private static int maxDataSize = 32;
    
    /**
     * Returns the data stored in this raining example in the form of array of bytes.
     *
     * @return serialized training example data.
     */
    public byte[] getData() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(maxDataSize);
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeInt(resultNeuronId);
            dos.writeInt(categoriesCount);
            int count = activatedNeuronIds.length;
            dos.writeInt(count);
            for (int i = 0; i < count; i++) {
                dos.writeInt(activatedNeuronIds[i]);
            }
            count = activationWeights.length;
            dos.writeInt(count);
            for (int i = 0; i < count; i++) {
                dos.writeDouble(activationWeights[i]);
            }
            dos.flush();
            byte[] result = baos.toByteArray();
            if (result.length > maxDataSize) {
                maxDataSize = result.length;
            }
            return result;
        } catch (IOException e) {
            logger.error("Cannot serialize training example into array of bytes.", e);
            return null;
        }
    }
    
    /**
     * Creates an instance of training example from the given data.
     *
     * @param data A set of data of an training example.
     *
     * @return created training example
     *
     * @throws TrainingSetException if training example cannot be created.
     */
    public TrainingExample createFromData(final byte[] data) throws TrainingSetException {
        try {
            DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
            resultNeuronId = dis.readInt();
            categoriesCount = dis.readInt();
            int count = dis.readInt();
            activatedNeuronIds = new int[count];
            for (int i = 0; i < count; i++) {
                activatedNeuronIds[i] = dis.readInt();
            }
            count = dis.readInt();
            activationWeights = new double[count];
            for (int i = 0; i < count; i++) {
                activationWeights[i] = dis.readDouble();
            }
            dis.close();
        } catch (IOException e) {
            throw new TrainingSetException(e);
        }
        InflectionTrainingExample result = new InflectionTrainingExample(
                activatedNeuronIds, activationWeights, resultNeuronId, categoriesCount);
        return result;
    }
    
    
    /**
     * Returns a tuple describing an object represented by this training example.
     *
     * The tuple is an array of values of attributes which correspondence to indexes in the array.
     *
     * @return the array of values of object's attributes.
     */
    public Object[] getTuple() {
        Object[] res = new Object[categoriesCount + 32];
        Arrays.fill(res, new Double(0));
        for (int i = activatedNeuronIds.length - 1; i >= 0; i--) {
            res[activatedNeuronIds[i]] = new Double(activationWeights[i]);
        }
        return res;
    }
    
    /**
     * Returns an object representing a category of this training example.
     *
     * @return the category of the object represented by this training example.
     */
    public Object getCategory() {
        return new Integer(resultNeuronId);
    }
    
}
