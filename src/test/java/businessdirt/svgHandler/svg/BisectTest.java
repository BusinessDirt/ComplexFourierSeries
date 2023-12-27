package businessdirt.svgHandler.svg;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BisectTest {

    @Test
    public void bisect_right() {
        System.out.println("bisect_right");
        double[] A = new double[]{0, 1, 2, 2, 2, 2, 3, 3, 5, 6};
        assertEquals(0, Bisect.bisect_right(A, -1));
        assertEquals(1, Bisect.bisect_right(A, 0));
        assertEquals(6, Bisect.bisect_right(A, 2));
        assertEquals(8, Bisect.bisect_right(A, 3));
        assertEquals(8, Bisect.bisect_right(A, 4));
        assertEquals(9, Bisect.bisect_right(A, 5));
        assertEquals(10, Bisect.bisect_right(A, 6));
        assertEquals(10, Bisect.bisect_right(A, 7));
    }

    @Test
    public void bisect_left() {
        System.out.println("bisect_left");
        double[] A = new double[]{0, 1, 2, 2, 2, 2, 3, 3, 5, 6};
        assertEquals(0, Bisect.bisect_left(A, -1));
        assertEquals(0, Bisect.bisect_left(A, 0));
        assertEquals(2, Bisect.bisect_left(A, 2));
        assertEquals(6, Bisect.bisect_left(A, 3));
        assertEquals(8, Bisect.bisect_left(A, 4));
        assertEquals(8, Bisect.bisect_left(A, 5));
        assertEquals(9, Bisect.bisect_left(A, 6));
        assertEquals(10, Bisect.bisect_left(A, 7));
    }
}