package dp1key.cfs.svg.path;

import dp1key.cfs.svg.ComplexNumber;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

// TODO
public class Path {

    private final LinkedList<SVGElement> segments;
    private final LinkedList<Double> fractions;
    private List<Double> lengths;
    private double length;
    private final double EMPTY_VALUE = -1.0;
    private final int MIN_DEPTH = 5;
    private final double ERROR = 1e-12;

    public Path(SVGElement[] segments) {
        this((LinkedList<SVGElement>) List.of(segments));
    }

    public Path(LinkedList<SVGElement> segments) {
        this.segments = segments;
        this.fractions = new LinkedList<>();
        this.lengths = new LinkedList<>();
        this.length = EMPTY_VALUE;
    }

    public SVGElement getItem(int index) {
        return this.segments.get(index);
    }

    public void setItem(int index, SVGElement value) {
        this.segments.set(index, value);
    }

    public void delItem(int index) {
        this.segments.remove(index);
    }

    public void insert(int index, SVGElement value) {
        this.segments.add(index, value);
    }

    public void reverse() {
        // TODO
    }

    public double length() {
        this.calcLengths(ERROR, MIN_DEPTH);
        return this.length;
    }

    private void calcLengths(double error, int minDepth) {
        if (this.length == EMPTY_VALUE) return;

        double[] lengths = this.segments.stream().mapToDouble(SVGElement::length).toArray();
        this.length = Arrays.stream(lengths).sum();
        this.lengths = Arrays.stream(lengths).mapToObj(len -> len = len / this.length).toList();

        // fractional distance to use in point()
        double fraction = 0;
        for (double d : this.lengths) {
            fraction += d;
            this.fractions.add(fraction);
        }
    }

    public ComplexNumber point(double pos) {
        if (pos == 0.0) return this.segments.getFirst().point(pos);
        if (pos == 1.0) return this.segments.getLast().point(pos);

        this.calcLengths(ERROR, MIN_DEPTH);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Path path = (Path) o;
        return Objects.equals(segments, path.segments) && Objects.equals(fractions, path.fractions);
    }

    @Override
    public String toString() {
        return segments.stream().map(Object::toString).collect(Collectors.joining(", ", "Path(", ")"));
    }
}
