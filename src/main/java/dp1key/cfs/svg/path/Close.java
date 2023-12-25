package dp1key.cfs.svg.path;

import dp1key.cfs.svg.ComplexNumber;

public class Close extends Linear {

    public Close(ComplexNumber start, ComplexNumber end) {
        super(start, end);
    }

    @Override
    public String toString() {
        return String.format("Close(start=%s, end=%s)", this.getStart(), this.getEnd());
    }
}
