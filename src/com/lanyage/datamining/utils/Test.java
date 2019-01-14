package com.lanyage.datamining.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Test {

    public static void main(String[] args) throws IOException {
        List<Integer> list = new ArrayList<>();
        list.add(20);
        System.out.println(list.stream().map(x -> x * 2).collect(Collectors.toList()));
    }
}