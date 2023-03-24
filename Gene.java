package sgas;

class Gene implements Cloneable {
	private String key;
	private double value;
	private double upper, lower;
   
    // Constructor
	public Gene(String key) { 
        this.key = key;
        this.value = 0.0;
        this.upper = 1.0;
        this.lower = 0.0;
    }
    public Gene(String key, double val, double upperBound, double lowerBound) {
        this.key = key;
        this.value = val;
        this.upper = upperBound;
        this.lower = lowerBound;
    }

    // Setters and getters
    public void setValue(double val) { this.value = val; }

    public double getValue() { return value; }
    public double getUpperBound() { return upper; }
    public double getLowerBound() { return lower; }

    // Other methods
    @Override
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

