package com.lanyage.datamining.run;

import com.lanyage.datamining.enums.FilePathEnum;
import com.lanyage.datamining.factory.StrategyFactory;
import com.lanyage.datamining.strategy.IStringSplitStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

/**
 * 二 为ItemSet的Items进行排序并且打上标签
 */
public class SortItemsForItemSetSecond {

    private static final Logger LOGGER = LoggerFactory.getLogger(SortItemsForItemSetSecond.class);
    private static final IStringSplitStrategy STRATEGY = StrategyFactory.stringSplitStrategy();

    private static void sortAndAddTags(Map<Object, Integer> valueAndCount, String dest, Integer[] tags, String[] sources) throws IOException {
        STRATEGY.sortAndAddTags(valueAndCount, dest, tags, sources);
    }

    public static void main(String[] args) throws IOException {
        LOGGER.info("———————————————————————————————————————————————————————————————————————————————————————————————————the beginning of getting map entries from \"resources/ITEMSCOUNT_FILE\"");
        Map<Object, Integer> valueAndCount = new HashMap<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(FilePathEnum.ITEM_COUNT_FILE.getSource())));
        String line;
        int total = 0;
        while ((line = br.readLine()) != null && !line.trim().equals("")) {
            Object[] objects = line.split(" ");
            valueAndCount.put(objects[0], Integer.valueOf((String) objects[1]));
            total += Integer.valueOf((String) objects[1]);
            LOGGER.info("get map entry : <{}={}> ", objects[0], objects[1]);
        }
        br.close();
        LOGGER.info("———————————————————————————————————————————————————————————————————————————————————————————————————the end of getting map entries from \"resources/ITEMSCOUNT_FILE\",total = " + total);

        LOGGER.info("———————————————————————————————————————————————————————————————————————————————————————————————————the beginning of putting sorted itemsets into \"resources/MIXED_DATASET\"");
        String dest = FilePathEnum.MIX_DATASET.getSource();
        File destFile = new File(dest);
        if (destFile.exists()) {
            destFile.delete();
        } else {
            destFile.createNewFile();
        }
        Integer[] tags = new Integer[]{1, 2};
        String[] sources = new String[]{FilePathEnum.DATA_SET_I.getSource(), FilePathEnum.DATA_SET_II.getSource()};
        sortAndAddTags(valueAndCount, dest, tags, sources);                                                         //将两个数据集排序好然后存入DATASET_ALL
        br.close();
        LOGGER.info("———————————————————————————————————————————————————————————————————————————————————————————————————the end of putting sorted itemsets into \"resources/MIXED_DATASET\"");
    }
}
