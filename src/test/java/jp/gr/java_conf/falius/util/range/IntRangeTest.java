package jp.gr.java_conf.falius.util.range;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
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
    public void incrementToArrayTest() {
        int start = 1;
        int end = 10;
        IntRange range = new IntRange(start, end);
        int[] result = range.toArray();

        assertThat(toIntegerArray(result), is(arrayContaining(1, 2, 3, 4, 5, 6, 7, 8, 9)));
    }

    private Integer[] toIntegerArray(int[] array) {
        Integer[] ret = new Integer[array.length];
        for (int i : new IntRange(array.length)) {
            ret[i] = array[i];
        }
        return ret;
    }

    @Test
    public void incrementIteratorTest() {
        int start = 4;
        int end = 18;

        int cnt = start;
        for (int i : new IntRange(start, end)) {
            assertThat(i, is(cnt++));
        }
    }

    @Test
    public void onlyEndToArrayTest() {
        int count = 10;
        int[] result = new IntRange(count).toArray();
        assertThat(toIntegerArray(result), is(arrayContaining(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)));
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
