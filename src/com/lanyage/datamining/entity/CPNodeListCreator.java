package com.lanyage.datamining.entity;

import com.lanyage.datamining.datastructure.CPTreeNode;
import com.lanyage.datamining.datastructure.OrdersAndCounts;

import java.io.IOException;
import java.util.*;

public class CPNodeListCreator {
    public Map<Object, List<OrdersAndCounts>> createNodeList(CPTreeNode<Object> root) {
        Map<Object, List<OrdersAndCounts>> nodeListMap = new HashMap<>();
        preTraverse(root, nodeListMap);
        return nodeListMap;
    }

    private void preTraverse(CPTreeNode<Object> root, Map<Object, List<OrdersAndCounts>> nodeListMap) {
        LinkedList<CPTreeNode<Object>> stack = new LinkedList<>();
        stack.addLast(root);
        while (!stack.isEmpty()) {
            CPTreeNode<Object> node = stack.pollLast();
            List<OrdersAndCounts> nodeList;
            if ((nodeList = nodeListMap.get(node.value())) == null) {
                nodeList = new ArrayList<>();
                nodeListMap.put(node.value(), nodeList);
            }
            nodeList.add(new OrdersAndCounts(node.preIndex(), node.postIndex(), node.c1(), node.c2()));
            int childSize;
            if ((childSize = node.children().size()) > 0) {
                for (int i = childSize - 1; i >= 0; i--) {
                    stack.addLast(node.children().get(i));
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        CPTreeConstructor treeConstructor = new CPTreeConstructor();
        CPNodeListCreator creator = new CPNodeListCreator();
        TreeTraverser treeTraverser = new TreeTraverser();
        CPTreeNode<Object> root = treeConstructor.createInitialCPTree();

        treeTraverser.preTraverse(root);
        treeTraverser.postTraverse(root);
        Map<Object, List<OrdersAndCounts>> nodeListMap = creator.createNodeList(root);

        for (Map.Entry<Object, List<OrdersAndCounts>> entry : nodeListMap.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}
