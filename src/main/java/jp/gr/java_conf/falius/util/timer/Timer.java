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
    private final Map<String, Long> mResult = new HashMap<>();

    public void start(String label) {
        mDataMap.put(label, new Data());
    }

    public void end(String label) {
        long timeNanos = mDataMap.get(label).end();
        mResult.put(label, timeNanos);
    }

    public long getNanos(String label) {
        return mResult.get(label);
    }

    public long getMillis(String label) {
        long timeNanos = mResult.get(label);
        BigDecimal bd = new BigDecimal(timeNanos);
        BigDecimal result = bd.divide(MILLIS_NANOS);
        return result.longValue();
    }

    public int getSecond(String label) {
        long timeNanos = mResult.get(label);
        BigDecimal bd = new BigDecimal(timeNanos);
        BigDecimal result = bd.divide(SECOND_NANOS);
        return result.intValue();
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

        private Data() {
            mStart = System.nanoTime();
        }

        private long end() {
            return System.nanoTime() - mStart;
        }
    }
}