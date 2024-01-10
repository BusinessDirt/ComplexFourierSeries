package businessdirt.svgHandler.svg.path;

import businessdirt.svgHandler.svg.ComplexNumber;

public interface SegmentLength {

    double ERROR = 1e-12;
    double MIN_DEPTH = 5;

    default double segmentLength(SVGElement curve, double start, double end, ComplexNumber startPoint, ComplexNumber endPoint, int depth) {
        double mid = (start + end) / 2;
        ComplexNumber midPoint = curve.point(mid);
        double length = ComplexNumber.subtract(endPoint, startPoint).mod();
        double length2 = ComplexNumber.subtract(midPoint, startPoint).mod() + ComplexNumber.subtract(endPoint, midPoint).mod();

        if ((length2 - length > ERROR) || (depth < MIN_DEPTH)) {
            depth += 1;
            return segmentLength(curve, start, mid, startPoint, midPoint, depth) +
                    segmentLength(curve, mid, end, midPoint, endPoint, depth);
        }
        return length2;
    }
}
