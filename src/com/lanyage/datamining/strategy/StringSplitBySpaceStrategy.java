package com.lanyage.datamining.strategy;

import com.lanyage.datamining.datastructure.CPTreeNode;
import com.lanyage.datamining.entity.Item;
import com.lanyage.datamining.entity.ItemSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

public class StringSplitBySpaceStrategy implements IStringSplitStrategy<Object> {

    public static final IStringSplitStrategy<Object> INSTANCE = new StringSplitBySpaceStrategy();
    private static final Logger logger = LoggerFactory.getLogger(StringSplitBySpaceStrategy.class);

    private StringSplitBySpaceStrategy() {
    }

    /*------------------
    | 将字符串封装称为节点|
     ------------------*/
    @Override
    public List<CPTreeNode<Object>> splitSequence(String sequence, String classTag) {

        List<CPTreeNode<Object>> nodes = new ArrayList<>();

        String[] items = sequence.split(" ");
        for (int i = 0; i < items.length; i++) {
            String value = items[i];
            CPTreeNode<Object> node = new CPTreeNode<>();
            node.setValue(value);
            if (classTag.equals("1")) {                                                                                 //设置左右计数
                node.set_1c(1);
                node.set_2c(0);
            }
            if (classTag.equals("2")) {
                node.set_1c(0);
                node.set_2c(1);
            }
            nodes.add(node);
        }
        return nodes;
    }

    /*-------------------------
    | 计算一个文件中ITEM出现的次数|
     -------------------------*/
    @Override
    public Map<Object, Integer> calculateCountOfItems(String source) throws IOException {
        Map<Object, Integer> map = new HashMap<>();
        BufferedReader br =
                new BufferedReader(new InputStreamReader(new FileInputStream(source)));
        String line;
        //int index = 0;
        while ((line = br.readLine()) != null && !line.trim().equals("")) {
            //if (line.contains("V3")) {
            //    index++;
            //}
            String[] items = line.trim().split(" ");
            for (String item : items) {
                if (!map.containsKey(item)) {
                    map.put(item, 1);
                } else {
                    map.put(item, map.get(item) + 1);
                }
            }
        }
        //System.out.println("V3 " + index);
        br.close();
        return map;
    }

    /*----------------------------------------------------------
    | 给Items进行排序，并且将排好序的ItemSet打上类标签汇总到一个文件中 |
     ---------------------------------------------------------——*/
    @Override
    public void sortAndAddTags(Map<Object, Integer> valueAndCount, String dest, Integer[] tags, String[] sources) throws IOException {
        Map<ItemSet<Object>, Integer> itemSetStringMap = new HashMap<>();
        for (int i = 0; i < sources.length; i++) {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(sources[i])));
            String line;
            while ((line = br.readLine()) != null && !line.trim().equals("")) {
                String[] items = line.split(" ");
                List<Item<Object>> itemList = new ArrayList<>();
                for (String itemString : items) {
                    Item<Object> item = new Item<>(itemString);
                    Integer count = valueAndCount.get(item.value());
                    item.setCount(count);
                    itemList.add(item);
                }
                Collections.sort(itemList);
                ItemSet<Object> itemSet = new ItemSet<>();
                itemSet.setItems(itemList);
                itemSetStringMap.put(itemSet, tags[i]);
            }
            br.close();
        }
        List<ItemSet<Object>> itemSetList = new ArrayList<>();
        itemSetList.addAll(itemSetStringMap.keySet());
        Collections.sort(itemSetList);

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dest, true)));
        for(int i = 0; i < itemSetList.size(); i++) {
            StringBuilder sb = new StringBuilder();
            for (Item<Object> item : itemSetList.get(i).items()) {
                sb.append(item.value()).append(" ");
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append(",").append(itemSetStringMap.get(itemSetList.get(i)));
            bw.write(sb.toString());
            bw.newLine();
            bw.flush();
        }
        bw.close();
    }
}

