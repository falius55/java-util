package jp.gr.java_conf.falius.util.range;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.junit.Test;

public class StringRangeTest {

    @Test
    public void testIterator() {
        String str = "abcdefghijklmnopqrstuvwxyz";
        char c = 'a';
        for (String s : new StringRange(str)) {
            assertThat(s, is(String.valueOf(c++)));
        }

        assertThat((int)c, is((int)('z' + 1)));
    }

    @Test
    public void testToArray() {
        String str = "abcdefghijklmnopqrstuvwxyz";
        char c = 'a';
        for (String s : new StringRange(str).toArray()) {
            assertThat(s, is(String.valueOf(c++)));
        }

        String[] array = new StringRange(str).toArray();
        assertThat(array.length, is(str.length()));
    }
}
