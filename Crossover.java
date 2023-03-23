package sgas;

import java.util.ArrayList;

public interface Crossover {
	public void cross(Individual[] indivs, int size);
}

class SinglePointCrossover implements Crossover {
    private double crossRate;

    public SinglePointCrossover(double crossoverRate) { crossRate = crossoverRate; }
    
    public void cross(Individual indivs[], int size) {
        for(int i=2; i<(size/2*2); i+=2){
        	int pI = i+1;

        // pair 
            int rndI = (int)(Math.random() * (size - i) + i);
            if(rndI != i) {
                Individual temp = indivs[i];
                indivs[i] = indivs[rndI];
                indivs[rndI] = temp;
            }

            rndI = (int)(Math.random() * (size - pI) + pI);
            if(rndI != pI) {
                Individual temp = indivs[pI];
                indivs[pI] = indivs[rndI];
                indivs[rndI] = temp;
            }

        // cross
            double rndRate = Math.random();

            if(rndRate < crossRate) {
            // rand cross point  
                int chromSize = indivs[0].getChromSize();
                rndI = (int)(Math.random() * (chromSize - 1) + 1);

            // get chrom and set
                ArrayList<Gene> chrom1, chrom2;
                chrom1 = indivs[i].getChrom(rndI, chromSize-1);
                chrom2 = indivs[pI].getChrom(rndI, chromSize-1);

                indivs[i].setChrom(rndI, chromSize-1, chrom2);
                indivs[pI].setChrom(rndI, chromSize-1, chrom1);
            }
        }
 
    }
}
