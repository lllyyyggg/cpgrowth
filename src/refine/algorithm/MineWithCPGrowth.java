package refine.algorithm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import refine.datastructure.ContrastPatternTree;
import refine.context.Context;

import java.util.LinkedList;
public class MineWithCPGrowth implements MiningAlgorithm{
    private static final Logger LOGGER = LoggerFactory.getLogger(MineWithCPGrowth.class);
    private static final ThreadLocal<Integer> total = ThreadLocal.withInitial(()->0);
    private double alpha;
    private double beta;
    public MineWithCPGrowth(double alpha, double beta) {
        this.alpha = alpha;
        this.beta = beta;
    }
    @Override
    public void mine() {
        LOGGER.info("————————————STARTING CPGROWTH ALGORITHM————————————");
        Context context = Context.getInstance();
        int n1 = context.getN1();
        int n2 = context.getN2();
        ContrastPatternTree tree = context.getTree();
        ContrastPatternTree.ContrastPatterTreeNode root = tree.getRoot();
        for (int i = 0; i < root.childrenSize(); i++) {
            ContrastPatternTree.ContrastPatterTreeNode currRootChild = root.getChild(i);                                                                         //root的直接子节点
            if (currRootChild.childrenSize() > 0) {
                // Copy with cloning
                // ContrastPatternTree.ContrastPatterTreeNode firstChild = currRootChild.getChild(0).copyWithClone();
                // Copy without cloning
                ContrastPatternTree.ContrastPatterTreeNode currentRootChildCopy = ContrastPatternTree.copy(currRootChild);
                ContrastPatternTree.ContrastPatterTreeNode firstChild = currentRootChildCopy.getChild(0);
                tree.addTree(firstChild);
                root.sortChildren();
            }
            //mine(currRootChild, n1, n2);
            mineRecursively(currRootChild, n1, n2);
        }
        LOGGER.info("TOTAL : {}, N1: {}, N2: {}, ALPHA: {}, BETA: {}", total.get(), n1, n2, alpha, beta);
    }
    private void mine(ContrastPatternTree.ContrastPatterTreeNode head, int n1, int n2) {
        LinkedList<ContrastPatternTree.ContrastPatterTreeNode> stack = new LinkedList<>();
        stack.addLast(head);
        while (!stack.isEmpty()) {
            ContrastPatternTree.ContrastPatterTreeNode node = stack.pollLast();
            String sequence = getSequence(node);
            if (isContrastPattern(node)) {
                total.set(total.get() + 1);
                LOGGER.info("1 - {}, [{} {}], [{} {}]", sequence, node.getC1(), node.getC2(), n1 * alpha, n2 * beta);
            } else if (!canPrune(node)) {
                LOGGER.info("2 - {}, [{} {}], [{} {}]", sequence, node.getC1(), node.getC2(), n1 * alpha, n2 * beta);
            } else {
                LOGGER.info("3 - {}, [{} {}], [{} {}]", sequence, node.getC1(), node.getC2(), n1 * alpha, n2 * beta);
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
    }
    private boolean mineRecursively(ContrastPatternTree.ContrastPatterTreeNode head, int n1, int n2) {
        if (head.isNull()) return false;
        if(!head.isRootChild()){
            StringBuilder sb = new StringBuilder(head.getParent().getValue()).append(" ").append(head.getValue());
            head.setValue(sb.toString());
        }
        //if (isContrastPattern(head)) {
        if (head.isContrastPattern(alpha, beta)) {
            total.set(total.get() + 1);
            LOGGER.info("1 - {}, [{} {}], [{} {}]", head.getValue(), head.getC1(), head.getC2(), n1 * alpha, n2 * beta);
        }else if (!head.canPrune(alpha)) {
            LOGGER.info("2 - {}, [{} {}], [{} {}]", head.getValue(), head.getC1(), head.getC2(), n1 * alpha, n2 * beta);
        } else {
            LOGGER.info("3 - {}, [{} {}], [{} {}]", head.getValue(), head.getC1(), head.getC2(), n1 * alpha, n2 * beta);
            ContrastPatternTree.ContrastPatterTreeNode parent = head.getParent();
            if (!head.isRootChild()) parent.removeChild(head);
            return true;
        }
        for(int j = 0; j < head.childrenSize(); j++) {
           if(mineRecursively(head.getChild(j), n1, n2)) j--;
        }
        return false;
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
}
