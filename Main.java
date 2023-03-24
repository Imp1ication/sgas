package sgas;

import java.io.IOException;

public class Main {
	public static void main(String[] args) throws IOException {
	}

    void RunGas() {
        GasEngine gas = new GasEngine();

        /* Generate population */
        int popSize = 10;
        String sampleGene = "bot/jerrySG.txt";

        int fightNum = 20;
        String boss = "config/Boss.java", player = "bot/jerry.cpp";

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

        gas.generatePopulation(sampleGene, popSize, pc, eva);

        /* Evolve setup */
        double crossRate = 0.6;
        double mutateRate = 0.05;

        Selector tms = new TornamentSelector();
        //Selector rws = new RouletteWheelSelector();
        Crossover spc = new SinglePointCrossover(crossRate);
        Mutator srm = new SimpleRandomMutator(mutateRate);

        gas.setupEvolve(tms, spc, srm);

        /* Evolve */
        gas.printPopulation();
        gas.printTest();
        gas.evolve(20);
        gas.printPopulation();
        gas.printTest();
    }
}
