package businessdirt.svgHandler.svg.path;

import com.vm.jcomplex.Complex;

public abstract class SVGElement{

    protected Complex start, end;

    public SVGElement(Complex start, Complex end) {
        this.start = start;
        this.end = end;
    }

    public abstract Complex point(double pos);

    public abstract double length();

    public Complex getStart() {
        return start;
    }

    public Complex getEnd() {
        return end;
    }
}
