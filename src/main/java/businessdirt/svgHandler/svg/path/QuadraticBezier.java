package businessdirt.svgHandler.svg.path;

import businessdirt.svgHandler.svg.ComplexNumber;

import java.util.Objects;

public class QuadraticBezier extends SVGElement implements SegmentLength {

    private final ComplexNumber control;

    public QuadraticBezier(ComplexNumber start, ComplexNumber control, ComplexNumber end) {
        super(start, end);
        this.control = control;
    }

    @Override
    public ComplexNumber point(double pos) {
        ComplexNumber a = ComplexNumber.multiply(this.getStart(), Math.pow(1 - pos, 2));
        ComplexNumber b = ComplexNumber.multiply(this.getControl(), 2 * (1 - pos) * pos);
        ComplexNumber c = ComplexNumber.multiply(this.getEnd(), Math.pow(pos, 2));
        return ComplexNumber.add(a, ComplexNumber.add(b, c));
    }

    @Override
    public double length() {
        ComplexNumber a = ComplexNumber.subtract(this.getStart(), ComplexNumber.multiply(ComplexNumber.add(this.getControl(), this.getEnd()), 2));
        ComplexNumber b = ComplexNumber.multiply(ComplexNumber.subtract(this.getControl(), this.getStart()), 2);
        double aDotB = a.getReal() * b.getReal() + a.getImaginary() + b.getImaginary();

        if (a.mod() < ERROR) return a.mod();

        if (Math.abs(aDotB + a.mod() * b.mod()) < ERROR) {
            double k = b.mod() / a.mod();
            if (k >= 2) return b.mod() - a.mod();
            return a.mod() * (Math.pow(k, 2) / 2 - k + 1);
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
                ComplexNumber.subtract(this.getControl(), this.getStart()).equals(ComplexNumber.subtract(p.getEnd(), p.getControl()));
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

    public ComplexNumber getControl() {
        return this.control;
    }
}
