package jp.gr.java_conf.falius.util.tree;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

public class MultipleTreeNodeTest {

    @Test
    public void childAddingTest() {
        List<String> children = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            children.add("child" + i);
        }

        TreeNode<String> root = new MultipleTreeNode<>("root");
        for (String strChild : children) {
            root.addChild(strChild);
        }

        for (TreeNode<String> child : root.children()) {
            assertThat(child.getParent().getElem(), is(root.getElem()));
            assertTrue(children.contains(child.getElem()));
        }

    }

    @Test
    public void iteratorTest() {
        int i_len = 4;
        int j_len = 6;
        int k_len = 10;

        TreeNode<String> root = new MultipleTreeNode<>("-1");
        for (int i = 0; i < i_len; i++) {
            TreeNode<String> childNode = root.addChild(Integer.toString(i));
            for (int j = 0; j < j_len; j++) {
                TreeNode<String> grandChildNode
                    = childNode.addChild(childNode.getElem() + "-" + j);
                for (int k = 0; k < k_len; k++) {
                    grandChildNode.addChild(grandChildNode.getElem() + "-" + k);
                }
            }
        }

        Set<String> expected = new HashSet<>();
        expected.add("-1");
        for (int i = 0; i < i_len; i++) {
            String childValue = Integer.toString(i);
            expected.add(childValue);
            for (int j = 0; j < j_len; j++) {
                String grandChildValue = childValue + "-" + j;
                    expected.add(grandChildValue);
                for (int k = 0; k < k_len; k++) {
                    expected.add(grandChildValue + "-" + k);
                }
            }
        }

        int cnt = 0;
        for (TreeNode<String> elem : root) {
            assertThat(expected, hasItem(elem.getElem()));
            cnt++;
        }

        assertThat(cnt, is(1 + (1 * i_len) + (1 * i_len * j_len) + (1 * i_len * j_len * k_len)));
    }
}
