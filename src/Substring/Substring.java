package Substring;

import org.jetbrains.annotations.NotNull;
import java.util.Iterator;
import java.util.Scanner;

public class Substring implements Iterable<String> {
    private StringBuilder s;

    Substring(String s) {
        this(new StringBuilder(s));
    }

    private Substring(StringBuilder s) {
        this.s = s;
    }

    private class SubstringIterator implements Iterator<String> {
        private int r, l;

        SubstringIterator() {
            l = 0;
            r = l + 1;
        }

        public boolean hasNext() {
            return l < s.length();
        }

        public String next() {
            String result = s.substring(l, r++);
            if (r > s.length())
                r = ++l + 1;
            return result;
        }
    }

    public Iterator<String> iterator() {
        return new SubstringIterator();
    }
}

class Test {
    public static void main(String[] args) {
        Substring sb = new Substring(new Scanner(System.in).nextLine());
        for (String s : sb)
            System.out.println(s);
    }
}