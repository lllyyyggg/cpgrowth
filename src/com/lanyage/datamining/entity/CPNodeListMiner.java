package com.lanyage.datamining.entity;

import com.lanyage.datamining.datastructure.CPTreeNode;
import com.lanyage.datamining.datastructure.Item;
import com.lanyage.datamining.datastructure.ItemSet;
import com.lanyage.datamining.datastructure.OrdersAndCounts;
import com.lanyage.datamining.enums.FilePathEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

public class CPNodeListMiner {
    public static final Logger LOGGER = LoggerFactory.getLogger(CPNodeListMiner.class);

    public void mineFromNodeList(Map<Object, List<OrdersAndCounts>> initialNodeListMap) {
        List<Item<Object>> sortedItemList = getSortedItemList();                                                        //前置条件initialNodeListMap、sortedItemList
        Map<Object, Integer> sortedItemMap = new HashMap<>();
        for (int i = 0; i < sortedItemList.size(); i++) {
            sortedItemMap.put(sortedItemList.get(i).value(), i);
        }
        Map<Object, List<OrdersAndCounts>> newMap = new HashMap<>(initialNodeListMap);                                  //生成的高项集的NodeList的Map
        while (newMap != null) {
            newMap = startMining(sortedItemList, sortedItemMap, initialNodeListMap, newMap);
        }
    }

    private Map<Object, List<OrdersAndCounts>> startMining(List<Item<Object>> sortedItemList, Map<Object, Integer> sortedItemMap, Map<Object, List<OrdersAndCounts>> initialNodeListMap, Map<Object, List<OrdersAndCounts>> newMap) {
        printNodeListMap(newMap);                                                                                       //此处可以进行挖掘和剪枝操作
        //Map<Object, List<OrdersAndCounts>> result = new HashMap<>();
        Map<Object, List<OrdersAndCounts>> tempTable = new HashMap<>();
        for (Object key : newMap.keySet()) {
            if (key.equals("$"))                                                                                        //如果是$代表是根节点。
                continue;
            Object prefix = String.valueOf(((String) key).charAt(0)).intern();
            int parentIndex = sortedItemMap.get(prefix) - 1;
            for (int i = parentIndex; i >= 0; i--) {
                List<OrdersAndCounts> parentNodeList = initialNodeListMap.get(sortedItemList.get(i).value());
                List<OrdersAndCounts> tobeCombinedNodeList = newMap.get(key);
                List<OrdersAndCounts> newOrdersAndCounts = combine(parentNodeList, tobeCombinedNodeList);               //生成高项集
                if (newOrdersAndCounts != null) {
                    tempTable.put(sortedItemList.get(i).value() + "" + key, newOrdersAndCounts);
                }
            }
        }
        return tempTable.size() == 0 ? null : tempTable;
    }

    private void printNodeListMap(Map<Object, List<OrdersAndCounts>> nodeListMap) {
        for (Map.Entry<Object, List<OrdersAndCounts>> entry : nodeListMap.entrySet()) {
            LOGGER.info("{}", entry);
        }
    }

    /*————————————————————————————————————————
    |           获取排好序的ItemList           |
     ————————————————————————————————————————*/
    private List<Item<Object>> getSortedItemList() {
        NodeCounter nodeCounter = new NodeCounter(FilePathEnum.ITEM_COUNT_FILE.getSource());
        Map<Object, Integer> nodeCountMap = nodeCounter.getNodeCountMap();
        List<Item<Object>> itemList = new ArrayList<>();
        for (Map.Entry<Object, Integer> entry : nodeCountMap.entrySet()) {
            Item<Object> item = new Item<>(entry.getKey(), entry.getValue());
            itemList.add(item);
        }
        Collections.sort(itemList);
        return itemList;
    }

    /*————————————————————————————————————————
    | 组合两个父子节点的NodeList形成新的NodeList |
     ————————————————————————————————————————*/
    public List<OrdersAndCounts> combine(List<OrdersAndCounts> parent, List<OrdersAndCounts> child) {
        int i = 0, j = 0;
        List<OrdersAndCounts> result = new ArrayList<>();
        while (i < parent.size() && j < child.size()) {
            OrdersAndCounts poc = parent.get(i);
            OrdersAndCounts coc = child.get(j);
            if (poc.preIndex() < coc.preIndex()) {
                if (poc.postIndex() > coc.postIndex()) {
                    OrdersAndCounts combinedOne = new OrdersAndCounts(poc.preIndex(), poc.postIndex(), coc.c1(), coc.c2());
                    combinedOne.setEndNode(coc.endNode());
                    result.add(combinedOne);
                    j++;
                } else {
                    i++;
                }
            } else {
                j++;
            }
        }
        return result.size() == 0 ? null : result;
    }

    public static void main(String[] args) throws IOException {
        CPTreeConstructor treeConstructor = new CPTreeConstructor();
        CPTreeNode<Object> root = treeConstructor.createInitialCPTree();                                                //创建初始CP树

        TreeTraverser treeTraverser = new TreeTraverser();
        treeTraverser.preTraverse(root);
        treeTraverser.postTraverse(root);                                                                               //前后序遍历添加索引

        treeTraverser.traverseAndPrintTransactions(root);

        CPNodeListCreator cpNodeListCreator = new CPNodeListCreator();
        Map<Object, List<OrdersAndCounts>> initialNodeListMap = cpNodeListCreator.createInitialNodeList(root);          //根据root创建初始NodeList

        CPNodeListMiner nodeListMiner = new CPNodeListMiner();
        nodeListMiner.mineFromNodeList(initialNodeListMap);                                                             //挖掘NodeList
    }
}
