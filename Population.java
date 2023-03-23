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

/* inti Population */
    public void initPopulation(String filePath) {  // default creator
    	PopulationCreator rpc = new RandomPopCreator();
        Evaluator eva = new BossWinRateEvaluator();

    	initPopulation(filePath, rpc, eva);
    }

    public void initPopulation(String filePath, PopulationCreator creator, Evaluator eva) {
    	try { 
    		Path path = Path.of(filePath);
    		String data = Files.readString(path);
    		
            creator.createPopulation(data, indivs, size);
    	} catch (Exception e) {
			e.printStackTrace();
		}

        evaluate(eva);
    }

/* get, set */
    public int getSize() { return size; }
    public Individual getIndividual(int index) {
    	if(index >= size) return null;
    	return indivs[index];
    }
    public void setIndividual(int index, Individual indiv) {
    	if(index >= size) return;
    	indivs[index] = indiv;
    }
    public int getGeneration() { return generation; }

/* Evolve func */

    public void evaluate(Evaluator eva) {
        eva.evaluate(indivs, size);
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

/* Tool func */


/* Other func */
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
