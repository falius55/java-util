package jp.gr.java_conf.falius.util.calc;

import java.util.Objects;

/**
 * 分数を扱うクラス
 * @author "ymiyauchi"
 * @since 1.2.0
 *
 */
public class Fraction extends Number {
    private final int mNumerator; // 分子
    private final int mDenominator; // 分母

    public static Fraction newInstance(int numerator, int denominator) {
        return new Fraction(numerator, denominator);
    }

    /**
     * 単位分数(分子が1の分数)のインスタンスを作成します。
     * @param denominator 分母
     * @return
     * @since 1.2.0
     */
    public static Fraction newUnit(int denominator) {
        return new Fraction(1, denominator);
    }

    /**
     * 整数によってインスタンス(分母が1)を作成します。
     * @param val 値
     * @return
     * @since 1.2.0
     */
    public static Fraction newInstanceByInteger(int val) {
        return new Fraction(val, 1);
    }

    /**
     *
     * 自動で約分されます。
     * @param numerator 分子
     * @param denominator 分母
     * @return
     * @throws ArithmeticException denominatorがゼロの場合
     * @since 1.2.0
     */
    private Fraction(int numerator, int denominator) {
        if (denominator == 0) {
            throw new ArithmeticException("denominator is not allowed zero");
        }

        if (numerator == 0) {
            mNumerator = 0;
            mDenominator = 1;
        } else {
            int greatestCommonDivisor = PairIntProcessor.newInstance(numerator, denominator).greatestCommonDivisor();
            mNumerator = numerator / greatestCommonDivisor;
            mDenominator = denominator / greatestCommonDivisor;
        }
    }

    /**
     * 分子を返します。
     * @return
     * @since 1.2.0
     */
    public int numerator() {
        return mNumerator;
    }

    /**
     * 分母を返します。
     * @return
     * @since 1.2.0
     */
    public int denominator() {
        return mDenominator;
    }

    /**
     * 真分数かどうかを返します。
     * @return 分子が分母より小さい(真分数の)場合にtrue
     * @since 1.2.0
     */
    public boolean isProper() {
        return Math.abs(mNumerator) < Math.abs(mDenominator);
    }

    /**
     * 仮分数かどうかを返します。
     * @return 分子が分母より大きい(仮分数の)場合、または等しい場合にtrue
     * @since 1.2.0
     */
    public boolean isImproper() {
        return Math.abs(mNumerator) >= Math.abs(mDenominator);
    }

    /**
     * 整数かどうかを返します。
     * @return 整数であればtrue
     * @since 1.2.0
     */
    public boolean isInt() {
        return Math.abs(mDenominator) == 1 || mNumerator == 0;
    }

    /**
     * 単位分数かどうかを返します。
     * @return 分子が１(単位分数の)場合にtrue
     * @since 1.2.0
     */
    public boolean isUnit() {
        return Math.abs(mNumerator) == 1;
    }

    /**
     * 帯分数の整数部をbyte値で返します。
     * @since 1.2.0
     */
    @Override
    public byte byteValue() {
        return (byte) intValue();
    }

    /**
     * 帯分数の整数部をshort値で返します。
     * @since 1.2.0
     */
    @Override
    public short shortValue() {
        return (short) intValue();
    }

    /**
     * 帯分数の整数部を返します。
     * @return
     * @since 1.2.0
     */
    @Override
    public int intValue() {
        return mNumerator / mDenominator;
    }

    /**
     * 帯分数の整数部をlong値で返します。
     * @return
     * @since 1.2.0
     */
    @Override
    public long longValue() {
        return intValue();
    }

    /**
     * 帯分数の分数部を返します。
     * @return
     * @since 1.2.0
     */
    public Fraction toProper() {
        int numerator = mNumerator % mDenominator;
        return new Fraction(numerator, mDenominator);
    }

    /**
     * この分数をdouble値で返します。
     * @since 1.2.0
     */
    @Override
    public double doubleValue() {
        return (double) mNumerator / mDenominator;
    }

    /**
     * この分数をfloat値で返します。
     * @since 1.2.0
     */
    @Override
    public float floatValue() {
        return (float) doubleValue();
    }

    /**
     * この分数に指定された値を加えた、新しいFractionオブジェクトを返します。
     * @param val
     * @return
     * @since 1.2.0
     */
    public Fraction plus(int val) {
        int numerator = mNumerator + mDenominator * val;
        int denominator = mDenominator;
        return new Fraction(numerator, denominator);
    }

    /**
     * この分数に指定された値を加えた、新しいFractionオブジェクトを返します。
     * @param val
     * @return
     * @since 1.2.0
     */
    public Fraction plus(Fraction val) {
        Objects.requireNonNull(val, "val is null");
        int numerator = this.mNumerator * val.mDenominator + val.mNumerator * this.mDenominator;
        int denominator = this.mDenominator * val.mDenominator;
        return new Fraction(numerator, denominator);
    }

    /**
     * この分数から指定された値を引いた、新しいFractionオブジェクトを返します。
     * @param val
     * @return
     * @since 1.2.0
     */
    public Fraction minus(int val) {
        int numerator = this.mNumerator - this.mDenominator * val;
        int denominator = mDenominator;
        return new Fraction(numerator, denominator);
    }

    /**
     * この分数から指定された値を引いた、新しいFractionオブジェクトを返します。
     * @param val
     * @return
     * @since 1.2.0
     */
    public Fraction minus(Fraction val) {
        int numerator = this.mNumerator * val.mDenominator - val.mNumerator * this.mDenominator;
        int denominator = this.mDenominator * val.mDenominator;
        return new Fraction(numerator, denominator);
    }

    /**
     * この分数に指定された値を乗じた、新しいFractionオブジェクトを返します。
     * @param val
     * @return
     * @since 1.2.0
     */
    public Fraction multiply(int val) {
        int numerator = this.mNumerator * val;
        int denominator = this.mDenominator;
        return new Fraction(numerator, denominator);
    }

    /**
     * この分数に指定された値を乗じた、新しいFractionオブジェクトを返します。
     * @param val
     * @return
     * @since 1.2.0
     */
    public Fraction multiply(Fraction val) {
        int numerator = this.mNumerator * val.mNumerator;
        int denominator = this.mDenominator * val.mDenominator;
        return new Fraction(numerator, denominator);
    }

    /**
     * この分数を指定された値で割った、新しいFractionオブジェクトを返します。
     * @param val
     * @return
     * @since 1.2.0
     */
    public Fraction devide(int val) {
        int numerator = this.mNumerator;
        int denominator = this.mDenominator * val;
        return new Fraction(numerator, denominator);
    }

    /**
     * この分数を指定された値で割った、新しいFractionオブジェクトを返します。
     * @param val
     * @return
     * @since 1.2.0
     */
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
