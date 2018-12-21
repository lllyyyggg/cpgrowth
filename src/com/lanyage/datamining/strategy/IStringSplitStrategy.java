package com.lanyage.datamining.strategy;


import com.lanyage.datamining.datastructure.CPTreeNode;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 该类用于按照不同的策略进行字符串序列的切分。
 *
 * @param <T> 切分之后的每一个子成分类型
 * @author lanyage
 */
public interface IStringSplitStrategy<T> {
    /*------------------
    | 将字符串封装称为节点|
     ------------------*/
    List<CPTreeNode<T>> splitSequence(String sequence, String classTag);


    /*-------------------------
    | 计算一个文件中ITEM出现的次数|
     -------------------------*/
    Map<T,Integer> calculateCountOfItems(String source) throws IOException;

    /*----------------------------------------------------------
    | 给Items进行排序，并且将排好序的ItemSet打上类标签汇总到一个文件中 |
     ---------------------------------------------------------——*/
    void sortAndAddTags(Map<Object, Integer> valueAndCount, String dest, Integer[] tags, String[] sources) throws IOException;
}
