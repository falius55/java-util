package jp.gr.java_conf.falius.util.list;

import java.util.ArrayList;
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
public class EstimateList<T> implements List<T> {
    private final List<T> mValues;
    private int mIndex = -1;

    public EstimateList() {
        mValues = new ArrayList<T>();
    }

    /**
     * 指定値を設定します。
     * 指定された値を保持していなければ何もせずにfalseを返します。
     * @param val
     * @return 新たな指定値の設定に成功するとtrue
     */
    public boolean estimate(T val) {
        int index = mValues.indexOf(val);
        if (index == -1) {
            return false;
        }
        mIndex = index;
        return true;
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
    public T estimatedValue() {
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
    public Iterator<T> iterator() {
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
    public boolean add(T e) {
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
        // 削除するものがないなら何もしない。
        if (rmIndex == -1) {
            return false;

        } else if (rmIndex == size() - 1) {
            // 最後の値が指定されていて、それを削除する。
            mIndex -= 1;

        }
        return mValues.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return mValues.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return mValues.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        return mValues.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return mValues.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return mValues.retainAll(c);
    }

    @Override
    public void clear() {
        mValues.clear();
        mIndex = -1;
    }

    @Override
    public T get(int index) {
        return mValues.get(index);
    }

    @Override
    public T set(int index, T element) {
        return mValues.set(index, element);
    }

    @Override
    public void add(int index, T element) {
        mValues.add(index, element);
    }

    @Override
    public T remove(int index) {
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
    public ListIterator<T> listIterator() {
        return mValues.listIterator();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return mValues.listIterator(index);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return mValues.subList(fromIndex, toIndex);
    }

}
