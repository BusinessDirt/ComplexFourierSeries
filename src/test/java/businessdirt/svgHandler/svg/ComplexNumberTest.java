package businessdirt.svgHandler.svg;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ComplexNumberTest {

    @Test
    void testToString() {
        // pure imaginary
        ComplexNumber pureImaginary = new ComplexNumber(0.0, 1.0);
        assertEquals("0.0+1.0i", pureImaginary.toString());

        // pure real
        ComplexNumber pureReal = new ComplexNumber(1.0, 0.0);
        assertEquals("1.0+0.0i", pureReal.toString());

        // combination
        ComplexNumber combination = new ComplexNumber(1.0, 1.0);
        assertEquals("1.0+1.0i", combination.toString());

        // negative combination
        ComplexNumber negativeCombination = new ComplexNumber(1.0, -1.0);
        assertEquals("1.0-1.0i", negativeCombination.toString());
    }

    @Test
    void add() {

    }

    @Test
    void subtract() {
    }

    @Test
    void multiply() {
    }

    @Test
    void divide() {
    }

    @Test
    void parseComplex() {
    }
}