package dp1key.cfs.svg.path;

import dp1key.cfs.svg.ComplexNumber;

public class Move extends SVGElement {

    public Move(ComplexNumber to) {
        super(to, to);
    }

    @Override
    public String toString() {
        return String.format("Move(to=%s)", this.getStart());
    }

    @Override
    public ComplexNumber point(double pos) {
        return this.getStart();
    }

    @Override
    public double length() {
        return 0;
    }
}
