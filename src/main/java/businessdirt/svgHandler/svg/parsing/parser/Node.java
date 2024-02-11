package businessdirt.svgHandler.svg.parsing.parser;

import java.util.LinkedList;
import java.util.List;

public abstract class Node<T> {
    protected final List<Node<?>> children;
    protected T value;

    public Node(T value) {
        this.children = new LinkedList<>();
        this.value = value;
    }

    public void addChild(Node<?> child) {
        this.children.add(child);
    }

    public void print(int depth) {
        System.out.println("\t".repeat(depth) + this);
        for (Node<?> child : this.children) {
            child.print(depth + 1);
        }
    }

    @Override
    public String toString() {
        return String.format("%s(%s)", this.getClass().getSimpleName(), value);
    }

    public T getValue() {
        return value;
    }

    public double asDouble() {
        return (double) value;
    }

    public boolean asBoolean() {
        return (boolean) value;
    }
}
