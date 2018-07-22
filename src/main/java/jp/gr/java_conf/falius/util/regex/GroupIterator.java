package jp.gr.java_conf.falius.util.regex;

import java.util.Iterator;

/**
     *  各マッチ文字列の指定されたインデックスのグルーピング文字列をイテレートするIterableであり、Iteratorです
     */
class GroupIterator implements Iterator<String>, Iterable<String> {
    private final int mIndex;
    private final int mSize;
    private final Regex mRegex;
    private int mIndexCounter = -1;

    GroupIterator(int index, Regex regex) {
        mIndex = index;
        mRegex = regex;
        mSize = regex.matchCount();
    }

    /**
     *  次の要素の有無を返します
     */
    public boolean hasNext() {
        return mIndexCounter + 1 < mSize;
    }

    /**
     *  次の要素を返します
     */
    public String next() {
        mIndexCounter++;
        return mRegex.find(mIndexCounter).group(mIndex);
    }

    /**
     *  自身を返します
     */
    public Iterator<String> iterator() {
        return this;
    }
}
