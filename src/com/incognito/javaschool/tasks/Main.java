package com.incognito.javaschool.tasks;

/**
 * The Calculator program implements an application which
 * calculates input string.
 *
 * @author  Alexander Dvortsov
 * @version 1.0
 * @since   2015-10-22
 */
public class Main {
    public static void main(String[] args) {
        Calculator c = new CalculatorImpl();

        System.out.println(c.evaluate("(1+38)*4-5")); // Результат: 151
        System.out.println(c.evaluate("7*6/2+8")); // Результат: 29

        System.out.println(c.evaluate("1.12349+((3*4*5 - 30)-(10.6/2+4.7))")); // Результат: 21.1235
        System.out.println(c.evaluate("(3-3*4/2 - 1 * 3)")); // Результат: -6
        System.out.println(c.evaluate("851.3+1")); // Результат: 852.3
    }
}
