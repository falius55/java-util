package jp.gr.java_conf.falius.util.datetime;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

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
        DateTime now = DateTime.now();
        DateTime today = DateTime.newInstance(new Date());
        assertThat(now.getYear(), is(today.getYear()));
        assertThat(now.getMonth(), is(today.getMonth()));
        assertThat(now.getDay(), is(today.getDay()));
    }

    @Test
    public void testNewInstance() {
        DateTime datetime = DateTime.newInstance();
        assertThat(datetime, instanceOf(DateTime.class));
        DateTime now = DateTime.now();
        assertThat(datetime.getYear(), is(now.getYear()));
        assertThat(datetime.getMonth(), is(now.getMonth()));
        assertThat(datetime.getDay(), is(now.getDay()));
    }

    @Test
    public void testNewInstanceDate() {
        Date date = new Date();
        DateTime datetime = DateTime.newInstance(date);
        DateTime now = DateTime.now();
        assertThat(datetime.getYear(), is(now.getYear()));
        assertThat(datetime.getMonth(), is(now.getMonth()));
        assertThat(datetime.getDay(), is(now.getDay()));
    }

    @Test
    public void testNewInstanceIntIntInt() {
        Calendar cal = Calendar.getInstance();
        DateTime dateTime = DateTime.newInstance(
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        DateTime dateTime2 = DateTime.newInstance(cal.getTime());
        assertThat(dateTime, is(dateTime2));
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
        int addNum = 10;
        Calendar cal = Calendar.getInstance();
        DateTime today = DateTime.newInstance(cal.getTime());
        cal.add(Calendar.DAY_OF_MONTH, addNum);
        DateTime after = DateTime.newInstance(cal.getTime());

        int elapsedDays = today.computeElapsedDays(after);
        int elapsedDays2 = after.computeElapsedDays(today);
        assertThat(elapsedDays, is(addNum));
        assertThat(elapsedDays, is(elapsedDays2));
        assertThat(elapsedDays, is(greaterThanOrEqualTo(0)));
        assertThat(elapsedDays2, is(greaterThanOrEqualTo(0)));
    }

    @Test
    public void testNextDay() {
        DateTime today = DateTime.newInstance();
        DateTime tommorow = today.nextDay();
        DateTime dayAfterTommorow = tommorow.nextDay();

        assertThat(today.computeElapsedDays(tommorow), is(1));
        assertThat(today.computeElapsedDays(dayAfterTommorow), is(2));

        assertThat(tommorow.compareTo(today), is(greaterThan(0)));
        assertThat(dayAfterTommorow.compareTo(tommorow), is(greaterThan(0)));
        assertThat(today.compareTo(dayAfterTommorow), is(lessThan(0)));

        assertThat(tommorow, is(not(sameInstance(today))));
        assertThat(dayAfterTommorow, is(not(sameInstance(tommorow))));
        assertThat(dayAfterTommorow, is(not(sameInstance(today))));
    }

    @Test
    public void testPrevDay() {
        DateTime today = DateTime.newInstance();
        DateTime yesterday = today.prevDay();
        DateTime dayBeforeYesterday = yesterday.prevDay();

        assertThat(today.computeElapsedDays(yesterday), is(1));
        assertThat(today.computeElapsedDays(dayBeforeYesterday), is(2));

        assertThat(yesterday.compareTo(today), is(lessThan(0)));
        assertThat(dayBeforeYesterday.compareTo(yesterday), is(lessThan(0)));
        assertThat(today.compareTo(dayBeforeYesterday), is(greaterThan(0)));

        assertThat(yesterday, is(not(sameInstance(today))));
        assertThat(dayBeforeYesterday, is(not(sameInstance(yesterday))));
        assertThat(dayBeforeYesterday, is(not(sameInstance(today))));
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
