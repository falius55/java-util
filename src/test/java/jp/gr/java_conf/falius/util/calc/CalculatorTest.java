package jp.gr.java_conf.falius.util.calc;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.junit.Test;

public class CalculatorTest {

    @Test
    public void sumTest() {
        int start = 1;
        int end = 10;
        int result = Calculator.sum(start, end);
        assertThat(result, is(55));
    }

    @Test
    public void sumZeroTest() {
        int start = 0;
        int end = 10;
        int result = Calculator.sum(start, end);
        assertThat(result, is(55));
    }

    @Test
    public void sumZeroTest2() {
        int start = 10;
        int end = 0;
        int result = Calculator.sum(start, end);
        assertThat(result, is(55));
    }

    @Test
    public void sumMinusToPlusTest() {
        int start = -5;
        int end = 10;

        int result = Calculator.sum(start, end);
        assertThat(result, is(40));
    }

    @Test
    public void sumMinusToMinusTest() {
        int start = -5;
        int end = -1;
        int result = Calculator.sum(start, end);
        assertThat(result, is(-15));
    }

    @Test
    public void sumBiggerTest() {
        int start = 1;
        int end = 10000;
        int result = Calculator.sum(start, end);
        assertThat(result, is(10001 * 5000));
    }

    @Test
    public void sumReverseTest() {
        int start = 10;
        int end = 0;
        int result = Calculator.sum(start, end);
        assertThat(result, is(55));
    }

    @Test
    public void sumReverseTest2() {
        int start = 3;
        int end = -4;
        int result = Calculator.sum(start, end);
        assertThat(result, is(-4));
    }

    @Test
    public void averageTest() {
        int result = Calculator.average(20, 10, 40, 70, 30);
        assertThat(result, is(34));
    }

    @Test
    public void averageTest2() {
        int result = Calculator.average(20, 10, 40, 70);
        assertThat(result, is((20 + 10 + 40 + 70) / 4));
    }

    @Test
    public void averageTest3() {
        int result = Calculator.average(100);
        assertThat(result, is(100));
    }

    @Test
    public void averageTest4() {
        int result = Calculator.average(-20, -10, -50, 0);
        assertThat(result, is(-20));
    }

    @Test
    public void averageTest5() {
        int result = Calculator.average(-20, 10, 50, 0, -45, 30);
        assertThat(result, is(25 / 6));
    }

    @Test
    public void permutationTest() {
        int n = 5;
        int r = 3;

        int result = Calculator.permutation(n, r);
        assertThat(result, is(5 * 4 * 3));
    }

    @Test
    public void permutationTest2() {
        int n = 5;
        int r = 1;
        int result = Calculator.permutation(n, r);
        assertThat(result, is(5));
    }

    @Test
    public void permutationTest3() {
        int n = 5;
        int r = 5;
        int result = Calculator.permutation(n, r);
        assertThat(result, is(5 * 4 * 3 * 2 * 1));
    }

    @Test
    public void permutationTest4() {
        int n = 5;
        int r = 0;
        int result = Calculator.permutation(n, r);
        assertThat(result, is(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void permutationExceptionTest() {
        int n = -2;
        int r = 4;
        int result = Calculator.permutation(n, r);
        assertThat(result, is(10));
    }

    @Test(expected = IllegalArgumentException.class)
    public void permutationExceptionTest2() {
        int n = 2;
        int r = 4;
        int result = Calculator.permutation(n, r);
        assertThat(result, is(10));
    }

    @Test
    public void factorialTest() {
        int n = 5;
        int result = Calculator.factorial(n);
        assertThat(result, is(5 * 4 * 3 * 2 * 1));
    }

    @Test
    public void factorialTest2() {
        int n = 0;
        int result = Calculator.factorial(n);
        assertThat(result, is(1));
    }

    @Test
    public void factorialTest3() {
        int n = 1;
        int result = Calculator.factorial(n);
        assertThat(result, is(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void factorialExceptionTest() {
        int n = -3;
        int result = Calculator.factorial(n);
        assertThat(result, is( -3 * -2 * -1));
    }

    @Test
    public void conbinationTest() {
        int n = 5;
        int r = 3;
        int result = Calculator.conbination(n, r);
        assertThat(result, is(10));
    }

    @Test
    public void conbinationTest2() {
        int n = 5;
        int r = 2;
        int result = Calculator.conbination(n, r);
        assertThat(result, is(10));
    }

    @Test
    public void conbinationTest3() {
        int n = 10;
        int r = 10;
        int result = Calculator.conbination(n, r);
        assertThat(result, is(1));
    }

    @Test
    public void conbinationTest4() {
        int n = 10;
        int r = 1;
        int result = Calculator.conbination(n, r);
        assertThat(result, is(10));
    }

    @Test(expected = IllegalArgumentException.class)
    public void conbinationExceptionTest() {
        int n = -5;
        int r = 2;
        int result = Calculator.conbination(n, r);
        assertThat(result, is(10));
    }

    @Test(expected = IllegalArgumentException.class)
    public void conbinationExceptionTest2() {
        int n = 5;
        int r = -2;
        int result = Calculator.conbination(n, r);
        assertThat(result, is(10));
    }
}
