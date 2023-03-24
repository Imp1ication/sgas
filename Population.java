package sgas;

import java.nio.file.Files;
import java.nio.file.Path;

public class Population {
    private int generation;
    private int size;
    private Individual[] indivs;

    public Population(int size) {
        this.generation = 0;
        this.size = size;
        
        this.indivs = new Individual[size];
    }

    // Initialize population
    public void initPopulation(String sampleChrom, PopulationCreator creator, Evaluator eva) {
    	try { 
    		Path path = Path.of(sampleChrom);
    		String data = Files.readString(path);
    		
            creator.createPopulation(data, indivs, size);
    	} catch (Exception e) {
			e.printStackTrace();
		}

        evaluate(eva);
    }

    // Setters and getters
    public int getGeneration() { return generation; }
    public int getSize() { return size; }

    public Individual getIndividual(int index) {
    	if(index < 0 || index >= size) return null;

        Individual indiv = null;
        try {
            indiv = indivs[index].clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

    	return indiv;
    }
    public void setIndividual(int index, Individual indiv) {
    	if(index < 0 || index >= size) return;

        try {
            indivs[index] = indiv.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }
    	

    // Evolve methods
    public void evaluate(Evaluator eva) {
        System.out.println("Evaluate generation " + generation);
        for(int i=0; i<size; ++i) {
            indivs[i].setFitness(eva.evaluate(indivs[i]));
            System.out.printf("Individual %d/%d\t%f\n", i, size, indivs[i].getFitness());
        }
        generation += 1;
    }
    
    public void select(Selector selector) {
        selector.select(indivs, size);
    }
    
    public void crossover(Crossover crs) {
    	crs.cross(indivs, size);
    }

    public void mutate(Mutator mutator) {
        mutator.mutate(indivs, size);
    }

    // Other methods
    @Override
    public String toString(){
        String msg = "";
        msg += "Generation: " + String.valueOf(generation) + "\n";
        msg += "Size: " + String.valueOf(size) + "\n";
        for(int i=0; i<size; ++i) {
            msg += "-----\n" + indivs[i].toString() + "-----\n";
        }
        return msg;
    }
}
