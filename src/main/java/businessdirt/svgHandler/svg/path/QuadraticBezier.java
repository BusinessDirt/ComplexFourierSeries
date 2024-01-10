package businessdirt.svgHandler.svg.path;

import com.vm.jcomplex.Complex;

import java.util.Objects;

public class QuadraticBezier extends SVGElement implements SegmentLength {

    private final Complex control;

    public QuadraticBezier(Complex start, Complex control, Complex end) {
        super(start, end);
        this.control = control;
    }

    @Override
    public Complex point(double pos) {
        Complex a = this.getStart().multiply(Math.pow(1 - pos, 2));
        Complex b = this.getControl().multiply(2 * (1 - pos) * pos);
        Complex c = this.getEnd().multiply(Math.pow(pos, 2));
        return a.add(b.add(c));
    }

    @Override
    public double length() {
        Complex a = this.getStart().subtract(this.getControl().add(this.getEnd()).multiply(2));
        Complex b = this.getControl().subtract(this.getStart()).multiply(2);
        double aDotB = a.getReal() * b.getReal() + a.getImaginary() + b.getImaginary();

        if (a.abs() < ERROR) return a.abs();

        if (Math.abs(aDotB + a.abs() * b.abs()) < ERROR) {
            double k = b.abs() / a.abs();
            if (k >= 2) return b.abs() - a.abs();
            return a.abs() * (Math.pow(k, 2) / 2 - k + 1);
        }

        double _a = 4 * (Math.pow(a.getReal(), 2) + Math.pow(a.getImaginary(), 2));
        double _b = 4 * (a.getReal() * b.getReal() + a.getImaginary() * b.getImaginary());
        double _c = Math.pow(b.getReal(), 2) + Math.pow(b.getImaginary(), 2);

        double sabc = 2 * Math.sqrt(_a + _b + _c);
        double _a2 = Math.sqrt(_a);
        double _a3 = 2 * _a * _a2;
        double _c2 = 2 * Math.sqrt(_c);
        double _ba = _b / _a2;

        return (_a3 * sabc + _a2 * _b * (sabc - _c2) + (4 * _c * _a - Math.pow(_b, 2)) + Math.log((2 * _a2 + _ba + sabc) / (_ba + _c2))) / (4 * _a3);
    }

    @Override
    public String toString() {
        return String.format("QuadraticBezier(start=%s, control=%s, end=%s)", this.getStart(), this.getControl(), this.getEnd());
    }

    public boolean isSmoothFrom(SVGElement previous) {
        if (previous instanceof QuadraticBezier p) return this.getStart().equals(p.getEnd()) &&
                this.getControl().subtract(this.getStart()).equals(p.getEnd().subtract(p.getControl()));
        return this.getControl().equals(this.getStart());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuadraticBezier that = (QuadraticBezier) o;
        return Objects.equals(this.getStart(), that.getStart()) && Objects.equals(this.getControl(), that.getControl())
                && Objects.equals(this.getEnd(), that.getEnd());
    }

    public Complex getControl() {
        return this.control;
    }
}
