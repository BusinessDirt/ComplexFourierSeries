package businessdirt.svgHandler.svg.path;

import businessdirt.svgHandler.svg.Utils;
import com.vm.jcomplex.Complex;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class Path extends LinkedList<SVGElement> {

    private final LinkedList<Double> fractions;
    private LinkedList<Double> lengths, nonLinearLengths;
    private double length;
    private final double EMPTY_VALUE = -1.0;
    private int move, close;

    public Path() {
        this(new LinkedList<>());
    }

    public Path(LinkedList<SVGElement> segments) {
        this.addAll(segments);
        this.fractions = new LinkedList<>();
        this.lengths = new LinkedList<>();
        this.nonLinearLengths = new LinkedList<>();
        this.length = EMPTY_VALUE;
        this.move = 0;
        this.close = 0;
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

        double[] nonLinearLengths = this.stream().filter(e -> !(e instanceof Linear)).mapToDouble(SVGElement::length).toArray();
        double nonLinearLength = Arrays.stream(nonLinearLengths).sum();
        this.nonLinearLengths = Arrays.stream(nonLinearLengths).mapToObj(len -> len / nonLinearLength).collect(Collectors.toCollection(LinkedList::new));

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
        int i = Utils.bisect(this.fractions.stream().mapToDouble(e -> e).toArray(), pos);

        if (i == 0) {
            double segmentPos = pos / this.fractions.get(0);
            return this.get(i).point(segmentPos);
        }

        double segmentPos = (pos - this.fractions.get(i - 1)) / (this.fractions.get(i) - this.fractions.get(i - 1));
        return this.get(i).point(segmentPos);
    }

    /**
     * Generates {@code n} points on the path and scales the resulting graph by the {@code multiplier}
     * @param n the amount of points on the path
     * @param multiplier the scale of the graph
     * @return a {@link List} of scaled complex points on the graph
     */
    public List<Complex> points(int n, double multiplier) {
        this.calcLengths();

        // Remove the Move and Close Element from the count
        int individualSegments = this.size() - this.close - this.move;

        // if the start and end points do not align we need to subtract one
        if (!closed()) individualSegments += 1;

        if (n < individualSegments) {
            List<Complex> points = new LinkedList<>();
            for (int i = 0; i < n; i++) {
                Complex z = this.point(i / (double) n).multiply(multiplier);
                points.add(z);
            }
            return points;
        }

        // Every Linear element technically has 2 Points but one always ends in another (square has 4 Lines but not 8 Points)
        // The amount of points is therefore n minus the amount of linear elements
        int pointsOnNonLinear = n - (individualSegments - this.nonLinearLengths.size());

        // Hashset for better time complexity O(1) instead of O(n)
        Set<Complex> pointsSet= new LinkedHashSet<>();
        SVGElement current = null, last;

        int linearIndex = 0, nonLinearIndex = 0;
        long pointsAvailable = pointsOnNonLinear;

        while (linearIndex + nonLinearIndex < individualSegments) {
            last = current;
            current = this.get(linearIndex + nonLinearIndex + 1);
            if (current instanceof Linear) {
                Complex point = current.getStart();

                // check if we are calculating the end point of the line
                // the list index gets only incremented when this is the case
                if (last == current) {
                    point = current.getEnd();
                    linearIndex += 1;
                }

                pointsSet.add(point.multiply(multiplier));
            } else {
                // percentage of 'space' the element needs times the total points on all non-linear elements
                double f = this.nonLinearLengths.get(nonLinearIndex) * pointsOnNonLinear;
                long pointsOnJ = Math.round(f);

                // make sure we actually have n points in total and not some other number
                pointsAvailable -= pointsOnJ;
                if (this.nonLinearLengths.size() - nonLinearIndex == 1 && pointsSet.size() + pointsAvailable + pointsOnJ <= n)
                    pointsOnJ += pointsAvailable;


                // calculate all the points and add them to the list
                for (int k = 0; k < pointsOnJ; k++) {
                    double pos = k / (double) pointsOnJ;
                    Complex point = current.point(pos).multiply(multiplier);
                    pointsSet.add(point);
                }
                nonLinearIndex += 1;
            }
        }
        return pointsSet.parallelStream().toList();
    }

    public List<Complex> points(int n) {
        return this.points(n, 1.0);
    }

    @Override
    public boolean add(SVGElement svgElement) {
        if (svgElement instanceof Close) this.close += 1;
        if (svgElement instanceof Move) this.move += 1;
        return super.add(svgElement);
    }

    public boolean closed() {
        SVGElement first = this.getFirst();
        SVGElement last = this.getLast();
        if (last instanceof Close) return ((Close) last).pathIsClosed();
        return Utils.equals(first.getStart(), last.getEnd());
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
