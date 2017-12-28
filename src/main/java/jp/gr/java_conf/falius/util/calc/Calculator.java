package jp.gr.java_conf.falius.util.calc;

public class Calculator {

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
