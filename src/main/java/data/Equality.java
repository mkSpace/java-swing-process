package data;

import extensions.Print;
import io.reactivex.annotations.NonNull;

import java.util.*;
import java.util.function.Consumer;

public class Equality {

    private static final int MAX_PARAMS_COUNT = 6;
    private static final int MAX_OPERATORS_COUNT = 5;

    private final int[] params;
    private final Character[] operators;

    private Equality(@NonNull int[] params, @NonNull Character[] operators) {
        Print.println("params.size : " + params.length + " operators.size : " + operators.length);
        this.params = Arrays.stream(params).limit(MAX_PARAMS_COUNT).toArray();
        this.operators = Arrays.stream(operators).limit(MAX_OPERATORS_COUNT).toArray(value -> new Character[operators.length]);
        Print.println("params.size : " + this.params.length + " operators.size : " + this.operators.length);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < params.length; i++) {
            if (i != 0) {
                builder.append(operators[i - 1]);
            }
            builder.append(params[i]);
        }
        return builder.toString();
    }

    public double calculate() {
        if (params.length <= 0) {
            throw new IllegalStateException("Params must not be empty");
        }
        if (operators.length != params.length - 1) {
            throw new IllegalStateException("Operators' length mush equal params.length - 1");
        }
        // 후위 표기법으로 변경
        List<String> postfix = new ArrayList<>();
        Stack<Character> tempOperators = new Stack<>();
        for (int i = 0; i < params.length; i++) {
            if (i > 0) {
                if (tempOperators.isEmpty()) {
                    tempOperators.push(operators[i - 1]);
                } else {
                    char last = tempOperators.lastElement();
                    int isBig = isBiggerThan(operators[i - 1], last);
                    if (isBig == 1) {
                        tempOperators.push(operators[i - 1]);
                    } else {
                        postfix.add(String.valueOf(tempOperators.pop()));
                        tempOperators.push(operators[i - 1]);
                    }
                }
            }
            postfix.add(String.valueOf(params[i]));
        }
        tempOperators.sort(this::isBiggerThan);
        while (!tempOperators.isEmpty()) postfix.add(String.valueOf(tempOperators.pop()));
        postfix.forEach(new Consumer<String>() {
            @Override
            public void accept(String s) {
                Print.print(s);
            }
        });
        Print.println("");
        Stack<String> calculator = new Stack<>();
        postfix.forEach(s -> {
            if (s.equals("+")) {
                double right = Double.parseDouble(calculator.pop());
                double left = Double.parseDouble(calculator.pop());
                calculator.push(String.valueOf(left + right));
            } else if (s.equals("-")) {
                double right = Double.parseDouble(calculator.pop());
                double left = Double.parseDouble(calculator.pop());
                calculator.push(String.valueOf(left - right));
            } else if (s.equals("*")) {
                double right = Double.parseDouble(calculator.pop());
                double left = Double.parseDouble(calculator.pop());
                calculator.push(String.valueOf(left * right));
            } else if (s.equals("/")) {
                double right = Double.parseDouble(calculator.pop());
                double left = Double.parseDouble(calculator.pop());
                calculator.push(String.valueOf(left / right));
            } else {
                calculator.push(s);
            }
        });
        return Double.parseDouble(calculator.pop());
    }

    private int isBiggerThan(char left, char right) {
        if (((left == '+' || left == '-') && (right == '+' || right == '-')) || ((left == '*' || left == '/') && (right == '*' || right == '/'))) {
            return 0;
        } else if ((left == '*' || left == '/') && (right == '+' || right == '-')) {
            return 1;
        } else return -1;
    }

    private static final Random random = new Random();

    public static Equality create() {
        int paramSize = Math.abs(random.nextInt()) % 4 + 3;
        int[] params = new int[paramSize];
        Character[] operators = new Character[paramSize - 1];
        for (int i = 0; i < paramSize; i++) {
            params[i] = random.nextInt() % 100;
        }
        for (int i = 0; i < paramSize - 1; i++) {
            int rand = random.nextInt() % 4;
            char operator;
            if (rand == 0) {
                operator = '+';
            } else if (rand == 1) {
                operator = '-';
            } else if (rand == 2) {
                operator = '*';
            } else {
                operator = '/';
            }
            operators[i] = operator;
        }
        return new Equality(params, operators);
    }
}
