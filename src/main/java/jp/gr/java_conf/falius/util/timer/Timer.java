package jp.gr.java_conf.falius.util.timer;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Timer {
    private final Map<String, Data> dataMap;
    private final BigDecimal MILLIS_NANOS = new BigDecimal(1000000);
    public Timer() {
        this.dataMap = new HashMap<String, Data>();
    }
    public void start(String label) {
        dataMap.put(label, new Data(label));
    }
    public void end(String label) {
        long timeNanos = dataMap.get(label).end();
        System.out.printf("%s:%sミリ秒%n",label,format(timeNanos));
    }
    private String format(long nanos) {
        BigDecimal bd = new BigDecimal(nanos);
        BigDecimal result = bd.divide(MILLIS_NANOS);
        return result.toPlainString();
    }
    public static void main(String args[]) {
        Timer timer = new Timer();
        timer.start("test");
        timer.end("test");
        long start = System.nanoTime();
        long end = System.nanoTime();
        System.out.println(end - start);
    }

    public static class Data {
        private final long start;
        private final String label;
        private Data(String label) {
            this.label = label;
            this.start = System.nanoTime();
        }

        private long end() {
            return System.nanoTime() - start;
        }
    }
}