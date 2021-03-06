package com.lanyage.version.two.datastructure;



import com.lanyage.version.two.utils.SequenceSplitUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
public class Transaction implements Comparable<Transaction> {
    public int size() {
        return itemList.size();
    }
    public Item get(int index) {
        return itemList.get(index);
    }
    private List<Item> itemList;
    public Transaction() {
        itemList = new ArrayList<>(3);
    }
    public Transaction(List<Item> itemList) {
        this.itemList = itemList;
    }
    public void addItem(String value) {
        addItem(value, 0);
    }
    public void addItem(String value, Integer count) {
        itemList.add(ItemFactory.create(value, count));
    }
    public void sort() {
        Collections.sort(itemList);
    }
    public List<Item> getItemList() {
        return itemList;
    }
    public int length() {
        return itemList.size();
    }
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (Item item : itemList) {
            s.append(item.value).append(" ");
        }
        s.deleteCharAt(s.length() - 1);
        return s.toString();
    }
    @Override
    public int compareTo(Transaction o) {
        List<Item> self = this.itemList;
        List<Item> other = o.itemList;
        int minLen = Math.min(self.size(), other.size());
        for (int i = 0; i < minLen; i++) {
            if (self.get(i).compareTo(other.get(i)) == 0) {
                continue;
            }
            return self.get(i).compareTo(other.get(i));
        }
        return 0;
    }
    public static class Factory {
        public static Transaction create(List<Item> itemList) {
            return new Transaction(itemList);
        }
        public static Transaction create(String itemString) {
            Transaction transaction = new Transaction();
            String[] items = SequenceSplitUtil.split(itemString.trim());
            for (String item : items) {
                transaction.addItem(item);
            }
            return transaction;
        }
    }
    public static class Item implements Comparable<Item> {
        private String value;
        private Integer count;
        private Item() {
            this.count = 0;
        }
        public Item(String value) {
            this();
            this.value = value;
        }
        public Item(String value, Integer count) {
            this(value);
            this.count = count;
        }
        public String getValue() {
            return value;
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Item item = (Item) o;
            return Objects.equals(value, item.value);
        }
        @Override
        public int hashCode() {
            return Objects.hash(value);
        }
        @Override
        public String toString() {
            return "[" + this.value + "," + this.count + "]";
        }
        @Override
        public int compareTo(Item anotherItem) {
            return anotherItem.count - count == 0 ? value.compareTo(anotherItem.value) : anotherItem.count - count;
        }
    }
    public static class ItemFactory {
        public static Item create() {
            return new Item();
        }
        public static Item create(String value, Integer count) {
            Item item = create();
            item.value = value;
            item.count = count;
            return item;
        }
    }
}
