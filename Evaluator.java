package sgas;

import java.io.FileWriter;
import java.io.IOException;

import com.codingame.gameengine.runner.MultiplayerGameRunner;
import com.codingame.gameengine.runner.simulate.GameResult;

public interface Evaluator {
    public double evaluate(Individual indiv);
    // public void evaluate(Individual indivs);
    // public void evaluate(Individual[] indivs, int size);
}

class BossWinScoreEvaluator implements Evaluator {
    private int FIGHT_NUM = 10;
    private long SEED = 53295539L;
    private boolean isRandomSeed = false;
    private String agentBoss;
    private String agentPlayer;
    private String chromBuffer = "bot/chromBuffer.txt";

    // Constructor
    public BossWinScoreEvaluator(String bossAgent, String playerAgent, int fightNum, boolean isRandomSeed) {
        this.agentBoss = bossAgent;
        this.agentPlayer = playerAgent;
        this.FIGHT_NUM = fightNum;
        this.isRandomSeed = isRandomSeed;
    }

    // Setters and Getters
    public void setChromBuffer(String bufferFilePath) {
        chromBuffer = bufferFilePath;
    }

    // Evaluate
    public double evaluate(Individual indiv) {
        // write gene file
        try {
            FileWriter fw = new FileWriter(chromBuffer);
            fw.write(indiv.toString());
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // run game
        double score = 0;

        for (int i = 0; i < FIGHT_NUM; ++i) {
            System.out.println("  Fight " + (i + 1) + "/" + FIGHT_NUM);

            MultiplayerGameRunner gameRunner = new MultiplayerGameRunner();
            gameRunner.setLeagueLevel(3);

            if (isRandomSeed) {
                gameRunner.setSeed((long) (Math.random() * 1000000));
            } else {
                gameRunner.setSeed(SEED);
            }

            gameRunner.addAgent(agentPlayer, "Player");
            gameRunner.addAgent(agentBoss, "Boss");

            GameResult result = gameRunner.simulate();

            // parse result
            int turn = (result.views.size() + 1) / 2;
            int winner, losser;
            double winScoreCal = 0;

            if (result.scores.get(0) > result.scores.get(1)) {
                winner = 0;
                losser = 1;
            } else {
                winner = 1;
                losser = 0;
            }

            switch (result.scores.get(winner)) {
                case 1:
                    // Complete win: win turns + 220
                    winScoreCal = (220 - turn) + 220;
                    break;
                case 3:
                case 2:
                    // Health win: win health + 220
                    winScoreCal = result.scores.get(winner) - result.scores.get(losser) + 220;
                    break;
                default:
                    // Mana win: win mana
                    winScoreCal = result.scores.get(winner) - result.scores.get(losser);
                    break;
            }

            winScoreCal /= 440;
            winScoreCal = (winner == 0) ? winScoreCal : winScoreCal * -1;

            score += winScoreCal;
        }

        // Set fitness(win score)
        double fitness = score / FIGHT_NUM;
        fitness = (fitness + indiv.getFitness()) / 2;
        fitness = Math.round(fitness * 1000.0) / 1000.0;

        return fitness;
    }

    // public void evaluate(Individual[] indivs, int size) {
    //     /* Calculate fitness(win rate): fight with boss n times */
    //     System.out.println("Progress:");

    //     for (int i = 0; i < size; ++i) {
    //         // write gene file
    //         try {
    //             FileWriter fw = new FileWriter(chromBuffer);
    //             fw.write(indivs[i].toString());
    //             fw.flush();
    //             fw.close();
    //         } catch (IOException e) {
    //             e.printStackTrace();
    //         }

    //         // run game
    //         double score = 0;
    //         for (int j = 0; j < FIGHT_NUM; ++j) {
    //             // setup game runner
    //             MultiplayerGameRunner gameRunner = new MultiplayerGameRunner();
    //             gameRunner.setLeagueLevel(3);
    //             gameRunner.setSeed(53295539L);

    //             gameRunner.addAgent(agentPlayer, "Player");
    //             gameRunner.addAgent(agentBoss, "Boss");
    //             // parse result
    //             GameResult gameResult = gameRunner.simulate();

    //             int turn = (gameResult.views.size() + 1) / 2;
    //             int winner, losser;
    //             if (gameResult.scores.get(0) > gameResult.scores.get(1)) {
    //                 winner = 0;
    //                 losser = 1;
    //             } else {
    //                 winner = 1;
    //                 losser = 0;
    //             }

    //             double winScoreCal = 0;
    //             switch (gameResult.scores.get(winner)) {
    //                 case 1:
    //                     // Complete win: win turns + 220
    //                     winScoreCal = (220 - turn) + 220;
    //                     break;
    //                 case 3:
    //                 case 2:
    //                     // Health win: win health + 220
    //                     winScoreCal = gameResult.scores.get(winner) - gameResult.scores.get(losser) + 220;
    //                     break;
    //                 default:
    //                     // Mana win: win mana
    //                     winScoreCal = gameResult.scores.get(winner) - gameResult.scores.get(losser);
    //                     break;
    //             }

    //             // System.out.println("GameResult: " + gameResult.scores);
    //             // System.out.print("WinScore: " + winScoreCal + ", ");

    //             winScoreCal /= 440;
    //             winScoreCal = (winner == 0) ? winScoreCal : winScoreCal * -1;

    //             // System.out.println(winScoreCal + "\n");

    //             score += winScoreCal;

    //             // print %
    //             int games = i * FIGHT_NUM + j + 1;
    //             String msg = (Math.round(winScoreCal * 1000000.0) / 1000000.0) + "\t"
    //                     + gameResult.gameParameters.get(0);

    //             writeLog(msg);
    //             System.out.printf("%d/%d\t%f\t%s", games, (FIGHT_NUM * size), winScoreCal,
    //                     gameResult.gameParameters.get(0));
    //         }

    //         // Set fitness(win rate)
    //         double fitness = score / FIGHT_NUM;
    //         fitness = Math.round(fitness * 1000.0) / 1000.0;
    //         fitness = (fitness + indivs[i].getFitness()) / 2;
    //         indivs[i].setFitness(fitness);

    //         // System.out.printf("size: %d/%d, fitness: %f\n", i, size, fitness);
    //         // System.out.println("fitness: " + fitness);
    //     }
    // }

    // private String logFileName = "bot/evolveLog.txt";

    // private void writeLog(String msg) {
    //     File file = new File(logFileName);

    //     try {
    //         if (!file.exists()) {
    //             file.createNewFile();
    //         }

    //         FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
    //         BufferedWriter bw = new BufferedWriter(fw);

    //         bw.write(msg);

    //         bw.close();
    //         fw.close();
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    // }

}

// class TestEvaluator implements Evaluator {
// private double tarVal = 0.01;

// public void evaluate(Individual[] indivs, int size) {
// for (int i = 0; i < size; ++i) {
// double fitness = 0;

// for (int j = 0; j < indivs[i].getChromSize(); ++j) {
// fitness += 1 - Math.abs(tarVal - indivs[i].getGene(j).getValue());
// }
// indivs[i].setFitness(fitness);
// }
// }
// }
