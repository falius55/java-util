package jp.gr.java_conf.falius.util.tree;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

public class MultipleTreeNode<E> implements TreeNode<E> {
    private MultipleTreeNode<E> mParent = null;
    private final E mElem;
    private final Set<MultipleTreeNode<E>> mChildren = new HashSet<>();

    public MultipleTreeNode(E elem) {
        mElem = elem;
    }

    @Override
    public void addChild(E child) {
        MultipleTreeNode<E> childElem = new MultipleTreeNode<E>(child);
        addChild(childElem);
    }

    public void addChild(MultipleTreeNode<E> child) {
        mChildren.add(child);

        if (Objects.nonNull(child.mParent)) {
            child.mParent.mChildren.remove(child);
        }
        child.mParent = this;
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
    public Set<? extends TreeNode<E>> find(Predicate<TreeNode<E>> func) {
        Set<TreeNode<E>> ret = new HashSet<>();
        find(func, ret);
        return ret;
    }

    private void find(
            Predicate<TreeNode<E>> func, Set<? super MultipleTreeNode<E>> set) {
        for (MultipleTreeNode<E> child : mChildren) {
            if (func.test(child)) {
                set.add(child);
            }
            child.find(func, set);
        }
    }

    @Override
    public int hashCode() {
        return mElem.hashCode();
    }

    @Override
    public boolean equals(Object another) {
        return mElem.equals(another);
    }
}
