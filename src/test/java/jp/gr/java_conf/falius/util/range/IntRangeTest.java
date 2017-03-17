package jp.gr.java_conf.falius.util.range;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class IntRangeTest {

    @Test
    public void iteratorTest() {
        int start = -5;
        int end = 29;
        int step = 2;
        int k = start;
        for (int i : new IntRange(k, end, step)) {
            assertThat(i, is(k));
            k += step;
        }
        assertTrue(k >= end);
        assertTrue(k < end + step);
    }

    @Test
    public void toArrayTest() {
        int start = -5;
        int end = 29;
        int step = 2;
        int k = start;
        for (int i : new IntRange(k, end, step).toArray()) {
            assertThat(i, is(k));
            k += step;
        }
        assertTrue(k >= end);
        assertTrue(k < end + step);
    }
}
