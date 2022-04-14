package data;

import io.reactivex.annotations.NonNull;

import java.util.*;

public class Equality {

    private static final int MAX_PARAMS_COUNT = 6;
    private static final int MAX_OPERATORS_COUNT = 5;

    private final int[] params;
    private final Character[] operators;
    private static final Random random = new Random();

    private Equality(@NonNull int[] params, @NonNull Character[] operators) {
        this.params = Arrays.stream(params).limit(MAX_PARAMS_COUNT).toArray();
        this.operators = Arrays.stream(operators).limit(MAX_OPERATORS_COUNT).toArray(value -> new Character[operators.length]);
    }

    public double calculate() {
        if (params.length <= 0) {
            throw new IllegalStateException("Params must not be empty");
        }
        if (operators.length != params.length - 1) {
            throw new IllegalStateException("Operators' length mush equal params.length - 1");
        }
        List<String> postfix = alignToPrefix();
        Stack<String> calculator = new Stack<>();
        postfix.forEach(s -> {
            switch (s) {
                case "+": {
                    double right = Double.parseDouble(calculator.pop());
                    double left = Double.parseDouble(calculator.pop());
                    calculator.push(String.valueOf(left + right));
                    break;
                }
                case "-": {
                    double right = Double.parseDouble(calculator.pop());
                    double left = Double.parseDouble(calculator.pop());
                    calculator.push(String.valueOf(left - right));
                    break;
                }
                case "*": {
                    double right = Double.parseDouble(calculator.pop());
                    double left = Double.parseDouble(calculator.pop());
                    calculator.push(String.valueOf(left * right));
                    break;
                }
                case "/": {
                    double right = Double.parseDouble(calculator.pop());
                    double left = Double.parseDouble(calculator.pop());
                    calculator.push(String.valueOf(left / right));
                    break;
                }
                default:
                    calculator.push(s);
                    break;
            }
        });
        return Double.parseDouble(calculator.pop());
    }

    public int[] getParams() {
        return params;
    }

    public Character[] getOperators() {
        return operators;
    }

    public int getParamsSize() {
        return params.length;
    }

    public int getOperatorsSize() {
        return operators.length;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < params.length; i++) {
            if (i != 0) builder.append(operators[i - 1]);
            builder.append(params[i]);
        }
        return builder.toString();
    }

    private List<String> alignToPrefix() {
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
        return postfix;
    }

    private int isBiggerThan(char left, char right) {
        if (((left == '+' || left == '-') && (right == '+' || right == '-')) || ((left == '*' || left == '/') && (right == '*' || right == '/'))) {
            return 0;
        } else if ((left == '*' || left == '/') && (right == '+' || right == '-')) {
            return 1;
        } else return -1;
    }

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
