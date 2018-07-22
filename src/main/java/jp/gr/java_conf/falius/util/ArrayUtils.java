package jp.gr.java_conf.falius.util;

/**
 * 配列を処理するユーティリティークラスです。
 * @author "ymiyauchi"
 * @since 1.2.0
 *
 */
public final class ArrayUtils {

    private ArrayUtils() { }

    public static int[] toIntArray(Integer[] integers) {
        int[] ret = new int[integers.length];
        for (int i = 0, size = integers.length; i < size; i++) {
            ret[i] = integers[i].intValue();
        }
        return ret;
    }

    public static Integer[] toIntegerArray(int[] ints) {
        Integer[] ret = new Integer[ints.length];
        for (int i = 0, size = ints.length; i < size; i++) {
            ret[i] = Integer.valueOf(ints[i]);
        }
        return ret;
    }
}
