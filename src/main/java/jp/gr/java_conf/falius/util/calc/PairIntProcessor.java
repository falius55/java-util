package jp.gr.java_conf.falius.util.calc;

/**
 * 約数を表すクラスです
 * @author "ymiyauchi"
 *
 */
public class PairIntProcessor {
    private final int mFirst;
    private final int mSecond;

    public static PairIntProcessor newInstance(int x, int y) {
        return new PairIntProcessor(x, y);
    }

    private PairIntProcessor(int first, int second) {
        mFirst = first;
        mSecond = second;
    }

    /**
     * 最大公約数を求めます
     * @return
     */
    public int gretestCommonDivisor() {
        int x = mFirst > mSecond ? mFirst : mSecond;
        int y = mFirst > mSecond ? mSecond : mFirst;
        int tmp;
        // ユークリッドの互除法
        while ((tmp = x % y) != 0) {
            x = y;
            y = tmp;
        }

        return y;
    }

}
