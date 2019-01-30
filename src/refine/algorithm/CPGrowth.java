package refine.algorithm;

import refine.ContrastPatternTree;
import refine.context.Context;

import java.util.ArrayList;
import java.util.List;

public class CPGrowth {
    private double alpha;
    private double beta;

    private ContrastPatternTree tree;

    public CPGrowth(double alpha, double beta) {
        this.alpha = alpha;
        this.beta = beta;
    }


    public void mine() {
        this.tree = ContrastPatternTree.newTree();
        ContrastPatternTree.ContrastPatterTreeNode root = tree.getRoot();
        for (int i = 0; i < root.childrenSize(); i++) {
            ContrastPatternTree.ContrastPatterTreeNode currRootChild = root.getChild(i);                                                                         //root的直接子节点
            if (currRootChild.childrenSize() == 0) continue;
            ContrastPatternTree.ContrastPatterTreeNode firstChild = currRootChild.getChild(0).copy();
            tree.addTree(firstChild);
            root.sortChildren();


            i = i + (mineCpFromNode(currRootChild) ? -1 : 0);                                                                    //如果删除的是直接孩子节点，那么必须退一格
        }
        //ContrastPatternTree.preTraverse(root);
    }

    /*————————————————————————
   | 从指的节点开始挖掘对比模式 |
    ————————————————————————*/
    public boolean mineCpFromNode(ContrastPatternTree.ContrastPatterTreeNode node) {                                    //返回-1说明删掉了一个节点
        return mineTraverse(new ArrayList<>(), node) == -1;
    }

    private int mineTraverse(List<ContrastPatternTree.ContrastPatterTreeNode> prefix, ContrastPatternTree.ContrastPatterTreeNode top) {
        Context context = Context.getInstance();
        int n1 = context.getN1();
        int n2 = context.getN2();
        if (top.isNull()) {
            return 0;
        } else {
            if (isContrastPattern(top)) {                                                                               //如果当前是对比模式
                prefix.add(top);                                                                                        //添加到前缀
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < prefix.size(); i++) {
                    sb.append(prefix.get(i).getValue()).append(" ");
                }
                System.out.println(String.format("1 - %s, [%d %d], [%.3f %.3f]", sb.toString().trim(), top.getC1(), top.getC2(), n1 * alpha, n2 * beta));
            } else if (!canPrune(top)) {
                prefix.add(top);                                                                                        //添加到前缀
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < prefix.size(); i++) {
                    sb.append(prefix.get(i).getValue()).append(" ");
                }
                System.out.println(String.format("2 - %s, [%d %d], [%.3f %.3f]", sb.toString().trim(), top.getC1(), top.getC2(), n1 * alpha, n2 * beta));
            } else {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < prefix.size(); i++) {
                    sb.append(prefix.get(i).getValue()).append(" ");
                }
                sb.append(top.getValue());
                System.out.println(String.format("3 - %s, [%d %d], [%.3f %.3f]", sb.toString().trim(), top.getC1(), top.getC2(), n1 * alpha, n2 * beta));

                ContrastPatternTree.ContrastPatterTreeNode topParent = top.getParent();

                for (int i = 0; i < topParent.childrenSize(); i++) {
                    ContrastPatternTree.ContrastPatterTreeNode node = topParent.getChild(i);
                    if (node.getSibling() == top) {
                        node.setSibling(top.getSibling());
                        break;
                    }
                    if (node == top) {
                        break;
                    }
                }
                topParent.getChildren().remove(top);
                return -1;
            }
        }
        List<ContrastPatternTree.ContrastPatterTreeNode> topChildren = top.getChildren();
        if (topChildren.size() == 0) {
            prefix.remove(prefix.size() - 1);
            return 0;
        }
        for (int i = 0; i < topChildren.size(); i++) {
            ContrastPatternTree.ContrastPatterTreeNode currChild = topChildren.get(i);
            int response = mineTraverse(prefix, currChild);
            if (response == -1) {
                i--;
            }
        }
        prefix.remove(prefix.size() - 1);
        return 0;
    }

    /*——————————————-
   |判断是不是对比模式|
    ——————————————-*/
    private boolean isContrastPattern(ContrastPatternTree.ContrastPatterTreeNode node) {
        Context context = Context.getInstance();
        int n1 = context.getN1();
        int n2 = context.getN2();
        boolean result = (node.getC1() > alpha * n1 && node.getC2() <= beta * n2) || (node.getC2() > alpha * n2 && node.getC1() <= beta * n1);
        return result;
    }

    /*——————————————-
    | 判断是不是能剪枝 |
     ——————————————-*/
    private boolean canPrune(ContrastPatternTree.ContrastPatterTreeNode node) {
        Context context = Context.getInstance();
        int n1 = context.getN1();
        int n2 = context.getN2();
        boolean result = node.getC1() > alpha * n1 || node.getC2() > alpha * n2;
        return !result;
    }
}
