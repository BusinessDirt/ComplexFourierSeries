package businessdirt.svgHandler.svg.path;

import com.vm.jcomplex.Complex;

import java.util.Objects;

public abstract class Linear extends SVGElement {

    public Linear(Complex start, Complex end) {
        super(start, end);
    }

    @Override
    public Complex point(double pos) {
        Complex distance = this.getEnd().subtract(this.getStart());
        return this.getStart().add(distance.multiply(pos));
    }

    @Override
    public double length() {
        Complex distance = this.getEnd().subtract(this.getStart());
        return distance.abs();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Linear linear = (Linear) o;
        return Objects.equals(getStart(), linear.getStart()) && Objects.equals(getEnd(), linear.getEnd());
    }
}
