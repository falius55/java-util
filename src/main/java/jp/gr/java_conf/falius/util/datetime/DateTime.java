package jp.gr.java_conf.falius.util.datetime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

/**
 * <p>
 * 日付や日時を扱う不変クラスです
 * 文字列のフォーマット機能もあります
 *
 * @since 1.1.1
 */

public final class DateTime implements Comparable<DateTime> {
    public final static String DEFAULT_DATETIME_FORMAT = "yyyy/MM/dd HH:mm:ss";
    public final static String DEFAULT_DATE_FORMAT = "yyyy/MM/dd";
    public final static String SQLITE_DATE_FORMAT = "yyyy-MM-dd";

    private final Calendar mCalendar;


    private DateTime(Calendar cal) {
        mCalendar = cal;
    }

    /**
     * 現在日時を返します。
     * @return 現在日時のインスタンス
     */
    public static DateTime now() {
        return newInstance();
    }

    /**
     * now()と同じです。
     * @return 現在日時のインスタンス
     */
    public static DateTime newInstance() {
        return new DateTime(Calendar.getInstance());
    }

    public static DateTime newInstance(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return new DateTime(cal);
    }

    public static DateTime newInstance(int year, int month, int date) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, date);
        return new DateTime(cal);
    }

    public static DateTime newInstance(int year, int month, int date, int hourOfDay, int minute, int second) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, date, hourOfDay, minute, second);
        return new DateTime(cal);
    }

    public static DateTime newInstance(long timeInMillis) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timeInMillis);
        return new DateTime(cal);
    }

    /**
     * @param textDate
     * @return
     * @throws IllegalArgumentException textDateがデフォルトの形式の文字列(yyyy/MM/dd)になっていなかった場合
     */
    public static DateTime newInstance(String textDate) {
        return newInstance(textDate, DEFAULT_DATE_FORMAT);
    }

    /**
     * @param textDate
     * @return
     * @throws IllegalArgumentException SQLiteのDate文字列(yyyy-MM-dd)の形にtextDateがなっていなかった場合
     */
    public static DateTime newInstanceFromSqliteDateString(String textDate) {
        return newInstance(textDate, SQLITE_DATE_FORMAT);
    }

    /**
     * @param textDate
     * @param format
     * @return
     * @throws IllegalArgumentException 指定のformatでtextDataをパースできなかった場合
     */
    public static DateTime newInstance(String textDate, String format) {
        Objects.requireNonNull(textDate);
        Objects.requireNonNull(format);

        Calendar cal = Calendar.getInstance();
        Date date;
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.JAPAN);
        try {
            date = sdf.parse(textDate);
        } catch (ParseException e) {
            throw new IllegalArgumentException("format exception : '" + textDate + "' -> " + format);
        }
        cal.setTime(date);
        return new DateTime(cal);
    }


    public long getTimeInMillis() {
        return mCalendar.getTimeInMillis();
    }

    /**
     * デフォルトのフォーマットでこのインスタンスを表す文字列を返します。
     * @return
     */
    public String format() {
        return formatTo(DEFAULT_DATE_FORMAT);
    }

    public String getDatetimeFormat() {
        return formatTo(DEFAULT_DATETIME_FORMAT);
    }

    /**
     * @param format
     * @return
     * @throws IllegalArgumentException 指定されたフォーマットのパターンが無効な場合
     */
    public String formatTo(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.JAPAN);
        return sdf.format(mCalendar.getTime());
    }

    public int getYear() {
        return mCalendar.get(Calendar.YEAR);
    }

    public int getMonth() {
        return mCalendar.get(Calendar.MONTH);
    }

    public int getDay() {
        return mCalendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 経過日数を計算します。引数は順不同です
     * 戻り値は必ず０以上の整数になります。
     *
     * @param start
     * @param end
     * @return
     * @throws NullPointerException 指定されたDateTimeがnullの場合
     */
    public static int computeElapsedDays(DateTime start, DateTime end) {
        // 起点日をミリ秒に変換
        long sTimeInMillis = start.getTimeInMillis();
        // 終点日をミリ秒に変換
        long eTimeInMillis = end.getTimeInMillis();
        // 差をミリ秒で計算
        long processMillis = Math.abs(eTimeInMillis - sTimeInMillis);
        // 24時間未満を切り捨て
        processMillis -= processMillis % (1000 * 60 * 60 * 24);
        // 何日分か
        processMillis /= (1000 * 60 * 60 * 24);

        return (int) processMillis;
    }

    /**
     * {@code DateTime.computeElepsedDays(this, another);} と同義です。
     * @param another
     * @return
     */
    public int computeElapsedDays(DateTime another) {
        return computeElapsedDays(this, another);
    }

    /**
     * 翌日を表す新しいインスタンスを返します。
     * @return
     */
    public DateTime nextDay() {
        Calendar ret = Calendar.getInstance();
        ret.setTimeInMillis(getTimeInMillis());
        ret.add(Calendar.DAY_OF_MONTH, 1);
        return new DateTime(ret);
    }

    /**
     * 前日を表す新しいインスタンスを返します。
     * @return
     */
    public DateTime prevDay() {
        Calendar ret = Calendar.getInstance();
        ret.setTimeInMillis(getTimeInMillis());
        ret.add(Calendar.DAY_OF_MONTH, -1);
        return new DateTime(ret);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof DateTime
                && format().equals(((DateTime) obj).format());
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + format().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return format();
    }

    @Override
    public int compareTo(DateTime another) {
        Objects.requireNonNull(another);
        return mCalendar.compareTo(another.mCalendar);
    }
}