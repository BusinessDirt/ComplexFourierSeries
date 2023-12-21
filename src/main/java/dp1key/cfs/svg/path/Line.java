package dp1key.cfs.svg.path;

import dp1key.cfs.svg.ComplexNumber;

public class Line extends Linear {

    public Line(ComplexNumber start, ComplexNumber end) {
        super(start, end);
    }

    @Override
    public String toString() {
        return String.format("Line(start=%s, end=%s)", this.getStart(), this.getEnd());
    }
}
