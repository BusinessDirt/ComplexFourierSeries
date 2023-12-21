package dp1key.cfs.svg.path;

import dp1key.cfs.svg.ComplexNumber;

import java.util.Objects;

public class Arc extends SVGElement implements Curve {

    private ComplexNumber radius;
    private ComplexNumber center;
    private double rotation;
    private boolean arc;
    private boolean sweep;
    private double radiusScale, theta, delta;

    public Arc(ComplexNumber start, ComplexNumber radius, double rotation, boolean arc, boolean sweep, ComplexNumber end) {
        super(start, end);
        this.radius = radius;
        this.rotation = rotation;
        this.arc = arc;
        this.sweep = sweep;
        this.radiusScale = 0;
        this.theta = 0;
        this.delta = 0;
        this.center = null;
        parametrize();
    }

    private void parametrize() {
        if (this.start.equals(this.end)) return;
        if (this.radius.getReal() == 0 || this.radius.getImaginary() == 0) return;

        double cosr = Math.cos(Math.toRadians(this.getRotation()));
        double sinr = Math.sin(Math.toRadians(this.getRotation()));
        double dx = (this.start.getReal() - this.end.getReal()) / 2;
        double dy = (this.start.getImaginary() - this.end.getImaginary()) / 2;
        double x1prim = cosr * dx + sinr * dy;
        double x1primSq = x1prim * x1prim;
        double y1prim = -sinr * dx + cosr * dy;
        double y1primSq = y1prim * y1prim;

        double rx = this.radius.getReal();
        double rxSq = rx * rx;
        double ry = this.radius.getImaginary();
        double rySq = ry * ry;

        double radiusScale = (x1primSq / rxSq) + (y1primSq / rySq);
        if (radiusScale > 1) {
            radiusScale = Math.sqrt(radiusScale);
            rx *= radiusScale;
            ry *= radiusScale;
            rxSq = rx * rx;
            rySq = ry * ry;
            this.radiusScale = radiusScale;
        } else {
            this.radiusScale = 1;
        }

        double t1 = rxSq * y1primSq;
        double t2 = rySq * x1primSq;
        double c = Math.sqrt(Math.abs((rxSq * rySq - t1 - t2) / (t1 + t2)));

        if (this.isArc() == this.isSweep()) c = -c;
        double cxprim = c * rx * y1prim / ry;
        double cyprim = -c * ry * x1prim / rx;
        this.center = new ComplexNumber(
                (cosr * cxprim - sinr * cyprim) + ((this.start.getReal() + this.end.getReal()) / 2),
                (sinr * cxprim + cosr * cyprim) + ((this.start.getImaginary() + this.end.getImaginary()) / 2)
        );

        double ux = (x1prim - cxprim) / rx;
        double uy = (y1prim - cyprim) / ry;
        double vx = (-x1prim - cxprim) / rx;
        double vy = (-y1prim - cyprim) / ry;
        double n = Math.sqrt(ux * ux + uy * uy);
        double p = ux;
        double theta = Math.toDegrees(Math.acos(p / n));
        if (uy < 0) theta = -theta;
        this.theta = theta % 360;

        n = Math.sqrt((ux * ux + uy * uy) * (vx * vx + vy * vy));
        p = ux * vx + uy * vy;
        double d = p / n;

        // account for floating point inaccuracies
        if (d > 1.0) d = 1.0;
        else if (d < -1.0) d = -1.0;
        double delta = Math.toDegrees(Math.acos(d));
        if (ux * vy - uy * vx < 0) delta = -delta;
        this.delta = delta % 360;
        if (!this.isSweep()) this.delta -= 360;
    }

    @Override
    public ComplexNumber point(double pos) {
        if (this.start == this.end) return this.start;
        if (this.radius.getReal() == 0 || this.radius.getImaginary() == 0) {
            ComplexNumber distance = ComplexNumber.subtract(this.end, this.start);
            return ComplexNumber.add(this.start, ComplexNumber.multiply(distance, pos));
        }

        double angle = Math.toRadians(this.theta + (this.delta * pos));
        double cosr = Math.cos(Math.toRadians(this.rotation));
        double sinr = Math.sin(Math.toRadians(this.rotation));
        ComplexNumber radius = ComplexNumber.multiply(this.radius, this.radiusScale);

        double x = cosr * Math.cos(angle) * radius.getReal()
                - sinr * Math.sin(angle) * radius.getImaginary()
                + this.center.getReal();
        double y = sinr * Math.cos(angle) * radius.getReal()
                + cosr * Math.sin(angle) * radius.getImaginary()
                + this.center.getImaginary();
        return new ComplexNumber(x, y);
    }

    @Override
    public double length() {
        if (this.start.equals(this.end)) return 0;

        if (this.radius.getReal() == 0 || this.radius.getImaginary() == 0) {
            ComplexNumber distance = ComplexNumber.subtract(this.end, this.start);
            return distance.mod();
        }

        if (this.radius.getReal() == this.radius.getImaginary()) {
            // arc is a circle
            double radius = this.radius.getReal() * this.radiusScale;
            return Math.abs(radius * this.delta * Math.PI / 180);
        }

        ComplexNumber startPoint = this.point(0);
        ComplexNumber endPoint = this.point(1);
        return segmentLength(this, 0, 1, startPoint, endPoint, ERROR, MIN_DEPTH, 0);
    }

    @Override
    public String toString() {
        return "Arc(" +
                "start=" + start +
                ", radius=" + radius +
                ", rotation=" + rotation +
                ", arc=" + arc +
                ", sweep=" + sweep +
                ", end=" + end +
                ')';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Arc arc1 = (Arc) o;
        return getRotation() == arc1.getRotation() && isArc() == arc1.isArc() && isSweep() == arc1.isSweep() && Objects.equals(getRadius(), arc1.getRadius());
    }

    public ComplexNumber getRadius() {
        return radius;
    }

    public void setRadius(ComplexNumber radius) {
        this.radius = radius;
    }

    public double getRotation() {
        return rotation;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    public boolean isArc() {
        return arc;
    }

    public void setArc(boolean arc) {
        this.arc = arc;
    }

    public boolean isSweep() {
        return sweep;
    }

    public void setSweep(boolean sweep) {
        this.sweep = sweep;
    }
}
