package com.lanyage.datamining.entity;

import com.lanyage.datamining.enums.FilePathEnum;

import java.io.*;

/**
 * 获取每个数据集中的TRANSACTION的个数并且返回
 */
public class DataSetCounter {
    /*———————————————————————————————
    | 获取每个数据集的TRANSACTION的个数 |
     ———————————————————————————————*/
    public Integer[] getCountOfDataSets() {
        Integer[] result = new Integer[2];
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(FilePathEnum.DATA_SET_I.getSource())));
            Integer countI = 0;
            while (br.readLine() != null) {
                countI++;
            }

            result[0] = countI;
        } catch (IOException e) {
            throw new RuntimeException("读取文件出错了:" + FilePathEnum.DATA_SET_II.getSource());
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                throw new RuntimeException("关闭流出错了");
            }
        }
        try {
            Integer countII = 0;
            br = new BufferedReader(new InputStreamReader(new FileInputStream(FilePathEnum.DATA_SET_II.getSource())));
            while (br.readLine() != null) {
                countII++;
            }
            result[1] = countII;
        } catch (IOException e) {
            throw new RuntimeException("读取文件出错了:" + FilePathEnum.DATA_SET_II.getSource());
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                throw new RuntimeException("关闭流出错了");
            }
        }
        return result;
    }
}
