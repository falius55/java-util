package jp.gr.java_conf.falius.util.calc;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.junit.Test;

public class FractionTest {

    @Test
    public void newInstanceTest() {
        int numerator = 10;
        int denominator = 20;
        Fraction frac = Fraction.newInstance(numerator, denominator);
        assertThat(frac.toString(), is("1 / 2"));
    }

    @Test
    public void newInstanceTest2() {
        int numerator = 13;
        int denominator = 26;
        Fraction frac = Fraction.newInstance(numerator, denominator);
        assertThat(frac.toString(), is("1 / 2"));
    }

    @Test
    public void newInstanceTest3() {
        int numerator = 3;
        int denominator = 11;
        Fraction frac = Fraction.newInstance(numerator, denominator);
        assertThat(frac.toString(), is("3 / 11"));
    }

    @Test
    public void newInstanceTest4() {
        int numerator = 24;
        int denominator = 30;
        Fraction frac = Fraction.newInstance(numerator, denominator);
        assertThat(frac.toString(), is("4 / 5"));
    }

    @Test
    public void newInstanceTest5() {
        int numerator = 30;
        int denominator = 3;
        Fraction frac = Fraction.newInstance(numerator, denominator);
        assertThat(frac.toString(), is("10 / 1"));
    }

    @Test(expected = ArithmeticException.class)
    public void newInstanceTest6() {
        int numerator = 30;
        int denominator = 0;  // ゼロ除算
        Fraction frac = Fraction.newInstance(numerator, denominator);
        assertThat(frac.toString(), is("10 / 1"));
    }

    @Test
    public void newInstanceTest7() {
        int numerator = -4;
        int denominator = 10;
        Fraction frac = Fraction.newInstance(numerator, denominator);
        assertThat(frac.toString(), is("-2 / 5"));
    }

    @Test
    public void newInstanceTest8() {
        int numerator = 4;
        int denominator = -12;
        Fraction frac = Fraction.newInstance(numerator, denominator);
        assertThat(frac.toString(), is("-1 / 3"));
    }

    @Test
    public void plusIntTest() {
        int numerator = 1;
        int denominator = 2;
        Fraction frac = Fraction.newInstance(numerator, denominator);
        Fraction result = frac.plus(2);
        assertThat(result.toString(), is("5 / 2"));
    }

    @Test
    public void plusFractionTest() {
        int numerator = 1;
        int denominator = 2;
        Fraction frac = Fraction.newInstance(numerator, denominator);
        Fraction val = Fraction.newInstance(2, 3);
        Fraction result = frac.plus(val);
        assertThat(result.toString(), is("7 / 6"));
    }

    @Test
    public void minusIntTest() {
        int numerator = 1;
        int denominator = 2;
        Fraction frac = Fraction.newInstance(numerator, denominator);
        Fraction result = frac.minus(2);
        assertThat(result.toString(), is("-3 / 2"));
    }

    @Test
    public void minusIntTest2() {
        int numerator = 27;
        int denominator = 12;
        Fraction frac = Fraction.newInstance(numerator, denominator);
        Fraction result = frac.minus(2);
        assertThat(result.toString(), is("1 / 4"));
    }

    @Test
    public void minusFractionTest() {
        int numerator = 1;
        int denominator = 2;
        Fraction frac = Fraction.newInstance(numerator, denominator);
        Fraction val = Fraction.newInstance(2, 3);
        Fraction result = frac.minus(val);
        assertThat(result.toString(), is("-1 / 6"));
    }

    @Test
    public void minusFractionTest2() {
        int numerator = 78;
        int denominator = 34;
        Fraction frac = Fraction.newInstance(numerator, denominator);
        Fraction val = Fraction.newInstance(5, 11);
        Fraction result = frac.minus(val);
        assertThat(result.toString(), is("344 / 187"));
    }
}
