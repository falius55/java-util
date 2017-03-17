package jp.gr.java_conf.falius.util.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

public class MultipleTreeNode<E> implements TreeNode<E> {

    private MultipleTreeNode<E> mParent = null;
    private final E mElem;
    private final List<MultipleTreeNode<E>> mChildren = new ArrayList<>();

    public MultipleTreeNode(E elem) {
        mElem = elem;
    }

    public MultipleTreeNode<E> addChild(E child) {
        MultipleTreeNode<E> childNode = new MultipleTreeNode<E>(child);
        return addChild(childNode);
    }

    public MultipleTreeNode<E> addChild(MultipleTreeNode<E> child) {
        if (mChildren.contains(child)) {
            return mChildren.get(mChildren.indexOf(child));
        }

        mChildren.add(child);

        if (Objects.nonNull(child.mParent)) {
            child.mParent.mChildren.remove(child);
        }
        child.mParent = this;

        return child;
    }

    @Override
    public E getElem() {
        return mElem;
    }

    @Override
    public MultipleTreeNode<E> getParent() {
        return mParent;
    }

    @Override
    public Collection<MultipleTreeNode<E>> children() {
        return Collections.unmodifiableCollection(mChildren);
    }

    @Override
    public TreeNode<E> find(Predicate<TreeNode<E>> func) {
        for(TreeNode<E> node : this) {
            if(func.test(node)) {
                return node;
            }
        }
        return null;
    }

    @Override
    public Set<TreeNode<E>> findAll(Predicate<TreeNode<E>> func) {
        Set<TreeNode<E>> ret = new HashSet<>();
        for (TreeNode<E> node : this) {
            if (func.test(node)) {
                ret.add(node);
            }
        }
        return ret;
    }

    @Override
    public int hashCode() {
        return mElem.hashCode();
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object another) {
        return another instanceof TreeNode && mElem.equals(((TreeNode<E>) another).getElem());
    }

    @Override
    public Iterator<TreeNode<E>> iterator() {
        return new TreeIterator<E>(this);
    }

    private MultipleTreeNode<E> getNextSibling() {
        if (Objects.isNull(mParent)) {
            return null;
        }

        int siblingIndex = mParent.mChildren.indexOf(this);
        if (mParent.mChildren.size() > siblingIndex + 1) {
            return mParent.mChildren.get(siblingIndex + 1);
        }
        return null;
    }

    private static class TreeIterator<E> implements Iterator<TreeNode<E>> {

        private MultipleTreeNode<E> mNext;

        private TreeIterator(MultipleTreeNode<E> startNode) {
            mNext = startNode;
        }

        @Override
        public boolean hasNext() {
            return mNext != null;
        }

        @Override
        public MultipleTreeNode<E> next() {
            MultipleTreeNode<E> ret = mNext;
            if (mNext.mChildren.size() > 0) {
                mNext = mNext.mChildren.get(0);
                return ret;
            }

            MultipleTreeNode<E> nextSibling = mNext.getNextSibling();
            if (nextSibling != null) {
                mNext = nextSibling;
                return ret;
            }

            MultipleTreeNode<E> parent = mNext.mParent;
            while (parent != null) {
                MultipleTreeNode<E> next = parent.getNextSibling();
                if (next != null) {
                    mNext = next;
                    return ret;
                }
                parent = parent.mParent;
            }

            mNext = null;
            return ret;
        }

    }
}
