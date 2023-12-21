package dp1key.cfs.svg.path;

import dp1key.cfs.svg.ComplexNumber;

import java.util.Objects;

public class CubicBezier extends SVGElement implements Curve {

    private ComplexNumber control1;
    private ComplexNumber control2;

    public CubicBezier(ComplexNumber start, ComplexNumber control1, ComplexNumber control2, ComplexNumber end) {
        super(start, end);
        this.control1 = control1;
        this.control2 = control2;
    }

    @Override
    public boolean isSmoothFrom(SVGElement previous) {
        if (previous instanceof CubicBezier p) return this.start.equals(p.getEnd()) &&
                ComplexNumber.subtract(this.getControl1(), this.getControl2()).equals(ComplexNumber.subtract(p.getEnd(), p.getControl2()));
        return this.getControl1().equals(this.getStart());
    }

    @Override
    public ComplexNumber point(double pos) {
        ComplexNumber a = ComplexNumber.multiply(this.getStart(), Math.pow(1 - pos, 3));
        ComplexNumber b = ComplexNumber.multiply(this.getControl1(), 3 * Math.pow(1 - pos, 2) * pos);
        ComplexNumber c = ComplexNumber.multiply(this.getControl2(), 3 * (1 - pos) * Math.pow(pos, 2));
        ComplexNumber d = ComplexNumber.multiply(this.getEnd(), Math.pow(pos, 3));
        return ComplexNumber.add(ComplexNumber.add(a, b), ComplexNumber.add(a, b));
    }

    @Override
    public double length() {
        ComplexNumber startPoint = this.point(0);
        ComplexNumber endPoint = this.point(1);
        return segmentLength(this, 0, 1, startPoint, endPoint, ERROR, MIN_DEPTH, 0);
    }

    @Override
    public String toString() {
        return "CubicBezier(" +
                "start=" + start +
                ", control1=" + control1 +
                ", control2=" + control2 +
                ", end=" + end +
                ')';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CubicBezier that = (CubicBezier) o;
        return Objects.equals(start, that.start) && Objects.equals(control1, that.control1) && Objects.equals(control2, that.control2) && Objects.equals(end, that.end);
    }

    public ComplexNumber getControl1() {
        return control1;
    }

    public ComplexNumber getControl2() {
        return control2;
    }

    public void setControl1(ComplexNumber control1) {
        this.control1 = control1;
    }

    public void setControl2(ComplexNumber control2) {
        this.control2 = control2;
    }
}
