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

    public static Fraction newInstance(int numerator, int denominator) {
        return new Fraction(numerator, denominator);
    }

    /**
     *
     * 自動で約分されます。
     * @param numerator 分子
     * @param denominator 分母
     * @return
     * @throws ArithmeticException denominatorがゼロの場合
     */
    private Fraction(int numerator, int denominator) {
        if (denominator == 0) {
            throw new ArithmeticException("denominator is not allowed zero");
        }

        if (numerator == 0) {
            mNumerator = 0;
            mDenominator = 1;
        } else {
            int greatestCommonDivisor = PairIntProcessor.newInstance(numerator, denominator).gretestCommonDivisor();
            mNumerator = numerator / greatestCommonDivisor;
            mDenominator = denominator / greatestCommonDivisor;
        }
    }

    public int numerator() {
        return mNumerator;
    }

    public int denominator() {
        return mDenominator;
    }

    /**
     *
     * @return 分子が分母より小さい(真分数の)場合にtrue
     */
    public boolean isProper() {
        return Math.abs(mNumerator) < Math.abs(mDenominator);
    }

    /**
     *
     * @return 分子が分母より大きい(仮分数の)場合にtrue
     */
    public boolean isImproper() {
        return Math.abs(mNumerator) > Math.abs(mDenominator);
    }

    /**
     *
     * @return 整数であればtrue
     */
    public boolean isInt() {
        return Math.abs(mDenominator) == 1 || mNumerator == 0;
    }

    /**
     *
     * @return 分子が１(単位分数の)場合にtrue
     */
    public boolean isUnit() {
        return Math.abs(mNumerator) == 1;
    }

    /**
     * 帯分数の整数部を返します。
     * @return
     */
    public int toIntValue() {
        return mNumerator / mDenominator;
    }

    /**
     * 帯分数の分数部を返します。
     * @return
     */
    public Fraction toProper() {
        int numerator = mNumerator % mDenominator;
        return new Fraction(numerator, mDenominator);
    }

    public Double toDoubleValue() {
        return (double) mNumerator / mDenominator;
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

    public Fraction multiply(int val) {
        int numerator = this.mNumerator * val;
        int denominator = this.mDenominator;
        return new Fraction(numerator, denominator);
    }

    public Fraction multiply(Fraction val) {
        int numerator = this.mNumerator * val.mNumerator;
        int denominator = this.mDenominator * val.mDenominator;
        return new Fraction(numerator, denominator);
    }

    public Fraction devide(int val) {
        int numerator = this.mNumerator;
        int denominator = this.mDenominator * val;
        return new Fraction(numerator, denominator);
    }

    public Fraction devide(Fraction val) {
        int numerator = this.mNumerator * val.mDenominator;
        int denominator = this.mDenominator * val.mNumerator;
        return new Fraction(numerator, denominator);
    }

    @Override
    public String toString() {
        // "n / m"
        // 整数で表せるなら整数のみ
        if (mNumerator == 0) {
            return "0";
        }

        StringBuilder ret = new StringBuilder();
        if ((mNumerator >= 0 && mDenominator < 0) || (mNumerator < 0 && mDenominator >= 0)) {
            ret.append('-');
        }
        if (mDenominator == 1) {
            return ret.append(Math.abs(mNumerator)).toString();
        }

        return ret
                .append(Math.abs(mNumerator))
                .append(" / ")
                .append(Math.abs(mDenominator))
                .toString();
    }

}
