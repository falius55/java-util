package jp.gr.java_conf.falius.util.calc;

import jp.gr.java_conf.falius.util.ArrayUtils;
import jp.gr.java_conf.falius.util.list.EstimateList;

public final class Calculator {

    private Calculator() {}

    /**
     * 合計値を計算します。
     * @param start
     * @param end
     * @return
     */
    public static int sum(int start, int end) {
        if (end < start) {
            int tmp = start;
            start = end;
            end = tmp;
        }

        int ret = 0;
        for (int i = start; i <= end; i++ ) {
            ret += i;
        }

        return ret;
    }

    /**
     * 平均値を計算します。
     * @param first
     * @param others
     * @return
     */
    public static int average(int first, int... others) {
        int sum = first;
        for (int elem : others) {
            sum += elem;
        }
        return sum / (others.length + 1);
    }

    /**
     * 偏差値を求めます。
     * @param param
     * @return
     * @throws IllegalArgumentException 引数リストに要素が入っていない場合
     */
    public static int deviation(EstimateList<Integer> param) {
        if (param.isEmpty()) {
            throw new IllegalArgumentException();
        }

        EstimateList<Integer> list = new EstimateList<>(param);
        int score = list.estimatedValue().intValue();
        list.remove(list.estimatedValue());
        int[] otherScores = ArrayUtils.toIntArray(list.toArray(new Integer[0]));

        return deviation(score, otherScores);
    }

    /**
     * 偏差値を求めます。
     * @param score 点数
     * @param otherScores 他の点数
     * @return
     * @throws IllegalArgumentException 引数が負の数の場合
     */
    public static int deviation(int score, int... otherScores) {

        int avg = average(score, otherScores);
        int diff = Math.abs(score - avg) * 10 / standardDeviation(score, otherScores);

        final int STANDARD = 50;
        if (score > avg) {
            return STANDARD + diff;
        } else if (score < avg) {
            return STANDARD - diff;
        }
        return STANDARD;
    }

    /**
     * 分散を求めます。
     * @param first
     * @param others
     * @return
     * @throws IllegalArgumentException 引数が負の数の場合
     */
    public static int variance(int first, int... others) {
        if (first < 0) {
            throw new IllegalArgumentException("first < 0: " + first);
        }
        for (int elem : others) {
            if (elem < 0) {
                throw new IllegalArgumentException("other < 0: " + elem);
            }
        }

        // 平均点との差の平方数の平均
        int avg = average(first, others);
        int ret = (first - avg) * (first - avg);
        for (int elem : others) {
            ret += (elem - avg) * (elem - avg);
        }

        ret /= others.length + 1;

        return ret;
    }

    /**
     * 標準偏差を求めます。
     * @param first
     * @param others
     * @return
     * @throws IllegalArgumentException 引数が負の数の場合
     */
    public static int standardDeviation(int first, int... others) {
        // 分散の平方根
        return (int) Math.sqrt(variance(first, others));
    }

    /**
     * 順列の場合の数(nPr)を計算します。
     * @param n n >= 0, n >= r を満たす数
     * @param r r >= 0, r <= n を満たす数
     * @return
     * @throws IllegalArgumentException 引数が負の数であった場合、r > n であった場合
     */
    public static int permutation(int n, int r) {
        if (n < 0 || r < 0) {
            throw new IllegalArgumentException("n < 0 || r < 0");
        }
        if (r > n) {
            throw new IllegalArgumentException("r > n exception! : n = " + n + ", r = " + r);
        }

        int ret = 1;
        for (int i = n, cnt = r; cnt > 0; cnt--, i--) {
            ret *= i;
        }
        return ret;
    }

    /**
     * 階乗(n!)を計算します。
     * @param n
     * @return
     * @throws IllegalArgumentException n < 0の場合
     */
    public static int factorial(int n) {
        return permutation(n, n);
    }

    /**
     * 組み合わせの場合の数(nCr)を計算します。
     * @param n
     * @param r
     * @return
     * @throws IllegalArgumentException n < 0 または r < 0 の場合
     */
    public static int conbination(int n, int r) {
        if (n < 0 || r < 0) {
            throw new IllegalArgumentException(" n < 0 || r < 0");
        }

        int result = permutation(n, r) / factorial(r);
        return result;
    }

}
