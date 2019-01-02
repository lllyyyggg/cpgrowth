package com.lanyage.datamining.run;

import com.lanyage.datamining.datastructure.CPTreeNode;
import com.lanyage.datamining.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MineFromMergeCPTreeThird {
    public static final Logger LOGGER = LoggerFactory.getLogger(TreeTraverser.class);

    public static void main(String[] args) {

        /*—————————————————————————————————————————
        | 初始化创建根节点,并且将ITEM出现次数存到Map中 |
         ————————————————————————————-————————————*/
        CPTreeConstructor constructor = new CPTreeConstructor();
        CPTreeNode<Object> root = constructor.createInitialCPTree();
        LOGGER.info("Initial CP Tree Nodes : {}", TreeAppender.INDEX - 1);
        /*—————————————
        | merge并且挖掘 |
         ——————————————*/
        Integer[] Ns = new DataSetCounter().getCountOfDataSets();
        CPGrowth cpGrowth = new CPGrowth(constructor.nodeAndCount(), Ns[0], Ns[1]);

        long start = System.currentTimeMillis();
        cpGrowth.mergeAndMine(root);
        long end = System.currentTimeMillis();

        LOGGER.info("NUMBER OF NODES : {}, N1 {}, N2 {}", TreeAppender.INDEX - 1, Ns[0], Ns[1]);
        LOGGER.info("cost : {} ms.", end - start);
    }
}
