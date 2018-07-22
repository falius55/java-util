package jp.gr.java_conf.falius.util.list;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * いずれかの値を指定するリスト。
 * 常にいずれかの要素が指定されている。
 * リストが空の時に限り、指定インデックスは-1になる。
 * @author "ymiyauchi"
 *
 */
public class EstimateList<E> implements List<E>, Checkable<E> {
    private final List<E> mValues;
    private int mIndex = -1;

    public EstimateList() {
        mValues = new ArrayList<>();
    }

    public EstimateList(Collection<? extends E> c) {
        mValues = new ArrayList<>(c);

        if (c instanceof EstimateList) {
            @SuppressWarnings("unchecked")
            EstimateList<E> r = (EstimateList<E>) c;
            mIndex = r.estimatedIndex();
        }
    }

    //  内部クラスEstimateListViewのコンストラクタで使用
    private EstimateList(List<E> original) {
        mValues = original;
    }

    public EstimateList(int initialCapacity) {
        mValues = new ArrayList<>(initialCapacity);
    }

    public EstimateList(@SuppressWarnings("unchecked") E... es) {
        this(Arrays.asList(es));
    }

    /**
     * 指定値を設定します。
     * 指定された値を保持していなければ何もせずにfalseを返します。
     * @param val
     * @return 新たな指定値の設定に成功するとtrue
     */
    public boolean estimate(E val) {
        int index = mValues.indexOf(val);
        if (index == -1) {
            return false;
        }
        mIndex = index;
        return true;
    }

    /**
     * 指定値を設定します。
     * estimate(E val)と同等です。
     * @param e
     */
    @Override
    public void check(E e) {
        estimate(e);
    }

    /**
     * インデックスで指定値を設定します。
     * 保持している値がなければ何もしません。
     * 指定したインデックスが保持している要素数よりも大きい場合は最後の値を指定します。
     * @param index
     * @return 新たな指定値の設定に成功するとtrue
     * @throws IllegalArgumentException 引数が負の数だった場合
     */
    public boolean estimateByIndex(int index) {
        if (index < 0) {
            throw new IllegalArgumentException();
        }

        if (isEmpty()) {
            return false;
        }
        if (index >= size()) {
            mIndex = size() - 1;
            return true;
        }

        mIndex = index;
        return true;
    }

    /**
     * 指定している要素のインデックスを返します。
     * 未指定の場合、最初の値を指定していると見なします。
     * @return 指定要素のインデックス。保持要素がなければ－１
     */
    public int estimatedIndex() {
        if (mIndex < 0 && size() > 0) {
            mIndex = 0;
        }
        return mIndex;
    }

    /**
     * 指定している要素を返します。
     * @return
     * @throws IllegalSttateException 保持要素が存在しない場合
     */
    public E estimatedValue() {
        int index = estimatedIndex();
        if (index < 0) {
            throw new IllegalStateException();
        }

        return mValues.get(index);
    }

    /**
     * 現在指定されている値の次の値を指定します。
     * 現在指定されている値が最後の値である場合には、最初の値が新たに指定されます。
     * 要素を保持していなければ何もしません。
     * @return
     */
    public boolean estimateNext() {
        int index = estimatedIndex();
        if (index == -1) {
            return false;
        }

        if (index == size() - 1) {
            mIndex = 0;
            return true;
        }

        mIndex++;
        return true;
    }

    /**
     * 現在指定されている値の前の値を指定します。
     * 現在指定されている値が最初の値である場合には、最後の値が新たに指定されます。
     * 要素を保持していなければ何もしません。
     * @return
     */
    public boolean estimatePrev() {
        int index = estimatedIndex();
        if (index == -1) {
            return false;
        }

        if (index == 0) {
            mIndex = size() - 1;
            return true;
        }

        mIndex--;
        return true;
    }

    @Override
    public int size() {
        return mValues.size();
    }

    @Override
    public boolean isEmpty() {
        return mValues.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return mValues.contains(o);
    }

    @Override
    public Iterator<E> iterator() {
        return mValues.iterator();
    }

    @Override
    public Object[] toArray() {
        return mValues.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return mValues.toArray(a);
    }

    @Override
    public boolean add(E e) {
        return mValues.add(e);
    }

