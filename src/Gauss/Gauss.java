package Gauss;

import java.util.Scanner;

class Rational implements Comparable<Rational> {
    private static int gcd(int a, int b) {
        return (b == 0) ? a : gcd(b, a % b);
    }

    Rational(int numerator) {
        this(numerator, 1);
    }

    private Rational(int numerator, int denominator) {
        if (0 == denominator)
            throw new IllegalArgumentException("Zero denominator");
        if (0 == numerator) {
            this.numerator = 0;
            this.denominator = 1;
        } else {
            int gcd_ = Rational.gcd(numerator, denominator);
            this.numerator = numerator / gcd_;
            this.denominator = denominator / gcd_;
            if (this.denominator < 0) {
                this.denominator = -this.denominator;
                this.numerator = -this.numerator;
            }
        }
    }

    void negate() {
        this.numerator *= -1;
    }

    public void invert() {
        if (0 == this.numerator)
            throw new UnsupportedOperationException("Inverting Zero");
        int tmp = this.denominator;
        this.denominator = this.numerator;
        this.numerator = tmp;
        if (this.denominator < 0) {
            this.denominator = -this.denominator;
            this.numerator = -this.numerator;
        }
    }

    Rational abs() {
        Rational cp = new Rational(this.numerator, this.denominator);
        if (this.numerator < 0)
            cp.negate();
        return cp;
    }

    public Rational add(Rational rha) {
        return new Rational(
                this.numerator * rha.denominator + rha.numerator * this.denominator, this.denominator * rha.denominator);
    }

    public Rational dif(Rational rha) {
        return new Rational(
                this.numerator * rha.denominator - rha.numerator * this.denominator, this.denominator * rha.denominator);
    }

    Rational mul(Rational rha) {
        return new Rational(
                this.numerator * rha.numerator, this.denominator * rha.denominator);
    }

    Rational div(Rational rha) {
        if (0 == rha.numerator)
            throw new UnsupportedOperationException("Inverting Zero");
        return new Rational(
                this.numerator * rha.denominator, this.denominator * rha.numerator);
    }

    @Override
    public int compareTo(Rational rha) {
        return Integer.compare(this.numerator * rha.denominator, rha.numerator * this.denominator);
    }

    @Override
    public String toString() {
        return this.numerator + "/" + this.denominator;
    }

    private int numerator, denominator;
}

public class Gauss {
    public static void main(String[] args) {
        Scanner sn = new Scanner(System.in);
        int n = sn.nextInt();
        Rational[][] mA = new Rational[n][n];
        Rational[] b = new Rational[n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++)
                mA[i][j] = new Rational(sn.nextInt());
            b[i] = new Rational(sn.nextInt());
        }
        for (int nn = n; nn > 1; --nn) {
            leader(mA, b, nn);
            if (0 != mA[n - nn][n - nn].compareTo(new Rational(0)))
                for (int k = 1; k < nn; k++)
                    reduceRow(mA, b, nn, k);
        }
        reverseRows(mA);
        reverseColumns(mA, b);
        for (int nn = n; nn > 1; --nn) {
            if (0 != mA[n - nn][n - nn].compareTo(new Rational(0)))
                for (int k = 1; k < nn; k++)
                    reduceRow(mA, b, nn, k);
        }
        boolean solution = true;
        Rational[] res = new Rational[n];
        for (int i = 0; i < n; i++)
            if (0 != mA[i][i].compareTo(new Rational(0)))
                res[n - i - 1] = b[i].div(mA[i][i]);
            else {
                solution = false;
                break;
            }
        if (!solution) {
            System.out.println("No solution");
            return;
        }
        for (int i = 0; i < n; i++)
            System.out.println(res[i]);
    }

    private static void leader(Rational[][] mA, Rational[] b, int nn) {
        int k = 0, N = mA.length;
        Rational max = mA[N - nn][N - nn].abs();
        for (int i = 1; i < nn; ++i)
            if (max.compareTo(mA[N - nn + i][N - nn].abs()) < 0) {
                max = mA[N - nn + i][N - nn].abs();
                k = i;
            }
        Rational[] tmp = mA[N - nn];
        mA[N - nn] = mA[N - nn + k];
        mA[N - nn + k] = tmp;
        Rational tmpB = b[N - nn];
        b[N - nn] = b[N - nn + k];
        b[N - nn + k] = tmpB;
    }

    private static void reduceRow(Rational[][] mA, Rational[] b, int nn, int k) {
        int N = mA.length;
        Rational a0 = mA[N - nn][N - nn];
        Rational b0 = mA[N - nn + k][N - nn];
        Rational coeff = b0.div(a0);
        coeff.negate();
        for (int i = N - nn; i < N; i++)
            mA[N - nn + k][i] = mA[N - nn + k][i].add(coeff.mul(mA[N - nn][i]));
        b[N - nn + k] = b[N - nn + k].add(coeff.mul(b[N - nn]));
    }

    private static void reverseRows(Rational[][] mA) {
        int N = mA.length;
        for (int k = 0; k < N; k++)
            for (int i = 0, j = N - 1; i < j; ++i, --j) {
                Rational tmp = mA[k][i];
                mA[k][i] = mA[k][j];
                mA[k][j] = tmp;
            }
    }

    private static void reverseColumns(Rational[][] mA, Rational[] b) {
        int N = mA.length;
        for (int i = 0, j = N - 1; i < j; ++i, --j) {
            Rational[] tmp = mA[i];
            mA[i] = mA[j];
            mA[j] = tmp;
            Rational tmpB = b[i];
            b[i] = b[j];
            b[j] = tmpB;
        }
    }
}