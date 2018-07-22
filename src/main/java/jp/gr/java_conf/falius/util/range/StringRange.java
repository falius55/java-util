package jp.gr.java_conf.falius.util.range;

import java.util.Arrays;
import java.util.Iterator;

/**
 * 文字列を１文字ずつイテレートするクラスです。
 * @author "ymiyauchi"
 *
 * @since 1.1.1
 *
 */
public class StringRange implements Iterable<String>, Iterator<String> {
    private final String[] mArray;
    private int mNextIndex = 0;

    public StringRange(String string) {
        mArray = toStringArray(string);
    }

    private String[] toStringArray(String string) {
        String[] ret = new String[string.length()];
        for (int i = 0; i < string.length(); i++) {
            String s = String.valueOf(string.charAt(i));
            ret[i] = s;
        }
        return ret;
    }

    @Override
    public boolean hasNext() {
        return mNextIndex < mArray.length;
    }

    @Override
    public String next() {
        return mArray[mNextIndex++];
    }

    @Override
    public Iterator<String> iterator() {
        return this;
    }

    public String[] toArray() {
        return Arrays.copyOf(mArray, mArray.length);
    }
}
