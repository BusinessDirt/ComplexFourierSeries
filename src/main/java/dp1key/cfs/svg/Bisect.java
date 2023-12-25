package dp1key.cfs.svg;

public class Bisect {
    public static int bisect_right(double[] A, double x) {
        return bisect_right(A, x, 0, A.length);
    }

    public static int bisect_right(double[] A, double x, int lo, int hi) {
        int N = A.length;
        if (N == 0) return 0;
        if (x < A[lo]) return lo;
        if (x > A[hi - 1]) return hi;

        for (;;) {
            if (lo + 1 == hi) return lo + 1;

            int mi = (hi + lo) / 2;
            if (x < A[mi]) {
                hi = mi;
            } else {
                lo = mi;
            }
        }
    }

    public static int bisect_left(double[] A, double x) {
        return bisect_left(A, x, 0, A.length);
    }

    public static int bisect_left(double[] A, double x, int lo, int hi) {
        int N = A.length;
        if (N == 0) return 0;
        if (x < A[lo]) return lo;
        if (x > A[hi - 1]) return hi;

        for (;;) {
            if (lo + 1 == hi) return x == A[lo] ? lo : (lo + 1);
            int mi = (hi + lo) / 2;
            if (x <= A[mi]) {
                hi = mi;
            } else {
                lo = mi;
            }
        }
    }
}
