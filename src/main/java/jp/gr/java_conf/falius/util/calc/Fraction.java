package jp.gr.java_conf.falius.util.calc;

import java.util.Objects;

/**
 * 分数を扱うクラス
 * @author "ymiyauchi"
 *
 */
public class Fraction {
    private final int mNumerator; // 分子
    private final int mDenominator; // 分母

    /**
     *
     * @param numerator 分子
     * @param denominator 分母
     * @throws ArithmeticException denominatorがゼロの場合
     * @return
     */
    public static Fraction newInstance(int numerator, int denominator) {
        return new Fraction(numerator, denominator);
    }

    private Fraction(int numerator, int denominator) {
        int greatestCommonDivisor = PairIntProcessor.newInstance(numerator, denominator).gretestCommonDivisor(); // throw ArithmeticException
        mNumerator = numerator / greatestCommonDivisor;
        mDenominator = denominator / greatestCommonDivisor;
    }

    public int numerator() {
        return mNumerator;
    }

    public int denominator() {
        return mDenominator;
    }

    public int toIntValue() {
        return mNumerator / mDenominator;
    }

    public Fraction plus(int val) {
        int numerator = mNumerator + mDenominator * val;
        int denominator = mDenominator;
        return new Fraction(numerator, denominator);
    }

    public Fraction plus(Fraction val) {
        Objects.requireNonNull(val, "val is null");
        int numerator = this.mNumerator * val.mDenominator + val.mNumerator * this.mDenominator;
        int denominator = this.mDenominator * val.mDenominator;
        return new Fraction(numerator, denominator);
    }

    public Fraction minus(int val) {
        int numerator = this.mNumerator - this.mDenominator * val;
        int denominator = mDenominator;
        return new Fraction(numerator, denominator);
    }

    public Fraction minus(Fraction val) {
        int numerator = this.mNumerator * val.mDenominator - val.mNumerator * this.mDenominator;
        int denominator = this.mDenominator * val.mDenominator;
        return new Fraction(numerator, denominator);
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        if ((mNumerator >= 0 && mDenominator < 0) || (mNumerator < 0 && mDenominator >= 0)) {
            ret.append('-');
        }
        return ret
                .append(Math.abs(mNumerator))
                .append(" / ")
                .append(Math.abs(mDenominator))
                .toString();
    }

}
