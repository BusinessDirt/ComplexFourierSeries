package businessdirt.svgHandler.svg.parsing.parser;

public class BooleanNode extends Node<Boolean> {
    public BooleanNode(String value) {
        super(Boolean.parseBoolean(value));
    }


}
