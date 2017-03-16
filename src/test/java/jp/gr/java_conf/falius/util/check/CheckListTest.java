package jp.gr.java_conf.falius.util.check;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class CheckListTest {

    @Test
    public void addingTest() {
        CheckList<String> list = new CheckList<>("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");

        for (int i = 0; i < 10; i++) {
            assertThat(list.get(i), is(Integer.toString(i)));
        }

        List<Integer> elems = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            elems.add(i);
        }

        CheckList<Integer> intList = new CheckList<>(elems);
        for (int i = 0; i < 100; i++) {
            assertThat(intList.get(i), is(Integer.valueOf(i)));
        }
    }

    @Test
    public void iteratorTest() {
        CheckList<String> list = new CheckList<>("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");

        int cnt = 0;
        for (String elem : list) {
            assertThat(elem, is(Integer.toString(cnt++)));
        }

        List<Integer> elems = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            elems.add(i);
        }

        CheckList<Integer> intList = new CheckList<>(elems);
        cnt = 0;
        for (int i : intList) {
            assertThat(i, is(cnt++));
        }
    }
}
