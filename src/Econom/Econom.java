package Econom;

import java.util.*;
import static java.lang.Character.isLetter;

public class Econom {
    public static void main(String[] args) {
        String sn = new Scanner(System.in).nextLine();
        String reverse = new StringBuffer(sn).reverse().toString();
        Map<String, Character> opMap = new HashMap<>();
        Stack<Character> opStack = new Stack<>();
        char count = 0;
        for (char i : reverse.toCharArray()) {
            if (i == ')' || i == '(');
            else {
                if (isLetter(i))
                    opStack.push(i);
                else {
                    char a = opStack.pop();
                    char b = opStack.pop();
                    String values = "" + i + a + b;
                    if (opMap.containsKey(values))
                        opStack.push(opMap.get(values));
                    else {
                        opMap.put(values, count);
                        opStack.push(count++);
                    }
                }
            }
        }
        System.out.println(opMap.size());
    }
}