package sgas;

import java.util.ArrayList;

class Individual implements Cloneable {
    private String data;
    private ArrayList<Gene> chromosome;
    private double fitness;

    // Constructor
    public Individual(ArrayList<Gene> chromosome) {
        this.data = "";
        this.chromosome = chromosome;
        this.fitness = 0.0;

        this.decode();
    }

    // Setters and getters
    public String getData() { return data; }
    public int getChromSize() { return chromosome.size(); }

    public double getFitness() { return fitness; }
    public void setFitness(double fitness) { this.fitness = fitness; }

    public Gene getGene(int index) {
        if(isValidIndex(index) == false) return null;

        Gene g = null;
        try {
            g = chromosome.get(index).clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return g;
    }
    public void setGene(int index, Gene g) {
        if(isValidIndex(index) == false) return;
        
        try {
            chromosome.set(index, g.clone());
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        this.decode();
    }

    public ArrayList<Gene> getChrom(int start, int end) {
        if(isValidIndex(start) == false || isValidIndex(end) == false || start >= end) return null;

        ArrayList<Gene> chrom = new ArrayList<Gene>();
        for(int i=start; i<=end; ++i) {
            try {
                chrom.add( chromosome.get(i).clone() );
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }

        return chrom;
    }
    public void setChrom(int start, int end, ArrayList<Gene> chrom){
        if(isValidIndex(start) == false || isValidIndex(end) == false || start >= end) return;
        if(chrom.size() != end - start + 1) return;

        for(int i=start, j=0; i<=end && j<chrom.size(); ++i, ++j) {
            try {
                chromosome.set(i, chrom.get(j).clone());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
        
        this.decode();

        return;
    }

    // Decoding
    private void decode() {
        data = this.toString();
    }

    // Other methods
    private boolean isValidIndex(int index) {
        return (index >= 0 && index < getChromSize());
    }
        
    @Override
    public String toString() {
        String s = "";
        for(Gene g : chromosome) {
            s += g.toString() + "\n";
        }

        return s;
    }
        
    @Override
    public Individual clone() throws CloneNotSupportedException {
        Individual c = null;

        c = (Individual)super.clone();
        c.chromosome = new ArrayList<Gene>();
        for(Gene g : chromosome) {
            c.chromosome.add(g.clone());
        }
        
        return c;
    }
}


// abstract class Individual implements Cloneable {
//     protected String data; 
//     protected ArrayList<Gene> chromosome;
//     protected double fitness = 0;

//     protected abstract void coding();
//     protected abstract void decode();
//     public abstract void generateIndividual();
//     
// /* get, set */
//     public int getChromSize() { return chromosome.size(); } 
//     
//     public double getFitness() { return fitness; }
//     public void setFitness(double fitness) { this.fitness = fitness; }

//     public Gene getGene(int index) {
//         if(index < 0 || index >= getChromSize() ) return null;
//         return chromosome.get(index);
//     }
//     public void setGene(int index, Gene g) {
//         if(index < 0 || index >= getChromSize() ) return;
//         chromosome.set(index, g);
//     }

//     public ArrayList<Gene> getChrom(int start, int end) {
//         if(start < 0 || end >= getChromSize()) return null;

//         ArrayList<Gene> chrom = new ArrayList<Gene>();
//         for(int i=start; i<=end; ++i) {
//             chrom.add( chromosome.get(i) );
//         }

//         return chrom;
//     }
//     public void setChrom(int start, int end, ArrayList<Gene> chrom){
//         if(start < 0 || end >= getChromSize()) return;

//         for(int i=start, j=0; i<=end && j<chrom.size(); ++i, ++j) {
//             chromosome.set(i, chrom.get(j));
//         }
//         
//         this.decode();

//         return;
//     }
//     

// /* Other func */
//     public String toString() {
//         String s = "";
//         for(Gene g : chromosome) {
//             s += g.toString() + "\n";
//         }

//         return s;
//     }
//     
//     @Override
//     protected Individual clone() throws CloneNotSupportedException {
//         Individual c = null;

//         c = (Individual)super.clone();
//         c.chromosome = new ArrayList<Gene>();
//         for(Gene g : chromosome) {
//             c.chromosome.add(g.clone());
//         }
//         
//         return c;
//     }
// }

// class RandomIndividual extends Individual {
//     
//     public RandomIndividual(String dataString) {
//         this.data = dataString;
//         chromosome = new ArrayList<Gene>();
//     }

//     protected void coding() {
//         String[] lines = data.split("\n"); 

//         for(String line : lines) {
//             String[] values = line.split("\\s*,\\s*");
//        
//             if(values[0].equals("#")) continue;

//             Gene g = new Gene(
//                 values[0],
//                 Double.parseDouble(values[1]),
//                 Double.parseDouble(values[2]),
//                 Double.parseDouble(values[3])
//             );

//             chromosome.add(g);
//         }
//     } 

//     protected void decode() {
//         data = this.toString();
//     }
//     
//     public void generateIndividual() {
//         this.coding();

//         for(Gene g : chromosome) {
//             double upper = g.getUpperBound();
//             double lower = g.getLowerBound();
// 			double val = (double)(Math.random() * (upper - lower)) + lower;
// 			val = Math.round(val * 100.0) / 100.0;

//             g.setValue(val);
//         }

//         this.decode();
//     }
// }
