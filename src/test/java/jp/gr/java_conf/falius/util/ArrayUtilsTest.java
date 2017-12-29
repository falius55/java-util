package jp.gr.java_conf.falius.util;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.junit.Test;

public class ArrayUtilsTest {

    @Test
    public void toIntArrayTest() {
        Integer[] integers = { 10, 20, 30 };

        int[] ret = ArrayUtils.toIntArray(integers);
        assertThat(ret, is(instanceOf(int[].class)));
        assertThat(ret, is(not(instanceOf(Integer[].class))));
    }

    @Test
    public void toIntegerArrayTest() {
        int[] ints = { 10, 20, 30 };

        Integer[] ret = ArrayUtils.toIntegerArray(ints);
        assertThat(ret, is(instanceOf(Integer[].class)));
        assertThat(ret, is(not(instanceOf(int[].class))));
    }
}
