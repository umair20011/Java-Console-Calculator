import java.util.*;

public class Application {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<String> tokens = new ArrayList<>();

        System.out.println("Enter number/operator one by one. Press Enter on empty line to calculate:");

        while (true) {
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                break;
            }

            if (input.matches("-?\\d+(\\.\\d+)?") || input.matches("[+\\-*/]")) {
                tokens.add(input);
            } else {
                System.out.println("Invalid input. Enter a number or operator (+ - * /).");
            }
        }

        try {
            double result = calculate(tokens);
            System.out.println("Result: " + result);
        } catch (Exception e) {
            System.out.println("Error: Invalid expression. " + e.getMessage());
        }

        scanner.close();
    }

    private static double calculate(List<String> tokens) {
        Stack<Double> numbers = new Stack<>();
        Stack<String> operators = new Stack<>();

        for (String token : tokens) {
            if (token.matches("-?\\d+(\\.\\d+)?")) {
                numbers.push(Double.parseDouble(token));
            } else if (token.matches("[+\\-*/]")) {
                while (!operators.isEmpty() && preference(token, operators.peek())) {
                    double b = numbers.pop();
                    double a = numbers.pop();
                    String op = operators.pop();
                    numbers.push(calculation(a, b, op));
                }
                operators.push(token);
            }
        }

        while (!operators.isEmpty()) {
            double b = numbers.pop();
            double a = numbers.pop();
            String op = operators.pop();
            numbers.push(calculation(a, b, op));
        }

        return numbers.pop();
    }

    private static boolean preference(String op1, String op2) {
        if ((op1.equals("*") || op1.equals("/")) && (op2.equals("+") || op2.equals("-"))) {
            return false;
        }
        return true;
    }

    private static double calculation(double a, double b, String op) {
        return switch (op) {
            case "+" -> a + b;
            case "-" -> a - b;
            case "*" -> a * b;
            case "/" -> {
                if (b == 0) throw new ArithmeticException("Division by zero.");
                yield a / b;
            }
            default -> throw new IllegalArgumentException("Unknown operator: " + op);
        };
    }
}
