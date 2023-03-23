package sgas;

class Gene implements Cloneable {
	private String key;
	private double value;
	private double upper, lower;
   
	public Gene(String keyString) { this.key = keyString; }

    public void setGene(double val) { this.value = val; }
    public void setGene(double val, double upperBound, double lowerBound) {
        this.value = val;
        this.upper = upperBound;
        this.lower = lowerBound;
    }

    public double getValue() { return value; }
    public double getUpperBound() { return upper; }
    public double getLowerBound() { return lower; }

    public String toString() {
        String s = key + "," 
            + Double.toString(value) +  ","
            + Double.toString(upper) +  ","
            + Double.toString(lower);
        return s;
    }

    @Override
    protected Gene clone() throws CloneNotSupportedException {
        return (Gene)super.clone();
    }
}

