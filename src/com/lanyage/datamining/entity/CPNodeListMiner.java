package com.lanyage.datamining.entity;

import com.lanyage.datamining.datastructure.CPTreeNode;
import com.lanyage.datamining.datastructure.Item;
import com.lanyage.datamining.datastructure.OrdersAndCounts;
import com.lanyage.datamining.enums.FilePathEnum;
import com.lanyage.datamining.factory.StrategyFactory;
import com.lanyage.datamining.strategy.IStringSplitStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class CPNodeListMiner {
    public static final Logger LOGGER = LoggerFactory.getLogger(CPNodeListMiner.class);
    public static final IStringSplitStrategy STRATEGY = StrategyFactory.stringSplitStrategy();
    public static final double MINIMAL_THRESHOLD = 0.7d;
    public static final double MAXIMUM_THRESHOLD = 0.3d;
    public static final Integer[] NS = new DataSetCounter().getCountOfDataSets();
    public static final Set<Object> MINED_ALREADY = new HashSet<>();

    /*——————————————————
    | 从NodeList中挖掘模式|
     ———————————————————*/
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

    /*——————————————————
    |       开始挖掘     |
     ———————————————————*/
    private Map<Object, List<OrdersAndCounts>> startMining(List<Item<Object>> sortedItemList, Map<Object, Integer> sortedItemMap, Map<Object, List<OrdersAndCounts>> initialNodeListMap, Map<Object, List<OrdersAndCounts>> newMap) {

        newMap = mineNodeListMapAndRemoveUselessSuffix(newMap);                                                          //此处可以进行挖掘和剪枝操作

        Map<Object, List<OrdersAndCounts>> tempTable = new HashMap<>();                                                 //临时的Map用于存储高项集的NodeList

        for (Object key : newMap.keySet()) {
            if (key.equals("$"))                                                                                        //如果是$代表是根节点。
                continue;
            Object prefix = String.valueOf(((String) key).split(" ")[0]).intern();                               //获取首Item,如 A11 D1获取A1
            int parentIndex = sortedItemMap.get(prefix) - 1;
            for (int i = parentIndex; i >= 0; i--) {
                List<OrdersAndCounts> parentNodeList = initialNodeListMap.get(sortedItemList.get(i).value());           //每次都要从initialNodeListMap选取一个头进行combine
                List<OrdersAndCounts> tobeCombinedNodeList = newMap.get(key);
                List<OrdersAndCounts> newOrdersAndCounts = combine(parentNodeList, tobeCombinedNodeList);               //生成高项集
                if (newOrdersAndCounts != null) {
                    tempTable.put(sortedItemList.get(i).value() + " " + key, newOrdersAndCounts);
                }
            }
        }
        return tempTable.size() == 0 ? null : tempTable;
    }

    /*——————————————————————————————————————————
    | 挖掘模式、移除不是对比模式的后缀用于后续combine|
     ——————————————————————————————————————————*/
    private Map<Object, List<OrdersAndCounts>> mineNodeListMapAndRemoveUselessSuffix(Map<Object, List<OrdersAndCounts>> nodeListMap) {                              //arg = newMap

        Set<Object> tobeRemoved = new HashSet<>();                                                                      //用于存储剪枝的key
        for (Map.Entry<Object, List<OrdersAndCounts>> entry : nodeListMap.entrySet()) {                                 //打印并且挖掘
            Object key = entry.getKey();
            List<OrdersAndCounts> nodeList = entry.getValue();
            Map<String, OrdersAndCounts> candidatesMap = new HashMap<>();
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < nodeList.size(); i++) {                                                                 //遍历NodeList然后如果getCompleteSequence一样就进行融合

                OrdersAndCounts oac = nodeList.get(i);
                String s = getCompleteSequence(oac);

                if (candidatesMap.containsKey(s)) {                                                                     //这里用于将路径相同的NodeList进行合并
                    OrdersAndCounts origin = candidatesMap.get(s);
                    origin.setC1(oac.c1() + origin.c1());
                    origin.setC2(oac.c2() + origin.c2());
                } else {
                    OrdersAndCounts tempOac = new OrdersAndCounts(oac);
                    candidatesMap.put(s, tempOac);
                }

                sb.append("(").append(oac.preIndex()).append(",").append(oac.postIndex()).append(",").append(oac.c1()).append(",").append(oac.c2()).append(",").append(s).append(")");
            }
            sb.append("]");
            LOGGER.info("{} {}", key, sb.toString());
            /*—————————————————
            | 判断是不是对比模式 |
             —————————————————*/
            tobeRemoved.addAll(mineAndReturnTobeRemoved(candidatesMap));
            /*—————————————————————
            | 判断是不是对比模式:EOF |
             —————————————————————*/
        }


        Map<Object, List<OrdersAndCounts>> replacementMap = new HashMap<>();

        for (Map.Entry<Object, List<OrdersAndCounts>> entry : nodeListMap.entrySet()) {
            Object key = entry.getKey();
            List<OrdersAndCounts> nodeList = entry.getValue();
            List<OrdersAndCounts> replacementList = new ArrayList<>();
            for (int i = 0; i < nodeList.size(); i++) {
                OrdersAndCounts oac = nodeList.get(i);
                if (tobeRemoved.contains(getCompleteSequence(oac))) {
                    //nodeList.remove(i--);
                    continue;
                } else {
                    replacementList.add(oac);
                }
            }
            if (replacementList.size() > 0) {
                replacementMap.put(key, replacementList);
            }
        }
        nodeListMap.clear();
        return replacementMap;
    }

    /*————————————————————————————————————————
    |    挖掘对比模式并且返回需要移除的后缀KEY    |
     ————————————————————————————————————————*/
    private Set<Object> mineAndReturnTobeRemoved(Map<String, OrdersAndCounts> candidatesMap) {

        Set<Object> tempSet = new HashSet<>();
        for (Map.Entry<String, OrdersAndCounts> entry : candidatesMap.entrySet()) {
            String key = entry.getKey();
            if (checkMined(key)) {
                LOGGER.info("{} already mined!!!@ so do not need to be mined again", key);
                continue;
            }
            OrdersAndCounts oac = entry.getValue();
            if (isContrastPattern(oac, NS[0], NS[1])) {
                LOGGER.info("1 - {}, [{} {}], [{} {}]", key, oac.c1(), oac.c2(), MINIMAL_THRESHOLD * NS[0], MAXIMUM_THRESHOLD * NS[1]);
            } else {
                tempSet.add(key);                                                                                   //如果不符合模式，那么以它为后缀的所有sequence都不可能是对比模式，因此不需要参与后续的combine了，因此将其再次移除
                LOGGER.info("3 - {}, [{} {}], [{} {}]", key, oac.c1(), oac.c2(), MINIMAL_THRESHOLD * NS[0], MAXIMUM_THRESHOLD * NS[1]);
            }
        }
        return tempSet;
    }

    /*————————————————————————————————————————
    |          该路径是否已经挖掘过了           |
     ————————————————————————————————————————*/
    private boolean checkMined(Object key) {
        boolean result = MINED_ALREADY.contains(key);
        if (!result) {
            MINED_ALREADY.add(key);
        }
        return result;
    }

    /*————————————————————————————————————————
    |               是不是对比模式             |
     ————————————————————————————————————————*/
    private boolean isContrastPattern(OrdersAndCounts oac, Integer N1, Integer N2) {
        boolean result = (oac.c1() > MINIMAL_THRESHOLD * N1 && oac.c2() <= MAXIMUM_THRESHOLD * N2) || (oac.c2() > MINIMAL_THRESHOLD * N2 && oac.c1() <= MAXIMUM_THRESHOLD * N1);
        return result;
    }

    /*————————————————————————————————————————
    |       根据EndNode来获取完整路径           |
     ————————————————————————————————————————*/
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
            sb.append(nodeValueList.get(i).value()).append(" ");
        }
        return sb.toString().trim();
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
