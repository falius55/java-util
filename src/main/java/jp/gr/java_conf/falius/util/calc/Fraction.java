package jp.gr.java_conf.falius.util.calc;

/**
 * 分数を扱うクラス
 * @author "ymiyauchi"
 *
 */
public class Fraction {
    private final int mNumerator;  // 分子
    private final int mDenominator;  // 分母

    public static Fraction newInstance(int numerator, int denominator) {
        return new Fraction(numerator, denominator);
    }

    private Fraction(int numerator, int denominator) {
        int greatestCommonFactor = Divisor.newInstance(numerator, denominator).gretestCommonFactor();
        mNumerator = numerator / greatestCommonFactor;
        mDenominator = denominator / greatestCommonFactor;
    }


    public int numerator() {
        return mNumerator;
    }

    public int denominator() {
        return mDenominator;
    }

    public Fraction plus(int val) {
        int numerator = mNumerator + mDenominator * val;
        int denominator = mDenominator;
        return new Fraction(numerator, denominator);
    }

    @Override
    public String toString() {
       return new StringBuilder()
               .append(mNumerator)
               .append(" / ")
               .append(mDenominator)
               .toString();
    }

}
