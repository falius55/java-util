package jp.gr.java_conf.falius.util.calc;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.junit.Test;

public class DivisorTest {

    @Test
    public void gretestCommonFactorTest() {
        int x = 24;
        int y = 30;

        Divisor div = Divisor.newInstance(x, y);
        int result = div.gretestCommonFactor();

        assertThat(result, is(6));
    }

    @Test
    public void gretestCommonFactorTest2() {
        int x = 3;
        int y = 9;
        int result = Divisor.newInstance(x, y).gretestCommonFactor();
        assertThat(result, is(3));
    }

    @Test
    public void gretestCommonFactorTest3() {
        int x = 33;
        int y = 121;
        int result = Divisor.newInstance(x, y).gretestCommonFactor();
        assertThat(result, is(11));
    }

    @Test
    public void gretestCommonFactorTest4() {
        int x = 3;
        int y = 11;
        int result = Divisor.newInstance(x, y).gretestCommonFactor();
        assertThat(result, is(1));
    }
}
