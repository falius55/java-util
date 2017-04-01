package jp.gr.java_conf.falius.util.check;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class CheckListTest {

    @Test
    public void addingTest() {
        CheckList<String> list = new CheckList<>("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
        assertThat(list.size(), is(10));

        for (int i = 0; i < 10; i++) {
            assertThat(list.get(i), is(Integer.toString(i)));
        }

        List<Integer> elems = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            elems.add(i);
        }

        CheckList<Integer> intList = new CheckList<>(elems);
        assertThat(intList.size(), is(100));
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

    @Test
    public void simpleCheckTest() {
        List<String> elems = new ArrayList<>();
        for (char c = 'a'; c <= 'z'; c++) {
            elems.add(String.valueOf(c));
        }

        CheckList<String> checkList = new CheckList<>(elems);

        List<String> checkChars = new ArrayList<String>() {
            {
                add("c");
                add("j");
                add("u");
                add("o");
                add("p");
                add("v");
            }
        };

        for (String c : checkChars) {
            checkList.check(c);
        }

        for (String elem : checkList) {
            boolean isChecked = checkList.isChecked(elem);
            if (checkChars.contains(elem)) {
                assertTrue(isChecked);
            } else {
                assertFalse(isChecked);
            }
        }

        for (String elem : checkList) {
            checkList.check(elem);
        }

        assertTrue(checkList.isCheckedAll());
    }

    @Test
    public void indexCheckTest() {
        List<String> elems = new ArrayList<>();
        for (char c = 'a'; c <= 'z'; c++) {
            elems.add(String.valueOf(c));
        }

        CheckList<String> checkList = new CheckList<>(elems);

        Map<String, Integer> checkChars = new HashMap<String, Integer>() {
            {
                put("c", 2);
                put("h", 7);
                put("l", 11);
                put("x", 23);
                put("z", 25);
            }
        };

        for (Integer i : checkChars.values()) {
            checkList.checkByIndex(i);
        }

        for (String c : checkChars.keySet()) {
            assertTrue(checkList.isChecked(c));
        }
    }

    @Test
    public void checkedSetTest() {
        List<String> checkElem = new ArrayList<String>() {
            {
                add("aaa");
                add("check it!");
                add("hello world");
                add("ccc");
                add("hello world");
            }
        };
        List<String> nonCheckElem = new ArrayList<String>() {
            {
                add("bbb");
                add("I am Bob.");
                add("in test");
                add("abcdefg");
            }
        };
        List<String> allElem = new ArrayList<String>();
        allElem.addAll(checkElem);
        allElem.addAll(nonCheckElem);
        allElem.add("check it!");  // 重複要素

        CheckList<String> checkList = new CheckList<>(allElem);

        for (String elem : checkElem) {
            checkList.check(elem);
        }

        Set<String> checkedSet = checkList.checkedSet();
        for (String elem : checkElem) {
            assertTrue(checkedSet.contains(elem));
        }

        Set<String> nonCheckedSet = checkList.nonCheckedSet();
        for (String elem : nonCheckElem) {
            assertTrue(nonCheckedSet.contains(elem));
        }
    }

    @Test
    public void testCheckInteger() {
        CheckList<Integer> list = new CheckList<>(0, 1, 2);
        list.check(1);
        assertThat(list.isChecked(1), is(true));
    }

    @Test
    public void testCheckInteger2() {
        CheckList<Integer> list = new CheckList<>(0, 2, 8, 1, 13, 3);
        list.check(1);
        assertThat(list.isChecked(2), is(false));
        assertThat(list.isChecked(1), is(true));
    }
}
