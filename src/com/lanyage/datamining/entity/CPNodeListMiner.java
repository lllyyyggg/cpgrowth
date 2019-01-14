package com.lanyage.datamining.entity;

import com.lanyage.datamining.datastructure.Item;
import com.lanyage.datamining.datastructure.SequenceSuffix;
import com.lanyage.datamining.enums.FilePathEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class CPNodeListMiner {
    public static final Logger LOGGER = LoggerFactory.getLogger(CPNodeListMiner.class);
    public static final double MINIMAL_THRESHOLD = 0.6d;
    public static final double MAXIMUM_THRESHOLD = 0.05d;
    private final Set<String> PRUNE_SET = new HashSet<>();
    private Integer N1;
    private Integer N2;
    private static int cpcount = 0;
    private static int calccount = 0;

    public CPNodeListMiner(Integer N1, Integer N2) {
        this.N1 = N1;
        this.N2 = N2;
    }

    /*———————————————————
    | 从NodeList中挖掘模式|
     ———————————————————*/
    public void mineFromNodeList(Map<Object, List<SequenceSuffix>> initialNodeListMap) {
        List<Item<Object>> sortedItemList = getSortedItemList();                                                        //排好序的Item
        Map<Object, Integer> sortedItemMap = new HashMap<>();                                                           //Item -> index, 用于快速查找后缀的前缀
        for (int i = 0; i < sortedItemList.size(); i++) {
            sortedItemMap.put(sortedItemList.get(i).value(), i);
        }
        Map<Object, List<SequenceSuffix>> newMap = new HashMap<>(initialNodeListMap);                                   //生成的高项集的NodeList的Map
        newMap.remove("$");
        LOGGER.info("———————————————————————————————————————————————————————————————————————————————————————————————————the beginning of mining from nodelists");
        while (newMap != null) {                                                                                        //和数据集的维数有关
            //LOGGER.info("项集大小 : {}", newMap.size());
            mineNodeList(newMap);
            Map<Object, List<SequenceSuffix>> tempTable = new HashMap<>();                                              //临时的Map用于存储高项集的NodeList
            for (Object key : newMap.keySet()) {
                Object prefix = ((String) key).split(" ")[0];                                                    //获取首Item,如 A11 D1获取A11
                int parentIndex = sortedItemMap.get(prefix) - 1;
                for (int i = parentIndex; i >= 0; i--) {
                    Item<Object> parentItem = sortedItemList.get(i);
                    List<SequenceSuffix> parentNodeList = initialNodeListMap.get(parentItem.value());                   //每次都要从initialNodeListMap选取一个头进行combine
                    List<SequenceSuffix> tobeCombinedNodeList = newMap.get(key);
                    List<SequenceSuffix> newOrdersAndCounts = combine(parentNodeList, tobeCombinedNodeList);            // 生成高项集
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

    public Integer prunesetSize() {
        return PRUNE_SET.size();
    }

    /*————————————————————————————————————————
        | 组合两个父子节点的NodeList形成新的NodeList |
         ————————————————————————————————————————*/
    public List<SequenceSuffix> combine(List<SequenceSuffix> parent, List<SequenceSuffix> child) {
        int i = 0, j = 0;
        List<SequenceSuffix> highLevelItemSet = new ArrayList<>();
        while (i < parent.size() && j < child.size()) {
            SequenceSuffix poc = parent.get(i);
            SequenceSuffix coc = child.get(j);
            if (poc.preIndex() < coc.preIndex()) {
                if (poc.postIndex() > coc.postIndex()) {
                    boolean canCombine = coc.node().parent() == poc.node();
                    if (canCombine) {
                        SequenceSuffix combinedOne = new SequenceSuffix(poc.preIndex(), poc.postIndex(), coc.c1(), coc.c2());
                        combinedOne.setNode(poc.node());
                        combinedOne.setSequence(poc.sequence() + " " + coc.sequence());
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


    /*——————————————————————————————————————————
    | 挖掘模式、移除不是对比模式的后缀用于后续combine|
     ——————————————————————————————————————————*/
    private void mineNodeList(Map<Object, List<SequenceSuffix>> nodeListMap) {
        for (Map.Entry<Object, List<SequenceSuffix>> entry : nodeListMap.entrySet()) {                                 //打印并且挖掘, 循环次数和项集的大小有关
            Object key = entry.getKey();
            List<SequenceSuffix> nodeList = entry.getValue();
            SequenceSuffix tobeMined = getToBeMined(key, nodeList);
            mine(key, tobeMined);
        }
    }

    /*————————————————————————————————————————————————————————
    |    打印一个项集的toString, 并且填充待挖掘的candidatesMap    |
     —————————————————————————————————————————————————————————*/
    private SequenceSuffix getToBeMined(Object key, List<SequenceSuffix> nodeList) {
        SequenceSuffix candidate = new SequenceSuffix();
        candidate.setSequence(key);
        candidate.setPreIndex(-1);
        candidate.setPostIndex(-1);
        for (int i = 0; i < nodeList.size(); i++) {                                                                     //遍历NodeList然后如果getCompleteSequence一样就进行融合
            SequenceSuffix tempOac = nodeList.get(i);
            candidate.setC1(tempOac.c1() + candidate.c1());
            candidate.setC2(tempOac.c2() + candidate.c2());
        }
        return candidate;
    }

    /*————————————————————————————————————————
    |    挖掘对比模式并且返回需要移除的后缀KEY    |
     ————————————————————————————————————————*/
    private void mine(Object key, SequenceSuffix candidate) {

        String sequence = ((String) key).intern();
        int lastIndexOfSpace = sequence.lastIndexOf(" ");
        lastIndexOfSpace = lastIndexOfSpace > 0 ? lastIndexOfSpace : 0;
        String prefix = sequence.substring(0, lastIndexOfSpace).trim();
        boolean exists = false;
        while (prefix.length() > 0) {
            if (PRUNE_SET.contains(prefix)) {
                exists = true;
                break;
            }
            int lstIndexOfSpace = prefix.lastIndexOf(" ");
            lstIndexOfSpace = lstIndexOfSpace > 0 ? lstIndexOfSpace : 0;
            prefix = prefix.substring(0, lstIndexOfSpace).trim();
        }
        if (exists) {
            //LOGGER.info("{} PASSED BECAUSE OF {}", sequence, prefix);
            return;
        }
        calccount++;
        if (isContrastPattern(candidate, this.N1, this.N2)) {
            cpcount++;
            LOGGER.info("1 - {}, [{} {}], [{} {}]", key, candidate.c1(), candidate.c2(), MINIMAL_THRESHOLD * this.N1, MAXIMUM_THRESHOLD * this.N2);
        } else if (!canPrune(candidate, this.N1, this.N2)) {
            LOGGER.info("2 - {}, [{} {}], [{} {}]", key, candidate.c1(), candidate.c2(), MINIMAL_THRESHOLD * this.N1, MAXIMUM_THRESHOLD * this.N2);
        } else {
            PRUNE_SET.add(sequence);
            LOGGER.info("3 - {}, [{} {}], [{} {}]", key, candidate.c1(), candidate.c2(), MINIMAL_THRESHOLD * this.N1, MAXIMUM_THRESHOLD * this.N2);
        }
    }

    private boolean canPrune(SequenceSuffix oac, Integer N1, Integer N2) {
        boolean result = oac.c1() > MINIMAL_THRESHOLD * N1 || oac.c2() > MINIMAL_THRESHOLD * N2;
        return !result;
    }

    /*————————————————————————————————————————
    |               是不是对比模式             |
     ————————————————————————————————————————*/
    private boolean isContrastPattern(SequenceSuffix oac, Integer N1, Integer N2) {
        boolean result = (oac.c1() > MINIMAL_THRESHOLD * N1 && oac.c2() <= MAXIMUM_THRESHOLD * N2) || (oac.c2() > MINIMAL_THRESHOLD * N2 && oac.c1() <= MAXIMUM_THRESHOLD * N1);
        return result;
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
