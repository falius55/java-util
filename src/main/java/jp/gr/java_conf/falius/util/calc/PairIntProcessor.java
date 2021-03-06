package jp.gr.java_conf.falius.util.calc;

/**
 * ２つの値を処理するクラスです。
 * @author "ymiyauchi"
 * @since 1.2.0
 *
 */
public class PairIntProcessor {
    private final int mFirst;
    private final int mSecond;

    public static PairIntProcessor newInstance(int first, int second) {
        return new PairIntProcessor(first, second);
    }

    private PairIntProcessor(int first, int second) {
        mFirst = first;
        mSecond = second;
    }

    /**
     * 最大公約数を求めます
     * @return
     * @throws ArithmeticException firstおよびsecondにゼロが含まれている場合
     * @since 1.2.0
     */
    public int greatestCommonDivisor() {
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

    /**
     * 最小公倍数を求めます
     * @return
     * @since 1.2.0
     */
    public int leastCommonMultiple() {
        return mFirst *mSecond / greatestCommonDivisor();
    }

}
