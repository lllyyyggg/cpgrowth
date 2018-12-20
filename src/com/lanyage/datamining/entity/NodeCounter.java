package com.lanyage.datamining.entity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;


/**
 * —————————————————————————
 * 获取指定文件中的ItemCount *
 * —————————————————————————
 **/
public class NodeCounter {

    private String itemCountFile;

    public NodeCounter(String itemCountFile) {
        this.itemCountFile = itemCountFile;
    }

    /*———————————————————————————
    | 获取节点的在数据集中出现的总数 |
     ———————————————————————————*/
    public Map<Object, Integer> getNodeCountMap() {
        BufferedReader br = null;
        try {
            Map<Object, Integer> NODEANDCOUNT = new HashMap<>();
            br = new BufferedReader(new InputStreamReader(new FileInputStream(this.itemCountFile)));
            String line;
            while ((line = br.readLine()) != null && !line.trim().equals("")) {
                Object[] objects = line.split(" ");
                NODEANDCOUNT.put(objects[0], Integer.valueOf((String) objects[1]));
            }
            return NODEANDCOUNT;
        }catch (Exception e){
            throw new RuntimeException("IO异常 in NodeCounter.class");
        }finally {
            if(br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    throw new RuntimeException("关闭流 in NodeCounter.class");
                }
            }
        }
    }
}
