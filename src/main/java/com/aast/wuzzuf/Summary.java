
package com.aast.wuzzuf;

public class Summary {
    //column: String, count: long, min: double, avg: double, max: double
    private String column;
    private long count;
    private double min;
    private double avg;
    private double max;

    public Summary(String column, long count, double min, double avg, double max) {
        this.column = column;
        this.count = count;
        this.min = min;
        this.avg = avg;
        this.max = max;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getAvg() {
        return avg;
    }

    public void setAvg(double avg) {
        this.avg = avg;
    }
    
}
