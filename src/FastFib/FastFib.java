package FastFib;

import java.math.BigInteger;
import java.util.Scanner;
import java.util.Stack;

interface Operation {
    BigInteger[] calc(BigInteger[] m);
}

class Square implements Operation {
    Square() {
    }

    public BigInteger[] calc(BigInteger[] m) {
        BigInteger[] result = new BigInteger[4];
        BigInteger tmpA = m[0].add(m[3]);
        BigInteger tmpB = m[1].multiply(m[2]);
        result[0] = m[0].multiply(m[0]).add(tmpB);
        result[1] = m[1].multiply(tmpA);
        result[2] = m[2].multiply(tmpA);
        result[3] = m[3].multiply(m[3]).add(tmpB);
        return result;
    }
}

class Next implements Operation {
    Next() {
    }

    public BigInteger[] calc(BigInteger[] m) {
        BigInteger[] result = new BigInteger[4];
        result[0] = m[0].add(m[1]);
        result[1] = m[0];
        result[2] = m[2].add(m[3]);
        result[3] = m[2];
        return result;
    }
}

public class FastFib {
    public static void main(String[] args) {
        int p = new Scanner(System.in).nextInt();
        if (p < 3) {
            System.out.println("1");
            return;
        }
        BigInteger[] init = new BigInteger[4];
        init[0] = BigInteger.ONE;
        init[1] = BigInteger.ONE;
        init[2] = BigInteger.ONE;
        init[3] = BigInteger.ZERO;
        BigInteger[] result = pow(init, p - 2);
        System.out.println(result[0].add(result[1]).toString());
    }

    private static BigInteger[] pow(BigInteger[] m, int p) {
        Stack<Operation> opStack = new Stack<Operation>();
        while (p > 1) {
            if (1 == p % 2)
                opStack.add(new Next());
            opStack.add(new Square());
            p /= 2;
        }
        BigInteger[] res = m;
        while (!opStack.empty())
            res = opStack.pop().calc(res);
        return res;
    }
}