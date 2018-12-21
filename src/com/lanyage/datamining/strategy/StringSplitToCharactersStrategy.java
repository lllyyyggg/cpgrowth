package com.lanyage.datamining.strategy;

import com.lanyage.datamining.datastructure.CPTreeNode;
import com.lanyage.datamining.datastructure.Item;
import com.lanyage.datamining.datastructure.ItemSet;

import java.io.*;
import java.util.*;

public class StringSplitToCharactersStrategy implements IStringSplitStrategy<Object> {

    public static final IStringSplitStrategy<Object> INSTANCE = new StringSplitToCharactersStrategy();

    private StringSplitToCharactersStrategy() {
    }

    /*------------------
    | 将字符串封装称为节点|
     ------------------*/
    @Override
    public List<CPTreeNode<Object>> splitSequence(String sequence, String classTag) {
        List<CPTreeNode<Object>> nodes = new ArrayList<>();
        for (int i = 0; i < sequence.length(); i++) {
            char c = sequence.charAt(i);
            String value = String.valueOf(c).intern();
            CPTreeNode<Object> node = new CPTreeNode<>();
            node.setValue(value);                                                                                       //设置值
            if (classTag.equals("1")) {                                                                                 //设置左右计数
                node.setC1(1);
                node.setC2(0);
            }
            if (classTag.equals("2")) {
                node.setC1(0);
                node.setC2(1);
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
        while ((line = br.readLine()) != null && !line.trim().equals("")) {
            char[] cs = line.toCharArray();
            for (char c : cs) {
                if (!map.containsKey(c)) {
                    map.put(c, 1);
                } else {
                    map.put(c, map.get(c) + 1);
                }
            }
        }
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
                char[] cs = line.toCharArray();
                List<Item<Object>> itemList = new ArrayList<>();
                for (char c : cs) {
                    Item<Object> item = new Item<>(String.valueOf(c));
                    Integer count = valueAndCount.get(item.value());
                    itemList.add(item);
                    item.setCount(count);
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
        for (int i = 0; i < itemSetList.size(); i++) {
            StringBuilder sb = new StringBuilder();
            for (Item<Object> item : itemSetList.get(i).items()) {
                sb.append(item.value());
            }
            sb.append(",").append(itemSetStringMap.get(itemSetList.get(i)));
            bw.write(sb.toString());
            bw.newLine();
            bw.flush();
        }
        bw.close();
    }
}
