package businessdirt.svgHandler.svg.path;

import com.vm.jcomplex.Complex;

public abstract class SVGElement{

    protected Complex start, end;

    public SVGElement(Complex start, Complex end) {
        this.start = start;
        this.end = end;
    }

    public void reverse() {
        Complex _start = new Complex(this.start.getReal(), this.start.getImaginary());
        this.start = new Complex(this.end.getReal(), this.end.getImaginary());
        this.end = _start;
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
