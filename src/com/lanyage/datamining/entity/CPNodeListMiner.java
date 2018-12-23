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
    //public static final double MINIMAL_THRESHOLD = 0.6d;
    //public static final double MAXIMUM_THRESHOLD = 0.05d;
    public static final double MINIMAL_THRESHOLD = 0.7d;
    public static final double MAXIMUM_THRESHOLD = 0.3d;
    private Integer N1;
    private Integer N2;
    private static int cpcount = 0;
    private static int calccount = 0;

    public CPNodeListMiner(Integer N1, Integer N2) {
        this.N1 = N1;
        this.N2 = N2;
    }

    /*————————————————————————————————————————
        | 组合两个父子节点的NodeList形成新的NodeList |
         ————————————————————————————————————————*/
    public List<OrdersAndCounts> combine(List<OrdersAndCounts> parent, List<OrdersAndCounts> child) {
        int i = 0, j = 0;
        List<OrdersAndCounts> highLevelItemSet = new ArrayList<>();
        while (i < parent.size() && j < child.size()) {
            OrdersAndCounts poc = parent.get(i);
            OrdersAndCounts coc = child.get(j);
            if (poc.preIndex() < coc.preIndex()) {
                if (poc.postIndex() > coc.postIndex()) {
                    OrdersAndCounts combinedOne = new OrdersAndCounts(poc.preIndex(), poc.postIndex(), coc.c1(), coc.c2());
                    combinedOne.setStartNode(poc.startNode());
                    combinedOne.setEndNode(coc.endNode());
                    String fullString = combinedOne.startNode().value() + " " + getCompleteSequence(coc);
                    String combinedString = getCompleteSequence(combinedOne);
                    if (fullString.equals(combinedString)) {
                        highLevelItemSet.add(combinedOne);
                    }
                    j++;
                } else {
                    i++;
                }
            } else {
                j++;
            }
        }
        return highLevelItemSet.size() == 0 ? null : highLevelItemSet;
    }

    /*——————————————————
    | 从NodeList中挖掘模式|
     ———————————————————*/
    public void mineFromNodeList(Map<Object, List<OrdersAndCounts>> initialNodeListMap) {
        List<Item<Object>> sortedItemList = getSortedItemList();                                                        //排好序的Item
        Map<Object, Integer> sortedItemMap = new HashMap<>();                                                           //Item -> index, 用于快速查找后缀的前缀
        for (int i = 0; i < sortedItemList.size(); i++) {
            sortedItemMap.put(sortedItemList.get(i).value(), i);
        }
        Map<Object, List<OrdersAndCounts>> newMap = new HashMap<>(initialNodeListMap);                                  //生成的高项集的NodeList的Map
        newMap.remove("$");
        LOGGER.info("———————————————————————————————————————————————————————————————————————————————————————————————————the beginning of mining from nodelists");
        while (newMap != null) {                                                                                        //和数据集的维数有关
            //LOGGER.info("项集大小 : {}", newMap.size());
            mineNodeList(newMap);
            Map<Object, List<OrdersAndCounts>> tempTable = new HashMap<>();                                             //临时的Map用于存储高项集的NodeList
            for (Object key : newMap.keySet()) {
                Object prefix = ((String) key).split(" ")[0];                                                    //获取首Item,如 A11 D1获取A11
                int parentIndex = sortedItemMap.get(prefix) - 1;
                for (int i = parentIndex; i >= 0; i--) {
                    Item<Object> parentItem = sortedItemList.get(i);
                    List<OrdersAndCounts> parentNodeList = initialNodeListMap.get(parentItem.value());                  //每次都要从initialNodeListMap选取一个头进行combine
                    List<OrdersAndCounts> tobeCombinedNodeList = newMap.get(key);

                    List<OrdersAndCounts> newOrdersAndCounts = combine(parentNodeList, tobeCombinedNodeList);           //生成高项集

                    if (newOrdersAndCounts != null) {
                        tempTable.put(sortedItemList.get(i).value() + " " + key, newOrdersAndCounts);
                    }
                }
            }
            newMap = tempTable.size() == 0 ? null : tempTable;
        }
        LOGGER.info("———————————————————————————————————————————————————————————————————————————————————————————————————the end of mining from nodelists");
        LOGGER.info("TOTAL OF CPS : {}, AND CALCULATED FOR {} TIMES", cpcount, calccount);
    }

    /*——————————————————————————————————————————
    | 挖掘模式、移除不是对比模式的后缀用于后续combine|
     ——————————————————————————————————————————*/
    private void mineNodeList(Map<Object, List<OrdersAndCounts>> nodeListMap) {
        for (Map.Entry<Object, List<OrdersAndCounts>> entry : nodeListMap.entrySet()) {                                 //打印并且挖掘, 循环次数和项集的大小有关
            //Object key = entry.getKey();
            List<OrdersAndCounts> nodeList = entry.getValue();
            Map<String, OrdersAndCounts> candidatesMap = new HashMap<>();
            //String fancyString = fillCandidatesAndReturnFancyString(nodeList, candidatesMap);
            fillCandidatesAndReturnFancyString(nodeList, candidatesMap);
            //LOGGER.info("{} {}", key, fancyString);
            mine(candidatesMap);
        }
    }

    private void fillCandidatesAndReturnFancyString(List<OrdersAndCounts> nodeList, Map<String, OrdersAndCounts> candidatesMap) {
        //StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < nodeList.size(); i++) {                                                                     //遍历NodeList然后如果getCompleteSequence一样就进行融合

            OrdersAndCounts oac = nodeList.get(i);
            String s = getCompleteSequence(oac);

            if (candidatesMap.containsKey(s)) {                                                                         //这里用于将路径相同的NodeList进行合并
                OrdersAndCounts origin = candidatesMap.get(s);
                origin.setC1(oac.c1() + origin.c1());
                origin.setC2(oac.c2() + origin.c2());
            } else {
                OrdersAndCounts tempOac = new OrdersAndCounts(oac);
                candidatesMap.put(s, tempOac);
            }

            //sb.append("(").append(oac.preIndex()).append(",").append(oac.postIndex()).append(",").append(oac.c1()).append(",").append(oac.c2()).append(",").append(s).append(")");
        }
        //sb.append("]");
        //return sb.toString();
    }

    /*————————————————————————————————————————
    |    挖掘对比模式并且返回需要移除的后缀KEY    |
     ————————————————————————————————————————*/
    private void mine(Map<String, OrdersAndCounts> candidatesMap) {

        for (Map.Entry<String, OrdersAndCounts> entry : candidatesMap.entrySet()) {
            String key = entry.getKey();
            OrdersAndCounts oac = entry.getValue();
            calccount++;
            if (isContrastPattern(oac, this.N1, this.N2)) {
                cpcount++;
                LOGGER.info("1 - {}, [{} {}], [{} {}]", key, oac.c1(), oac.c2(), MINIMAL_THRESHOLD * this.N1, MAXIMUM_THRESHOLD * this.N2);
            } else {
                LOGGER.info("3 - {}, [{} {}], [{} {}]", key, oac.c1(), oac.c2(), MINIMAL_THRESHOLD * this.N1, MAXIMUM_THRESHOLD * this.N2);
            }
        }
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
    private String getCompleteSequence(OrdersAndCounts ordersAndCounts) {                                               //复杂度为O(H),H为树的高度
        CPTreeNode<Object> currNode = ordersAndCounts.endNode();
        CPTreeNode<Object> toNode = ordersAndCounts.startNode();
        StringBuilder sb = new StringBuilder();
        LinkedList<CPTreeNode<Object>> nodeValueList = new LinkedList<>(); //from A to A
        while (currNode != toNode) {
            nodeValueList.addLast(currNode);
            currNode = currNode.parent();
        }
        nodeValueList.addLast(toNode);
        while (!nodeValueList.isEmpty()) {
            sb.append(nodeValueList.pollLast().value()).append(" ");
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
}
