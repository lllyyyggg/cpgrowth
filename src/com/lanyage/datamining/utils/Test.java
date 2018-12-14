package com.lanyage.datamining.utils;


import java.io.*;

public class Test {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("resources/ITEMSCOUNT_FILE")));
        int sum = 0;
        String s;
        while ((s = br.readLine()) != null) {
            String[] ss = s.split(" ");
            sum += Integer.parseInt(ss[1]);
        }
        System.out.println(sum);
        br.close();
    }
}