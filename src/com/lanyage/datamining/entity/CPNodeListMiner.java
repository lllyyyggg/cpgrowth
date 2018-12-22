package com.lanyage.datamining.entity;

import com.lanyage.datamining.datastructure.CPTreeNode;
import com.lanyage.datamining.datastructure.Item;
import com.lanyage.datamining.datastructure.OrdersAndCounts;
import com.lanyage.datamining.enums.FilePathEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class CPNodeListMiner {
    public static final Logger LOGGER = LoggerFactory.getLogger(CPNodeListMiner.class);

    public static final double MINIMAL_THRESHOLD = 0.7d;
    public static final double MAXIMUM_THRESHOLD = 0.3d;
    public static final Integer[] NS = new DataSetCounter().getCountOfDataSets();
    public static final Set<Object> MINED_ALREADY = new HashSet<>();

    public void mineFromNodeList(Map<Object, List<OrdersAndCounts>> initialNodeListMap) {
        List<Item<Object>> sortedItemList = getSortedItemList();                                                        //前置条件initialNodeListMap、sortedItemList
        Map<Object, Integer> sortedItemMap = new HashMap<>();
        for (int i = 0; i < sortedItemList.size(); i++) {
            sortedItemMap.put(sortedItemList.get(i).value(), i);
        }

        Map<Object, List<OrdersAndCounts>> newMap = new HashMap<>(initialNodeListMap);                                  //生成的高项集的NodeList的Map
        LOGGER.info("———————————————————————————————————————————————————————————————————————————————————————————————————the beginning of mining from nodelists");
        while (newMap != null) {
            newMap = startMining(sortedItemList, sortedItemMap, initialNodeListMap, newMap);
        }
        LOGGER.info("———————————————————————————————————————————————————————————————————————————————————————————————————the end of mining from nodelists");
    }

    private Map<Object, List<OrdersAndCounts>> startMining(List<Item<Object>> sortedItemList, Map<Object, Integer> sortedItemMap, Map<Object, List<OrdersAndCounts>> initialNodeListMap, Map<Object, List<OrdersAndCounts>> newMap) {
        printAndMineNodeListMap(newMap);                                                                                       //此处可以进行挖掘和剪枝操作
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

    private void printAndMineNodeListMap(Map<Object, List<OrdersAndCounts>> nodeListMap) {
        for (Map.Entry<Object, List<OrdersAndCounts>> entry : nodeListMap.entrySet()) {                                 //打印并且挖掘
            Object key = entry.getKey();
            List<OrdersAndCounts> nodeList = entry.getValue();
            Map<String, OrdersAndCounts> candidatesMap = new HashMap<>();
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < nodeList.size(); i++) {
                OrdersAndCounts oac = nodeList.get(i);
                String s = getCompleteSequence(oac);
                if (candidatesMap.containsKey(s)) {
                    OrdersAndCounts origin = candidatesMap.get(s);
                    origin.setC1(oac.c1() + origin.c1());
                    origin.setC2(oac.c2() + origin.c2());
                } else {
                    OrdersAndCounts tempOac = new OrdersAndCounts(oac);
                    candidatesMap.put(s, tempOac);
                }
                sb.append("「").append(oac.preIndex()).append(",").append(oac.postIndex()).append(",").append(oac.c1()).append(",").append(oac.c2()).append(",").append(s).append("」");
            }
            sb.append("]");
            //LOGGER.info("{} {}", key, sb.toString());
            /*—————————————————
            | 判断是不是对比模式 |
             —————————————————*/
            mine(candidatesMap);
            /*—————————————————————
            | 判断是不是对比模式:EOF |
             —————————————————————*/
        }
    }

    private boolean checkMined(Object key) {
        boolean result = MINED_ALREADY.contains(key);
        if (!result) {
            MINED_ALREADY.add(key);
        }
        return result;
    }

    private void mine(Map<String, OrdersAndCounts> candidatesMap) {
        for (Map.Entry<String, OrdersAndCounts> entry : candidatesMap.entrySet()) {
            String key = entry.getKey();
            if (checkMined(key))
                continue;
            OrdersAndCounts oac = entry.getValue();
            if (isContrastPattern(oac, NS[0], NS[1])) {
                LOGGER.info("1 - {}, [{} {}], [{} {}]", key, oac.c1(), oac.c2(), MINIMAL_THRESHOLD * NS[0], MAXIMUM_THRESHOLD * NS[1]);
            } else {
                LOGGER.info("3 - {}, [{} {}], [{} {}]", key, oac.c1(), oac.c2(), MINIMAL_THRESHOLD * NS[0], MAXIMUM_THRESHOLD * NS[1]);
            }
        }
    }

    private boolean isContrastPattern(OrdersAndCounts oac, Integer N1, Integer N2) {
        boolean result = (oac.c1() > MINIMAL_THRESHOLD * N1 && oac.c2() <= MAXIMUM_THRESHOLD * N2) || (oac.c2() > MINIMAL_THRESHOLD * N2 && oac.c1() <= MAXIMUM_THRESHOLD * N1);
        return result;
    }

    private String getCompleteSequence(OrdersAndCounts ordersAndCounts) {
        Integer preIndex = ordersAndCounts.preIndex();
        Integer postIndex = ordersAndCounts.postIndex();
        CPTreeNode<Object> endNode = ordersAndCounts.endNode();
        StringBuilder sb = new StringBuilder();
        List<CPTreeNode<Object>> nodeValueList = new ArrayList<>();
        while (true) {
            nodeValueList.add(endNode);
            if (endNode.preIndex().equals(preIndex) && endNode.postIndex().equals(postIndex))
                break;
            endNode = endNode.parent();
        }
        for (int i = nodeValueList.size() - 1; i >= 0; i--) {
            sb.append(nodeValueList.get(i).value());
        }
        return sb.toString();
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
}
