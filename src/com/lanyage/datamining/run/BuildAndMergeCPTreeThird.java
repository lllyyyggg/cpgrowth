package com.lanyage.datamining.run;

import com.lanyage.datamining.datastructure.CPTreeNode;
import com.lanyage.datamining.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BuildAndMergeCPTreeThird {
    public static final Logger LOGGER = LoggerFactory.getLogger(TreeTraverser.class);

    public static void main(String[] args) {

        /*—————————————————————————————————————————
        | 初始化创建根节点,并且将ITEM出现次数存到Map中 |
         ————————————————————————————-————————————*/
        CPTreeConstructor constructor = new CPTreeConstructor();
        CPTreeNode<Object> root = constructor.createInitialCPTree();
        /*—————————————————————————————————
        | 遍历ROOT，确保所有路径正确。如:FDGB |
         ————————————————————————————-————*/
        //TreeTraverser treeTraverser = new TreeTraverser();
        //treeTraverser.traverseAndPrintTransactions(root);

        /*—————————————
        | merge并且挖掘 |
         ——————————————*/
        Integer[] Ns = new DataSetCounter().getCountOfDataSets();
        CPGrowth cpGrowth = new CPGrowth(constructor.nodeAndCount(), Ns[0], Ns[1]);
        cpGrowth.mergeAndMine(root);

        /*—————————————————————————————————
        | 遍历ROOT，确保所有路径正确。如:FDGB |
         —————————————————————————————————*/
        //treeTraverser.traverseAndPrintTransactions(root);
        LOGGER.info("INDEX {}", TreeAppender.INDEX - 1);
    }
}
