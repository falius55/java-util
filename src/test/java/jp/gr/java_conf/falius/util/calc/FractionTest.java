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
}
