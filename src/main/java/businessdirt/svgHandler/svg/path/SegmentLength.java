package businessdirt.svgHandler.svg.path;

import com.vm.jcomplex.Complex;

public interface SegmentLength {

    double ERROR = 1e-12;
    double MIN_DEPTH = 5;

    default double segmentLength(SVGElement curve, double start, double end, Complex startPoint, Complex endPoint, int depth) {
        double mid = (start + end) / 2;
        Complex midPoint = curve.point(mid);
        double length = endPoint.subtract(startPoint).abs();
        double length2 = midPoint.subtract(startPoint).abs() + endPoint.subtract(midPoint).abs();

        if ((length2 - length > ERROR) || (depth < MIN_DEPTH)) {
            depth += 1;
            return segmentLength(curve, start, mid, startPoint, midPoint, depth) +
                    segmentLength(curve, mid, end, midPoint, endPoint, depth);
        }
        return length2;
    }
}
