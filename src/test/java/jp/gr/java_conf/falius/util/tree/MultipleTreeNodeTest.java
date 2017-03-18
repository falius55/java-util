package jp.gr.java_conf.falius.util.tree;

public class MultipleTreeNodeTest {
//
//    @Test
//    public void childAddingTest() {
//        List<String> children = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            children.add("child" + i);
//        }
//
//        MultipleTreeNode<String> root = new MultipleTreeNode<>("root");
//        for (String strChild : children) {
//            root.addChild(strChild);
//        }
//
//        for (TreeNode<String> child : root.children()) {
//            assertThat(child.getParent().getElem(), is(root.getElem()));
//            assertTrue(children.contains(child.getElem()));
//        }
//
//    }
//
//    @Test
//    public void iteratorTest() {
//        int i_len = 4;
//        int j_len = 6;
//        int k_len = 10;
//
//        MultipleTreeNode<String> root = new MultipleTreeNode<>("-1");
//        for (int i = 0; i < i_len; i++) {
//            MultipleTreeNode<String> childNode = root.addChild(Integer.toString(i));
//            for (int j = 0; j < j_len; j++) {
//                MultipleTreeNode<String> grandChildNode
//                    = childNode.addChild(childNode.getElem() + "-" + j);
//                for (int k = 0; k < k_len; k++) {
//                    grandChildNode.addChild(grandChildNode.getElem() + "-" + k);
//                }
//            }
//        }
//
//        Set<String> expected = new HashSet<>();
//        expected.add("-1");
//        for (int i = 0; i < i_len; i++) {
//            String childValue = Integer.toString(i);
//            expected.add(childValue);
//            for (int j = 0; j < j_len; j++) {
//                String grandChildValue = childValue + "-" + j;
//                    expected.add(grandChildValue);
//                for (int k = 0; k < k_len; k++) {
//                    expected.add(grandChildValue + "-" + k);
//                }
//            }
//        }
//
//        int cnt = 0;
//        for (TreeNode<String> elem : root) {
//            assertThat(expected, hasItem(elem.getElem()));
//            cnt++;
//        }
//
//        assertThat(cnt, is(1 + (1 * i_len) + (1 * i_len * j_len) + (1 * i_len * j_len * k_len)));
//    }
//
//    @Test
//    public void equalsTest() {
//        TreeNode<String> strNode = new MultipleTreeNode<>("abc");
//        TreeNode<String> strNode2 = new MultipleTreeNode<>("abc");
//        assertThat(strNode, is(strNode2));
//
//        TreeNode<Integer> intNode = new MultipleTreeNode<>(10);
//        TreeNode<String> strNode3 = new MultipleTreeNode<>("10");
//        assertThat(intNode, is(not(strNode3)));
//    }
//
//    @Test
//    public void hashTest() {
//        Set<TreeNode<String>> set = new HashSet<>();
//
//        TreeNode<String> strNode = new MultipleTreeNode<>("aaa");
//        set.add(strNode);
//        assertTrue(set.contains(strNode));
//
//        TreeNode<String> strNode2 = new MultipleTreeNode<>("aaa");
//        assertTrue(set.contains(strNode2));
//
//        TreeNode<String> strNode3 = new MultipleTreeNode<>("bbb");
//        TreeNode<String> strNode4 = new MultipleTreeNode<>("ccc");
//
//        set.add(strNode);
//        set.add(strNode2);
//        set.add(strNode3);
//        set.add(strNode4);
//
//        assertThat(set.size(), is(3));
//    }
//
//    @Test
//    public void overlapChildTest() {
//        MultipleTreeNode<String> root = new MultipleTreeNode<>("root");
//        TreeNode<String> childA = root.addChild("aaa");
//        TreeNode<String> childB = root.addChild("bbb");
//        TreeNode<String> childC = root.addChild("ccc");
//        assertThat(root.children().size(), is(3));
//
//        TreeNode<String> childB2 = root.addChild("bbb");
//        assertThat(root.children().size(), is(3));
//        assertThat(childB, is(childB2));
//        assertThat(childB, sameInstance(childB2));
//
//        Queue<String> valueExpected = new ArrayDeque<>();
//        valueExpected.add("aaa");
//        valueExpected.add("bbb");
//        valueExpected.add("ccc");
//        Queue<TreeNode<String>> nodeExpected = new ArrayDeque<>();
//        nodeExpected.add(childA);
//        nodeExpected.add(childB);
//        nodeExpected.add(childC);
//
//        for (TreeNode<String> child : root.children()) {
//            String elem = valueExpected.poll();
//            assertThat(child.getElem(), is(elem));
//            TreeNode<String> node = nodeExpected.poll();
//            assertThat(child, sameInstance(node));
//        }
//    }
//
//    @Test
//    public void findTest() {
//        int i_len = 4;
//        int j_len = 6;
//        int k_len = 10;
//
//        MultipleTreeNode<String> root = new MultipleTreeNode<>("root");
//        for (int i = 0; i < i_len; i++) {
//            MultipleTreeNode<String> childNode = root.addChild(Integer.toString(i));
//            for (int j = 0; j < j_len; j++) {
//                MultipleTreeNode<String> grandChildNode
//                    = childNode.addChild(childNode.getElem() + "-" + j);
//                for (int k = 0; k < k_len; k++) {
//                    grandChildNode.addChild(grandChildNode.getElem() + "-" + k);
//                }
//            }
//        }
//
//        TreeNode<String> foundRoot = root.find(node -> node.getElem().equals("root"));
//        assertThat(foundRoot, sameInstance(root));
//
//        TreeNode<String> node3_4 = root.find(node -> node.getElem().equals("3-4"));
//        assertThat(node3_4.getElem(), is("3-4"));
//
//        TreeNode<String> parentNode = root.find(node -> node.equals(node3_4.getParent()));
//        assertThat(parentNode.getElem(), is("3"));
//
//        TreeNode<String> nonExistsNode = root.find(node -> node.getElem().length() > 100);
//        assertThat(nonExistsNode, nullValue());
//
//        Set<TreeNode<String>> oneContainningNode = root.findAll(node -> node.getElem().contains("1"));
//        assertThat(oneContainningNode.size(), is((1 + j_len + j_len * k_len) + (1 + k_len + j_len - 1) * (i_len - 1)));
//
//        Set<TreeNode<String>> childrenOf3 = root.findAll(node ->
//            Objects.nonNull(node.getParent()) && node.getParent().equals(parentNode));
//        for (int i = 0; i < j_len; i++) {
//            assertThat(childrenOf3, hasItem(new MultipleTreeNode<String>("3-" + i)));
//        }
//    }
}