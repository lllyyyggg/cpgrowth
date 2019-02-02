package com.lanyage.version.one.run;

import com.lanyage.version.one.datastructure.CPTreeNode;
import com.lanyage.version.one.datastructure.SequenceSuffix;
import com.lanyage.version.one.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class MineFromNodeListForth {
    public static final Logger LOGGER = LoggerFactory.getLogger(MineFromNodeListForth.class);

    public static void main(String[] args) {
        /*—————————————————————————————————————————
        | 初始化创建根节点,并且将ITEM出现次数存到Map中 |
         ————————————————————————————-————————————*/
        CPTreeConstructor treeConstructor = new CPTreeConstructor();
        CPTreeNode<Object> root = treeConstructor.createInitialCPTree();                                                //创建初始CP树

        TreeTraverser treeTraverser = new TreeTraverser();
        treeTraverser.preTraverse(root);
        treeTraverser.postTraverse(root);                                                                               //前后序遍历添加索引
        treeTraverser.breadthFirstTraverse(root);
        /*—————————————————————————————————
        | 遍历ROOT，确保所有路径正确。如:FDGB |
         ————————————————————————————-————*/

        CPNodeListCreator cpNodeListCreator = new CPNodeListCreator();
        Map<Object, List<SequenceSuffix>> initialNodeListMap = cpNodeListCreator.createInitialNodeList(root);           //根据root创建初始NodeList

        /*———————————————
        | 开始挖掘对比模式 |
         ————————————————*/
        Integer[] Ns = new DataSetCounter().getCountOfDataSets();
        CPNodeListMiner nodeListMiner = new CPNodeListMiner(Ns[0], Ns[1]);

        long start = System.currentTimeMillis();
        nodeListMiner.mineFromNodeList(initialNodeListMap);                                                             //挖掘NodeList
        long end = System.currentTimeMillis();

        LOGGER.info("INDEX {}, N1 {}, N2 {}", TreeAppender.INDEX - 1, Ns[0], Ns[1]);
        LOGGER.info("cost : {} ms.", end - start);
        LOGGER.info("SIZE OF PRUNE PREFIX : {}", nodeListMiner.prunesetSize());
    }
}
