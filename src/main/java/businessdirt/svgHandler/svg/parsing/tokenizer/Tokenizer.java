package businessdirt.svgHandler.svg.parsing.tokenizer;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Tokenizer extends LinkedList<Type<?>> {

    public Tokenizer(String src) {
        this.addAll(Tokenizer.tokenize(src));
    }

    public void print() {
        System.out.println("ParseTree()");
        for (Type<?> token : this) {
            token.print(1);
        }
    }

    private static final String COMMANDS = "MmZzLlHhVvCcSsQqTtAa";
    private static final Pattern COMMAND_PATTERN = Pattern.compile("((?=[" + Tokenizer.COMMANDS + "])|(?<=[" + Tokenizer.COMMANDS + "]))");

    private static List<Type<?>> tokenize(String src) {
        List<String> tokens = Tokenizer.COMMAND_PATTERN.splitAsStream(src).toList();
        return tokens.stream().map(Tokenizer::typeFromToken).collect(Collectors.toList());
    }

    private static Type<?> typeFromToken(String token) {
        if (Identifier.matches(token)) return new Identifier(token);
        return new Attribute(token);
    }
}
