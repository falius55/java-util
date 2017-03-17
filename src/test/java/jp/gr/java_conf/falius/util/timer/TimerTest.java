package jp.gr.java_conf.falius.util.timer;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.junit.Test;

public class TimerTest {

    @Test
    public void testGetNanos() {
        long sleepMillis = 1000;
        String label = "get nanos";
        Timer timer = new Timer();
        timer.start(label);
        sleep(sleepMillis);
        timer.end(label);

        long nanos = timer.getNanos(label);
        assertThat(nanos, is(greaterThanOrEqualTo(sleepMillis * 1000)));
    }

    @Test
    public void testGetMillis() {
        long sleepMillis = 1000;
        String label = "get millis";
        Timer timer = new Timer();
        timer.start(label);
        sleep(sleepMillis);
        timer.end(label);

        long nanos = timer.getNanos(label);
        assertThat(nanos, is(greaterThanOrEqualTo(sleepMillis)));
    }

    @Test
    public void testGetSecond() {
        long sleepMillis = 1000;
        String label = "get nanos";
        Timer timer = new Timer();
        timer.start(label);
        sleep(sleepMillis);
        timer.end(label);

        long nanos = timer.getNanos(label);
        assertThat(nanos, is(greaterThanOrEqualTo(sleepMillis / 1000)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotStart() {
        Timer timer = new Timer();
        timer.getMillis("not start");
    }

    @Test(expected = IllegalStateException.class)
    public void testNotEnd() {
        String label = "not end";
        Timer timer = new Timer();
        timer.start(label);
        sleep(100);
        long nanos = timer.getNanos(label);
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new IllegalStateException();
        }
    }
}
