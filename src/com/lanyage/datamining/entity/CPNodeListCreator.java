package com.lanyage.datamining.entity;

import com.lanyage.datamining.datastructure.CPTreeNode;
import com.lanyage.datamining.datastructure.OrdersAndCounts;

import java.util.*;

/**
 * ————————————————————————————————————————————————————————————
 * CPNodeList创建类
 *
 * @author lanyage
 * @see OrdersAndCounts 一个Item的前后序索引和两个数据集中的计数
 * ————————————————————————————————————————————————————————————
 **/
public class CPNodeListCreator {
    /*———————————————————————————————
    |     获取每个Item的NodeList      |
     ———————————————————————————————*/
    public Map<Object, List<OrdersAndCounts>> createInitialNodeList(CPTreeNode<Object> root) {
        Map<Object, List<OrdersAndCounts>> nodeListMap = new HashMap<>();
        preTraverseToGetInitialNodeList(root, nodeListMap);
        return nodeListMap;
    }

    /*———————————————————————————————————————
    | 前序遍历root节点并得到initialNodeListMap |
     ———————————————————————————————————————*/
    private void preTraverseToGetInitialNodeList(CPTreeNode<Object> root, Map<Object, List<OrdersAndCounts>> nodeListMap) {
        LinkedList<CPTreeNode<Object>> stack = new LinkedList<>();
        stack.addLast(root);
        while (!stack.isEmpty()) {
            CPTreeNode<Object> node = stack.pollLast();
            List<OrdersAndCounts> nodeList;
            if ((nodeList = nodeListMap.get(node.value())) == null) {
                nodeList = new ArrayList<>();
                nodeListMap.put(node.value(), nodeList);
            }
            OrdersAndCounts oac = new OrdersAndCounts(node.preIndex(), node.postIndex(), node.c1(), node.c2());
            oac.setStartNode(node);
            oac.setEndNode(node);
            nodeList.add(oac);
            int childSize;
            if ((childSize = node.children().size()) > 0) {
                for (int i = childSize - 1; i >= 0; i--) {
                    stack.addLast(node.children().get(i));
                }
            }
        }
    }
}
