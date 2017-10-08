package Lab;

import java.util.Arrays;

public class Polygon implements Comparable<Polygon> {
    private Line[] arr;

    @Override
    public int compareTo(Polygon o) {
        return Double.compare(o.arr[0].length, arr[0].length);
    }

    private class Line implements Comparable<Line> {
        int x1, x2, y1, y2;
        double length;
        Line(int _x1, int _y1, int _x2, int _y2) {
            x1 = _x1;
            x2 = _x2;
            y1 = _y1;
            y2 = _y2;
            length = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
        }

        @Override
        public int compareTo(Line o) {
            return Double.compare(o.length, length);
        }
    }

    private Polygon(int n, int[] Array) {
        int nel = Array.length;
        arr = new Line[nel];
        int x = Array[0], y = Array[1];
        for (int i = 1; i < nel; i++) {
            int x_2 = Array[i * 2], y_2 = Array[i * 2 + 1];
            arr[i - 1] = new Line(x, y, x_2, y_2);
            x = x_2;
            y = y_2;
        }
        arr[nel - 1] = new Line(Array[0], Array[1], x, y);

        Arrays.sort(arr);
    }

    @Override
    public String toString() {
        return "Length = " + arr[0].length + " [" + arr[0].x1 + "; " + arr[0].y1 + "] [" + arr[0].x2 + "; " + arr[0].y2 + "]";
    }

    public static void main(String[] args) {
        int[] f1 = {0, 0, 0, 1, 1, 0},
                f2 = {0, 0, 0, 1, 1, 1, 1, 0},
                f3 = {0, 0, 0, 1, 1, 2, 50, 50, 1, 0};
        Polygon[] a = {new Polygon(3, f1),
                new Polygon(4, f2),
                new Polygon(5, f3)};
        Arrays.sort(a);
        for (Polygon i : a)
            System.out.println(i);
    }
}