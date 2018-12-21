package com.lanyage.datamining.entity;

import com.lanyage.datamining.datastructure.CPTreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * CPGrowth 算法
 */
public class CPGrowth {

    public static final Logger LOGGER = LoggerFactory.getLogger(CPGrowth.class);
    public static final double MINIMAL_THRESHOLD = 0.7d;
    public static final double MAXIMUM_THRESHOLD = 0.3d;


    private int n1;
    private int n2;
    private Map<Object, Integer> nodeCountMap;


    public CPGrowth(Map<Object, Integer> nodeCountMap, int n1, int n2) {
        this.n1 = n1;
        this.n2 = n2;
        this.nodeCountMap = nodeCountMap;
    }

    /*————————————————————————
    |将所有的后缀融合到CPTree上去|
     —————————————————————————*/
    public void mergeAndMine(CPTreeNode<Object> root) {
        LOGGER.info("———————————————————————————————————————————————————————————————————————————————————————————————————the beginning of merging the subtrees to the cp tree");
        for (int i = 0; i < root.children().size(); i++) {
            CPTreeNode<Object> node = root.children().get(i);                                                           //root的直接子节点
            //————————————————————————————————————————————————————————————————
            List<CPTreeNode<Object>> nodeChildren = node.children();
            TreeAppender treeAppender = new TreeAppender();
            for (int j = 0; j < nodeChildren.size(); j++) {
                CPTreeNode<Object> subHead = nodeChildren.get(j);
                CPTreeNode<Object> toAdd = copyTree(subHead);                                                           //拷贝子树
                LOGGER.info("———————————————————————————————————merge—————————————————————————————————————————");
                LOGGER.info("SUBHEAD {} APPENDED TO {}", toAdd, root);
                treeAppender.addTreeToTree(toAdd, root);
                LOGGER.info("—————————————————————————————————merge——end——————————————————————————————————————");
                Collections.sort(root.children(), (o1, o2) -> {
                    if (!this.nodeCountMap.get(o1.value()).equals(this.nodeCountMap.get(o2.value()))) {
                        return this.nodeCountMap.get(o2.value()).compareTo(this.nodeCountMap.get(o1.value()));
                    } else {
                        return ((Comparable) o1.value()).compareTo(o2.value());
                    }
                });
                for (int k = 1; k < root.children().size(); k++) {
                    CPTreeNode<Object> prev = root.children().get(k - 1);
                    CPTreeNode<Object> now = root.children().get(k);
                    prev.setSibling(now);
                    now.setSibling(null);
                }
            }
            LOGGER.info("————————————————————————————————mine {}——————————————————————————————————", node);
            i = i + (mineCpFromNode(node) ? -1 : 0);                                                                    //如果删除的是直接孩子节点，那么必须退一格
            LOGGER.info("—————————————————————————————————mine——end———————————————————————————————————————");
        }
        LOGGER.info("———————————————————————————————————————————————————————————————————————————————————————————————————the end of merging the subtrees to the cp tree");
    }

    /*———————————————————————————————————
    |根据head节点拷贝树，返回拷贝的树的头节点|
     ———————————————————————————————————*/
    private CPTreeNode<Object> copyTree(CPTreeNode<Object> head) {
        CPTreeNode<Object> newHead = new CPTreeNode<>();
        newHead.setValue(head.value());
        newHead.setC1(head.c1());
        newHead.setC2(head.c2());
        List<CPTreeNode<Object>> nodeList = new ArrayList<>();
        nodeList.add(newHead);
        copySubTree(nodeList, head);
        return newHead;
    }


    /*——————————————-
    |判断是不是对比模式|
     ——————————————-*/
    private boolean isContrastPattern(CPTreeNode<Object> node) {
        //double supportOfD1 = node.supportOfD1(_1total);
        //double supportOfD2 = node.supportOfD2(_2total);
        //boolean result = (supportOfD1 > MINIMAL_THRESHOLD && supportOfD2 <= MAXIMUM_THRESHOLD) || (supportOfD2 > MINIMAL_THRESHOLD && supportOfD1 <= MAXIMUM_THRESHOLD);
        boolean result = (node.c1() > MINIMAL_THRESHOLD * this.n1 && node.c2() <= MAXIMUM_THRESHOLD * this.n2) || (node.c2() > MINIMAL_THRESHOLD * this.n2 && node.c1() <= MAXIMUM_THRESHOLD * this.n1);
        return result;
    }

    /*——————————————-
    | 判断是不是能剪枝 |
     ——————————————-*/
    private boolean canPrune(CPTreeNode<Object> node) {
        //double supportOfD1 = node.supportOfD1(_1total);
        //double supportOfD2 = node.supportOfD2(_2total);
        //boolean result = supportOfD1 > MINIMAL_THRESHOLD || supportOfD2 > MINIMAL_THRESHOLD;
        boolean result = node.c1() > MINIMAL_THRESHOLD * this.n1 || node.c2() > MINIMAL_THRESHOLD * this.n2;
        return !result;
    }

    /*————————————————————————
   | 从指的节点开始挖掘对比模式 |
    ————————————————————————*/
    public boolean mineCpFromNode(CPTreeNode<Object> node) {                                                                //返回-1说明删掉了一个节点
        return mineTraverse(new ArrayList<>(), node) == -1;
    }

