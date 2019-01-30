package refine.algorithm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import refine.ContrastPatternTree;
import refine.context.Context;

import java.util.LinkedList;

public class CPGrowth {
    private static final Logger LOGGER = LoggerFactory.getLogger(CPGrowth.class);
    private double alpha;
    private double beta;
    private ContrastPatternTree tree;
    public CPGrowth(double alpha, double beta) {
        this.alpha = alpha;
        this.beta = beta;
    }
    public void mine() {
        LOGGER.info("————————————STARTING CPGROWTH ALGORITHM————————————");
        Context context = Context.getInstance();
        int n1 = context.getN1();
        int n2 = context.getN2();
        int total = 0;
        this.tree = ContrastPatternTree.newTree();
        ContrastPatternTree.ContrastPatterTreeNode root = tree.getRoot();
        for (int i = 0; i < root.childrenSize(); i++) {
            ContrastPatternTree.ContrastPatterTreeNode currRootChild = root.getChild(i);                                                                         //root的直接子节点
            if (currRootChild.childrenSize() > 0) {
                ContrastPatternTree.ContrastPatterTreeNode firstChild = currRootChild.getChild(0).copy();
                //ContrastPatternTree.ContrastPatterTreeNode firstChild = ContrastPatternTree.copy(currRootChild.getChild(0));
                tree.addTree(firstChild);
                root.sortChildren();
            }
            total += mine(currRootChild);
        }
        LOGGER.info("TOTAL : {}, N1: {}, N2: {}, ALPHA: {}, BETA: {}", total, n1, n2, alpha, beta);
    }
    private int mine(ContrastPatternTree.ContrastPatterTreeNode head) {
        int sum = 0;
        LinkedList<ContrastPatternTree.ContrastPatterTreeNode> stack = new LinkedList<>();
        stack.addLast(head);
        Context context = Context.getInstance();
        int n1 = context.getN1();
        int n2 = context.getN2();
        while (!stack.isEmpty()) {
            ContrastPatternTree.ContrastPatterTreeNode node = stack.pollLast();
            String sequence = getSequence(node);
            if (isContrastPattern(node)) {
                sum++;
                LOGGER.info("1 - {}, [{} {}], [{} {}]", sequence, node.getC1(), node.getC2(), n1 * alpha, n2 * beta);
            } else if (!canPrune(node)) {
                //LOGGER.info("2 - {}, [{} {}], [{} {}]", sequence, node.getC1(), node.getC2(), n1 * alpha, n2 * beta);
            } else {
                //LOGGER.info("3 - {}, [{} {}], [{} {}]", sequence, node.getC1(), node.getC2(), n1 * alpha, n2 * beta);
                ContrastPatternTree.ContrastPatterTreeNode parent = node.getParent();
                if (!node.isRootChild()) parent.removeChild(node);
                continue;
            }
            if (node.childrenSize() > 0) {
                for (int j = node.childrenSize() - 1; j >= 0; j--) {
                    stack.addLast(node.getChild(j));
                }
            }
        }
        return sum;
    }
    private String getSequence(ContrastPatternTree.ContrastPatterTreeNode node) {
        LinkedList<String> list = new LinkedList<>();
        ContrastPatternTree.ContrastPatterTreeNode last = node;
        while (!last.isRoot()) {
            list.addFirst(last.getValue());
            last = last.getParent();
        }
        return list.toString();
    }
    private boolean isContrastPattern(ContrastPatternTree.ContrastPatterTreeNode node) {
        Context context = Context.getInstance();
        int n1 = context.getN1();
        int n2 = context.getN2();
        boolean result = (node.getC1() > alpha * n1 && node.getC2() <= beta * n2) || (node.getC2() > alpha * n2 && node.getC1() <= beta * n1);
        return result;
    }
    private boolean canPrune(ContrastPatternTree.ContrastPatterTreeNode node) {
        Context context = Context.getInstance();
        int n1 = context.getN1();
        int n2 = context.getN2();
        boolean result = node.getC1() > alpha * n1 || node.getC2() > alpha * n2;
        return !result;
    }

    public static void main(String[] args) {
        CPGrowth cpGrowth = new CPGrowth(0.6d, 0.05d);
    }
}
