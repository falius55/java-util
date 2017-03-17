package jp.gr.java_conf.falius.util.datetime;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class DateTimeTest {

    @Test
    public void testHashCode() {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_WEEK, 2);
        DateTime dateTime = DateTime.newInstance(date);
        DateTime dateTime2 = DateTime.newInstance(cal.getTime());
        assertThat(dateTime.hashCode(), is(not(dateTime2.hashCode())));

        DateTime dateTime3 = DateTime.newInstance(date);
        assertThat(dateTime.hashCode(), is(dateTime3.hashCode()));

        Map<DateTime, String> map = new HashMap<>();
        map.put(dateTime, "1");
        assertThat(map.get(dateTime), is("1"));
        map.put(dateTime2, "2");
        assertThat(map.get(dateTime2), is("2"));
        map.put(dateTime3, "3");
        assertThat(map.get(dateTime3), is("3"));
        assertThat(map.get(dateTime), is("3"));
    }

    @Test
    public void testNow() {
    }

    @Test
    public void testNewInstance() {
    }

    @Test
    public void testNewInstanceDate() {
    }

    @Test
    public void testNewInstanceIntIntInt() {
    }

    @Test
    public void testNewInstanceIntIntIntIntIntInt() {
    }

    @Test
    public void testNewInstanceLong() {
    }

    @Test
    public void testNewInstanceString() {
    }

    @Test
    public void testNewInstanceFromSqliteDateString() {
    }

    @Test
    public void testNewInstanceStringString() {
    }

    @Test
    public void testGetTimeInMillis() {
    }

    @Test
    public void testFormat() {
    }

    @Test
    public void testGetDatetimeFormat() {
    }

    @Test
    public void testFormatTo() {
    }

    @Test
    public void testGetYear() {
    }

    @Test
    public void testGetMonth() {
    }

    @Test
    public void testGetDay() {
    }

    @Test
    public void testComputeElapsedDaysDateTimeDateTime() {
    }

    @Test
    public void testComputeElapsedDaysDateTime() {
    }

    @Test
    public void testNextDay() {
    }

    @Test
    public void testPrevDay() {
    }

    @Test
    public void testEqualsObject() {
    }

    @Test
    public void testToString() {
    }

    @Test
    public void testCompareTo() {
    }

}
