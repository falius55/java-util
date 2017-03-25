package jp.gr.java_conf.falius.util.range;

import java.util.Iterator;

/**
 * <p>
 * startを含み、endを含まない連続した数値のイテレータです。
 * toArrayメソッドを利用することで拡張for文におけるIntegerへのオートボクシング変換を防ぐことができます。
 * <p>
 * <p>
 * stepを省略すると１として扱われ、さらにstartを省略すると０から始まります。
 * </p>
 */

public class IntRange implements Iterable<Integer>, Iterator<Integer> {
    private static final int DEFAULT_START = 0;
    private static final int DEFAULT_STEP = 1;

    private final int mStart;
    private final int mEnd;
    private final int mStep;
    private int mCount;

    public IntRange(int start, int end, int step) {
        mStart = start;
        mEnd = end;
        mStep = step;
        mCount = start - step;
    }

    public IntRange(int start, int end) {
        this(start, end, DEFAULT_STEP);
    }

    public IntRange(int end) {
        this(DEFAULT_START, end);
    }

    @Override
    public boolean hasNext() {
        return mCount + mStep < mEnd;
    }

    @Override
    public Integer next() {
        return mCount = mCount + mStep;
    }

    @Override
    public Iterator<Integer> iterator() {
        return this;
    }

    /**
     * このオブジェクトをイテレータとして使用した際に取得できる値を配列として返します。
     * このメソッドは内部の状態に影響を与えません。
     * @return
     */
    public int[] toArray() {
        int start = mStart;
        int end = mEnd;
        int step = mStep;

        boolean isDivisible = (end - start) % step == 0;
        int len = (end - start) / step;
        len = isDivisible ? len : len + 1;
        int[] ret = new int[len];

        for (int i = 0, k = start; k < end; i++, k += step) {
            ret[i] = k;
        }
        return ret;
    }

    /**
     * 指定回数分処理を繰り返します。
     * インデックスも必要な場合はIterator#forEachRemainingを利用してください。
     * このメソッドはこのオブジェクトをイテレータとして使用するため、内部の状態が変化します。
     * @param runnable
     */
    public void simpleForEach(Runnable runnable) {
        for (int i : this) {
            runnable.run();
        }
    }
}