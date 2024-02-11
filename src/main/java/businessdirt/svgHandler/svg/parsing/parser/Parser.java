package businessdirt.svgHandler.svg.parsing.parser;

import businessdirt.svgHandler.svg.parsing.tokenizer.Attribute;
import businessdirt.svgHandler.svg.parsing.tokenizer.Identifier;
import businessdirt.svgHandler.svg.parsing.tokenizer.Tokenizer;
import businessdirt.svgHandler.svg.parsing.tokenizer.Type;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Parser extends LinkedList<Node<?>> {

    public Parser(Tokenizer tokens) {
        this.addAll(Parser.parse(tokens));
    }

    public void print() {
        // TODO: maybe add better indentations; possibly use streams with flatMap() and obviously recursion
        System.out.println("ParseTree()");
        for (Node<?> node : this) node.print(1);
    }

    private static List<Node<?>> parse(Tokenizer tokens) {
        List<Node<?>> parseTree = new LinkedList<>();
        for (int i = 0; i < tokens.size(); i++) {
            parseTree.add(parseToken(tokens.get(i), i > 0 ? tokens.get(i - 1).getValue().toString() : ""));
        }
        return parseTree;
    }

    private static Node<?> parseToken(Type<?> token, String cmd) {
        try {
            if (token instanceof Identifier) return new IdentifierNode(token.getValue().toString());
            if (token instanceof Attribute) {
                if (Objects.equals(cmd, "")) throw new InvalidTokenException();
                return new AttributeNode(token.getValue().toString(), cmd);
            }
            throw new InvalidTokenException();
        } catch (InvalidTokenException e) {
            throw new RuntimeException(e);
        }
    }
}
