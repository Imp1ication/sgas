package sgas;

public interface Mutator {
	public void mutate(Individual[] indivs, int size);
}

class SimpleRandomMutator implements Mutator {
    private double mutateRate = 0.01;

    public SimpleRandomMutator(double mutateRate) { this.mutateRate = mutateRate; }

	public void mutate(Individual[] indivs, int size){
        for(int i=1; i<size; ++i) {
            for(int j=0; j<indivs[i].getChromSize(); ++j){
                if(Math.random() < mutateRate) {
                // get gene
                    Gene temp = null;
					try {
						temp = indivs[i].getGene(j).clone();
					} catch (CloneNotSupportedException e) {
						e.printStackTrace();
					}
                    double upper = temp.getUpperBound();
                    double lower = temp.getLowerBound();

                // mutate value 
                    double val = (double)(Math.random() * (upper - lower)) + lower;
                    val = Math.round(val * 100.0) / 100.0;
                    temp.setValue(val);
                    indivs[i].setGene(j, temp);
                }
            }
        }

    }

}
