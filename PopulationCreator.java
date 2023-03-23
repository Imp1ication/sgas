package sgas;

public interface PopulationCreator {
	public void createPopulation(String dataString, Individual[] indivs, int size);
}

class RandomPopCreator implements PopulationCreator {
	public void createPopulation(String dataString, Individual[] indivs, int size) {
        for(int i=0; i<size; ++i) {
            indivs[i] = new RandomIndividual(dataString); 
            indivs[i].generateIndividual();
        }
    }
}
