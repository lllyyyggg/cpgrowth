package com.lanyage.datamining.entity;

import com.lanyage.datamining.datastructure.CPTreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TreeTraverser {
    public static final Logger LOGGER = LoggerFactory.getLogger(TreeTraverser.class);

    /*————————————————————————————
    |遍历根节点，打印所有transaction|
     ————————————————————————————*/
    public void traverseAndPrintTransactions(CPTreeNode<Object> head) {
        //LOGGER.info("———————————————————————————————————————————————————————————————————————————————————————————————————the beginning of traversing the cp tree");
        for (int i = 0; i < head.children().size(); i++) {
            traverseHelp(new ArrayList<>(0), head.children().get(i));
        }
        //LOGGER.info("———————————————————————————————————————————————————————————————————————————————————————————————————the end of traversing the cp tree");
    }

    /*————————————————————————————
    |遍历根节点，打印所有transaction|
     ————————————————————————————*/
    private void traverseHelp(List<CPTreeNode<Object>> nodeList, CPTreeNode<Object> top) {
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
            traverseHelp(nodeList, currChild);
        }
        nodeList.remove(nodeList.size() - 1);
    }

    /*————————————————————
    |前序遍历并且添加前序索引|
     ————————————————————*/
    public void preTraverse(CPTreeNode<Object> root) {
        //LOGGER.info("———————————————————————————————————————————————————————————————————————————————————————————————————the beginning of pre-traversing");
        int preIndex = 0;
        LinkedList<CPTreeNode<Object>> stack = new LinkedList<>();
        stack.addLast(root);

        while (!stack.isEmpty()) {
            CPTreeNode<Object> node = stack.pollLast();
            node.preIndex(preIndex++);
            //LOGGER.info("{}", node);
            int childSize;
            if ((childSize = node.children().size()) > 0) {
                for (int i = childSize - 1; i >= 0; i--) {
                    stack.addLast(node.children().get(i));
                }
            }
        }
        //LOGGER.info("———————————————————————————————————————————————————————————————————————————————————————————————————the end of pre-traversing");
    }


    /*————————————————————
    |后序遍历并且添加后序索引|
     ————————————————————*/
    public void postTraverse(CPTreeNode<Object> root) {
        //LOGGER.info("———————————————————————————————————————————————————————————————————————————————————————————————————the beginning of post-traversing");
        int postIndex = 0;
        LinkedList<CPTreeNode<Object>> stack = new LinkedList<>();
        stack.addLast(root);
        while (!stack.isEmpty()) {
            CPTreeNode<Object> curr = stack.peekLast();
            while (curr.children().size() > 0 && !curr.children().get(0).isVisited()) {
                curr = curr.children().get(0);
                stack.addLast(curr);
            }
            CPTreeNode<Object> node = stack.pollLast();
            node.visited(true);
            node.postIndex(postIndex++);
            //LOGGER.info("{}", node);
            if (node.sibling() != null) {
                stack.addLast(node.sibling());
            }
        }
        //LOGGER.info("———————————————————————————————————————————————————————————————————————————————————————————————————the end of post-traversing");
    }

    public void breadthFirstTraverse(CPTreeNode<Object> root) {
        LinkedList<CPTreeNode<Object>> queue = new LinkedList<>();
        for (CPTreeNode<Object> rootChild : root.children()) {
            queue.addFirst(rootChild);
        }
        while (!queue.isEmpty()) {
            CPTreeNode<Object> node = queue.pollLast();
            for (CPTreeNode<Object> child : node.children()) {
                queue.addFirst(child);
            }
        }
    }
}

