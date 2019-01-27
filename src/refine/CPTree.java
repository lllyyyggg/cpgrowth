package refine;


import java.util.ArrayList;
import java.util.List;

// Tested
public class CPTree {
    public static final CPTreeNode NULL = CPTreeNode.NullCpTreeNode.NULL;

    public static class CPTreeNode implements Cloneable {
        private String value;
        private Integer c1;
        private Integer c2;
        private Integer preIndex;
        private Integer postIndex;
        private Boolean isVisited;
        private CPTreeNode parent;
        private CPTreeNode sibling;
        private List<CPTreeNode> children;

        private CPTreeNode() {
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

        public CPTreeNode getParent() {
            return parent;
        }

        public void setParent(CPTreeNode parent) {
            this.parent = parent;
            parent.addChild(this);
        }

        public CPTreeNode getChild(int index) {
            return children.get(index);
        }

        public int size() {
            return children.size();
        }

        public CPTreeNode getSibling() {
            return sibling;
        }

        public void setSibling(CPTreeNode sibling) {
            this.sibling = sibling;
            parent.addChild(sibling);
            sibling.parent = parent;

        }

        public List<CPTreeNode> getChildren() {
            return children;
        }

        public void addChild(CPTreeNode child) {
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
        protected CPTreeNode clone() throws CloneNotSupportedException {
            CPTreeNode newNode = (CPTreeNode) super.clone();
            newNode.parent = NULL;
            if (!this.sibling.isNull()) {
                CPTreeNode sibling = newNode.sibling.clone();
                newNode.sibling = sibling;
            }
            newNode.children = new ArrayList<>(3);
            if (children.size() > 0) {
                for (CPTreeNode child : children) {
                    CPTreeNode newChild = child.clone();
                    newNode.addChild(newChild);
                }
            }
            return newNode;
        }

        public CPTreeNode copy() {
            CPTreeNode newNode;
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

        private static class NullCpTreeNode extends CPTreeNode {
            public static final CPTreeNode NULL = new NullCpTreeNode();

            private NullCpTreeNode() {
            }

            @Override
            public boolean isNull() {
                return true;
            }

            @Override
            protected CPTreeNode clone() {
                throw new RuntimeException("Method not supported");
            }

            @Override
            public String toString() {
                return "NULL";
            }
        }

    }

    static class CPTreeNodeFactory {
        public static CPTreeNode newNode() {
            CPTreeNode node = new CPTreeNode();
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

        public static CPTreeNode newNode(String value) {
            CPTreeNode node = newNode();
            node.value = value;
            return node;
        }
    }
}
