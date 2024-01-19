package businessdirt.svgHandler.svg.path;

import businessdirt.svgHandler.svg.Bisect;
import com.vm.jcomplex.Complex;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class Path extends LinkedList<SVGElement> {

    private final LinkedList<Double> fractions;
    private LinkedList<Double> lengths, nonLinearLengths;
    private double length;
    private final double EMPTY_VALUE = -1.0;

    public Path() {
        this(new LinkedList<>());
    }

    public Path(LinkedList<SVGElement> segments) {
        this.addAll(segments);
        this.fractions = new LinkedList<>();
        this.lengths = new LinkedList<>();
        this.nonLinearLengths = new LinkedList<>();
        this.length = EMPTY_VALUE;
    }

    public double length() {
        this.calcLengths();
        return this.length;
    }

    private void calcLengths() {
        if (this.length != EMPTY_VALUE) return;

        double[] lengths = this.stream().mapToDouble(SVGElement::length).toArray();
        this.length = Arrays.stream(lengths).sum();
        this.lengths = Arrays.stream(lengths).mapToObj(len -> len / this.length).collect(Collectors.toCollection(LinkedList::new));
        this.nonLinearLengths = this.stream().filter(e -> !(e instanceof Linear) && !(e instanceof Move)).mapToDouble(SVGElement::length)
                .mapToObj(len -> len / this.stream().mapToDouble(SVGElement::length).sum()).collect(Collectors.toCollection(LinkedList::new));

        // fractional distance to use in point()
        double fraction = 0;
        for (double d : this.lengths) {
            fraction += d;
            this.fractions.add(fraction);
        }
    }

    public Complex point(double pos) {
        if (pos == 0.0) return this.getFirst().point(pos);
        if (pos == 1.0) return this.getLast().point(pos);

        this.calcLengths();
        int i = Bisect.bisect(this.fractions.stream().mapToDouble(e -> e).toArray(), pos);

        if (i == 0) {
            double segmentPos = pos / this.fractions.get(0);
            return this.get(i).point(segmentPos);
        }

        double segmentPos = (pos - this.fractions.get(i - 1)) / (this.fractions.get(i) - this.fractions.get(i - 1));
        return this.get(i).point(segmentPos);
    }

    /**
     * Generates <code>n</code> points on the path and scales the resulting Graph by the <code>multiplier</code>
     * @param n the amount of points on the path
     * @param multiplier the scale of the graph
     * @return a <code>List</code> of scaled complex points on the graph
     */
    public List<Complex> points(int n, double multiplier) {
        this.calcLengths();
        int individualSegments = this.size() - 2;
        if (n < this.size()) n = individualSegments;
        int pointsOnNonLinear = n - (this.size() - this.nonLinearLengths.size() - 2);
        if (n < this.size()) pointsOnNonLinear -= 1;
        List<Complex> points = new LinkedList<>();

        int j = 0, h = 0;
        long pointsAvailable = pointsOnNonLinear;
        SVGElement current = null;
        SVGElement last = null;

        while (j < individualSegments) {
            last = current;
            current = this.get(j + 1);
            if (current instanceof Linear) {
                Complex point = current.getStart();
                if (last == current) {
                    point = current.getEnd();
                    j += 1;
                }
                if (complexAlreadyInList(points, point)) points.add(point.multiply(multiplier));
            } else {
                double f = this.nonLinearLengths.get(h) * pointsOnNonLinear;
                long pointsOnJ = Math.round(f);
                pointsAvailable -= pointsOnJ;
                if (this.nonLinearLengths.size() - h == 1) pointsOnJ += pointsAvailable;
                for (int k = 0; k < pointsOnJ; k++) {
                    double pos = k / (double) pointsOnJ;
                    Complex point = current.point(pos).multiply(multiplier);
                    if (complexAlreadyInList(points, point)) points.add(point);
                }
                h += 1;
                j += 1;
            }
        }
        return points;
    }

    private boolean complexAlreadyInList(List<Complex> complexes, Complex c) {
        return complexes.stream().noneMatch(z -> z.equals(c));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Path that = (Path) o;
        return Double.compare(length, that.length) == 0 && Objects.equals(fractions, that.fractions) && Objects.equals(lengths, that.lengths);
    }

    @Override
    public String toString() {
        return this.stream().map(Object::toString).collect(Collectors.joining(", ", "Path(", ")"));
    }
}
