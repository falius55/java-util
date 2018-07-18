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
        assertThat(frac.toString(), is("10"));
    }

    @Test(expected = ArithmeticException.class)
    public void newInstanceTest6() {
        int numerator = 30;
        int denominator = 0; // ゼロ除算
        Fraction frac = Fraction.newInstance(numerator, denominator);
        assertThat(frac.toString(), is("10"));
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
    public void isProperTest() {
        {
            int numerator = 4;
            int denominator = -12;
            Fraction frac = Fraction.newInstance(numerator, denominator);
            assertThat(frac.isProper(), is(true));
        }

        {
            int numerator = 13;
            int denominator = 2;
            Fraction frac = Fraction.newInstance(numerator, denominator);
            assertThat(frac.isProper(), is(false));
        }
    }

    @Test
    public void isImproperTest() {
        {
            int numerator = 7;
            int denominator = 2;
            Fraction frac = Fraction.newInstance(numerator, denominator);
            assertThat(frac.isImproper(), is(true));
        }

        {
            int numerator = 3;
            int denominator = 8;
            Fraction frac = Fraction.newInstance(numerator, denominator);
            assertThat(frac.isImproper(), is(false));
        }
    }

    @Test
    public void isIntTest() {
        {
            int numerator = 18;
            int denominator = 6;
            Fraction frac = Fraction.newInstance(numerator, denominator);
            assertThat(frac.isInt(), is(true));
        }

        {
            int numerator = 3;
            int denominator = 8;
            Fraction frac = Fraction.newInstance(numerator, denominator);
            assertThat(frac.isInt(), is(false));
        }

        {
            int numerator = 0;
            int denominator = 8;
            Fraction frac = Fraction.newInstance(numerator, denominator);
            assertThat(frac.isInt(), is(true));
        }
    }

    @Test
    public void isUnitTest() {
        {
            int numerator = 1;
            int denominator = 5;
            Fraction frac = Fraction.newInstance(numerator, denominator);
            assertThat(frac.isUnit(), is(true));
        }

        {
            int numerator = 3;
            int denominator = 8;
            Fraction frac = Fraction.newInstance(numerator, denominator);
            assertThat(frac.isUnit(), is(false));
        }
    }

    @Test
    public void toIntValueTest() {
        int numerator = 90;
        int denominator = 30;
        Fraction frac = Fraction.newInstance(numerator, denominator);
        assertThat(frac.intValue(), is(3));
    }

    @Test
    public void toIntValueTest2() {
        int numerator = 12;
        int denominator = 5;
        Fraction frac = Fraction.newInstance(numerator, denominator);
        assertThat(frac.intValue(), is(2));
    }

    @Test
    public void toProperTest() {
        int numerator = 12;
        int denominator = 5;
        Fraction frac = Fraction.newInstance(numerator, denominator);
        Fraction prop = frac.toProper();
        assertThat(prop.toString(), is("2 / 5"));
    }

    @Test
    public void toDoubleValueTest() {
        int numerator = 2;
        int denominator = 5;
        Fraction frac = Fraction.newInstance(numerator, denominator);
        assertThat(frac.doubleValue(), is(0.4d));
    }

    @Test
    public void toDoubleValueTest2() {
        int numerator = 7;
        int denominator = 5;
        Fraction frac = Fraction.newInstance(numerator, denominator);
        assertThat(frac.doubleValue(), is(1.4d));
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

    @Test
    public void multiplyIntTest() {
        int numerator = 27;
        int denominator = 12;
        Fraction frac = Fraction.newInstance(numerator, denominator);
        Fraction result = frac.multiply(2);
        assertThat(result.toString(), is("9 / 2"));
    }

    @Test
    public void multiplyFractionTest() {
        int numerator = 78;
        int denominator = 34;
        Fraction frac = Fraction.newInstance(numerator, denominator);
        Fraction val = Fraction.newInstance(5, 11);
        Fraction result = frac.multiply(val);
        assertThat(result.toString(), is("195 / 187"));
    }

    @Test
    public void multiplyFractionTest2() {
        int numerator = 78;
        int denominator = -34;
        Fraction frac = Fraction.newInstance(numerator, denominator);
        Fraction val = Fraction.newInstance(5, 11);
        Fraction result = frac.multiply(val);
        assertThat(result.toString(), is("-195 / 187"));
    }

    @Test
    public void devideIntTest() {
        int numerator = 27;
        int denominator = 12;
        Fraction frac = Fraction.newInstance(numerator, denominator);
        Fraction result = frac.devide(2);
        assertThat(result.toString(), is("9 / 8"));
    }

    @Test
    public void devideFractionTest() {
        int numerator = 78;
        int denominator = 34;
        Fraction frac = Fraction.newInstance(numerator, denominator);
        Fraction val = Fraction.newInstance(5, 11);
        Fraction result = frac.devide(val);
        assertThat(result.toString(), is("429 / 85"));
    }

    @Test
    public void devideFractionTest2() {
        int numerator = 3;
        int denominator = 4;
        Fraction frac = Fraction.newInstance(numerator, denominator);
        Fraction val = Fraction.newInstance(1, -2);
        Fraction result = frac.devide(val);
        assertThat(result.toString(), is("-3 / 2"));
    }

    @Test
    public void devideFractionTest3() {
        int numerator = -3;
        int denominator = 4;
        Fraction frac = Fraction.newInstance(numerator, denominator);
        Fraction val = Fraction.newInstance(1, -2);
        Fraction result = frac.devide(val);
        assertThat(result.toString(), is("3 / 2"));
    }
}
