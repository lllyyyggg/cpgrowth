package com.lanyage.datamining.datastructure;


import java.util.*;

public class ItemSet<T> implements Comparable<ItemSet<T>> {
    private List<Item<T>> items;
    //private Integer classTag;

    public List<Item<T>> items() {
        return items;
    }

    //public Integer classTag() {
    //    return classTag;
    //}
    //
    //public void setClassTag(Integer classTag) {
    //    this.classTag = classTag;
    //}

    public void setItems(List<Item<T>> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return items.toString();
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
