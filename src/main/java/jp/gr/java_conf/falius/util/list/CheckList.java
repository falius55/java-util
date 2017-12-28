package jp.gr.java_conf.falius.util.list;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

/**
 * <p>
 * 各要素に対してチェックされたかどうかを保持するクラスです。<br>
 * 保持する要素に重複は許容されません。
 * また、要素を後から増減することはできません。
 *
 * @author "ymiyauchi"
 *
 * @param <E> 保持する要素の型
 */
public class CheckList<E> implements Checkable<E>, Iterable<E>, List<E> {
    private final List<Entry<E>> mEntries = new ArrayList<>();

    public CheckList(Iterable<E> elems) {
        for (E elem : elems) {
            mEntries.add(new Entry<E>(elem));
        }
    }

    @SuppressWarnings("unchecked")
    public CheckList(E... elems) {
        this(Arrays.asList(elems));
    }

    /**
     * @throws IllegalArgumentException 存在しない要素が渡された場合
     */
    @Override
    public void check(E e) {
        Entry<E> entry = find(e);
        entry.mIsChecked = true;
    }

    /**
     * <p>
     * 追加された順序であるインデックスで要素を指定してチェックをつけます。<br>
     * @param index
     * @throws IndexOutOfBoundsException indexが範囲外の場合
     */
    public void checkByIndex(int index) {
        Entry<E> entry = mEntries.get(index);
        entry.mIsChecked = true;
    }

    /**
     * 指定した要素にチェックが付いているかどうかを返します。
     * @param e
     * @return
     */
    public boolean isChecked(E e) {
        Entry<E> entry = find(e);
        return entry.mIsChecked;
    }

    /**
     * すべての要素がチェックされたかどうかを返します。
     * @return
     */
    public boolean isCheckedAll() {
        for (Entry<E> entry : mEntries) {
            if (!entry.mIsChecked) {
                return false;
            }
        }
        return true;
    }

    /**
     * チェックされた要素の集合を返します。
     * @return
     */
    public Set<E> checkedSet() {
        Set<E> ret = new HashSet<>();
        for (Entry<E> entry : mEntries) {
            if (entry.mIsChecked) {
                ret.add(entry.mElem);
            }
        }
        return ret;
    }

    /**
     * チェックされていない要素の集合を返します。
     * @return
     */
    public Set<E> nonCheckedSet() {
        Set<E> ret = new HashSet<>();
        for (Entry<E> entry : mEntries) {
            if (!entry.mIsChecked) {
                ret.add(entry.mElem);
            }
        }
        return ret;
    }

    /**
     * 指定されたインデックスの要素を返します。
     * @throws IndexOutOfBoundsException インデックスが範囲外の場合
     */
    @Override
    public E get(int index) {
        return mEntries.get(index).mElem;
    }

    /**
     * 要素数を返します。
     */
    @Override
    public int size() {
        return mEntries.size();
    }

    @Override
    public Iterator<E> iterator() {
        return new EntryIterator<E>(this);
    }

    private Entry<E> find(Object e) {
        for (Entry<E> entry : mEntries) {
            if (entry.mElem.equals(e)) {
                return entry;
            }
        }
        throw new IllegalArgumentException(String.format("%s is not in %s", e, mEntries));
    }

    private static class EntryIterator<E> implements Iterator<E> {
        private final CheckList<E> mOuter;
        private int nextIndex = 0;

        private EntryIterator(CheckList<E> outer) {
            mOuter = outer;
        }

        @Override
        public boolean hasNext() {
            return nextIndex < mOuter.mEntries.size();
        }

        @Override
        public E next() {
            return mOuter.get(nextIndex++);
        }
    }

    private static class Entry<E> {
        private final E mElem;
        private volatile boolean mIsChecked = false;

        private Entry(E elem) {
            mElem = elem;
        }

        public String toString() {
            return mElem.toString();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty() {
        return mEntries.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(Object o) {
        for (Entry<E> entry : mEntries) {
            if (entry.mElem.equals(o)) {
                return true;
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object[] toArray() {
        int size = mEntries.size();
        Object[] array = new Object[size];
        for (int i = 0; i < size; i++) {
            array[i] = mEntries.get(i).mElem;
        }
        return array;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T[] toArray(T[] a) {
        return (T[]) toArray();
    }

    /**
     * サポートされていません。
     * @throws UnsupportedOperationException 常に投げられる
     */
    @Override
    public boolean add(E e) {
        throw new UnsupportedOperationException();
    }

    /**
     * サポートされていません。
     * @throws UnsupportedOperationException 常に投げられる
     */
    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    /**
     * サポートされていません。
     * @throws UnsupportedOperationException 常に投げられる
     */
    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    /**
     * サポートされていません。
     * @throws UnsupportedOperationException 常に投げられる
     */
    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    /**
     * サポートされていません。
     * @throws UnsupportedOperationException 常に投げられる
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    /**
     * サポートされていません。
     * @throws UnsupportedOperationException 常に投げられる
     */
    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    /**
     * サポートされていません。
     * @throws UnsupportedOperationException 常に投げられる
     */
    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    /**
     * サポートされていません。
     * @throws UnsupportedOperationException 常に投げられる
     */
    @Override
    public E set(int index, E element) {
        throw new UnsupportedOperationException();
    }

    /**
     * サポートされていません。
     * @throws UnsupportedOperationException 常に投げられる
     */
    @Override
    public void add(int index, E element) {
        throw new UnsupportedOperationException();
    }

    /**
     * サポートされていません。
     * @throws UnsupportedOperationException 常に投げられる
     */
    @Override
    public E remove(int index) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < mEntries.size(); i++) {
            if (mEntries.get(i).mElem.equals(o)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int lastIndexOf(Object o) {
        for (int i = mEntries.size() - 1; i >= 0; i--) {
            if (mEntries.get(i).mElem.equals(o)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ListIterator<E> listIterator() {
        @SuppressWarnings("unchecked")
        E[] array = (E[]) toArray();
        List<E> list = Arrays.asList(array);
        return list.listIterator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ListIterator<E> listIterator(int index) {
        @SuppressWarnings("unchecked")
        E[] array = (E[]) toArray();
        List<E> list = Arrays.asList(array);
        return list.listIterator(index);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        List<Entry<E>> subEntries = mEntries.subList(fromIndex, toIndex);
        return new CheckList<E>((E[]) subEntries.toArray());
    }
}
