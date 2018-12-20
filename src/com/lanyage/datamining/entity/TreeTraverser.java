package com.lanyage.datamining.entity;

import com.lanyage.datamining.datastructure.CPTreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class TreeTraverser {
    public static final Logger LOGGER = LoggerFactory.getLogger(TreeTraverser.class);

    /*————————————————————————————
    |遍历根节点，打印所有transaction|
     ————————————————————————————*/
    public void traverse(CPTreeNode<Object> head) {
        LOGGER.info("———————————————————————————————————————————————————————————————————————————————————————————————————the beginning of traversing the cp tree");
        for (int i = 0; i < head.children().size(); i++) {
            traverse(new ArrayList<>(0), head.children().get(i));
        }
        LOGGER.info("———————————————————————————————————————————————————————————————————————————————————————————————————the end of traversing the cp tree");
    }

    private void traverse(List<CPTreeNode<Object>> nodeList, CPTreeNode<Object> top) {
        if (top == null) {
            return;
        } else {
            nodeList.add(top);
        }
        List<CPTreeNode<Object>> topChildren = top.children();
        if (topChildren.size() == 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < nodeList.size(); i++) {
                sb.append(nodeList.get(i)).append(" ");
            }
            sb.deleteCharAt(sb.length() - 1);
            LOGGER.info("TRANSACTION — {}", sb.toString());
            nodeList.remove(nodeList.size() - 1);
            return;
        }
        for (int i = 0; i < topChildren.size(); i++) {
            CPTreeNode<Object> currChild = topChildren.get(i);
            traverse(nodeList, currChild);
        }
        nodeList.remove(nodeList.size() - 1);
    }
}
