package jp.gr.java_conf.falius.util.check;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * <p>
 * 各要素に対してチェックされたかどうかを保持する不変クラスです。
 *
 * @author "ymiyauchi"
 *
 * @param <E> 保持する要素の型
 */
public class CheckList<E> implements Checkable<E>, Iterable<E> {
    private final Set<Entry<E>> mEntries = new LinkedHashSet<>();

    public CheckList(Collection<E> elems) {
        for (E elem : elems) {
            mEntries.add(new Entry<E>(elem));
        }
    }

    @SuppressWarnings("unchecked")
    public CheckList(E... elems) {
        this(Arrays.asList(elems));
    }

    @Override
    public void check(E e) {
        Entry<E> entry = find(e);
        entry.mIsChecked = true;
    }

    public void check(int index) {
        Entry<E> entry = find(index);
        entry.mIsChecked = true;
    }

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

    public Iterator<E> iterator() {
        return new EntryIterator();
    }

    private Entry<E> find(E e) {
       for (Entry<E> entry : mEntries) {
           if (entry.mElem.equals(e)) {
               return entry;
           }
       }
        throw new NoSuchElementException(String.format("%s is not in %s", e, mEntries));
    }

    private Entry<E> find(int index) {
        int cnt = 0;
        for (Entry<E> entry : mEntries) {
            if (cnt == index) {
                return entry;
            }
        }
        throw new NoSuchElementException(
                String.format("invalid index %d : out of %d entries", index, mEntries.size()));
    }

    public class EntryIterator implements Iterator<E> {
        private int nextIndex = 0;

        @Override
        public boolean hasNext() {
            return nextIndex < mEntries.size();
        }

        @Override
        public E next() {
            return find(nextIndex++).mElem;
        }
    }

    private static class Entry<E> {
        private final E mElem;
        private boolean mIsChecked = false;

        private Entry(E elem) {
            mElem = elem;
        }

        public String toString() {
            return mElem.toString();
        }
    }
}
