package businessdirt.svgHandler.svg.parsing.parser;

import java.util.Locale;

public class IdentifierNode extends Node<String> {

    public final boolean absolute;

    public IdentifierNode(String command) {
        super(command);
        this.absolute = Character.isUpperCase(command.charAt(0));
        this.value = command.toUpperCase(Locale.ROOT);
    }
}
