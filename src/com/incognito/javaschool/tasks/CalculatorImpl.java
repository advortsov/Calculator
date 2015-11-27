package com.incognito.javaschool.tasks;

import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.Locale;

/**
 * It's an implementation of calculate process uses two different stacks for numbers and operators
 *
 * @author  Alexander Dvortsov
 * @version 1.0
 * @since   2015-10-22
 */
public class CalculatorImpl implements Calculator {

    public CalculatorImpl() {
    }

    /**
     * Checks is this character is a operator sign
     *
     * @param c current character
     * @return true, if character is an operator sign
     */
    private static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    /**
     * Returns operator's priority level
     *
     * @param operator is a operator for rate
     * @return the number of priority level
     */
    private static int priority(char operator) {
        switch (operator) { // ������ ����� ���, ��� ��� ��� ��������������� � evaluate
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            default:
                return -1;
        }
    }

    /**
     * Performs one of supported arithmetic actions for pair of last elements in the stack
     *
     * @param stack it's a stack of statement's digits
     * @param operator it's a one of supported arithmetic operator
     */
    private static void executeOperator(LinkedList<Double> stack, char operator) {
        double last = stack.removeLast();
        double penult = stack.removeLast();
        switch (operator) { // ��������� �������������� �������� � ����� ���������� ���������� ����� �����
            case '+':
                stack.add(penult + last);
                break;
            case '-':
                stack.add(penult - last);
                break;
            case '*':
                stack.add(penult * last);
                break;
            case '/':
                stack.add(penult / last);
                break;
        }
    }

    /**
     * Returns formatted value
     *
     * @param num it is a double for change quantity of shown digits after delimiter
     * @return the formatted value as string
     */
    private static String toRound(double num){
        NumberFormat formatter = NumberFormat.getNumberInstance(Locale.ENGLISH); // ���������� ���� ������, ����� ����� ����������� ������
        formatter.setMaximumFractionDigits(4); // �������� ������� 4 ����� ����� �����
        formatter.setMinimumFractionDigits(0); // ���� �� ���, �� ������ ����� ����� �����
        return formatter.format(num);
    }

    @Override
    public String evaluate(String statement) {
        LinkedList<Double> digits = new LinkedList<>(); // ���� ��� �����
        LinkedList<Character> operators = new LinkedList<>(); // ���� ��� ����������
        statement = statement.replaceAll(" ", ""); // ������� ������� � �������� ������
        try {
            for (int i = 0; i < statement.length(); i++) { // �������� �� ������� ������� ��������� ������
                char c = statement.charAt(i);
                if (c == '(') {
                    operators.add('(');
                } else if (c == ')') {
                    while (operators.getLast() != '(') {
                        executeOperator(digits, operators.removeLast()); // ����� �������� ����������� ������, �� ����� ����� ��������� ��� ��������� � �������
                    }
                    operators.removeLast(); // ������� ����������� ������ (����� �������� ����������� ������)
                } else if (isOperator(c)) {
                    while (!operators.isEmpty() && priority(operators.getLast()) >= priority(c)) { // ���� ��������� ���������� ��������� >= ���������� ��������
                        executeOperator(digits, operators.removeLast()); // �� ����� ��������� ����������
                    }
                    operators.add(c);
                } else {
                    String operand = ""; // ���� �� ������ � ���� ����, �� ������ �������� ������ ������ ���� �� ������� (�������� 0.65)
                    while (i < statement.length() &&
                            (Character.isDigit(statement.charAt(i)) // ������� ������
                                    || (statement.charAt(i) == '.' && Character.isDigit(statement.charAt(i-1)) ) ) ) { // ��� ������������ ������
                        operand += statement.charAt(i++);
                    }
                    --i;
                    digits.add(Double.parseDouble(operand)); // ��������� �� ����� � ����
                }
            }
            while (!operators.isEmpty()) { // ���� � ��� �� ��� ��� �������� ��������� � �����
                executeOperator(digits, operators.removeLast()); // �������� ����������
            }
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
        return toRound(digits.get(0)); // ��������� ����� � ����� (��� ������ � ������) � ���� �����. ������� ��� � ������� ������� ������.
    }

}
