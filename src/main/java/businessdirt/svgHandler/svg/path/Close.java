package businessdirt.svgHandler.svg.path;

import businessdirt.svgHandler.svg.Utils;
import com.vm.jcomplex.Complex;

public class Close extends Linear {

    public Close(Complex start, Complex end) {
        super(start, end);
    }

    @Override
    public String toString() {
        return String.format("Close(start=%s, end=%s)", this.getStart(), this.getEnd());
    }

    public boolean pathIsClosed() {
        return Utils.equals(this.getStart(), this.getEnd());
    }
}
