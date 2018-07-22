package jp.gr.java_conf.falius.util.list;

/**
 *
 * @author "ymiyauchi"
 *
 * @param <T> 要素の型
 * @since 1.0
 * @version 1.2.0
 */
public interface Checkable<T> {

    void check(T e);
}
