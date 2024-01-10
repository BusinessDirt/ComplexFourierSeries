package businessdirt.svgHandler.svg.path;

import com.vm.jcomplex.Complex;

public class Move extends SVGElement {

    public Move(Complex to) {
        super(to, to);
    }

    @Override
    public String toString() {
        return String.format("Move(to=%s)", this.getStart());
    }

    @Override
    public Complex point(double pos) {
        return this.getStart();
    }

    @Override
    public double length() {
        return 0;
    }
}
