package sgas;

import java.util.ArrayList;

public interface PopulationCreator {
    public void createPopulation(String dataString, Individual[] indivs, int size);
}

class RandomPopCreator implements PopulationCreator {
    public void createPopulation(String dataString, Individual[] indivs, int size) {
        for (int i = 0; i < size; ++i) {
            // create chromosome
            String[] lines = dataString.split("\n");
            ArrayList<Gene> chromosome = new ArrayList<Gene>();

            for (String line : lines) {
                String[] values = line.split("\\s*,\\s*");

                // parse data value
                if (values[0].equals("#")) continue;
                String key = values[0]; 
                double value = Double.parseDouble(values[1]);
                double upper = Double.parseDouble(values[2]);
                double lower = Double.parseDouble(values[3]);

                // create gene by random
                value =  (double) (Math.random() * (upper - lower)) + lower;
                value = Math.round(value * 100.0) / 100.0;

                Gene gene = new Gene(key, value, upper, lower);

                chromosome.add(gene);
            }

            // create Individual
            indivs[i] = new Individual(chromosome);
        }
    }
}
