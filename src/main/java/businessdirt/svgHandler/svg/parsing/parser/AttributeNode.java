package businessdirt.svgHandler.svg.parsing.parser;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class AttributeNode extends Node<String> {

    private static final Pattern DOUBLE_PATTERN = Pattern.compile("[-+]?\\d*[.]?\\d+(?:[eE][-+]?\\d+)?");

    public AttributeNode(String value, String cmd) {
        super(value);
        parse(cmd.toLowerCase(Locale.ROOT));
    }

    public void parse(String cmd) {
        List<String> attrs = split(this.value);

        // if the previous command is 'A' then we must parse booleans
        if (cmd.equals("a")) {
            // every 4th and 5th value is a boolean
            for (int i = 0; i < attrs.size(); i++) {
                int internalIndex = i % 7;
                if (internalIndex == 3 || internalIndex == 4) {
                    this.addChild(new BooleanNode(attrs.get(i)));
                } else {
                    this.addChild(new DoubleNode(attrs.get(i)));
                }
            }
        } else { // else all the values will be a double
            for (String attr : attrs) {
                this.addChild(new DoubleNode(attr));
            }
        }

    }

    public boolean isEmpty() {
        return this.children.isEmpty();
    }

    public Node<?> remove(int index) {
        return this.children.remove(index);
    }

    private static List<String> split(String token) {
        List<String> tokens = new LinkedList<>();
        Matcher doubleMatcher = DOUBLE_PATTERN.matcher(token);
        while (doubleMatcher.find()) tokens.add(doubleMatcher.group());
        return tokens;
    }
}
