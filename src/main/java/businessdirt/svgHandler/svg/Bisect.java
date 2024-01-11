package businessdirt.svgHandler.svg;

public class Bisect {
    public static int bisect(double[] array, double x) {
        return bisect(array, x, 0, array.length);
    }

    public static int bisect(double[] array, double x, int low, int high) {
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
}
