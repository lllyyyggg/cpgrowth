package com.lanyage.datamining.run;


import com.lanyage.datamining.factory.StrategyFactory;
import com.lanyage.datamining.strategy.IStringSplitStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 一 首先计算所有的Item在两个数据集D1和D2中的Count。
 */
public class CalculateItemCountFirst {
    private static final Logger logger = LoggerFactory.getLogger(CalculateItemCountFirst.class);
    private static final String DATASET_I = "resources/DATASET_I";
    private static final String DATASET_II = "resources/DATASET_II";
    private static final String ITEMSCOUNT_FILE = "resources/ITEMSCOUNT_FILE";

    private static final IStringSplitStrategy STRATEGY = StrategyFactory.stringSplitStrategy();

    public static Map<Object, Integer> calculateCountOfItems(String source) throws IOException {                        //计算Item的Count
        return STRATEGY.calculateCountOfItems(source);
    }

    public static void main(String[] args) throws IOException {

        logger.info("———————————————————————————————————————————————————————————————————————————————————————————————————the beginning of calculating item counts");

        Map<Object, Integer> itemCountMap = new HashMap<>();
        Map<Object, Integer> map1 = calculateCountOfItems(DATASET_I);
        logger.info("successfully count items from \"{}\"", DATASET_I);
        Map<Object, Integer> map2 = calculateCountOfItems("resources/DATASET_II");
        logger.info("successfully count items from \"{}\"", DATASET_II);

        logger.info("———————————————————————————————————————————————————————————————————————————————————————————————————the end of calculating item counts");

        logger.info("———————————————————————————————————————————————————————————————————————————————————————————————————the beginning of mixing item counts");
        itemCountMap.putAll(map1);
        for (Map.Entry<Object, Integer> entry : map2.entrySet()) {
            Object key = entry.getKey();
            Integer count = entry.getValue();
            if (itemCountMap.containsKey(key)) {
                itemCountMap.put(key, itemCountMap.get(key) + count);
            } else {
                itemCountMap.put(key, count);
            }
        }

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(ITEMSCOUNT_FILE)));
        for (Map.Entry<Object, Integer> entry : itemCountMap.entrySet()) {
            Object key = entry.getKey();
            Integer count = entry.getValue();
            String content = key + " " + count;
            bw.write(content);
            bw.newLine();
            logger.info("write <{}={}> to the file \"{}\"", key, count, ITEMSCOUNT_FILE);
        }
        bw.flush();
        bw.close();
        logger.info("———————————————————————————————————————————————————————————————————————————————————————————————————the end of mixing item counts");
    }

     /*——————————————————————————————————————result log————————————————————————————————————————
     |2018-12-13 11:06:41  [ main:0 ] - [ INFO ]  write <A=2> to the file resources/ITEMSCOUNT_FILE|
     |2018-12-13 11:06:41  [ main:0 ] - [ INFO ]  write <B=3> to the file resources/ITEMSCOUNT_FILE|
     |2018-12-13 11:06:41  [ main:1 ] - [ INFO ]  write <C=4> to the file resources/ITEMSCOUNT_FILE|
     |2018-12-13 11:06:41  [ main:1 ] - [ INFO ]  write <D=5> to the file resources/ITEMSCOUNT_FILE|
     |2018-12-13 11:06:41  [ main:1 ] - [ INFO ]  write <E=3> to the file resources/ITEMSCOUNT_FILE|
     |2018-12-13 11:06:41  [ main:1 ] - [ INFO ]  write <F=6> to the file resources/ITEMSCOUNT_FILE|
     |2018-12-13 11:06:41  [ main:1 ] - [ INFO ]  write <G=4> to the file resources/ITEMSCOUNT_FILE|
      ————————————————————————————————————————————————————————————————————————————————————————*/
}
