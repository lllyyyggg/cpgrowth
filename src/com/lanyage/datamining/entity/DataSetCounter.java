package com.lanyage.datamining.entity;

import com.lanyage.datamining.enums.FilePathEnum;

import java.io.*;

/**
 *  获取每个数据集中的TRANSACTION的个数并且返回
 */
public class DataSetCounter {
    /*———————————————————————————————
    | 获取每个数据集的TRANSACTION的个数 |
     ———————————————————————————————*/
    public Integer[] getCountOfDataSets() throws IOException {
        Integer[] result = new Integer[2];
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(FilePathEnum.DATA_SET_I.getSource())));
        Integer countI = 0;
        while ( br.readLine() != null) {
            countI++;
        }
        br.close();
        result[0] = countI;
        Integer countII = 0;
        br = new BufferedReader(new InputStreamReader(new FileInputStream(FilePathEnum.DATA_SET_II.getSource())));
        while ( br.readLine() != null) {
            countII++;
        }
        br.close();
        result[1] = countII;
        return result;
    }
}
