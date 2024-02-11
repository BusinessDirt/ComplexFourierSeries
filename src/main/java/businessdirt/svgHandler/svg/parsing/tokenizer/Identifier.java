package businessdirt.svgHandler.svg.parsing.tokenizer;

import java.util.Locale;
import java.util.regex.Pattern;

public class Identifier extends Type<Character> {

    private static final Pattern pattern = Pattern.compile("^[MmZzLlHhVvCcSsQqTtAa]$");

    public Identifier(String value) {
        super(value.toCharArray()[0]);
    }

    public boolean absolute() {
        return Character.isUpperCase(this.getValue());
    }

    public String command() {
        return this.getValue().toString().toUpperCase(Locale.ROOT);
    }

    public static boolean matches(String token) {
        return pattern.matcher(token).matches();
    }
}
