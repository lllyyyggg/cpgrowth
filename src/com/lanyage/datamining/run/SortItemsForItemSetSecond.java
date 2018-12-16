package com.lanyage.datamining.run;

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

    private static final Logger logger = LoggerFactory.getLogger(SortItemsForItemSetSecond.class);
    private static final IStringSplitStrategy STRATEGY = StrategyFactory.stringSplitStrategy();

    private static final String DATASET_I = "resources/DATASET_I";
    private static final String DATASET_II = "resources/DATASET_II";
    private static final String ITEMSCOUNT_FILE = "resources/ITEMSCOUNT_FILE";

    private static void sortAndAddTags(Map<Object, Integer> valueAndCount, String source, String dest, Integer tag) throws IOException {
        STRATEGY.sortAndAddTags(valueAndCount, source, dest, tag);
    }

    public static void main(String[] args) throws IOException {
        //logger.info("———————————————————————————————————————————————————————————————————————————————————————————————————the beginning of getting map entries from \"resources/ITEMSCOUNT_FILE\"");
        Map<Object, Integer> valueAndCount = new HashMap<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(ITEMSCOUNT_FILE)));
        String line;
        while ((line = br.readLine()) != null && !line.trim().equals("")) {
            Object[] objects = line.split(" ");
            valueAndCount.put(objects[0], Integer.valueOf((String) objects[1]));
            //logger.info("get map entry : <{}={}> ", objects[0], objects[1]);
        }
        br.close();
        //logger.info("———————————————————————————————————————————————————————————————————————————————————————————————————the end of getting map entries from \"resources/ITEMSCOUNT_FILE\"");

        //logger.info("———————————————————————————————————————————————————————————————————————————————————————————————————the beginning of putting sorted itemsets into \"resources/MIXED_DATASET\"");
        String dest = "resources/MIXED_DATASET";
        File destFile = new File(dest);
        if (destFile.exists()) {
            destFile.delete();
        } else {
            destFile.createNewFile();
        }

        sortAndAddTags(valueAndCount, DATASET_I, dest, 1);                                                         //将两个数据集排序好然后存入DATASET_ALL
        sortAndAddTags(valueAndCount, DATASET_II, dest, 2);
        br.close();
        //logger.info("———————————————————————————————————————————————————————————————————————————————————————————————————the end of putting sorted itemsets into \"resources/MIXED_DATASET\"");
    }

    /*————————————————————————————————————————————————————————————————————————
     |   2018-12-14 10:19:20  [ main:   0 ] - [ INFO ]  get map entry : <A=2> |
     |   2018-12-14 10:19:20  [ main:   1 ] - [ INFO ]  get map entry : <B=3> |
     |   2018-12-14 10:19:20  [ main:   1 ] - [ INFO ]  get map entry : <C=4> |
     |   2018-12-14 10:19:20  [ main:   2 ] - [ INFO ]  get map entry : <D=5> |
     |   2018-12-14 10:19:20  [ main:   2 ] - [ INFO ]  get map entry : <E=3> |
     |   2018-12-14 10:19:20  [ main:   2 ] - [ INFO ]  get map entry : <F=6> |
     |   2018-12-14 10:19:20  [ main:   2 ] - [ INFO ]  get map entry : <G=4> |
     |   2018-12-14 10:19:20  [ main:   4 ] - [ INFO ]  get line : BDFG       |
     |   2018-12-14 10:19:20  [ main:   5 ] - [ INFO ]  sorted line : FDGB,1  |
     |   2018-12-14 10:19:20  [ main:   6 ] - [ INFO ]  get line : DFG        |
     |   2018-12-14 10:19:20  [ main:   6 ] - [ INFO ]  sorted line : FDG,1   |
     |   2018-12-14 10:19:20  [ main:   7 ] - [ INFO ]  get line : ACD        |
     |   2018-12-14 10:19:20  [ main:   7 ] - [ INFO ]  sorted line : DCA,1   |
     |   2018-12-14 10:19:20  [ main:   7 ] - [ INFO ]  get line : BDGE       |
     |   2018-12-14 10:19:20  [ main:   7 ] - [ INFO ]  sorted line : DGBE,1  |
     |   2018-12-14 10:19:20  [ main:   8 ] - [ INFO ]  get line : CF         |
     |   2018-12-14 10:19:20  [ main:   8 ] - [ INFO ]  sorted line : FC,2    |
     |   2018-12-14 10:19:20  [ main:  14 ] - [ INFO ]  get line : DEFG       |
     |   2018-12-14 10:19:20  [ main:  15 ] - [ INFO ]  sorted line : FDGE,2  |
     |   2018-12-14 10:19:20  [ main:  15 ] - [ INFO ]  get line : ACF        |
     |   2018-12-14 10:19:20  [ main:  15 ] - [ INFO ]  sorted line : FCA,2   |
     |   2018-12-14 10:19:20  [ main:  15 ] - [ INFO ]  get line : BCEF       |
     |   2018-12-14 10:19:20  [ main:  15 ] - [ INFO ]  sorted line : FCBE,2  |
      ————————————————————————————————————————————————————————————————————————*/
}
