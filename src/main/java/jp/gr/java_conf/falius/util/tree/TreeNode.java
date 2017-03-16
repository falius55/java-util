package jp.gr.java_conf.falius.util.tree;

import java.util.Collection;
import java.util.Set;
import java.util.function.Predicate;

public interface TreeNode<E> {

    void addChild(E child);

    E getElem();

    Collection<? extends TreeNode<E>> children();

    TreeNode<E> getParent();

    Set<? extends TreeNode<E>> find(Predicate<TreeNode<E>> func);
}
