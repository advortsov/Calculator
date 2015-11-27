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
        switch (operator) { // скобок здесь нет, так как они обрабатываеются в evaluate
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
        switch (operator) { // исполняем арифметическую операцию с двумя последними элементами стека чисел
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
        NumberFormat formatter = NumberFormat.getNumberInstance(Locale.ENGLISH); // используем англ локаль, чтобы число разделялось точкой
        formatter.setMaximumFractionDigits(4); // максимум выводим 4 знака после точки
        formatter.setMinimumFractionDigits(0); // если их нет, то просто целую часть числа
        return formatter.format(num);
    }

    @Override
    public String evaluate(String statement) {
        LinkedList<Double> digits = new LinkedList<>(); // стек для чисел
        LinkedList<Character> operators = new LinkedList<>(); // стек для операторов
        statement = statement.replaceAll(" ", ""); // убираем пробелы в исходной строке
        try {
            for (int i = 0; i < statement.length(); i++) { // проходим по каждому символу введенной строки
                char c = statement.charAt(i);
                if (c == '(') {
                    operators.add('(');
                } else if (c == ')') {
                    while (operators.getLast() != '(') {
                        executeOperator(digits, operators.removeLast()); // когда получаем закрывающую скобку, мы имеем право посчитать все выражение в скобках
                    }
                    operators.removeLast(); // удаляем открывающую скобку (после подсчета содержимого скобок)
                } else if (isOperator(c)) {
                    while (!operators.isEmpty() && priority(operators.getLast()) >= priority(c)) { // пока приоритет последнего оператора >= приоритета текущего
                        executeOperator(digits, operators.removeLast()); // мы можем выполнять вычисления
                    }
                    operators.add(c);
                } else {
                    String operand = ""; // если мы попали в этот блок, то символ является полной цифрой либо ее началом (например 0.65)
                    while (i < statement.length() &&
                            (Character.isDigit(statement.charAt(i)) // обычным числом
                                    || (statement.charAt(i) == '.' && Character.isDigit(statement.charAt(i-1)) ) ) ) { // или вещественным числом
                        operand += statement.charAt(i++);
                    }
                    --i;
                    digits.add(Double.parseDouble(operand)); // добавляем всё число в стек
                }
            }
            while (!operators.isEmpty()) { // если у нас до сих пор остались операторы в стеке
                executeOperator(digits, operators.removeLast()); // выолняем оставшиеся
            }
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
        return toRound(digits.get(0)); // последнее число в стеке (или первое в списке) и есть ответ. Выводим его в формате условия задачи.
    }

}
