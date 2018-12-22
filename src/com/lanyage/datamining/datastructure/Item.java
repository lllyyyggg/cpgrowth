package com.lanyage.datamining.datastructure;

import java.util.*;

public class Item<T> implements Comparable<Item> {
    private T value;  //Item的值
    private Integer count;  //Item的出现次数

    public Item() {
    }

    public Item(T value) {
        this.value = value;
    }

    public Item(T value, Integer count) {
        this.value = value;
        this.count = count;
    }

    public T value() {
        return this.value;
    }

    public Item setValue(T value) {
        this.value = value;
        return this;
    }

    public Integer count() {
        return this.count;
    }

    public Item setCount(Integer count) {
        this.count = count;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item<?> item = (Item<?>) o;
        return Objects.equals(value, item.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "<" + this.value + "," + this.count + ">";
    }

    @Override
    public int compareTo(Item o) {
        if (!o.count.equals(this.count)) {
            return o.count.compareTo(this.count);
        } else {
            return ((Comparable) this.value).compareTo(o.value);
        }
    }

    //public static void main(String[] args) {
    //    List<Item<Object>> nodes = new ArrayList<>();
    //    Item<Object> node1 = new Item<>();
    //    Item<Object> node2 = new Item<>();
    //
    //    node1.setValue("E9");
    //    node1.setCount(222);
    //
    //    node2.setValue("E4");
    //    node2.setCount(222);
    //
    //    nodes.add(node1);
    //    nodes.add(node2);
    //
    //
    //    Collections.sort(nodes);
    //    System.out.println(nodes);
    //}
}
