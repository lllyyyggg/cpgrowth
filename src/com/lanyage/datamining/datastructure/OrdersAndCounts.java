package com.lanyage.datamining.datastructure;

/**
 * ————————————————————————————————————————————————————————————
 * Node-code类，封装了一个Item对应的所有节点计数和前后序索引
 *
 * @author lanyage
 * @see com.lanyage.datamining.entity.CPNodeListCreator
 * ————————————————————————————————————————————————————————————
 **/
public class OrdersAndCounts {
    private Integer preIndex;
    private Integer postIndex;
    private Integer c1;
    private Integer c2;
    private CPTreeNode<Object> startNode;
    private CPTreeNode<Object> endNode;

    public OrdersAndCounts(Integer preIndex, Integer postIndex, Integer c1, Integer c2) {
        this.preIndex = preIndex;
        this.postIndex = postIndex;
        this.c1 = c1;
        this.c2 = c2;
    }

    public OrdersAndCounts(OrdersAndCounts cpone) {
        this.preIndex = cpone.preIndex;
        this.postIndex = cpone.postIndex;
        this.c1 = cpone.c1;
        this.c2 = cpone.c2;
        this.startNode = cpone.startNode;
        this.endNode = cpone.endNode;
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

    public CPTreeNode<Object> endNode() {
        return endNode;
    }

    public void setEndNode(CPTreeNode<Object> endNode) {
        this.endNode = endNode;
    }

    public CPTreeNode<Object> startNode() {
        return startNode;
    }

    public void setStartNode(CPTreeNode<Object> startNode) {
        this.startNode = startNode;
    }

    @Override
    public String toString() {
        return "(" +
                preIndex + ","
                + postIndex + ","
                + c1 + ","
                + c2 +
                ")";
    }
}
