package businessdirt.svgHandler.svg.parsing.tokenizer;

public abstract class Type<T> {

    private final T value;

    public Type(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public double asDouble() {
        return (double) this.value;
    }

    public boolean asBoolean() {
        return (boolean) this.value;
    }

    public void print(int depth) {
        System.out.println("\t".repeat(depth) + this);
    }

    @Override
    public String toString() {
        return String.format("%s(%s)", this.getClass().getSimpleName(), value);
    }
}
