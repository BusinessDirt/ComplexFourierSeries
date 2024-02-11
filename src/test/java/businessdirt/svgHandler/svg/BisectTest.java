package businessdirt.svgHandler.svg;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BisectTest {

    @Test
    public void bisectRight() {
        double[] A = new double[]{0, 1, 2, 2, 2, 2, 3, 3, 5, 6};
        assertEquals(0, Utils.bisect(A, -1));
        assertEquals(1, Utils.bisect(A, 0));
        assertEquals(6, Utils.bisect(A, 2));
        assertEquals(8, Utils.bisect(A, 3));
        assertEquals(8, Utils.bisect(A, 4));
        assertEquals(9, Utils.bisect(A, 5));
        assertEquals(10, Utils.bisect(A, 6));
        assertEquals(10, Utils.bisect(A, 7));
    }
}