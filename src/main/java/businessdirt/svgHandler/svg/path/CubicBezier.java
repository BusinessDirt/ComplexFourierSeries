package businessdirt.svgHandler.svg.path;

import com.vm.jcomplex.Complex;

import java.util.Objects;

public class CubicBezier extends SVGElement implements SegmentLength {

    private Complex control1, control2;

    public CubicBezier(Complex start, Complex control1, Complex control2, Complex end) {
        super(start, end);
        this.control1 = control1;
        this.control2 = control2;
    }

    public boolean isSmoothFrom(SVGElement previous) {
        if (previous instanceof CubicBezier p) return this.getStart().equals(p.getEnd()) &&
                this.getControl1().subtract(this.getControl2()).equals(this.getEnd().subtract(p.getControl2()));
        return this.getControl1().equals(this.getStart());
    }

    @Override
    public Complex point(double pos) {
        // (1 - pos)^3 * this.getStart()
        Complex a = this.getStart().multiply(Math.pow(1 - pos, 3));

        // 3 * (1 - pos)^2 * pos * this.getControl1()
        Complex b = this.getControl1().multiply(3 * Math.pow(1 - pos, 2) * pos);

        // 3 * (1 - pos) * pos^2 * this.getControl2()
        Complex c = this.getControl2().multiply(3 * (1 - pos) * Math.pow(pos, 2));

        // pos^3 * this.getEnd()
        Complex d = this.getEnd().multiply(Math.pow(pos, 3));

        return a.add(b).add(c).add(d);
    }

    @Override
    public double length() {
        Complex startPoint = this.point(0);
        Complex endPoint = this.point(1);
        return segmentLength(this, 0, 1, startPoint, endPoint, 0);
    }

    @Override
    public String toString() {
        return String.format("CubicBezier(start=%s, control1=%s, control2=%s, end=%s)",
                this.getStart(), this.getControl1(), this.getControl2(), this.getEnd());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CubicBezier that = (CubicBezier) o;
        return Objects.equals(this.getStart(), that.getStart()) && Objects.equals(this.getControl1(), that.getControl1())
                && Objects.equals(this.getControl2(), that.getControl2()) && Objects.equals(this.getEnd(), that.getEnd());
    }

    @Override
    public void reverse() {
        super.reverse();
        Complex _control1 = new Complex(this.getControl1().getReal(), this.getControl1().getImaginary());
        this.control1 = new Complex(this.getControl2().getReal(), this.getControl2().getImaginary());
        this.control2 = _control1;
    }

    public Complex getControl1() {
        return this.control1;
    }

    public Complex getControl2() {
        return this.control2;
    }
}
