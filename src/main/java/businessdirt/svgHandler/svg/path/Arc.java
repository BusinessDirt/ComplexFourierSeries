package businessdirt.svgHandler.svg.path;

import businessdirt.svgHandler.svg.ComplexNumber;

import java.util.Objects;

public class Arc extends SVGElement implements Curve {

    private final ComplexNumber radius;
    private ComplexNumber center;
    private final boolean arc;
    private final boolean sweep;
    private final double rotation;
    private double radiusScale;
    private double theta;
    private double delta;

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
        if (this.getStart().equals(this.getEnd())) return;
        if (this.getRadius().getReal() == 0 || this.getRadius().getImaginary() == 0) return;

        double cosr = Math.cos(Math.toRadians(this.getRotation()));
        double sinr = Math.sin(Math.toRadians(this.getRotation()));
        double dx = (this.start.getReal() - this.getEnd().getReal()) / 2;
        double dy = (this.start.getImaginary() - this.getEnd().getImaginary()) / 2;
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
            this.setRadiusScale(radiusScale);
        } else {
            this.setRadiusScale(radiusScale);
        }

        double t1 = rxSq * y1primSq;
        double t2 = rySq * x1primSq;
        double c = Math.sqrt(Math.abs((rxSq * rySq - t1 - t2) / (t1 + t2)));

        if (this.isArc() == this.isSweep()) c = -c;
        double cxprim = c * rx * y1prim / ry;
        double cyprim = -c * ry * x1prim / rx;
        this.setCenter(new ComplexNumber(
                (cosr * cxprim - sinr * cyprim) + ((this.getStart().getReal() + this.getEnd().getReal()) / 2),
                (sinr * cxprim + cosr * cyprim) + ((this.getStart().getImaginary() + this.getEnd().getImaginary()) / 2)
        ));

        double ux = (x1prim - cxprim) / rx;
        double uy = (y1prim - cyprim) / ry;
        double vx = (-x1prim - cxprim) / rx;
        double vy = (-y1prim - cyprim) / ry;
        double n = Math.sqrt(ux * ux + uy * uy);
        double p = ux;
        double theta = Math.toDegrees(Math.acos(p / n));
        if (uy < 0) theta = -theta;
        this.setTheta(theta % 360);

        n = Math.sqrt((ux * ux + uy * uy) * (vx * vx + vy * vy));
        p = ux * vx + uy * vy;
        double d = p / n;

        // account for floating point inaccuracies
        if (d > 1.0) d = 1.0;
        else if (d < -1.0) d = -1.0;
        double delta = Math.toDegrees(Math.acos(d));
        if (ux * vy - uy * vx < 0) delta = -delta;
        this.setDelta(delta % 360);
        if (!this.isSweep()) this.setDelta(this.getDelta() - 360);
    }

    @Override
    public ComplexNumber point(double pos) {
        if (this.getStart().equals(this.getEnd())) return this.getStart();
        if (this.getRadius().getReal() == 0 || this.getRadius().getImaginary() == 0) {
            ComplexNumber distance = ComplexNumber.subtract(this.getEnd(), this.getStart());
            return ComplexNumber.add(this.getStart(), ComplexNumber.multiply(distance, pos));
        }

        double angle = Math.toRadians(this.getTheta() + (this.getDelta() * pos));
        double cosr = Math.cos(Math.toRadians(this.getRotation()));
        double sinr = Math.sin(Math.toRadians(this.getRotation()));
        ComplexNumber radius = ComplexNumber.multiply(this.getRadius(), this.getRadiusScale());

        double x = cosr * Math.cos(angle) * radius.getReal()
                - sinr * Math.sin(angle) * radius.getImaginary()
                + this.getCenter().getReal();
        double y = sinr * Math.cos(angle) * radius.getReal()
                + cosr * Math.sin(angle) * radius.getImaginary()
                + this.getCenter().getImaginary();
        return new ComplexNumber(x, y);
    }

    @Override
    public double length() {
        if (this.getStart().equals(this.getEnd())) return 0;

        if (this.getRadius().getReal() == 0 || this.getRadius().getImaginary() == 0) {
            ComplexNumber distance = ComplexNumber.subtract(this.getEnd(), this.getStart());
            return distance.mod();
        }

        if (this.getRadius().getReal() == this.getRadius().getImaginary()) {
            // circle
            double radius = this.getRadius().getReal() * this.getRadiusScale();
            return Math.abs(radius * this.delta * Math.PI / 180);
        }

        return this.segmentLength(this, 0.0, 1.0, this.point(0.0), this.point(1.0), ERROR, MIN_DEPTH, 0);
    }

    @Override
    public String toString() {
        return String.format("Arc(start=%s, radius=%s, rotation=%s, arc=%s, sweep=%s, end=%s)",
                this.getStart(), this.getRadius(), this.getRotation(), this.isArc(), this.isSweep(), this.getEnd());
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


    public double getRotation() {
        return rotation;
    }

    public boolean isArc() {
        return arc;
    }

    public boolean isSweep() {
        return sweep;
    }

    public ComplexNumber getCenter() {
        return center;
    }

    public double getRadiusScale() {
        return radiusScale;
    }

    public double getTheta() {
        return theta;
    }

    public double getDelta() {
        return delta;
    }

    public void setCenter(ComplexNumber center) {
        this.center = center;
    }

    public void setRadiusScale(double radiusScale) {
        this.radiusScale = radiusScale;
    }

    public void setTheta(double theta) {
        this.theta = theta;
    }

    public void setDelta(double delta) {
        this.delta = delta;
    }
}
