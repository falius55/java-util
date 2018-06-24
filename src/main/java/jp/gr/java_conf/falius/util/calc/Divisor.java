package jp.gr.java_conf.falius.util.calc;

/**
 * 約数を表すクラスです
 * @author "ymiyauchi"
 *
 */
public class Divisor {
    private final int mX;
    private final int mY;

    public static Divisor newInstance(int x, int y) {
        return new Divisor(x, y);
    }

    private Divisor(int x, int y) {
        if (x > y) {
            mX = x;
            mY = y;
        } else {
            mX = y;
            mY = x;
        }
    }

    /**
     * 最大公約数を求めます
     * @return
     */
    public int gretestCommonFactor() {
        int x = mX;
        int y = mY;
        int tmp;
        // ユークリッドの互除法
        while ((tmp = x % y) != 0) {
            x = y;
            y = tmp;
        }

        return y;
    }

}
