package Hakaton;

import java.util.*;

public class Hakaton {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int num = in.nextInt();
        int[] result = new int[num];
        for (int i = 0; i < num; i++) {
            int n1 = Reverse(in.nextInt());
            int n2 = Reverse(in.nextInt());
            result[i] = Reverse(n1 + n2);
        }
        for (int i = 0; i < num; i++)
            System.out.println(result[i]);
    }

    private static int Reverse(int a) {
        int result = 0;
        for (int i = a; i != 0; i /= 10)
            result = result * 10 + i % 10;
        return result;
    }
}