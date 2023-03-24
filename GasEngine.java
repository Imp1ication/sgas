package sgas;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.google.common.io.Files;

public class GasEngine {
    private Population population;

    private Evaluator evaluator;
    private Selector selector;
    private Crossover crossover;
    private Mutator mutator;

    private String logFileName = "bot/gasLog.txt";

    public void generatePopulation(String sampleGene, int popSize, PopulationCreator pc, Evaluator eva) {
        evaluator = eva;

        population = new Population(popSize);
        population.initPopulation(sampleGene, pc, evaluator);

        writeLog(logFileName, false);
    }
    
    public int getGeneration() { return population.getGeneration(); }

/* Evolve */
    public void setSelector(Selector select) { selector = select; }
    public void setCrossover(Crossover cross) { crossover = cross; }
    public void setMutator(Mutator mutate) { mutator = mutate; }
    public void setupEvolve(Selector select, Crossover cross, Mutator mutate) {
        setSelector(select);
        setCrossover(cross);
        setMutator(mutate);
    }

    public void evolve() {
        population.select(selector);
        population.crossover(crossover);
        population.mutate(mutator);
        population.evaluate(evaluator);

        writeLog(logFileName, true);
    }

    public void evolve(int times) {
        for(int i=0; i<times; ++i) {
        	System.out.printf("\nEvolve: %d/%d\n", i+1, times);
        	evolve();
        }
    }

/* Tool func */
    public void printPopulation() {
        System.out.println(population.toString());
    }
    
    public void printTest() {
    	int maxInd = 0;
    	double maxFit = 0;
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

    public static String compileJava(String botFile) throws IOException, InterruptedException {

        File outFolder = Files.createTempDir();
        
        System.out.println("Compiling Boss.java... " + botFile);
        Process compileProcess = Runtime.getRuntime().exec(
            new String[] {
                "bash", "-c", "javac " + botFile + " -d " + outFolder.getAbsolutePath()
            }
        );
        compileProcess.waitFor();
        return "java -cp " + outFolder + " Player";
    }
    
	public static String compileCpp(String botFile, String playerName) throws IOException, InterruptedException {
	    System.out.println("Compiling Cpp " + botFile);
	    Process compileProcess = Runtime.getRuntime().exec(
	        "g++ " + botFile + " -o " + playerName
	    );
	    compileProcess.waitFor();
	    System.out.println("./"+playerName);
	    return "./" + playerName;
	}
    
}
