package com.lanyage.datamining.utils;


import java.io.*;
import java.util.*;

public class Test {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("resources/ITEMSCOUNT_FILE")));
        String s;
        List<Map.Entry<String, Integer>> list = new ArrayList<>();
        Map<String, Integer> map = new HashMap<>();
        while ((s = br.readLine()) != null) {
            String[] ss = s.split(" ");
            map.put(ss[0], Integer.parseInt(ss[1]));
        }

        list.addAll(map.entrySet());
        Collections.sort(list, (o1, o2) -> o2.getValue() - o1.getValue());

        for (Map.Entry entry : list) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
        br.close();
    }
}