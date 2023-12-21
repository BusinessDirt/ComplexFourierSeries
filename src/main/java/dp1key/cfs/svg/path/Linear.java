package dp1key.cfs.svg.path;

import dp1key.cfs.svg.ComplexNumber;

import java.util.Objects;

public class Linear extends SVGElement {

    public Linear(ComplexNumber start, ComplexNumber end) {
        super(start, end);
    }

    @Override
    public ComplexNumber point(double pos) {
        ComplexNumber distance = ComplexNumber.subtract(this.end, this.start);
        return ComplexNumber.add(this.start, ComplexNumber.multiply(distance, pos));
    }

    @Override
    public double length() {
        ComplexNumber distance = ComplexNumber.subtract(this.end, this.start);
        return distance.mod();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Linear linear = (Linear) o;
        return Objects.equals(getStart(), linear.getStart()) && Objects.equals(getEnd(), linear.getEnd());
    }
}
