package MaxNum;

import java.util.*;

public class MaxNum {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int n = input.nextInt();
        MyInteger[] numbers = new MyInteger[n];
        for (int i = 0; i < n; ++i)
            numbers[i] = new MyInteger(input.nextInt());
        Arrays.sort(numbers, Collections.reverseOrder());
        for (int i = 0; i < numbers.length; ++i)
            System.out.print(numbers[i]);
    }
}

class MyInteger implements Comparable<MyInteger> {
    private int i;

    MyInteger(int i) {
        this.i = i;
    }

    public String toString() {
        return Integer.toString(i);
    }

    public int compareTo(MyInteger obj) {
        long a = (long) i;
        long b = (long) obj.i;
        int tmp = obj.i;
        for (; tmp > 0; tmp /= 10, a *= 10) ;
        tmp = i;
        for (; tmp > 0; tmp /= 10, b *= 10) ;
        a += obj.i;
        b += i;
        if (a == b)
            return 0;
        if (a > b)
            return 1;
        return -1;
    }
}