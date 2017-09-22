package com;

import weka.core.Instances;
import java.io.BufferedReader;
import java.io.*;
import java.io.FileReader;
import java.util.Random;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
public class NaiveBayes_Classification_API {
	
	public static void main(String arg[]) {
	try {
		StringBuilder strb=new StringBuilder();
		/*DataSource dst=new DataSource("/home/venkat/weka-3-8-1/dataweather.nominal.arff");
		Instance dataset=Instance(dst);
		dataset.setClassValue(arg0);*/
		//BufferedReader breader =new BufferedReader(new FileReader("/home/venkat/weka-3-8-1/data/weather.nominal.arff"));
        Instances train = new Instances(new BufferedReader(new FileReader("/home/venkat/weka-3-8-1/data/weather.nominal.arff")));
        train.setClassIndex(train.numAttributes() - 1);
        //breader.close();
        NaiveBayes nB=new NaiveBayes();
        nB.buildClassifier(train);
        
        Evaluation eval=new Evaluation(train);
        eval.crossValidateModel(nB,train,10,new Random(1));
        System.out.println("run info");
        System.out.println("Scheme: " + train.getClass().getName());
        System.out.println("Relation: ");

        System.out.println("\nClassifier Model(full training set)\n===============================");
        System.out.println(nB);

        System.out.println(eval.toSummaryString("\nSummary Results\n==================", true));
        System.out.println(eval.toClassDetailsString());
        System.out.println(eval.toMatrixString());

/*
        //txtArea output 
        strb.append("\n\n\n");
        strb.append("Run Information\n===================\n");
        strb.append("Scheme: " + train.getClass().getName());

        strb.append("\n\nClassifier Model(full training set)"
                + "\n======================================\n");
        //strb.append("" + nB);

        strb.append(eval.toSummaryString("\n\nSummary Results\n==================\n", true));
        strb.append(eval.toClassDetailsString());
        strb.append(eval.toMatrixString());
        strb.append("\n\n\n");

        System.out.println(strb.toString());*/

    } catch (FileNotFoundException ex) {
        System.err.println("File not found");
        System.exit(1);
    } catch (IOException ex) {
        System.err.println("Invalid input or output.");
        System.exit(1);
    } catch (Exception ex) {
        System.err.println("Exception occured!");
        System.exit(1);
	}
}
}
