package dp1key.cfs.svg.path;

import dp1key.cfs.svg.ComplexNumber;

public interface Curve {

    default boolean isSmoothFrom(SVGElement previous) {
        return false;
    }

    default double segmentLength(SVGElement curve, double start, double end, ComplexNumber startPoint, ComplexNumber endPoint, double error, int minDepth, int depth) {
        double mid = (start + end) / 2;
        ComplexNumber midPoint = curve.point(mid);
        double length = ComplexNumber.subtract(endPoint, startPoint).mod();
        double firstHalf = ComplexNumber.subtract(midPoint, startPoint).mod();
        double secondHalf = ComplexNumber.subtract(endPoint, midPoint).mod();
        double length2 = firstHalf + secondHalf;

        if (length2 - length > error || depth < minDepth) {
            depth++;
            return segmentLength(curve, start, mid, startPoint, endPoint, error, minDepth, depth) +
                    segmentLength(curve, mid, end, startPoint, endPoint, error, minDepth, depth);
        }
        return length2;
    }
}
