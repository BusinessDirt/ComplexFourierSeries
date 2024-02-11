package businessdirt.svgHandler.svg.parsing.parser;

public class DoubleNode extends Node<Double> {

    public DoubleNode(String value) {
        super(Double.parseDouble(value));
    }
}
