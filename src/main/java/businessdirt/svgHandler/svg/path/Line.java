package businessdirt.svgHandler.svg.path;

import com.vm.jcomplex.Complex;

public class Line extends Linear {

    public Line(Complex start, Complex end) {
        super(start, end);
    }

    @Override
    public String toString() {
        return String.format("Line(start=%s, end=%s)", this.getStart(), this.getEnd());
    }
}
