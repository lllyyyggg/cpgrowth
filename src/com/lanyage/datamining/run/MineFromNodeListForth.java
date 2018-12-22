package com.lanyage.datamining.run;

import com.lanyage.datamining.datastructure.CPTreeNode;
import com.lanyage.datamining.datastructure.OrdersAndCounts;
import com.lanyage.datamining.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class MineFromNodeListForth {
    public static final Logger LOGGER = LoggerFactory.getLogger(MineFromNodeListForth.class);

    public static void main(String[] args) {
        CPTreeConstructor treeConstructor = new CPTreeConstructor();
        CPTreeNode<Object> root = treeConstructor.createInitialCPTree();                                                //创建初始CP树

        TreeTraverser treeTraverser = new TreeTraverser();
        treeTraverser.preTraverse(root);
        treeTraverser.postTraverse(root);                                                                               //前后序遍历添加索引

        //treeTraverser.traverseAndPrintTransactions(root);

        CPNodeListCreator cpNodeListCreator = new CPNodeListCreator();
        Map<Object, List<OrdersAndCounts>> initialNodeListMap = cpNodeListCreator.createInitialNodeList(root);          //根据root创建初始NodeList

        CPNodeListMiner nodeListMiner = new CPNodeListMiner();
        nodeListMiner.mineFromNodeList(initialNodeListMap);                                                             //挖掘NodeList

        /*—————————————————————————————————
        | 遍历ROOT，确保所有路径正确。如:FDGB |
         —————————————————————————————————*/
        treeTraverser.traverseAndPrintTransactions(root);
        LOGGER.info("INDEX {}", TreeAppender.INDEX - 1);
    }
}
