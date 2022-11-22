package sgas;

public class Chromosome {
    private float value;
    private float upperBound, lowerBound;

    public Chromosome(float val, float upper, float lower){
        this.value = val;
        this.upperBound = upper;
        this.lowerBound = lower;
    }
}
