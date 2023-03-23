package sgas;

import java.io.IOException;

public class Main {
	public static void main(String[] args) throws IOException {
	}

    void RunGas() {
        GasEngine gas = new GasEngine();

        /* Generate population */
        String sampleGene = "bot/jerrySG.txt";
        int populationSize = 20;
        PopulationCreator rpc = new RandomPopCreator();

        BossWinRateEvaluator bwe = new BossWinRateEvaluator();
        try {
            bwe.addBoss(GasEngine.compileJava("config/Boss.java"));
            bwe.addPlayer(GasEngine.compileCpp("bot/jerry.cpp", "PlayerJ"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        bwe.setBuffer("bot/chromBuffer.txt");
        bwe.setFightNum(1);

        gas.generatePopulation(sampleGene, populationSize, rpc, bwe);

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
