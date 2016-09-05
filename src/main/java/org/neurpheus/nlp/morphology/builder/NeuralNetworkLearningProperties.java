/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.neurpheus.nlp.morphology.builder;

import org.neurpheus.machinelearning.neuralnet.xml.XmlNeuralNetwork;

/**
 *
 * @author jstrychowski
 */
public class NeuralNetworkLearningProperties {

    private int maximumNumberOfTrainingExamples;
    
    private int numberOfTestExamples;
    
    private int learningAlgorithm;
    
    private int numberOfEpochs;
    
    private double learningFactor;
    
    private double learningFactorMultipler;
    
    private double learningFactorMultiplerByNumberOfEpochs;
    
    private double momentumFactor;
    
    private double maxJumpFactor;
    
    private double weightsAmplitude;
    
    private double mimimumErrorValue;
    
    private XmlNeuralNetwork architecture;
    
    private boolean classificationMode;
    
    
    public NeuralNetworkLearningProperties() {
        
    }

    public int getMaximumNumberOfTrainingExamples() {
        return maximumNumberOfTrainingExamples;
    }

    public void setMaximumNumberOfTrainingExamples(int maximumNumberOfTrainingExamples) {
        this.maximumNumberOfTrainingExamples = maximumNumberOfTrainingExamples;
    }

    public int getLearningAlgorithm() {
        return learningAlgorithm;
    }

    public void setLearningAlgorithm(int learningAlgorithm) {
        this.learningAlgorithm = learningAlgorithm;
    }

    public int getNumberOfEpochs() {
        return numberOfEpochs;
    }

    public void setNumberOfEpochs(int numberOfEpochs) {
        this.numberOfEpochs = numberOfEpochs;
    }

    public double getLearningFactor() {
        return learningFactor;
    }

    public void setLearningFactor(double learningFactor) {
        this.learningFactor = learningFactor;
    }

    public double getLearningFactorMultipler() {
        return learningFactorMultipler;
    }

    public void setLearningFactorMultipler(double learningFactorMultipler) {
        this.learningFactorMultipler = learningFactorMultipler;
    }

    public double getLearningFactorMultiplerByNumberOfEpochs() {
        return learningFactorMultiplerByNumberOfEpochs;
    }

    public void setLearningFactorMultiplerByNumberOfEpochs(double learningFactorMultiplerByNumberOfEpochs) {
        this.learningFactorMultiplerByNumberOfEpochs = learningFactorMultiplerByNumberOfEpochs;
    }

    public double getMomentumFactor() {
        return momentumFactor;
    }

    public void setMomentumFactor(double momentumFactor) {
        this.momentumFactor = momentumFactor;
    }

    public XmlNeuralNetwork getArchitecture() {
        return architecture;
    }

    public void setArchitecture(XmlNeuralNetwork architecture) {
        this.architecture = architecture;
    }

    public double getMaxJumpFactor() {
        return maxJumpFactor;
    }

    public void setMaxJumpFactor(double maxJumpFactor) {
        this.maxJumpFactor = maxJumpFactor;
    }

    public double getWeightsAmplitude() {
        return weightsAmplitude;
    }

    public void setWeightsAmplitude(double weightsAmplitude) {
        this.weightsAmplitude = weightsAmplitude;
    }

    public double getMimimumErrorValue() {
        return mimimumErrorValue;
    }

    public void setMimimumErrorValue(double mimimumErrorValue) {
        this.mimimumErrorValue = mimimumErrorValue;
    }

    public boolean isClassificationMode() {
        return classificationMode;
    }

    public void setClassificationMode(boolean classificationMode) {
        this.classificationMode = classificationMode;
    }

    public int getNumberOfTestExamples() {
        return numberOfTestExamples;
    }

    public void setNumberOfTestExamples(int numberOfTestExamples) {
        this.numberOfTestExamples = numberOfTestExamples;
    }
    
}
