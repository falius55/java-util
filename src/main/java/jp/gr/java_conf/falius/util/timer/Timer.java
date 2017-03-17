package jp.gr.java_conf.falius.util.timer;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * startメソッドの呼び出しからendメソッドの呼び出しまでにかかった時間を保持するクラスです。
 * @author "ymiyauchi"
 *
 */
public class Timer {
    private static final BigDecimal MILLIS_NANOS = new BigDecimal(1000000);
    private static final BigDecimal SECOND_NANOS = new BigDecimal(1000000000);

    private final Map<String, Data> mDataMap = new HashMap<>();

    public void start(String label) {
        mDataMap.put(label, new Data());
    }

    public void end(String label) {
        mDataMap.get(label).end();
    }

    /**
     *
     * @param label
     * @return
     * @throws IllegalArgumentException まだstartメソッドを実行されていないラベルを渡された場合
     * @throws IllegalStateException まだendメソッドを実行されていないラベルが引数に渡された場合
     */
    public long getNanos(String label) {
        checkOrThrow(label);
        long nanos = mDataMap.get(label).getNanos();
        return nanos;
    }

    /**
     *
     * @param label
     * @return
     * @throws IllegalArgumentException まだstartメソッドを実行されていないラベルを渡された場合
     * @throws IllegalStateException まだendメソッドを実行されていないラベルが引数に渡された場合
     */
    public long getMillis(String label) {
        checkOrThrow(label);
        long timeNanos = mDataMap.get(label).getNanos();
        BigDecimal bd = new BigDecimal(timeNanos);
        BigDecimal result = bd.divide(MILLIS_NANOS);
        return result.longValue();
    }

    /**
     *
     * @param label
     * @return
     * @throws IllegalArgumentException まだstartメソッドを実行されていないラベルを渡された場合
     * @throws IllegalStateException まだendメソッドを実行されていないラベルが引数に渡された場合
     */
    public int getSecond(String label) {
        checkOrThrow(label);
        long timeNanos = mDataMap.get(label).getNanos();
        BigDecimal bd = new BigDecimal(timeNanos);
        BigDecimal result = bd.divide(SECOND_NANOS);
        return result.intValue();
    }

    private void checkOrThrow(String label) {
        if (!mDataMap.containsKey(label)) {
            throw new IllegalArgumentException("no data of label : " + label);
        }
    }

    public static void main(String args[]) {
        Timer timer = new Timer();
        timer.start("test");
        timer.end("test");
        long resultNanos = timer.getNanos("test");
        System.out.println(resultNanos);
    }

    public static class Data {
        private final long mStart;
        private long mResult = -1;

        private Data() {
            mStart = System.nanoTime();
        }

        private long end() {
            mResult = System.nanoTime() - mStart;
            return mResult;
        }

        private long getNanos() {
            if (mResult < 0) {
                throw new IllegalStateException();
            }
            return mResult;
        }
    }
}