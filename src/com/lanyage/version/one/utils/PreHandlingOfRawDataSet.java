package com.lanyage.version.one.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

/**
 * 对数据集进行预处理。
 */
public class PreHandlingOfRawDataSet {

    private static final Logger logger = LoggerFactory.getLogger(Test.class);

    private static final List<Map<Character, String>> mapList = new ArrayList<>();

    public static void main(String[] args) throws IOException {

        setUp();
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("resources/agaricus-lepiota.data")));

        String DATASET_I = "resources/DATASET_I";
        String DATASET_II = "resources/DATASET_II";
        File f1 = new File(DATASET_I);
        File f2 = new File(DATASET_II);
        if(f1.exists()) f1.delete();
        if(f2.exists()) f2.delete();
        BufferedWriter bw1 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f1)));
        BufferedWriter bw2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f2)));

        String s;
        while ((s = br.readLine()) != null) {
            String[] result = convertString(s);
            if(result[1].equals("e")) {
                bw1.write(result[0]);
                bw1.newLine();
            }else {
                bw2.write(result[0]);
                bw2.newLine();
            }
        }
        bw1.flush();
        bw2.flush();
        br.close();
        bw1.close();
        bw2.close();
    }

    /*------------------
    | 建立字母和ITEM的映射|
     ------------------*/
    private static void setUp() {
        Map<Character, String> map0 = new HashMap<>();
        map0.put('e', "1");
        map0.put('p', "2");
        mapList.add(map0);

        Map<Character, String> map1 = new HashMap<>();
        map1.put('b', "A1");
        map1.put('c', "A2");
        map1.put('x', "A3");
        map1.put('f', "A4");
        map1.put('k', "A5");
        map1.put('s', "A6");
        mapList.add(map1);
        Map<Character, String> map2 = new HashMap<>();
        map2.put('f', "B1");
        map2.put('g', "B2");
        map2.put('y', "B3");
        map2.put('s', "B4");
        mapList.add(map2);
        Map<Character, String> map3 = new HashMap<>();
        map3.put('n', "C1");
        map3.put('b', "C2");
        map3.put('c', "C3");
        map3.put('g', "C4");
        map3.put('r', "C5");
        map3.put('p', "C6");
        map3.put('u', "C7");
        map3.put('e', "C8");
        map3.put('w', "C9");
        map3.put('y', "C0");
        mapList.add(map3);
        Map<Character, String> map4 = new HashMap<>();
        map4.put('t', "D1");
        map4.put('f', "D2");
        mapList.add(map4);
        Map<Character, String> map5 = new HashMap<>();
        map5.put('a', "E1");
        map5.put('l', "E2");
        map5.put('c', "E3");
        map5.put('y', "E4");
        map5.put('f', "E5");
        map5.put('m', "E6");
        map5.put('n', "E7");
        map5.put('p', "E8");
        map5.put('s', "E9");
        mapList.add(map5);

        Map<Character, String> map6 = new HashMap<>();
        map6.put('a', "F1");
        map6.put('d', "F2");
        map6.put('f', "F3");
        map6.put('n', "F4");
        mapList.add(map6);


        Map<Character, String> map7 = new HashMap<>();
        map7.put('c', "G1");
        map7.put('w', "G2");
        map7.put('d', "G3");
        mapList.add(map7);

        Map<Character, String> map8 = new HashMap<>();
        map8.put('b', "H1");
        map8.put('n', "H2");
        mapList.add(map8);

        Map<Character, String> map9 = new HashMap<>();
        map9.put('k', "I1");
        map9.put('n', "I2");
        map9.put('b', "I3");
        map9.put('h', "I4");
        map9.put('g', "I5");
        map9.put('r', "I6");
        map9.put('o', "I7");
        map9.put('p', "I8");
        map9.put('u', "I9");
        map9.put('e', "I0");
        map9.put('w', "IA");
        map9.put('y', "IB");
        mapList.add(map9);

        Map<Character, String> map10 = new HashMap<>();
        map10.put('e', "J1");
        map10.put('t', "J2");
        mapList.add(map10);

        Map<Character, String> map11 = new HashMap<>();
        map11.put('b', "K1");
        map11.put('c', "K2");
        map11.put('u', "K3");
        map11.put('e', "K4");
        map11.put('z', "K5");
        map11.put('r', "K6");
        map11.put('?', "K7");
        mapList.add(map11);

        Map<Character, String> map12 = new HashMap<>();
        map12.put('f', "L1");
        map12.put('y', "L2");
        map12.put('k', "L3");
        map12.put('s', "L4");
        mapList.add(map12);

        Map<Character, String> map13 = new HashMap<>();
        map13.put('f', "M1");
        map13.put('y', "M2");
        map13.put('k', "M3");
        map13.put('s', "M4");
        mapList.add(map13);

        Map<Character, String> map14 = new HashMap<>();
        map14.put('n', "N1");
        map14.put('b', "N2");
        map14.put('c', "N3");
        map14.put('g', "N4");
        map14.put('o', "N5");
        map14.put('p', "N6");
        map14.put('e', "N7");
        map14.put('w', "N8");
        map14.put('y', "N9");
        mapList.add(map14);

        Map<Character, String> map15 = new HashMap<>();
        map15.put('n', "O1");
        map15.put('b', "O2");
        map15.put('c', "O3");
        map15.put('g', "O4");
        map15.put('o', "O5");
        map15.put('p', "O6");
        map15.put('e', "O7");
        map15.put('w', "O8");
        map15.put('y', "O9");
        mapList.add(map15);

        Map<Character, String> map16 = new HashMap<>();
        map16.put('p', "P1");
        map16.put('u', "P2");
        mapList.add(map16);

        Map<Character, String> map17 = new HashMap<>();
        map17.put('n', "Q1");
        map17.put('o', "Q2");
        map17.put('w', "Q3");
        map17.put('y', "Q4");
        mapList.add(map17);

        Map<Character, String> map18 = new HashMap<>();
        map18.put('n', "R1");
        map18.put('o', "R2");
        map18.put('t', "R3");
        mapList.add(map18);

        Map<Character, String> map19 = new HashMap<>();
        map19.put('c', "S1");
        map19.put('e', "S2");
        map19.put('f', "S3");
        map19.put('l', "S4");
        map19.put('n', "S5");
        map19.put('p', "S6");
        map19.put('s', "S7");
        map19.put('z', "S8");
        mapList.add(map19);

        Map<Character, String> map20 = new HashMap<>();
        map20.put('k', "T1");
        map20.put('n', "T2");
        map20.put('b', "T3");
        map20.put('h', "T4");
        map20.put('r', "T5");
        map20.put('o', "T6");
        map20.put('u', "T7");
        map20.put('w', "T8");
        map20.put('y', "T9");
        mapList.add(map20);

        Map<Character, String> map21 = new HashMap<>();
        map21.put('a', "U1");
        map21.put('c', "U2");
        map21.put('n', "U3");
        map21.put('s', "U4");
        map21.put('v', "U5");
        map21.put('y', "U6");
        mapList.add(map21);

        Map<Character, String> map22 = new HashMap<>();
        map22.put('g', "V1");
        map22.put('l', "V2");
        map22.put('m', "V3");
        map22.put('p', "V4");
        map22.put('u', "V5");
        map22.put('w', "V6");
        map22.put('d', "V7");
        mapList.add(map22);


        for (int i = 0; i < mapList.size(); i++) {
            logger.info("get map {}", mapList.get(i));
        }
    }


    /*--------------------------------
    |将pwaw字符串转化为A1 A2 A3, 2字符串|
     --------------------------------*/
    private static String[] convertString(String s) {
        s = s.replaceAll(",", "");
        char[] cs = s.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < cs.length; i++) {
            sb.append(mapList.get(i).get(cs[i])).append(" ");
        }
        sb.deleteCharAt(sb.length() - 1);
        String[] result = new String[2];
        result[0] = sb.toString().trim();
        result[1] = String.valueOf(cs[0]);
        return result;
    }
}
