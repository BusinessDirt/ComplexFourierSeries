package dp1key.cfs.svg.path;

import dp1key.cfs.svg.ComplexNumber;

public abstract class SVGElement{

    protected ComplexNumber start, end;
    protected final int MIN_DEPTH = 5;
    protected final double ERROR = 1e-12;

    public SVGElement(ComplexNumber start, ComplexNumber end) {
        this.start = start;
        this.end = end;
    }

    public abstract ComplexNumber point(double pos);

    public abstract double length();

    public ComplexNumber getStart() {
        return start;
    }

    public void setStart(ComplexNumber start) {
        this.start = start;
    }

    public ComplexNumber getEnd() {
        return end;
    }

    public void setEnd(ComplexNumber end) {
        this.end = end;
    }
}
