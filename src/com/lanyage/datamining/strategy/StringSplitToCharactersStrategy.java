package com.lanyage.datamining.strategy;

import com.lanyage.datamining.datastructure.CPTreeNode;
import com.lanyage.datamining.entity.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

public class StringSplitToCharactersStrategy implements IStringSplitStrategy<Object> {

    private static final Logger logger = LoggerFactory.getLogger(StringSplitToCharactersStrategy.class);
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
    public void sortAndAddTags(Map<Object, Integer> valueAndCount, String source, String dest, Integer classTag) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(source)));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dest, true)));
        String line;
        while ((line = br.readLine()) != null && !line.trim().equals("")) {
            //logger.info("origin itemset without tag: {} ", line);
            char[] cs = line.toCharArray();
            List<Item<Object>> itemList = new ArrayList<>();
            for (char c : cs) {
                Item<Object> item = new Item<>(String.valueOf(c));
                Integer count = valueAndCount.get(item.value());
                item.setCount(count);
                itemList.add(item);
            }
            Collections.sort(itemList);
            StringBuilder sb = new StringBuilder();
            for (Item<Object> item : itemList) {
                sb.append(item.value());
            }
            sb.append(",").append(classTag);
            //logger.info("sorted itemset    with tag: {} ", sb.toString());
            bw.write(sb.toString());
            bw.newLine();
            bw.flush();
        }
        bw.close();
        br.close();
    }
}
