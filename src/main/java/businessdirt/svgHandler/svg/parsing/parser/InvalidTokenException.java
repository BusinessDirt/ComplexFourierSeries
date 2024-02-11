package businessdirt.svgHandler.svg.parsing.parser;

import java.io.IOException;

public class InvalidTokenException extends IOException {
    public InvalidTokenException() {
        super("Invalid Token");
    }
}
