package jp.gr.java_conf.falius.util.list;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class EstimateListTest {

    @Test
    public void estimateTest() {
        EstimateList<String> list = new EstimateList<String>() {
            {
                add("abc");
                add("def");
                add("ghi");
                add("jkl");
                add("mno");
            }
        };
        assertThat(list.estimatedIndex(), is(0));
        assertThat(list.estimatedValue(), is("abc"));

        boolean ret = list.estimate("aaa");
        assertThat(ret, is(false));
        assertThat(list.estimatedIndex(), is(0));

        ret = list.estimate("jkl");
        assertThat(ret, is(true));
        assertThat(list.estimatedValue(), is("jkl"));
        assertThat(list.estimatedIndex(), is(3));

    }

    @Test
    public void estimateByIndexTest() {
        EstimateList<String> list = new EstimateList<>();

        boolean ret = list.estimateByIndex(1);
        assertThat(ret, is(false));
        assertThat(list.estimatedIndex(), is(-1));

        list.add("abc");
        list.add("def");
        list.add("ghi");
        list.add("jkl");
        list.add("mno");
        assertThat(list.size(), is(5));
        assertThat(list.estimatedIndex(), is(0));

        ret = list.estimateByIndex(2);
        assertThat(ret, is(true));
        assertThat(list.estimatedIndex(), is(2));

        ret = list.estimateByIndex(8);
        assertThat(ret, is(true));
        assertThat(list.estimatedIndex(), is(4));

    }

    @Test(expected = IllegalArgumentException.class)
    public void estimateByIndexExceptionTest() {
        EstimateList<String> list = new EstimateList<String>() {
            {
                add("abc");
                add("def");
                add("ghi");
                add("jkl");
                add("mno");
            }
        };
        assertThat(list.size(), is(5));

        list.estimateByIndex(-1); // throw
    }

    @Test(expected = IllegalStateException.class)
    public void estimatedValueExceptionTest() {
        EstimateList<String> list = new EstimateList<>();

        boolean ret = list.estimateByIndex(0);
        assertThat(ret, is(false));
        assertThat(list.estimatedIndex(), is(-1));
        String val = list.estimatedValue(); // throw
        assertThat(val, is("aaa"));
    }
}
