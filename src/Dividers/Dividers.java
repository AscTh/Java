package Dividers;

import java.util.*;

public class Dividers {
    public static void main(String[] args) {
        long n = new Scanner(System.in).nextLong();
        ArrayList<Long> arr = new ArrayList<>();
        for (long i = 1; i * i <= n; i++)
            if (n % i == 0)
                arr.add(i);
        for (long i = arr.size() - 1; i >= 0; i--) {
            long p = n / arr.get((int) i);
            if (p != arr.get((int) i)) arr.add(p);
        }
        System.out.println("graph {");
        for (long x : arr)
            System.out.println("    " + x);
        for (int i = 0; i < arr.size(); i++) {
            J:
            for (int j = i + 1; j < arr.size(); j++)
                if (arr.get(j) % arr.get(i) == 0) {
                    for (int k = i + 1; k < j; k++)
                        if (arr.get(j) % arr.get(k) == 0 && arr.get(k) % arr.get(i) == 0)
                            continue J;
                    System.out.println(arr.get(j) + " -- " + arr.get(i));
                }
        }
        System.out.println("}");
    }
}