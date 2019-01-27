package refine;


import java.util.ArrayList;
import java.util.List;

// Tested
public class ContrastPatternTree {
    public static final ContrastPatterTreeNode NULL = ContrastPatterTreeNode.NullContrastPatterTreeNode.NULL;

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

        private ContrastPatterTreeNode() {
        }

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

        public ContrastPatterTreeNode getChild(int index) {
            return children.get(index);
        }

        public int size() {
            return children.size();
        }

        public ContrastPatterTreeNode getSibling() {
            return sibling;
        }

        public void setSibling(ContrastPatterTreeNode sibling) {
            this.sibling = sibling;
            parent.addChild(sibling);
            sibling.parent = parent;

        }

        public List<ContrastPatterTreeNode> getChildren() {
            return children;
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

    static class ContrastPatternTreeNodeFactory {
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
    }
}
