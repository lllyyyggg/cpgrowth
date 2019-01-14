package com.lanyage.datamining.datastructure;

import java.util.ArrayList;
import java.util.List;

public class CPTreeNode<T> {
    private T value;
    private Integer c1;                                                                                                 //第一个类的count
    private Integer c2;                                                                                                 //第二个类的count
    private Integer preIndex;                                                                                           //前序遍历索引
    private Integer postIndex;                                                                                          //后序遍历索引
    private Boolean isVisited;                                                                                          //是否已经遍历
    private CPTreeNode<T> parent;                                                                                       //父节点
    private CPTreeNode<T> sibling;                                                                                      //右边的兄弟节点
    private List<CPTreeNode<T>> children = new ArrayList<>(3);                                             //孩子节点集合

    public CPTreeNode() {
        this.isVisited = false;
    }

    public CPTreeNode(T value, Integer _1c, Integer _2c) {
        this.value = value;
        this.c1 = _1c;
        this.c2 = _2c;
        this.isVisited = false;
    }

    public T value() {
        return value;
    }

    public Integer c1() {
        return c1;
    }

    public CPTreeNode c1(Integer c1) {
        this.c1 = c1;
        return this;
    }

    public Integer c2() {
        return c2;
    }

    public CPTreeNode<T> c2(Integer c2) {
        this.c2 = c2;
        return this;
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

    public CPTreeNode<T> value(T value) {
        this.value = value;
        return this;
    }

    public CPTreeNode<T> parent(CPTreeNode<T> parent) {
        this.parent = parent;
        return this;
    }

    public CPTreeNode<T> sibling(CPTreeNode<T> sibling) {
        this.sibling = sibling;
        return this;
    }

    public static <T> CPTreeNode<T> getInstance() {
        return new CPTreeNode<>();
    }

    public Integer preIndex() {
        return preIndex;
    }

    public CPTreeNode<T> preIndex(Integer preIndex) {
        this.preIndex = preIndex;
        return this;
    }

    public Integer postIndex() {
        return postIndex;
    }

    public CPTreeNode<T> postIndex(Integer postIndex) {
        this.postIndex = postIndex;
        return this;
    }

    public Boolean isVisited() {
        return isVisited;
    }

    public CPTreeNode<T> visited(Boolean visited) {
        isVisited = visited;
        return this;
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
