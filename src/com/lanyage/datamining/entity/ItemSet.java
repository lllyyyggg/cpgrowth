package com.lanyage.datamining.entity;


import java.util.*;

public class ItemSet <T>{
    private List<Item<T>> items;
    private Integer classTag;
    public List<Item<T>> items() {
        return items;
    }

    public Integer classTag() {
        return classTag;
    }

    public void setClassTag(Integer classTag) {
        this.classTag = classTag;
    }

    public void setItems(List<Item<T>> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return items.toString();
    }

    public static void main(String[] args) {
        Item<String> item = new Item<>("c");
        Item<String> item2 = new Item<>("b");

        List<Item<String>> list = new ArrayList<>();
        list.add(item);
        list.add(item2);

        ItemSet<String> itemSet = new ItemSet<>();
        itemSet.setItems(list);

        System.out.println(itemSet);
    }
}