    /**
     * {@inheritDoc}
     * 指定されている値が削除された場合、直後の値に指定が移る。
     * 最後の値が指定されていて削除された場合、直前の値が指定される。
     */
    @Override
    public boolean remove(Object o) {
        int rmIndex = indexOf(o);
        E ret = remove(rmIndex);
        if (ret == null) {
            return false;
        }

        return true;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return mValues.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return mValues.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (index <= mIndex) {
            mIndex += c.size();
        }
        return mValues.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean ret = true;
        for (Object elem : c) {
            if (!remove(elem)) {
                ret = false;
            }
        }
        return ret;
    }

    /**
     * {@inheritDoc}
     * 指定されているのと同じ要素を複数保持している場合、予期しない動作をする可能性があります。
     */
    @Override
    public boolean retainAll(Collection<?> c) {
        E estimatedValue = estimatedValue();
        boolean ret = mValues.retainAll(c);
        mIndex = lastIndexOf(estimatedValue);
        return ret;
    }

    @Override
    public void clear() {
        mValues.clear();
        mIndex = -1;
    }

    @Override
    public E get(int index) {
        return mValues.get(index);
    }

    /**
     * {@inheritDoc}
     * 指定要素を置き換えると、置き換えられた要素がそのまま指定されます。
     */
    @Override
    public E set(int index, E element) {
        return mValues.set(index, element);
    }

    @Override
    public void add(int index, E element) {
        if (index <= mIndex) {
            mIndex++;
        }
        mValues.add(index, element);
    }

    @Override
    public E remove(int index) {
        // 削除するものがないなら何もしない。
        if (index == -1) {
            return null;
        }
        if (index >= size()) {
            return null;
        }

        if (index < mIndex || (mIndex == size() - 1 && index == mIndex)) {
            // 指定インデックスより小さいインデックスの値を削除する。
            // 最後の値が指定されていて、それを削除する。
            mIndex--;
        }

        return mValues.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return mValues.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return mValues.lastIndexOf(o);
    }

    @Override
    public ListIterator<E> listIterator() {
        return mValues.listIterator();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return mValues.listIterator(index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EstimateList<E> subList(int fromIndex, int toIndex) {
        EstimateListView ret = new EstimateListView(this, mValues.subList(fromIndex, toIndex), fromIndex, toIndex);
        return ret;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("index: ").append(mIndex).append(System.lineSeparator());
        sb.append("value: ").append(mIndex < 0 ? "non" : get(mIndex)).append(System.lineSeparator());
        sb.append(Arrays.toString(toArray()));
        return sb.toString();
    }

    /**
     * サブリスト用クラス
     * このクラスのインスタンスへの変更は、オリジナルのインスタンスにも影響する
     * @author "ymiyauchi"
     *
     */
    private class EstimateListView extends EstimateList<E> {
        // TODO: clear()以外の操作(estimateByIndex(int)など)も追加する
        private final EstimateList<E> mOriginal;
        private final int mFromIndex;
        private final int mToIndex;

        /**
         *
         * @param original アウタークラス
         * @param listView アウタークラスで保持している要素のサブリスト
         * @param fromIndex サブリストの開始インデックス
         * @param toIndex サブリストの終了インデックス
         */
        private EstimateListView(EstimateList<E> original, List<E> listView, int fromIndex, int toIndex) {
            super(listView);
            mOriginal = original;
            mFromIndex = fromIndex;
            mToIndex = toIndex;

            // 範囲内に指定値がある
            if (mOriginal.mIndex >= mFromIndex && mOriginal.mIndex < mToIndex) {
                super.mIndex = mOriginal.mIndex - mFromIndex;
            } else {
                // TODO: 範囲外に指定値がある時、ビューのインデックスは－１を返すように(現状では０を返してしまう)
                super.mIndex = -1;
            }
        }

        @Override
        public void clear() {
            super.clear();
            if (mOriginal.mIndex >= mFromIndex && mOriginal.mIndex < mToIndex) {
                // 指定値が範囲内にあった場合、消去したら指定値がなくなる
                mOriginal.mIndex = -1;
            } else if (mOriginal.mIndex >= mToIndex) {
                mOriginal.mIndex -= (mToIndex - mFromIndex);
            }
        }

        @Override
        public int estimatedIndex() {
            return super.mIndex;
        }

    }
}
