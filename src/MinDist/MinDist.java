package MinDist;

import java.util.Scanner;


public class MinDist {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String s = in.nextLine();
        char x = in.next().charAt(0), y = in.next().charAt(0);
        System.out.println(new MinDist(x, y, s).run());
    }

    private MinDist(char x, char y, String in) {
        this.x = x;
        this.y = y;
        this.in = in;
    }

    private int run() {
        pos = x_pos = y_pos = 0;
        state = State.START;
        min_dist = in.length();
        for (int i = 0; i < in.length(); ++i) advance(in.charAt(i));
        return min_dist;
    }

    private enum State {
        START, X, Y
    }

    private final char x, y;
    private final String in;
    private State state;
    private int pos, x_pos, y_pos, min_dist;

    private void advance(char c) {
        switch (state) {
            case START:
                if (c == x) {
                    x_pos = pos;
                    state = State.X;
                } else if (c == y) {
                    y_pos = pos;
                    state = State.Y;
                }
                break;
            case X:
                if (c == x) {
                    x_pos = pos;
                } else if (c == y) {
                    y_pos = pos;
                    state = State.Y;
                    min_dist = Math.min(y_pos - x_pos - 1, min_dist);
                }
                break;
            case Y:
                if (c == y) {
                    y_pos = pos;
                } else if (c == x) {
                    x_pos = pos;
                    state = State.X;
                    min_dist = Math.min(x_pos - y_pos - 1, min_dist);
                }
                break;
        }
        ++pos;
    }
}