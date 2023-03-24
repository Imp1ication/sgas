package sgas;

import java.io.IOException;
import java.util.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class DE {
    Population population = null;
    Evaluator evaluator = null;

    double DIFF_WEIGHT, CROSS_RATE;

    String logFileName = "bot/gasLog.txt";

    public static void main(String[] args) throws IOException {
        // setup DE
        int popSize = 10;
        String sampleGene = "bot/jerrySG.txt";

        int fightNum = 20;
        String boss = "config/Boss.java", player = "bot/jerry.cpp";

        double diffWeight = 0.8;
        double crossRate = 0.4;

        DE de = new DE(diffWeight, crossRate);

        // generate population
        PopulationCreator pc = new RandomPopCreator();
        Evaluator eva = null;

        try {
            String bossAgent = GasEngine.compileJava(boss);
            String playerAgent = GasEngine.compileCpp(player, "Player");
            eva = new BossWinScoreEvaluator(bossAgent, playerAgent, fightNum, false);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        de.generatePopulation(popSize, sampleGene, pc, eva);

        System.out.println("Initial population:");

        de.printPopulation();
        de.printTest();
        de.evolve(10);
        de.printPopulation();
        de.printTest();
    }

    public void evolve(int generation) {
        for (int i = 0; i < generation; ++i) {
        	System.out.printf("\nEvolve: %d/%d\n", i+1, generation);
            evolve();
        }
    }
    private void evolve() {
        for (int i = 0; i < population.getSize(); i++) {
            System.out.printf("Evaluating .. %d/%d\n", i+1, population.getSize());

            Individual indiv = population.getIndividual(i);
            Individual mutant = mutate(i);
            Individual child = crossover(indiv, mutant);

            child.setFitness(evaluator.evaluate(child));
            if (child.getFitness() > indiv.getFitness()) {
                // System.out.println("Child is better than indiv" + i);
                population.setIndividual(i, child);
            }
            else {
                // System.out.println("Child is worse than indiv" + i);
            }
        }

        population.evaluate(evaluator);
        writeLog(logFileName, true);
    }

    Individual mutate(int parentInd) {
        // get individuals
        Random random = new Random();
        Set<Integer> parentNumSet = new HashSet<>();

        while (parentNumSet.size() < 3) {
            int num = random.nextInt(population.getSize());
            if (num != parentInd)
                parentNumSet.add(num);
        }
        Integer[] parentNums = parentNumSet.toArray(new Integer[0]);

        // sort parents by fitness
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 2 - i; ++j) {
                Individual a = population.getIndividual(parentNums[j]);
                Individual b = population.getIndividual(parentNums[j + 1]);
                if (a.getFitness() > b.getFitness()) {
                    Integer tmp = parentNums[j];
                    parentNums[j] = parentNums[j + 1];
                    parentNums[j + 1] = tmp;
                }
            }
        }

        Individual[] parents = new Individual[3];

        for (int i = 0; i < 3; ++i) {
            parents[i] = population.getIndividual(parentNums[i]);
        }

        // mutate
        Individual mutant = null;

        try {
            mutant = parents[2].clone();
        } catch (CloneNotSupportedException e) {
            System.out.println("CloneNotSupportedException");
        }

        for (int i = 0; i < mutant.getChromSize(); ++i) {
            Gene gene = mutant.getGene(i);
            double value = parents[2].getGene(i).getValue()
                    + DIFF_WEIGHT * (parents[1].getGene(i).getValue() - parents[0].getGene(i).getValue());

            value = Math.round(value * 1000) / 1000.0;

            // check bound
            if (value <= gene.getUpperBound() && value >= gene.getLowerBound()) {
                gene.setValue(value);
                // System.out.println("Mutate gene " + i + " to " + value);
            }

            mutant.setGene(i, gene);
        }

        return mutant;
    }

    Individual crossover(Individual indiv, Individual mutant) {
        Individual child = null;

        try {
            child = indiv.clone();
        } catch (CloneNotSupportedException e) {
            System.out.println("CloneNotSupportedException");
        }

        Random random = new Random();
        double crossPoint = random.nextInt(child.getChromSize());

        for(int i = 0; i < child.getChromSize(); ++i) {
            if (random.nextDouble() < CROSS_RATE || i == crossPoint) {
                child.setGene(i, mutant.getGene(i));
            }
        }

        child.setFitness(0);
        return child;
    }

    void generatePopulation(int popSize, String sampleGene, PopulationCreator pc, Evaluator eva) {
        evaluator = eva;

        population = new Population(popSize);
        population.initPopulation(sampleGene, pc, eva);

        writeLog(logFileName, false);
    }

/* Tool func */
    public void printPopulation() {
        System.out.println(population.toString());
    }
    
    public void printTest() {
    	int maxInd = 0;
    	double maxFit = -1;
    	for(int i=0; i<population.getSize(); ++i) {
    		if(population.getIndividual(i).getFitness() > maxFit) {
    			maxInd = i;
    			maxFit = population.getIndividual(i).getFitness();
    		}
    	}
    	System.out.println(population.getIndividual(maxInd));
    	System.out.println("Max Fitness = " + maxFit);
    }

    private void writeLog(String fileName, Boolean append){
        File file = new File(fileName);

        try{
            if( !file.exists() ){
                    file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile(), append);
            BufferedWriter bw = new BufferedWriter(fw);

            String msg = "";
            
            for(int i=0; i<population.getSize(); ++i){
                Individual indiv = population.getIndividual(i);
                msg += indiv.getFitness() + " ";
                for(int j=0; j<indiv.getChromSize(); ++j){
                    msg += indiv.getGene(j).getValue();
                    msg += (j != indiv.getChromSize()-1) ? " " : "\n";
                }
            }

            bw.write(msg + "\n");

            bw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DE(double diffWeight, double crossRate) {
        this.DIFF_WEIGHT = diffWeight;
        this.CROSS_RATE = crossRate;
    }
}
