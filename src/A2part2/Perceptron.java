package A2part2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;

public class Perceptron {
	private double learningRate = 0.5;
	private List <Double> weights;
	public void run() {
		//Includes all features for each instance. The last value in each list
		//is the actual class.
		List <List <String>> instances = loadData("ionosphere.data");
		//Code for splitting data. Uncomment if needed.
//		List <List <String>> training = instances.subList(0, instances.size()/2);
//		List <List <String>> testing = instances.subList(instances.size()/2, instances.size());
//		startLearning(training);
		startLearning(instances);
		System.out.println("Final weights:" + this.weights);
		double correct = 0;
		for (List <String> instance : instances) {
			int prediction = predict(instance, this.weights);
			int actualClass = instance.get(instance.size()-1).equals("g")? 1 : 0;
			if (prediction == actualClass) {
				correct++;
			}
		}
		//Uncomment for getting accuracy after spliting data.
//		double correct = 0;
//		for (List <String> instance : testing) {
//			int prediction = predict(instance, this.weights);
//			int actualClass = instance.get(instance.size()-1).equals("g")? 1 : 0;
//			if (prediction == actualClass) {
//				correct++;
//			}
//		}
		//System.out.println("Final Classification Accuracy:" + (correct/testing.size())*100);
		System.out.println("Final Classification Accuracy:" + (correct/instances.size())*100);
	}
	
	public void startLearning(List <List <String>> instances) {
		this.weights = getWeights(instances);
		double correctPredictions = 0;
		int totalIterations = 0;
		while (correctPredictions < instances.size() && totalIterations < 100) {
			correctPredictions = 0;
			List <Double> previousWeights = new ArrayList <Double>();
			previousWeights.addAll(this.weights);
			for (int a = 0; a < instances.size(); a++) {
				int prediction = predict(instances.get(a), this.weights);
				//if perceptron is correct do nothing
				int actualClass = instances.get(a).get(instances.get(a).size()-1).equals("g")? 1 : 0;
				if (prediction != actualClass) {
					learnNewWeight(instances.get(a), this.weights, prediction, actualClass);
				}
				else {
					correctPredictions++;

				}
			}
			totalIterations++;
		}
		//All instances classfied correctly
		if (correctPredictions == instances.size()) {
			System.out.println("Total of " + totalIterations + " for convergence.");
		}
		else {
			System.out.println("Failed to converge. There are still " + ((int)(instances.size()-correctPredictions)) + " instances classified wrongly.");
		}
		
		
		
	}
	public void learnNewWeight(List <String> instances, List <Double> weights, int prediction, int actualClass) {
		weights.set(0, weights.get(0) + learningRate*(actualClass-prediction)*1);
		for (int i = 0; i < instances.size()-1; i++) {
			double feature = Double.valueOf(instances.get(i));
			weights.set(i+1, weights.get(i+1) + learningRate*(actualClass-prediction)*feature);
		}
	}
	public Integer predict(List <String> instances, List <Double> weights) {
		double sum = 0.0;
		// “dummy” feature that is always 1 (bias)
		sum = sum + (weights.get(0)*1);
		for (int i = 0; i < instances.size()-1; i++) {
			double feature = Double.valueOf(instances.get(i));
			sum = sum + (feature*weights.get(i+1));
		}

		if (sum > 0) {
			return 1;
		}
		return 0;
	}
	public List <Double> getWeights(List <List <String>> instances){
		List <Double> weights = new ArrayList <Double>();
		//add the weight for threshold first.
		weights.add(0.0);
		//randomize the weights
		for (int i = 0; i < instances.get(0).size()-1; i++) {
//			https://stackoverflow.com/questions/3680637/generate-a-random-double-in-a-range
			double random = new Random().nextDouble();
			double result = 0 + (random * (1));
			weights.add(result);
		}
		
		return weights;
	}
	public List<List<String>> loadData(String dataFileName) {
		List <List<String>> instances = new ArrayList <List <String>>();
		Map <List <Double>, String> featuresAndClass = new LinkedHashMap <List <Double>, String>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(dataFileName));
			String instance = reader.readLine();
			Map <Integer, List <Double>> test = new HashMap <Integer, List <Double>>();
			
			int a = 1;
			while (instance!=null) {
				List <String> featuresAndLabel = new ArrayList <String>();
				String [] rows = instance.split(",");
				List <Double> features = new ArrayList <Double>();
				for (int i = 0; i < rows.length-1; i++) {
					features.add(Double.parseDouble(rows[i]));
					featuresAndLabel.add(rows[i]);
					
				}
				featuresAndLabel.add(rows[rows.length-1]);
				instances.add(featuresAndLabel);
				featuresAndClass.put(features, rows[rows.length-1]);
			
				test.put(a, features);
				a++;
				instance = reader.readLine();
				
			}
		

			
		}catch (Exception e) {
			e.printStackTrace();
		}
	
		return instances;
	}
	public static void main (String [] args) {
		Perceptron p = new Perceptron();
		p.run();
	}
}
