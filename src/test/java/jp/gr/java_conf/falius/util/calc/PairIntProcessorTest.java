package jp.gr.java_conf.falius.util.calc;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.junit.Test;

public class PairIntProcessorTest {

    @Test
    public void gretestCommonDivisorTest() {
        int x = 24;
        int y = 30;

        PairIntProcessor div = PairIntProcessor.newInstance(x, y);
        int result = div.gretestCommonDivisor();

        assertThat(result, is(6));
    }

    @Test
    public void gretestCommonDivisorTest2() {
        int x = 3;
        int y = 9;
        int result = PairIntProcessor.newInstance(x, y).gretestCommonDivisor();
        assertThat(result, is(3));
    }

    @Test
    public void gretestCommonDivisorTest3() {
        int x = 33;
        int y = 121;
        int result = PairIntProcessor.newInstance(x, y).gretestCommonDivisor();
        assertThat(result, is(11));
    }

    @Test
    public void gretestCommonFactorTest4() {
        int x = 3;
        int y = 11;
        int result = PairIntProcessor.newInstance(x, y).gretestCommonDivisor();
        assertThat(result, is(1));
    }
}
