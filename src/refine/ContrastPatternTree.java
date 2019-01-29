package refine;



import refine.context.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

// Tested
public class ContrastPatternTree {
    private static final ContrastPatterTreeNode NULL = ContrastPatterTreeNode.NullContrastPatterTreeNode.NULL;
    private static final Context CONTEXT = Context.getInstance();
    private ContrastPatterTreeNode root;
    public void addTree(ContrastPatterTreeNode newNode) {
        addTree(this.root, newNode);
    }
    public void preTraverse() {
        LinkedList<ContrastPatterTreeNode> stack = new LinkedList<>();
        stack.addLast(root);
        while (!stack.isEmpty()) {
            ContrastPatterTreeNode node = stack.pollLast();
            System.out.println("preTraverse : (" + node.value + "," + node.c1 + "," + node.c2 + ")");
            if (node.childrenSize() > 0) {
                for (int j = node.childrenSize() - 1; j >= 0; j--) {
                    stack.addLast(node.getChild(j));
                }
            }
        }
    }
    public void postTraverse() {
        int postIndex = 0;
        LinkedList<ContrastPatterTreeNode> stack = new LinkedList<>();
        stack.addLast(root);
        while (!stack.isEmpty()) {
            ContrastPatterTreeNode curr = stack.peekLast();
            while (curr.childrenSize() > 0 && !curr.getChild(0).isVisited) {
                curr = curr.getChild(0);
                stack.addLast(curr);
            }
            ContrastPatterTreeNode node = stack.pollLast();
            System.out.println("preTraverse : (" + node.value + "," + node.c1 + "," + node.c2 + ")");
            node.setVisited(true);
            if (!node.getSibling().isNull()) {
                stack.addLast(node.getSibling());
            }
        }
    }
    public ContrastPatterTreeNode getRoot() {
        return root;
    }
    public void addTree(ContrastPatterTreeNode root, ContrastPatterTreeNode newNode) {
        if (root.isNull() || newNode.isNull()) return;
        ContrastPatterTreeNode toadd = newNode;
        if (root.childrenSize() == 0) {
            while (!toadd.isNull()) {
                root.addChild(toadd);
                toadd = toadd.sibling;
            }
        } else {
            while (!toadd.isNull()) {
                int j;
                for (j = 0; j < root.childrenSize(); j++) {
                    ContrastPatterTreeNode currChild = root.getChild(j);
                    if (currChild.value.equals(toadd.value)) {
                        currChild.c1 = toadd.c1 + currChild.c1;
                        currChild.c2 = toadd.c2 + currChild.c2;
                        addTree(currChild, toadd.childrenSize() == 0 ? NULL : toadd.getChild(0));
                        break;
                    }
                }
                ContrastPatterTreeNode next = toadd.sibling;
                if (j == root.childrenSize()) {
                    root.getChildren().get(root.childrenSize() - 1).sibling = toadd;
                    toadd.sibling = NULL;
                    root.addChild(toadd);
                    root.sortChildren();
                }
                toadd = next;
            }
        }
    }

    public static ContrastPatternTree newTree() {
        return Factory.newTree();
    }
    public static void preTraverse(ContrastPatterTreeNode node) {
        Traverser.preTraverse(node);
    }
    public static void postTraverse(ContrastPatterTreeNode node) {
        Traverser.postTraverse(node);
    }

