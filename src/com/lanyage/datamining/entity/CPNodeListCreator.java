package com.lanyage.datamining.entity;

import com.lanyage.datamining.datastructure.CPTreeNode;
import com.lanyage.datamining.datastructure.SequenceSuffix;

import java.util.*;

/**
 * ————————————————————————————————————————————————————————————
 * CPNodeList创建类
 *
 * @author lanyage
 * @see SequenceSuffix 一个Item的前后序索引和两个数据集中的计数
 * ————————————————————————————————————————————————————————————
 **/
public class CPNodeListCreator {
    /*———————————————————————————————
    |     获取每个Item的NodeList      |
     ———————————————————————————————*/
    public Map<Object, List<SequenceSuffix>> createInitialNodeList(CPTreeNode<Object> root) {
        Map<Object, List<SequenceSuffix>> nodeListMap = new HashMap<>();
        preTraverseToGetInitialNodeList(root, nodeListMap);
        return nodeListMap;
    }

    /*———————————————————————————————————————
    | 前序遍历root节点并得到initialNodeListMap |
     ———————————————————————————————————————*/
    private void preTraverseToGetInitialNodeList(CPTreeNode<Object> root, Map<Object, List<SequenceSuffix>> nodeListMap) {
        LinkedList<CPTreeNode<Object>> stack = new LinkedList<>();
        stack.addLast(root);
        while (!stack.isEmpty()) {
            CPTreeNode<Object> node = stack.pollLast();
            List<SequenceSuffix> nodeList;
            if ((nodeList = nodeListMap.get(node.value())) == null) {
                nodeList = new ArrayList<>();
                nodeListMap.put(node.value(), nodeList);
            }
            SequenceSuffix oac = new SequenceSuffix(node.preIndex(), node.postIndex(), node.c1(), node.c2());
            oac.setSequence(node.value());
            oac.setNode(node);
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
