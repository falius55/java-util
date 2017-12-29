package jp.gr.java_conf.falius.util.list;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;

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

    @Test
    public void estimateNextTest() {
        EstimateList<String> list = new EstimateList<>();
        boolean ret = list.estimateNext();
        assertThat(ret, is(false));
        assertThat(list.estimatedIndex(), is(-1));

        list.add("abc");
        list.add("def");
        list.add("ghi");
        list.add("jkl");
        list.add("mno");
        assertThat(list.estimatedIndex(), is(0));
        assertThat(list.estimatedValue(), is("abc"));

        int k = 0;
        for (String elem : list) {
            assertThat(list.estimatedValue(), is(elem));
            assertThat(list.estimatedIndex(), is(k++));
            ret = list.estimateNext();
            assertThat(ret, is(true));
        }
        assertThat(list.estimatedValue(), is("abc"));
        assertThat(list.estimatedIndex(), is(0));
    }

    @Test
    public void estimatePrevTest() {
        EstimateList<String> list = new EstimateList<>();
        boolean ret = list.estimatePrev();
        assertThat(ret, is(false));
        assertThat(list.estimatedIndex(), is(-1));

        list.add("abc");
        list.add("def");
        list.add("ghi");
        list.add("jkl");
        list.add("mno");
        assertThat(list.estimatedIndex(), is(0));
        assertThat(list.estimatedValue(), is("abc"));

        ret = list.estimatePrev();
        assertThat(ret, is(true));
        for (int k = list.size() - 1; k >= 0; k--) {
            assertThat(list.estimatedValue(), is(list.get(k)));
            assertThat(list.estimatedIndex(), is(k));
            ret = list.estimatePrev();
            assertThat(ret, is(true));
        }
        assertThat(list.estimatedValue(), is("mno"));
        assertThat(list.estimatedIndex(), is(list.size() - 1));
    }

    @Test
    public void removeTest() {
        EstimateList<String> list = new EstimateList<>();

        // 削除対象を保持していない場合
        boolean ret = list.remove("ghi");
        assertThat(ret, is(false));
        assertThat(list.estimatedIndex(), is(-1));

        list.add("abc");
        list.add("def");
        list.add("ghi");
        list.add("jkl");
        list.add("mno");
        assertThat(list.estimatedIndex(), is(0));
        assertThat(list.estimatedValue(), is("abc"));

        // "abc", "def", "ghi", "jkl", "mno"
        // estimate: "abc"
        ret = list.remove("abc");
        assertThat(ret, is(true));
        assertThat(list.estimatedValue(), is("def"));
        assertThat(list.estimatedIndex(), is(0));

        // 最後の値を削除する
        // "def", "ghi", "jkl", "mno"
        // estimate: "def"
        ret = list.estimate("mno");
        assertThat(ret, is(true));
        ret = list.remove("mno");
        assertThat(ret, is(true));
        assertThat(list.estimatedIndex(), is(2));
        assertThat(list.estimatedValue(), is("jkl"));

        // 指定要素より小さいインデックスの値を削除する。
        // "def", "ghi", "jkl"
        // estimate: "jkl"
        ret = list.remove("ghi");
        assertThat(ret, is(true));
        assertThat(list.estimatedIndex(), is(1));
        assertThat(list.estimatedValue(), is("jkl"));

        // 指定要素より大きいインデックスの値を削除する。
        // "def", "jkl"
        // estimate: "jkl"
        list.estimate("def");
        ret = list.remove("jkl");
        assertThat(ret, is(true));
        assertThat(list.estimatedIndex(), is(0));
        assertThat(list.estimatedValue(), is("def"));

        // 存在しない要素を削除する。
        // "def"
        // estimate: "def"
        ret = list.remove("abc");
        assertThat(ret, is(false));
        assertThat(list.estimatedIndex(), is(0));
        assertThat(list.estimatedValue(), is("def"));

        // "def"
        // estimate: "def"
        ret = list.remove("def");
        assertThat(ret, is(true));
        assertThat(list.estimatedIndex(), is(-1));


        list.add("aaa");
        assertThat(list.estimatedIndex(), is(0));
        assertThat(list.estimatedValue(), is("aaa"));

    }

    @Test
    public void removeAllTest() {
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

        list.estimate("def");
        assertThat(list.estimatedIndex(), is(1));
        boolean ret = list.removeAll(Arrays.asList("def", "jkl", "mno"));
        assertThat(ret, is(true));
        assertThat(list.estimatedIndex(), is(1));
        assertThat(list.estimatedValue(), is("ghi"));

        ret = list.removeAll(Arrays.asList("abc", "ghi"));
        assertThat(ret, is(true));
        assertThat(list.estimatedIndex(), is(-1));
    }

    @Test
    public void addTest() {
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
        list.estimateByIndex(3);  // "jkl"
        list.add(2, "ccc");
        assertThat(list.estimatedValue(), is("jkl"));
        assertThat(list.estimatedIndex(), is(4));
    }

    @Test
    public void addAllTest() {
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
        list.estimateByIndex(3);  // "jkl"
        list.addAll(3, Arrays.asList("aaa", "ccc", "eee"));
        assertThat(list.estimatedValue(), is("jkl"));
        assertThat(list.estimatedIndex(), is(6));
    }
}
