package sgas;

public interface Selector {
	public void select(Individual[] indivs, int size);
}

class RouletteWheelSelector implements Selector {
    public void select(Individual[] indivs, int size) {
    /* Calculate relative fitness */
        double totalFitness = 0;
        double[] relativeFitness = new double[size];
        
        for(int i=0; i<size; ++i) totalFitness += indivs[i].getFitness();
        for(int i=0; i<size; ++i) relativeFitness[i] = indivs[i].getFitness() / totalFitness;

    /* Create Roulette */
        double[] wheel = new double[size];
        
        wheel[0] = relativeFitness[0];
        wheel[size-1] = 1;
        for(int i=1; i<size-1; ++i) wheel[i] = relativeFitness[i] + wheel[i-1];

    /* Select */
        Individual[] newIndivs = new Individual[size];

        for(int i=0; i<size; ++i) {
            double dart = Math.random();
            
            for(int j=0; j<size; ++j) {
                if( dart < wheel[j] ) {
                    try {
						newIndivs[i] = indivs[j].clone();
					} catch (CloneNotSupportedException e) {
						e.printStackTrace();
					}
                    break;
                }
            }
        }
        
        for(int i=0; i<size; ++i) indivs[i] = newIndivs[i];
    }
}

class TornamentSelector implements Selector {
	public void select(Individual[] indivs, int size){
        Individual[] newIndivs = new Individual[size];
        
        int max = 0;
        for(int i=0; i<size; ++i) {
        	if(indivs[i].getFitness() > indivs[max].getFitness())
        		max = i;
        }
        
        try {
			newIndivs[0] = indivs[max].clone();
		} catch (CloneNotSupportedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        for(int i=1; i<size; ++i){
            int rd1 = (int)(Math.random() * (size-1));
            int rd2 = (int)(Math.random() * (size-1));

            try {
				newIndivs[i] = 
				    indivs[rd1].getFitness() > indivs[rd2].getFitness() ? indivs[rd1].clone() : indivs[rd2].clone();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
        }

        for(int i=0; i<size; ++i) indivs[i] = newIndivs[i];
        
    } // select end
}

