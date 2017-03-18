package jp.gr.java_conf.falius.util.tree;

import java.util.Collection;
import java.util.Set;

public interface TreeNode<E> extends Iterable<TreeNode<E>> {

    E getElem();

    Collection<? extends TreeNode<E>> children();

    TreeNode<E> getParent();

    TreeNode<E> find(Predicate<TreeNode<E>> func);

    Set<TreeNode<E>> findAll(Predicate<TreeNode<E>> func);

    public interface Predicate<T> {

        public boolean test(T t);
    }
}
