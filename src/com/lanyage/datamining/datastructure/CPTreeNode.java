package com.lanyage.datamining.datastructure;

import java.util.ArrayList;
import java.util.List;

public class CPTreeNode<T> {
    private T value;
    private Integer _1c; //第一个类的count
    private Integer _2c; //第二个类的count
    private Integer index;  //索引

    private CPTreeNode<T> parent;   //父节点
    private List<CPTreeNode<T>> children = new ArrayList<>(0);    //孩子节点集合
    private CPTreeNode<T> sibling;  //右边的兄弟节点

    public T value() {
        return value;
    }

    public Integer _1c() {
        return _1c;
    }

    public Integer _2c() {
        return _2c;
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

    public Integer index() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public void set_1c(Integer _1c) {
        this._1c = _1c;
    }

    public void set_2c(Integer _2c) {
        this._2c = _2c;
    }

    public void setParent(CPTreeNode<T> parent) {
        this.parent = parent;
    }

    public void setSibling(CPTreeNode<T> sibling) {
        this.sibling = sibling;
    }

    public double supportOfD1(Integer N1) {
        return (double)this._1c / N1;
    }

    public double supportOfD2(Integer N2) {
        return (double)this._2c / N2;
    }

    public static <T> CPTreeNode<T> getInstance() {
        return new CPTreeNode<>();
    }

    @Override
    public String toString() {
        return "<" + index + ":" +
                value + "," +
                _1c + "," +
                _2c +
                '>';
    }
}
