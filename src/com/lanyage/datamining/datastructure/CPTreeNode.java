package com.lanyage.datamining.datastructure;

import java.util.ArrayList;
import java.util.List;

public class CPTreeNode<T> {
    private T value;
    private Integer c1;                                                                                                 //第一个类的count
    private Integer c2;                                                                                                 //第二个类的count
    private CPTreeNode<T> parent;                                                                                       //父节点
    private List<CPTreeNode<T>> children = new ArrayList<>(0);                                             //孩子节点集合
    private CPTreeNode<T> sibling;                                                                                      //右边的兄弟节点
    private Integer preIndex;
    private Integer postIndex;
    private Boolean isVisited;
    //private Integer level;

    public CPTreeNode() {
        this.isVisited = false;
    }

    public CPTreeNode(T value, Integer _1c, Integer _2c) {
        this.value = value;
        this.c1 = _1c;
        this.c2 = _2c;
        this.isVisited = false;
    }

    //public Integer level() {
    //    return level;
    //}
    //
    //public void setLevel(Integer level) {
    //    this.level = level;
    //}

    public T value() {
        return value;
    }

    public Integer c1() {
        return c1;
    }

    public void setC1(Integer c1) {
        this.c1 = c1;
    }

    public Integer c2() {
        return c2;
    }

    public void setC2(Integer c2) {
        this.c2 = c2;
    }

    public CPTreeNode<T> parent() {
        return parent;
    }

    public List<CPTreeNode<T>> children() {
        return this.children;
    }

    public CPTreeNode<T> sibling() {
        return sibling;
    }


    public void setValue(T value) {
        this.value = value;
    }


    public void setParent(CPTreeNode<T> parent) {
        this.parent = parent;
    }

    public void setSibling(CPTreeNode<T> sibling) {
        this.sibling = sibling;
    }

    public double supportOfD1(Integer N1) {
        return (double) this.c1 / N1;
    }

    public double supportOfD2(Integer N2) {
        return (double) this.c2 / N2;
    }

    public static <T> CPTreeNode<T> getInstance() {
        return new CPTreeNode<>();
    }

    public Integer preIndex() {
        return preIndex;
    }

    public void setPreIndex(Integer preIndex) {
        this.preIndex = preIndex;
    }

    public Integer postIndex() {
        return postIndex;
    }

    public void setPostIndex(Integer postIndex) {
        this.postIndex = postIndex;
    }

    public Boolean isVisited() {
        return isVisited;
    }

    public void setVisited(Boolean visited) {
        isVisited = visited;
    }

    @Override
    public String toString() {
        return "(" +
                value + "," +
                preIndex + "," +
                postIndex + "," +
                c1 + "," +
                c2 +
                ')';
    }
}