    private int mineTraverse(List<CPTreeNode<Object>> prefix, CPTreeNode<Object> top) {
        if (top == null) {
            return 0;
        } else {
            //DecimalFormat decimalFormat = new DecimalFormat("#0.0000");
            if (isContrastPattern(top)) {                                                                               //如果当前是对比模式
                prefix.add(top);                                                                                        //添加到前缀
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < prefix.size(); i++) {
                    sb.append(prefix.get(i).value());
                }
                //LOGGER.info("1 - {},[{} {}],[{} {}],[{} {}]", sb.toString(), top._1c(), top._2c(), decimalFormat.format(top.supportOfD1(_1total)), decimalFormat.format(top.supportOfD2(_2total)), MINIMAL_THRESHOLD, MAXIMUM_THRESHOLD);
                LOGGER.info("1 - {},[{} {}],[{} {}]", sb.toString(), top.c1(), top.c2(), this.n1 * MINIMAL_THRESHOLD, this.n2 * MAXIMUM_THRESHOLD);
            } else if (!canPrune(top)) {
                prefix.add(top);                                                                                        //添加到前缀
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < prefix.size(); i++) {
                    sb.append(prefix.get(i).value());
                }
                //LOGGER.info("2 - {},[{} {}],[{} {}],[{} {}]", sb.toString(), top._1c(), top._2c(), decimalFormat.format(top.supportOfD1(_1total)), decimalFormat.format(top.supportOfD2(_2total)), MINIMAL_THRESHOLD, MAXIMUM_THRESHOLD);
                LOGGER.info("2 - {},[{} {}],[{} {}]", sb.toString(), top.c1(), top.c2(), this.n1 * MINIMAL_THRESHOLD, this.n2 * MAXIMUM_THRESHOLD);
            } else {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < prefix.size(); i++) {
                    sb.append(prefix.get(i).value());
                }
                sb.append(top.value());
                //LOGGER.info("3 - {},[{} {}],[{} {}],[{} {}]", sb.toString(), top._1c(), top._2c(), decimalFormat.format(top.supportOfD1(_1total)), decimalFormat.format(top.supportOfD2(_2total)), MINIMAL_THRESHOLD, MAXIMUM_THRESHOLD);
                LOGGER.info("3 - {},[{} {}],[{} {}]", sb.toString(), top.c1(), top.c2(), this.n1 * MINIMAL_THRESHOLD, this.n2 * MAXIMUM_THRESHOLD);

                List<CPTreeNode<Object>> topChildren = top.parent().children();                                         //top.parent不可能为null
                for (int i = 0; i < topChildren.size(); i++) {
                    CPTreeNode<Object> node = topChildren.get(i);
                    if (node.sibling() == top) {
                        node.setSibling(top.sibling());
                        break;
                    }
                    if (node == top) {
                        break;
                    }
                }
                top.parent().children().remove(top);
                return -1;
            }
        }
        List<CPTreeNode<Object>> topChildren = top.children();
        if (topChildren.size() == 0) {
            prefix.remove(prefix.size() - 1);
            return 0;
        }
        for (int i = 0; i < topChildren.size(); i++) {
            CPTreeNode<Object> currChild = topChildren.get(i);
            int response = mineTraverse(prefix, currChild);
            if (response == -1) {
                i--;
            }
        }
        prefix.remove(prefix.size() - 1);
        return 0;
    }

    /*—————————————————
    | 拷贝子树，深度拷贝 |
     —————————————————*/
    private void copySubTree(List<CPTreeNode<Object>> nodeList, CPTreeNode<Object> top) {
        int nodeChildrenSize = top.children().size();                                                                   //获取top的子节点的个数
        if (nodeChildrenSize == 0) {
            return;
        } else {
            List<CPTreeNode<Object>> topChildren = top.children();

            CPTreeNode<Object> parent = null;
            for (int i = 0; i < nodeList.size(); i++) {                                                                 //确保和nodeList里面的newChildK的值相等, 获取parent用于下面的设置子节点
                if (nodeList.get(i).value().equals(top.value())) {
                    parent = nodeList.get(i);
                    break;
                }
            }

            for (int i = 0; i < topChildren.size(); i++) {
                CPTreeNode<Object> child = topChildren.get(i);
                CPTreeNode<Object> newChild = CPTreeNode.getInstance();
                newChild.setValue(child.value());
                newChild.setC1(child.c1());
                newChild.setC2(child.c2());

                parent.children().add(newChild);
                newChild.setParent(parent);

                nodeList.add(newChild);
                copySubTree(nodeList, child);                                                                           //递归调用方法
            }

            for (int i = nodeList.size() - nodeChildrenSize + 1; i < nodeList.size(); i++) {                            //在这里父子关系已经从做好了，开始做兄弟关系
                CPTreeNode<Object> prev = nodeList.get(i - 1);
                CPTreeNode<Object> curr = nodeList.get(i);
                prev.setSibling(curr);
            }

            for (int i = 0; i < nodeChildrenSize; i++) {                                                                //兄弟关系设置好了，然后就可以移除后缀，回溯到上一层
                nodeList.remove(nodeList.size() - 1);
            }
        }
    }
}
