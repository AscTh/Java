package Stack;

import java.util.Arrays;

public class Stack {
    private int[] stackArray = new int[4];
    private int top;

    public void push(int v) {
        if (top == stackArray.length)
            stackArray = Arrays.copyOf(stackArray, top * 2);
        stackArray[top++] = v;
    }

    private boolean empty() {
        return top == 0;
    }

    public int pop() {
        if (empty())
            throw new java.util.EmptyStackException();
        return stackArray[--top];
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < top; i++) {
            s.append(stackArray[i]).append(" ");
        }
        return s.toString();
    }
}