package com.lanyage.version.one.run;

import com.lanyage.version.one.enums.FilePathEnum;
import com.lanyage.version.one.factory.StrategyFactory;
import com.lanyage.version.one.strategy.IStringSplitStrategy;
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
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(FilePathEnum.getPath("itemcount"))));
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
        String dest = FilePathEnum.getPath("mixeddataset");
        File destFile = new File(dest);
        if (destFile.exists()) {
            destFile.delete();
        } else {
            destFile.createNewFile();
        }
        Integer[] tags = new Integer[]{1, 2};
        String[] sources = new String[]{FilePathEnum.getPath("dataset1"), FilePathEnum.getPath("dataset2")};
        sortAndAddTags(valueAndCount, dest, tags, sources);                                                         //将两个数据集排序好然后存入DATASET_ALL
        br.close();
        LOGGER.info("———————————————————————————————————————————————————————————————————————————————————————————————————the end of putting sorted itemsets into \"resources/MIXED_DATASET\"");
    }
}
