package dp1key.cfs.svg.path;

import dp1key.cfs.svg.Bisect;
import dp1key.cfs.svg.ComplexNumber;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

// TODO
@SuppressWarnings("unused")
public class Path extends LinkedList<SVGElement> {

    private final LinkedList<Double> fractions;
    private List<Double> lengths;
    private double length;
    private final double EMPTY_VALUE = -1.0;

    public Path() {
        this(new LinkedList<>());
    }

    public Path(LinkedList<SVGElement> segments) {
        this.addAll(segments);
        this.fractions = new LinkedList<>();
        this.lengths = new LinkedList<>();
        this.length = EMPTY_VALUE;
    }

    public void reverse() {
        // TODO
    }

    public double length() {
        this.calcLengths();
        return this.length;
    }

    private void calcLengths() {
        if (this.length != EMPTY_VALUE) return;

        double[] lengths = this.stream().mapToDouble(SVGElement::length).toArray();
        this.length = Arrays.stream(lengths).sum();
        this.lengths = Arrays.stream(lengths).mapToObj(len -> len / this.length).toList();

        // fractional distance to use in point()
        double fraction = 0;
        for (double d : this.lengths) {
            fraction += d;
            this.fractions.add(fraction);
        }
    }

    public ComplexNumber point(double pos) {
        if (pos == 0.0) return this.getFirst().point(pos);
        if (pos == 1.0) return this.getLast().point(pos);

        this.calcLengths();
        int i = Bisect.bisect_right(this.fractions.stream().mapToDouble(e -> e).toArray(), pos);

        if (i == 0) {
            double segmentPos = pos / this.fractions.get(0);
            return this.get(i).point(segmentPos);
        }

        double segmentPos = (pos - this.fractions.get(i - 1)) / (this.fractions.get(i) - this.fractions.get(i - 1));
        return this.get(i).point(segmentPos);
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
