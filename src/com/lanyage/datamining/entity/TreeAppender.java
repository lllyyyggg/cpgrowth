package com.lanyage.datamining.entity;

import com.lanyage.datamining.datastructure.CPTreeNode;
import com.lanyage.datamining.enums.FilePathEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class TreeAppender {
    public static final Logger LOGGER = LoggerFactory.getLogger(TreeAppender.class);
    public static final Map<Object, Integer> NODE_AND_COUNT = new NodeCounter(FilePathEnum.ITEM_COUNT_FILE.getSource()).getNodeCountMap();
    public static Integer INDEX = 1;

    /*————————————————————
    | 将一颗树添加到另一棵树 |
     —————————————————————*/
    public void addTreeToTree(CPTreeNode<Object> subTreeHead, CPTreeNode<Object> parent) {
        if (subTreeHead == null || parent == null) {
            return;
        }
        if (parent.children().size() == 0) {                                                                            //如果root没有孩子节点
            CPTreeNode<Object> toAdd = subTreeHead;
            while (toAdd != null) {
                LOGGER.info("ADD {} TO {}", toAdd, parent);
                parent.children().add(toAdd);
                toAdd.setParent(parent);
                traverseAndAddIndexForNodes(toAdd);                                                                     //把toAdd添加index
                toAdd = toAdd.sibling();
            }
        } else {
            CPTreeNode<Object> toAdd = subTreeHead;
            while (toAdd != null) {
                int i;
                for (i = 0; i < parent.children().size(); i++) {
                    CPTreeNode<Object> currChild = parent.children().get(i);
                    if (parent.children().get(i).value().equals(toAdd.value())) {
                        CPTreeNode<Object> prev = new CPTreeNode<>(currChild.value(), currChild.c1(), currChild.c2());
                        currChild.setC1(toAdd.c1() + currChild.c1());
                        currChild.setC2(toAdd.c2() + currChild.c2());
                        LOGGER.info("COMBINE {} TO {}, now the node is {}", toAdd, prev, currChild);
                        addTreeToTree(toAdd.children().size() == 0 ? null : toAdd.children().get(0), currChild);
                        break;                                                                                          //找到了就跳出循环，开始将兄弟节点添加到root
                    }
                }
                CPTreeNode<Object> next = toAdd.sibling();                                                              //此处也就是root没有child和toAdd的value等价，那么直接将toAdd加到root
                if (i == parent.children().size()) {
                    LOGGER.info("ADD {} TO {}", toAdd, parent);
                    parent.children().get(parent.children().size() - 1).setSibling(toAdd);
                    toAdd.setSibling(null);
                    parent.children().add(toAdd);
                    toAdd.setParent(parent);

                    List<CPTreeNode<Object>> parentChildren = parent.children();
                    Collections.sort(parentChildren, (o1, o2) -> {
                        if (NODE_AND_COUNT.get(o1.value()) != NODE_AND_COUNT.get(o2.value())) {
                            return NODE_AND_COUNT.get(o2.value()).compareTo(NODE_AND_COUNT.get(o1.value()));
                        } else {
                            return ((Comparable) o1.value()).compareTo(o2.value());
                        }
                    });

                    for (int k = 1; k < parentChildren.size(); k++) {
                        CPTreeNode<Object> prev = parentChildren.get(k - 1);
                        CPTreeNode<Object> curr = parentChildren.get(k);
                        prev.setSibling(curr);
                        curr.setSibling(null);
                    }
                    traverseAndAddIndexForNodes(toAdd);                                                                 //把toAdd添加index
                }
                toAdd = next;
            }
        }
    }

    /*———————————————————————————————————————————————————————————————————
    | 当添加了一个新的孩子的时候，如果是新节点，那么必须初始化其下所有子节点的计数 |
     ————————————————————————————————————————————————————————————————————*/
    private void traverseAndAddIndexForNodes(CPTreeNode<Object> top) {
        if (top == null) {
            return;
        } else {
            //top.setIndex(++INDEX);
            INDEX++;
            LOGGER.info("SET {} INDEX {}]", top, INDEX - 1);
        }
        List<CPTreeNode<Object>> rootChildren = top.children();
        if (rootChildren.size() == 0) {
            return;
        }
        for (int i = 0; i < rootChildren.size(); i++) {
            CPTreeNode<Object> currChild = rootChildren.get(i);
            traverseAndAddIndexForNodes(currChild);
        }
    }
}
