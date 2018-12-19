package com.lanyage.datamining.entity;


import java.util.*;

public class ItemSet<T> implements Comparable<ItemSet<T>> {
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
        Item<Object> i = new Item<>();
        i.setValue("F");
        i.setCount(6);
        Item<Object> i2 = new Item<>();
        i2.setValue("C");
        i2.setCount(4);
        Item<Object> i3 = new Item<>();
        i3.setValue("A");
        i3.setCount(2);
        Item<Object> i4 = new Item<>();
        i4.setValue("B");
        i4.setCount(2);
        Item<Object> i5 = new Item<>();
        i5.setValue("E");
        i5.setCount(3);

        ItemSet<Object> itemSet1 = new ItemSet<>();
        List<Item<Object>> list1 = new ArrayList<>();
        list1.add(i);
        list1.add(i2);
        list1.add(i3);
        itemSet1.setItems(list1);


        ItemSet<Object> itemSet2 = new ItemSet<>();
        List<Item<Object>> list2 = new ArrayList<>();
        list2.add(i);
        list2.add(i2);
        list2.add(i4);
        list2.add(i5);
        itemSet2.setItems(list2);

        List<ItemSet<Object>> itemSets = new ArrayList<>();
        itemSets.add(itemSet2);
        itemSets.add(itemSet1);

        System.out.println(itemSets);       //[[<F,6>, <C,3>, <A,1>], [<F,6>, <C,3>, <B,1>, <E,1>]]

        Collections.sort(itemSets);
        System.out.println(itemSets);
    }

    @Override
    public int compareTo(ItemSet<T> o) {
        List<Item<T>> self = this.items();
        List<Item<T>> other = o.items();
        int minLen = Math.min(self.size(), other.size());
        for(int i = 0; i < minLen; i++) {
            if(self.get(i).compareTo(other.get(i)) == 0) {
                continue;
            }
            return self.get(i).compareTo(other.get(i));
        }
        return 0;
    }
}
