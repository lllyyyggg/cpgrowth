package com.lanyage.datamining.run;

import com.lanyage.datamining.entity.NodeCounter;
import com.lanyage.datamining.enums.FilePathEnum;

import java.util.*;

public class Test {

    //public static void main(String[] args) {
    //
    //    NodeCounter nodeCounter = new NodeCounter(FilePathEnum.ITEM_COUNT_FILE.getSource());
    //    Map<Object, Integer> nodeCountMap = nodeCounter.getNodeCountMap();
    //    List<Map.Entry<Object, Integer>> nodeCountList = new ArrayList<>(nodeCountMap.entrySet());
    //
    //    Collections.sort(nodeCountList, (o1, o2) -> {
    //        if (!o1.getValue().equals(o2.getValue())) {
    //            return o2.getValue().compareTo(o1.getValue());
    //        } else {
    //            return ((Comparable) o1.getKey()).compareTo(o2.getKey());
    //        }
    //    });
    //
    //    for (int i = 0; i < nodeCountList.size(); i++) {
    //        System.out.println(i + " " + nodeCountList.get(i).getKey() + " " + nodeCountList.get(i).getValue());
    //    }
    //}

    //public static void main(String[] args) {
    //    int[] a = new int[]{8, 7, 6, 5, 4, 3, 2};
    //    a = mergeSort(a, 0, a.length - 1);
    //    System.out.println(Arrays.toString(a));
    //}
    //
    //private static int[] mergeSort(int[] a, int head, int tail) {
    //    if (head == tail) {
    //        //达到递归基
    //        int[] result = new int[]{a[head]};
    //        return result;
    //    }
    //    int mid = (head + tail) / 2;
    //    int[] left = mergeSort(a, head, mid);
    //    int[] right = mergeSort(a, mid + 1, tail);
    //    return merge(left, right);
    //}
    //
    //private static int[] merge(int[] left, int[] right) {
    //    int[] temp = new int[left.length + right.length];
    //    int i = left.length - 1, j = right.length - 1;
    //    int k = temp.length - 1;
    //    while (i >= 0 && j >= 0) {
    //        if (left[i] > right[j]) {
    //            temp[k--] = left[i--];
    //        } else {
    //            temp[k--] = right[j--];
    //        }
    //    }
    //    while (i >= 0) {
    //        temp[k--] = left[i--];
    //    }
    //    while (j >= 0) {
    //        temp[k--] = right[j--];
    //    }
    //    return temp;
    //}
}
