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
 * 各要素に対してチェックされたかどうかを保持する不変クラスです。<br>
 * 保持する要素に重複は許容されません。
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

    /**
     * <p>
     * 追加された順序であるインデックスで要素を指定してチェックをつけます。<br>
     * <p>
     * 注意：<br>
     * 要素がIntegerの場合のみcheck(E e)に委譲され、同値のオブジェクトにチェックがつけられます
     *     (そのためインデックスによる指定はできません。また、型パラメータではなく要素オブジェクト自体の型によって
     *     判断されますので、型パラメータがNumberなどIntegerの基底型の場合は注意が必要となります)。
     *     その場合は対象オブジェクトの検索が二度行われることになるため、効率が求められる際はintではなく直接Integerを
     *     引数に渡すようにしてください。
     * @param index
     */
    public void check(int index) {
        Entry<E> entry = find(index);

        /*
         * もし要素がIntegerの場合は、プリミティブのintが渡されても
         *     Indexではなく同値オブジェクトを探してチェックをつける。
         * Integerを要素にしているのにindexで要素を指定することは考えづらいので、
         *     おそらくこの方が期待していた動作になるはず。
         * この際、検索を二度(find(int)とfind(E))行うことになるため効率はよくない。
         */
        if (entry.mElem instanceof Integer) {
            check(Integer.valueOf(index));
            return;
        }

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

    public E get(int index) {
        return find(index).mElem;
    }

    public int size() {
        return mEntries.size();
    }

    public Iterator<E> iterator() {
        return new EntryIterator<E>(this);
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
            if (cnt++ == index) {
                return entry;
            }
        }
        throw new NoSuchElementException(
                String.format("invalid index %d : out of %d entries", index, mEntries.size()));
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
        private boolean mIsChecked = false;

        private Entry(E elem) {
            mElem = elem;
        }

        public String toString() {
            return mElem.toString();
        }
    }
}
