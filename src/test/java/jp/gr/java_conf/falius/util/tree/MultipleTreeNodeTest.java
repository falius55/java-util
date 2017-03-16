package jp.gr.java_conf.falius.util.tree;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

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
}
