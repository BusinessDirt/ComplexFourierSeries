package businessdirt.svgHandler.svg.path;

import businessdirt.svgHandler.svg.ComplexNumber;

public abstract class SVGElement{

    protected ComplexNumber start, end;
    protected final int MIN_DEPTH = 5;
    protected final double ERROR = 1e-12;

    public SVGElement(ComplexNumber start, ComplexNumber end) {
        this.start = start;
        this.end = end;
    }

    public void reverse() {
        ComplexNumber _start = this.start.clone();
        this.start.set(this.end.clone());
        this.end.set( _start);
    }

    public abstract ComplexNumber point(double pos);

    public abstract double length();

    public ComplexNumber getStart() {
        return start;
    }

    public ComplexNumber getEnd() {
        return end;
    }
}
