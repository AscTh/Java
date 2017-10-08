package Kth;

public class Kth {
    public static void main(String[] args) {
        long l = 9, i = 1;
        long k = new java.util.Scanner(System.in).nextLong() + 1 - l * i;

        for (; k > 0; l *= 10, ++i, k -= l * i) ;
        if (0 == k) {
            System.out.println("9");
            return;
        }
        k = -k;
        System.out.println(take_kdigit(max_with_kdigits(i) - k / i, k % i));
    }

    private static long take_kdigit(long l, long k) {
        for (int i = 0; i < k; ++i)
            l /= 10;
        return l % 10;
    }

    private static long max_with_kdigits(long k) {
        long res = 0, p = 1;
        for (int i = 0; i < k; ++i, p *= 10)
            res += 9 * p;
        return res;
    }
}