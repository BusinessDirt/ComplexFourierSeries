package businessdirt.svgHandler.svg;

public class Bisect {
    public static int bisect_right(double[] array, double x) {
        return bisect_right(array, x, 0, array.length);
    }

    public static int bisect_right(double[] array, double x, int low, int high) {
        if (array.length == 0) return 0;
        if (x < array[low]) return low;
        if (x > array[high - 1]) return high;

        while (true) {
            if (low + 1 == high) return low + 1;
            int mid = (high + low) / 2;
            if (x < array[mid]) high = mid;
            else low = mid;
        }
    }

    public static int bisect_left(double[] array, double x) {
        return bisect_left(array, x, 0, array.length);
    }

    public static int bisect_left(double[] array, double x, int low, int high) {
        if (array.length == 0) return 0;
        if (x < array[low]) return low;
        if (x > array[high - 1]) return high;

        while (true) {
            if (low + 1 == high) return x == array[low] ? low : (low + 1);
            int mid = (high + low) / 2;
            if (x <= array[mid]) high = mid;
            else low = mid;
        }
    }
}