    public static class Factory {
        public static ContrastPatternTree newTree() {
            ContrastPatternTree tree = new ContrastPatternTree();
            tree.root = ContrastPatterTreeNode.Factory.newNode("$");
            BufferedReader br = FunctorFactory.getBufferReaderGetter().apply(CONTEXT.getMixedDatasetFile());
            String line;
            try {
                while (null != (line = br.readLine()) && !"".equals(line = line.trim())) {
                    String[] transactionAndclasstag = line.split(",");
                    String transaction = transactionAndclasstag[0];
                    String classTag = transactionAndclasstag[1];
                    ContrastPatterTreeNode newNode = ContrastPatterTreeNode.Factory
                                         .getFromTransaction(transaction, classTag);
                    tree.addTree(newNode);
                }
            } catch (IOException e) {
                throw new RuntimeException("读取异常");
            } finally {
                if (null != br) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        throw new RuntimeException("关闭失败");
                    }
                }
            }
            return tree;
        }
    }
    public static class Traverser {
        public static void preTraverse(ContrastPatternTree.ContrastPatterTreeNode node) {
            if (node.isNull()) return;
            System.out.println("preTraverse : (" + node.getValue() + "," + node.getC1() + "," + node.getC2() + ")");
            for (ContrastPatternTree.ContrastPatterTreeNode child : node.getChildren()) {
                preTraverse(child);
            }
        }

        public static void postTraverse(ContrastPatternTree.ContrastPatterTreeNode node) {
            if (node.isNull()) return;
            for (ContrastPatternTree.ContrastPatterTreeNode child : node.getChildren()) {
                postTraverse(child);
            }
            System.out.println("postTraverse : (" + node.getValue() + "," + node.getC1() + "," + node.getC2() + ")");
        }
    }
    public static class ContrastPatterTreeNode implements Cloneable {
        private String value;
        private Integer c1;
        private Integer c2;
        private Integer preIndex;
        private Integer postIndex;
        private Boolean isVisited;
        private ContrastPatterTreeNode parent;
        private ContrastPatterTreeNode sibling;
        private List<ContrastPatterTreeNode> children;

        private ContrastPatterTreeNode() {}
        public String getValue() {
            return value;
        }
        public void setValue(String value) {
            this.value = value;
        }
        public Integer getC1() {
            return c1;
        }
        public void setC1(Integer c1) {
            this.c1 = c1;
        }
        public Integer getC2() {
            return c2;
        }
        public void setC2(Integer c2) {
            this.c2 = c2;
        }
        public Integer getPreIndex() {
            return preIndex;
        }
        public void setPreIndex(Integer preIndex) {
            this.preIndex = preIndex;
        }
        public Integer getPostIndex() {
            return postIndex;
        }
        public void setPostIndex(Integer postIndex) {
            this.postIndex = postIndex;
        }
        public Boolean getVisited() {
            return isVisited;
        }
        public void setVisited(Boolean visited) {
            isVisited = visited;
        }
        public ContrastPatterTreeNode getParent() {
            return parent;
        }
        public void setParent(ContrastPatterTreeNode parent) {
            this.parent = parent;
            parent.addChild(this);
        }
        public void setSibling(ContrastPatterTreeNode sibling) {
            this.sibling = sibling;
            parent.addChild(sibling);
            sibling.parent = parent;
        }
        public List<ContrastPatterTreeNode> getChildren() {
            return children;
        }

        public void sortChildren() {
            Map<String, Integer> itemCount = CONTEXT.getItemcountMap();
            Collections.sort(this.children, (o1, o2) -> {
                if (!itemCount.get(o1.getValue()).equals(itemCount.get(o2.getValue()))) {
                    return itemCount.get(o2.getValue()).compareTo(itemCount.get(o1.getValue()));
                } else {
                    return o1.getValue().compareTo(o2.getValue());
                }
            });
            for (int k = 1; k < childrenSize(); k++) {
                ContrastPatterTreeNode prev = getChild(k - 1);
                ContrastPatterTreeNode now = getChild(k);
                prev.sibling = now;
                now.sibling = NULL;
            }
        }
        public ContrastPatterTreeNode getChild(int index) {
            return children.get(index);
        }
        public int childrenSize() {
            return children.size();
        }
        public ContrastPatterTreeNode getSibling() {
            return sibling;
        }
        public void addChild(ContrastPatterTreeNode child) {
            if (children.size() > 0) {
                children.get(children.size() - 1).sibling = child;
            }
            children.add(child);
            child.parent = this;
        }
        public boolean isNull() {
            return false;
        }
        public ContrastPatterTreeNode copy() {
            ContrastPatterTreeNode newNode;
            try {
                newNode = this.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException("克隆出错");
            }
            return newNode;
        }
        @Override
        protected ContrastPatterTreeNode clone() throws CloneNotSupportedException {
            ContrastPatterTreeNode newNode = (ContrastPatterTreeNode) super.clone();
            newNode.parent = NULL;
            if (!this.sibling.isNull()) {
                ContrastPatterTreeNode sibling = newNode.sibling.clone();
                newNode.sibling = sibling;
            }
            newNode.children = new ArrayList<>(3);
            if (children.size() > 0) {
                for (ContrastPatterTreeNode child : children) {
                    ContrastPatterTreeNode newChild = child.clone();
                    newNode.addChild(newChild);
                }
            }
            return newNode;
        }
        @Override
        public String toString() {
            return "(" +
                    value + "," +
                    preIndex + "," +
                    postIndex + "," +
                    c1 + "," +
                    c2 + "," +
                    isVisited +
                    ')';
        }

        public static class Factory {
            public static ContrastPatterTreeNode newNode() {
                ContrastPatterTreeNode node = new ContrastPatterTreeNode();
                node.c1 = 0;
                node.c2 = 0;
                node.preIndex = -1;
                node.postIndex = -1;
                node.isVisited = false;
                node.parent = NULL;
                node.sibling = NULL;
                node.children = new ArrayList<>(3);
                return node;
            }

            public static ContrastPatterTreeNode newNode(String value) {
                ContrastPatterTreeNode node = newNode();
                node.value = value;
                return node;
            }

            public static ContrastPatterTreeNode getFromTransaction(Transaction transaction, String classTag) {
                List<ContrastPatterTreeNode> nodes = new ArrayList<>();
                for (int i = 0; i < transaction.size(); i++) {
                    Transaction.Item item = transaction.get(i);
                    ContrastPatterTreeNode node = newNode(item.getValue());
                    if ("1".equals(classTag)) {
                        node.setC1(1);
                    } else {
                        node.setC2(1);
                    }
                    nodes.add(node);
                }
                int nodeSize = nodes.size();
                if (nodeSize >= 1) {
                    if (nodeSize > 1) {
                        for (int i = 1; i < nodes.size(); i++) {
                            ContrastPatterTreeNode prev = nodes.get(i - 1);
                            ContrastPatterTreeNode curr = nodes.get(i);
                            prev.addChild(curr);
                        }
                    }
                    return nodes.get(0);
                }
                return null;
            }

            public static ContrastPatterTreeNode getFromTransaction(String transaction, String classTag) {
                return getFromTransaction(Transaction.Factory.create(transaction.trim()), classTag);
            }
        }
        private static class NullContrastPatterTreeNode extends ContrastPatterTreeNode {
            public static final ContrastPatterTreeNode NULL = new NullContrastPatterTreeNode();

            private NullContrastPatterTreeNode() {
            }

            @Override
            public boolean isNull() {
                return true;
            }

            @Override
            protected ContrastPatterTreeNode clone() {
                throw new RuntimeException("Method not supported");
            }

            @Override
            public String toString() {
                return "NULL";
            }
        }
    }
}
